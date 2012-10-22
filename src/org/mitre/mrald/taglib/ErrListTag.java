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
package org.mitre.mrald.taglib;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.mitre.mrald.admin.ErrFileFilter;
import org.mitre.mrald.util.Config;

/**
 *  Description of the Class
 *
 *@author     jchoyt
 *@created    August 30, 2003
 */
public class ErrListTag extends TagSupport
{

    long now = System.currentTimeMillis();


    /**
     *  Constructor for the PersonalFormsListTag object
     */
    public ErrListTag()
    {
        super();
    }


    /**
     *  Description of the Method
     *
     *@param  filename  Description of the Parameter
     *@param  i         Description of the Parameter
     *@return           Description of the Return Value
     */
    public StringBuffer deleteLink( String filename, int i )
    {
        File errFile = new File( Config.getProperty( "LOGPATH" ), filename );
        boolean olderThanAWeek = ( now - errFile.lastModified() ) > 1000 * 60 * 60 * 24 * 7;
        StringBuffer ret = new StringBuffer();
        ret.append( " &nbsp; ( " );
        ret.append( formatDate( errFile.lastModified() ) );
        ret.append( ", ");
        ret.append( formatSize( errFile.length() ) );
        ret.append( "kb ) &nbsp; " );
        ret.append( "<input name=\"errFileDelete" );
        ret.append( i );
        ret.append( "\" value=\"" );
        ret.append( filename );
        ret.append( "\" type=\"checkbox\"" );
        if ( olderThanAWeek )
        {
            ret.append( " checked>" );
        }
        else
        {
            ret.append( ">" );
        }
        ret.append( " Delete<br>" );
        ret.append( Config.NEWLINE );
        return ret;
    }


    /**
     *  Retrieves and prints a list of
     *
     *@return                   An int. Not used for anything.
     *@exception  JspException  Required by TagSupport. Used to pass up the
     *      other exceptions.
     */
    public int doStartTag()
        throws JspException
    {
        StringBuffer ret = new StringBuffer();
        File errDir = new File( Config.getProperty( "LOGPATH" ) );
        String[] files = errDir.list( new ErrFileFilter() );
        Arrays.sort( files );
        for ( int i = files.length - 1; i > 0; i-- )
        {
            ret.append( " &nbsp; <a href=\"" );
            ret.append( Config.getProperty( "LOGURL" ) );
            ret.append( files[i] );
            ret.append( "\">" );
            ret.append( files[i] );
            ret.append( "</a>" );
            ret.append( deleteLink( files[i], i ) );
        }
        try
        {
            pageContext.getOut().print( ret.toString() );
        }
        catch ( IOException e )
        {
            throw new JspException( e );
        }
        return 0;
    }


    /**
     *  Description of the Method
     *
     *@param  dateTime  Description of the Parameter
     *@return           Description of the Return Value
     */
    public String formatDate( long dateTime )
    {
        SimpleDateFormat f = new SimpleDateFormat( "EEEE, d MMMM yyyy, H:mm:ss" );
        return f.format( new Date( dateTime ) );
    }

    public String formatSize( long fileSize )
    {
        double kbSize = fileSize/1024.0;
        DecimalFormat f = new DecimalFormat("#,##0.0" );
        return f.format(kbSize);
    }
}

