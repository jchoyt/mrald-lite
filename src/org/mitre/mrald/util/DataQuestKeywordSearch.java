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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
/**
 *  Description of the Class
 *
 *@author     ghamilton
 *@created    Jan 20th , 2005
 */
public class DataQuestKeywordSearch extends KeywordSearch
{

	/**
	 *  Constructor for the AllTablesListTag object
	 */
	public DataQuestKeywordSearch()
	{
		super();
	}


	/**
	 *  Description of the Method
	 *
	 *@param  searchParams        Description of the Parameter
	 *@return                     Description of the Return Value
	 *@exception  MraldException  Description of the Exception
	 */
	public String search(String[] searchParams) throws MraldException
	{
		try
		{
			Connection conn = new MraldConnection( "main" ).getConnection();

			Statement stmt = conn.createStatement();

			ArrayList<String> headers = new ArrayList<String>();

			StringBuffer strRtn = new StringBuffer();
			headers.add("piname");
			headers.add("granttitle");
			headers.add("fundingagency");
			headers.add("datasetid");
			String headerStr = "<tr><th colspan=\"2\" bgcolor=\"#ccccff\"><:" + headers.get(0).toString() + ":> - <:" + headers.get(1).toString() + ":> - <:" + headers.get(2).toString() + ":> (<a href=\"details.jsp?DataSetID=<:" + headers.get(3).toString() + ":>\">Details</a>)</th></tr>";

			if (searchParams.length < 1)
			{
				strRtn.append("\nYou must enter a search criteria.\n");
				return strRtn.toString();
			}
			//Must enter a search parameter

			/*
			 *  all tables and their metadata should now be in memory
			 */
            DBMetaData md = MetaData.getDbMetaData( datasource );

			Collection tableMetaData = md.getAllTableMetaData();
			TableMetaData tableInfo;
			Iterator iter = tableMetaData.iterator();
//			int orderNo = 1;
			/*
			 *  for each table . . .
			 */
			ResultSet rs;
			while (iter.hasNext())
			{
				tableInfo = (TableMetaData) iter.next();


				ArrayList fieldNames = (ArrayList) tableInfo.getColumnNames();
				Iterator iter2 = fieldNames.iterator();
				/*
				 *  and for each field, output a row of options
				 */
				String field;
				String whereClause = "";
				if (iter2 == null)
				{
					// MraldOutFile.logToFile(Config.getProperty("LOGFILE"), "Moogle:   No field info found ");
				} else if (!iter2.hasNext())
				{
					// MraldOutFile.logToFile(Config.getProperty("LOGFILE"), "Moogle:   No field info found ");
				}

				while (iter2.hasNext())
				{
					field = (String) iter2.next();

					Integer type = tableInfo.getFieldType(field);

					if (!FBUtils.isNumberType(type) && !FBUtils.isDateType(type) && !FBUtils.isBooleanType(type))
					{
						whereClause += " UPPER (" + field + ") like '%" + searchParams[0].toUpperCase() + "%' OR ";
					}
				}
				if (whereClause.equals(""))
				{
					continue;
				}
				whereClause += "1=0";

				String query = "select * from " + tableInfo.getName() + " where " + whereClause;

				rs = stmt.executeQuery(query);


				StringBuffer strOut = new StringBuffer();
				String projectId="-1";
				String dataSetId="-1";
				while (rs.next())
				{
					headerStr = "<TR><TH COLSPAN=\"2\" BGCOLOR=\"#ccccff\"><:" + headers.get(0).toString() + ":> - <:" + headers.get(1).toString() + ":> - <:" + headers.get(2).toString() + ":> (<A HREF=\"details.jsp?DataSetID=<:" + headers.get(3).toString() + ":>\">Details</A>)</TH></TR>";

					strOut = new StringBuffer();
					strOut.append("\n\t");
					for (int i = 0; i < fieldNames.size(); i++)
					{

						String value = rs.getString(i + 1);
						if (value == null)
						{
							continue;
						}

						String fieldName = fieldNames.get(i).toString().toLowerCase();
						//do not show private data
						if (fieldName.equals("email") || fieldName.equals("phone") )
						{
							continue;
						}
						if (fieldName.equals("projectid"))
						{
							projectId = value;
						}
						if (fieldName.equals("datasetid"))
						{
							dataSetId = value;
						}

						if (headers.contains(fieldName))
						{

							headerStr = headerStr.replaceAll("<:" + fieldName + ":>", value);
						}
						strOut.append("<TR><TD ALIGN=\"right\" WIDTH=\"15%\">");
						strOut.append(fieldNames.get(i).toString());
						strOut.append("</TD>");
						strOut.append("<TD>&nbsp;" + value + "</TD></TR>");

					}
					if (tableInfo.getName().toLowerCase().equals("dataset"))
					{
						headerStr = getProjectInfo(headerStr, dataSetId, conn);
					}
					if (tableInfo.getName().toLowerCase().equals("project"))
					{
						headerStr = headerStr.replaceAll("DataSetID", "ProjectID");
						headerStr = headerStr.replaceAll("<:datasetid:>",projectId);
					}
					strRtn.append(headerStr + strOut.toString());
				}
			}
			return strRtn.toString();
		} catch (SQLException e)
		{
			MraldException se = new MraldException(e);
			throw se;
		}
	}


	/**
	 *  Description of the Method
	 *
	 *@param  header              Description of the Parameter
	 *@param  datasetid           Description of the Parameter
	 *@param  conn                Description of the Parameter
	 *@return                     The projectInfo value
	 *@exception  MraldException  Description of the Exception
	 */
	private String getProjectInfo(String header, String datasetid, Connection conn) throws MraldException
	{
		try
		{

			String sql = "select granttitle, grantnumber, fundingagency, piname from project, dataset where datasetid = '" + datasetid + "'";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			//headers.add("piname");
			//headers.add("granttitle");
			//headers.add("fundingagency");

			if (rs.next())
			{

				header = header.replaceAll("<:piname:>", rs.getString("piname"));
				header = header.replaceAll("<:granttitle:>", rs.getString("granttitle"));
				header = header.replaceAll("<:fundingagency:>", rs.getString("fundingagency"));
			}

			return header;
		} catch (SQLException e)
		{
			MraldException se = new MraldException(e);
			throw se;
		}

	}
}

