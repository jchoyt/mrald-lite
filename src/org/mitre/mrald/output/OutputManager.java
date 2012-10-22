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

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;
import java.util.StringTokenizer;
import javax.servlet.ServletException;
import org.mitre.mrald.control.AbstractStep;
import org.mitre.mrald.control.MsgObject;
import org.mitre.mrald.control.MsgObjectException;
import org.mitre.mrald.control.WorkflowStepException;
import org.mitre.mrald.query.PivotedResultSet;
import org.mitre.mrald.query.PivotFilter;
import org.mitre.mrald.util.Config;
import org.mitre.mrald.util.FormTags;
import org.mitre.mrald.util.MraldException;
import org.mitre.mrald.util.JdbcPropertyPublisher;
import org.mitre.mrald.util.MiscUtils;
import org.mitre.mrald.util.MraldConnection;
import org.mitre.mrald.util.MraldError;
import org.mitre.mrald.util.MraldOutFile;

/**
 *  The OutputManager class controls the output on the information. It processes
 *  all of the user's preferences based on their requests and formats the output
 *  accordingly.
 *
 *@author     Brian Blake
 *@created    January 2, 2001
 *@version    1.0
 */

public abstract class OutputManager extends AbstractStep
{

    protected float bytesReturned;
    protected String[] classNames;
    protected Connection conn;
    protected String[] dbQueries;
    protected String dbQuery = null; //the currently processed query
    protected String datasource;
    protected int fetchSize;
    protected String[] formats;
    protected int lineLimitSize;
    protected float mbLimitSize;
    protected String[] niceNames;
    protected int recordsReturned;
    protected ResultSet rs;
    protected SQLException sqlExceptionMessage;
    protected Statement stmt;
    protected PreparedStatement prepStmt;
    protected String userID;
    protected boolean printQuery = false;
    protected boolean doPivot = false;
    protected String[] pivotPieces;
    protected Thread t;
    protected PrintWriter out;
    protected MsgObject msg;
    protected boolean firstTime = true;

    /**
     *  Constructor for the OutputManager object
     *
     *@since    1.2
     */
    public OutputManager()
    {
        // INITIALIZE GLOBAL VARIABLES
        stmt = null;
        rs = null;
        conn = null;
        fetchSize = 0;
        lineLimitSize = -1;
        mbLimitSize = -1;
        recordsReturned = 0;
        bytesReturned = 0;
    }


    /**
     *  This is method is called to allow a user specified limit on the number
     *  of records returned
     *
     *@param  userSpecifiedLimitOnLines  Integer value on limit on output rows
     *@since                             1.2
     */

    public void setLineLimitSize( int userSpecifiedLimitOnLines )
    {
        lineLimitSize = userSpecifiedLimitOnLines;
    }


    /**
     *  This is method is called to allow a user specified limit on the output
     *  size.
     *
     *@param  userSpecifiedLimitOnSize  Integer value on limit on output file
     *      size
     *@since                            1.2
     */

    public void setMbLimitSize( float userSpecifiedLimitOnSize )
    {
        mbLimitSize = userSpecifiedLimitOnSize * 1000000;
    }


    /**
     *  Gets the LineLimitSize attribute of the OutputManager object
     *
     *@return    The LineLimitSize value
     *@since
     */
    public int getLineLimitSize()
    {
        return lineLimitSize;
    }


    /**
     *  Gets the MbLimitSize attribute of the OutputManager object
     *
     *@return    The MbLimitSize value
     *@since
     */
    public float getMbLimitSize()
    {
        return mbLimitSize;
    }


