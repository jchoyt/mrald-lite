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
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.mitre.mrald.control.MsgObject;
import org.mitre.mrald.parser.ParserElement;
import org.mitre.mrald.query.FilterElement;
import org.mitre.mrald.util.FormTags;
import org.mitre.mrald.util.MiscUtils;
/**
 * This FilterOutput formats the output into a new
 * Filter Object.
 * This is to be used in the case of the multiQueries
 * as a passthro object
 *
 * @author     Gail Hamilton
 * @created    June 7th, 2001
 * @version    1.0
 */

public class FilterOutput extends OutputManager
{
    /**
     *  Constructor for the HTMLOutput object
     *
     * @since
     */

    ArrayList<ParserElement> filterObjects = new ArrayList<ParserElement>();

    public FilterOutput()
    {
        super();
    }

    /*
     *  This method outputs the headers so that the file is recognized in the proper manner
     */
    protected void prepareHeaders( )
        throws OutputManagerException
    {
        //doesn't really output - gives filters back
        return;
    }

    /**
     *  This method prepares the output data so that it appears as a filter
     *
     */
    public void printBody( )
        throws IOException, SQLException
    {
        /*
         * Iterate through the ResultSet and build
         * new Filter Objects
         * If they have the same name an 'OR' is created
         * If they don't an 'AND' is created
         */
        ArrayList<String> tableColumns = MiscUtils.getTableColumns(msg.getQuery()[0]);
        //Should only ever be empty if the select statement is Select *
        if (tableColumns.size() == 0 )
            {
                ResultSetMetaData rsmd = rs.getMetaData();
                String columnName = null;

                //In this case - Select * from tableName
                //there should only be one table
                ArrayList tables = MiscUtils.getTables(msg.getQuery()[0]);

                for (int i = 1; i < (rsmd.getColumnCount() + 1); i++)
                {
                    columnName = rsmd.getColumnName(i);
                    tableColumns.add( tables.get(0) +"." + columnName );
                }
        }


        //To read more than one line - will need to read into memory
        //Probably a HashMap

        String tableColName = null;
        String colName = null;
        String result = null;
        MsgObject newMsg = null;

        HashMap<Integer,ArrayList<String>> hashResults = new HashMap<Integer,ArrayList<String>>();
        Integer rowNo = null;
        int row = -1;
        ArrayList<String> rowData = null;

        //This is temporary until we have Scrollable Database
        while (rs.next())
        {
            row++;
            rowNo = new Integer( row) ;
            rowData = new ArrayList<String>();

            for (int col = 1; col < (tableColumns.size() + 1 ); col++)
            {
                rowData.add(rs.getString( col));
            }
            hashResults.put(rowNo, rowData);
        }

        //For each column create a new FilterObject
        for (int i =0 ; i < tableColumns.size(); i++)
        {
                newMsg = new MsgObject();
//                int colNo = i + 1;
                //Clear up the name/pair values before creating the next Filter
                //Object
                //There is one Filter Object created for each column of data
                newMsg.clearNvPairs();
                for (int rowNum = 0; rowNum < hashResults.size(); rowNum ++)
                {
                    //If there isn't a value then do not limit on this column
                    rowData = hashResults.get(new Integer(rowNum));
                    //Get the data for this column - colNo  i
                    if ( rowData.get(i)== null) continue;
                    result =rowData.get(i).toString();
                    newMsg.setValue(FormTags.VALUE_TAG, result );
                    //temp - Operator tag for now
                    newMsg.setValue(FormTags.OPERATOR_TAG,"=" );
                }
                tableColName = tableColumns.get(i).toString();
                colName = tableColName.substring(tableColName.indexOf(".") + 1);
                tableColName = tableColName.substring(0, tableColName.indexOf("."));
                newMsg.setValue(FormTags.TABLE_TAG,tableColName );
                newMsg.setValue(FormTags.FIELD_TAG, colName );
                //
                //probably need something here for non-Filter objects
                //e.g. a Range Filter
                filterObjects.add(new FilterElement(newMsg));
        }
        //Clear out the old working objects, and replace with new
        //set of Elements to be used in the next iteration
        msg.setWorkingObjects(filterObjects);

    }

    /**
     *  This method returns the Filter Objects
     */
    public ArrayList getFilters( )
    {
        return filterObjects;
    }
}

