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

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.http.Cookie;
import org.mitre.mrald.control.AbstractStep;
import org.mitre.mrald.control.MsgObject;
import org.mitre.mrald.control.WorkflowStepException;

/**
 *  Description of the Class
 *
 *@author     jchoyt
 *@created    October 2, 2005
 */
public class UserLogin extends AbstractStep
{
    private String username;
    private String password;
    private String pageurl;
    private String loginAction;
    private User mraldUser;
    private MsgObject msg;
    /**
     *  Enumeration of all the ways this can fail
     */
    private enum FailReason { InvalidPassword, UnvalidatedUser, UserDoesNotExist, UnequalPasswords, PasswordMismatch, CantCreatePeopleTable, InvalidAction }

    /**
     *  error code for this object - if all goes well, this will be null
     */
    private FailReason errCode = null;



    /**
     *  Constructor for the UserLogin object
     */
    public UserLogin()
    {
        username = null;
        password = null;
        pageurl = null;
        loginAction = null;
        mraldUser = null;
    }


    /**
     *  The execute method needed to satisfy the AbstractStep interface. This is
     *  the default method that executes when this step in the workflow is
     *  called. Think of it as <i>main</i> for working in the MRALD framework.
     *
     *@param  msgObject                  Container for all information passed
     *      from the last step, and will carry the information to the next step.
     *@exception  WorkflowStepException  Required to satisfy the AbstractStep
     *      interface. All Exceptions not handled should be rethrown to this.
     *@since
     */
    public void execute( MsgObject msgObject )
        throws WorkflowStepException
    {
        msg = msgObject;
        try
        {
            //
            // Retrieve the necessary values from the MsgObject.
            // (These are important no matter the operation)
            //
            username = ( msgObject.getValue( "userName" ) )[0];
            password = ( msgObject.getValue( "password" ) )[0];
            pageurl = ( msgObject.getValue( "pageurl" ) )[0];
            if ( pageurl == null )
            {
                pageurl = Config.getProperty( "BaseUrl" );//do not make this URL - this should return to FirstRun for initial setup
            }
            loginAction = msgObject.getValue( "loginAction" )[0];
            //
            // Create a User Object in memory
            //
            mraldUser = new User( username );
            //
            // Big Check as to whether or not the security is in place
            //
            if ( Config.usingSecurity == false )
            {
                //
                // NO SECURITY IN USE IF IN HERE
                //
                new UserFiller( username, mraldUser ).run();
            }
            else
            {
                //
                // Updating a user in the system.  Most likely coming from ChangePassword.jsp
                //
                if ( loginAction.equals( "updateLogin" ) )
                {
                    String newPassword = msgObject.getValue( "newPassword" )[0];
                    String checkPassword = msgObject.getValue( "checkPassword" )[0];
                    if ( !( newPassword.equals( checkPassword ) ) )
                    {
                        //
                        // Throw an error here because the passwords do not match
                        //
                        errCode = FailReason.PasswordMismatch;
                        WorkflowStepException ipe = new WorkflowStepException( "Your passwords did not match. Please go back and enter matching passwords." );
                        throw ipe;
                    }
                    else
                    {
                        if ( !updatePassword( newPassword, checkPassword ) )
                        {
                            errCode = FailReason.UnequalPasswords;
                            WorkflowStepException e = new WorkflowStepException( "You must enter a valid password." );
                            throw e;
                        }
                    }
                }
                //
                // Verifying username and password for access.  Most likely coming from MraldLogin.jsp.
                // if something went wrong, stop processing here - logInUser should take care of routing.
                //
                else if ( loginAction.equals( "checkUser" ) )
                {
                    if ( !logInUser() )
                    {

                        return;
                    }
                }
                //
                // Generating and emailing a new password to the requesting user. Most likely coming from RetrievePassword.jsp
                //
                else if ( loginAction.equals( "retrievePassword" ) )
                {
                    generatePassword();
                }
                //
                // Changing the COI of a User. Most likely coming from ChangeCOI.jsp
                //
                else if ( loginAction.equals( "changeCOI" ) )
                {
                    String newCoi = msgObject.getValue( "COI" )[0];
                    pageurl = "success.jsp";
                    mraldUser = ( User ) msgObject.getReq().getSession().getAttribute( Config.getProperty( "cookietag" ) );
                    //
                    // Changing the COI Group of the User Object
                    //
                    mraldUser.setGroup( newCoi );
                }
                //
                // No loginAction so throw an error describing invalid submission.
                //
                else
                {
                    errCode = FailReason.InvalidAction;
                    WorkflowStepException e = new WorkflowStepException( "Invalid Entry Point.  Please go to " + Config.getProperty( "BaseUrl" ) + "/MraldLogin.jsp and log in through a valid entry point." );
                    throw e;
                }
            }
            if ( loginAction.equals( "checkUser" ) || loginAction.equals( "changeCOI" ) )
            {
                //
                // Setting the cookie on the client machine
                //
                msgObject.getReq().getSession().setAttribute( Config.getProperty( "cookietag" ), null );
                msgObject.getReq().getSession().setAttribute( Config.getProperty( "cookietag" ), mraldUser );
                Cookie c = new Cookie( Config.getProperty( "cookietag" ), username );
                c.setMaxAge( java.lang.Integer.MAX_VALUE / 2 );
                msgObject.getRes().addCookie( c );

                msgObject.setRedirect( pageurl );
                return;
            }
            else if ( loginAction.equals( "updateLogin" ) )
            {
                //
                // Changing Password so Log Then out and Re-Login
                //
                msgObject.setRedirect( "logout.jsp" );
                return;
            }
            else
            {
                //
                // Redirect to the intended page
                //
                msgObject.setRedirect( pageurl );
                return;
            }
        }
        catch ( WorkflowStepException e )
        {
            if( errCode != null )
            {
                niceErrorPage(msgObject);
            }
            else throw e;
        }
        catch ( Exception e )
        {
            throw new WorkflowStepException( e );
        }
    }

