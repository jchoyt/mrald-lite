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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;


/**
 *  Description of the Class
 *
 *@author     jchoyt
 *@created    January 26, 2003
 */
public class TableMetaData
{
    HashMap <String, String> columnComments;
    ArrayList <String> columnNames;
    String comments;
    String name;
    // ArrayList tableDotColumnNames;
    HashMap <String,Integer> columnTypes;
    ArrayList <String> primaryKeys;


    /**
     *  Constructor for the TableMetaData object
     *
     *@param  name  Name of the database table
     */
    public TableMetaData( String name )
    {
        this.name = name;
        columnComments = new HashMap <String, String> ();
        columnNames = new ArrayList <String> ();
        columnTypes=new HashMap <String,Integer> ();
        primaryKeys=new ArrayList <String> ();
    }


    /**
     *  Constructor for the TableMetaData object
     */
    public TableMetaData()
    {
        columnComments = new HashMap <String, String> ();
        columnNames = new ArrayList <String> ();
        columnTypes=new HashMap <String,Integer> ();
        primaryKeys=new ArrayList <String> ();
    }


    /**
     *  Sets the comments attribute of the TableMetaDataTag object
     *
     *@param  comments  The new comments value
     */
    public void setComments( String comments )
    {
        this.comments = comments;
    }


    /**
     *  Sets the name attribute of the TableMetaDataTag object
     *
     *@param  name  The new name value
     */
    public void setName( String name )
    {
        this.name = name;
    }


    /**
     *  Gets the columnNames attribute of the TableMetaDataTag object
     *
     *@return    ArrayList of column names in this table
     */
    public Collection <String> getColumnNames()
    {
        return columnNames;
    }


    /**
     *  Gets the comments attribute of the TableMetaData object
     *
     *@return    The comments value
     */
    public String getComments()
    {
        return comments;
    }


    /**
     *  Gets the comments attribute of the TableMetaDataTag object
     *
     *@param  fieldName  Description of the Parameter
     *@return            The comments value
     */
    public String getFieldComments( String fieldName )
    {
        return columnComments.get( fieldName );
    }


    public Integer getFieldType( String fieldName )
    {
        return columnTypes.get( fieldName );
    }

    /**
     *  Gets the name attribute of the TableMetaDataTag object
     *
     *@return    The name value
     */
    public String getName()
    {
        return name;
    }

    /**
     *  Adds a column to this table
     *
     *@param  name      The feature to be added to the Column attribute
     *@param  comments  The feature to be added to the Column attribute
     */
    public Collection <String> getPrimaryKeys()
    {
         return primaryKeys;
    }

    /**
     *  Adds a column to this table
     *
     *@param  name      The feature to be added to the Column attribute
     *@param  comments  The feature to be added to the Column attribute
     */
    public boolean isPrimaryKey(String column)
    {
        return primaryKeys.contains(column);
    }

    /**
     *  Adds a column to this table
     *
     *@param  name      The feature to be added to the Column attribute
     *@param  comments  The feature to be added to the Column attribute
     */
    public boolean isPrimaryKeyNoCase(String column)
    {
    	for (String pKey: primaryKeys)
    	{
    		if (pKey.toUpperCase().equals( column.toUpperCase()))
    			return true;
    	}
        return false;
    }
     /**
     *  Adds a column to this table
     *
     *@param  name      The feature to be added to the Column attribute
     *@param  comments  The feature to be added to the Column attribute
     */
    public void addPrimaryKey( String name )
    {
        if (!primaryKeys.contains( name))
                  primaryKeys.add( name );
    }

    /**
     *  Adds a column to this table
     *
     *@param  name      The feature to be added to the Column attribute
     *@param  comments  The feature to be added to the Column attribute
     */
    public void addColumn( String name, String comments, int columnType )
    {
        columnNames.add( name );
        columnComments.put( name, comments );
        columnTypes.put(name, new Integer(columnType));
    }

    /*
     *  void buildTableDotColumnNames()
     *  {
     *  tableDotColumnNames = new ArrayList();
     *  for( int i=0; i<columnNames.size(); i++)
     *  {
     *  tableDotColumnNames.add(name + columnNames.get(i));
     *  }
     *  }
     */
}

