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

import java.util.ArrayList;

import org.mitre.mrald.parser.ParserElement;
import org.mitre.mrald.util.FormTags;
import org.mitre.mrald.util.MraldException;


/**
 *  Parser Element that adds
 *
 *@author     jchoyt
 *@created    November 27, 2002
 */
public abstract class DdlElement extends ParserElement
{
    private int hashCache = 0;
    private String table = null;


    /**
     *  Gets the elementType attribute of the InsertElement object
     *
     *@return    The elementType value
     */
    public abstract String getElementType();


    /**
     *  Gets the order attribute of the DdlElement object
     *
     *@return    The order value
     */
    public String getOrder()
    {
        return nameValues.getValue( FormTags.ORDER_TAG )[0];
    }


    /**
     *  Gets the table attribute of the InsertElement object
     *
     *@return    The table value
     */
    public String getTable()
    {
        if ( table == null )
        {
            /*
             *  some databases allow spaces in a table name.
             *  Check here and put double quotes (") around any table name
             *  Check to make sure the quotes aren't already there, as well
             */
            table = "";
            /*
             *  However, we first have to take off the double quotes put in
             *  by the ParserElement when it was processed
             */
            String value = nameValues.getValue( FormTags.TABLE_TAG )[0];

	    if (value == null || value.equals(""))
		    return "";

            if ( value.charAt( 0 ) == '"' && value.charAt( value.length() - 1 ) == '"' )
            {
                value = value.substring( 1, value.length() - 1 );
            }

            /*
             *  Now we can split the list and add quotes where necessary
             */
            String[] tables = value.split( "," );
            for ( int i = 0; i < tables.length; i++ )
            {
                tables[i] = tables[i].trim();
                if ( tables[i].indexOf( ' ' ) > -1 )
                {
                    table = table + "\"" + tables[i] + "\"";
                }
                else
                {
                    table = table + tables[i];
                }
                if ( i < tables.length - 1 )
                {
                    table = table + ",";
                }
            }
        }
        return table;
    }



    /**
     *  Gets the table attribute of the InsertElement object
     *
     *@return    The table value
     */
    public String getSqlThread()
    {
        return nameValues.getValue( FormTags.SQL_THREAD_NUM_TAG )[0];
    }


    /**
     *  Adds a String to the ArrayList passed to it - this String represents the
     *  Field field name in the nameValues object. Nothing should be done if no
     *  Value is given. <br>
     *  TODO: remove the ~1 after the InsertBuilder is built.
     *
     *@param  currentFieldList  Description of the Parameter
     *@return                   Description of the Return Value
     */
    public abstract ArrayList buildFieldList( ArrayList<String> currentFieldList );


    //inherit nameValues from ParserElement
    /**
     *  Adds a String to the ArrayList passed to it - this String represents the
     *  Value value in the nameValues object. Nothing should be done if no Value
     *  is given. <br>
     *  TODO: remove the ~1 after the InsertBuilder is built.
     *
     *@param  currentValueList    Description of the Parameter
     *@return                     Description of the Return Value
     *@exception  MraldException  Description of the Exception
     */
    public abstract ArrayList buildValueList( ArrayList<String> currentValueList )
        throws MraldException;


    /**
     *  Equals method - should be of the form:<br>
     *  if ( this == o )<br>
     *  {<br>
     *  return true;<br>
     *  }<br>
     *  if ( o instanceof DdlElement )<br>
     *  {<br>
     *  return ( o.hashCode() == hashCode() );<br>
     *  }<br>
     *  return false;<br>
     *
     *
     *@param  o  Description of the Parameter
     *@return    Description of the Return Value
     */
    public abstract boolean equals( Object o );


    /**
     *  Description of the Method
     *
     *@return    Description of the Return Value
     */
    public int hashCode()
    {
        if ( hashCache == 0 )
        {
            hashCache =
                getElementType().hashCode() ^
                getTable().hashCode() ^
//		    getSqlThread().hashCode() ^
                nameValues.getValue( FormTags.ORDER_TAG )[0].hashCode();
        }
        return hashCache;
    }
}

