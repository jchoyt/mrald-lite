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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;


/**
 *  Description of the Class
 *
 *@author     jchoyt
 *@created    February 3, 2003
 */
public class FBUtils extends java.lang.Object
{

    /**
     *  for structural tables in FormBuilderElements - this ends a row
     */
    public final static String ROW_END = "</tr>";
    /**
     *  for structural tables in FormBuilderElements - this starts a row
     */
    public final static String ROW_START = "<tr>";
    /**
     *  for structural tables in FormBuilderElements - this ends the table
     */
    public final static String TABLE_END = "\n</table>";
    /**
     *  for structural tables in FormBuilderElements - this starts the table
     */
    public final static String TABLE_START = "\n<table cellspacing=\"0\" cellpadding=\"0\" border=\"0\">";
    /**
     *  Array of Integers that represent date/time types as returned by a JDBC
     *  driver
     */
    protected final static ArrayList<Integer> dateTypes = new ArrayList<Integer>();
    /**
     *  Array of Integers that represent numeric types as returned by a JDBC
     *  driver
     */
    protected final static ArrayList<Integer> numberTypes = new ArrayList<Integer>();
    /**
     *  Description of the Field
     */
    /**
     *  Array of Integers that represent numeric types as returned by a JDBC
     *  driver
     */
    protected final static ArrayList<Integer> binaryTypes = new ArrayList<Integer>();

     protected final static ArrayList<Integer> booleanTypes = new ArrayList<Integer>();
    private static Properties columnNames;
    private static PropertyChangeListener pcl;

    static
    {
        numberTypes.add( new Integer( Types.BIGINT ) );
        numberTypes.add( new Integer( Types.DECIMAL ) );
        numberTypes.add( new Integer( Types.DOUBLE ) );
        numberTypes.add( new Integer( Types.FLOAT ) );
        numberTypes.add( new Integer( Types.INTEGER ) );
        numberTypes.add( new Integer( Types.NUMERIC ) );
        numberTypes.add( new Integer( Types.SMALLINT ) );
        numberTypes.add( new Integer( Types.TINYINT ) );
        booleanTypes.add( new Integer( Types.BOOLEAN ) );
        booleanTypes.add( new Integer( Types.BIT ) );
        binaryTypes.add( new Integer( Types.BLOB ) );
        binaryTypes.add( new Integer( Types.OTHER ) );
        binaryTypes.add( new Integer( Types.LONGVARBINARY ) );
        dateTypes.add( new Integer( Types.DATE ) );
        dateTypes.add( new Integer( Types.TIME ) );
        dateTypes.add( new Integer( Types.TIMESTAMP ) );
    }


    /**
     *  Given a TABLE.FIELDNAME String, gets the columnName attribute as
     *  sepcified in the the appropriate properties file
     *
     *@param  tableDotField  Description of the Parameter
     *@return                The columnName value
     */
    public static String getColumnName( String tableDotField )
    {
        if ( columnNames == null )
        {
            initColumnNames();
            Config.addPropertyChangeListener( pcl );
        }
        return ( String ) columnNames.get( tableDotField );
    }


    /**
     *  Adds a feature to the ColumnNames attribute of the FBUtils class
     *
     *@param  newProps  The feature to be added to the ColumnNames attribute
     *@return           Description of the Return Value
     */
    public static void addColumnNames( Properties newProps )
    {
        if ( columnNames==null )
        {
            getColumnName("dummy to get the columnNames to initialize - so I don't have to duplicate code");
        }
        boolean changed = false;
        /*
         *  iterate through properties and add any that are missing
         */
        for ( Enumeration e = newProps.propertyNames(); e.hasMoreElements();  )
        {
            String name = ( String ) e.nextElement();
            String value = columnNames.getProperty( name );
            if ( value == null )
            {
                changed = true;
                columnNames.setProperty( name, newProps.getProperty( name ) );
            }
        }
        /*
         *  if any were changed, write out properties
         */
        if ( changed )
        {
            try
            {
                String base_path = Config.getProperty( "BasePath" );
                File config_file = new File( base_path + "/WEB-INF/props/columnNames.props" );
                FileOutputStream fout = new FileOutputStream( config_file );
                columnNames.store( fout, "modified for new form" );
                fout.close();
            }
            catch(IOException e)
            {
                throw new RuntimeException(e);
            }
        }

    }


