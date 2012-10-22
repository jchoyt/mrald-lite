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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.mitre.mrald.util.DBMetaData;
import org.mitre.mrald.util.FBUtils;
import org.mitre.mrald.util.Link;
import org.mitre.mrald.util.MetaData;
import org.mitre.mrald.util.MiscUtils;
import org.mitre.mrald.util.TableMetaData;
/**
 *  Shows the structure of the database.
 *
 *@author     ghamilton
 *@created    January 26, 2005
 */
public class DisplayDBTag extends TagSupport
{

	/**
	 *  Description of the Field
	 */

	DBMetaData md;
	ServletRequest req;
    String datasource;
	String schema;

	private String open="false";

	private static String SPACER = "<img alt=\"\" src=\"images/spacer.gif\" width=\"<:width:>\" height=\"1\"/>";
	private static String STARTER = "<:DisplayName:><a onClick=\"toggle('<:IDName:>', 'images/')\" ><img id=\"<:IDName:>Image\" src=\"images/plus.gif\"></img></a><br /><div id=\"<:IDName:>\" style=\"display:none;\"><br/>";
	private static String END = "</div>";


	/**
	 *  Description of the Method
	 *
	 *@return                   Description of the Return Value
	 *@exception  JspException  Description of the Exception
	 */
	public int doStartTag()
		 throws JspException
	{
		//Set up variables.
		init();
		//Display all the Tables in a tree view
		//Cycle through all the data in MetaData

		try
		{
			String divLine = STARTER.replaceAll("<:IDName:>", "DataBase_" + datasource);
			divLine = divLine.replaceAll("<:DisplayName:>", "<b><i>Database Schema </i></b>");

			if (open.equals("true"))
				divLine = divLine.replaceAll("display:none", "display:block");

			pageContext.getOut().println("<img src=\"images/db.png\" width=\"30\" height=\"30\"/>");
			pageContext.getOut().println(divLine);
			//pageContext.getOut().println("<b><i><p class=\"sideBarTitle\">Database Tables</p></i></b>");
			pageContext.getOut().println(displayTables());
			pageContext.getOut().flush();
			//pageContext.getOut().println("<br/><br/><b><i><p class=\"sideBarTitle\">Database Keys</p></i></b>");
			pageContext.getOut().println(displayKeys());
			pageContext.getOut().println(END);

			pageContext.getOut().flush();
		} catch (java.io.IOException ioe)
		{
			throw new JspException(ioe);
		}

		return 0;
	}


	/**
	 *  Description of the Method
	 *
	 *@exception  JspException  Description of the Exception
	 */
	void init()
		 throws JspException
	{
        md = MetaData.getDbMetaData( datasource );
		req = pageContext.getRequest();
		// schema = MetaData.getDbProperties( datasource ).getProperty("SCHEMA");
	}


	/**
	 *  Description of the Method
	 *
	 *@return                   Description of the Return Value
	 *@exception  JspException  Description of the Exception
	 */
	private String displayTables()
		 throws JspException
	{

		String width = "25";
		StringBuffer strOut = new StringBuffer();
		ArrayList tables = (ArrayList) md.getAllTableMetaData();
		Iterator iter = tables.iterator();

		HashMap linksMap = getFKeysPerTable();
		while (iter.hasNext())
		{
			TableMetaData table = (TableMetaData) iter.next();
			String tableName = table.getName();
			strOut.append(SPACER.replaceAll("<:width:>", width));
			String divLine = MiscUtils.replace(STARTER,  "<:IDName:>", "Table_" + datasource + tableName);
			divLine = MiscUtils.replace( divLine, "<:DisplayName:>", "<b><i>" + tableName + "</i></b>");

			strOut.append("<img src=\"images/table.jpg\" width=\"17\" height=\"17\"/>");
			strOut.append(MiscUtils.replace(SPACER, "<:width:>","10"));
			strOut.append(divLine);
			strOut.append(displayColumns(table));
			strOut.append(displayPKeys(table));
			strOut.append(displayKeyData(tableName, linksMap));
			strOut.append(END);
		}

		return strOut.toString();
	}


	/**
	 *  Description of the Method
	 *
	 *@param  table             Description of the Parameter
	 *@return                   Description of the Return Value
	 *@exception  JspException  Description of the Exception
	 */
	private String displayColumns(TableMetaData table)
		 throws JspException
	{
		String width = "50";

		StringBuffer strOut = new StringBuffer();

		Collection columns = table.getColumnNames();
		Iterator iter = columns.iterator();

		while (iter.hasNext())
		{

			String columnName = (String) iter.next();
			strOut.append(MiscUtils.replace( SPACER, "<:width:>", width));
			String divLine = MiscUtils.replace( STARTER, "<:IDName:>", "Column_" + datasource + table.getName() + "_" + columnName);
			divLine = MiscUtils.replace( divLine, "<:DisplayName:>", "<b>" + columnName + "</b>");

			strOut.append(divLine);
			strOut.append(displayColumnDetails(table, columnName));
			strOut.append(END);
		}
		// strOut.append("<br />");
		return strOut.toString();
	}


