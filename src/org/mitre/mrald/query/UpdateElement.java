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
package org.mitre.mrald.query;

import java.util.ArrayList;

import org.mitre.mrald.util.FormTags;
import org.mitre.mrald.util.MiscUtils;
import org.mitre.mrald.util.MraldException;
/**
 *  The SqlElement class is the abstract class for all of the query elements
 *  templates needed to populate a complete query string. This class contains
 *  the common functionality for building query strings in general
 *
 *@author     Gail Hamilton
 *@created    February 17, 2001
 *@version    1.0
 *@see        java.lang.Object
 */
public class UpdateElement extends SqlElements
{

	//private Integer order = null;

	/**
	 *  Constructor for the QueryElements object
	 *
	 *@since
	 */
	public UpdateElement()
	{
		super();
		elementType = FormTags.UPDATE_TAG;
	}


	/**
	 *  Builds a value String for a name value pair from the given parameters. Ex:
	 *  <code>Table:ASQPARRGMT~Field:OAGATGDATETOD~Order:28</code>
	 *
	 *@param  table    Table name
	 *@param  synonym  Synonym name, if requried. If it is not required, pass a
	 *      <code>null</code>.
	 *@param  field    Field name in table
	 *@param  order    Order string. This must be unique in the form.
	 *@param  as       Synonym for the field name. The column returned will have
	 *      this name.
	 *@return          A string suitable for a HTML tag in a form.
	 */
	public static String buildValue(String table, String synonym, String field, String as, String order)
	{
		StringBuffer ret = new StringBuffer();
		ret.append(FormTags.TABLE_TAG);
		ret.append(":");
		ret.append(table);
		if (synonym != null)
		{
			ret.append("~");
			ret.append(FormTags.SYN_TAG);
			ret.append(":");
			ret.append(synonym);
		}
		ret.append("~");
		ret.append(FormTags.FIELD_TAG);
		ret.append(":");
		ret.append(field);
		if (as != null)
		{
			ret.append("~");
			ret.append(FormTags.AS_TAG);
			ret.append(":");
			ret.append(as);
		}
		ret.append("~");
		ret.append(FormTags.ORDER_TAG);
		ret.append(":");
		ret.append(order);
		return ret.toString();
	}


	/**
	 *  Description of the Method
	 *
	 *@param  currentGroupByList  List of Group By parameters
	 *@return                     Description of the Returned Value
	 *@exception  MraldException  Description of Exception
	 *@since
	 */
	public ArrayList<String> buildGroupBy(ArrayList<String> currentGroupByList)
		 throws MraldException
	{
		return currentGroupByList;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  currentSelectList  Description of Parameter
	 *@return                    Description of the Returned Value
	 *@since
	 */
	public ArrayList<String> buildSelect(ArrayList<String> currentSelectList)
	{
//		String table = nameValues.getValue(FormTags.TABLE_TAG)[0];
		String newValue = nameValues.getValue(FormTags.FIELD_TAG)[0];
		String value = nameValues.getValue(FormTags.VALUE_TAG)[0];
		String typeStr = nameValues.getValue(FormTags.TYPE_TAG)[0];

		if (typeStr != null)
		{
			//int type = new Integer(typeStr).intValue();

			//Make sure that is this string is empty and is non string that a valid value is entered. Or name/value pair is ommitted from update statement
			//if (TypeUtils.isBooleanType(type) || TypeUtils.isDateType(type) || TypeUtils.isNumberType(type))
			if (typeStr.equals("Numeric") || typeStr.equals("Date"))
			{
				if ((value == null) || (value.equals("null")) || (value.equals("")))
				{
					newValue = newValue + " = null ";
					if (!currentSelectList.contains(newValue))
					{
						currentSelectList.add(newValue);
					}
					return currentSelectList;
				}
				else
				{
					if (typeStr.equals("Date"))
					{
						value =   " to_date('" + value + "', 'MM/DD/YYYY')";
						newValue = newValue + " = " + value;
						if (!currentSelectList.contains(newValue))
						{
							currentSelectList.add(newValue);
						}
						return currentSelectList;
					}
				}
			}
			else if (typeStr.equals("Binary")) //Ignore
			{
				return currentSelectList;
			}
		}

		newValue = newValue + " = '" + MiscUtils.checkApostrophe(value) + "'";

		if (!currentSelectList.contains(newValue))
		{
			currentSelectList.add(newValue);
		}
		return currentSelectList;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  currentFromList  Description of Parameter
	 *@return                  Description of the Returned Value
	 *@since
	 */
	public ArrayList<String> buildFrom(ArrayList<String> currentFromList)
	{
		String newValue = nameValues.getValue(FormTags.TABLE_TAG)[0];

		if (newValue.equals(""))
		{
			return currentFromList;
		}

		if (!currentFromList.contains(newValue))
		{
			currentFromList.add(newValue);
		}
		return currentFromList;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  currentWhereList    Description of Parameter
	 *@return                     Description of the Returned Value
	 *@exception  MraldException  Description of Exception
	 *@since
	 */
	public ArrayList<String> buildWhere(ArrayList<String> currentWhereList)
		 throws MraldException
	{
		return currentWhereList;
	}


	/**
	 *  Description of the Method
	 *
	 *@param  currentOrderBy      Description of the Parameter
	 *@return                     Description of the Returned Value
	 *@exception  MraldException  Description of Exception
	 *@since
	 */
	/*
	 *  public ArrayList buildWhereOr(ArrayList currentWhereOrList)
	 *  throws MraldException
	 *  {
	 *  return currentWhereOrList;
	 *  }
	 */
	/**
	 *  Description of the Method
	 *
	 *@param  currentOrderBy      Description of Parameter
	 *@return                     Description of the Returned Value
	 *@exception  MraldException  Description of Exception
	 *@since
	 */
	public ArrayList<String> buildOrderBy(ArrayList<String> currentOrderBy)
		 throws MraldException
	{
		return currentOrderBy;
	}
}

