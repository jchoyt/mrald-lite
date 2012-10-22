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

import org.mitre.mrald.query.*;

import org.mitre.mrald.util.*;
import java.io.*;
import java.util.*;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import org.mitre.mrald.util.Config;
import org.mitre.mrald.util.JdbcTemplates;
import org.mitre.mrald.util.MetaData;
import org.mitre.mrald.util.MraldOutFile;
import org.mitre.mrald.util.OtherDbPropsSubscriber;

/**
 *  This allows for initial MRALD setup, inlcuding
 *  <ul>
 *    <li> setup of Config parameters using the System.Properties file</li>
 *    <li> Initializing the JdbcPropertySubscriber class</li>
 *  </ul>
 *
 *
 *@author     Gail Hamilton
 *@created    May 11th, 2001
 *@version    1.0
 *@see        javax.servlet.http
 */

public class InitServlet extends HttpServlet
{

    /**
     *  Constructor for the PqmServlet object
     *
     *@since
     */
    public InitServlet()
    {
        super();
    }


    /**
     *  Description of the Method
     *
     *@param  config                Description of the Parameter
     *@exception  ServletException  Description of the Exception
     */
    public void init( ServletConfig config )
        throws ServletException
    {
        /*
         *  required for all Servlets
         */
        super.init( config );
        /*
         *  load up Config.java
         */
        String base_path = config.getServletContext().getRealPath( "/" );
        String config_dir = base_path + "WEB-INF/props/";

        Config.init( config_dir );
        Config.setProperty( "BasePath", base_path );

        /*
         *  set up the JdbcPropertySubscriber
         */
        JdbcPropertySubscriber sub = new OtherDbPropsSubscriber();
        /*
         *  Check that hsqldb lock files do not exist - if they do, delete them
         */
        clearHsqldbLckFiles();
        /*
         *  load the metadata asynchronously
         */
        MetaData.reload();

        /*
         * set up the JdbcTemplates
         */
        JdbcTemplates.init( base_path + "/WEB-INF" );
        /*
         *  If this is the first time MRALD has been run, log the install date/time
         *  This is used for creating backups of any changed file.
         */
        File installDT = new File(Config.getProperty( "LOGPATH" ), "installDT.txt");
        if( !installDT.exists() )
        {
            Date nowPlus = new Date( System.currentTimeMillis() + 60*1000*2 ); //two minutes past now to make sure unpacking is done
            DateFormat df = DateFormat.getDateTimeInstance();
            String fomrattedDate = df.format(nowPlus);
            MraldOutFile.appendToFile( "installDT.txt", fomrattedDate );
        }
    }

    /**
     *  Checks all configured databases for hsqldb datasources and ensures there are no lck files
     */
    protected void clearHsqldbLckFiles()
    {
        /*
         *  troll for db props files
         */
        File config_dir = new File( Config.getProperty( "BasePath" ) + "/WEB-INF/props/" );

        File[] files = config_dir.listFiles( new DbPropsFilter() );

        if ( files == null ) {
            //no props files to process
            return ;
        }

        Properties config;

        for ( int i = 0; i < files.length; i++ ) {

            config = new Properties();
            /*
             *  load the props file
             */

            try
            {
                InputStream in = new FileInputStream( files[ i ] );
                config.load( in );
                in.close();
            }
            catch ( IOException e )
            {
                MraldOutFile.logToFile( e );
                //don't want to stop the process on this, though....
            }

            /*
             *  check DBDRIVER and see if the datasource is hsqldb
             */
            String url = Config.replacements( config.getProperty( "DBSERVER", "" ) ).trim();
            if ( url.startsWith( HSQLDB_PREFIX ) )
            {
                /*
                 * this is an in-process hsqldb datasource - we don't want to
                 * mess with server versions, so we need to check for this way
                 * of running hsqldb
                 */
                String dbFileBase = url.substring( HSQLDB_PREFIX.length() );
                File lckFile = new File( dbFileBase + ".lck" );
                if( lckFile.exists() )
                {
                    boolean deleted = lckFile.delete();
                    MraldOutFile.appendToFile( lckFile + " found.  " + (deleted ? "Deleted." : "Delete attempt unsuccessful.") );
                }
            }
        }
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
        throws IOException, ServletException
    {
        doPost( req, resp );
    }


    /**
     *  Description of the Method
     *
     *@param  req                   Description of the Parameter
     *@param  resp                  Description of the Parameter
     *@exception  ServletException  Description of the Exception
     *@exception  IOException       Description of the Exception
     */
    public void doPost( HttpServletRequest req, HttpServletResponse resp )
        throws IOException, ServletException
    {
        try
        {
            String base_path = req.getSession().getServletContext().getRealPath( "/" );
            String config_dir = base_path + "WEB-INF/props/";
            Config.init( config_dir );
            Config.setProperty( "BasePath", base_path );
        }
        catch ( Exception e )
        {
            throw new ServletException( "Config Location Corrupted" );
        }
    }


    public static final String HSQLDB_PREFIX = "jdbc:hsqldb:file:";
}

