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

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.mitre.mrald.query.MraldDijkstra;
import org.mitre.mrald.util.Config;
import org.mitre.mrald.util.MraldError;

/**
 *  Description of the Class
 *
 *@author     jchoyt
 *@created    August 19, 2002
 */
public class BadFormStructureTag extends TagSupport
{
    String errorCode;
    int errorType;


    /**
     *  Description of the Method
     *
     *@return                   Description of the Return Value
     *@exception  JspException  Description of the Exception
     */
    public int doStartTag()
        throws JspException
    {
        errorCode = pageContext.getRequest().getParameter( "errorCode" );
        System.out.println(errorCode);
        try
        {
            errorType = Integer.parseInt( errorCode );
            if ( errorType > MraldDijkstra.JOIN_PATH_NOT_COMPLETE_OR_UNIQUE || errorType < MraldDijkstra.JOIN_PATH_OK )
            {
                errorCode = errorCode + ", an invalid number";
                throw new NumberFormatException( errorCode + " is a bad value for errorCode" );
            }
        }
        catch ( NumberFormatException e )
        {
            try
            {
                pageContext.getOut().print( "<h4>An invalid error code was passed.</h4>  '" );
                pageContext.getOut().print( errorCode );
                pageContext.getOut().print( "' was passed.  What should have been passed was an integer that is one of the static values in the MraldDijkstra class.  Please report this to your <a href=\"mailto:" );
                pageContext.getOut().print( Config.getProperty( "MAILTO" ) );
                pageContext.getOut().print( "\">system administrator</a>.  In the meantime, check the possible reasons for this error below." );
            }
            catch ( Exception ee )
            {
                throw new MraldError( ee );
            }

            return 0;
        }
        StringBuffer ret = new StringBuffer();
        if ( errorType != MraldDijkstra.JOIN_PATH_OK )
        {
            ret.append( "\n<h3>The following error(s) in form structure were detected</h3><ul>" );
        }
        if ( errorType == MraldDijkstra.JOIN_PATH_NOT_UNIQUE || errorType == MraldDijkstra.JOIN_PATH_NOT_COMPLETE_OR_UNIQUE )
        {
            ret.append( "\n<li>" );
            ret.append( "The form you are attempting to build does not have a unique join path among all tables.</li>" );
        }
        if ( errorType == MraldDijkstra.JOIN_PATH_NOT_COMPLETE || errorType == MraldDijkstra.JOIN_PATH_NOT_COMPLETE_OR_UNIQUE )
        {
            ret.append( "\n<li>" );
            ret.append( "The form you are attempting to build does not have a complete join path among all tables.</li>" );
        }
        if ( errorType != MraldDijkstra.JOIN_PATH_OK )
        {
            ret.append( "</ul><br>Please use the browsers back button to correct the form.  For more information, see the description below of the restrictions on table and join structures in an MRALD form." );
        }
        ret.append( "<hr>" );
        try
        {
            pageContext.getOut().print( ret.toString() );
        }
        catch ( IOException e )
        {
            throw new MraldError( e );
        }
        return 0;
    }

}

