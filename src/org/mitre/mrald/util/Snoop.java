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

import java.util.Enumeration;
import java.util.Iterator;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.mitre.mrald.control.MsgObject;

/**
 *@author     Jeffrey Hoyt
 *@created    January 2, 2001
 */

public class Snoop
{


    /**
     *  Appends the date and all cookie information passed via the
     *  HttpServletRequest to the log file.
     *
     *@param  request  The HttpServletRequest the data comes from.
     *@since           1.2
     */
    public static void logCookies( HttpServletRequest request )
    {
        StringBuilder ret = new StringBuilder();
        ret.append( "Cookies in this request:" );
        Cookie[] cookies = request.getCookies();
        for ( int i = 0; i < cookies.length; i++ )
        {
            Cookie cookie = cookies[i];
            ret.append( Config.NEWLINE );
            ret.append( cookie.getName() );
            ret.append( " = " );
            ret.append( cookie.getValue() );
        }
        MraldOutFile.logToFile( ret.toString() );
    }


    /**
     *  Appends the date and all header information passed via the
     *  HttpServletRequest to the log file.
     *
     *@param  request  The HttpServletRequest the data comes from.
     *@since           1.2
     */
    public static void logHeaders( HttpServletRequest request )
    {
        StringBuilder ret = new StringBuilder();
        ret.append( "Headers in this request:" );
        Enumeration e = request.getHeaderNames();
        while ( e.hasMoreElements() )
        {
            String key = ( String ) e.nextElement();
            String value = request.getHeader( key );
            ret.append( Config.NEWLINE );
            ret.append( key );
            ret.append( ": " );
            ret.append( value );
        }
        MraldOutFile.logToFile( ret.toString() );
    }


    /**
     *  Description of the Method
     *
     *@param  msg  Description of the Parameter
     */
    public static void logParameters( MsgObject msg )
    {
        StringBuilder logInfo = new StringBuilder( "" );
        logInfo.append( "Name/value pairs in this MsgObject:" );
        Iterator e = msg.getNames().iterator();
        try
        {
            while ( e.hasNext() )
            {
                String key = ( String ) e.next();
				if ( key.toLowerCase().indexOf( "password" ) == -1 )
				{
					String[] values = msg.getValue( key );
					logInfo.append( Config.NEWLINE + key + " = " );
					for ( int i = 0; i < values.length; i++ )
					{
						logInfo.append( values[i] + " " );
					}
				}
            }
            logInfo.append( Config.NEWLINE + "User IP Address = " + msg.getUserUrl() );
        }
        catch ( Exception ex )
        {
            throw new MraldError( ex, msg );
        }
        finally
        {
            MraldOutFile.logToFile( logInfo.toString() );
        }
    }



    /**
     *  Description of the Method
     *
     *@param  msg    Description of the Parameter
     *@param  log    Description of the Parameter
     *@deprecated    Use logParameters( MsgObject ) instead.
     */
    public static void logParameters( MsgObject msg, String log )
    {
        logParameters( msg );
    }


    /**
     *  Appends the date and all parameters passed via the HttpServletRequest to
     *  the log file.
     *
     *@param  request  The HttpServletRequest the data comes from.
     *@since           1.2
     */
    public static void logParameters( HttpServletRequest request )
    {
        StringBuilder ret = new StringBuilder();
        ret.append( "Parameter names in this request:" );
        Enumeration e = request.getParameterNames();
        while ( e.hasMoreElements() )
        {
            String key = ( String ) e.nextElement();
			if ( key.toLowerCase().indexOf( "password" ) == -1 )
			{
				String[] values = request.getParameterValues( key );
				ret.append( Config.NEWLINE );
				ret.append( key );
				ret.append( " = " );
				for ( int i = 0; i < values.length; i++ )
				{
					ret.append( values[i] );
					ret.append( " " );
				}
			}
        }
        MraldOutFile.logToFile( ret.toString() );
    }


    /**
     *  Appends the date and time and the user name and IP address to the log
     *  file.
     *
     *@param  request  The HttpServletRequest the data comes from.
     *@since           1.2
     */
    public static void logUserInfo( HttpServletRequest request )
    {
        StringBuilder ret = new StringBuilder();
        ret.append( "Remote User: " + request.getRemoteUser() );
        ret.append( Config.NEWLINE + "User IP address: " + request.getRemoteAddr() );
        MraldOutFile.logToFile( ret.toString() );
    }


    /**
     *  Description of the Method
     *
     *@param  msg  Description of Parameter
     *@since
     */
    public static void logUserInfo( MsgObject msg )
    {
        StringBuilder ret = new StringBuilder();
        //ret.append( msg.getUserId() + Config.NEWLINE );
        ret.append( msg.getUserUrl() + Config.NEWLINE );
        MraldOutFile.logToFile( ret.toString() );
    }
}

