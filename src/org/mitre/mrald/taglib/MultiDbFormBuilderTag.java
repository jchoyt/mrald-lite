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

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.jsp.JspException;

import org.mitre.mrald.formbuilder.CrossLinkElement;
import org.mitre.mrald.util.Config;
import org.mitre.mrald.util.DBMetaData;
import org.mitre.mrald.util.MraldError;
import org.mitre.mrald.util.MraldOutFile;

/**
 *  A formbuilder that applies to elements that need to span
 *  multiple databases. e.g. The crossLink that specifies how to connect
 *  across two disparagate databases
 *
 *@author     ghamilton
 *@created    August 29, 2007
 */
public class MultiDbFormBuilderTag extends FormBuilderTag
{

    /**
     *  Constructor for the FormBuilderTag object
     */
    public MultiDbFormBuilderTag()
    {
        super();
    }



    /**
     *  Standard entry for the Tag - kinda like main() for stand alone apps
     *
     *@return                   Always 0.
     *@exception  JspException  Standard exception - ones not caught will fall
     *      through to the ErrorPage.jsp
     */

    @SuppressWarnings("unchecked")
    public int doEndTag()
        throws JspException
    {
    	MraldOutFile.logToFile(Config.getProperty("LOGFILE"), "MultiDbFormBuilder: doEndTag start" );
    	//The main difference between FormBuilder and MultiDbFormBuilder
    	//ArrayList<DBMetaData> mds = ( ArrayList<DBMetaData> ) pageContext.getAttribute( "MultiDBMetaData" );
    	HashMap<String, DBMetaData> mds = (HashMap<String,DBMetaData>)pageContext.getRequest().getAttribute("MultiDBMetaData");
    	MraldOutFile.logToFile(Config.getProperty("LOGFILE"), "MultiDbFormBuilder: doEndTag: mds " + mds.size());

    	StringBuffer ret = new StringBuffer();
		 /*
	     *  add the HTML
	     */
	     CrossLinkElement thisElement;
	     try
	     {
	         thisElement = (CrossLinkElement)castElement();
	     }
	     catch ( ServletException e )
	     {
	         throw new MraldError( e );
	     }
	     setNumber();

	     ArrayList<DBMetaData> rearrangeDBs = new ArrayList<DBMetaData>();
	     for (DBMetaData md: mds.values())
	     {
	    	 rearrangeDBs.add(md);
	     }

	     for ( int i = 0; i < number; i++ )
	        {
	            ret.append( thisElement.getFBHtml( rearrangeDBs, i , thread) );
	            if ( i < number - 1 )
	            {
	                ret.append( "<hr width=\"90%\">\n" );
	            }
	        }
	          ret.append(  SECTION_CLOSE );

	     try
	     {
	        pageContext.getOut().print( ret.toString() );
	     }
	     catch ( java.io.IOException e )
	     {
	        throw new JspException( e.getMessage() );
	     }

        return 0;
    }
}

