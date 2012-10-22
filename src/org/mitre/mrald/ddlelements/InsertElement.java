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
package org.mitre.mrald.ddlelements;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.mitre.mrald.control.MsgObject;
import org.mitre.mrald.parser.MraldParserException;
import org.mitre.mrald.util.Config;
import org.mitre.mrald.util.FormTags;
import org.mitre.mrald.util.MiscUtils;
import org.mitre.mrald.util.MraldError;
import org.mitre.mrald.util.MraldException;
import org.mitre.mrald.util.MraldOutFile;
/**
 *  Parser Element that is used to build an INSERT SQL statement. The standard
 *  tag should be of the form: <br>
 *  Table:TestTable~Field:SecondField~Value:testing string~Type:String~Order:2
 *  hashCode() is inherited from the extended classes. A comma separated
 *  SqlThread number list is optional if this is to be part of multiple insert
 *  statements.
 *
 *@author     jchoyt
 *@created    November 27, 2002
 */
public class InsertElement extends DdlElement
{
//    private int hashCache = 0;


    /**
     *  Constructor for the InsertElement object
     */
    public InsertElement() { }


    /**
     *  Constructor for the InsertElement object
     *
     *@param  table      Description of the Parameter
     *@param  field      Description of the Parameter
     *@param  type       Description of the Parameter
     *@param  order      Description of the Parameter
     *@param  sqlthread  Description of the Parameter
     *@param  _value     Description of the Parameter
     */
    public InsertElement( String table, String field, String _value, String type, String order, String sqlthread )
    {
        String name = FormTags.TABLE_TAG;
        String value = table;
        nameValues.setValue( name, value );

        name = FormTags.FIELD_TAG;
        value = field;
        nameValues.setValue( name, value );

        name = FormTags.VALUE_TAG;
        value = _value;
        nameValues.setValue( name, value );

        name = FormTags.TYPE_TAG;
        value = type;
        nameValues.setValue( name, value );

        name = FormTags.ORDER_TAG;
        value = order;
        nameValues.setValue( name, value );

        name = FormTags.SQL_THREAD_NUM_TAG;
        value = sqlthread;
        nameValues.setValue( name, value );

    }


    /**
     *  Gets the elementType of the InsertElement object
     *
     *@return    The elementType value
     */
    public String getElementType()
    {
        return "InsertElement";
    }


    /**
     *  Gets the field attribute of the InsertSequenceElement object
     *
     *@return    The field value
     */
    public String getField()
    {
        return nameValues.getValue( FormTags.FIELD_TAG )[0];
    }


    /**
     *  Gets the field attribute of the InsertSequenceElement object
     *
     *@return    The field value
     */
    public String getValue()
    {
        return nameValues.getValue( FormTags.VALUE_TAG )[0];
    }


    /**
     *  Preprocessor - for each SqlThread, insert multiple copies of this
     *  Element, one for each SqlThread.
     *
     *@param  msg                       The workflow level MsgObject
     *@param  currentName               The name of the element currently being
     *      processed. This was found in the <code>name=""</code> attribute in
     *      the HTML.
     *@return                           The name of the element that should be
     *      removed from the main MsgObject Hashmap of elements to process. This
     *      usually is the same as the currentName paramenter passed in.
     *@exception  MraldParserException  Description of Exception
     *@since
     */
    public String postProcess( MsgObject msg, String currentName )
        throws MraldParserException
    {
        /*
         *  if sqlThread is -1, we need toinsert into all tables, so set table
         *  name to !All  (FormTags.INSERT_INTO_ALL_TABLES)
         */
        if ( getSqlThread().equals( "-1" ) )
        {
            nameValues.removeValue( FormTags.TABLE_TAG );
            nameValues.setValue( FormTags.TABLE_TAG, FormTags.INSERT_INTO_ALL_TABLES );
            return currentName;
        }
        //else
        if ( getSqlThread().indexOf( "," ) == -1 )
        {
            //no prob - just move on
            return currentName;
        }
        //else
        setIsActive( false );
        // /*
         // *  need to set the table name to !Multiple (FormTags.INSERT_INTO_MULTIPLE)
         // */
        // nameValues.removeValue( FormTags.TABLE_TAG );
        // nameValues.setValue( FormTags.TABLE_TAG, FormTags.INSERT_INTO_MULTIPLE );
        /*
         *  grab all the values we need to work on
         */
        String[] threads = getSqlThread().split( "," );
        String[] tables = getTable().split( "," );
        String[] fields = getField().split( "," );
        String[] orders = nameValues.getValue( FormTags.ORDER_TAG )[0].split( "," );
        /*
         *  the size of the tables and fields arrays must be 1 or the same as the size of the threads array
         */
        if ( tables.length != 1 && tables.length != threads.length )
        {
            throw new MraldError( "When using an insert element with multiple SqlThreads, the number of " +
                    "tables in the list must either be 1 or the same as the number of threads.  In the element named " +
                    currentName + " the number of threads was " + threads.length +
                    " and the number of tables was " + tables.length + ".", msg );
        }
        if ( fields.length != 1 && fields.length != threads.length )
        {
            throw new MraldError( "When using an insert element with multiple SqlThreads, the number of " +
                    "fields in the list must either be 1 or the same as the number of threads.  In the element named " +
                    currentName + " the number of threads was " + threads.length +
                    " and the number of fields was " + fields.length + ".", msg );
        }
        if ( orders.length != 1 && orders.length != threads.length )
        {
            throw new MraldError( "When using an insert element with multiple SqlThreads, the number of " +
                    "orders in the list must either be 1 or the same as the number of threads.  In the element named " +
                    currentName + " the number of threads was " + threads.length +
                    " and the number of orders was " + orders.length + ".", msg );
        }
        /*
         *  Can now assume it's sturcturally ok, so now process
         */
        String table = nameValues.getValue( FormTags.TABLE_TAG )[0];
        String field = nameValues.getValue( FormTags.FIELD_TAG )[0];
        String _value = nameValues.getValue( FormTags.VALUE_TAG )[0];
        String type = nameValues.getValue( FormTags.TYPE_TAG )[0];
        String order = nameValues.getValue( FormTags.ORDER_TAG )[0];
        String sqlthread = null;
        for ( int i = 0; i < threads.length; i++ )
        {
            sqlthread = threads[i];
            if ( tables.length != 1 )
            {
                table = tables[i];
            }
            if ( fields.length != 1 )
            {
                table = fields[i];
            }
            if ( orders.length != 1 )
            {
                order = orders[i];
            }
            InsertElement ele = new InsertElement( table, field, _value, type, order, sqlthread );
            ele.setIsActive( true );
            msg.getWorkingObjects().add( ele );
        }
        return currentName;
    }


