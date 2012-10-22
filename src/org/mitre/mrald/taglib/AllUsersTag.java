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
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import org.mitre.mrald.util.Config;
import org.mitre.mrald.util.MetaData;
import org.mitre.mrald.util.MiscUtils;
import org.mitre.mrald.util.MraldConnection;
import org.mitre.mrald.util.MraldOutFile;
import org.mitre.mrald.util.User;


/**
 *  Description of the Class
 *
 *@author     jchoyt
 *@created    October 9, 2002
 */
public class AllUsersTag extends TagSupport
{

	User user;
	String userid;


	/**
	 *  Constructor for the AllUsersTag object
	 */
	public AllUsersTag() { }


	/**
	 *  Description of the Method
	 *
	 *@return                   Description of the Return Value
	 *@exception  JspException  Description of the Exception
	 */
	public int doStartTag()
		throws JspException
	{
//		int scope = pageContext.SESSION_SCOPE;
		//user = ( User ) pageContext.getAttribute( Config.getProperty( "cookietag" ), scope );

		user = ( User ) pageContext.getSession().getAttribute( Config.getProperty( "cookietag" ) );

		userid = user.getEmail();
		try
		{
			MraldConnection conn = new MraldConnection( MetaData.ADMIN_DB );
			String GETUSERS = "SELECT email FROM people WHERE email !='<:email:>' ORDER BY email ";
			String SERVEUSERLISTITEM = "<input type='checkbox' name='shareUserId' size='30' maxlength='80' value='<:email:>' ><strong><:email:></strong><br>" + System.getProperty( "line.separator" );
			String query = MiscUtils.replace( GETUSERS, "<:email:>", user.getEmail() );
//            System.out.println( query );
			ResultSet r = conn.executeQuery( query );
			MraldOutFile.appendToFile( Config.getProperty( "DBLOGFILE" ), query );
			StringBuffer ret = new StringBuffer();
			while ( r.next() )
			{
				//String replacement = MiscUtils.replace( SERVEUSERLISTITEM, "<:peopleid:>", r.getString( "peopleid" ) );
				String replacement = MiscUtils.replace( SERVEUSERLISTITEM, "<:email:>", r.getString( "email" ) );
				ret.append( replacement );
			}
			pageContext.getOut().print( ret.toString() );
			return 0;
		}
		catch ( SQLException e )
		{
			throw new JspException( e );
		}
		catch ( IOException e )
		{
			throw new JspException( e );
		}
	}
}

