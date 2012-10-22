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

import java.util.Properties;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 *  This class stores data about the structure of the tables the user chooses in
 *  the first step in the BuildForm process. It should be populated and stored
 *  by the MetaData class.<br/>
 *  <br/>
 *  It also stores a Properties object which contains the information necessary
 *  to connect to the database.
 *
 *@author     jchoyt
 *@created    January 24, 2003
 *@see        org.mitre.mrald.taglib.DBMetaDataTag
 */

public class DBMetaData extends java.lang.Object
{
    private Set<Link> linkList = new HashSet<Link>();
    private ArrayList<TableMetaData> tableMetaData;
    private String[] originalTables;
    private Properties dbProps;
    private String dbVersion;
    private State state;
    public enum State { NotInitialized, Loading, Loaded, Broken }

    public String toString()
    {
        StringBuilder ret = new StringBuilder( );
        ret.append( "DBMetaData for data source \n" );
        ret.append( dbProps.toString() );
        ret.append( "\nNumber of tables: " );
        ret.append( tableMetaData.size() );
        return ret.toString();
    }

    /**
     *  Constructor for the DBMetaData object
     */
    public DBMetaData()
    {
        tableMetaData = new ArrayList<TableMetaData>();
        dbProps = new Properties();
        state = State.NotInitialized;
    }

    public void setState( State s )
    {
        this.state = s;
    }

    /**
     *  Constructor for the DBMetaData object
     *
     *@param  props  Description of the Parameter
     */
    public DBMetaData( Properties props )
    {
        dbProps = props;
        tableMetaData = new ArrayList<TableMetaData>();
        state = State.NotInitialized;
    }


    /**
     *  Returns the value of originalTables.
     *
     *@return    The originalTables value
     */
    public String[] getOriginalTables()
    {
        return originalTables;
    }

    public State getState()
    {
        return state;
    }


    /**
     *  Sets the value of originalTables.
     *
     *@param  originalTables  The value to assign originalTables.
     */
    public void setOriginalTables( String[] originalTables )
    {
        this.originalTables = originalTables;
    }


    /**
     *  Gets meta data for all tables in this request
     *
     *@return    A Collection of TableMetaData objects
     */
    public Collection<TableMetaData> getAllTableMetaData()
    {
        return tableMetaData;
    }


    /**
     *  Gets the linkList attribute of the DBMetaData object
     *
     *@return    The linkList value
     */
    public Set getLinkList()
    {
        return linkList;
    }

    public String getDbVersion()
    {
        return dbVersion;
    }


    /**
     *  Sets the linkList attribute of the DBMetaData object
     *
     *@param  newLink  The feature to be added to the Link attribute
     */
    public void addLink( Link newLink )
    {
        linkList.add( newLink );
    }


    /**
     *  Sets the linkList attribute of the DBMetaData object
     *
     *@param  newLinks  The feature to be added to the Links attribute
     */
    public void addLinks( Set<Link> newLinks )
    {
        Iterator<Link> linkIter = newLinks.iterator();

        while ( linkIter.hasNext() ) {
            linkList.add( linkIter.next() );
        }

    }


    /**
     *  Sets the linkList attribute of the DBMetaData object
     *
     *@param  links  The new linkList value
     */
    public void setLinkList( Set<Link> links )
    {
        this.linkList = links;
    }


    public void setDbVersion( String product, String productVersion )
    {
        dbVersion = product + " " + productVersion;
    }


    /**
     *  Gets the tableMetaData attribute of the DBMetaData object
     *
     *@param  tableName  Description of the Parameter
     *@return            The tableMetaData value
     */
    public TableMetaData getTableMetaData( String tableName )
    {
        TableMetaData md;

        for ( int i = 0; i < tableMetaData.size(); i++ ) {
            md = tableMetaData.get( i );

            if ( md.getName().equals( tableName ) ) {
                return md;
            }
        }

        return null;
    }

    /**
     *  Gets the tableMetaData attribute of the DBMetaData object
     *
     *@param  tableName  Description of the Parameter
     *@return            The tableMetaData value
     */
    public TableMetaData getTableMetaDataNoCase( String tableName )
    {
        TableMetaData md;

        tableName = tableName.toUpperCase();

        for ( int i = 0; i < tableMetaData.size(); i++ ) {
            md = tableMetaData.get( i );

            if ( md.getName().toUpperCase().equals( tableName ) ) {
                return md;
            }
        }

        return null;
    }

    /**
     *  Gets the Links when the FK TableNAME and column Name is used to specify
     *  the Link
     *
     *@param  tableName  Description of the Parameter
     *@param  fieldName  Description of the Parameter
     *@return            The tableMetaData value
     */
    public Link getFKLinkData( String tableName, String fieldName )
    {
        Link link;
        Iterator linkIter = linkList.iterator();

        while ( linkIter.hasNext() ) {
            link = ( Link ) linkIter.next();

            if ( link.getFtable().equals( tableName ) &&
                    link.getFcolumn().equals( fieldName ) ) {
                return link;
            }
        }

        return null;
    }


