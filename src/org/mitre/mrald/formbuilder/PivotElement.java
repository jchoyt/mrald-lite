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

import org.mitre.mrald.parser.ParserElement;
import org.mitre.mrald.util.DBMetaData;
import org.mitre.mrald.util.FBUtils;
import org.mitre.mrald.util.FormTags;
import org.mitre.mrald.util.TableMetaData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * <p>This element creates a checkbox that, when checked, indicates that the form should
 * pivot its results.  Moreover, filters should be generated based on pivoted values.</p>
 * @author pmork
 */
public class PivotElement extends ParserElement implements FormBuilderElement
{
	@Override
	public String getElementType()
    {
        return "Formbuilder Pivot Element";
    }

    public String getFBHtml( DBMetaData md, int num, int thread )
    {
        StringBuffer ret = new StringBuffer();

        ret.append(FBUtils.TABLE_START);
        ret.append(FBUtils.ROW_START);

        ret.append("<td width=\"5%\" align=\"center\"><input name=\"FBPivot\" type=\"checkbox\" value=\"PivotResults\"></td>\n");
        ret.append("<td width=\"20%\">Pivot the output</td>\n");
        ret.append("<td width=\"5%\">Entity:</td>\n");
        ret.append("<td width=\"20%\"><select name=\"FBPivot\" onChange=\"HandlePivot(this);\">");
        appendOptions(md, ret, FormTags.ENTITY_TAG);
   		ret.append("</select></td>\n");
        ret.append("<td width=\"5%\">Attribute:</td>\n");
        ret.append("<td width=\"20%\"><select name=\"FBPivot\" onChange=\"HandlePivot(this);\">");
        appendOptions(md, ret, FormTags.ATTRIBUTE_TAG);
   		ret.append("</select></td>\n");
        ret.append("<td width=\"5%\">Value:</td>\n");
        ret.append("<td width=\"20%\"><select name=\"FBPivot\" onChange=\"HandlePivot(this);\">");
        appendOptions(md, ret, FormTags.VALUE_TAG);
   		ret.append("</select></td>\n");

        ret.append(FBUtils.ROW_END);
        ret.append(FBUtils.TABLE_END);

        return ret.toString();
    }

    private void appendOptions(DBMetaData md, StringBuffer ret, String which) {
    	ret.append("<option></option>");
		Collection tableMetaData = md.getAllTableMetaData();
		TableMetaData tableInfo;
		Iterator tableItr = tableMetaData.iterator();
		while (tableItr.hasNext()) {
			tableInfo = (TableMetaData) tableItr.next();
			String tableName = tableInfo.getName();
			Collection fieldNames = tableInfo.getColumnNames();
			Iterator fieldItr = fieldNames.iterator();
			while (fieldItr.hasNext()) {
				String fieldName = (String) fieldItr.next();
				Integer colType = tableInfo.getFieldType(fieldName);
				String typeName = FBUtils.isDateType(colType)   ? "Date" :
								  FBUtils.isNumberType(colType) ? "Numeric" :
									  						      "String";
				ret.append("<option value=\"");
				// Table info
				ret.append(which);
				ret.append(FormTags.TABLE_TAG);
				ret.append(FormTags.NAMEVALUE_TOKEN_STR);
				ret.append(tableName);
				// Field info
				ret.append(FormTags.TOKENIZER_STR);
				ret.append(which);
				ret.append(FormTags.FIELD_TAG);
				ret.append(FormTags.NAMEVALUE_TOKEN_STR);
				ret.append(fieldName);
				// Type info
				ret.append(FormTags.TOKENIZER_STR);
				ret.append(which);
				ret.append(FormTags.TYPE_TAG);
				ret.append(FormTags.NAMEVALUE_TOKEN_STR);
				ret.append(typeName);
				ret.append("\">");
				// Human-readable text
				ret.append(tableName);
				ret.append(".");
				ret.append(fieldName);
				ret.append("</option>");
			}
		}
	}

