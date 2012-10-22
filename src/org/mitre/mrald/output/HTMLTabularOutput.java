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

import javax.servlet.ServletException;

/**
 *  This HTMLOutput Class specializes the OutputManager class. It specifically
 *  gathers information pertaining to the output columns desired by the user. As
 *  the query is constructed, this class adds output information and updates the
 *  columns and joins necessary to obtain this output information
 *
 *@author     Brian Blake
 *@created    February 2, 2001
 *@version    1.0
 */

public class HTMLTabularOutput extends HTMLOutput {

    /**
     *  Retrieve the contents of the result set and send them to out as the guts
     *  of an HTML table. This includes only the header row with the field names
     *  as column titles and the each query result as table rows. The
     *  <table>
     *    , <HTML>, <body>, <title>and <style>tags are not included.
     *
     *@param  out                   the output stream
     *@exception  ServletException  Description of the Exception
     *@exception  IOException       Description of the Exception
     *@exception  SQLException      Description of the Exception
     *@since                        1.2
     */
    public @Override void printBody() throws IOException,  SQLException {
        /*
         *  niceNames[] contains the column names, either from the db field name or specified in the HTML form
         */
        StringBuffer headerRow = new StringBuffer("<tr>");

        // PM: funcName was written, but never read.
/*        String funcName = "";
        if (dbQuery.indexOf("Count") > 0) {
            funcName = "Count";
        } else{
            if (dbQuery.indexOf("Count(*)") > 0) {
                funcName = "Count(*)";
            } else{
                if (dbQuery.indexOf("Max") > 0) {
                    funcName = "Max";
                } else{
                    if (dbQuery.indexOf("Min") > 0) {
                        funcName = "Min";
                    } else {
                        funcName = "";
                    }
                }
            }
        }*/


        String title = "Results";
        headerRow.append("<th colspan=\"2\">" + title + "</th>");

        headerRow.append("</tr>");
        out.println(headerRow.toString());

        /*
         *  Iterate through the ResultSet and extract the rows
         *  if a custom format exists, use it, otherwise print the column name
         */
        int row_count = 0;
        float fileSize = 0;
        boolean even = false;
        String formattedString;
        String trHeaderStr = "<tr class=\"selected\">";
        while ((rs.next()) && ((lineLimitSize == - 1) || (row_count < lineLimitSize)) && ((mbLimitSize == - 1) || (fileSize < mbLimitSize))) {
            if (even) {

                trHeaderStr = "<tr class=\"selected\"><td><table border=\"0\" cellpadding=\"3\">";
            } else {
                trHeaderStr = "<tr><td><table border=\"0\" cellpadding=\"3\">" ;
            }


            even = ! even;

            out.print(trHeaderStr);

            for (int i = 0; i < niceNames.length; i++) {
                out.print("<tr><td>" + niceNames[i] + "</td>");
                /*
                 *  Custom formatting here
                 */
                if (classNames[i].equals("Timestamp")) {
                    formattedString = getAndFormat(rs.getTimestamp(i + 1),
                             formats[i]);

                } else{
                    if (classNames[i].equals("BigDecimal")) {
                        formattedString = getAndFormat(rs.getBigDecimal(i + 1),
                                 formats[i]);

                    } else {
                        formattedString = rs.getString(i + 1);
                    }
                }

                out.print("<td>" + formattedString + "</td>");
                try {
                    fileSize += formattedString.length() + 9;
                    //9 for tags
                } catch (NullPointerException npe) {
                    fileSize += 13;
                    //13 for tags and "null"
                }
                out.println("</tr>");
            }
            out.println("</table></td></tr>");
            // ENDFOR

            row_count++;
            fileSize += 10;
            //for row tags
            // for very large result sets, break it up in several tables
            if (row_count / 1000.0 == (int) (row_count / 1000.0)) {
                out.println("</table><table border=\"1\" cellpadding=\"3\">" + headerRow.toString());
            }
        }
        // ENDWHILE
        recordsReturned = row_count;
        bytesReturned = fileSize;
    }
}