    /**
     *  build the operator list
     *
     *@param  name            Description of the Parameter
     *@param  operatorNumber  Description of the Parameter
     *@return                 The operatorList value
     */
    public static StringBuffer getOperatorList( String name, String operatorNumber )
    {
        StringBuffer opList = new StringBuffer();
        opList.append( "\n<select name=\"" );
        opList.append( name );
        opList.append( "\">" );
        opList.append( "<option value=\"Operator" + operatorNumber + ":=\">=</option>" );
        opList.append( "<option value=\"Operator" + operatorNumber + ":!=\">Not equal (!=)</option>" );
        opList.append( "<option value=\"Operator" + operatorNumber + ":<\">&lt;</option>" );
        opList.append( "<option value=\"Operator" + operatorNumber + ":>\">&gt;</option>" );
        opList.append( "<option value=\"Operator" + operatorNumber + ":<=\">&lt;=</option>" );
        opList.append( "<option value=\"Operator" + operatorNumber + ":>=\">&gt;=</option>" );
        opList.append( "<option value=\"Operator" + operatorNumber + ":like\">Contains</option>" );
        opList.append( "<option value=\"Operator" + operatorNumber + ":not like\">Does Not Contain</option>" );
        opList.append( "<option value=\"Operator" + operatorNumber + ":starts\">Starts With</option>" );
        opList.append( "<option value=\"Operator" + operatorNumber + ":not starts\">Does Not Start With</option>" );
        opList.append( "<option value=\"Operator" + operatorNumber + ":IN\">IN</option>" );
        opList.append( "<option value=\"Operator" + operatorNumber + ":NOT IN\">NOT IN</option>" );
        opList.append( "<option value=\"Operator" + operatorNumber + ":IS NULL\">IS NULL</option>" );
        opList.append( "<option value=\"Operator" + operatorNumber + ":IS NOT NULL\">IS NOT NULL</option>" );
        opList.append( "</select>" );
        return opList;
    }

    public static StringBuffer getSimpleOperatorList( String name, String operatorNumber )
    {
        StringBuffer opList = new StringBuffer();
        opList.append( "\n<select name=\"" );
        opList.append( name );
        opList.append( "\">" );
        opList.append( "<option value=\"Operator" + operatorNumber + ":=\">=</option>" );
        opList.append( "<option value=\"Operator" + operatorNumber + ":!=\">Not equal (!=)</option>" );
        opList.append( "<option value=\"Operator" + operatorNumber + ":<\">&lt;</option>" );
        opList.append( "<option value=\"Operator" + operatorNumber + ":>\">&gt;</option>" );
        opList.append( "<option value=\"Operator" + operatorNumber + ":<=\">&lt;=</option>" );
        opList.append( "<option value=\"Operator" + operatorNumber + ":>=\">&gt;=</option>" );
        opList.append( "</select>" );
        return opList;
    }