	public Node getFBNode( Document document )
    {
    	// Get the value of the checkbox.
        String checkValue = nameValues.getValue("Value")[0];
        // If checked, create an empty node to serve as a flag in the future.
        if (checkValue.equals("PivotResults") && !missingAnyTag() && allFieldsFromOneTable()) {
        	Element result = document.createElement("pivot");
//        	Element entity = document.createElement(FormTags.ENTITY_TAG);
//        	entity.setAttribute(FormTags.TABLE_TAG, nameValues.getValue(FormTags.ENTITY_TABLE_TAG)[0]);
//        	entity.setAttribute(FormTags.FIELD_TAG, nameValues.getValue(FormTags.ENTITY_FIELD_TAG)[0]);
//        	entity.setAttribute(FormTags.TYPE_TAG, nameValues.getValue(FormTags.ENTITY_TYPE_TAG)[0]);
//        	result.appendChild(entity);
//        	Element attribute = document.createElement(FormTags.ATTRIBUTE_TAG);
//        	attribute.setAttribute(FormTags.TABLE_TAG, nameValues.getValue(FormTags.ATTRIBUTE_TABLE_TAG)[0]);
//        	attribute.setAttribute(FormTags.FIELD_TAG, nameValues.getValue(FormTags.ATTRIBUTE_FIELD_TAG)[0]);
//        	attribute.setAttribute(FormTags.TYPE_TAG, nameValues.getValue(FormTags.ATTRIBUTE_TYPE_TAG)[0]);
//        	result.appendChild(attribute);
//        	Element value = document.createElement(FormTags.VALUE_TAG);
//        	value.setAttribute(FormTags.TABLE_TAG, nameValues.getValue(FormTags.VALUE_TABLE_TAG)[0]);
//        	value.setAttribute(FormTags.FIELD_TAG, nameValues.getValue(FormTags.VALUE_FIELD_TAG)[0]);
//        	value.setAttribute(FormTags.TYPE_TAG, nameValues.getValue(FormTags.VALUE_TYPE_TAG)[0]);
//        	result.appendChild(value);
        	result.setTextContent(getText());
        	return result;
        } else {
        	return null;
        }
    }

	private boolean missingAnyTag() {
		return (nameValues.getValue(FormTags.ENTITY_TABLE_TAG)[0].equals("") ||
				nameValues.getValue(FormTags.ENTITY_FIELD_TAG)[0].equals("") ||
				nameValues.getValue(FormTags.ENTITY_TYPE_TAG)[0].equals("") ||

				nameValues.getValue(FormTags.ATTRIBUTE_TABLE_TAG)[0].equals("") ||
				nameValues.getValue(FormTags.ATTRIBUTE_FIELD_TAG)[0].equals("") ||
				nameValues.getValue(FormTags.ATTRIBUTE_TYPE_TAG)[0].equals("") ||

				nameValues.getValue(FormTags.VALUE_TABLE_TAG)[0].equals("") ||
				nameValues.getValue(FormTags.VALUE_FIELD_TAG)[0].equals("") ||
				nameValues.getValue(FormTags.VALUE_TYPE_TAG)[0].equals(""));
	}

	// This is here because later in the pipeline we make the assumption that all of the
	// fields can be retrieved from a single table.
	private boolean allFieldsFromOneTable() {
		String entity = nameValues.getValue(FormTags.ENTITY_TABLE_TAG)[0];
		String attribute = nameValues.getValue(FormTags.ATTRIBUTE_TABLE_TAG)[0];
		String value = nameValues.getValue(FormTags.VALUE_TABLE_TAG)[0];
		if (entity == null) return false;
		return entity.equals(attribute) && entity.equals(value);
	}

	private String getText()
	{
		StringBuffer result = new StringBuffer();
		// The text of the pivot element enumerates the entity, attribute and value fields delimited
		// using a tilde (~).  These nine values are copied to a hidden field in the simple and advanced
		// forms so that they are available to the output manager when the custom form is used.
		result.append(nameValues.getValue(FormTags.ENTITY_TABLE_TAG)[0]).append(FormTags.TOKENIZER_STR);
		result.append(nameValues.getValue(FormTags.ENTITY_FIELD_TAG)[0]).append(FormTags.TOKENIZER_STR);
		result.append(nameValues.getValue(FormTags.ENTITY_TYPE_TAG)[0]).append(FormTags.TOKENIZER_STR);

		result.append(nameValues.getValue(FormTags.ATTRIBUTE_TABLE_TAG)[0]).append(FormTags.TOKENIZER_STR);
		result.append(nameValues.getValue(FormTags.ATTRIBUTE_FIELD_TAG)[0]).append(FormTags.TOKENIZER_STR);
		result.append(nameValues.getValue(FormTags.ATTRIBUTE_TYPE_TAG)[0]).append(FormTags.TOKENIZER_STR);

		result.append(nameValues.getValue(FormTags.VALUE_TABLE_TAG)[0]).append(FormTags.TOKENIZER_STR);
		result.append(nameValues.getValue(FormTags.VALUE_FIELD_TAG)[0]).append(FormTags.TOKENIZER_STR);
		result.append(nameValues.getValue(FormTags.VALUE_TYPE_TAG)[0]);
		return result.toString();
	}

}