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


/**
 *  Copyright 2006 The MITRE Corporation. ALL RIGHTS RESERVED.
 */
package org.mitre.mrald.output;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.servlet.ServletException;
import org.mitre.mrald.control.MsgObject;
import org.mitre.mrald.output.HTMLOutput;
import org.mitre.mrald.output.OutputManagerException;
import org.mitre.mrald.util.Config;
import org.mitre.mrald.util.MraldOutFile;

/**
 *  Description of the Class
 *
 *@author     ghamilton
 *@created    November 9, 2005
 */
public class CountListOutput extends HTMLOutput
{

    /**
     *  Description of the Field
     */
    public static final String KEY = CountListOutput.class.getName();
    /**
     *  Description of the Field
     */
    public int count = 0;//This must be set before printCount() is called.
                /**
     *  Description of the Field
     */

    //Temp variables

    public static String groupName = "";



    /**
     *  Constructor for the HTMLOutput object
     *
     *@since
     */
    public CountListOutput()
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

    	 StringBuffer output = new StringBuffer();
         int rowCount = 0;
         out.println( "<html><head>" );
         out.println( "<link rel=\"icon\" href=\"favicon.ico\" type=\"image/x-icon\"><link rel=\"shortcut icon\" href=\"favicon.ico\" type=\"image/x-icon\"></head>" );

         while ( rs.next() )
         {

             rowCount++;

         }

        output.append(rowCount);
        out.println( "<input type='text' name='resultslist' value='" + rowCount + "'></input>");
    	out.println("</html>");
        // ENDWHILE
    }


    /**
     *  Prints the Query and formats it
     *
     *@param  out  the output stream
     *@since       1.2
     */
    public @Override void printQuery( )
    {

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
    }


}

