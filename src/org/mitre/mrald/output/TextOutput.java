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
 *  This TextOutput Class specializes the OutputManager class. It sends delimited text output to
 *  both browser and as a file download.
 *
 * @author     Brian Blake
 * @created    February 2, 2001
 * @version    1.0
 */

public class TextOutput extends OutputManager
{
    String formatType = Config.EMPTY_STR;
    /**
     *  Constructor for the TextOutput object
     *
     * @since
     */
    public TextOutput()
    {
        super();
    }

    /**
     *  This method formats the output in the correct style of text, whether it is CSV, text file, or Browser CSV
     *
     */
    public @Override void printBody(  )
        throws IOException, SQLException
    {
        if ( formatType.equals( "fileCsv" ) )
        {
            printTextResults( msg.getValue( "fileFormatDelimiter" )[0] );
        }
        else if ( formatType.equals( "fileText" ) )
        {
            printTextResults( "\t" );
        }
        else if ( formatType.equals( "browserCsv" ) )
        {
            printTextResults( ( msg.getValue( "browserFormatDelimiter" )[0] ) );
        }
        else
        {    //( formatType.equals( "browserText" ) )
            printTextResults( "\t" );
        }
        out.println();
    }


    /*
     * This method prepares the headers for the file types based on the contents of the MsgObject Instance
     */

    protected @Override void prepareHeaders( )
    {
        String filename_base =  msg.getValue( "form" )[0];
        if( filename_base.equals(Config.EMPTY_STR) )
        {
            filename_base = "output";
        }
        formatType = ( msg.getValue( "Format" )[0] );
        if ( formatType.equals( "fileCsv" ) )
        {
            msg.setContentType( "application/text" );
            msg.setHeader( "Content-Disposition", "attachment; filename=" + filename_base + ".csv;" );
        }
        else if ( formatType.equals( "fileText" ) )
        {
            msg.setContentType( "application/text" );
            msg.setHeader( "Content-Disposition", "attachment; filename=" + filename_base + ".txt;" );
        }
        else
        {
            msg.setContentType( "text/plain" );
            msg.setHeader( "Content-Disposition", "inline;" );
        }
    }

    /**
     *  Cycles through the result set and prints the rows to out.
     *
     */
    private void printTextResults(  String delim )
        throws SQLException, IOException
    {
        /*
         * niceNames[] contains the column names, either from the db field name or specified in the HTML form
         */
        for ( int p = 0; p < niceNames.length; p++ )
        {
            out.print( niceNames[p] );
            if ( p < niceNames.length - 1 )
            {
                out.print( delim );
            }
        }
        out.println();
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
            for ( int i = 0; i < niceNames.length; i++ )
            {
                /*
                 * Custom formatting here
                 */
                if ( classNames[i].equals( "Timestamp" ) )
                {
                    formattedString = getAndFormat( rs.getTimestamp( i + 1 ), formats[i] );
                }
                else if ( classNames[i].equals( "BigDecimal" ) )
                {
                    formattedString = getAndFormat( rs.getBigDecimal( i + 1 ), formats[i] );
                }
                else
                {
                    formattedString = rs.getString( i + 1 );
                }
                out.print( formattedString );
                if ( i < niceNames.length - 1 )
                {
                    out.print( delim );
                }
                try
                {
                    fileSize += formattedString.length() + 1;    //1 for delimiter
                }
                catch ( NullPointerException npe )
                {
                    fileSize += 5;
                }
            }    // ENDFOR
            out.println();
            row_count++;
        }    // ENDWHILE
        recordsReturned = row_count;
        bytesReturned = fileSize;
    }

}

