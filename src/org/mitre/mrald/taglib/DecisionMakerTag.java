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

import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.mitre.mrald.util.Config;
import org.mitre.mrald.util.FormUtils;
import org.mitre.mrald.util.MraldOutFile;
import org.mitre.mrald.util.User;


/**
 *  Description of the Class
 *
 *@author     jchoyt
 *@created    November 8, 2002
 */
public class DecisionMakerTag extends TagSupport
{

    //
    // Getting the location of the custom forms in the web server
    //
    private String dir_struct = Config.getProperty( "customForms" );
//    private String form_id;
    private String formType;
    private String formAccess="Personal";


    /**
     *  Constructor for the DecisionMakerTag object
     */
    public DecisionMakerTag()
    {
        super();
    }


    /**
     *  The main program for the DecisionMakerTag class
     *
     *@param  args  The command line arguments
     */
    public static void main( String[] args )
    {
        new DecisionMakerTag();
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
        //
        // Establish the User is logged in and valid
        //
        /*
         *  TODO: use ValidateUserTag to do this
         */
        User user = ( User ) pageContext.getSession().getAttribute( Config.getProperty( "cookietag" ) );

        //User user = ( User ) pageContext.findAttribute( Config.getProperty( "cookietag" ) );
        if ( user == null )
        {
            throw new JspException( "Couldn't find the User object in the session" );
        }
        String userid = user.getEmail();

        if ( pageContext.getRequest().getParameter( "formAccess" ) != null && (pageContext.getRequest().getParameter( "formAccess" )!=""))
        {
            setFormAccess( pageContext.getRequest().getParameter( "formAccess" ) );
        }
        if (formAccess.equals("Public") || formAccess.equals("PublicEdit"))
               userid =  "public";
        //
        // Locate the formid from the request URL
        //
        String form_id = pageContext.getRequest().getParameter( "formid" );
        /* need to make sure the user gets the file they ask for, BEFORE we modify the name.

        Need to modify the file to make sure we rebuild the forms if we ask for a simple form and the xml is newer
        than the simple form jsp file.

        */

        File requested_file = new File( dir_struct + userid + "_" + form_id + ".jsp" );
        try{MraldOutFile.appendToFile( requested_file.getCanonicalPath() );}catch(Exception e){}
        int loc = form_id.indexOf( "-simple");
        if ( loc != -1 )
        {
            form_id = form_id.substring(0 ,loc );
        }
        if ( form_id.equals( "" ) )
        {
            throw new JspException( "No form_id was provided." );
        }
        //MraldOutFile.logToFile( Config.getProperty("LOGFILE") , "DecisionMakerTag : Getting FormType: " + pageContext.getAttribute("formType"));

        if ( pageContext.getRequest().getParameter( "formType" ) != null )
        {
            setFormType( pageContext.getRequest().getParameter( "formType" ) );
        }
        else
        {//do default which is just Select

            setFormType( "Select" );
        }



        //MraldOutFile.logToFile( Config.getProperty("LOGFILE") , "DecisionMakerTag : Getting FormType: " + getFormType());

        //
        // Attempting to identify the files
        //

        File xml_file = new File( dir_struct + userid + "_" + form_id + ".xml" );
        //File html_file = new File( dir_struct + userid + "_" + form_id + ".html" );
        File jsp_file = new File( dir_struct + userid + "_" + form_id + ".jsp" );
        File simple_jsp_file = new File( dir_struct + FormUtils.getSimpleJspName( form_id, userid ) );
        File redirect_file = requested_file;
        if ( xml_file.exists() && jsp_file.exists() )
        {
            //
            // Both exist so you need to check the modified date
            //
            if ( xml_file.lastModified() > jsp_file.lastModified() )
            {
                //
                // Run it through the XSLT for transformation
                //
                FormUtils.makeForm( xml_file );
                if ( simple_jsp_file.exists() )
                {
                    redirect_file = requested_file;
                }
            }
            // if
        }
        else if ( xml_file.exists() )
        {
            /*
             *  Only the XML file exists. Need to run it through the XSLT for
             */
            FormUtils.makeForm( xml_file );
            if ( simple_jsp_file.exists() )
            {
                redirect_file = simple_jsp_file;
            }
        }
        else if ( !jsp_file.exists() )
        {
            /*
             *  Neither file exists
             */
            throw new JspException( "Couldn't find either the XML or HTML file for the requested form : FormId " +  dir_struct + userid + "_" + form_id + ".jsp" );
        }
        // else
        try
        {
            HttpServletResponse res = ( HttpServletResponse ) pageContext.getResponse();
            res.sendRedirect( FormUtils.getRedirect( redirect_file.getAbsolutePath() ) );
        }
        catch ( IOException e )
        {
            JspException je = new JspException( e );
            throw je;
        }
        pageContext.getRequest().removeAttribute( "formAccess");
	pageContext.getRequest().removeAttribute( "formType");

        return 0;
    }



    /**
     *  Sets the formType attribute of the DecisionMakerTag object
     *
     *@param  formType  The new formType value
     */
    protected void setFormType( String formType )
    {
        this.formType = formType;
    }


    /**
     *  Gets the formType attribute of the DecisionMakerTag object
     *
     *@return    The formType value
     */
    protected String getFormType()
    {
        return formType;
    }

      /**
     *  Sets the formType attribute of the DecisionMakerTag object
     *
     *@param  formType  The new formType value
     */
    public void setFormAccess(String formAccess )
    {
        this.formAccess = formAccess;
    }


    /**
     *  Gets the formType attribute of the DecisionMakerTag object
     *
     *@return    The formType value
     */
    public String getFormAccess()
    {
        return formAccess;
    }

    /**
     *  Gets the formType attribute of the DecisionMakerTag object
     *
     *@return    The formType value
     */
    protected String getDirStruct()
    {
        return dir_struct;
    }

}

