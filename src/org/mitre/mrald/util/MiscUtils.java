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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;
import java.util.StringTokenizer;

import org.mitre.mrald.control.MsgObject;
import org.mitre.mrald.control.MsgObjectException;
//import org.mitre.mrald.util.MraldOutFile;

/**
 *  This object will contain static methods that are MiscUtils to SQL objects
 *
 *@author     Gail Hamilton
 *@created    May 31st, 2001
 */

public class MiscUtils
{
    /**
     *  Description of the Field
     */
//    private static String COOKIE_TAG;


    /**
     *  Constructor for the MiscUtils object
     */
    public MiscUtils()
    {
    	/* Code appears to do nothing. (PM)
        if ( Config.getProperty( "cookietag" ) == null )
        {
            COOKIE_TAG = "mralduser";
        }
        else
        {
            COOKIE_TAG = Config.getProperty( "cookietag" );
        }*/
    }


    /**
     *@param  sSql  Description of Parameter
     *@return       The TableColumns value
     *@since        1.2
     */
    public static ArrayList<String> getTableColumns( String sSql )
    {

        ArrayList<String> tableColumns = new ArrayList<String>();

        if ( sSql.indexOf( "*" ) > 0 )
        {
            return tableColumns;
        }

        sSql = sSql.toUpperCase();
        int selectIndex = sSql.indexOf( "SELECT" );
        int distinctIndex = sSql.indexOf( "DISTINCT" );
        if ( distinctIndex > selectIndex )
        {
            selectIndex = distinctIndex + 2;
        }

        int fromIndex = sSql.indexOf( "FROM" );
        int spacePos = 0;
        StringTokenizer sParse = new StringTokenizer( sSql.substring( selectIndex + 6, fromIndex ), "," );

        while ( sParse.hasMoreTokens() )
        {
            String columnSelect = sParse.nextElement().toString().trim();
            spacePos = columnSelect.indexOf( " " );
            if ( spacePos > 0 )
            {
                columnSelect = columnSelect.substring( 0, spacePos );
            }
            tableColumns.add( columnSelect );
        }

        return tableColumns;
    }



    /**
     *@param  sSql         Description of Parameter
     *@param  includeSyns  Description of the Parameter
     *@return              The Tables value
     *@since               1.2
     */
    public static ArrayList<String> getTables( String sSql, boolean includeSyns )
    {

        ArrayList<String> tables = new ArrayList<String>();
        sSql = sSql.toUpperCase();
        int fromIndex = sSql.indexOf( "FROM" );
        int whereIndex = sSql.indexOf( "WHERE" );
        if ( !( whereIndex > 0 ) )
        {
            whereIndex = sSql.length();
        }
        int spacePos = 0;
        StringTokenizer sParse = new StringTokenizer( sSql.substring( fromIndex + 5, whereIndex ), "," );

        while ( sParse.hasMoreTokens() )
        {
            String tableSelect = sParse.nextElement().toString().trim();
            if ( !includeSyns )
            {
                spacePos = tableSelect.indexOf( " " );
                if ( spacePos > 0 )
                {
                    tableSelect = tableSelect.substring( 0, spacePos );
                }
            }
            tables.add( tableSelect );
        }

        return tables;
    }


    /**
     *@param  sSql  Description of Parameter
     *@return       The Tables value
     *@since        1.2
     */
    public static ArrayList<String> getTables( String sSql )
    {

        ArrayList<String> tables = new ArrayList<String>();
        sSql = sSql.toUpperCase();
        int fromIndex = sSql.indexOf( "FROM" );
        int whereIndex = sSql.indexOf( "WHERE" );
        if ( !( whereIndex > 0 ) )
        {
            whereIndex = sSql.length();
        }
        int spacePos = 0;
        StringTokenizer sParse = new StringTokenizer( sSql.substring( fromIndex + 5, whereIndex ), "," );

        while ( sParse.hasMoreTokens() )
        {
            String tableSelect = sParse.nextElement().toString().trim();
            spacePos = tableSelect.indexOf( " " );
            if ( spacePos > 0 )
            {
                tableSelect = tableSelect.substring( 0, spacePos );
            }
            tables.add( tableSelect );
        }

        return tables;
    }


    /**
     *  Given a Collection object and a StringBuffer, iterates through the
     *  Collection and appends each Object's toString() value to the
     *  StringBuffer.
     *
     *@param  list  Collection containing the information to be extracted
     *@param  ret   StringBuffer to append the values to
     */
    public static void appendCollectionContents( Collection list, StringBuffer ret )
    {
        if ( list.size() == 0 )
        {
            ret.append( " none." );
        }
        else
        {
            Iterator iter = list.iterator();
            while ( iter.hasNext() )
            {
                ret.append( "\n\t" + iter.next().toString() );
            }
        }
    }


