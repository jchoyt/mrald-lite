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
package org.mitre.mrald.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;


/**
 *  Description of the Class
 *
 *@author     Jeffrey Hoyt
 *@created    January 2, 2001
 */
public class MraldOutFile
{

    static String fileUrl = Config.getProperty( "LOGFILE" );


    /**
     *  Constructor for the MraldOutFile object
     *
     *@since    1.2
     */
    public MraldOutFile() { }


    /**
     *  Appends the passed String to the file located at
     *  Config.LOGPATH/fileName. If the file does not exist, one will be
     *  created.
     *
     *@param  fileName  Name of the file to be appended to
     *@param  text      Text to be appended
     *@since            1.2
     */
    public static void appendToFile( String fileName, String text )
    {
        PrintWriter out = null;
        String fileUrl = Config.getProperty( "LOGPATH" ) + fileName;
        try
        {
            out = new PrintWriter( new BufferedWriter( new FileWriter( fileUrl, true ) ) );
            out.print( Config.NEWLINE + text );
            out.flush();
            out.close();
        }
        catch ( IOException e )
        {
            System.out.println( MiscUtils.formatThrowable(e) );
            if ( out != null )
            {
                out.close();
            }
            MraldOutFile.createFile( fileName, text );
        }
    }



    /**
     *  Creates a file at Config.LOGPATH/fileName and inserts the passed String.
     *
     *@param  fileName  Name of the file to be appended to
     *@param  text      Text to be appended
     *@since            1.2
     */
    public static synchronized void createFile( String fileName, String text )
    {
        String fileUrl = Config.getProperty( "LOGPATH" ) + fileName;
        PrintWriter writer = null;
        try
        {
            writer = new PrintWriter( new BufferedWriter( new FileWriter( fileUrl, false ) ) );
            writer.print( text );
            writer.flush();
            writer.close();
        }
        catch ( IOException e )
        {
            String mailText = "Couldn't create " + fileUrl +
                "\n\nThe stack trace was printed to System.out in the MRALD install at " +
                Config.getProperty( "BaseUrl" ) +
                ".  The message that was to be conveyed was:\n\n" +
                text;
            System.out.println( mailText );
            e.printStackTrace( System.out );
            try
            {
                Mailer.send( Config.getProperty( "MAILTO" ), "MraldOutFile", Config.getProperty( "SMTPHOST" ), mailText, "Error in MraldOutFile.createFile()" );
            }
            catch ( Exception e2 )
            {
                e2.printStackTrace( System.out );
            }
        }
    }


    public static void logToFile( Throwable t )
    {
        logToFile( MiscUtils.formatThrowable( t ) );
    }


    /**
     *  Description of the Method
     *
     *@param  fileName  Description of the Parameter
     *@param  ste       Description of the Parameter
     */
    public static void logToFile( String fileName, StackTraceElement[] ste )
    {
        String time = Calendar.getInstance().getTime().toString();
        PrintWriter out = null;
        String fileUrl = Config.getProperty( "LOGPATH" ) + fileName;
        try
        {
            out = new PrintWriter( new BufferedWriter( new FileWriter( fileUrl, true ) ) );
            out.print( Config.NEWLINE + time );
            for ( int i = 0; i < ste.length; i++ )
            {
                out.print( Config.NEWLINE + ste[i].toString() );
            }
            out.flush();
            out.close();
        }
        catch ( IOException e )
        {
            try
            {
                out.close();
            }
            catch ( Exception e2 )
            {
            }
            StringBuffer ret = new StringBuffer();
            for ( int i = 0; i < ste.length; i++ )
            {
                ret.append( Config.NEWLINE + ste[i].toString() );
            }
            String text = ret.toString();
            MraldOutFile.createFile( fileName, time + Config.NEWLINE + text );
        }
    }


    /**
     *  Convenience method for appending to the default log file
     *
     *@param  text  Description of the Parameter
     */
    public static void appendToFile( String text )
    {
        appendToFile( Config.getProperty( "LOGFILE" ), text );
    }


    /**
     *  Convenience method for logging (includes timestamp) to the default log
     *  file
     *
     *@param  text  Description of the Parameter
     */
    public static void logToFile( String text )
    {
        logToFile( Config.getProperty( "LOGFILE" ), text );
    }


    /**
     *  Convenience method for logging (includes timestamp) to the default log
     *  file
     *
     *@param  ste  Description of the Parameter
     */
    public static void logToFile( StackTraceElement[] ste )
    {
        logToFile( Config.getProperty( "LOGFILE" ), ste );
    }


    /**
     *  Appends the date/time followed by the passed String to the file located
     *  at Config.LOGPATH/fileName. If the file does not exist, one will be
     *  created.
     *
     *@param  fileName  Name of the file to be appended to
     *@param  text      Text to be appended
     *@since            1.2
     */
    public static void logToFile( String fileName, String text )
    {
        String time = Calendar.getInstance().getTime().toString();
        PrintWriter out = null;
        String fileUrl = Config.getProperty( "LOGPATH" ) + fileName;
        try
        {
            out = new PrintWriter( new BufferedWriter( new FileWriter( fileUrl, true ) ) );
            out.print( Config.NEWLINE + time );
            out.print( Config.NEWLINE + text );
            out.flush();
            out.close();
        }
        catch ( IOException e )
        {
            try
            {
                out.close();
            }
            catch ( Exception e2 )
            {
            }
            MraldOutFile.createFile( fileName, time + Config.NEWLINE + text );
        }
    }
}

