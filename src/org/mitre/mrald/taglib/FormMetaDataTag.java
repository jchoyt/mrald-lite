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


import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.mitre.mrald.util.DBMetaData;
import org.mitre.mrald.util.FormTags;
import org.mitre.mrald.util.MetaData;
import org.mitre.mrald.util.MraldOutFile;

/**
 *  This Tag populates a DBMetaData object for use by the other Tags in the
 *  second step of the formbuilding process.
 *
 *@author     jchoyt
 *@created    January 26, 2003
 */
public class FormMetaDataTag extends TagSupport
{

	protected ServletRequest req;

	protected boolean listAllLinkTables = false;


	/**
	 *  Description of the Method
	 *
	 *@return                   Description of the Return Value
	 *@exception  JspException  Description of the Exception
	 */
	public int doStartTag()
		 throws JspException
	{
		req = pageContext.getRequest();
        String datasource = pageContext.getRequest().getParameter( FormTags.DATASOURCE_TAG );
        String[] tables = req.getParameterValues(FormTags.TABLE_TAG);

        if( tables == null || tables.length == 0 )
		{
			throw new JspException("No tables were chosen in the previous step.  Please go back and choose one or more tables.");
		}
		int tableDepth = 1;
		if (req.getParameter(FormTags.TABLE_DEPTH) != null)
		{
			tableDepth = Integer.parseInt(req.getParameter(FormTags.TABLE_DEPTH).trim());
		}
        DBMetaData newDbmd = setMetaData(datasource, tables, tableDepth);

        pageContext.setAttribute("DBMetaData", newDbmd, PageContext.PAGE_SCOPE);

		return 0;
	}

	//GH MultiDB: Put process into a method to allow for calling multiple times
	protected DBMetaData setMetaData(String datasource, String[] tables, int tableDepth) throws JspException
	{


		try
		{
            DBMetaData md = MetaData.getDbMetaData( datasource );
            DBMetaData newDbmd = MetaData.getDataSubSet(md, tables, tableDepth);
            newDbmd.setDbProps(md.getDbProps());
            return newDbmd;
		} catch (Exception e)
		{
			//Log the error but don't interrupt the process
			MraldOutFile.logToFile( e );
			return null;
		}
	}
	/**
	 *  Description of the Method
	 *
	 *@exception  JspException  Description of the Exception
	 */
	void init()
		 throws JspException
	{
	}



	/**
	 *  Sets the listAllLinkTables attribute of the DBMetaDataTag object
	 *
	 *@param  listAllLinkTables  The new listAllLinkTables value
	 */
	public void setListAllLinkTables(boolean listAllLinkTables)
	{
		this.listAllLinkTables = listAllLinkTables;
	}


	/**
	 *  Sets the listAllLinkTables attribute of the FormMetaDataTag object
	 *
	 *@param  bool  The new listAllLinkTables value
	 */
	public void setListAllLinkTables(String bool)
	{
		listAllLinkTables = Boolean.getBoolean(bool);
	}


	/**
	 *  Gets the listAllLinkTables attribute of the DBMetaDataTag object
	 *
	 *@return    The listAllLinkTables value
	 */
	public boolean getListAllLinkTables()
	{
		return this.listAllLinkTables;
	}
}