    /**
     *  Description of the Method
     *
     *@param  msg                     Description of Parameter
     *@return                         Description of the Returned Value
     *@exception  MsgObjectException  Description of Exception
     *@since
     */
    public static MsgObject groupNVPairs( MsgObject msg )
        throws MsgObjectException
    {
        String currentName;
        Iterator nvNames = msg.getNames().iterator();
        MsgObject returnObject = new MsgObject();
        /*
         *  For each name, append the pairs to build one long value, properly structured
         */
        int outputFormats = 0;
        while ( nvNames.hasNext() )
        {
            currentName = ( String ) nvNames.next();
            if ( currentName.toLowerCase().startsWith( FormTags.OUTPUT_TAG ) )
            {
                String[] values = msg.getValue( currentName );
                StringBuffer ret = new StringBuffer( values[0] );
                for ( int i = 1; i < values.length; i++ )
                {
                    ret.append( "~" + values[i] );
                }
                returnObject.setValue( Integer.toString( outputFormats ), ret.toString() );
                outputFormats++;
            }
        }
        return returnObject;
    }


    /**
     *  Description of the Method
     *
     *@param  msg   Description of the Parameter
     *@param  e     Description of the Parameter
     *@param  html  Description of the Parameter
     */
    public static void handleException( MsgObject msg, Exception e, boolean html )
    {
        //  Capture the stack in the log file
        MraldOutFile.logToFile( e );
        try
        {
            msg.SetOutPrintWriter();
        }
        catch ( MsgObjectException moe )
        {
            MraldOutFile.logToFile( moe );
        }
        finally
        {
            PrintWriter out = msg.getOutPrintWriter();
            if ( html )
            {
                out.println( "<HTML>" + Config.getProperty( "CSS" ) + "<BODY>" );
                out.println( "<div class=holder>" );
                out.println( "<p>There has been a problem processing your request." );
                SimpleDateFormat formatter = new SimpleDateFormat( "M/d/yy 'at' h:mm" );
                Date currentTime_1 = new Date();
                out.println( "<br>at: " + formatter.format( currentTime_1 ) + "<br>" );
                out.println( e.getMessage() );
                out.println( "<p>Please <a href='mailTo: " +
                    Config.getProperty( "MAILTO" ) + "'>report" + "</a> this to us and " +
                    "we will correct it as soon as possible.  Please include both the " +
                    "time and message above in your report." );
                out.println( "</div>" );
                out.println( "<!-- " + MiscUtils.formatThrowable(e) + "-->" );
                out.println( "</BODY></HTML>" );
                out.close();
            }
            else
            {
                if ( out != null )
                {
                    out.println( "There has been a problem processing your request." );
                    SimpleDateFormat formatter = new SimpleDateFormat( "M/d/yy 'at' h:mm" );
                    Date currentTime_1 = new Date();
                    out.println( "at: " + formatter.format( currentTime_1 ) );
                    out.println( e.getMessage() );
                    out.println( "Please report this to " + Config.getProperty( "MAILTO" ) +
                        " to us and we will correct it as soon as possible. " +
                        " Please include both the time and message above in your report." );
                    out.close();
                }
            }
        }
    }


    /**
     *  Creates a new Properties object from the properties file located at the
     *  argument location
     *
     *@param  propsFile  location of the properties file
     *@return            New properties object created from the file located at
     *      the argument location
     */
    public static Properties loadProperties( String propsFile )
    {
        Properties ret = new Properties();
        try
        {
            File test = new File( propsFile );
            if ( !test.exists() )
            {
                RuntimeException re = new RuntimeException( "Could not find the properties file: " + propsFile );
                throw re;
            }
            InputStream in = new FileInputStream( propsFile );
            ret.load( in );
        }
        catch ( IOException e )
        {
            RuntimeException re = new RuntimeException( "Could not find the properties file: " + propsFile, e );
            throw re;
        }
        return ret;
    }


    /**
     *  log query
     *
     *@param  userId   Description of the Parameter
     *@param  query    Description of the Parameter
     *@param  logInfo  Description of the Parameter
     *@return          start time
     */

