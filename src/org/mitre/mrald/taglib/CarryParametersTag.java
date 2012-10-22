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
import java.util.Enumeration;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.mitre.mrald.util.User;

/**
 *  Description of the Class
 *
 *@author     jchoyt
 *@created    August 19, 2002
 */
public class CarryParametersTag extends TagSupport
{
    User user = new User();


    /**
     *  Constructor for the PersonalFormsListTag object
     */
    public CarryParametersTag()
    {
        super();
    }


    /**
     *  Constructor for the carryPreviousTags object
     *
     *@return          Description of the Return Value
     */
    public String carryPreviousTags()
    {
        StringBuffer ret = new StringBuffer();
        Enumeration e = pageContext.getRequest().getParameterNames();
        while ( e.hasMoreElements() )
        {
            String key = ( String ) e.nextElement();
            String[] values = pageContext.getRequest().getParameterValues( key );
            for ( int i = 0; i < values.length; i++ )
            {
                ret.append( "\n<input type=\"hidden\" name=\"" );
                ret.append( key );
                ret.append( "\" value=\"" );
                ret.append( values[i] );
                ret.append( "\" />" );
            }
        }
        return ret.toString();
    }


    /**
     *  Retrieves and prints the user's personal forms list
     *
     *@return                   An int. Not used for anything.
     *@exception  JspException  Required by TagSupport. Used ot pass up the
     *      other exceptions.
     */
    public int doStartTag()
        throws JspException
    {
        try
        {
            pageContext.getOut().print( carryPreviousTags() );
        }
        catch ( IOException e )
        {
            JspException je = new JspException( e );
            throw je;
        }
        return 0;
    }
}