    /**
     *  Gets the Links when the PK is used to specify the tableName
     *
     *@param  tableName  Description of the Parameter
     *@return            The tableMetaData value
     */
    public List <Link> getPKLinkData( String tableName )
    {
        Link link;
        Iterator <Link> linkIter = linkList.iterator();
        ArrayList <Link> links = new ArrayList <Link> ();

        while ( linkIter.hasNext() ) {
            link = linkIter.next();

            if ( link.getPtable().equals( tableName ) ) {
                if ( !links.contains( link ) ) {
                    links.add( link );
                }
            }
        }

        return links;
    }


    /*
     *  Generate a list of Links between two tables
     */
    /**
     *  Gets the links attribute of the DBMetaData object
     *
     *@param  table1              Description of the Parameter
     *@param  table2              Description of the Parameter
     *@return                     The links value
     *@exception  MraldException  Description of the Exception
     */
    public ArrayList getLinks( String table1, String table2 )
    throws MraldException
    {
        Link link;
        Iterator linkIter = linkList.iterator();
        ArrayList<Link> links = new ArrayList<Link>();

        while ( linkIter.hasNext() ) {
            link = ( Link ) linkIter.next();

            if ( ( link.getPtable().equals( table1 ) ) && ( link.getFtable().equals( table2 ) ) ) {
                links.add( link );
            }

            else if ( ( link.getPtable().equals( table2 ) ) && ( link.getFtable().equals( table1 ) ) ) {
                links.add( link );
            }
        }

        if ( links.size() == 0 ) {
            throw new MraldException( "The tables selected in the Drop Downs are not linked." );
        }

        return links;
    }


    /**
     *  Adds a feature to the TableMetaData attribute of the DBMetaData object
     *
     *@param  addee  The feature to be added to the TableMetaData attribute
     */
    public void addTableMetaData( TableMetaData addee )
    {
        if ( !tableMetaData.contains( addee ) ) {
            tableMetaData.add( addee );
        }
    }


    /**
     *  Sets the links attribute of the DBMetaData class
     *
     *@param  md  The new links value
     *@return     Description of the Return Value
     */
    public static HashMap setLinks( DBMetaData md )
    {
        Set linkList = md.getLinkList();
        Iterator linkIter = linkList.iterator();

        HashMap<String,ArrayList<Link>> keysMap = new HashMap<String,ArrayList<Link>>();
//        Link[] keyList;
        //Loop through the LinkList, and if PKey table or FKey table is in Table List

        //Loop through the list of LInks and re order them to associate themwith Primary key
        while ( linkIter.hasNext() ) {
            Link link = ( Link ) linkIter.next();

            String fTable = link.getFtable();

            if ( !( keysMap.containsKey( fTable ) ) ) {
                ArrayList<Link> keys = new ArrayList<Link>();
                keys.add( link );
                keysMap.put( fTable, keys );
            }

            else {
                ArrayList<Link> keys = keysMap.get( fTable );
                keys.add( link );
                keysMap.put( fTable, keys );
            }

            String pTable = link.getPtable();

            if ( !( keysMap.containsKey( pTable ) ) ) {
                ArrayList<Link> keys = new ArrayList<Link>();
                keys.add( link );
                keysMap.put( pTable, keys );
            }

            else {
                ArrayList<Link> keys = keysMap.get( pTable );
                keys.add( link );
                keysMap.put( pTable, keys );
            }

        }

        return keysMap;
    }


    /**
     *  Gets the fkey attribute of the DBMetaData class
     *
     *@param  table    Description of the Parameter
     *@param  column   Description of the Parameter
     *@param  keysMap  Description of the Parameter
     *@return          The fkey value
     */
    public static boolean isFkey( String table, String column, HashMap keysMap )
    {
        if ( keysMap.containsKey( table ) ) {
            ArrayList links = ( ArrayList ) keysMap.get( table );

            for ( int i = 0; i < links.size(); i++ ) {
                Link link = ( Link ) links.get( i );

                // System.out.println( "DBMetaData : isFKey: Link " + link.toString() + " column :" + column );
                if ( link.getFcolumn().equals( column ) ) {
                    return true;
                }

                if ( link.getPcolumn().equals( column ) ) {
                    return true;
                }
            }
        }

        return false;
    }


    /**
     *  Gets the dbProps attribute of the DBMetaData object
     *
     *@return    The dbProps value
     */
    public Properties getDbProps()
    {
        return dbProps;
    }


    /**
     *  Sets the dbProps attribute of the DBMetaData object
     *
     *@param  props  The new dbProps value
     */
    public void setDbProps( Properties props )
    {
        dbProps = props;
    }

}

