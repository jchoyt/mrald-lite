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
package org.mitre.mrald.ddlelements;

import org.mitre.mrald.util.MraldOutFile;
import java.util.Comparator;
/**
 *  Comparator class for DdlElements. Sorts the DdlElements by Table name, then
 *  by Order. THIS CLASS MAKES THE COMPARISON FOR THE MULTI INSERT STATEMENTS
 *  FROM ONE FORM.
 *
 *@author     tcornett
 *@created    June 30, 2004
 */
public class MultiInsertElementComparator implements Comparator<DdlElement>
{


	/**
	 *  Sorts the DdlElements by sqlThread number, then by Order.
	 *
	 *@param  _de1  First DdlElement to be compared
	 *@param  _de2  Second DdlElement to be compared
	 *@return       a negative integer, zero, or a positive integer as the first
	 *      argument is less than, equal to, or greater than the second.
	 */
	public int compare(DdlElement de1, DdlElement de2)
	{
		int threadNo1 = getThreadNo(de1);
		int threadNo2 = getThreadNo(de2);
		int orderDiff = Integer.parseInt(de1.getOrder()) - Integer.parseInt(de2.getOrder());
		int sqlNoDiff = threadNo1 - threadNo2;
		return 1000 * sqlNoDiff + orderDiff;
	}


	/**
	 *  Gets the threadNo attribute of the MultiInsertElementComparator object
	 *
	 *@param  de  Description of the Parameter
	 *@return     The threadNo value
	 */
	protected int getThreadNo(DdlElement de)
	{
		try
		{
			return Integer.parseInt(de.getSqlThread());
		} catch (NumberFormatException e)
		{

			try
			{
				/*
				 *  most likely, we have a sqlThread number that is for multiple
				 *  queries - just use the first one so it gets into the ArrayList
				 *  in MultiQueryBuilder in time
				 */
				MraldOutFile.appendToFile( "Number Format Exception: " + de.getSqlThread() + " With object : Table Name " + de.getTable() + " : " + de.toString() + e.getMessage());
				return Integer.parseInt(de.getSqlThread().substring(0, de.getSqlThread().indexOf(',')));
			} catch (NumberFormatException ne)
			{
				/*
				 *  most likely, we have a sqlThread number that is for multiple
				 *  queries - just use the first one so it gets into the ArrayList
				 *  in MultiQueryBuilder in time
				 */
				MraldOutFile.appendToFile( "Number Format Exception: " + de.getSqlThread() + ne.getMessage());
				throw ne;
			}
		}
	}
}

