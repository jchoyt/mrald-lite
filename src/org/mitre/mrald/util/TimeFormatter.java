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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *  The Timeformatter is a class that formats the time according to predefined
 *  date formats that user may enter and then returns
 *
 *@author     jchoyt
 *@created    February 7, 2001
 */
public class TimeFormatter
{

    /**
     *  Description of the Field
     */
    public final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
     *  Description of the Field
     */
    public final static String DATE_FORM_FORMAT = "MM/dd/yyyy hh:mm:ss a";
    /**
     *  Description of the Field
     *
     *@since
     */
    public final static String ORACLE_SQL_TIME_FORMAT = "YYYY-MM-DD HH24:MI:SS";
    /**
     *  Description of the Field
     *
     *@since
     */
    public final static String ORACLE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
     *  Description of the Field
     */
    public final static String SYBASE_SQL_TIME_FORMAT = "mon dd yyyy hh:mm AM";

    //Sybase: example '2001-06-01 06:33:54'
    /**
     *  Description of the Field
     */
    public final static String SYBASE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";


    /**
     *  Gets the timeAsString attribute of the TimeFormatter class
     *
     *@param  thisTime  Description of the Parameter
     *@return           The timeAsString value
     */
    public static String getTimeAsString( Calendar thisTime )
    {

        java.text.DateFormat thisDateFormat = new java.text.SimpleDateFormat( DATE_FORMAT );
        return thisDateFormat.format( thisTime.getTime() );
    }


    /**
     *  We want dates of format year-mone-day, optionally with a time, either
     *  hour:minute, or hour:minute:second
     *
     *@param  dateTime  Description of the Parameter
     *@return           The validDateTime value
     */

    public static boolean isValid( String dateTime )
    {
        String[] dateTimeParts = dateTime.split( " " );
        String[] dateParts = dateTimeParts[0].split( "[/-]" );
        if ( dateParts.length != 3 )
        {
            return false;
        }
        if ( !( dateParts[0].matches( "[1-9]|1[012]|0[1-9]" )
                 && dateParts[1].matches( "[1-9]|[0123]\\d" )
                 && dateParts[2].matches( "\\d{2}|\\d{4}" ) )
                 )
        {
            return false;
        }
        String[] timePart = null;
        if ( dateTimeParts.length == 2 )
        {
            timePart = dateTimeParts[1].split( ":" );
            if ( timePart.length < 2 || timePart.length > 3 )
            {
                return false;
            }
        }
        return true;
    }//don't use - use the one in GenerateXmlForm


    /**
     *  Description of the Method
     *
     *@param  date                     The String to be parsed
     *@param  bReturnCal               Whether to return a Calandar object or a
     *      String object. A passed <code>true</code> will send a Calandar
     *      pbject.
     *@return                          Description of the Returned Value
     *@exception  TimeFormatException  Description of Exception
     *@since
     */
    public static Object formatDateTime( String date, boolean bReturnCal )
        throws TimeFormatException
    {
        TimeFormatException thisException = new TimeFormatException( "Invalid date format: " + date + ". Please reenter date.  The expected formats are month/day/year.  If you entered a time, the format should be either hour:minute, or hour:minute:second." );
        date = date.trim();
        boolean validDate = isValid( date );
        if ( !validDate )
        {
            throw thisException;
        }

        String[] stringParts = date.split( "[-/ :]" );
        int[] intParts = new int[stringParts.length];
        for ( int i = 0; i < stringParts.length; i++ )
        {
            intParts[i] = Integer.parseInt( stringParts[i] );
        }
        Calendar cal;
        //NOTE: month is 0-based, so 0 is January.
        switch ( stringParts.length )
        {
            case 3:
                cal = new GregorianCalendar( intParts[2], intParts[0] - 1, intParts[1] );
                break;
            case 5:
                cal = new GregorianCalendar( intParts[2], intParts[0] - 1, intParts[1], intParts[3], intParts[4] );
                break;
            case 6:
                cal = new GregorianCalendar( intParts[2], intParts[0] - 1, intParts[1], intParts[3], intParts[4], intParts[5] );
                break;
            default:
                throw thisException;
        }
        if ( bReturnCal )
        {
            return cal;
        }
        else
        {
            return formatPattern( cal );
        }
    }


    /**
     *  Description of the Method
     *
     *@param  cal                      Description of the Parameter
     *@return                          Description of the Returned Value
     *@exception  TimeFormatException  Description of Exception
     *@since
     */
    /*
     *  public static Object formatTime( String date, boolean bReturnCal )
     *  throws TimeFormatException
     *  {
     *  /Locale currentLocale = new Locale( "en", "US" );
     *  Object dateFormat = null;
     *  String[] timePatterns = {
     *  "hh:mm:ss",
     *  "hh:mm",
     *  "hh"
     *  };
     *  for ( int k = 0; k < timePatterns.length; k++ )
     *  {
     *  try
     *  {
     *  dateFormat = formatPattern( timePatterns[k], date.trim(), bReturnCal );
     *  if ( dateFormat != null )
     *  {
     *  return dateFormat;
     *  }
     *  }
     *  catch ( TimeFormatException e )
     *  {
     *  throw e;
     *  }
     *  }
     *  /  If it makes it to here there was a problem - throw error
     *  TimeFormatException thisException = new TimeFormatException( "Invalid date format : " + date + ". Please reenter date." );
     *  throw thisException;
     *  }
     */
    /**
     *  Output to the format expected by Oracle <br>
     *  TODO: output to format expected by database!!!!!!!!!
     *
     *@param  cal                      Description of the Parameter
     *@return                          Description of the Return Value
     *@exception  TimeFormatException  Description of the Exception
     */
    private static String formatPattern( Calendar cal )
        throws TimeFormatException
    {
        SimpleDateFormat formatter = new SimpleDateFormat();
        // if ( Config.getProperty( "DBDRIVER" ).startsWith( "com.sybase" ) )
        // {
            // formatter.applyPattern( SYBASE_TIME_FORMAT );
        // }
        // else
        // {
            formatter.applyPattern( ORACLE_TIME_FORMAT );
        // }
        return formatter.format( cal.getTime() );
    }
}