    public static long logQuery( String userId, String datasource, String query, StringBuffer logInfo )
    {
        Date date = Calendar.getInstance().getTime();
        logInfo.append( "User: " + userId + "; Start Time: " + date +
            "; Datasource: " + datasource + "; Query: " + query + ";" );
        MraldOutFile.appendToFile( Config.getProperty( "LOGFILE" ),
            logInfo.toString() );
        return date.getTime();
    }


    /**
     *  log query run
     *
     *@param  startTime  Description of the Parameter
     *@param  logInfo    Description of the Parameter
     *@return            runTime
     */

    public static long logQueryRun( long startTime, StringBuffer logInfo )
    {
        long dbTime = System.currentTimeMillis() - startTime;
        logInfo.append( " Execute time: " + dbTime / 1000.0 + " s." + ";" );
        MraldOutFile.appendToFile( Config.getProperty( "DBLOGFILE" ),
            logInfo.toString() );
        return dbTime;
    }


    /**
     *  log workflow
     *
     *@param  userId    Description of the Parameter
     *@param  workflow  Description of the Parameter
     */

    public static void logWorkFlow( String userId, String workflow )
    {
        MraldOutFile.appendToFile( Config.getProperty( "LOGFILE" ),
            "User: " + userId + "; Workflow: " + workflow + "; " +
            Calendar.getInstance().getTime() + ";" );
    }


    /**
     *  Description of the Method
     *
     *@param  input  Description of Parameter
     *@return        Description of the Returned Value
     *@since
     */
    public static String parseHtmlToText( String input )
    {
        input = input.replaceAll( "<br>", Config.NEWLINE );
        input = input.replaceAll( "&#39", "'" );
        return input;
    }


    /**
     *  Description of the Method
     *
     *@param  parseString  Description of Parameter
     *@return              Description of the Returned Value
     *@since
     */
    public static MsgObject parseNVPairs( String parseString )
    {
        MsgObject nameValues = new MsgObject();
        StringTokenizer valueTokens = new StringTokenizer( parseString, FormTags.TOKENIZER_STR );

        /*
         *  Parse out the ~ and loop
         */
        int split;
        String pair;
        String name;
        String value;
        while ( valueTokens.hasMoreTokens() )
        {
            pair = valueTokens.nextToken();
            split = pair.indexOf( FormTags.NAMEVALUE_TOKEN_STR );
            name = pair.substring( 0, split );
            value = pair.substring( split + FormTags.NAMEVALUE_TOKEN_STR.length(), pair.length() );
            nameValues.setValue( name, value );
        }
        //org.mitre.mrald.util.Snoop.logParameters(nameValues);
        return nameValues;
    }


    /**
     *  Removes the actual table name when there is a synonym given. This method
     *  keeps the schema name, if there is one attached.
     *
     *@param  table  Description of Parameter
     *@return        Description of the Returned Value
     */
    public static String parseSynonym( String table )
    {
        String schemaPart = "";
        String tableName = "";
        /*
         *  table could have a schema.table, so parse out that first
         */
        StringTokenizer tableNameTokens = new StringTokenizer( table, "." );
        if ( tableNameTokens.countTokens() > 1 )
        {
            schemaPart = tableNameTokens.nextToken() + ".";
        }
        /*
         *  now get only the synonym
         */
        tableNameTokens = new StringTokenizer( table, " " );
        tableName = tableNameTokens.nextToken();
        if ( tableNameTokens.hasMoreTokens() )
        {
            tableName = tableNameTokens.nextToken();
        }
        /*
         *  return the schemaPart+tableName
         */
        return schemaPart + tableName;
    }


    /**
     *  Description of the Method
     *
     *@param  input  Description of Parameter
     *@return        Description of the Returned Value
     *@since
     */
    public static String parseTextToHtml( String input )
    {
        input = input.replaceAll( "&", "&amp;" );
        input = input.replaceAll( "'", "&#39" );
        input = input.replaceAll( "<", "&lt;" );
        input = input.replaceAll( ">", "&gt;" );
        input = input.replaceAll( "\r\n", "<br>" );
        input = input.replaceAll( "\n\r", "<br>" );
        input = input.replaceAll( "\r", "<br>" );
        input = input.replaceAll( "\n", "<br>" );
        return input;
    }


    /**
     *  Description of the Method
     *
     *@param  input  Description of Parameter
     *@return        Description of the Returned Value
     *@since
     */
    public static String parseTextToXml( String input )
    {
        input = input.replaceAll( "<", "&lt;" );
        input = input.replaceAll( ">", "&gt;" );
        input = input.replaceAll( "&", "&amp;" );
        return input;
    }


