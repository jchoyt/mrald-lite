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
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.mitre.mrald.util.Config;
import org.mitre.mrald.util.XSLTranslator;


/**
 *  Description of the Class
 *
 *@author     jchoyt
 *@created    August 19, 2002
 */
public class XslTransformTag extends TagSupport
{
    String xml;
    String xsl;


    /**
     *  Constructor for the XslTransformTag object
     */
    public XslTransformTag()
    {
        super();
    }


    /**
     *  Sets the xml attribute of the XslTransformTag object
     *
     *@param  xml  The new xml value
     */
    public void setXml( String xml )
    {
        this.xml = xml;
    }


    /**
     *  Sets the xsl attribute of the XslTransformTag object
     *
     *@param  xsl  The new xsl value
     */
    public void setXsl( String xsl )
    {
        this.xsl = xsl;
    }


    /**
     *  Gets the xml attribute of the XslTransformTag object
     *
     *@return    The xml value
     */
    public String getXml()
    {
        return xml;
    }


    /**
     *  Gets the xsl attribute of the XslTransformTag object
     *
     *@return    The xsl value
     */
    public String getXsl()
    {
        return xsl;
    }


    /**
     *  Description of the Method
     *
     *@return                   Description of the Return Value
     *@exception  JspException  Description of the Exception
     */
    public int doStartTag()
        throws JspException
    {
        try
        {
	    File xmlFile = new File( xml );
	    if (!xmlFile.exists())
		    xmlFile = new File( Config.getProperty("BasePath") + "/" + xml );
	    if (!xmlFile.exists())
		    xmlFile = new File( Config.getProperty("BasePath") + "/WEB-INF/props/" + xml );

	    File xslFile =   new File( xsl );
	    if (!xslFile.exists())
		    xslFile = new File( Config.getProperty("BasePath") + "/WEB-INF/" + xsl );

            pageContext.getOut().print( XSLTranslator.xslTransform( xmlFile , xslFile ).toString() );
        }
	catch ( FileNotFoundException e )
        {
		throw new JspException( e );
        }
        catch ( IOException e )
        {
            throw new JspException( e );
        }
        return 0;
    }

}