	/**
	 *  Description of the Method
	 *
	 *@param  table             Description of the Parameter
	 *@return                   Description of the Return Value
	 *@exception  JspException  Description of the Exception
	 */
	private String displayPKeys(TableMetaData table)
		 throws JspException
	{
		String width = "50";

		StringBuffer strOut = new StringBuffer("");

		strOut.append(MiscUtils.replace( SPACER, "<:width:>", width));
		Collection pKeys = table.getPrimaryKeys();
		Iterator iter = pKeys.iterator();

		strOut.append("<img src=\"images/pkey.png\" width=\"20\" height=\"20\"/>");
		strOut.append(MiscUtils.replace( SPACER, "<:width:>", "10"));

		strOut.append("<i>Primary Keys</i><br/>");

		width = "75";
		while (iter.hasNext())
		{

			String primaryKey = (String) iter.next();
			strOut.append(MiscUtils.replace( SPACER, "<:width:>", width));
			//String divLine = STARTER.replaceAll("<:IDName:>", "PKey_" + table.getName() + "_" + primaryKey);
			//divLine = divLine.replaceAll("<:DisplayName:>", "<b>" + primaryKey + "</b>");

			strOut.append("<b>" +  primaryKey  + "</b><br/>");
			//strOut.append(divLine);
			//strOut.append(END);
		}
		// strOut.append("<br />");
		return strOut.toString();
	}


	/**
	 *  Description of the Method
	 *
	 *@param  table             Description of the Parameter
	 *@param  columnName        Description of the Parameter
	 *@return                   Description of the Return Value
	 *@exception  JspException  Description of the Exception
	 */
	private String displayColumnDetails(TableMetaData table, String columnName)
		 throws JspException
	{
		String width = "75";
		StringBuffer strOut = new StringBuffer();
		strOut.append(MiscUtils.replace( SPACER, "<:width:>", width));
		strOut.append("Type: " +FBUtils.getSqlType(table.getFieldType(columnName).intValue()) + "<br/>");
		strOut.append(MiscUtils.replace( SPACER, "<:width:>", width));
		strOut.append("Comments: " + table.getFieldComments(columnName) + "<br/>");
		strOut.append("<br/>");
		return strOut.toString();
	}



	/**
	 *  Description of the Method
	 *
	 *@return                   Description of the Return Value
	 *@exception  JspException  Description of the Exception
	 */
	private String displayKeys()
		 throws JspException
	{

		String width = "25";
		StringBuffer strOut = new StringBuffer("<br/><br/>");
		strOut.append(MiscUtils.replace( SPACER, "<:width:>", width));
		String divLine = MiscUtils.replace( STARTER, "<:IDName:>", "Keys_" + datasource );
		divLine = MiscUtils.replace( divLine, "<:DisplayName:>", "<b><i>Database Keys</i></b>");
		strOut.append(divLine);

		Set linkList = md.getLinkList();
		Iterator linkIter = linkList.iterator();

		HashMap<String,ArrayList<Link>> keysMap = new HashMap<String,ArrayList<Link>>();
//		Link[] keyList;
		//Loop through the LinkList, and if PKey table or FKey table is in Table List

		//Loop through the list of LInks and re order them to associate themwith Primary key
		while (linkIter.hasNext())
		{
			Link link = (Link) linkIter.next();

			String pTable = link.getFtable() + "_" + link.getPtable();
			if (!(keysMap.containsKey(pTable)))
			{
				ArrayList<Link> keys = new ArrayList<Link>();
				keys.add(link);
				keysMap.put(pTable, keys);
			} else
			{
				ArrayList<Link> keys = keysMap.get(pTable);
				keys.add(link);
				keysMap.put(pTable, keys);
			}

		}

		Iterator linkMapIter = keysMap.keySet().iterator();

		while (linkMapIter.hasNext())
		{
			String pKeyTable = (String) linkMapIter.next();
			ArrayList<Link> links = keysMap.get(pKeyTable);

			strOut.append(MiscUtils.replace( SPACER, "<:width:>", width));
			strOut.append("<img src=\"images/keys.png\" width=\"25\" height=\"25\"/>");
			strOut.append(MiscUtils.replace( SPACER, "<:width:>", "10"));

			for (int i = 0; i < links.size(); i++)
			{
				Link link = links.get(i);
				if (i == 0)
				{
					divLine = MiscUtils.replace( STARTER, "<:IDName:>", "Link_" + datasource + link.getPtable() + "_" + link.getFtable());
					divLine = MiscUtils.replace( divLine, "<:DisplayName:>", "<b>" + link.getPtable() + " : " + link.getFtable() + "</b>");
					strOut.append(divLine);
				}
				strOut.append(displayKeyDetails(link));

			}
			strOut.append(END);
		}
		strOut.append(END);

		return strOut.toString();
	}


