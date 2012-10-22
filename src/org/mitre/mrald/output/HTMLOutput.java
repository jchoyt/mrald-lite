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

import org.mitre.mrald.util.Config;
import org.mitre.mrald.util.MiscUtils;

/**
 *  This HTMLOutput Class specializes the OutputManager class and formats it for easy viewing in a browser.
 *
 *@author     Brian Blake
 *@created    February 2, 2001
 *@version    1.0
 */

public class HTMLOutput extends OutputManager
{
    /**
     *  Constructor for the HTMLOutput object
     *
     *@since
     */
    public HTMLOutput()
    {
        super();
    }


    /**
     *  This method prepares the output file for the HTML format data
     *
     *@param  msg                         Description of the Parameter
     *@exception  ServletException        Description of the Exception
     *@exception  IOException             Description of the Exception
     *@exception  SQLException            Description of the Exception
     *@exception  OutputManagerException  Description of the Exception
     *@since
     */
    public @Override void printStart()
        throws IOException
    {
        out.print( "<html><head>\n" + Config.getProperty( "CSS" ) + "\n</head><body>\n" );
        out.print("<div id=\"header\"><h1 class=\"headerTitle\">Query Results</h1></div>");
        out.print( "<script language=\"JavaScript1.2\" type=\"text/javascript\" src=\"" );
        out.print( Config.getProperty( "BaseUrl" ) );
        out.println( "/navi.js\"></script>\n" );
        out.println( "<!--\n" );
        super.printQuery();
        out.println(" -->" );
        out.println( "\n<br/><center>" );


    }


    public @Override void printEnd()
    {
        out.println( "</center></body></html>" );
    }

    /**
     *  This method outputs the header so that the file is recognized as an HTML
     *  file
     *
     *@param  msg                         Description of the Parameter
     *@exception  OutputManagerException  Description of the Exception
     *@since
     */
    protected @Override void prepareHeaders()
    {
        msg.setContentType( "text/html" );
        msg.setHeader( "Content-Disposition", "inline;" );

    }


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
    public @Override void printBody( )
        throws IOException, SQLException
    {
        /*
         *  niceNames[] contains the column names, either from the db field name or specified in the HTML form
         */

    	//Print out the query for each query
    	out.println( "<!--\n" );
        super.printQuery();
        out.println(" -->" );

        StringBuffer headerRow = new StringBuffer( "<tr>" );

		String funcName = "";
		if (dbQuery.indexOf("Count")>0){
			funcName = "Count";
        }else if (dbQuery.indexOf("Count(*)")>0){
            funcName = "Count(*)";
		}else if (dbQuery.indexOf("Max")>0){
			funcName = "Max";
		}else if (dbQuery.indexOf("Min")>0){
			funcName = "Min";
		}else {
			funcName = "";
		}

		out.println( "\n<br/><center>" );
        out.print("<table border=\"1\" cellpadding=\"3\"><tbody class=\"output\">");
        for ( int p = 0; p < niceNames.length; p++ )
        {
        	if (niceNames[p].equals("")){
				headerRow.append( "<th>" + funcName + "</th>" );
        	}else {
               headerRow.append( "<th>" + niceNames[p] + "</th>" );
        	}
        }
        headerRow.append( "</tr>" );
        out.println( headerRow.toString() );

        /*
         *  Iterate through the ResultSet and extract the rows
         *  if a custom format exists, use it, otherwise print the column name
         */
        int row_count = 0;
        float fileSize = 0;
        boolean even=false;
        String formattedString;
        while ( ( rs.next() ) &&
                ( ( lineLimitSize == -1 ) || ( row_count < lineLimitSize ) ) &&
                ( ( mbLimitSize == -1 ) || ( fileSize < mbLimitSize ) ) )
        {
            if( even )
            {
                out.print( "<tr class=\"selected\">" );
            }
            else out.print( "<tr>" );
            even = !even;
            for ( int i = 0; i < niceNames.length; i++ )
            {
                /*
                 *  Custom formatting here
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
                out.print( "<td>" + formattedString + "</td>" );
                try
                {
                    fileSize += formattedString.length() + 9;
                    //9 for tags
                }
                catch ( NullPointerException npe )
                {
                    fileSize += 13;
                    //13 for tags and "null"
                }
            }
            // ENDFOR
            out.println( "</tr>" );
            row_count++;
            fileSize += 10;
            //for row tags
            // for very large result sets, break it up in several tables
            if ( row_count / 1000.0 == ( int ) ( row_count / 1000.0 ) )
            {
                out.println( "</tbody></table><table border=\"1\" cellpadding=\"3\"><tbody class=\"output\">" + headerRow.toString() );
            }
        }
        // ENDWHILE
        recordsReturned = row_count;
        bytesReturned = fileSize;
        out.println("</tbody></table></center>");
    }


    /**
     *  Prints the limit and return size data. Both line and size information is
     *  printed.
     *
     *@param  out  the output stream
     *@since       1.2
     */
    public @Override void printLimit( )
    {
        out.println( "<center><div class=\"holder\">" );
        out.print( "There were " + recordsReturned + " records returned.  " );
        if ( lineLimitSize == -1 )
        {
            out.print( "There was no line limit." );
        }
        else
        {
            out.print( "The limit was " + lineLimitSize + " lines." );
        }
        out.print( "  There were approximately " + bytesReturned + " bytes returned.  " );
        if ( mbLimitSize == -1 )
        {
            out.print( "There was no file size limit." );
        }
        else
        {
            out.print( "The limit was " + mbLimitSize + " bytes.  " );
        }
        /*
         *  if (recordsReturned == 0)
         *  {
         *  out.print("\n\nTo see if the appropriate data is loaded, there are links on the ");
         *  out.print("\nMRALD splash page to determine what data is \ncurrently available in MRALD.  ");
         *  out.println("\nTo request to have specific data loaded, \ncontact the MRALD Team at overarching-developer-list@lists.mitre.org.</a>");
         *  }
         */
        out.println( "</div></center>" );
        if ( printQuery )
        {
            printQuery( );
        }
    }


    /**
     *  Prints the Query and formats it
     *
     *@param  out  the output stream
     *@since       1.2
     */
    public @Override void printQuery( )
    {
        out.println( "<br/><br/><center><div class=\"holder\">Query: " );
        out.println( MiscUtils.parseTextToHtml( dbQuery.toString() ) );
        out.println( "</div></center>" );
    }

}

