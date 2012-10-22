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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Random;
import org.mitre.mrald.control.AbstractStep;
import org.mitre.mrald.control.MsgObject;
import org.mitre.mrald.control.WorkflowStepException;
import javax.servlet.http.Cookie;


/**
 *  Description of the Class
 *
 *@author     jchoyt
 *@created    October 2, 2005
 */
public class UserRegistration extends AbstractStep
{
    /**
     *  Description of the Field
     */
    protected String username;
    /**
     *  Description of the Field
     */
    protected String password;
    /**
     *  Description of the Field
     */
    protected String pageurl;
    /**
     *  Description of the Field
     */
    protected String loginAction;
    /**
     *  Description of the Field
     */
    protected User mraldUser;
    /**
     *  Special string to detect if the user already exists or not.
     */
    protected final String USER_EXISTS = "AlreadyExists";

    /**
     *  Enumeration of all the ways this can fail
     */
    protected enum FailReason { AlreadyExists, PasswordMismatch, CantCreatePeopleTable, InvalidAction, ValidationFailureNoKey, ValidationFailureBadKey, UserDeleted }

    /**
     *  error code for this object - if all goes well, this will be null
     */
    protected FailReason errCode = null;

    /**
     *  Constructor for the UserRegistration object
     */
    public UserRegistration()
    {
    }


