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

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.mitre.mrald.util.Config;
import org.mitre.mrald.util.XSLTranslator;


/**
 *  Description of the Class
 *
 *@author     tcornett
 *@created    June 8, 2004
 */
public class NewsTag extends TagSupport
{
	private String newsArchive;
    /**
	  *
     *  Constructor for the NewsTag object
     */
    public NewsTag()
    {
        super();
    }

    /**
     *  Retrieves and displays the news for the system
     *
     *@return                   An int. Not used for anything.
     *@exception  JspException  Required by TagSupport. Used ot pass up the
     *      other exceptions.
     */
    public int doStartTag() throws JspException
    {
        try
        {
			String xmlStr = Config.getProperty( "NEWSXML" );
			String xslStr;
			if (newsArchive == null)
				xslStr = Config.getProperty( "NEWSDISPLAYXSL" );
			else
			{
				if ( newsArchive.equals( "yes" ) )
					xslStr = Config.getProperty( "NEWSARCHIVEXSL" );
				else
					xslStr = Config.getProperty( "NEWSDISPLAYXSL" );
			}
			StringBuffer ret = new StringBuffer();

			// Take of the leading 7 characters
			xmlStr = xmlStr.substring( 7 );

			if ( xmlStr == null || xslStr == null || xmlStr.equals("") || xslStr.equals("") )
			{
				ret.append( "Unable to Display News<br>Missing Attributes for News.\n" );
			}
			else
			{
				File xmlFile = new File( xmlStr );
				File xslFile = new File( xslStr );
				if ( !xmlFile.exists() || !xslFile.exists() )
					ret.append( "Unable to Display News<br>One or Both Files Do Not Exist.\n" );
				else
					ret.append( XSLTranslator.xslTransform( xmlFile, xslFile ).toString() );
			}
			pageContext.getOut().print( ret.toString() );
        }
        catch ( NullPointerException npe )
        {
            throw new JspException( npe );
        }
        catch( IOException e )
        {
            throw new JspException(e);
        }
        return 0;
    }

	public void setNewsArchive( String newsArchive )
	{
		this.newsArchive = newsArchive;
	}
}

