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

import org.mitre.mrald.output.HTMLOutput;
import java.io.IOException;
import java.sql.SQLException;
import org.mitre.mrald.util.Config;
import org.mitre.mrald.util.MiscUtils;
import org.mitre.mrald.util.MraldError;

/**
 *  This class does the actual deletes from the database
 *
 *@author     Gail hamilton
 *@created    February 2, 2004
 *@version    1.0
 */

public class DeleteOutput extends HTMLOutput
{

    int deleteCount;
    /**
     *  Constructor for the HTMLOutput object
     *
     *@since
     */
    public DeleteOutput()
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
        out.print("<div id=\"header\"><h1 class=\"headerTitle\">Deletion Results</h1></div>");
        out.print( "<script language=\"JavaScript1.2\" type=\"text/javascript\" src=\"" );
        out.print( Config.getProperty( "BaseUrl" ) );
        out.println( "/navi.js\"></script>\n" );
        out.println( "<!--\n" );
        super.printQuery();
        out.println(" -->" );
        out.println( "\n<br/><center>" );
    }


    /**
     *  This method prepares the output file for the HTML format data
     *
     *@since
     */
    public @Override void printBody( )
        throws IOException, SQLException
    {
        out.print("<th>");
        if( deleteCount==1 )
        {
            out.print("One row has been deleted successfully.");
        }
        else
            out.print(deleteCount + " rows have been deleted successfully.");
        out.println( "</th>");
    }


     /**
     *  This is method connects to the database and runs the specified query
     *
     *@param  msg  Description of Parameter
     *@since       1.2
     */
    public @Override void runUserQuery( )
    {
        try
        {
            StringBuffer logInfo = new StringBuffer();
            long startTime = MiscUtils.logQuery( userID, datasource, dbQuery, logInfo );
            deleteCount = stmt.executeUpdate( dbQuery );
            /*
             *  RETURN success status
             */
            MiscUtils.logQueryRun( startTime, logInfo );
        }
        catch ( SQLException se )
        {
            throw new MraldError( se, msg );
        }
    }

    protected @Override void getFormatInfo(  ) {}

}

