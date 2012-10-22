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
package org.mitre.mrald.servlets;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mitre.mrald.util.Config;
import org.mitre.mrald.util.MraldError;

/**
 *  Description of the Class
 *
 *@author     jchoyt
 *@created    January 18, 2004
 */
public class ConfigServlet extends HttpServlet
{

    /**
     *  Constructor for the ConfigServlet object
     */
    public ConfigServlet()
    {
        super();
    }


    /**
     *  Description of the Method
     */
    public void destroy()
    {
        super.destroy();
    }


    /**
     *  Description of the Method
     *
     *@param  req                   Description of the Parameter
     *@param  resp                  Description of the Parameter
     *@exception  ServletException  Description of the Exception
     *@exception  IOException       Description of the Exception
     */
    public void doGet( HttpServletRequest req, HttpServletResponse resp )
        throws ServletException, IOException
    {
        doPost( req, resp );
    }


    /**
     *  Description of the Method
     *
     *@param  req   Description of the Parameter
     *@param  resp  Description of the Parameter
     */
    public void doPost( HttpServletRequest req, HttpServletResponse resp )
    {
        try
        {
            String filename = req.getParameter( "filename" );

            if ( filename == null || filename.equals( "" ) )
            {
                filename = Config.PROPS_FILE;
            }

            StringBuilder contents = new StringBuilder();
            String temp;

            int threshold = Integer.parseInt( req.getParameter( "count" ) );

            //
            // Rip the nv pairs out of the ServletRequest
            //
            for ( int i = 0; i < threshold; i++ )
            {
                if ( req.getParameter( "DeleteField" + i ) == null )
                {
                    temp = req.getParameter( "NewFieldName" + i ).trim() + "=" + req.getParameter( "NewFieldValue" + i ).trim() + "\n";
                    contents.append( temp );
                }// if
            }// for

            //
            // Check for any new nv to be added
            //
            for ( int j = 1; j < 5; j++ )
            {
                if ( !req.getParameter( "AddedFieldName" + j ).equals( "" ) )
                {
                    temp = req.getParameter( "AddedFieldName" + j ).trim() + "=" + req.getParameter( "AddedFieldValue" + j ).trim() + "\n";
                    contents.append( temp );
                }// if
            }//for

            //
            // Opening the file for writing
            //
            if ( filename == null || filename.equals( "" ) )
            {
                filename = Config.PROPS_FILE;
            }

            String base_path = req.getSession().getServletContext().getRealPath( "/" );
            File config_file = new File( base_path + "WEB-INF/props/" +filename );

            FileOutputStream fout = new FileOutputStream( config_file );
            PrintStream pout = new PrintStream( fout );

            pout.println( contents.toString() );

            pout.close();
            fout.close();

            //
            // Re-initializing of the Config
            //
            String config_dir = base_path + "WEB-INF/props/";
            Config.init( config_dir );

            //
            // Send redirect to appropriate page
            //
            if ( req.getParameter( "AddMoreTuples" ) != null )
            {
                resp.sendRedirect( Config.getProperty( "BaseUrl" ) + "/AdminConfig.jsp?filename=" + filename );
            }
            else
            {
                resp.sendRedirect( Config.getProperty( "BaseUrl" ) );
            }

        }
        catch ( IOException e )
        {
            throw new MraldError( e );
        }

    }
}

