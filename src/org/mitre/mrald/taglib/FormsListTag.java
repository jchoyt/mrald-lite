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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.mitre.mrald.util.Config;
import org.mitre.mrald.util.FormUtils;
import org.mitre.mrald.util.MiscUtils;
import org.mitre.mrald.util.NonPublicFilenameFilter;
import org.mitre.mrald.util.PublicFilenameFilter;
import org.mitre.mrald.util.User;
import org.mitre.mrald.util.UserFilenameFilter;
/**
 *  Description of the Class
 *
 *@author     jchoyt
 *@created    August 19, 2002
 */
public class FormsListTag extends TagSupport
{

	User user = new User();
	/**
	 *  puts in the links and icons for managing personal forms
	 */
	public final static String SERVEFORMLISTITEM = "\n<br /> " +
		"<a href=\"deleteForm.jsp?formid=<:formid:>&formAccess=<:formAccess:>\"><img src=\"images/delete.gif\" border=\"0\" /></a>" +
		"<a href=\"ShareForm.jsp?formid=<:formid:>&formAccess=<:formAccess:>\"><img src=\"images/share.gif\" border=\"0\" /></a>" +
		"<a href=\"FormServer.jsp?formid=<:formid:>&formAccess=<:formAccess:>&action=edit\"><img src=\"images/edit.gif\" border=\"0\" /></a>" +
		"<a href=\"FormServer.jsp?formid=<:formid:>&formAccess=<:formAccess:>&action=download\"><img src=\"images/down.gif\" border=\"0\" /></a>" +
		"<a href=\"uploadForm.jsp?formid=<:formid:>&formAccess=<:formAccess:>\"><img src=\"images/up.gif\" border=\"0\" /></a> ";

	private final static String PUBLICSERVEFORMLISTITEM = "\n<br /> " ;
	//+
	//"<a href=\"FormServer.jsp?formid=<:formid:>&formAccess=<:formAccess:>&action=download\"><img src=\"images/down.gif\" border=\"0\" /></a>" +
	//"<a href=\"uploadForm.jsp?formid=<:formid:>&formAccess=<:formAccess:>\"><img src=\"images/up.gif\" border=\"0\" /></a> ";



	/**
	 *  Description of the Field
	 */
	public final static String SINGLE_LINK = "<a href=\"FormServer.jsp?formid=<:formid:>&formAccess=<:formAccess:>\"><:formtitle:></a>";
	/**
	 *  Description of the Field
	 */
	public final static String DOUBLE_LINK = "<a href=\"FormServer.jsp?formid=<:formid:>-simple&formAccess=<:formAccess:>\"><:formtitle:></a> (<a href=\"FormServer.jsp?formid=<:formid:>&formAccess=<:formAccess:>\">adv</a>)";
	/****
	* List the form to be published
	*/
	public final static String PUBLISH_LINK = "<input type=\"checkbox\" name=\"formid\"  value=\"<:formid:>\" /><b><:formtitle:>: <i><:userId:></i></b><br/>";

	private final int XML_EXISTS = 4;
	private final int SIMPLE_EXISTS = 2;
	private final int ADVANCED_EXISTS = 1;

	private String formType = "Personal";


	/**
	 *  Constructor for the PersonalFormsListTag object
	 */
	public FormsListTag()
	{
		super();
	}


