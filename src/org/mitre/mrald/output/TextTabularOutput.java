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
import org.mitre.mrald.util.*;

/**
*  Class to provide output as a series of name: value pairs, each field of the tuple on it's own line.
 */

public class TextTabularOutput extends TextOutput
{
    private final String TAB = "\t";

    public @Override void prepareHeaders()
    {
        String filename_base = msg.getValue("form")[0];
        if (filename_base.equals(Config.EMPTY_STR))
        {
            filename_base = "output";
        }
        formatType = (msg.getValue("Format")[0]);
        if (formatType.equals("fileTabularText"))
        {
            msg.setContentType("application/text");
            msg.setHeader("Content-Disposition",
                     "attachment; filename=" + filename_base + ".txt;");
        }
        else
        {
            msg.setContentType("text/plain");
            msg.setHeader("Content-Disposition", "inline;");
        }
    }

    /**
     *  Cycles through the result set and prints the rows to out.
     *
     */
    public void printBody() throws SQLException, IOException
    {
        /*
         * Iterate through the ResultSet and extract the rows
         * if a custom format exists, use it, otherwise print the column name
         */
        int row_count = 0;
        float fileSize = 0;
        String formattedString;
        while ((rs.next()) && ((lineLimitSize == - 1) || (row_count < lineLimitSize)) && ((mbLimitSize == - 1) || (fileSize < mbLimitSize))) {
            String headerStr = "<------------------------------------------->";
            out.println(headerStr);

            for (int i = 0; i < niceNames.length; i++)
            {
                out.print(niceNames[i] + TAB);
                /*
                 * Custom formatting here
                 */
                if (classNames[i].equals("Timestamp"))
                {
                    formattedString = getAndFormat(rs.getTimestamp(i + 1),
                             formats[i]);

                } else{
                    if (classNames[i].equals("BigDecimal"))
                    {
                        formattedString = getAndFormat(rs.getBigDecimal(i + 1),
                                 formats[i]);

                    }
                    else
                    {
                        formattedString = rs.getString(i + 1);
                    }
                }

                out.println(formattedString);

                try
                {
                    fileSize += formattedString.length() + 1;
                }
                catch (NullPointerException npe)
                {
                    fileSize += 5;
                }
            }
            out.println();
            row_count++;
        }
        recordsReturned = row_count;
        bytesReturned = fileSize;
    }
}

