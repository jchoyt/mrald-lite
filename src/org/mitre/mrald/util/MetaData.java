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
package org.mitre.mrald.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

/**
 *  Description of the Class
 *
 *@author     ghamilton
 *@created    Jan 20th , 2005
 */

public class MetaData
{


    boolean listAllLinkTables = false;

    private static HashMap<String, DBMetaData> databases = new HashMap<String, DBMetaData>();

    /**
     *  Description of the Field
     */
    public static String ADMIN_DB = "db_admin.props";

    /**
     *  Constructor for the AllTablesListTag object
     */
    public MetaData()
    { }


    public static void loadDbProperties(HashMap<String, DBMetaData> newDatabases)
    {
    	databases= newDatabases;
    }

    public static HashMap<String, DBMetaData> getDbProperties()
    {
    	return databases;

    }

    public static Properties getDbProperties( String key )
    {
        DBMetaData dbmd = databases.get( key );

        if ( dbmd == null ) {
            throw new NullPointerException( "A data source with the name " + key +
                                            " has not been defined.  Please contact your administrator as the form you are using will not work until this is fixed." );
        }

        Properties ret = dbmd.getDbProps();
        return ret;
    }

    /**
     *  this is ONLY to be used for unit testing.  If you have an urge to use
     *  this for ANYTHING else, FIGHT IT
     */
    public static void putDbProperties( String key, DBMetaData md)
    {
        databases.put(key, md);
    }

    /**
     *  Given the datasource name (the file name of the properties object),
     *  get the database meta data.
     */
    public static DBMetaData getDbMetaData( String key )
    {
        DBMetaData ret = databases.get( key );

        if ( ret == null )
        {
            ret = databases.get( "main" );
        }

        return ret;
    }


    /**
     *  The reverse of getDbMetaData().  Given a DBMetaData object, find the
     *  key that the object is stored under - basically a reverse lookup of the
     *  databases HashMap.  Will return the name of the main props file instead
     *  of "main".  Returns null if the DBMetaData is not found.
     */
    public static String getFileName( DBMetaData md )
    {
        for ( String key : databases.keySet() )
        {
            if ( md == databases.get( key ) )
            {
                if( key.equals("main") )
                {
                    return Config.PROPS_FILE;
                }
                else
                    return key;
            }
        }
        return null;
    }

    /**
     *  The reverse of getDbMetaData().  Given a DBMetaData object, find the
     *  key that the object is stored under - basically a reverse lookup of the
     *  databases HashMap.  Will return the name of the main props file instead
     *  of "main".  Returns null if the DBMetaData is not found.
     */
    public static String getDataSource( DBMetaData md )
    {
        for ( String key : databases.keySet() )
        {
            if ( md == databases.get( key ) )
            {
                if( key.equals("main") )
                {
                    return key;
                }
                else
                    return key;
            }
        }
        return null;
    }
    /**
     *  Description of the Method
     */
    public static void reload()
    {

        databases.clear();
        /*
         *  This will hopefully eventually be removed as the properties
         *  are removed from config.properties
         *  JCH - nope, can't do - the unit tests use the "main" datasource
         */

        try {
            Properties mainProps = new Properties();
            mainProps.put( "DBLOGIN", Config.getProperty( "DBLOGIN" ) );
            mainProps.put( "DBPASSWORD", Config.getProperty( "DBPASSWORD" ) );
            mainProps.put( "DBDRIVER", Config.getProperty( "DBDRIVER" ) );
            mainProps.put( "DBSERVER", Config.getProperty( "DBSERVER" ) );
            mainProps.put( "SCHEMA", Config.getProperty( "SCHEMA" ) );
            mainProps.put( "DBNAME", Config.getProperty( "DBNAME" ) );
             //load the driver
             // System.out.println( String.valueOf(mainProps) );
            try {
                Class.forName( Config.getProperty( "DBDRIVER" ) );
            }

            catch ( ClassNotFoundException e ) {
                String mailText = MiscUtils.formatThrowable( e );
                e.printStackTrace();
                Mailer.send( Config.getProperty( "MAILTO" ),
                             "MetaData",
                             Config.getProperty( "SMTPHOST" ),
                             mailText, "The driver for the main datasource, " +
                             Config.getProperty( "DBDRIVER" ) + ", could not be found" );
            }

            DBMetaData main = new DBMetaData( mainProps );
            databases.put( "main", main );

            Thread t = new Thread( new AsynchronousMetaDataLoader( "main" ) );
            t.start();
        }

        catch ( NullPointerException e ) {
            //that's OK - preferred, actually.  there shouldn't be any datasource configured in there anymore
            e.printStackTrace();
        }

        /*
         *  troll for db props files
         */
        File config_dir = new File( Config.getProperty( "BasePath" ) + "/WEB-INF/props/" );

        File[] files = config_dir.listFiles( new DbPropsFilter() );

        if ( files == null ) {
            //no props files to process
            return ;
        }

        Properties config;

        for ( int i = 0; i < files.length; i++ ) {

            config = new Properties();
            /*
             *  load the props file
             */

            try {
                InputStream in = new FileInputStream( files[ i ] );
                config.load( in );
                in.close();
            }

            catch ( IOException e ) {
                throw new RuntimeException( e );
            }

            /*
             *  check for DBSERVER - if it doesn't exist, it's not a db props file
             */
            if ( config.getProperty( "DBSERVER" ) != null ) {
                //load the driver
                try {
                    Class.forName( config.getProperty( "DBDRIVER" ) );
                }

                catch ( ClassNotFoundException e ) {
                    String mailText = MiscUtils.formatThrowable( e );
                    e.printStackTrace();
                    Mailer.send( Config.getProperty( "MAILTO" ),
                                 "MetaData",
                                 Config.getProperty( "SMTPHOST" ),
                                 mailText, "The driver for the datasource in " + files[ i ] + ", " +
                                 config.getProperty( "DBDRIVER" ) + ", could not be found" );
                    continue;
                }

                DBMetaData metaData = new DBMetaData( config );
                databases.put( files[ i ].getName(), metaData );

                Thread t = new Thread( new AsynchronousMetaDataLoader( files[ i ].getName() ) );
                t.start();
            }
        }


    }


