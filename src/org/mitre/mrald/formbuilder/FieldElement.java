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

package org.mitre.mrald.formbuilder;

import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

import org.mitre.mrald.parser.ParserElement;
import org.mitre.mrald.util.DBMetaData;
import org.mitre.mrald.util.FBUtils;
import org.mitre.mrald.util.FormTags;
import org.mitre.mrald.util.MiscUtils;
import org.mitre.mrald.util.TableMetaData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *  Description of the Class
 *
 *@author     jchoyt
 *@created    January 30, 2003
 */
public class FieldElement extends ParserElement implements FormBuilderElement
{

	Document document;


	/**
	 *  Constructor for the FieldElement object
	 */
	public FieldElement() { }


	/**
	 *  Gets the elementType of the ParserElement-derived object
	 *
	 *@return    The elementType value
	 */
	public String getElementType()
	{
		return "Formbuilder Field Element";
	}


	/**
	 *  Produces the HTML for inclusion on the second step of form building. The
	 *  HTML returned should be self-supporting - i.e., only the guts of a
	 *  &lt;div&gt; or a &lt;td&gt; tag. It should not be part of a larger table
	 *  structure. It is used in building the second page of the form buliding
	 *  process.
	 *
	 *@param  num  Which iteration this is. This should be used to create unique
	 *      tag names
	 *@param  md   Description of the Parameter
	 *@return      The HTML for inclusion in the second form building page.
	 */
	public String getFBHtml(DBMetaData md, int num, int thread)
	{
		StringBuffer ret = new StringBuffer();
		String SECTION_CLOSE = "\n</table></td></tr></table>";
		String SECTION_OPENER = "\n<table summary=\"\" width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><tr><td class=\"bord\"><table summary=\"\" width=\"100%\" border=\"0\" cellspacing=\"1\" cellpadding=\"2\">";
		String TABLE_HEADER = "\n<!-- new table!! -->\n<tr align=\"left\"><font size=\"-1\"><th>Ignore</th><th>Table</th><th>Column Name</th><th>Column Label</th><th>Output</th><th>Default Selection</th><th>Filter</th><th>Stats</th><th>Group By</th><th>Order</th><th>Sort</th><th>Format</th><th>Comments</th></font></tr><tr>";
		String CHECK_ALL_ROW = "\n<th><input type=checkbox onclick=\"if(this.checked==false){ClearAll('Ignore:true~Table:<:tableName:>');} else{CheckAll('Ignore:true~Table:<:tableName:>');}\"></th><th>&nbsp;</th><th>&nbsp;</th><th>&nbsp;</th><th><input type=checkbox onclick=\"if(this.checked==false){ClearAll('Output:true~Table:<:tableName:>');} else{CheckAll('Output:true~Table:<:tableName:>');}\" checked></th><th><input type=checkbox onclick=\"if(this.checked==false){ClearAll('Default:true~Table:<:tableName:>');} else{CheckAll('Default:true~Table:<:tableName:>');}\" checked></th><th><input type=checkbox onclick=\"if(this.checked==false){ClearAll('Filter:true~Table:<:tableName:>');} else{CheckAll('Filter:true~Table:<:tableName:>');}\" checked></th><th><input type=checkbox onclick=\"if(this.checked==false){ClearAll('Stat:true~Table:<:tableName:>');} else{CheckAll('Stat:true~Table:<:tableName:>');}\" checked></th><th><input type=checkbox onclick=\"if(this.checked==false){ClearAll('Group:true~Table:<:tableName:>');} else{CheckAll('Group:true~Table:<:tableName:>');}\" checked></th><th>&nbsp;</th><th><input type=checkbox onclick=\"if(this.checked==false){ClearAll('Sort:true~Table:<:tableName:>');} else{CheckAll('Sort:true~Table:<:tableName:>');}\" checked></th><th>&nbsp;</th><th>&nbsp;</th></tr>";
		String FIELD_ROW = "\n<tr><td align=\"center\"><input name=\"Field<:orderNo:>\" value=\"Ignore:true~Table:<:tableName:>\" type=\"checkbox\"></td><td><:tableName:></td><td><input type=\"hidden\" name=\"Field<:orderNo:>\" value=\"DBName:<:db_name:>~Schema:<:db_schema:>~Table:<:tableName:>~Field:<:column_name:>~Type:<:type:>\"><:column_name:></td><td><input name=\"Field<:orderNo:>\" type=\"text\" size=\"15\" value=\"<:nice_column_name:>\"></td><td align=\"center\"><input name=\"Field<:orderNo:>\" value=\"Output:true~Table:<:tableName:>\" type=\"checkbox\" checked></td><td align=\"center\"><input name=\"Field<:orderNo:>\" value=\"Default:true~Table:<:tableName:>\" type=\"checkbox\" checked></td><td align=\"center\"><input name=\"Field<:orderNo:>\" value=\"Filter:true~Table:<:tableName:>\" type=\"checkbox\" checked></td><td align=\"center\"><input name=\"Field<:orderNo:>\" value=\"Stat:true~Table:<:tableName:>\" type=\"checkbox\" <:stat checked:>></td><td align=\"center\"><input name=\"Field<:orderNo:>\" value=\"Group:true~Table:<:tableName:>\" type=\"checkbox\" checked></td><td align=\"center\"><input name=\"Field<:orderNo:>\" type=\"text\" size=\"3\" value=\"<:orderNo:>\"></td><td align=\"center\"><input name=\"Field<:orderNo:>\" value=\"Sort:true~Table:<:tableName:>\" type=\"checkbox\" checked></td><td><:formatChoices:></td><td align=\"left\"><:comments:></td></tr>\n<input type=\"hidden\" name=\"Field<:orderNo:>\" value=\"SqlThread:<:threadNo:>\"/>\n";
		String NUMBER_FORMAT_REGEX = "\n<select name=\"Field<:orderNo:>\"><option value=\"\" SELECTED>As Stored</option><option value=\"Format:#\">#</option><option value=\"Format:#.#\">#.#</option><option value=\"Format:#.##\">#.##</option><option value=\"Format:#.0\">#.0</option><option value=\"Format:#,###\">#,###</option><option value=\"Format:#,###.0\">#,###.0</option><option value=\"Format:#,##0.0\">#,##0.0</option><option value=\"Format:#.##%\">#.##%</option><option value=\"Format:$#,##0.00\">$#,##0.00</option><option value=\"Format:$#,##0.00;($#,##0.00)\">$#,##0.00;($#,##0.00)</option><option value=\"Format:#.###E0\">#.###E0</option><option value=\"Format:#.#####E0\">#.#####E0</option></select>";
		String DATE_FORMAT_REGEX = "\n<select name=\"Field<:orderNo:>\"><option value=\"Format:yyyy\">yyyy</option><option value=\"Format:MM/yy\">MM/yy</option><option value=\"Format:MM/dd\">MM/dd</option><option value=\"Format:MM/yyyy\">MM/yyyy</option><option value=\"Format:MM/dd/yyyy\">MM/dd/yyyy</option><option value=\"Format:yyyy MMMM d\">yyyy MMMM d</option><option value=\"Format:d MMMM yyyy\">d MMMM yyyy</option><option value=\"Format:MM/dd/yyyy hh:mm a\">MM/dd/yyyy hh:mm a</option><option value=\"Format:MM/dd/yyyy HH:mm\">MM/dd/yyyy HH:mm</option><option value=\"Format:MM/dd/yyyy hh:mm:ss a\">MM/dd/yyyy hh:mm:ss a</option><option value=\"Format:dd/MMM/yyyy:hh:mm:ss\">DD/MON/YYYY:HH:MM:SS</option><option value=\"Format:MM/dd/yyyy HH:mm:ss\" SELECTED>MM/dd/yyyy HH:mm:ss</option><option value=\"Format:yyyy MMMM d, HH:mm:ss\">yyyy MMMM d, HH:mm:ss</option><option value=\"Format:d MMMM yyyy, HH:mm:ss\">d MMMM yyyy, HH:mm:ss</option></select>";
		String OTHER_FORMAT_REGEX = "\n&nbsp;";

		/*
		 *  append the table heading
		 */
		Collection tableMetaData = md.getAllTableMetaData();
		TableMetaData tableInfo;
		Iterator iter = tableMetaData.iterator();
		int orderNo = 1;
		ret.append(SECTION_OPENER);
		/*
		 *  for each table . . .
		 */

		Properties props = md.getDbProps();
		String schema = props.getProperty("SCHEMA");
	    String sidName = props.getProperty("DBNAME");

		while (iter.hasNext())
		{
			tableInfo = (TableMetaData) iter.next();
			Collection fieldNames = tableInfo.getColumnNames();
			ret.append(TABLE_HEADER);
			ret.append(CHECK_ALL_ROW.replaceAll("<:tableName:>", tableInfo.getName()));
			Iterator iter2 = fieldNames.iterator();
			/*
			 *  and for each field, output a row of options
			 */
//			int i = 0;
			String field;
			while (iter2.hasNext())
			{
				field = (String) iter2.next();
				/*
				 *  append the columns - one to a row
				 */
				String newRow = FIELD_ROW.replaceAll("<:column_name:>", field);
				newRow = newRow.replaceAll("<:db_name:>", sidName);
				newRow = newRow.replaceAll("<:db_schema:>", schema);
				String niceName = FBUtils.getColumnName(tableInfo.getName() + "." + field);
				if (niceName == null)
				{
					newRow = newRow.replaceAll("<:nice_column_name:>", field);
				} else
				{
					newRow = MiscUtils.replace(newRow, "<:nice_column_name:>", niceName);
				}
				/*
				 *  make substitutions depending on the type of field it is - date, numeric, or other)
				 */
				Integer colType = tableInfo.getFieldType(field);
				if (FBUtils.isDateType(colType))
				{
					newRow = MiscUtils.replace(newRow, "<:formatChoices:>", DATE_FORMAT_REGEX);
					newRow = MiscUtils.replace(newRow, "<:stat checked:>", "");
					newRow = newRow.replaceAll("<:type:>", "Date");
				} else if (FBUtils.isNumberType(colType))
				{
					newRow = MiscUtils.replace(newRow, "<:formatChoices:>", NUMBER_FORMAT_REGEX);
					newRow = MiscUtils.replace(newRow, "<:stat checked:>", "checked");
					newRow = newRow.replaceAll("<:type:>", "Numeric");
				} else
				{
					newRow = MiscUtils.replace(newRow, "<:formatChoices:>", OTHER_FORMAT_REGEX);
					newRow = MiscUtils.replace(newRow, "<:stat checked:>", "");
					newRow = newRow.replaceAll("<:type:>", "String");
				}
				/*
				 *  insert the comments
				 */
				String comments = tableInfo.getFieldComments(field);
				if (comments == null)
				{
					comments = "no comments for this field";
				}
				newRow = newRow.replaceAll("<:comments:>", comments);
				newRow = newRow.replaceAll("<:tableName:>", tableInfo.getName());
				newRow = newRow.replaceAll("<:threadNo:>", "" + thread);
				newRow = MiscUtils.replace(newRow, "<:orderNo:>", thread + "" +  orderNo++);

				ret.append(newRow);

			}
		}
		ret.append(SECTION_CLOSE);
		return ret.toString();
	}

