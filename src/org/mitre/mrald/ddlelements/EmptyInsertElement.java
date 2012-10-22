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

import org.mitre.mrald.control.MsgObject;
import org.mitre.mrald.parser.MraldParserException;
import org.mitre.mrald.util.MraldException;
/**
 *  An InsertElement used to force the creation of a DDL statement. It doesn't
 *  add anything to a SQL statement, but rather acts as a placeholder to force
 *  building a query that wouldn't otherwise be built. <br>
 *  <br>
 *  Why is this needed? Becuase if all the other normal (meaning they all have
 *  normal SqlThread numbers, not -1) InsertElements were discarded from a
 *  SqlThread, the query won't be built. Using this element forces a query to be
 *  built because it has a distinct SqlThread number, but doesn't add anything
 *  to the DDL statement, so the resulting INSERT staement will be made up only
 *  of InsertElements with a SqlThread of -1. <br>
 *  <br>
 *  The standard tag should be of the form: <br>
 *  Table:TestTable~Order:4~SqlThread:2
 *
 *@author     jchoyt
 *@created    November 27, 2002
 */
public class EmptyInsertElement extends InsertElement
{
//    private int hashCache = 0;


    /**
     *  Constructor for the EmptyInsertElement object
     */
    public EmptyInsertElement() { }


    /**
     *  Gets the elementType of the EmptyInsertElement object
     *
     *@return    The elementType value
     */
    public String getElementType()
    {
        return "EmptyInsertElement";
    }


    /**
     *  Gets the field attribute of the InsertSequenceElement object
     *
     *@return    The field value
     */
    public String getField()
    {
        return "";
    }


    /**
     *  Gets the field attribute of the InsertSequenceElement object
     *
     *@return    The field value
     */
    public String getValue()
    {
        return "";
    }


    /**
     *  this should always be active
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
        setIsActive( true );
        return currentName;
    }


    /**
     *  Doesn't do anything other than play nice in the architecture
     *
     *@param  currentFieldList  list of Strings representing the field list of
     *      an INSERT SQL statemnt
     *@return                   potentially modified currentFieldList
     */
    public ArrayList<String> buildFieldList( ArrayList<String> currentFieldList )
    {
        return currentFieldList;
    }


    /**
     *  Doesn't do anything other than play nice in the architecture *
     *
     *@param  currentValueList    list of Strings representing the value list of
     *      an INSERT SQL statemnt
     *@return                     potentially modified currentValueList
     *@exception  MraldException  Description of the Exception
     */
    public ArrayList<String> buildValueList( ArrayList<String> currentValueList )
        throws MraldException
    {
        return currentValueList;
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
        if ( o instanceof EmptyInsertElement )
        {
            return ( o.hashCode() == hashCode() );
        }
        return false;
    }

}