    /**
     *  Description of the Method
     *
     *@param  input        Description of Parameter
     *@param  target       Description of Parameter
     *@param  replacement  Description of Parameter
     *@return              Description of the Returned Value
     *@since
     */
    public static String replace( String input, String target, String replacement )
    {

        String upperCaseContent;
        int last = 0;
        String returnString = "";
        String currentParagraph;
        target = target.toUpperCase();
        int len = target.length();
        while ( input.length() > 0 )
        {
            upperCaseContent = input.toUpperCase();
            last = upperCaseContent.indexOf( target );
            currentParagraph = "";
            if ( last == -1 )
            {
                currentParagraph = input.substring( 0, input.length() );
                returnString += currentParagraph;
                input = "";
            }
            else
            {
                currentParagraph = input.substring( 0, last );
                returnString += currentParagraph + replacement;
                input = input.substring( last + len );
            }
        }
        return returnString;
    }


    /**
     *  Description of the Method
     *
     *@param  input        Description of Parameter
     *@param  target       Description of Parameter
     *@param  replacement  Description of Parameter
     *@return              Description of the Returned Value
     *@since
     */
    public static String replace( String input, String target, int replacement )
    {
        String upperCaseContent;
        int last = 0;
        String returnString = "";
        String currentParagraph;
        target = target.toUpperCase();
        int len = target.length();

        while ( input.length() > 0 )
        {
            upperCaseContent = input.toUpperCase();
            last = upperCaseContent.indexOf( target );
            currentParagraph = "";

            if ( last == -1 )
            {
                currentParagraph = input.substring( 0, input.length() );
                returnString += currentParagraph;
                input = "";
            }
            else
            {
                currentParagraph = input.substring( 0, last );
                returnString += currentParagraph + replacement;
                input = input.substring( last + len );
            }
        }
        return returnString;
    }


    /**
     *  Checks to see if "'" exists, if it does then checks to see if a current
     *  "\" exists. If it doesn't then applies the "\" before the "'"
     *
     *@param  input  text to be changed
     *@return        Description of the Returned Value
     *@since
     */
    public static String checkApostrophe( String input )
    {
        if( input == null )
        {
            return Config.EMPTY_STR;
        }
        return input.replaceAll( "'", "''" );
    }

    /**
     * @param filename The name of a file.
     * @param suffix A suffix to append to the filename (including the .)
     * @return filenamesuffix.
     */
    public static String appendSuffix(String filename, String suffix) {
    	return filename + suffix;
    }

    /**
     * @param filename The name of a file.
     * @param suffix A suffix (including the .) to insert before the last .
     * @return If the filename is of the form foo.bar: foosuffix.bar;
     * in the presence of multiple .s, the final . is used to split.
     */
    public static  String insertSuffix(String filename, String suffix) {
    	if (filename.lastIndexOf('.') >= 0)
    		return filename.substring(0, filename.lastIndexOf('.')) + suffix + filename.substring(filename.lastIndexOf('.'));
    	else return appendSuffix(filename, suffix);
    }

    /**
     * @param filename The name of a file.
     * @param suffix A suffix (including the .) to insert before the last .
     * @return If the filename is of the form foo.bar: foosuffix.bar;
     * in the presence of multiple .s, the final . is used to split.
     */
    public static  String insertSuffixInFileName(String filename, String suffix) {

    	int dotPos = filename.lastIndexOf('.');
    	if ( dotPos>= 0)
    	{
    		int fileSepPos = filename.lastIndexOf(Config.FILE_SEPARATOR);
    		if (fileSepPos > dotPos) //The dot is in the directory - not the filename
    			return appendSuffix(filename, suffix);
    		else
    			return filename.substring(0, filename.lastIndexOf('.')) + suffix + filename.substring(filename.lastIndexOf('.'));
    	}
    	else return appendSuffix(filename, suffix);
    }
    /**
     * @param filename The name of a file.
     * @param suffix A suffix (including the .) to insert before the first .
     * @return If the filename is of the form foo.bar: foosuffix.bar;
     * in the presence of multiple .s, the first . is used to split.
     */
    public static  String prependSuffix(String filename, String suffix) {
    	if (filename.indexOf('.') >= 0)
    		return filename.substring(0, filename.indexOf('.')) + suffix + filename.substring(filename.indexOf('.'));
    	else return appendSuffix(filename, suffix);
    }