    /**
     *  Description of the Method
     *
     *@param  dbmd                Description of the Parameter
     *@return                     Description of the Return Value
     *@exception  MraldException  Description of the Exception
     */
    public static void populateDBMetaData( String key )
    {
        try {
            //System.out.println( "Datasources : populating Meta Data :" + key );

            Connection conn = new MraldConnection( key ).getConnection( );
            DatabaseMetaData dbmd = conn.getMetaData();
            String schema = getDbProperties( key ).getProperty( "SCHEMA" );
            TableMetaData tableData;
            /* Get the empty instance */
            DBMetaData md = getDbMetaData( key );
            md.setState( DBMetaData.State.Loading );
            /* set the version */
            md.setDbVersion( dbmd.getDatabaseProductName(), dbmd.getDatabaseProductVersion() );
            String[] types = { "TABLE", "VIEW" };
            String tableName;

            ResultSet rs = dbmd.getTables( null, schema, null, types );

            while ( rs.next() ) {
                tableName = rs.getString( 3 );
                /*
                 *  This if block explicitly ignores Oracle 10 recycle bin tables.
                 *  This should be removed after Oracle fixes the bug with their JDBC
                 *  driver where some of these tables throw ORA-01424 (missing or
                 *  illegal character following the escape character)
                 */

                if ( tableName.startsWith( "BIN$" ) ) {
                    continue;
                }

                tableData = new TableMetaData( tableName );
                tableData.setComments( rs.getString( 5 ) );
                md.addTableMetaData( tableData );
            }

            md = buildLinksSet( dbmd, md );
            md = setColumnData( dbmd, md );
            md.setState( DBMetaData.State.Loaded );
        }

        catch ( RuntimeException e ) {
            MraldOutFile.logToFile( e );
            //if the dataset doesn't load - remove it from the list
            removeDbMetaData( key );

            try {
                String mailText = MiscUtils.formatThrowable( e );
                Mailer.send( Config.getProperty( "MAILTO" ), "MetaData", Config.getProperty( "SMTPHOST" ), mailText, "Error loading " + key + " dataset:\n" );
            }

            catch ( Exception e2 ) {
                e2.printStackTrace( );
            }
        }

        catch ( SQLException e ) {
            MraldOutFile.logToFile( e );
            //if the dataset doesn't load - remove it from the list
            removeDbMetaData( key );

            try {
                String mailText = MiscUtils.formatThrowable( e );
                Mailer.send( Config.getProperty( "MAILTO" ), "MetaData", Config.getProperty( "SMTPHOST" ), mailText, "Error loading " + key + " dataset:\n" );
            }

            catch ( Exception e2 ) {
                e2.printStackTrace( );
            }
        }
    }


    protected static void removeDbMetaData( String key )
    {

        databases.remove( key );

    }