    /**
     *  Puts a message into session memory and redirects the request to a jsp that will read the message, then remove it
     *  from session memory.
     */
    protected void niceErrorPage(MsgObject msgObject)
    {
        String message;
        switch( errCode )
        {
            case InvalidPassword:
                message = "The user with that email address has an invalide password, you must request a new password <a href=\"RetrievePassword.jsp\">here</a>.";
                break;
            case UnvalidatedUser:
                message = "The user with that email address must be validated.  Please check your email for instructions.";
                break;
            case UserDoesNotExist:
                message = "No user with that email exists.  Please register <a href=\"RegistrationForm.jsp\">here</a>.";
                break;
            case UnequalPasswords:
                message = "The passwords provided did not match.  Please use the browser back button and try again.";
                break;
            case PasswordMismatch:
                message = "The passwords provided did not match.  Please use the browser back button and try again.";
                break;
            case CantCreatePeopleTable:
                message = "Unable to properly set up the administrative database.  Please report this to the system administrator.";
                break;
            case InvalidAction:
                message = "An invalide action was requested by the calling page.  Please report this to the system administrator.";
                break;
            default:
                message="";
        }
        try{
            msgObject.getReq().getSession().setAttribute( "responseMessage", message );
            msgObject.getRes().sendRedirect( "responseMessage.jsp" );
        }
        catch(IOException e)
        {
            throw new MraldError(e, msgObject);
        }
    }


    /**
     *  Description of the Method
     *
     *@param  newpassword    Description of the Parameter
     *@param  checkpassword  Description of the Parameter
     *@return                Description of the Return Value
     *@exception  Exception  Description of the Exception
     */
    protected boolean updatePassword( String newpassword, String checkpassword )
        throws Exception
    {
        MraldConnection conn = new MraldConnection( MetaData.ADMIN_DB );
//        MraldCrypter crypt = new MraldCrypter();
        ResultSet r = null;
        String query = "SELECT email, peopletypeid, password FROM people WHERE people.email = '" + this.username.toLowerCase() + "'";
        String prevPass = null;
        r = conn.executeQuery( query );
        if ( r.next() )
        {
            //
            // Retrieving the stored password
            //
            prevPass = r.getString( "password" );
        }
        else
        {
            //
            // No user with that email address
            //
            r.close();
            conn.close();
            errCode = FailReason.UserDoesNotExist;
            WorkflowStepException e = new WorkflowStepException( "No User Found with the corresponding Email Address" );
            throw e;
        }
        if ( MraldCrypter.matchPassword( prevPass, this.password ) )
        {
            //
            // Passwords match...
            //
            String passEncrypt = MraldCrypter.encodePassword( newpassword, null );
            // MraldOutFile.logToFile( Config.getProperty( "LOGFILE" ), "UserLogin: updatePassword() - Changing Password" );
            setPassword( this.username, passEncrypt, conn );
            r.close();
            conn.close();
            return true;
        }
        else
        {
            //
            // Passwords DO NOT match...
            //
            r.close();
            conn.close();
            return false;
        }
    }


    /**
     *  setPassword() should be the only method in which a password is changed
     *  or added in the system.
     *
     *@param  emailId        The new password value
     *@param  password       Description of the Parameter
     *@param  connect        must be a Connection to the admin database
     *@return                Description of the Return Value
     *@exception  Exception  Description of the Exception
     */
    protected boolean setPassword( String emailId, String password, Object connect )
        throws Exception
    {
        MraldConnection conn = ( MraldConnection ) connect;
        String update = "UPDATE people SET password = '" + password + "' WHERE email = '" + emailId + "'";
        conn.executeUpdate( update );
        conn.close();
        return true;
    }


