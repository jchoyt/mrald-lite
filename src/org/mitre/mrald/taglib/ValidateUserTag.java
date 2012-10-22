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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.mitre.mrald.control.WorkflowStepException;
import org.mitre.mrald.util.Config;
import org.mitre.mrald.util.User;

/**
 *  Description of the Class
 *
 *@author     jchoyt
 *@created    August 19, 2002
 */
public class ValidateUserTag extends TagSupport
{
    protected String doAdminCheck = "no";
    protected String doEmailCheck = "no";
    protected String doCoiCheck = "no";
    protected User user;
    protected boolean forwarded = false;


    /**
     *  Constructor for the ValidateUserTag object
     */
    public ValidateUserTag()
    {
        super();
    }


    /**
     *  Checks for a User bean in the session. If one doesn not exist, attempts
     *  to create one from the cookie-passed user name. If the cookie isn't
     *  found, forces the user to log in.
     *
     *@return                   An int. Not used for anything.
     *@exception  JspException  Description of the Exception
     */
    public int doStartTag() throws JspException
    {
        try
        {
            HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();
            HttpServletResponse res = (HttpServletResponse) pageContext.getResponse();

            user = ( User ) pageContext.getSession().getAttribute( Config.getProperty( "cookietag" ) );
			if ( user == null )
			{
				//
				// Force the user to login for each new session go to log in
				//
				try
				{
					res.sendRedirect( Config.getProperty( "BaseUrl" ) + "/MraldLogin.jsp?pageurl=" + req.getRequestURL() );
				}
				catch ( IOException e )
				{
					throw new JspException( e );
				}
			}
			else
			{
				if ( user.getGroup() == null )
				{
					//
					// If the User name is found, but no group has been assigned,
					// then force to login to get groupName
					//
					try
					{
						res.sendRedirect( Config.getProperty( "BaseUrl" ) + "/MraldLogin.jsp?pageurl=" + req.getRequestURL() );
					}
					catch ( IOException e )
					{
						throw new WorkflowStepException( e.getMessage() );
					}
				}
                adminCheck();
                emailCheck();
				coiCheck();
			}
            return 0;
        }
        catch (WorkflowStepException w)
        {
// PM: Nothing was done with the exception!
//            JspException je = new JspException(w.getMessage());
        }

        return 0;
    }



    /**
     *  Description of the Method
     *
     *@return    Description of the Return Value
     */
    public int doEndTag()
    {
        if ( user == null || forwarded )
        {
            return SKIP_PAGE;
        }
        else
        {
            return EVAL_PAGE;
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  JspException  Description of the Exception
     */
    public void adminCheck()
        throws JspException
    {
		//
        // if the doAdminCheck is requested in the tag, redirect to an access denied page
		//
        if ( doAdminCheck.equals( "no" ) )
            return;

		//
        // Redirect if they do not have access...
		//
        if ( user.getTypeId() < 3 )
        {
            try
            {
                pageContext.forward( "denied.jsp" );
                forwarded = true;
            }
            catch ( javax.servlet.ServletException se )
            {
                throw new JspException( se );
            }
            catch ( IOException e )
            {
                throw new JspException( e );
            }
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  JspException  Description of the Exception
     */
    public void emailCheck()
        throws JspException
    {
        if ( doEmailCheck.equals( "no" ) )
        {
            return;
        }
        // if the doAdminCheck is requested in the tag, redirect to an access denied page
        // Redirect if they do not have access...
        if ( !user.getEmail().equals( doEmailCheck ) )
        {
            try
            {
                pageContext.forward( "denied.jsp" );
                forwarded = true;
            }
            catch ( javax.servlet.ServletException se )
            {
                throw new JspException( se );
            }
            catch ( IOException e )
            {
                throw new JspException( e );
            }
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  JspException  Description of the Exception
     */
    public void coiCheck()
        throws JspException
    {
        // if the doAdminCheck is requested in the tag, redirect to an access denied page
        if ( doCoiCheck.equals( "no" ) )
        {
            return;
        }
        // Redirect if they do not have access...
        if ( user.getGroup().equals("Public")  )
        {
            try
            {
                pageContext.forward( "denied.jsp" );
                forwarded = true;
            }
            catch ( javax.servlet.ServletException se )
            {
                throw new JspException( se );
            }
            catch ( IOException e )
            {
                throw new JspException( e );
            }
        }
    }

    /**
     *  Sets the doAdminCheck attribute of the ValidateUserTag object
     *
     *@param  doAdminCheck  The new doAdminCheck value
     */
    public void setDoAdminCheck( String doAdminCheck )
    {
        this.doAdminCheck = doAdminCheck;
    }

    /**
     *  Sets the doEmailCheck attribute of the ValidateUserTag object
     *
     *@param  doEmailCheck  The new doEmailCheck value
     */
    public void setDoEmailCheck( String doEmailCheck )
    {
		this.doEmailCheck = doEmailCheck;
    }

     /**
     *  Sets the doEmailCheck attribute of the ValidateUserTag object
     *
     *@param  doEmailCheck  The new doEmailCheck value
     */
    public void setDoCoiCheck( String doCoiCheck )
    {
        this.doCoiCheck = doCoiCheck;
    }
}

