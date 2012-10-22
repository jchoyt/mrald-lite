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
import java.sql.SQLException;
import org.mitre.mrald.util.Config;
/**
 *  This ExcelOutput Class specializes the OutputManager class. It specifically
 *  gathers information pertaining to the output columns desired by the user.
 *  As the query is constructed, this class adds output information and updates
 *  the columns and joins necessary to obtain this output information
 *
 * @author     Brian Blake
 * @created    February 2, 2001
 * @version    1.0
 */

public class ExcelOutput extends OutputManager
{
    public @Override void printStart()
        throws IOException
    {
        out.println( "<table>" );
    }

    public @Override void printEnd()
    {
        out.println( "</table>" );
    }

    /**
     * This method outputs the headers so that the file is recongnized as an Excel file
     */
    protected void prepareHeaders( )
    {
        String filename_base =  msg.getValue( "form" )[0];
        if( filename_base.equals(Config.EMPTY_STR) )
        {
            filename_base = "output";
        }
        msg.setContentType( "application/msexcel" );
        msg.setHeader( "Content-Disposition", "attachment; filename=" + filename_base + ".xls;" );
    }

    /**
     *  Retrieve the contents of the result set and send them to out as the
     *  guts of an HTML table. This includes only the header row with the field
     *  names as column titles and the each query result as table rows. The
     *
     *  <table>
     *    , <HTML>, <body>, <title>and <style>tags are not included.
     *
     * @param  out                   the output stream
     */
    public @Override void printBody(  )
        throws IOException, SQLException
    {
        out.println( "<tr>" );
        /*
         * niceNames[] contains the column names, either from the db field name or specified in the HTML form
         */
        for ( int p = 0; p < niceNames.length; p++ )
        {
            out.println( "<th>" + niceNames[p] + "</th>" );
        }
        out.println( "</tr>" );
        /*
         * Iterate through the ResultSet and extract the rows
         * if a custom format exists, use it, otherwise print the column name
         */
        int row_count = 0;
        float fileSize = 0;
        String formattedString;
        while ( ( rs.next() ) &&
                ( ( lineLimitSize == -1 ) || ( row_count < lineLimitSize ) ) &&
                ( ( mbLimitSize == -1 ) || ( fileSize < mbLimitSize ) ) )
        {
            out.print( "<tr>" );
            for ( int i = 0; i < niceNames.length; i++ )
            {
                formattedString = rs.getString( i + 1 );
                out.print( "<td>" + formattedString + "</td>" );
                try
                {
                    fileSize += formattedString.length() + 9;    //9 for tags
                }
                catch ( NullPointerException npe )
                {
                    fileSize += 13;    //13 for tags and "null"
                }
            }    // ENDFOR
            out.println( "</tr>" );
            row_count++;
            fileSize += 10;    //for row tags
        }    // ENDWHILE
        recordsReturned = row_count;
        bytesReturned = fileSize;
    }


    public @Override void printLimit()
        throws IOException
    {
        out.print("<tr><td>");
        super.printLimit();
        out.print("<\td><\tr>");
    }
}