    /**
     *  If appropriate, adds a String to the passed ArrayList. The String added
     *  is an entry in the field list of an INSERT SQL statement.
     *
     *@param  currentFieldList  list of Strings representing the field list of
     *      an INSERT SQL statemnt
     *@return                   potentially modified currentFieldList
     */
    public ArrayList<String> buildFieldList( ArrayList<String> currentFieldList )
    {

        /*
         *  check for a value - of none is given, don't add it to the field list
         */
        String valueCheck = nameValues.getValue( FormTags.VALUE_TAG )[0];
        if ( valueCheck.equals( "" ) )
        {

            return currentFieldList;
        }
        /*
         *  OK, it has a value, so build the String for the field list
         */
        String newValue = nameValues.getValue( FormTags.FIELD_TAG )[0];
        currentFieldList.add( newValue );
        return currentFieldList;
    }

    /**
     *  If appropriate, adds a String to the passed ArrayList. The String added
     *  is an entry in the value list of an INSERT SQL statement.
     *
     *@param  currentValueList    list of Strings representing the value list of
     *      an INSERT SQL statemnt
     *@return                     potentially modified currentValueList
     *@exception  MraldException  Description of the Exception
     */
    public ArrayList<String> buildValueList( ArrayList<String> currentValueList )
        throws MraldException
    {
        try
        {
        	
            /*
             *  check for a value - of none is given, don't add it to the field list
             */
        	 MraldOutFile.logToFile(Config.getProperty("LOGFILE"), "InsertElement : buildValueList element " + this.toString());
             
        	String valueCheck = nameValues.getValue( FormTags.VALUE_TAG )[0];
            
        	MraldOutFile.logToFile(Config.getProperty("LOGFILE"), "Insert Element : element value " + valueCheck );
        	
            //String[] values = nameValues.getValue( FormTags.VALUE_TAG );
            
            String[] values = nameValues.getValue( "" );
            
            MraldOutFile.logToFile(Config.getProperty("LOGFILE"), "Insert Element : no of values " + values.length );
        	
            for (String valueTxt: values)
            {
            	MraldOutFile.logToFile(Config.getProperty("LOGFILE"), "Insert Element : element value is " + valueTxt );
            	
            }
            if ( valueCheck.equals( "" ) )
            {
                return currentValueList;
            }
            /*
             *  OK, it has a value, so build the String for the field list
             *  Check if this is a String/VARCHAR type - if so, put in single quotes
             */
            String typeCheck = nameValues.getValue( "Type" )[0];
            String newValue;
            if ( typeCheck.equals( "String" ) )
            {
                newValue = "'" + MiscUtils.checkApostrophe(valueCheck) + "'";
            }
            else if ( typeCheck.equals( "Date" ) || typeCheck.equals( "DateTime" ) || typeCheck.equals( "Timestamp" ) )
            {
                //one format is standard - 'yyyy-mm-dd HH:MM:SS' come in as mm/dd/yy
                SimpleDateFormat df = new SimpleDateFormat( "mm/dd/yyyy" );
                Date date = df.parse( valueCheck );
                df.applyPattern( "yyyy-mm-dd" );
                String dateVal = df.format( date );
                //newValue = "'" + dateVal + "'";

                newValue =  " to_date('" + dateVal + "', 'YYYY-MM-DD')";


            }
            else
            {
                /* TODO: check that it's actually a number */
                /*try
                {
                    double temp = Double.parseDouble(valueCheck);
                }
                catch( NumberFormatException e)
                {
                    StringBuffer ret = new StringBuffer(  );
                    ret.append( "The field " );
                    ret.append( getField() );
                    ret.append( " requires a numeric value.  You gave it the value " );
                    ret.append( valueCheck );
                    ret.append( ".  Please go back and correct this." );
                    throw new RuntimeException( ret.toString(), e);
                }*/
                newValue = valueCheck;
            }
            MraldOutFile.logToFile(Config.getProperty("LOGFILE"), "Insert Element : element value at end of add " + newValue );
        	
            currentValueList.add( newValue );
            return currentValueList;
        }
        catch ( java.text.ParseException e )
        {
            throw new MraldException( e.getMessage() );
        }
    }


    /**
     *  Compares this object to another. Note hashCode is overwritten in the
     *  parent class.
     *
     *@param  o  Description of the Parameter
     *@return    Description of the Return Value
     */
    public boolean equals( Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( o instanceof InsertElement )
        {
            return ( o.hashCode() == hashCode() );
        }
        return false;
    }

}

