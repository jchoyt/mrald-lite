/*
 *  Copyright 2008 The MITRE Corporation (http://www.mitre.org/). All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mitre.mrald.output;

import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.mitre.mrald.control.MsgObject;
import org.mitre.mrald.control.WorkflowStepException;
import org.mitre.mrald.join.Join;
import org.mitre.mrald.join.PostgresSource;
import org.mitre.mrald.join.SimpleOptimizer;
import org.mitre.mrald.join.SortMergeJoin;
import org.mitre.mrald.join.Source;
import org.mitre.mrald.query.PivotFilter;
import org.mitre.mrald.util.Config;
import org.mitre.mrald.util.DBMetaData;
import org.mitre.mrald.util.MetaData;
import org.mitre.mrald.util.MraldConnection;
import org.mitre.mrald.util.MraldError;
import org.mitre.mrald.util.MraldOutFile;

import Zql.ParseException;
import Zql.ZQuery;
import Zql.ZqlJJParser;

/**
 *  The OutputManager
 *  The MultiDbOutputManager class controls the output on the information for multiple databases.
 *  It processes all of the user's preferences based on their requests and formats the output
 *  accordingly.
 *
 *@author     Gail Hamilton
 *@created    Spetember 5th, 2007
 *@version    1.0
 */

public abstract class MultiDbOutputManager extends OutputManager
{

	 protected String[] datasources;
	 protected HashMap<String,String> groupedQueries;
	 protected ArrayList<Statement> statements;
	 protected ArrayList<Connection> crossDbConns;
	 HashMap<String,Statement> querySet = new HashMap<String, Statement>();

		 /**
     *  Constructor for the MultiDbOutputManager object
     *
     *@since    1.2
     */
    public MultiDbOutputManager()
    {
        super();
    }


    /**
     *  This is method that is called from the Workflow. For this step, the
     *  limits are set and the database connection is established. The passed
     *  MsgObject is searched for special DB connection information (looking for
     *  a Datasource name with the value representing the name of the properties
     *  file that houses the connection information). If none is found, the
     *  default is used.
     *
     *@param  msg                        Description of Parameter
     *@exception  WorkflowStepException  Description of Exception
     *@since                             1.2
     */
    public void execute( MsgObject msg )
        throws WorkflowStepException
    {
        this.msg = msg;
        userID = msg.getUserId();

        crossDbConns = new ArrayList<Connection>();
        statements = new ArrayList<Statement>();
        /*
         *  figure out whether or not to show the query
         */
        if( msg.getValue("showQuery")[0].equals("true") )
        {
            printQuery = true;
        }
        String pivot = msg.getValue("pivot")[0];
        if (pivot.length() > 0)
        {
        	doPivot = true;
        	pivotPieces = PivotFilter.parse(pivot);
        }
        /*
         *  Set the output sizes
         */
        try
        {
            String outStr = ( msg.getValue( "outputSize" )[0] );
            if ( outStr.equals( "lines" ) )
            {
                outStr = ( msg.getValue( "outputLinesCount" )[0] );
                outStr = outStr == null ? "-1" : outStr;
                setLineLimitSize( new Integer( outStr ).intValue() );
            }
            else if ( outStr.equals( "mb" ) )
            {
                outStr = ( msg.getValue( "outputMBSize" )[0] );
                outStr = outStr == null ? "-1" : outStr;
                setMbLimitSize( new Float( outStr ).floatValue() );
            }
        }
        catch ( NumberFormatException nfe )
        {
            MraldOutFile.logToFile( nfe );
        }
        try
        {

            dbQueries = msg.getQuery();

            if ( dbQueries[0].equals( Config.EMPTY_STR ) )
            {
                throw new WorkflowStepException( "Error in Output Manager: There was no SQL query." );
            }
            /*
             *  get the Connection to the database
             */
            datasources = msg.getValue( "Datasource" );

            if( datasources[0].equals("") )
            {
                datasources[0] = "main";
            }
            //MultiDB GH A bit of a hack. Just to get the code working for various things
            //like rowcount etc,..
            dbQuery = dbQueries[0];

            //If there is more than one datasource then group the queries together
            //according to associated datasource
            if (datasources.length > 1)
            {
            	groupedQueries = groupQueries(datasources,dbQueries );
            }
            else //Group the queries into the expected data structure
            	//Only allowed one query per datasource
            {
            	groupedQueries = new HashMap<String, String>();
            	groupedQueries.put(datasources[0], dbQueries[0]);
            }


            for (String ds: groupedQueries.keySet())
            {

	            Connection dbConn = new MraldConnection( ds, msg ).getConnection();
	            String query = groupedQueries.get(ds);
	            Statement statement = dbConn.createStatement();
	            querySet.put(query, statement);

	            //ArrayList to keep a list of statements to be used to close them off
	            crossDbConns.add(dbConn);
	            //ArrayList to keep a list of statements to be used to close them off
		        statements.add(statement);

            }
            //Assume that there can only be one crossDb Query at a time

            prepareHeaders( );
            runUserQuery( );

	        formatOutput( );

            out.close(); //GH removed from the format output - as formatOutput may run multiple times

            //To be used to close off the multiple statements
            closeStatements();

            //To close off the multiple connections
            freeConnection();

        }
        catch ( Exception e )
        {
            MraldOutFile.logToFile( e );
            try
            {
                conn.close();
            }
            catch ( Exception se )
            {
                //don't do anything - just cleaning up
                MraldOutFile.logToFile( e );
            }
            throw new MraldError( e, msg );
        }
  	}