    /**
     *  Sets the columnData attribute of the Moogle object
     *
     *@param  dbmd                The new columnData value
     *@param  md                  The new columnData value
     *@return                     Description of the Return Value
     *@exception  MraldException  Description of the Exception
     */
    private static DBMetaData setColumnData( DatabaseMetaData dbmd, DBMetaData md )
    throws SQLException
    {

        ArrayList tables = ( ArrayList ) md.getAllTableMetaData();

//        String table;
        ResultSet rs;
        String schema = md.getDbProps( ).getProperty( "SCHEMA" );


        //TableMetaData tableData;
        for ( int i = 0; i < tables.size(); i++ ) {
            //If this table is not already added to the DBMetaData add details.
            TableMetaData tableData = ( TableMetaData ) tables.get( i );
            String tableName = tableData.getName();

            if ( tableData != null ) {

                /*
                 *  This if block explicitly ignores Oracle 10 recycle bin tables.
                 *  This should be removed after Oracle fixes the bug with their JDBC
                 *  driver where some of these tables throw ORA-01424 (missing or
                 *  illegal character following the escape character)
                 */

                if ( tableName.startsWith( "BIN$" ) ) {
                    continue;
                }

                rs = dbmd.getColumns( null, schema, tableName, null );

                while ( rs.next() ) {
                    tableData.addColumn( rs.getString( "COLUMN_NAME" ), rs.getString( "REMARKS" ), rs.getInt( "DATA_TYPE" ) );
                }

                rs.close();

                rs = dbmd.getPrimaryKeys( null, schema, tableName );

                while ( rs.next() ) {
                    tableData.addPrimaryKey( rs.getString( "COLUMN_NAME" ) );
                }

                rs.close();
            }
        }

        return md;
    }


    /**
     *  Produces a Set of links from the jdbc driver - all links between the
     *  tables contained in tableSet are included. buildTableSet() must be
     *  called before this method.
     *
     *@param  dbmd                Description of the Parameter
     *@param  md                  Description of the Parameter
     *@return                     Description of the Return Value
     *@exception  MraldException  Description of the Exception
     */
    private static DBMetaData buildLinksSet( DatabaseMetaData dbmd, DBMetaData md )
    throws SQLException
    {
        Collection<TableMetaData> tables = md.getAllTableMetaData();
        Link link;
        Iterator iter = tables.iterator();
        Set<Link> linkList = new HashSet<Link>();

        while ( iter.hasNext() ) {
            TableMetaData tableInfo = ( TableMetaData ) iter.next();
            String table = tableInfo.getName();
            /*
             *  get imported keys
             */
            ResultSet rs = dbmd.getImportedKeys( null, null, table );

            while ( rs.next() ) {
                link = new Link( rs.getString( "PKTABLE_NAME" ),
                                 rs.getString( "PKCOLUMN_NAME" ),
                                 rs.getString( "FKTABLE_NAME" ),
                                 rs.getString( "FKCOLUMN_NAME" ) );

                linkList.add( link );
            }

            rs.close();
            /*
             *  get exported keys
             */
            rs = dbmd.getExportedKeys( null, null, table );

            while ( rs.next() ) {
                link = new Link( rs.getString( "PKTABLE_NAME" ),
                                 rs.getString( "PKCOLUMN_NAME" ),
                                 rs.getString( "FKTABLE_NAME" ),
                                 rs.getString( "FKCOLUMN_NAME" ) );

                linkList.add( link );
            }

            rs.close();
        }

        md.setLinkList( linkList );
        return md;
    }


    public static Set<String> getDatasourceNames()
    {
        return databases.keySet();
    }


    /**
     *  Starting from the initial set of tables, finds all tables within <code>depth</code>
     *  joins. Requires that the passed
     *
     *@param  tables              Description of the Parameter
     *@param  tableDepth          Description of the Parameter
     *@return                     Description of the Return Value
     *@exception  MraldException  Description of the Exception
     */
    public static DBMetaData getDataSubSet( String datasourceName, String[] tables, int tableDepth )
    throws MraldException
    {
        DBMetaData md = databases.get( datasourceName );
        return getDataSubSet( md, tables, tableDepth );
    }


