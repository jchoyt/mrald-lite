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
import java.io.Writer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Iterator;

import org.mitre.mrald.control.AbstractStep;
import org.mitre.mrald.control.MsgObject;
import org.mitre.mrald.control.WorkflowStepException;
/**
 *  Description of the Class
 *
 *@author     ghamilton
 *@created    Jan 20th , 2005
 */
public class KeywordSearch extends AbstractStep
{

	/**
	 *  Constructor for the AllTablesListTag object
	 */
	public KeywordSearch() { }
    protected String datasource;
    private MsgObject msg;
	/**
	 *  Description of the Method
	 *
	 *@param  msg                        Description of the Parameter
	 *@exception  WorkflowStepException  Description of the Exception
	 */
	public void execute(MsgObject msg) throws WorkflowStepException
	{
        this.msg = msg;
		try
		{
			String[] searchParams = msg.getValue("term");
            datasource = msg.getValue(FormTags.DATASOURCE_TAG)[0];
			java.io.PrintWriter out = msg.getOutPrintWriter();

			search(searchParams, out);
			out.write("\n");
			out.close();
		} catch (MraldException me)
		{
			throw new WorkflowStepException(me);
		}
	}

    public void setDatasource(String datasource)
    {
        this.datasource = datasource;
    }


	/**
	 *  Description of the Method
	 *
	 *@param  searchParams        Description of the Parameter
	 *@param  out                 Description of the Parameter
	 *@exception  MraldException  Description of the Exception
	 */
	public void search(String[] searchParams, Writer out) throws MraldException
	{
		try
		{
			Connection conn = new MraldConnection( datasource, msg ).getConnection();

			if (searchParams.length < 1)
			{
				out.write("\nYou must enter a search criteria.\n");
				return;
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
			while (iter.hasNext())
			{
				tableInfo = (TableMetaData) iter.next();
				out.write("\nChecking: " + tableInfo.getName() + "\n");
				// System.outBuf.append("\nChecking: " + tableInfo.getName());
				if (tableInfo.getName().indexOf(searchParams[0]) != -1)
				{
					out.write("\tTable name matches!\n");
				}
				Collection fieldNames = tableInfo.getColumnNames();
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

					if (field.indexOf(searchParams[0]) != -1)
					{
						out.write("\t" + field + " field name matches!\n");
					}
					Integer type = tableInfo.getFieldType(field);
					if (!FBUtils.isNumberType(type) && !FBUtils.isDateType(type) && !FBUtils.isBooleanType(type))
					{
						whereClause += field + " like '%" + searchParams[0] + "%' OR ";
					}
				}
				if (whereClause.equals(""))
				{
					out.flush();
					continue;
				}
				whereClause += "1=0";
				String query = "select * from " + tableInfo.getName() + " where " + whereClause;
				out.write("\trunning: " + query + "\n");

				out.flush();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				while (rs.next())
				{
					out.write("\n\t");
					for (int i = 0; i < fieldNames.size(); i++)
					{
						String value = rs.getString(i + 1);
						if (value == null)
						{
							continue;
						}
						out.write(value);
						out.write("\t");
					}
				}
			}
		} catch (SQLException e)
		{
			throw new MraldException(e);
		} catch (IOException e)
		{
			throw new MraldException(e);
		}
	}
}