    /**
     *  Gets the sqlType attribute of the FBUtils class
     *
     *@param  type  Description of the Parameter
     *@return       The sqlType value
     */
    public static String getSqlType( int type )
    {
        switch ( type )
        {
            case Types.ARRAY:
                return "ARRAY ";
            case Types.BIGINT:
                return "BIGINT ";
            case Types.BINARY:
                return "BINARY";
            case Types.BIT:
                return "BIT ";
            case Types.BLOB:
                return "BLOB";
            case Types.BOOLEAN:
                return "BOOLEAN ";
            case Types.CHAR:
                return "CHAR";
            case Types.CLOB:
                return "CLOB";
            case Types.DATALINK:
                return "DATALINK ";
            case Types.DATE:
                return "DATE ";
            case Types.DECIMAL:
                return "DECIMAL";
            case Types.DISTINCT:
                return "DISTINCT";
            case Types.DOUBLE:
                return "DOUBLE ";
            case Types.FLOAT:
                return "FLOAT ";
            case Types.INTEGER:
                return "INTEGER";
            case Types.JAVA_OBJECT:
                return "JAVA_OBJECT ";
            case Types.LONGVARBINARY:
                return "LONGVARBINARY";
            case Types.LONGVARCHAR:
                return "LONGVARCHAR";
            case Types.NULL:
                return "NULL";
            case Types.NUMERIC:
                return "NUMERIC";
            case Types.OTHER:
                return "OTHER";
            case Types.REAL:
                return "REAL ";
            case Types.REF:
                return "REF ";
            case Types.SMALLINT:
                return "SMALLINT	";
            case Types.STRUCT:
                return "STRUCT";
            case Types.TIME:
                return "TIME ";
            case Types.TIMESTAMP:
                return "TIMESTAMP";
            case Types.TINYINT:
                return "TINYINT";
            case Types.VARBINARY:
                return "VARBINARY";
	    case Types.VARCHAR:
                return "VARCHAR";
            default:
                return "VARCHAR";
        }
    }


    /**
     *  Gets the dateType attribute of the FBUtils class
     *
     *@param  _type  Description of the Parameter
     *@return        The dateType value
     */
    public static boolean isDateType( int _type )
    {
        Integer type = new Integer( _type );
        return dateTypes.contains( type );
    }


    /**
     *  Gets the dateType attribute of the FBUtils class
     *
     *@param  _type  Description of the Parameter
     *@return        The dateType value
     */
    public static boolean isBooleanType( int _type )
    {
        Integer type = new Integer( _type );
        return booleanTypes.contains( type );
    }


    /**
     *  Gets the dateType attribute of the FBUtils class
     *
     *@param  type  Description of the Parameter
     *@return       The dateType value
     */
    public static boolean isDateType( Integer type )
    {
        return dateTypes.contains( type );
    }


    /**
     *  Gets the numberType attribute of the FBUtils class
     *
     *@param  _type  Description of the Parameter
     *@return        The numberType value
     */
    public static boolean isNumberType( int _type )
    {
        Integer type = new Integer( _type );
        return numberTypes.contains( type );
    }


    /**
     *  Gets the numberType attribute of the FBUtils class
     *
     *@param  type  Description of the Parameter
     *@return       The numberType value
     */
    public static boolean isNumberType( Integer type )
    {
        return numberTypes.contains( type );
    }


     /**
     *  Gets the numberType attribute of the FBUtils class
     *
     *@param  type  Description of the Parameter
     *@return       The numberType value
     */
    public static boolean isBinaryType( Integer type )
    {
        return binaryTypes.contains( type );
    }

      /**
     *  Gets the numberType attribute of the FBUtils class
     *
     *@param  _type  Description of the Parameter
     *@return        The numberType value
     */
    public static boolean isBinaryType( int _type )
    {
        Integer type = new Integer( _type );
        return binaryTypes.contains( type );
    }

    /**
     *  Gets the numberType attribute of the FBUtils class
     *
     *@param  type  Description of the Parameter
     *@return       The numberType value
     */
    public static boolean isBooleanType( Integer type )
    {
        return booleanTypes.contains( type );
    }


