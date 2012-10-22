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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.mitre.mrald.control.MsgObject;
import org.mitre.mrald.util.DBMetaData;
import org.mitre.mrald.util.Link;
import org.mitre.mrald.util.MetaData;
import org.mitre.mrald.util.MiscUtils;
import org.mitre.mrald.util.MraldConnection;
import org.mitre.mrald.util.MraldException;
import org.mitre.mrald.util.TableMetaData;
/**
 *
 *
 *@author     ghamilton
 *@created    January 26, 2005
 */
public class DisplayTableTag extends TagSupport
{

    public String datasource = "";

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

		//Display all the Tables in a tree view
		//Cycle through all the data in MetaData

		try
		{
			ServletRequest req = pageContext.getRequest();
			HashMap parameters = (HashMap) req.getParameterMap();

			String[] tables = (String[]) parameters.get("Table");

			HashMap newParams = reformatHashMap(parameters);
			pageContext.getOut().println(getTableData(tables, newParams));


		} catch (MraldException e)
		{
			throw new JspException(e);
		}
		catch (java.io.IOException ioe)
		{
			throw new JspException(ioe);
		}
		return 0;
	}


	/**
	 *  Gets the tableData attribute of the DisplayTableTag object
	 *
	 *@param  tables              Description of the Parameter
	 *@param  parameters          Description of the Parameter
	 *@return                     The tableData value
	 *@exception  MraldException  Description of the Exception
	 */
	public String getTableData(String[] tables, HashMap parameters) throws MraldException
	{
		try
		{
			Connection conn = new MraldConnection(datasource,
                new MsgObject((HttpServletRequest)pageContext.getRequest(),
                    (HttpServletResponse)pageContext.getResponse() ) )
                .getConnection();
            DBMetaData md = MetaData.getDbMetaData( datasource );
			Statement stmt = conn.createStatement();
			StringBuffer strRtn = new StringBuffer();

			StringBuffer strOut = new StringBuffer();

//			String value = "";

			String HEADER = "<center><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\"  bgcolor=\"#ffffff\"><tr><td class=\"bord\"><table cellspacing=\"1\" cellpadding=\"1\" border=\"0\" width=\"100%\">";
			String FOOTER = "</table> </td></tr></table></center><br/>";
			String title = "";
			boolean rowFound = false;
			boolean anyFound = false;

			String query = "";

			ResultSet rs=null ;
			for (int j = 0; j < tables.length; j++)
			{
				title = "<tr><th colspan=\"<:colspan:>\">Table : <:table:></th></tr>";
				strOut = new StringBuffer();
				rowFound = false;
				query = " Select * from <:tableName:> where ";
				String table = tables[j];

                //get metaData IF CANNOT FIND then tableInfo = null
				TableMetaData tableInfo = md.getTableMetaData(table);

                ////BEGIN OF BLOCK
                if (tableInfo != null ) {
				   strOut.append(HEADER);

				   query = query.replaceAll("<:tableName:>", "\""+ tableInfo.getName()+ "\"");

				   Set<String> params = getPK(tableInfo, parameters);
				   params = getFK(md.getLinkList(), tableInfo, parameters, params);

				   query = query + getWhereParams(params);

				   rs = stmt.executeQuery(query);

				   ArrayList fieldNames = (ArrayList) tableInfo.getColumnNames();

				   title = title.replaceAll("<:colspan:>", fieldNames.size() + "");
				   title = title.replaceAll("<:table:>", table);
				   strOut.append(title);
				   strOut.append("<tr>");
				   for (int i = 0; i < fieldNames.size(); i++)
				   {
					  String field = (String) fieldNames.get(i);
					  strOut.append("<td><h2>" + field + "</h2></td>");

				   }
				   strOut.append("</tr>");

				   anyFound = true;
				   while (rs.next())
				   {
					   rowFound = true;

					   strOut.append("<tr>");
					   for (int i = 0; i < fieldNames.size(); i++)
					   {
						   strOut.append("<td>" + rs.getString(i + 1) + "</td>");

					   }
					   strOut.append("</tr>");
				    }
				    strOut.append(FOOTER);
				    if (rowFound)
				    {
					    strRtn.append(strOut.toString());
				    } else
				    {
					    strRtn.append(HEADER + title + "<td align=\"center\"><b><i>No references found in this table</i></b></td>" + FOOTER);
				    }
                 }//endif
				 //END OF BLOCK
			 }//endfor
			if (!anyFound)
			{
				strRtn.append("<tr><th>No other tables in the database reference this data</th></tr>");
				//strRtn.append(strOut.toString());
			}

			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
			if (conn != null)
				conn.close();

			return strRtn.toString();

		} catch (SQLException e)
		{
			throw new MraldException(e);
		}
	}


	/**
	 * Method to put the hashMap into a format that can be used for input in getTableData
	 *
	 *@param  inputHashMap  Description of the Parameter
	 *@return               Description of the Return Value
	 */
	private HashMap reformatHashMap(HashMap inputHashMap)
	{
		HashMap<String,String> outputHashMap = new HashMap<String,String>();

		Iterator iterKey = inputHashMap.keySet().iterator();

		while (iterKey.hasNext())
		{
			String key = (String) iterKey.next();
			String[] values;
			String value;
			if ((values = (String[]) inputHashMap.get(key)) != null)
			{
				value = values[0];
				if (value != null)
				{
					outputHashMap.put(key, value);
				}
			}
		}
		return outputHashMap;
	}


	/**
	 *  Gets the pK attribute of the DisplayTableTag object
	 *
	 *@param  tableInfo  Description of the Parameter
	 *@param  paramMap   Description of the Parameter
	 *@return            The pK value
	 */
	private Set<String> getPK(TableMetaData tableInfo, HashMap paramMap)
	{
		Iterator keyIter = tableInfo.getPrimaryKeys().iterator();

		Set<String> params = new HashSet<String>();

//		StringBuffer queryRtn = new StringBuffer();
		String value;
		int j = 0;
		while (keyIter.hasNext())
		{

			String pkName = keyIter.next().toString();

			//String[] values;
			//if ( (values = (String[]) paramMap.get(pkName)) != null)

			//{
			//value = values[0];
			value = (String) paramMap.get(pkName);
			if (value == null || value.equals(""))
			{
				continue;
			} else
			{
				value =  MiscUtils.checkApostrophe(value);

				params.add(pkName + " = '" + value + "'");
				//queryRtn.append(pkName + " = '" + value + "'");
			}
			//}
			j++;
		}

		return params;
	}


	/**
	 *  Gets the pK attribute of the DisplayTableTag object
	 *
	 *@param  tableInfo  Description of the Parameter
	 *@param  links      Description of the Parameter
	 *@param  params     Description of the Parameter
	 *@param  paramMap   Description of the Parameter
	 *@return            The pK value
	 */
	//private Set getFK(Set links, TableMetaData tableInfo, ServletRequest req, Set params)
	private Set<String> getFK(Set links, TableMetaData tableInfo, HashMap paramMap, Set<String> params)
	{
		Iterator linkIter = links.iterator();

		String value;
		String fkName = "";

		int j = 0;
		String table = tableInfo.getName();
		while (linkIter.hasNext())
		{
			Link link = (Link) linkIter.next();
			if (link.getPtable().equals(table))
			{
				fkName = link.getPcolumn();
			} else if (link.getFtable().equals(table))
			{
				fkName = link.getFcolumn();
			}

			//String[] values;
			//if ( (values = (String[]) paramMap.get(fkName)) != null)
			//{
			//value = values[0];
			value = (String) paramMap.get(fkName);
			if (value == null || value.equals(""))
			{
				continue;
			} else
			{
				value =  MiscUtils.checkApostrophe(value);
				params.add(fkName + " = '" + value + "'");
			}
			//}
			j++;
		}

		return params;
	}


	/**
	 *  Gets the whereParams attribute of the DisplayTableTag object
	 *
	 *@param  params  Description of the Parameter
	 *@return         The whereParams value
	 */
	private String getWhereParams(Set params)
	{
		StringBuffer queryRtn = new StringBuffer();
		Iterator paramIter = params.iterator();
		int i = 0;
		while (paramIter.hasNext())
		{

			if (i > 0)
			{
				queryRtn.append(" AND ");
			}

			queryRtn.append(paramIter.next().toString());
			i++;
		}
		return queryRtn.toString();
	}


    public void setDatasource(String ds)
    {
        datasource = ds;
    }

}

