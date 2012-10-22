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

import javax.servlet.ServletException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.mitre.mrald.formbuilder.FormBuilderElement;
import org.mitre.mrald.util.DBMetaData;
import org.mitre.mrald.util.MiscUtils;
import org.mitre.mrald.util.MraldError;

/**
 *  Description of the Class
 *
 *@author     jchoyt
 *@created    August 19, 2002
 */
public class FormBuilderTag extends BodyTagSupport
{
    String className;
    String howMany;
    int number;
    int thread=1;
    String other;
    String title;
    final static String SECTION_CLOSE = "</td></tr></table></td></tr></table>\n";
    final static String SECTION_OPENER_TEMPLATE = "<table summary=\"\" width=\"95%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><tr><td class=\"bord\"><table summary=\"\" width=\"100%\" border=\"0\" cellspacing=\"1\" cellpadding=\"2\"><tr><th><:title:></th></tr><tr><td>\n";


    /**
     *  Constructor for the FormBuilderTag object
     */
    public FormBuilderTag()
    {
        super();
    }


    /**
     *  Sets the className attribute of the FormBuilderTag object
     *
     *@param  className  The new className value
     */
    public void setClassName( String className )
    {
        this.className = className;
    }


    /**
     *  Sets the howMany attribute of the FormBuilderTag object
     *
     *@param  howMany  The new howMany value
     */
    public void setHowMany( String howMany )
    {
        this.howMany = howMany;
    }


    /**
     *  Sets the howMany attribute of the FormBuilderTag object
     */
    public void setNumber()
    {
        if(howMany==null)
        {
            number=1;
            return;
        }
        number = Integer.parseInt( howMany );
    }

    /**
     *  Sets the howMany attribute of the FormBuilderTag object
     */
    public void setThread(int threadNumber)
    {
       thread = threadNumber;
    }


    /**
     *  Sets the other attribute of the FormBuilderTag object
     *
     *@param  other  The new other value
     */
    public void setOther( String other )
    {
        this.other = other;
    }


    /**
     *  Sets the title attribute of the FormBuilderTag object
     *
     *@param  title  The new title value
     */
    public void setTitle( String title )
    {
        this.title = title;
    }


    /**
     *  Gets the title attribute of the FormBuilderTag object
     *
     *@return    The title value
     */
    public String getTitle()
    {
        return title;
    }


    /**
     *  Standard entry for the Tag - kinda like main() for stand alone apps
     *
     *@return                   Always 0.
     *@exception  JspException  Standard exception - ones not caught will fall
     *      through to the ErrorPage.jsp
     */

    public int doEndTag()
        throws JspException
    {
        DBMetaData md = ( DBMetaData ) pageContext.getAttribute( "DBMetaData" );
        StringBuffer ret = new StringBuffer();

		 /*
         *  add the HTML
         */
        FormBuilderElement thisElement;
        try
        {
            thisElement = castElement();
        }
        catch ( ServletException e )
        {
            throw new MraldError( e );
        }
        setNumber();
//        ret.append(MiscUtils.replace(SECTION_OPENER_TEMPLATE, "<:title:>", title));
        for ( int i = 0; i < number; i++ )
        {
            ret.append( thisElement.getFBHtml( md, i , thread) );
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


     public int doStartTag() throws JspException
     {
         try{
             pageContext.getOut().print( MiscUtils.replace(SECTION_OPENER_TEMPLATE, "<:title:>", title) );
         } catch (Exception e ){
             throw new JspException();
         }
         return EVAL_BODY_INCLUDE;
     }

    /**
     *  Based on the passed in class name, uses Reflection to create the new
     *  ParserElement objects for inclusion in the MsgObject workingObjects
     *  HashTable.
     *
     *@return                       ParserElement of the class defined by the
     *      input String
     *@exception  ServletException  Description of the Exception
     */
    protected FormBuilderElement castElement()
        throws ServletException
    {
        try
        {
            if ( className == null )
            {
                return null;
            }
            Class classDefinition = Class.forName( className );
            FormBuilderElement parserElement = ( FormBuilderElement ) classDefinition.newInstance();
            return parserElement;
        }
        catch ( InstantiationException wfe )
        {
            ServletException ce = new ServletException( wfe );
            throw ce;
        }
        catch ( ClassNotFoundException cne )
        {
            ServletException ce = new ServletException( cne );
            throw ce;
        }
        catch ( IllegalAccessException iae )
        {
            ServletException ce = new ServletException( iae );
            throw ce;
        }
    }
}

