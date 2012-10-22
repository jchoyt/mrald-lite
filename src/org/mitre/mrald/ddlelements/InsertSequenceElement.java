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

import java.sql.ResultSet;
import java.sql.SQLException;

import org.mitre.mrald.control.MsgObject;
import org.mitre.mrald.parser.MraldParserException;
import org.mitre.mrald.util.Config;
import org.mitre.mrald.util.FormTags;
import org.mitre.mrald.util.MraldConnection;
import org.mitre.mrald.util.MraldError;

/**
 *  Parser Element that is used to build a sequential field in an INSERT SQL statement. The standard tag should be of
 *  the form: <br>
 *  Table:TestTable~Field:SecondField~Order:2~SequenceTable:TestTable~Datasource:main hashCode() is inherited from the
 *  extended classes. DO NOT INSERT.
 *
 * @author     jchoyt
 * @created    November 27, 2002
 */
public class InsertSequenceElement extends InsertElement
{
//    private int hashCache = 0;
    private String datasource = null;
    private static final String QUERY = "select max(<:field:>)+1 from <:table:>";
    private static final String FIELD = "<:field:>";
    private static final String TABLE = "<:table:>";


    /**
     *  Gets the elementType of the InsertElement object
     *
     * @return    The elementType value
     */
    public String getElementType()
    {
        return "InsertSequenceElement";
    }


    /**
     *  Gets the field attribute of the InsertSequenceElement object
     *
     * @return    The field value
     */
    public int getOffset()
    {
        if (!nameValues.getValue("Offset")[0].equals(Config.EMPTY_STR))
        {
            return Integer.parseInt(nameValues.getValue("Offset")[0]);
        }
        return 0;
    }


    /**
     *  Gets the datasource attribute of the InsertSequenceElement object
     *
     * @return    The datasource value
     */
    public String getDatasource()
    {
        if (datasource == null)
        {
            datasource = nameValues.getValue("Datasource")[0];
            if (datasource.equals(Config.EMPTY_STR))
            {
                throw new RuntimeException("A datasource is required in order to get the next sequence number.  Please ask your system admin to make sure your InsertSequenceElement tag has a datasource provided.");
            }
        }
        return datasource;
    }


    /**
     *  Gets the table attribute of the InsertElement object. Overrides the one from DDLElement so we can pick up the
     *  SequenceElement, if present
     *
     * @return    The table value
     */
    public String getSequenceTable()
    {
        if (!nameValues.getValue("SequenceTable")[0].equals(Config.EMPTY_STR))
        {
            return nameValues.getValue("SequenceTable")[0];
        }
        return nameValues.getValue(FormTags.TABLE_TAG)[0];
    }


    /**
     *  Goes to the db and gets the next sequence number and sets the type to "Numeric"
     *
     * @param  msg                       Description of Parameter
     * @param  currentName               The name of the element currently being processed. This was found in the <code>name=""</code>
     *      attribute in the HTML.
     * @return                           The name of the object being processed. It will be removed from the MsgObject
     *      Map of objects to be processed.
     * @exception  MraldParserException  Description of Exception
     */
    public String postProcess(MsgObject msg, String currentName)
        throws MraldParserException
    {
        /*
         *  set the ID number
         */
        String query = QUERY.replaceAll(FIELD, getField());
        query = query.replaceAll(TABLE, getSequenceTable());
        String value = null;
        try
        {
            MraldConnection conn = new MraldConnection( getDatasource(), msg );
            ResultSet rs = conn.executeQuery(query);
            if (rs.next())
            {
                value = rs.getString(1);
                if (value == null || value.equals("null"))
                {
                    value = "1";
                }
                int val = Integer.parseInt(value) + getOffset();

                value = new Integer(val).toString();
                nameValues.setValue(FormTags.VALUE_TAG, value);
            }
            else
            {
                throw new MraldParserException("Couldn't find a new sequence number for the " +
                        getField() + " field of table " + getSequenceTable() +
                        ".  No results from query: " + query);
            }
            rs.close();
            conn.close();
        }
        catch (SQLException e)
        {
            throw new MraldError("Error trying to acquire a new sequence number for the " +
                    getField() + " field of table " + getSequenceTable() +
                    ".  No results from query: " + query, e, msg);
        }
        /*
         *  set the type to Numeric
         */
        nameValues.setValue(FormTags.TYPE_TAG, "Numeric");
        super.postProcess(msg, currentName);
        return currentName;
    }


    /**
     *  Compares this object to another. Note hashCode is overwritten in the parent class.
     *
     * @param  o  Description of the Parameter
     * @return    Description of the Return Value
     */
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o instanceof InsertSequenceElement)
        {
            return (o.hashCode() == hashCode());
        }
        return false;
    }

}

