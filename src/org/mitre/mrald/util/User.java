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


/**
 *  Description of the Class
 *
 *@author     jchoyt
 *@created    October 7, 2002
 */
public class User extends java.lang.Object
{
    protected java.lang.String email = "";
    protected java.lang.String group = "";
    protected java.lang.String password = "";
    protected String[] formIds;
    protected java.lang.Object sharedUsers;
    protected int typeId;
    protected int userId;
    public final static int COMMON_USER = 1;
    public final static int ADMIN_USER = 3;


    /**
     *  Constructor for the User object
     */
    public User()
    {
        this.userId = 0;
    }


    /**
     *  Constructor for the User object
     *
     *@param  userName  Description of the Parameter
     */
    public User( String userName )
    {
        if ( WebUtils.isValidEmailAddress( userName ) )
            email = userName;
		else
            throw new RuntimeException( userName + " is not a valid email address" );
    }


    /**
     *  Sets the email attribute of the User object
     *
     *@param  email                 The new email value
     *@exception  RuntimeException  thrown if the email addess is not
     *      structurally valid
     */
    public void setEmail( java.lang.String email )
        throws RuntimeException
    {
        if ( WebUtils.isValidEmailAddress( email ) )
            this.email = email;
        else
            throw new RuntimeException( email + " is not a valid email address" );
    }


    /**
     *  Gets the email attribute of the User object
     *
     *@return    The email value
     */
    public java.lang.String getEmail()
    {
        return this.email;
    }


    /**
     *  Sets the formIds attribute of the User object
     *
     *@param  formIds  The new formIds value
     */
    public void setFormIds( String[] formIds )
    {
        this.formIds = formIds;
    }


    /**
     *  Gets the formIds attribute of the User object
     *
     *@return    The formIds value
     */
    public String[] getFormIds()
    {
        return this.formIds;
    }


    /**
     *  Sets the typeId attribute of the User object
     *
     *@param  typeId  The new typeId value
     */
    public void setTypeId( int typeId )
    {
        this.typeId = typeId;
    }


    /**
     *  Gets the typeId attribute of the User object
     *
     *@return    The typeId value
     */
    public int getTypeId()
    {
        return this.typeId;
    }


    /**
     *  Sets the email attribute of the User object
     *
     *@param  group  The new group value
     */
    public void setGroup( java.lang.String group )
    {
        this.group = group;
    }


    /**
     *  Gets the email attribute of the User object
     *
     *@return                           The email value
     *@exception  NullPointerException  Description of the Exception
     */
    public String getGroup()
        throws NullPointerException
    {
        return this.group;
    }


    /**
     *  Description of the Method
     *
     *@param  userName  Description of the Parameter
     */
    public void logUserAccess( String userName )
    {
        MraldOutFile.logToFile( Config.getProperty( "LOGFILE" ), "System accessed by " + userName );
    }


    public String toString()
    {
        StringBuffer ret = new StringBuffer();
        ret.append("Email: ");
        ret.append(getEmail());
        ret.append("\nPeopleType: ");
        ret.append(getTypeId());
        ret.append( "\nCOI: " );
        ret.append( getGroup() );
        return ret.toString();
    }

}