    /**
     *  The execute method needed to satisfy the AbstractStep interface. This is
     *  the default method that executes when this step in the workflow is called.
     *  Think of it as <i>main</i> for working in the MRALD framework.
     *
     *@param  msgObject                  Container for all information passed from
     *      the last step, and will carry the information to the next step.
     *@exception  WorkflowStepException  Required to satisfy the AbstractStep
     *      interface. All Exceptions not handled should be rethrown to this.
     *@since
     */
    public void execute( MsgObject msgObject )
        throws WorkflowStepException
    {
        try
        {
            //
            // Retrieve the necessary values from the MsgObject.
            // (These are important no matter the operation)
            //
            username = ( msgObject.getValue( "userName" ) )[0];
            password = ( msgObject.getValue( "password" ) )[0];
            pageurl = ( msgObject.getValue( "pageurl" ) )[0];
            loginAction = ( msgObject.getValue( "loginAction" ) )[0];

            //
            // Create a User Object in memory
            //
            mraldUser = new User( username );

            //
            // Adding a user to the system.  Most likely coming from RegistrationForm.jsp
            //
            if ( loginAction.equals( "addUser" ) )
            {
                String verifyPassword = msgObject.getReq().getParameter( "verifyPassword" );
                if ( !( password.equals( verifyPassword ) ) )
                {
                   errCode = FailReason.PasswordMismatch;
                    WorkflowStepException npe = new WorkflowStepException( "Your passwords did not match. Please go back and enter matching passwords." );
                    throw npe;
                }
                else
                {
                    String regInfo[] = new String[]{msgObject.getReq().getParameter( "firstName" ),
                        msgObject.getReq().getParameter( "lastName" ),
                        msgObject.getReq().getParameter( "company" ),
                        msgObject.getReq().getParameter( "department" ),
                        msgObject.getReq().getParameter( "address" ),
                        msgObject.getReq().getParameter( "city" ),
                        msgObject.getReq().getParameter( "state" ),
                        msgObject.getReq().getParameter( "zip" ),
                        msgObject.getReq().getParameter( "country" )};

                    try
                    {
                        if ( !( registerUserInfo( password, verifyPassword, regInfo ) ) )
                        {
                            throw new WorkflowStepException( "Error in Registering User. Please contact System Administrator." );
                        }
                    }
                    catch ( SQLException sqe )
                    {
                        try
                        {
                            if ( !MraldConnection.createPeopleTable() )
                            {
                            errCode = FailReason.CantCreatePeopleTable;
                            throw new WorkflowStepException( "Error in Registering User. Please contact System Administrator.", sqe );
                            }

                            if ( !( registerUserInfo( password, verifyPassword, regInfo ) ) )
                            {
                                throw new WorkflowStepException( "Error in Registering User. Please contact System Administrator.", sqe );
                            }
                        }
                        catch ( SQLException sqe2 )
                        {
                            throw new WorkflowStepException( sqe );
                        }
                    }
                }
                /*
                 *  add the cookie and session object so they don't have to log in...
                 */
                User mraldUser = new User( username );
                mraldUser.setEmail( username.toLowerCase() );
                mraldUser.setTypeId( 1 );
                mraldUser.setGroup( "Public" );
                msgObject.getReq().getSession().setAttribute( Config.getProperty( "cookietag" ), null );
                msgObject.getReq().getSession().setAttribute( Config.getProperty( "cookietag" ), mraldUser );
                Cookie c = new Cookie( Config.getProperty( "cookietag" ), username );
                c.setMaxAge( java.lang.Integer.MAX_VALUE / 2 );
                msgObject.getRes().addCookie( c );
            }

            //
            // Validating a User. Most likely coming from activate.jsp
            //
            else if ( loginAction.equals( "validateUser" ) )
            {
                String key = msgObject.getReq().getParameter( "key" );

                if ( key == null || key.equals( null ) || key.equals( "null" ) || key.equals( "" ) )
                {
                    errCode = FailReason.ValidationFailureNoKey;
                    throw new WorkflowStepException( "No Validation Key Provided.  unable to Authenicate Your Identity." );
                }
                else
                {
                    confirmUserInfo( key );
                }
            }

            //
            // No loginAction so throw an error describing invalid submission.
            //
            else
            {
                errCode = FailReason.InvalidAction;
                throw new WorkflowStepException( "Invalid Entry Point.  Please go to " + Config.getProperty( "BaseUrl" ) + "/MraldLogin.jsp and log in through a valid entry point." );
            }

            if ( loginAction.equals( "validateUser" ) )
            {
                //
                // Changing Password so Log them out and Re-Login
                //
                msgObject.getRes().sendRedirect( "ValidationSuccess.jsp" );
            }
            else if ( pageurl == null || pageurl.equals( null ) || pageurl.equals( "" ) || pageurl.equals( "null" ) )
            {
                //
                // PageURL is null so send them to the front page
                //
                msgObject.getRes().sendRedirect( Config.getProperty( "URL" ) );
            }
            else
            {
                //
                // Redirect to the intended page
                //
                msgObject.getRes().sendRedirect( pageurl );
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
            case AlreadyExists:
                message = "A user with that email address already exists.  Please use the browser back button and use a different email address`1, or you may request a new password <a href=\"RetrievePassword.jsp\">here</a>.";
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
            case ValidationFailureNoKey:
                message = "A validation key must be provided.  ";
                break;
            case ValidationFailureBadKey:
                message = "The validation key provided did not match the exptected value.  Please check your input for typos and try again.";
                break;
            case UserDeleted:
                message = "The user you are trying to validate for no longer exists.  Please re-register <a href=\"RegistrationForm.jsp\">here</a>.";
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
     *@param  key                           Description of the Parameter
     *@exception  SQLException              Description of the Exception
     *@exception  ClassNotFoundException    Description of the Exception
     *@exception  NoSuchAlgorithmException  Description of the Exception
     *@exception  IOException               Description of the Exception
     *@exception  NullPointerException      Description of the Exception
     *@exception  Exception                 Description of the Exception
     */
    protected void confirmUserInfo( String key )
        throws SQLException, ClassNotFoundException, NoSuchAlgorithmException, IOException, NullPointerException, Exception
    {
//        MraldCrypter crypt = new MraldCrypter();
        String query = "SELECT * FROM people WHERE people.email = '" + username.toLowerCase() + "'";
        MraldConnection conn = new MraldConnection( MetaData.ADMIN_DB );
        ResultSet r = conn.executeQuery( query );
        if ( r.next() )
        {
            String newHash = password + ":" + r.getString( "validationcode" );
            if ( MraldCrypter.matchPassword( key, newHash ) )
            {
                String update = "UPDATE people SET validated = 'Y' WHERE email = '" + username.toLowerCase() + "'";
                // MraldOutFile.logToFile( Config.getProperty( "LOGFILE" ), "UserRegistration: confirmUserInfo() - Query:" + update );
                conn.executeUpdate( update );
                generateWelcome( username.toLowerCase() );
            }
            else
            {
                //
                // User has not been validated so dont let them in
                //
                errCode = FailReason.ValidationFailureBadKey;
                r.close();
                conn.close();
                throw new WorkflowStepException( "Validation Key Is Invalid. Please Try Again." );
            }
        }
        else
        {
            //
            // User has not been validated so dont let them in
            //
            errCode = FailReason.UserDeleted;
            r.close();
            conn.close();
            throw new WorkflowStepException( "No Username Available for Validation. Please Try Again Later." );
        }
        r.close();
        conn.close();
    }


    /**
     *  Description of the Method
     *
     *@param  newpassword                       Description of the Parameter
     *@param  checkpassword                     Description of the Parameter
     *@param  regInfo                           Description of the Parameter
     *@return                                   Description of the Return Value
     *@exception  SQLException                  Description of the Exception
     *@exception  ClassNotFoundException        Description of the Exception
     *@exception  NoSuchAlgorithmException      Description of the Exception
     *@exception  UnsupportedEncodingException  Description of the Exception
     *@exception  Exception                     Description of the Exception
     */
    protected boolean registerUserInfo( String newpassword, String checkpassword, String[] regInfo )
        throws SQLException, ClassNotFoundException, NoSuchAlgorithmException, UnsupportedEncodingException, Exception
    {
        //this is done in the calling method, so I'm commenting it out here.  To be deleted after testing.
        // if ( !newpassword.equals( checkpassword ) )
        // {
        //     return false;
        // }

        String peopleId = addUserInfo();
        if( peopleId == USER_EXISTS )  //yes, I want ==, not .equals() - this is more exact
        {
            errCode = FailReason.AlreadyExists;
            return false;
        }
        String validationKey = generateKey();
        String hashKey = generateHash( validationKey );
        addRegInfo( peopleId, regInfo, validationKey );
        if ( Config.usingValidation == true )
        {
            generateConfirmation( peopleId, hashKey );
        }
        return true;
    }


    /**
     *  Description of the Method
     *
     *@return                                       Username
     *@exception  SQLException                      Description of the Exception
     *@exception  ClassNotFoundException            Description of the Exception
     *@exception  NoSuchAlgorithmException          Description of the Exception
     *@exception  UnsupportedEncodingException      Description of the Exception
     */
    protected String addUserInfo()
        throws SQLException, ClassNotFoundException, NoSuchAlgorithmException, UnsupportedEncodingException
    {
//        MraldCrypter crypt = new MraldCrypter();
        MraldConnection conn = new MraldConnection( MetaData.ADMIN_DB );

        String checkDup = "SELECT * FROM people WHERE email = '</email/>'";
        checkDup = MiscUtils.replace( checkDup, "</email/>", username.toLowerCase() );
        ResultSet rs = conn.executeQuery( checkDup );

        if ( rs.next() )
        {
            //
            // Duplicate username exists in the system.
            //
            return USER_EXISTS;
        }

        String insert = "INSERT INTO people ( email, peopletypeid, latticegroupid, password) VALUES ('</email/>',  1, '</latticegroupid/>', '</password/>')";
        String passEncrypt = MraldCrypter.encodePassword( password, null );
        String newCOI = findCOI();
        insert = MiscUtils.replace( insert, "</email/>", username.toLowerCase() );
        insert = MiscUtils.replace( insert, "</password/>", passEncrypt );
        insert = MiscUtils.replace( insert, "</latticegroupid/>", newCOI );
        long startTime = System.currentTimeMillis();
        conn.executeUpdate( insert );
        MiscUtils.logQueryRun( startTime, new StringBuffer() );
        mraldUser.setEmail( username.toLowerCase() );
        mraldUser.setTypeId( 1 );
        mraldUser.setGroup( newCOI );

        rs.close();
        conn.close();

        return username.toLowerCase();
    }


    /**
     *  AddRegInfo() should be the primary method through which registration
     *  information for a user is entered in the database. It is the location and
     *  contact information for a specific user.
     *
     *@param  peopleId                              The feature to be added to the
     *      RegInfo attribute
     *@param  regInfo                               The feature to be added to the
     *      RegInfo attribute
     *@param  validationKey                         The feature to be added to the
     *      RegInfo attribute
     *@exception  SQLException                      Description of the Exception
     *@exception  ClassNotFoundException            Description of the Exception
     */
    protected void addRegInfo( String peopleId, String[] regInfo, String validationKey )
        throws SQLException, ClassNotFoundException
    {
        MraldConnection conn = new MraldConnection( MetaData.ADMIN_DB );

        String insert = "UPDATE people SET firstName = '</firstName/>',  lastName = '</lastName/>', company = '</company/>', department = '</department/>', address = '</address/>', city = '</city/>', state = '</state/>', zip = '</zip/>', country = '</country/>', validated = 'N', validationcode = '</validationcode/>' WHERE email = '" + peopleId + "'";
        insert = MiscUtils.replace( insert, "</firstName/>", regInfo[0] );
        insert = MiscUtils.replace( insert, "</lastName/>", regInfo[1] );
        insert = MiscUtils.replace( insert, "</company/>", regInfo[2] );
        insert = MiscUtils.replace( insert, "</department/>", regInfo[3] );
        insert = MiscUtils.replace( insert, "</address/>", regInfo[4] );
        insert = MiscUtils.replace( insert, "</city/>", regInfo[5] );
        insert = MiscUtils.replace( insert, "</state/>", regInfo[6] );
        insert = MiscUtils.replace( insert, "</zip/>", regInfo[7] );
        insert = MiscUtils.replace( insert, "</country/>", regInfo[8] );
        insert = MiscUtils.replace( insert, "</validationcode/>", validationKey );
        // MraldOutFile.logToFile( Config.getProperty( "LOGFILE" ), "UserRegistration: addRegInfo() - Query:" + insert );
        conn.executeUpdate( insert );
        conn.close();
    }


    /**
     *  Description of the Method
     *
     *@return                                   Description of the Return Value
     *@exception  SQLException                  Description of the Exception
     *@exception  ClassNotFoundException        Description of the Exception
     *@exception  NoSuchAlgorithmException      Description of the Exception
     *@exception  UnsupportedEncodingException  Description of the Exception
     */
    protected String findCOI()
        throws SQLException, ClassNotFoundException, NoSuchAlgorithmException, UnsupportedEncodingException
    {
        String return_str;
        MraldConnection conn = new MraldConnection( MetaData.ADMIN_DB );
        String query = "SELECT DISTINCT latticegroupid FROM people WHERE people.email LIKE '%" + this.username.toLowerCase() + "%'";
        // MraldOutFile.logToFile( Config.getProperty( "LOGFILE" ), "UserRegistration: findCOI() - Query:" + query );
        ResultSet rs = conn.executeQuery( query );

        if ( rs.next() )
        {
            return_str = rs.getString( "latticegroupid" );
        }
        else
        {
            return_str = "Public";
        }

        rs.close();
        conn.close();

        return return_str;
    }


    /**
     *  Description of the Method
     *
     *@return    Description of the Return Value
     */
    protected String generateKey()
    {
        String key = "";
        Random rng = new Random( Calendar.getInstance().getTimeInMillis() );

        for ( int i = 0; i < 12; i++ )
        {
            key = key + ( char ) ( rng.nextInt( 26 ) + 97 );
        }

        return key;
    }


    /**
     *  Description of the Method
     *
     *@param  key                               Description of the Parameter
     *@return                                   Description of the Return Value
     *@exception  NoSuchAlgorithmException      Description of the Exception
     *@exception  UnsupportedEncodingException  Description of the Exception
     */
    protected String generateHash( String key )
        throws NoSuchAlgorithmException, UnsupportedEncodingException
    {
//        MraldCrypter crypt = new MraldCrypter();
        return MraldCrypter.encodePassword( password + ":" + key, null );
    }


    /**
     *  Description of the Method
     *
     *@param  username                          Description of the Parameter
     *@param  key                               Description of the Parameter
     *@exception  NullPointerException          Description of the Exception
     *@exception  ClassNotFoundException        Description of the Exception
     *@exception  UnsupportedEncodingException  Description of the Exception
     *@exception  Exception                     Description of the Exception
     */
    protected void generateConfirmation( String username, String key )
        throws NullPointerException, ClassNotFoundException, UnsupportedEncodingException, Exception
    {
        String newkey = URLEncoder.encode( key, "UTF-8" );
        StringBuffer emailText = new StringBuffer();
        emailText.append( "Thank you for registering with " + Config.getProperty( "TITLE" ) + " system.\n\n" );
        emailText.append( "This email has been sent from " + Config.getProperty( "BaseUrl" ) + "\n\n" );
        emailText.append( "You have received this email because this email address\n" );
        emailText.append( "was used during registration for our system.\n" );
        emailText.append( "If you did not register for our system, please disregard this\n" );
        emailText.append( "email. You do not need to unsubscribe or take any further action.\n\n" );
        emailText.append( "------------------------------------------------\n" );
        emailText.append( "Activation Instructions\n" );
        emailText.append( "------------------------------------------------\n\n" );
        emailText.append( "Thank you for registering.\n" );
        emailText.append( "We require that you 'validate' your registration to ensure that\n" );
        emailText.append( "the email address you entered was correct. This protects against\n" );
        emailText.append( "unwanted spam and malicious abuse.\n\n" );
        emailText.append( "To activate your account, simply click on the following link and\n" );
        emailText.append( "follow the instructions:\n\n" );
        emailText.append( Config.getProperty( "BaseUrl" ) + "/activate.jsp?key=" + newkey + "\n\n" );
        emailText.append( "(AOL Email users may need to cut and paste the link into your web browser).\n\n" );
        emailText.append( "------------------------------------------------\n" );
        emailText.append( "Not working?\n" );
        emailText.append( "------------------------------------------------\n\n" );
        emailText.append( "If you could not validate your registration by clicking on the link, please\n" );
        emailText.append( "visit this page:\n\n" );
        emailText.append( Config.getProperty( "BaseUrl" ) + "/activate.jsp\n\n" );
        emailText.append( "It will ask you to log in with your registered username, password,\n" );
        emailText.append( "and your validation key, which is provided below:\n\n" );
        emailText.append( "Validation Key: " + key + "\n\n" );
        emailText.append( "Please cut and paste, or type those numbers into the corresponding fields in the form.\n\n" );
        emailText.append( "If you still cannot validate your account, it's possible that the account has been removed.\n" );
        emailText.append( "If this is the case, please contact an administrator to rectify the problem.\n\n" );
        emailText.append( "Thank you for registering!\n\n" );
        emailText.append( "Regards,\n\n" );
        emailText.append( "The " + Config.getProperty( "TITLE" ) + " Development Team\n" );
        emailText.append( Config.getProperty( "BaseUrl" ) + "\n\n" );
        Mailer.send( username, Config.getProperty( "MAILTO" ), Config.getProperty( "SMTPHOST" ), emailText.toString(), "Account Activation" );
    }


    /**
     *  Description of the Method
     *
     *@param  username                    Description of the Parameter
     *@exception  NullPointerException    Description of the Exception
     *@exception  ClassNotFoundException  Description of the Exception
     *@exception  Exception               Description of the Exception
     */
    protected void generateWelcome( String username )
        throws NullPointerException, ClassNotFoundException, Exception
    {
        StringBuffer emailText = new StringBuffer();
        emailText.append( "Congratulations!\n\n" );
        emailText.append( "You have successfully completed the registration\n" );
        emailText.append( "process with our " + Config.getProperty( "TITLE" ) + " system.  We just wanted to point\n" );
        emailText.append( "out a few things we thought you would notice and want to\n" );
        emailText.append( "reduce any confusion.\n\n" );
        emailText.append( "If this system currently has a lattice-based security model,\n" );
        emailText.append( "it will limit the access and availabilty of the data in\n" );
        emailText.append( "our site. Unless you were previously authorized by a Data\n" );
        emailText.append( "Owner, you will be assigned a 'PUBLIC' tag which provides\n" );
        emailText.append( "the least amount of access to the system.  If you are associated\n" );
        emailText.append( "with a specific lab or research project, please feel free to send\n" );
        emailText.append( "an email to the Site Administrator at " + Config.getProperty( "MAILTO" ) + "\n\n" );
        emailText.append( "If you have further questions, please refer to our User's Guide\n" );
        emailText.append( "or spend some time exploring our system.\n\n" );
        emailText.append( "Thank you!\n\n" );
        emailText.append( "Regards,\n\n" );
        emailText.append( "The " + Config.getProperty( "TITLE" ) + " Development Team\n" );
        emailText.append( Config.getProperty( "BaseUrl" ) + "\n\n" );
        Mailer.send( username, Config.getProperty( "MAILTO" ), Config.getProperty( "SMTPHOST" ), emailText.toString(), "Welcome to " + Config.getProperty( "TITLE" ) );
    }

}

