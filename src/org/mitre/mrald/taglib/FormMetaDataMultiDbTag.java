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
import java.util.HashMap;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.mitre.mrald.util.DBMetaData;
import org.mitre.mrald.util.FormTags;

/**
* MultiDb: This Tag populates Multiple DBMetaData objects for use by the other Tags in the
 *  second step of the formbuilding process.
 *
 *@author    ghamilton
 *@created    August 20th, 2007
 */
public class FormMetaDataMultiDbTag extends FormMetaDataTag
{

	/**
	 *  Description of the Method
	 *
	 *@return                   Description of the Return Value
	 *@exception  JspException  Description of the Exception
	 */
	public int doStartTag()
		 throws JspException
	{
		init();
		String[] datasources = pageContext.getRequest().getParameterValues(FormTags.DATASOURCE_TAG );

		HashMap<String, DBMetaData> newDbmds = new HashMap<String, DBMetaData>();
		String[] tables = req.getParameterValues(FormTags.TABLE_TAG);

		HashMap<String, ArrayList<String>> groupedTables = groupTables(datasources, tables);

		for (int i=0; i < datasources.length; i++)
		{

			String datasource = datasources[i];
			int tableDepth = 1;
			if (req.getParameter(FormTags.TABLE_DEPTH + datasource) != null)
			{
				//Add the datasource to the TableDepth to assign the correct table depth to each datasource
				tableDepth = Integer.parseInt(req.getParameter(FormTags.TABLE_DEPTH + datasource).trim());
			}

			ArrayList<String> tableList = groupedTables.get(datasource);

			String[] tableListTemp = new String[tableList.size()];

			int j=0;
			for (String tableName: tableList)
			{
				tableListTemp[j] = tableName;
				j++;
			}
			DBMetaData newDbmd = setMetaData(datasource,tableListTemp,tableDepth);

			if (newDbmd == null)
			{
				throw new JspException("Database metadata for :" + datasource + " not found");
			}


			newDbmds.put(datasource, newDbmd);

		}

		pageContext.setAttribute("MultiDBMetaData", newDbmds, PageContext.REQUEST_SCOPE);

		return 0;
	}

	//Group tables according to the associated Datasource
	private HashMap<String, ArrayList<String>> groupTables(String[] datasources, String[] allTables)
	{
		HashMap<String, ArrayList<String>> groupedTables = new HashMap<String, ArrayList<String>>();
		for (String datasource: datasources)
		{
			ArrayList<String> tableList = new ArrayList<String>();
			for (String table: allTables)
			{

				String[] dbTable_pair = table.split("\\.");
				String dbName = dbTable_pair[0] + "." + dbTable_pair[1];
				if (dbName.equals(datasource))
				{
					tableList.add(dbTable_pair[2]);

				}
			}
			groupedTables.put(datasource, tableList);
		}

		return groupedTables;

	}
}