    /**
     * @param filename The name of a file.
     * @param suffix A suffix (including the .) that will replace the current suffix.
     * @return If the filename is of the form foo.bar: foosuffix;
     * in the presence of multiple .s, the final . is used to split.
     */
    public static  String replaceSuffix(String filename, String suffix) {
    	if (filename.lastIndexOf('.') >= 0)
    		return filename.substring(0, filename.lastIndexOf('.')) + suffix;
    	else return appendSuffix(filename, suffix);
    }

    /**
     * @param filename The name of a file.
     * @param suffix A suffix (including the .) that will replace the current suffix.
     * @return If the filename is of the form foo.bar: foosuffix;
     * in the presence of multiple .s, the first . is used to split.
     */
    public static  String replaceWholeSuffix(String filename, String suffix) {
    	if (filename.indexOf('.') >= 0)
    		return filename.substring(0, filename.indexOf('.')) + suffix;
    	else return appendSuffix(filename, suffix);
    }

    /**
     *  Truncates queries at a semicolon or comment marker. JDBC will choke if
     *  you try to pass it a query with a semicolon on the end. It also prevents
     *  SQL insertion attacks.
     *
     *@param  q  query to have the semicolon removed
     *@return    Description of the Returned Value
     *@since
     */
    public static String clearSemiColon( String q )
    {
        boolean inLiteral = false;
        char currentChar;
        char nextChar;
        for ( int i = 0; i < q.length(); i++ )
        {
            currentChar = q.charAt( i );
            if ( !inLiteral )
            {
                if ( currentChar == '\'' )
                {
                    inLiteral = true;
                    continue;
                }
                if ( currentChar == '{' || currentChar == ';' )
                {
                    return q.substring( 0, i );
                }
                else if ( i < q.length() - 1 )
                {
                    nextChar = q.charAt( i + 1 );
                    if ( ( currentChar == '/' && nextChar == '*' ) || ( currentChar == '-' && nextChar == '-' ) )
                    {
                        return q.substring( 0, i );
                    }
                }
            }
            else
            {//in literal - look for single ' to get out

                if ( currentChar == '\'' && i < q.length() - 1 )
                {
                    nextChar = q.charAt( i + 1 );
                    if ( nextChar == '\'' )
                    {
                        i++;
                        continue;
                    }
                    else
                    {
                        inLiteral = false;
                        continue;
                    }
                }
            }
        }
        return q;
    }

    /**
     *  Description of the Method
     *
     *@param  t  Description of the Parameter
     *@return    Description of the Return Value
     */
    public static String formatThrowable( Throwable t )
    {
        StringBuffer ret = new StringBuffer();
        /*
         *  show the message
         */
        ret.append( "\t--" );
        ret.append( Config.NEWLINE );
        ret.append("\t");
        ret.append( t.getClass().getName());
        ret.append( ": " );
        ret.append( t.getMessage() );
        /*
         *  If not the root cause, append the first 3 lines of the stack trace
         *  and then go on down the stack
         */
        StackTraceElement[] stack = t.getStackTrace();
        if ( t.getCause() != null )
        {
            ret.append( Config.NEWLINE );
            int limit = stack.length > 2 ? 3 : stack.length;
            for ( int i = 0; i < limit; i++ )
            {
                ret.append( "\t" );
                ret.append( stack[i] );
                ret.append( Config.NEWLINE );
            }
            ret.append( "\t..." + ( stack.length ) + " total\n" );
            ret.append( formatThrowable( t.getCause() ) );
        }
        /*
         *  if it is the root cause, make sure you go deep enough to get to
         *  mrald's classes, then show 2 more
         */
        else
        {
            int extra = 2;
            boolean foundMyClasses = false;
            ret.append( Config.NEWLINE );
            ret.append( "\t*Root cause*" );
            ret.append( Config.NEWLINE );
            for ( int i = 0; i < stack.length && extra >= 0; i++ )
            {
                ret.append( "\t" + stack[i] );
                ret.append( Config.NEWLINE );
                foundMyClasses = foundMyClasses || stack[i].getClassName().indexOf( "mrald" ) > -1;
                if ( foundMyClasses )
                {
                    extra--;
                }
            }
            ret.append( "\t..." + ( stack.length ) + " total" );
        }
        return ret.toString();
    }


    /**
     *  Takes a date in milliseconds and returns the date, formatted like: <br>
     *  <code>yy-mm-dd hh:mm:ss</code>
     *
     *@param  time_millis  Description of the Parameter
     *@return              Description of the Return Value
     */
    public static String formatDate( long time_millis )
    {
        SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        Date date = new Date( time_millis );
        return format.format( date );
    }
}