    /**
     *  Gets the QryResultsinRS attribute of the OutputManager object
     *
     *@return    The QryResultsinRS value
     *@since
     */
    public ResultSet getQryResultsinRS()
    {

        return rs;
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
    	MraldOutFile.logToFile(Config.getProperty("LOGFILE"), "OutputManager: execute: start" );

        this.msg = msg;
        userID = msg.getUserId();

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
             *  get additional properties to be used in getting the Connection
             */
            Properties props = new Properties();
            props.put("SetBigStringTryClob","true");  //<-- this should be removed an put into otherDb.props.  jch 7/29/08
            JdbcPropertyPublisher.populateProperties( msg.getReq(), props );
            /*
             *  get the Connection to the database
             */
            datasource = msg.getValue( "Datasource" )[0];
            if( datasource.equals("") )
            {
                datasource = "main";
            }
            conn = new MraldConnection( datasource, msg ).getConnection();
            /*
             *  do not use scrollable result set because Oracle 8i JDBC scrollable resultsets screw up date formatting when you do a getString(i) on it
             *  stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
             */
            stmt = conn.createStatement();
            prepareHeaders( );
            //Loop for each Query - default is there is one
            for ( int i = 0; i < dbQueries.length; i++ )
            {
                dbQuery = dbQueries[i];

                runUserQuery( );
                formatOutput( );
            }

            out.close(); //GH removed from the format output - as formatOutput may run multiple times
            stmt.close();
            freeConnection();

            String test = msg.getValue("CrossLink1")[0].toString();
          MraldOutFile.logToFile(Config.getProperty("LOGFILE"), "OutputManager: execute " + test);

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

            stmt = null;
            rs = null;
            conn.close();
        }
        catch ( SQLException e )
        {
            OutputManagerException thisOutputMangerException = new OutputManagerException( e.getMessage() );
            throw thisOutputMangerException;
        }
    }


    protected void printQuery( )
    {
        out.println( "Query: " + dbQuery );
    }


    /**
     *  This method returns the Results to the User
     *
     *@return    Description of the Returned Value
     *@since
     */
    public ResultSet returnResultsToUser()
    {
        return rs;
    }


    /**
     *  This is method connects to the database and runs the specified query
     *
     *@param  msg  Description of Parameter
     *@since       1.2
     */
    public void runUserQuery(  )
    {
        try
        {
            StringBuffer logInfo = new StringBuffer();
            long startTime = MiscUtils.logQuery( userID, datasource, dbQuery, logInfo );

            if (dbQuery.startsWith("U")) //Update statement
            {
            	stmt.executeUpdate(dbQuery);
            }
            else
            {
            	MraldOutFile.appendToFile(Config.getProperty("LOGFILE"), "OutputManager: RunUserQuery: About to execute the following query " + dbQuery );

            	rs = stmt.executeQuery( dbQuery );
            	MraldOutFile.appendToFile(Config.getProperty("LOGFILE"), "OutputManager: RunUserQuery: Executed the following query " + dbQuery );

            	if (doPivot)
            	{
            		rs = new PivotedResultSet(rs, pivotPieces);
            	}
            }
            /*
             *  RETURN success status
             */
            MiscUtils.logQueryRun( startTime, logInfo );
        }
        catch ( SQLException se )
        {
            try
            {
                prepStmt = conn.prepareStatement(dbQuery);
                rs= prepStmt.executeQuery();
            }
            catch(SQLException sqe)
            {
                try
                {
                    prepStmt.close();
                    stmt.close();
                    conn.close();
                }
                catch ( Exception e )
                {
                    //don't do anything - just cleaning up
                    MraldOutFile.logToFile( e );
                }
                throw new MraldError( se, msg );
            }
        }
    }


    /**
     *  Allow for Inherited methods to set an ArrayList
     *  of DB formats - before using nice names, in case needed.
     */
    protected void setDBFormats()
    {
        return;
    }


    /**
     *  Gets the AndFormat attribute of the OutputManager object
     *
     *@param  number  Description of Parameter
     *@param  format  Description of Parameter
     *@return         The AndFormat value
     *@since
     */
    protected String getAndFormat( BigDecimal number, String format )
    {
        if ( number == null )
        {
            return "null";
        }

        if ( format.equals( Config.EMPTY_STR ) )
        {
            return number.toString();
        }
        DecimalFormat df = new DecimalFormat( format );
        return df.format( number );
    }


    /**
     *  Gets the AndFormat attribute of the OutputManager object
     *
     *@param  date    Description of Parameter
     *@param  format  Description of Parameter
     *@return         The AndFormat value
     *@since
     */
    protected String getAndFormat( Timestamp date, String format )
    {
        if ( date == null )
        {
            return "null";
        }
        SimpleDateFormat df = new SimpleDateFormat( format );
        return df.format( date );
    }


    /**
     *  Gets the BigDecimal attribute of the OutputManager object
     *
     *@param  number  Description of Parameter
     *@return         The BigDecimal value
     *@since
     */
    protected BigDecimal getBigDecimal( String number )
    {
        return new BigDecimal( number );
    }


    /**
     *  Gets the Timestamp attribute of the OutputManager object
     *
     *@param  date                          Description of Parameter
     *@return                               The Timestamp value
     *@exception  java.text.ParseException  Description of Exception
     *@since
     */
    protected Date getTimestamp( String date )
        throws java.text.ParseException
    {
        SimpleDateFormat df = new SimpleDateFormat();
        return df.parse( date );
    }


    /**
     *  preprocess the name/value pairs to group them up to form:
     *  field:name~nicename:name~type:{date|number}s
     *
     *@param  msg                     Description of Parameter
     *@return                         Description of the Returned Value
     *@exception  MsgObjectException  Description of Exception
     *@since
     */
    protected MsgObject groupNVPairs( MsgObject msg )
        throws MsgObjectException
    {
        String currentName;
        Iterator nvNames = msg.getNames().iterator();
        MsgObject returnObject = new MsgObject();
        /*
         *  For each name, append the pairs to build one long value, properly structured
         */
        int outputFormats = 0;
        while ( nvNames.hasNext() )
        {
            currentName = ( String ) nvNames.next();
            if ( currentName.toLowerCase().startsWith( FormTags.OUTPUT_TAG ) )
            {
                String[] values = msg.getValue( currentName );
                StringBuffer ret = new StringBuffer( values[0] );
                for ( int i = 1; i < values.length; i++ )
                {
                    ret.append( "~" + values[i] );
                }
                returnObject.setValue( Integer.toString( outputFormats ), ret.toString() );
                outputFormats++;
            }
        }
        return returnObject;
    }


    /**
     *  Description of the Method
     *
     *@param  parseString  Description of Parameter
     *@return              Description of the Returned Value
     *@since
     */
    protected MsgObject parseNVPairs( String parseString )
    {
        MsgObject nameValues = new MsgObject();
        StringTokenizer valueTokens = new StringTokenizer( parseString, FormTags.TOKENIZER_STR );
        /*
         *  Parse out the ~ and loop
         */
        int split;
        String pair;
        String name;
        String value;
        while ( valueTokens.hasMoreTokens() )
        {
            pair = valueTokens.nextToken();
            split = pair.indexOf( FormTags.NAMEVALUE_TOKEN_STR );
            name = pair.substring( 0, split );
            value = pair.substring( split + 1, pair.length() );
            nameValues.setValue( name, value );
        }
        return nameValues;
    }


    /**
     *  The main control method for how the output is formatted.  Subclasses should override the methods
     *  here as needed/desired.
     *
     *@param  msg                         Description of Parameter
     *@exception  ServletException        Description of Exception
     *@exception  IOException             Description of Exception
     *@exception  SQLException            Description of Exception
     *@exception  OutputManagerException  Description of Exception
     *@exception  MsgObjectException      Description of Exception
     *@since                              1.2
     */
    public void formatOutput( )
        throws MraldException
    {
        try
        {
        	//Only set up this and print header on output from the first query

        	if (firstTime)
        	{
	            msg.SetOutPrintWriter();
	            out = msg.getOutPrintWriter();
	            getFormatInfo();
	            prepareHeaders(); //abstract
	            printStart();

	            firstTime = false;
        	}
            printBody(); //abstract
            printLimit();
            printEnd();
            //out.close(); //GH Moved to execute , as this method may be called multiple times for multiple queries
        }
        catch(SQLException e)
        {
            throw new MraldException(e, msg);
        }
        catch(IOException e)
        {
            throw new MraldException(e, msg);
        }
    }


    /**
     *  Sets the http headers.  E.g.,<br/>
        <code>
        msg.setContentType( "text/html" );
        msg.setHeader( "Content-Disposition", "inline;" );
        </code>
     *
     *@param  msg                         Description of Parameter
     *@exception  OutputManagerException  Description of Exception
     *@since
     */
    protected abstract void prepareHeaders() throws OutputManagerException;

    /**
     *  Used to print headers, start of HTML file, etc.
     */
    protected void printStart() throws IOException, SQLException{}

    /**
     *  Used to print headers
     */
    protected void printHeaders()throws IOException, SQLException{}

    /**
     *  Prints the main output body.  All subclasses must implement this.
     */
    public abstract void printBody() throws IOException, SQLException;

    /**
     *  Prints the output stats and limit data
     */
    public void printLimit()
        throws IOException
    {
        out.print( "There were " + recordsReturned + " records returned.  " );
        if ( lineLimitSize == -1 )
        {
            out.print( "There was no line limit." );
        }
        else
        {
            out.print( "The limit was " + lineLimitSize + " lines." );
        }
        out.print( "  There were approximately " + bytesReturned + " bytes returned.  " );
        if ( mbLimitSize == -1 )
        {
            out.println( "There was no file size limit." );
        }
        else
        {
            out.println( "The limit was " + mbLimitSize + " bytes.  " );
        }
        if ( printQuery )
        {
            printQuery(  );
        }
    }

    /**
     *  Any clean up that has to be done for the file.  E.g., </html> tags
     */
    protected void printEnd()throws IOException, SQLException{}

    /**
     *  Sets the field names, nice names, format strings, and class types for
     *  the result set
     *
     *@param  msg                     Description of Parameter
     *@exception  MsgObjectException  Description of Exception
     *@exception  SQLException        Description of Exception
     *@since
     */
    protected void getFormatInfo(  )
        throws MsgObjectException, SQLException
    {
        ResultSetMetaData md = rs.getMetaData();
        int count = md.getColumnCount();
        /*
         *  set the output arrays
         */
        niceNames = new String[count];
        formats = new String[count];
        classNames = new String[count];
        /*
         *  initially set niceNames to the field names
         */
        for ( int i = 0; i < count; i++ )
        {
            niceNames[i] = md.getColumnName( i + 1 );
            try
            {
                classNames[i] = md.getColumnClassName( i + 1 );
            }
            catch ( java.lang.AbstractMethodError e )
            {
                classNames[i] = md.getColumnTypeName( i + 1 );
            }
        }
        /*
         *  Allow for Inherited methods to set an ArrayList
         *  of DB formats - before using nice names, in case needed.
         */
        setDBFormats();
        /*
         *  preprocess the name/value pairs to group them up to form:
         *  field:name~nicename:name~type:{date|number}s
         */
        MsgObject outputFormats = groupNVPairs( msg );
        /*
         *  loop through the outputFormats and replace the niceNames if applicable
         */
        MsgObject thisFormat;
        Iterator nvNames = outputFormats.getNames().iterator();
        while ( nvNames.hasNext() )
        {
            /*
             *  break out n/v pairs in each item and store in hashTable
             *  reuse MsgObject functionality (again) note this puts all the
             *  keys in all lower case, regardless of the case on the form -
             *  this should make it easier to do the forms.
             */
            String currentName = ( String ) nvNames.next();
            thisFormat = parseNVPairs( outputFormats.getValue( currentName )[0] );
            String thisFieldName = thisFormat.getValue( FormTags.OUTPUT_FIELD_TAG )[0];
            String thisNiceName = thisFormat.getValue( FormTags.OUTPUT_NICENAME_TAG )[0];
            String thisFormatType = thisFormat.getValue( FormTags.OUTPUT_FORMAT_TYPE )[0];
            String thisFormatPattern = thisFormat.getValue( FormTags.OUTPUT_FORMAT_PATTERN )[0];

            for ( int i = 0; i < niceNames.length; i++ )
            {
                if ( thisFieldName.equalsIgnoreCase( niceNames[i] ) ||
                		// PM: When pivoting, any formatting applied to the column that generates cells should be propagated to pivot-columns.
                		(doPivot && i >= niceNames.length-2 && thisFieldName.equalsIgnoreCase(((PivotedResultSet)md).getValueColumnName())) )
                {
                    /*
                     *  if nice name, set it, PM: but not for pivot-columns
                     */
                    if ( !( thisNiceName.equals( "" ) || (doPivot && i >= niceNames.length-2) ) )
                    {
                        niceNames[i] = thisNiceName;
                    }
                    /*
                     *  if format type, set it
                     */
                    if ( !thisFormatType.equals( "" ) || !thisFormatPattern.equals( "" ) )
                    {
                        if ( thisFormatType.toLowerCase().startsWith( "da" ) ||
                                thisFormatType.startsWith( "TIME" )
                                 )
                        {
                            classNames[i] = "Timestamp";
                        }
                        else if ( thisFormatType.toLowerCase().startsWith( "num" ) ||
                                thisFormatType.startsWith( "BIGINT" ) ||
                                thisFormatType.startsWith( "DECIMAL" ) ||
                                thisFormatType.startsWith( "DOUBLE" ) ||
                                thisFormatType.startsWith( "FLOAT" ) ||
                                thisFormatType.startsWith( "INTEGER" ) ||
                                thisFormatType.startsWith( "NUMERIC" ) ||
                                thisFormatType.startsWith( "SMALLINT" ) ||
                                thisFormatType.startsWith( "TINYINT" )
                                 )
                        {
                            classNames[i] = "BigDecimal";
                        }
                        //Date, CHAR, VARCHAR,FLOAT, NUMBER
                        else if ( thisFormatType.toLowerCase().startsWith( "flo" ) )
                        {
                            classNames[i] = "BigDecimal";
                        }
                        else
                        {
                            classNames[i] = thisFormatType;
                        }
                        formats[i] = thisFormatPattern;
                    }
                }
            }
        }
    }


    public void setResultSet(ResultSet rs)
    {
        this.rs = rs;
    }

    public void setQuery(String query)
    {
        dbQuery = query;
    }
}