    /**
     *  Description of the Method
     *
     *@param  md           Description of the Parameter
     *@param  num          Description of the Parameter
     *@param  elementName  Description of the Parameter
     *@return              Description of the Return Value
     */
    public static StringBuffer buildTableFieldDropDown( DBMetaData md, String elementName, int num )
    {
        StringBuffer ret = new StringBuffer();
        ret.append( "\n<select name=\"" );
        ret.append( elementName );
        ret.append( num );
        ret.append( "\">" );
        ret.append( "<option></option>" );
        Collection tableMetaData = md.getAllTableMetaData();
        TableMetaData tableInfo;
        Iterator iter = tableMetaData.iterator();
        while ( iter.hasNext() )
        {
            tableInfo = ( TableMetaData ) iter.next();
            Collection fieldNames = tableInfo.getColumnNames();
            String field;
            Iterator iter2 = fieldNames.iterator();
            /*
             *  and for each field, output an option
             */
            while ( iter2.hasNext() )
            {
                field = ( String ) iter2.next();
                ret.append( "<option value=\"Table:" );
                ret.append( tableInfo.getName() );
                ret.append( "~Field:" );
                ret.append( field );
                ret.append( "\">" );
                ret.append( tableInfo.getName() );
                ret.append( "." );
                ret.append( field );
                ret.append( "</option>" );
            }
        }
        ret.append( "</select>" );
        return ret;
    }


    /**
     *  Description of the Method
     *
     *@param  md           Description of the Parameter
     *@param  num          Description of the Parameter
     *@param  elementName  Description of the Parameter
     *@param  tableName    Description of the Parameter
     *@param  colName      Description of the Parameter
     *@param  tableLit     Description of the Parameter
     *@param  pkLit        Description of the Parameter
     *@param  fieldLit     Description of the Parameter
     *@return              Description of the Return Value
     */
    // FBUtils.buildTableFieldDropDown( md, "FBUpdate", cnt, tableName, pkColumn, "listTable", "listIdCol", "listColumn" ).toString();

    public static StringBuffer buildTableFieldDropDown( DBMetaData md, String elementName, int num, String tableName, String colName, String tableLit, String pkLit, String fieldLit )
    {
        StringBuffer ret = new StringBuffer();
        ret.append( "\n<select name=\"" );
        ret.append( elementName );
        ret.append( num );
        ret.append( "\">" );
        ret.append( "<option></option>" );
        TableMetaData tableInfo = md.getTableMetaData( tableName );

        if ( tableInfo != null )
        {
            Collection fieldNames = tableInfo.getColumnNames();
            String field;
            Iterator iter2 = fieldNames.iterator();
            /*
             *  and for each field, output an option
             */
            while ( iter2.hasNext() )
            {
                field = ( String ) iter2.next();
                ret.append( "<option value=\"" + tableLit + ":" );
                ret.append( tableInfo.getName() );
                ret.append( "~" + fieldLit + ":" );
                ret.append( field );
                ret.append( "~" + pkLit + ":" + colName );
                ret.append( "\">" );
                ret.append( tableInfo.getName() );
                ret.append( "." );
                ret.append( field );
                ret.append( "</option>" );
            }
        }
        ret.append( "</select>" );
        return ret;
    }


    /**
     *  Description of the Method
     */
    protected static void initColumnNames()
    {
        loadProperties();
        pcl = (
            new PropertyChangeListener()
            {
                public void propertyChange( PropertyChangeEvent evt )
                {
                    loadProperties();
                }
            } );
    }


    /**
     *  Adds a TextNode to a given Element in a DOM
     *
     *@param  ret       The Element to add the TextNode to
     *@param  tag       The tag name of the TextNode
     *@param  value     The text value that goes between the open and close tags
     *@param  document  The feature to be added to the TextNode attribute
     */
    public static void addTextNode( Document document, Element ret, String tag, String value )
    {
        Element typeElement = document.createElement( tag );
        Text typeText = document.createTextNode( value );
        typeElement.appendChild( typeText );
        ret.appendChild( typeElement );
    }


    /**
     *  Loads the column names from columnNames.props. If none exist, no big
     *  deal - just log it so it is eventually caught and fixed.
     */
    protected static void loadProperties()
    {
        try
        {
            columnNames = MiscUtils.loadProperties( Config.getProperty( "columnNamesProps" ) );
        }
        catch ( RuntimeException e )
        {
            columnNames = new Properties();

        }
    }
}