    /**
     *  Checks user user email and password. If something goes wrong, either
     *  throws an exception, or redirects the user
     *
     *@return                true if checked in, false if there was a problem
     *      and processing should stop here
     *@exception  Exception  Description of the Exception
     */
    protected boolean logInUser()
        throws Exception
    {
        if ( username == null || password == null )
        {
            msg.setRedirect( Config.getProperty( "BaseUrl" ) + "/MraldLogin.jsp?badLogin=true" );
            return false;
        }
        String query = "SELECT email, peopletypeid, password, latticegroupid, validated FROM people WHERE people.email = '" + username.toLowerCase() + "'";
        MraldConnection conn = new MraldConnection( MetaData.ADMIN_DB );
        ResultSet r = null;
        try
        {
            r = conn.executeQuery( query );
        }
        catch ( SQLException e )
        {
            try
            {
                if ( !MraldConnection.createPeopleTable() )
                {
                    throw new SQLException( "Error creating the people/latticegroup table. Please check database configuration." );
                }
            }
            catch ( SQLException se )
            {
                throw e;
            }
            r = conn.executeQuery( query );
        }
        if ( r.next() )
        {
            //
            // User exists in the database so process the login
            //
//            MraldCrypter crypt = new MraldCrypter();
            String prevPass = r.getString( "password" );
            int peopleTypeId = r.getInt( "peopletypeid" );
            String groupid = r.getString( "latticegroupid" );
            String validationKey = r.getString( "validated" );
            if ( prevPass == null )
            {
                //
                // Stored password is not present. (Something wrong with the account?)
                // Instruct the user to request a new password
                //
                r.close();
                conn.close();
                // MraldOutFile.logToFile( Config.getProperty( "LOGFILE" ), "UserLogin: logInUser() - Account EXISTS BUT NO PASSWORD ENTRY?????????" );
                errCode = FailReason.InvalidPassword;
                WorkflowStepException e = new WorkflowStepException( "Account password is invalid.  You must request a new password." );
                throw e;
            }
            else if ( validationKey != null && validationKey.equals( "N" ) && Config.usingValidation == true )
            {
                //
                // User has not been validated so dont let them in
                //
                r.close();
                conn.close();
                errCode = FailReason.UnvalidatedUser;
                WorkflowStepException e = new WorkflowStepException( "Account has been registered but not validated.  Please check your email for more instructions." );
                throw e;
            }
            else
            {
                if ( !( MraldCrypter.matchPassword( prevPass, password ) ) )
                {
                    //
                    // Passwords do not match...
                    //
                    r.close();
                    conn.close();
                    msg.setRedirect( Config.getProperty( "BaseUrl" ) + "/MraldLogin.jsp?badLogin=true" );
                    return false;
                }
            }
            //
            // At this point, there have been no exceptions thrown so the username/password are good and set.
            //
            MraldOutFile.logToFile( Config.getProperty( "LOGFILE" ), "UserLogin: logInUser() - User Logged In:" + username.toLowerCase() );
            mraldUser.setEmail( username.toLowerCase() );
            mraldUser.setTypeId( peopleTypeId );
            mraldUser.setGroup( groupid );
            r.close();
            conn.close();
            return true;
        }
        else
        {
            //
            // No such user based on email address
            //
            r.close();
            conn.close();
            msg.setRedirect( Config.getProperty( "BaseUrl" ) + "/MraldLogin.jsp?badLogin=true" );
            return false;
        }
    }


    /**
     *  generatePassword() is the function that handles the situation when users
     *  forget their passwords and need to have a new one sent to them.
     *
     *@exception  Exception  Description of the Exception
     */
    public void generatePassword()
        throws Exception
    {
        String query = "SELECT email FROM people WHERE people.email = '" + username.toLowerCase() + "'";
        MraldConnection conn = new MraldConnection( MetaData.ADMIN_DB );
        ResultSet r = conn.executeQuery( query );
        if ( r.next() )
        {
            //
            // Creating the New Password
            //
            String password = "";
            java.util.Random rng = new java.util.Random( java.util.Calendar.getInstance().getTimeInMillis() );
            for ( int i = 0; i < 8; i++ )
            {
                password = password + ( char ) ( rng.nextInt( 26 ) + 97 );
            }
            //
            // Generating new password, encrypting, and storing in the Database
            //
//            MraldCrypter crypt = new MraldCrypter();
            String reset = MraldCrypter.encodePassword( password, null );
            // MraldOutFile.logToFile( Config.getProperty( "LOGFILE" ), "UserLogin: generatePassword() - Resetting Password" );
            setPassword( username, reset, conn );
            //
            // Email the user with the new password
            //
            StringBuffer emailText = new StringBuffer();
            emailText.append( "This email was generated at your request.\n" );
            emailText.append( "Below you will find your newly created password for the " );
            emailText.append( Config.getProperty( "TITLE" ) + " system located at:\n\n" );
            emailText.append( Config.getProperty( "URL" ) + "\n\n" );
            emailText.append( "Your new password is: " + password + "\n\n" );
            emailText.append( "You may change your password once you have logged successfully into the system.\n\n" );
            emailText.append( "Thank you,\nThe " + Config.getProperty( "TITLE" ) + " Development Team\n\n" );
            Mailer.send( username, Config.getProperty( "MAILTO" ), Config.getProperty( "SMTPHOST" ), emailText.toString(), "Password Request" );
            r.close();
            conn.close();
        }
        else
        {
            //
            // No User Found in the System with the Email Address
            //
            r.close();
            conn.close();
            errCode = FailReason.UserDoesNotExist;
            WorkflowStepException npe = new WorkflowStepException( "No User Found With Corresponding Email" );
            throw npe;
        }
    }
}