	/**
	 *  Retrieves and prints the user's personal forms list
	 *
	 *@return                   An int. Not used for anything.
	 *@exception  JspException  Required by TagSupport. Used ot pass up the other
	 *      exceptions.
	 */
	public int doStartTag()
		 throws JspException
	{
		User user = (User) pageContext.getSession().getAttribute(Config.getProperty("cookietag"));
		if (user == null)
		{
			throw new JspException("Trying to access your " + formType + " forms, but your session seems to have expired.  Please <a href=\"MraldLogin.jsp\">log in</a>.");
		}
		String userid = user.getEmail();
		try
		{
			File dir;
//			StringBuffer ret = new StringBuffer();
			String temp;
			TreeSet<String> set = new TreeSet<String>();

			List<String> list = new ArrayList<String>();

			dir = new File(Config.getProperty("customForms"));
			if (!dir.exists())
			{
				dir.mkdir();
			}

			if(formType.equals("Public"))
			{
				PublicFilenameFilter filter = new PublicFilenameFilter();
				list = new ArrayList<String>(Arrays.asList(filter.filter(dir)));
				userid="public";
			}
			else if(formType.equals("PublicEdit"))
			{
				PublicFilenameFilter filter = new PublicFilenameFilter();
				list = new ArrayList<String>(Arrays.asList(filter.filter(dir)));
				userid="public";
			}
			else if (formType.equals("All"))
			{
				NonPublicFilenameFilter filter = new NonPublicFilenameFilter();
				list = new ArrayList<String>(Arrays.asList(filter.filter(dir) ) );
			}
			else //(formType.equals("Personal"))
			{
				UserFilenameFilter filter = new UserFilenameFilter(userid);
				list = new ArrayList<String>(Arrays.asList(dir.list(filter)));
			}

			if (list.size() == 0)
			{
                if ( formType.equals("Personal") )
                {
                    set.add("<br /><center>You have no personal forms.</center>");
                }
                else if ( formType.equals("Public") )
                {
                    set.add( "<p>Forms for general use should be placed here. The deployers of the system should pre build " +
                            "forms that will accommodate most of the users' needs and place them here. They can be listed by " +
                            "simple links, or they can be accessed via more complex javascript or applet menu systems. There " +
                            "are several of these freely available on the internet.</p>");
                }
			}
			else if (formType.equals("All"))
			{

				String listItem;
				String formId;
				String userId;
				for (int i=0; i <list.size(); i++)
				{
					listItem = list.get(i);
					formId = getFormIdName(listItem);
					if (listItem.lastIndexOf("@") < 1) continue;
					userId =  listItem.substring(0, listItem.lastIndexOf("@"));

					temp = MiscUtils.replace(PUBLISH_LINK, "<:formid:>", formId);
					temp = MiscUtils.replace(temp, "<:formAccess:>", formType);
					temp = MiscUtils.replace(temp, "<:formtitle:>",
					getTitle(Config.getProperty("customForms") + listItem));
					temp = MiscUtils.replace(temp, "<:userId:>", userId);

					set.add(temp);
				}
			}
			else
			{
				String listItem;
				String formId;
				String titleCheck;
				int i = 0;
				int formGenState = 0;
				while (list.size() > 0 && i++ < 100)
				{
					temp = Config.EMPTY_STR;
					listItem = list.get(0);
					formId = getFormId(listItem);
					titleCheck = listItem;
					formGenState = getFormGenState(list, formId, userid);

					/*
					 *  now we have the formId - go find out what else is out there
					 */
					switch (formGenState)
					{
                        case 3:
                        //just the two jsps are there
                        case 7:
                            //all three files are here
                            temp = DOUBLE_LINK;
                            break;
                        case 4:
                            //xml file only - this link will create both jsps and show the simple form when clicked
                            temp = SINGLE_LINK;
                            break;
                        case 1:
                        //only jsp exists
                        case 2:
                        //only simple jsp exists
                        case 5:
                            //xml file and normal jsp exists - it may be an update or edit form
                            //the single link will just show the jsp
                            temp = SINGLE_LINK;
                            break;
                        case 6:
                            /*
                             *  this one is messed up.  The xml file exists and
                             *  only the simple jsp. Wipe the jsps and regen all
                             *  from scratch
                             */
                            titleCheck = FormUtils.getXmlFileName(formId, userid);
                            FormUtils.deleteJspsForForm(formId, userid);
                            temp = SINGLE_LINK;
                        default:
                            throw new RuntimeException("It appears no file exists, yet one was found.  The formId is " + formId);
					}

					removeAll(formId, userid, list);


					if (formType.equals("Public"))
						temp = MiscUtils.replace( PUBLICSERVEFORMLISTITEM + temp, "<:formid:>", formId);
					else
						temp = MiscUtils.replace(SERVEFORMLISTITEM + temp, "<:formid:>", formId);
					temp = MiscUtils.replace(temp, "<:formAccess:>", formType);
					temp = MiscUtils.replace(temp, "<:formtitle:>",
						getTitle(Config.getProperty("customForms") + titleCheck));
					//need to add to a set to elimitate duplicates due to .xml and .html files with the same user id and form number.
					set.add(temp);
				}
			}
			Iterator iter = set.iterator();
			while (iter.hasNext())
			{
				pageContext.getOut().print(iter.next());
			}
		} catch (IOException e)
		{
			throw new RuntimeException(e);
		}
		return 0;
	}