	/**
	 *  Description of the Method
	 *
	 *@param  keyName  Description of the Parameter
	 *@return          Description of the Return Value
	 */
	private String displayKeyData(String keyTable, HashMap keysMap)
	{
		String width = "50";
		StringBuffer strOut = new StringBuffer();
		//MraldOutFile.logToFile(Config.getProperty("LOGFILE"), "DisplayDBTag:  displayKeyData : " + keyTable);

		ArrayList links = (ArrayList) keysMap.get(keyTable);

		if (links == null)
			return "";

		strOut.append(MiscUtils.replace( SPACER, "<:width:>", width));
		String divLine;
		strOut.append("<img src=\"images/fkey.png\" width=\"20\" height=\"20\"/>");
		strOut.append(MiscUtils.replace( SPACER, "<:width:>", "10"));

		StringBuffer delayStrOut = new StringBuffer();

		for (int i = 0; i < links.size(); i++)
		{
			Link link = (Link) links.get(i);
			if (i == 0)
			{
				divLine = MiscUtils.replace( STARTER, "<:IDName:>", "Link_" + datasource + keyTable + " _" + link.getPtable() + "_" + link.getFtable());
				divLine = MiscUtils.replace( divLine, "<:DisplayName:>", "<i>Relations</i>");
				strOut.append(divLine);
			}
			//Make sure that the Primary Keys appear first
			if (link.getPtable().equals(keyTable))
				strOut.append(displayKeyDetailsInTable(link, keyTable));
			else
				delayStrOut.append(displayKeyDetailsInTable(link, keyTable));
		}

		strOut.append(delayStrOut);
		strOut.append(END);
		strOut.append("<br/>");
		return strOut.toString();
	}


	/**
	 *  Description of the Method
	 *
	 *@return                   Description of the Return Value
	 *@exception  JspException  Description of the Exception
	 */
	private HashMap  getFKeysPerTable()
		 throws JspException
	{

		Set linkList = md.getLinkList();
		Iterator linkIter = linkList.iterator();

		HashMap<String,ArrayList<Link>> keysMap = new HashMap<String,ArrayList<Link>>();
//		Link[] keyList;
		//Loop through the LinkList, and if PKey table or FKey table is in Table List

		//Loop through the list of LInks and re order them to associate themwith Primary key
		while (linkIter.hasNext())
		{
			Link link = (Link) linkIter.next();
			String pTable = link.getPtable();
			if (!(keysMap.containsKey(pTable)))
			{
				ArrayList<Link> keys = new ArrayList<Link>();
				keys.add(link);
				keysMap.put(pTable, keys);
			} else
			{
				ArrayList<Link> keys = keysMap.get(pTable);
				keys.add(link);
				keysMap.put(pTable, keys);
			}

			String fTable = link.getFtable();

			if (!(keysMap.containsKey(fTable)))
			{
				ArrayList<Link> keys = new ArrayList<Link>();
				keys.add(link);
				keysMap.put(fTable, keys);
			} else
			{
				ArrayList<Link> keys = keysMap.get(fTable);
				keys.add(link);
				keysMap.put(fTable, keys);
			}

		}

		return keysMap;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  link              Description of the Parameter
	 *@return                   Description of the Return Value
	 *@exception  JspException  Description of the Exception
	 */
	private String displayKeyDetails(Link link)
	{
		String width = "75";
		StringBuffer strOut = new StringBuffer();

		strOut.append(MiscUtils.replace( SPACER, "<:width:>", width));
		strOut.append("PK : <b>" + link.getPtable() + "</b>." + link.getPcolumn() + "<br/>");
		strOut.append(MiscUtils.replace( SPACER, "<:width:>", width));
		strOut.append("FK:  <b>" + link.getFtable() + "</b>." + link.getFcolumn() + "<br/>");
		strOut.append("<br/>");
		return strOut.toString();
	}

	/**
	 *  Description of the Method
	 *
	 *@param  link              Description of the Parameter
	 *@return                   Description of the Return Value
	 *@exception  JspException  Description of the Exception
	 */
	private String displayKeyDetailsInTable(Link link, String tableName)
	{
		String width = "75";
		StringBuffer strOut = new StringBuffer();
		strOut.append(MiscUtils.replace( SPACER,"<:width:>", width));

		strOut.append("<img src=\"images/table.jpg\" width=\"17\" height=\"17\"/>");
		strOut.append(MiscUtils.replace( SPACER, "<:width:>","10"));

		width = "100";
		if (link.getPtable().equals(tableName))
			strOut.append("<b>" + link.getFtable() + "</b><br/>");
		else
			strOut.append("<b>" + link.getPtable() + "</b><br/>");

		strOut.append(MiscUtils.replace( SPACER, "<:width:>", width));
		strOut.append("PK : <b>" + link.getPtable() + "</b>." + link.getPcolumn() + "<br/>");
		strOut.append(MiscUtils.replace( SPACER, "<:width:>", width));
		strOut.append("FK :  <b>" + link.getFtable() + "</b>." + link.getFcolumn() + "<br/>");
		//strOut.append("<br/>");
		return strOut.toString();
	}

	/**
	 *  Description of the Method
	 *
	 *@param  table             Description of the Parameter
	 *@return                   Description of the Return Value
	 *@exception  JspException  Description of the Exception
	 */
	public String getOpen()
	{
		return open;
	}
	public void setOpen(String thisOpen)
	{
		open = thisOpen;
	}

    public void setDatasource(String ds)
    {
        datasource = ds;
    }
}

