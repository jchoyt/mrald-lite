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

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *  Description of the Class
 *
 *@author     jchoyt
 *@created    February 17, 2003
 */
public class UserFiller extends Object
{
	User user = null;
	String username = null;


	/**
	 *  Constructor for the UserFiller object
	 *
	 *@param  username  Description of the Parameter
	 *@param  user      Description of the Parameter
	 */
	public UserFiller( String username, User user )
	{
		this.username = username;
		this.user = user;
	}


	/**
	 *  Main processing method for the UserFiller object
	 */
	public void run()
	{
		if ( username == null )
		{
			NullPointerException e = new NullPointerException( "No username was given.  Please go to " + Config.getProperty( "BaseUrl" ) + "/MraldLogin.jsp and log interrupt()." );
			throw e;
		}
		try
		{
			int peopleTypeId = 0;
			String query = "select email, peopletypeid from people where people.email='" + username.toLowerCase() + "'";
			MraldConnection conn = new MraldConnection( MetaData.ADMIN_DB );
			ResultSet r = null;
			try
			{
				r = conn.executeQuery( query );
			}
			catch ( SQLException e )
			{
				if ( !MraldConnection.createPeopleTable() )
				{
					throw e;
				}

				r = conn.executeQuery( query );
			}
			if ( r.next() )
			{
				//peopleid = r.getInt( "peopleid" );
				peopleTypeId = r.getInt( "peopletypeid" );

				//user.setUserId( peopleid );
				user.setEmail( username.toLowerCase() );
				user.setTypeId( peopleTypeId );
			}
			else
			{
				storeUserInfo();
			}
			r.close();
			conn.close();
		}
		catch ( SQLException e )
		{
			throw new MraldError( e );
		}
		catch ( ClassNotFoundException e )
		{
			throw new MraldError( e );
		}
	}


	/**
	 *  Description of the Method
	 *
	 *@exception  java.sql.SQLException             Description of the Exception
	 *@exception  java.lang.ClassNotFoundException  Description of the Exception
	 */
	public void storeUserInfo()
		throws java.sql.SQLException, java.lang.ClassNotFoundException
	{

		String insert = "insert into people ( email, peopletypeid, latticegroupid ) values ( '</email/>',  1, 'Public' ) ";

		MraldConnection conn = new MraldConnection( MetaData.ADMIN_DB);

		/*
		 *  ResultSet rs = conn.executeQuery( "Select max(peopleid) from people" );
		 *  rs.next();
		 *  int id = rs.getInt( 1 ) + 1;
		 */
		//insert = MiscUtils.replace( insert, "</peopleId/>", id );
		insert = MiscUtils.replace( insert, "</email/>", username.toLowerCase() );

		conn.executeUpdate( insert );

		//user.setUserId( id );
		user.setEmail( username.toLowerCase() );
		user.setTypeId( 1 );

	}

}