    /**
     *  This method takes the datasources and the queries and matches them together
     * There can only be one query for each datasource, as the otherwise, will not know
     * which query is to be matched as a crossDb and which isn't
     *
     *@param   String[] datasources :List of datasources to e accessed to create the crossDb query
     *		   String[] queries: List of queries -one for each datasource
     *@since       1.2
     */
    private HashMap<String, String> groupQueries(String[] datasources, String[] queries)
    {
    	HashMap<String, String> groupedQueries = new HashMap<String, String>();
    	for (String ds: datasources)
    	{
    		DBMetaData md = MetaData.getDbMetaData( ds );
    		Properties props = md.getDbProps();
    		String schema = props.getProperty("SCHEMA");
            String sidName = props.getProperty("DBNAME");

            String sidNameSchema = sidName + "." + schema;

            for (String query: queries)
            {
            	//Check to see if the database name and the schema name from the property for datasource
            	//is contained in the query. If it is then this query matches this datasource
            	if (query.indexOf(sidNameSchema ) > 0)
            	{

            		groupedQueries.put(ds, query);
            	}
            }
    	}
    	return groupedQueries;
    }

    /**
     *  This is method connects to the database and runs the specified query
     *
     *@param  msg  Description of Parameter
     *@since       1.2
     */
    public void runUserQuery()
    {
    	MraldOutFile.logToFile(Config.getProperty("LOGFILE"), "MultiDbOutputManager: runUserQuery : Start");
            try {
				runCrossDbQuery();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

    }

    private void runCrossDbQuery() throws ParseException, SQLException
    {

    	// This is a HashMap containing one statement and one query for each datasource
		HashMap<String,Statement> qry2stmt = querySet;

		// Extract an array of cross-db links.
		String[] links = extractLinks();
		MraldOutFile.logToFile(Config.getProperty("LOGFILE"), "MultiDbOutputManager:runCrossDbQuery no of links " +  links.length   );


		// Extract a set of database names from the cross-db links.
		HashSet<String> dbnames = extractDBnames(links);


		// Associate database names with sql commands.
		HashMap<String,String> dbname2qry = bindDBnames2queries(qry2stmt, dbnames);

		for (String bindName: dbname2qry.keySet() )
			MraldOutFile.logToFile(Config.getProperty("LOGFILE"), "MultiDbOutputManager:runCrossDbQuery datasource " + bindName + " sql " + dbname2qry.get(bindName) );


		// The code currently only works on pairs of databases.
		assert(dbname2qry.size() <= 2);


		// Extract the outer and inner database information.
		CrossDBInfo info = extractQueryInfo(qry2stmt, dbnames, dbname2qry, links.length);


		// Use the extracted information and links to populate the join columns.
		populateJoinColumns(links, info);


		// TODO: Select a join algorithm intelligently.
		// Populate the result set for the output manager.
		//Join join  = new SortMergeJoin(info.outerSrc, info.innerSrc, info.outerCols, info.innerCols);
//		Join join  = new IndexJoin(info.outerSrc, info.innerSrc, info.outerCols, info.innerCols);
		Join join = SimpleOptimizer.chooseJoinAlgorithm(info.outerSrc, info.innerSrc, info.outerCols, info.innerCols, true);

		rs = join.execute();

    }


	/**
	 * @param qry2stmt
	 * @param dbnames
	 * @param dbname2qry
	 * @param info
	 * @throws ParseException
	 */
	private static CrossDBInfo extractQueryInfo(HashMap<String,Statement> qry2stmt, HashSet<String> dbnames, HashMap<String,String> dbname2qry, int numCols) throws ParseException {
		CrossDBInfo result = new CrossDBInfo(numCols);
		MraldOutFile.logToFile(Config.getProperty("LOGFILE"), "MultiDbOutputManager:extractQueryInfo no of dbnames " +  dbnames.size()   );

		for (String dbname : dbnames) {
			String sql = dbname2qry.get(dbname);

			MraldOutFile.logToFile(Config.getProperty("LOGFILE"), "MultiDbOutputManager:extractQueryInfo dbName is " +  dbname  + " query is :"+ sql );

			Statement stmt = qry2stmt.get(sql);
			sql = sql.replaceAll(dbname + "\\.", "");
			if (!sql.contains(";")) sql += ";";
			// TODO: Choose a source based on metadata.
			Source src = new PostgresSource(stmt, sql);
			ZQuery query = new ZqlJJParser(new StringReader(sql)).QueryStatement();
			Vector selectCols = query.getSelect();
			if (result.outerSrc == null) {
				result.outerSrc = src;
				result.outerName = dbname;
				result.outerSelect = selectCols;
			} else {
				result.innerSrc = src;
				result.innerName = dbname;
				result.innerSelect = selectCols;
			}
		}
		return result;
	}


	/**
	 * @param qry2stmt
	 * @param dbnames
	 * @return
	 */
	private static HashMap<String,String> bindDBnames2queries(HashMap<String,Statement> qry2stmt, HashSet<String> dbnames) {
		HashMap<String,String> dbname2qry = new HashMap<String,String>();
		for (String dbname : dbnames) {
			for (String sql : qry2stmt.keySet()) {
				if (sql.contains(dbname + ".")) {
					if (dbname2qry.containsKey(dbname)) {
						throw new IllegalStateException("Ambiguous name: " + dbname);
					}
					dbname2qry.put(dbname, sql);
				}
			}
		}
		return dbname2qry;
	}


	/**
	 * @param links
	 */
	private static HashSet<String> extractDBnames(String[] links) {
		HashSet<String> dbnames = new HashSet<String>();
		for (int i = 0; i < links.length; i++) {
			String[] linkelements = links[i].split("~");
			for (int j = 0; j < linkelements.length; j++) {
				String[] split = linkelements[j].split(":");
				if (split[0].endsWith("Link")) {
					dbnames.add(split[1].split("\\.")[0]);
				}
			}
		}
		return dbnames;
	}


	/**
	 * @return
	 */
	private String[] extractLinks() {
		MraldOutFile.logToFile(Config.getProperty("LOGFILE"), "MultiDbOutputManager: extractLInks : Start");

		String[] links;
		ArrayList<String> tmpLinks = new ArrayList<String>();
		int link_num = 1;
		while (true) {
			String link = msg.getValue("DbCrossLink" + link_num)[0];
			if (link == Config.EMPTY_STR) break;
			tmpLinks.add(link);
			link_num++;
		}
		links = new String[tmpLinks.size()];
		links = tmpLinks.toArray(links);
		return links;
	}

    /**
     * This method closes the multiple open statements that are required for the crossDbJoin
     * @throws SQLException
     *
     */
    private void closeStatements() throws SQLException
    {
    	if (statements == null ) return;
    	for (Statement statement: statements)
    	{
    		if (statement != null)
    		{
    			statement.close();
    		}
    		statements.remove(statement);
    	}
    }

    /**
     *  This methods attempts to free any open connections to the Database
     *
     *@exception  OutputManagerException  Passes the exception up to PQMServlet
     *      for handling.
     *@since                              1.2
     */
    public void freeConnection()
        throws OutputManagerException
    {
        try
        {
            niceNames = null;
            formats = null;
            classNames = null;
            rs = null;

            closeStatements();

            if (crossDbConns == null) return;

            for (Connection connection: crossDbConns)
        	{
            	if (connection != null)
            		connection.close();
            	crossDbConns.remove(connection);
        	}
        }
        catch ( SQLException e )
        {
            OutputManagerException thisOutputMangerException = new OutputManagerException( e.getMessage() );
            throw thisOutputMangerException;
        }
    }

    public static class CrossDBInfo {
		protected Source outerSrc = null;
		protected Source innerSrc = null;
		protected String outerName = null;
		protected String innerName = null;
		protected Vector outerSelect = null;
		protected Vector innerSelect = null;
		protected int[] outerCols;
		protected int[] innerCols;
		protected CrossDBInfo(int numCols) {
			outerCols = new int[numCols];
			innerCols = new int[numCols];
		}
    }


	/**
	 * @param links
	 * @param info
	 */
	private static void populateJoinColumns(String[] links, CrossDBInfo info) {
		for (int i = 0; i < links.length; i++) {
			String[] linkelements = links[i].split("~");
			for (int j = 0; j < linkelements.length; j++) {
				String[] split = linkelements[j].split(":");
				if (split[0].endsWith("Link")) {
					int dot = split[1].indexOf('.');
					String dbname = split[1].substring(0, dot);
					String field = split[1].substring(dot+1);
					if (dbname.equals(info.outerName)) {
						for (int k = 0; k < info.outerSelect.size(); k++) {
							if (info.outerSelect.get(k).toString().equals(field)) {
								info.outerCols[i] = k+1;
								break;
							}
						}
					}
					else if (dbname.equals(info.innerName)) {
						for (int k = 0; k < info.innerSelect.size(); k++) {
							if (info.innerSelect.get(k).toString().equals(field)) {
								info.innerCols[i] = k+1;
								break;
							}
						}
					}
				}
			}
		}
	}

    public static void main(String[] args) throws ClassNotFoundException, SQLException, ParseException, ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException {
    	// Strategy: Once outer and inner are known:
    	// Associate each with a db-name
    	// Strip out db-name references
    	// Find appropriate columns

		Class.forName("org.postgresql.Driver");

		Connection db1 = DriverManager.getConnection(
				"jdbc:postgresql://localhost:5432/dils0",
				"postgres", "postgres");
		Statement stmt1 = db1.createStatement(/*ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY*/);
		String sql1 = "select dils0.public.organism.organism_id, dils0.public.organism.species, dils0.public.organism.dob, dils0.public.organism.gender from dils0.public.organism";

		Connection db2 = DriverManager.getConnection(
				"jdbc:postgresql://localhost:5432/movies",
				"postgres", "postgres");
		Statement stmt2 = db2.createStatement(/*ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY*/);
		String sql2 = "select movies.public.ratings.userid, movies.public.ratings.movieid, movies.public.ratings.rating from movies.public.ratings where movies.public.ratings.userid < 10000 and movies.public.ratings.rating >= 4";

		HashMap<String,Statement> qry2stmt = new HashMap<String,Statement>();
		qry2stmt.put(sql1, stmt1);
		qry2stmt.put(sql2,stmt2);

		String[] links = {"PrimaryLink:dils0.public.organism.organism_id~SecondaryLink:movies.public.ratings.userid~SqlThread:-1"};
		HashSet<String> dbnames = extractDBnames(links);
		// dbnames now contains all of the database names to replace.

		HashMap<String,String> dbname2qry = bindDBnames2queries(qry2stmt, dbnames);

		assert(dbname2qry.size() <= 2);
		CrossDBInfo info = extractQueryInfo(qry2stmt, dbnames, dbname2qry, links.length);

		System.out.println(info.outerName + ":" + info.outerSrc.sql);
		System.out.println(info.innerName + ":" + info.innerSrc.sql);

		populateJoinColumns(links, info);

		Join join  = new SortMergeJoin(info.outerSrc, info.innerSrc, info.outerCols, info.innerCols);
		ResultSet rs = join.execute();
		Source.print(rs);

/*		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        File file = new File("C:\\Program Files\\Apache Software Foundation\\Tomcat 5.5\\webapps\\dils\\custom\\admin@mitre.org_1189190887148.xml");
        boolean itshere = file.exists();
        System.out.println(itshere + "==>" + file.getAbsolutePath());
        StreamResult destination = new StreamResult( file );
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        DOMSource source = new DOMSource(doc);
        transformer.transform(source, destination);
*/		System.out.println("Done");
    }

}