	/**
	 *  This method should build a Node object (or object that inherits from Node)
	 *  for inclusion in the xml representation of the MRALD form. Unless otherwise
	 *  noted, it is assumed that this will be added to the root node. It is used
	 *  in buliding the XML file. Stuture should be:<code>
	 *<field output='yes' checked='yes' filter='yes' stats='yes' groupby='yes' sort='yes'>
	 *<column>FORCASTISSUE</column>
	 *  <table>
	 *    CCFPVIEW
	 *  </table>
	 *  <label>Forcast Issue</label> <type>DATE</type> <format>MM/dd/yyyy hh:mm:ss
	 *  a</format> <order>1</order> </field> </code>
	 *
	 *@param  document  The Document object the return Node will be added to
	 *@return           A Node object for inclusion in the Document being built.
	 */
	public Node getFBNode(Document document)
	{
		this.document = document;
		/*
		 *  chance to opt out
		 */
		if (nameValues.getValue("Ignore")[0].equals("true"))
		{
			return null;
		}
		Element ret = document.createElement("field");
		/*
		 *  attributes
		 */
		String output = nameValues.getValue("Output")[0];
		String checked = nameValues.getValue("Default")[0];
		String filter = nameValues.getValue(FormTags.FILTER_TAG)[0];
		String stats = nameValues.getValue(FormTags.STAT_TAG)[0];
		String groupby = nameValues.getValue(FormTags.GROUP_STR)[0];
		String sort = nameValues.getValue(FormTags.SORT_TAG)[0];
		/**Added for MultiDb **/
//		String dataSourceName = nameValues.getValue(FormTags.DATASOURCE_TAG)[0];
		String dbName = nameValues.getValue(FormTags.DB_NAME_TAG)[0];
		String schemaName = nameValues.getValue(FormTags.SCHEMA_TAG)[0];
		String thread = nameValues.getValue(FormTags.SQL_THREAD_NUM_TAG)[0];
		addAttribute(ret, "output", output);
		addAttribute(ret, "checked", checked);
		addAttribute(ret, "filter", filter);
		addAttribute(ret, "stats", stats);
		addAttribute(ret, "groupby", groupby);
		addAttribute(ret, "sort", sort);
		/*
		 *  child nodes
		 */
		String type = nameValues.getValue("Type")[0];
		String format = nameValues.getValue(FormTags.FORMAT_TAG)[0];
		String table = nameValues.getValue(FormTags.TABLE_TAG)[0];
		String field = nameValues.getValue(FormTags.FIELD_TAG)[0];
		String[] values = nameValues.getValue(FormTags.VALUE_TAG);
		String label;
		String order;
        try{
            if (isNumber(values[0]))
            {
                order = values[0];
                label = values[1];
            } else
            {
                label = values[0];
                order = values[1];
            }
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            throw new RuntimeException( "You need to specify a label and an order for the " +
                table + "." + field +
                " column.  The order can be any integer or float value.");
        }
		FBUtils.addTextNode(document, ret, "column", field);
		FBUtils.addTextNode(document, ret, "table", table);
		FBUtils.addTextNode(document, ret, "label", label);
		FBUtils.addTextNode(document, ret, "type", type);
		if ( ! format.equals( "" ) )
		{
			FBUtils.addTextNode(document, ret, "format", format);
		}
		/**MultiDB Start:
		 * Add for case of multiple queries **/

		if ( ! thread.equals( "" ) )
		{
			FBUtils.addTextNode(document, ret, "sqlThread", thread);
		}

		//Need both parameters
		if ( (! dbName.equals( "" ) ) && (! schemaName.equals( "" ) ) )
		{
			FBUtils.addTextNode(document, ret, "dbName", dbName);
			FBUtils.addTextNode(document, ret, "schemaName", schemaName);
		}
		/**MultiDB End **/

		FBUtils.addTextNode(document, ret, "order", order);
		return ret;
	}


	/**
	 *  Gets the number attribute of the FieldElement object
	 *
	 *@param  testee  Description of the Parameter
	 *@return         The number value
	 */
	public boolean isNumber(String testee)
	{
		return testee.matches("\\d*\\.*\\d*");
	}


	/**
	 *  Adds a feature to the Attribute attribute of the FieldElement object
	 *
	 *@param  ret    The feature to be added to the Attribute attribute
	 *@param  name   The feature to be added to the Attribute attribute
	 *@param  value  The feature to be added to the Attribute attribute
	 */
	public void addAttribute(Element ret, String name, String value)
	{
		if (value.equals("true"))
		{
			ret.setAttribute(name, "yes");
		} else
		{
			ret.setAttribute(name, "no");
		}
	}

}

