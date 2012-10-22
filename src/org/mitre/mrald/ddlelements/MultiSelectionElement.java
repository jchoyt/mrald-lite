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

import org.mitre.mrald.util.FormTags;
import org.mitre.mrald.util.MraldException;
/**
 *
 *@author     tcornett
 *@created    July 22, 2004
 */
public class MultiSelectionElement extends InsertElement
{
//    private int hashCache = 0;

    /**
     *  Gets the elementType of the InsertElement object
     *
     *@return    The elementType value
     */
    public String getElementType()
    {
        return "MultiSelectionElement";
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
             *  Checks to see if the first value is empty, if so, dont add it.
         *  ASSU
             */
        String[] valueCheck = nameValues.getValue( FormTags.VALUE_TAG );
        if ( valueCheck[0].equals( "" ) )
            {
                return currentValueList;
            }
            /*
             *  OK, it has a value, so build the String for the field list
         *
         *  Since it may have more than one value, you need to go through the entire
         *  list to format the value
         *
             *  Check if this is a String/VARCHAR type - if so, put in single quotes
             */
            String typeCheck = nameValues.getValue( "Type" )[0];
            String newValue;
        for ( int i = 0; i < valueCheck.length; i++ ) {
            if ( typeCheck.equals( "String" ) )
            {
            newValue = "'" + valueCheck[i] + "'";
            }
            else if ( typeCheck.equals( "Date" ) || typeCheck.equals( "DateTime" ) || typeCheck.equals( "Timestamp" ) )
            {
            //one format is standard - 'yyyy-mm-dd HH:MM:SS' come in as mm/dd/yy
            SimpleDateFormat df = new SimpleDateFormat( "mm/dd/yyyy" );
            Date date = df.parse( valueCheck[i] );
            df.applyPattern( "yyyy-mm-dd" );
            String dateVal = df.format( date );
            newValue = "'" + dateVal + "'";
            }
            else
            {
            newValue = valueCheck[i];
            }
            currentValueList.add( newValue );
            }
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
        if ( o instanceof InsertSequenceElement )
        {
            return ( o.hashCode() == hashCode() );
        }
        return false;
    }

}