	/**
	 *  Gets the formId attribute of the PersonalFormsListTag object
	 *
	 *@param  formItem  Description of the Parameter
	 *@return           The formId value
	 */
	public String getFormId(String formItem)
	{

		String formId = formItem.substring(formItem.indexOf("_") + 1, formItem.lastIndexOf("."));
		int simpleloc = formId.lastIndexOf("-simple");
		if (simpleloc != -1)
		{
			formId = formId.substring(0, simpleloc);
		}
		return formId;
	}

	/**
	 *  Gets the formId attribute of the PersonalFormsListTag object
	 *
	 *@param  formItem  Description of the Parameter
	 *@return           The formId value
	 */
	private String getFormIdName(String formItem)
	{

		String formId = formItem.substring(0, formItem.lastIndexOf("."));
		int simpleloc = formId.lastIndexOf("-simple");
		if (simpleloc != -1)
		{
			formId = formId.substring(0, simpleloc);
		}
		return formId;
	}

	/**
	 *  Gets the formGenState attribute of the PersonalFormsListTag object
	 *
	 *@param  list    Description of the Parameter
	 *@param  formId  Description of the Parameter
	 *@param  userid  Description of the Parameter
	 *@return         The formGenState value
	 */
	private int getFormGenState(List list, String formId, String userid)
	{
		int ret = 0;
		if (list.contains(FormUtils.getJspFileName(formId, userid)))
		{
			ret += ADVANCED_EXISTS;
		}
		if (list.contains(FormUtils.getSimpleJspName(formId, userid)))
		{
			ret += SIMPLE_EXISTS;
		}
		if (list.contains(FormUtils.getXmlFileName(formId, userid)))
		{
			ret += XML_EXISTS;
		}
		return ret;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  formid  Description of the Parameter
	 *@param  list    Description of the Parameter
	 *@param  userid  Description of the Parameter
	 */
	public void removeAll(String formid, String userid, List list)
	{
		list.remove(FormUtils.getJspFileName(formid, userid));
		list.remove(FormUtils.getSimpleJspName(formid, userid));
		list.remove(FormUtils.getXmlFileName(formid, userid));
	}


	/**
	 *  The Forms can either be either Public or Personal
	 *
	 *@return                            The title value
	 */
	public String getFormType()
	{
		return formType;
	}


	/**
	 *  Sets the formType attribute of the FormsListTag object
	 *
	 *@param  thisType  The new formType value
	 */
	public void setFormType(String thisType)
	{
		formType = thisType;
	}


	/**
	 *  Gets the title attribute of the PersonalFormsListTag object
	 *
	 *@param  fileName                   Description of the Parameter
	 *@return                            The title value
	 *@exception  FileNotFoundException  Description of the Exception
	 *@exception  IOException            Description of the Exception
	 */
	String getTitle(String fileName)
		 throws FileNotFoundException, IOException
	{
		File file = new File(fileName);
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		String line = reader.readLine();
		String title = null;
		while (line != null)
		{
			if (line.indexOf("<title>") == -1)
			{
				line = reader.readLine();
			} else
			{
				/*
				 *  keep reading till you find the </title>
				 */
				String nextLine = reader.readLine();
				while (nextLine != null && line.indexOf("</title>") == -1)
				{
					line = line + nextLine;
					nextLine = reader.readLine();
				}
				/*
				 *  now get the title
				 */
				int start = line.indexOf("<title>") + 7;
				try
				{
					title = line.substring(start, line.indexOf("<", start + 1));
					title = title.replaceAll("\n|\r", "");
				} catch (StringIndexOutOfBoundsException e)
				{
					title = "None";
				}
				break;
			}
		}
		reader.close();
		return title;
	}
}
