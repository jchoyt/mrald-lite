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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import org.mitre.mrald.control.MsgObject;
import org.mitre.mrald.util.*;

public class PivotFilter extends AbstractPivotFilter {

    public PivotFilter() throws MraldException { super(); }

    public PivotFilter( MsgObject msg ) throws MraldException { super( msg ); }

	@Override
	public ArrayList<String> buildFrom(ArrayList<String> currentFromList) throws MraldException {
		if (field.equals("") || operator.equals("")) { /* Do nothing */ }
		else
		{
			String attrTable = nameValues.getValue(FormTags.ATTRIBUTE_TABLE_TAG)[0];
			String valueTable = nameValues.getValue(FormTags.VALUE_TABLE_TAG)[0];
			if (!currentFromList.contains(attrTable)) currentFromList.add(attrTable);
			if (!currentFromList.contains(valueTable)) currentFromList.add(valueTable);
		}
		return currentFromList;
	}

	@Override
	public ArrayList<String> buildWhere(ArrayList<String> currentWhereList) throws MraldException {
//		MraldOutFile.appendToFile("~~" + field + operator + nameValues.getValue(FormTags.VALUE_TAG)[0]);
		if (field.equals("") || operator.equals("")) { /* Do nothing */ }
		else
		{
			StringBuffer clause = new StringBuffer();
			buildOuter(clause);
			buildInner(clause);
			currentWhereList.add(clause.toString());
//			MraldOutFile.appendToFile(clause.toString());
		}
		return currentWhereList;
	}

	private void buildInner(StringBuffer clause) {
		String entityTable = nameValues.getValue(FormTags.ENTITY_TABLE_TAG)[0];
		String entityField = nameValues.getValue(FormTags.ENTITY_FIELD_TAG)[0];
		clause.append("(SELECT ");
		clause.append(entityField);
		clause.append(" FROM ");
		clause.append(entityTable);
		clause.append(" WHERE ");
		buildInnerWhere(clause);
		clause.append(")");
	}

	private void buildInnerWhere(StringBuffer clause) {
		String attributeField = nameValues.getValue(FormTags.ATTRIBUTE_FIELD_TAG)[0];
		String valueField = nameValues.getValue(FormTags.VALUE_FIELD_TAG)[0];
		String value = nameValues.getValue(FormTags.VALUE_TAG)[0];
		clause.append(attributeField);
		clause.append("=");
		buildValue(clause, FormTags.ATTRIBUTE_TAG, field, false);
		clause.append(" AND ");
		clause.append(valueField);
		clause.append(" ");
		clause.append(operator);
		if (operator.indexOf("NULL") < 0) {
			clause.append(" ");
			buildValue(clause, FormTags.VALUE_TAG, value, operator.endsWith("IN"));
		}
	}

	public static String populateOptions(String pivot, String datasource) {
		String[] pieces = parse(pivot);
		StringBuffer result = new StringBuffer();
		MraldConnection db = new MraldConnection(datasource);
		Statement stmt = db.createStatement();
		try {
			// WOW, this is a major injection hole!
			String sql = "SELECT DISTINCT " + pieces[ATTRIBUTE_FIELD] + " FROM " + pieces[ATTRIBUTE_TABLE];
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				result.append("<option/>");
				do {
					String attr = rs.getString(1);
					// Selected value
					result.append("<option value=\"");
					result.append(FormTags.FIELD_TAG);
					result.append(FormTags.NAMEVALUE_TOKEN_STR);
					result.append(attr);
					buildEAVinfo(result, pieces);
					result.append("\">").append(attr).append("</option>");
				} while (rs.next());
			}
		} catch (SQLException e) {
			// TODO: Log this someplace better!
			MraldOutFile.logToFile( e );
		}
		return result.toString();
	}

}