    /**
     *  Starting from the initial set of tables, finds all tables within <code>depth</code>
     *  joins. Requires that the passed
     *
     *@param  dbmd                Description of the Parameter
     *@param  tables              Description of the Parameter
     *@param  tableDepth          Description of the Parameter
     *@return                     Description of the Return Value
     *@exception  MraldException  Description of the Exception
     */
    public static DBMetaData getDataSubSet( DBMetaData dbmd, String[] tables, int tableDepth )
    throws MraldException
    {
        DBMetaData subDbmd = new DBMetaData();

        if ( tables.length == 0 ) {
            throw new MraldException( "No tables were chosen in the previous step.  Please go back and choose one or more tables." );
        }

        subDbmd.setOriginalTables( tables );
        Set<String> tableList = new HashSet<String>();
        /*
         *  add the initial tables to the list
         */
        tableList.addAll( Arrays.asList( tables ) );

        Set<Link> links;

        Iterator iter = tableList.iterator();
        HashSet<String> newTables = new HashSet<String>();
        String tableName;

        if ( tableDepth == 0 ) {
            links = buildLinksSubSet( dbmd, tableList, true );

            while ( iter.hasNext() ) {
                tableName = ( String ) iter.next();

                subDbmd.addTableMetaData( dbmd.getTableMetaData( tableName ) );
            }

            subDbmd.addLinks( links );

            return subDbmd;
        }

        else {
            links = buildLinksSubSet( dbmd, tableList );
        }

        Iterator linksIter = links.iterator();

        for ( int i = 0; i < tableDepth; i++ ) {
            if ( tableDepth == 0 ) {
                continue;
            }

            while ( iter.hasNext() ) {
                tableName = ( String ) iter.next();
                subDbmd.addTableMetaData( dbmd.getTableMetaData( tableName ) );

                while ( linksIter.hasNext() ) {

                    Link link = ( Link ) linksIter.next();

                    //if ((tableList.contains(link.getPtable())) || (tableList.contains(link.getFtable())) )
                    if ( tableList.contains( link.getPtable() ) ) {

                        String fKeyTable = link.getFtable();
                        newTables.add( link.getFtable() );
                        subDbmd.addLink( link );

                        subDbmd.addTableMetaData( dbmd.getTableMetaData( fKeyTable ) );

                    }

                    else {
                        String pKeyTable = link.getPtable();
                        newTables.add( link.getPtable() );
                        subDbmd.addLink( link );

                        subDbmd.addTableMetaData( dbmd.getTableMetaData( pKeyTable ) );

                    }
                }
            }

            tableList.addAll( newTables );

            links = buildLinksSubSet( dbmd, newTables );

            linksIter = links.iterator();
            iter = tableList.iterator();

        }

        //Final filter on Links.
        //Make sure that only include  links where both of the tables are contained within the final table list
        links = buildLinksSubSet( subDbmd, tableList, true );

        subDbmd.addLinks( links );

        return subDbmd;
    }


    /**
     *  Produces a Set of links from the jdbc driver - all links between the
     *  tables contained in tableSet are included. buildTableSet() must be
     *  called before this method.
     *
     *@param  md                  Description of the Parameter
     *@param  tableSubSet         Description of the Parameter
     *@return                     Description of the Return Value
     *@exception  MraldException  Description of the Exception
     */
    private static Set<Link> buildLinksSubSet( DBMetaData md, Set tableSubSet )
    throws MraldException
    {
        Set<Link> linkSubSet = new HashSet<Link>();
//        String tableName;

        Set linkList = md.getLinkList();
        Iterator linkIter = linkList.iterator();

        //Loop through the LinkList, and if PKey table or FKey table is in Table List
        while ( linkIter.hasNext() ) {

            Link link = ( Link ) linkIter.next();

            if ( tableSubSet.contains( link.getPtable() ) ) {
                linkSubSet.add( link );
            }

            if ( tableSubSet.contains( link.getFtable() ) ) {
                linkSubSet.add( link );
            }
        }

        return linkSubSet;
    }


    /**
     *  Produces a Set of links from the jdbc driver - all links between the
     *  tables contained in tableSet are included. buildTableSet() must be
     *  called before this method.
     *
     *@param  md                  Description of the Parameter
     *@param  tableSubSet         Description of the Parameter
     *@param  checkBoth           Description of the Parameter
     *@return                     Description of the Return Value
     *@exception  MraldException  Description of the Exception
     */
    private static Set<Link> buildLinksSubSet( DBMetaData md, Set tableSubSet, boolean checkBoth )
    throws MraldException
    {
        Set<Link> linkSubSet = new HashSet<Link>();
//        String tableName;

        Set linkList = md.getLinkList();
        Iterator linkIter = linkList.iterator();

        //Loop through the LinkList, and if PKey table or FKey table is in Table List
        while ( linkIter.hasNext() ) {

            Link link = ( Link ) linkIter.next();

            if ( tableSubSet.contains( link.getPtable() ) && tableSubSet.contains( link.getFtable() ) ) {
                linkSubSet.add( link );
            }

        }

        return linkSubSet;
    }

}


/**
 *  Description of the Class
 *
 *@author     jchoyt
 *@created    January 20, 2006
 */

class AsynchronousMetaDataLoader implements Runnable
{
    String dbMetaDataKey;

    private AsynchronousMetaDataLoader()
    {}


    public AsynchronousMetaDataLoader( String key )
    {
        dbMetaDataKey = key;
    }

    /**
     *  Main processing method for the AsynchronousMetaDataLoader object
     */
    public void run()
    {
        MraldOutFile.logToFile( new Date() + ": begin loading " + dbMetaDataKey );
        MetaData.populateDBMetaData( dbMetaDataKey );
        MraldOutFile.logToFile( new Date() + ": finished reloading " + dbMetaDataKey );
    }
}
