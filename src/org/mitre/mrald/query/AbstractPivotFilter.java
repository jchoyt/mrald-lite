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

import org.mitre.mrald.control.MsgObject;
import org.mitre.mrald.util.FormTags;
import org.mitre.mrald.util.MraldException;

public abstract class AbstractPivotFilter extends SqlElements {

	public static final int ENTITY_TABLE = 0;
	public static final int ENTITY_FIELD = 1;
	public static final int ENTITY_TYPE = 2;
	public static final int ATTRIBUTE_TABLE = 3;
	public static final int ATTRIBUTE_FIELD = 4;
	public static final int ATTRIBUTE_TYPE = 5;
	public static final int VALUE_TABLE = 6;
	public static final int VALUE_FIELD = 7;
	public static final int VALUE_TYPE = 8;

    public AbstractPivotFilter() throws MraldException
    {
        super();
        elementType = FormTags.FILTER_TAG;
    }

    public AbstractPivotFilter( MsgObject msg ) throws MraldException
    {
        super( msg );
        elementType = FormTags.FILTER_TAG;
    }

	@Override
	public ArrayList<String> buildOrderBy(ArrayList<String> currentOrderBy) throws MraldException {
		/* Do nothing */
		return currentOrderBy;
	}

	@Override
	public ArrayList<String> buildSelect(ArrayList<String> currentSelectList) throws MraldException {
		/* Do nothing */
		return currentSelectList;
	}

	protected void buildOuter(StringBuffer clause) {
		String entityTable = nameValues.getValue(FormTags.ENTITY_TABLE_TAG)[0];
		String entityField = nameValues.getValue(FormTags.ENTITY_FIELD_TAG)[0];
		clause.append(entityTable).append(".").append(entityField).append(" IN ");
	}

	protected void buildValue(StringBuffer clause, String which, String value, boolean useParens) {
		String type = nameValues.getValue(which + FormTags.TYPE_TAG)[0];
		String delimiter = type.equals("Numeric") ? "" :
							type.equals("Date") ? "#" : "'";
		if (useParens) clause.append("(");
		clause.append(delimiter);
		clause.append(value);
		clause.append(delimiter);
		if (useParens) clause.append(")");
	}

	public static void buildEAVinfo(StringBuffer result, String[] pieces) {
		// Entity info
		result.append(FormTags.TOKENIZER_STR);
		result.append(FormTags.ENTITY_TABLE_TAG);
		result.append(FormTags.NAMEVALUE_TOKEN_STR);
		result.append(pieces[ENTITY_TABLE]);
		result.append(FormTags.TOKENIZER_STR);
		result.append(FormTags.ENTITY_FIELD_TAG);
		result.append(FormTags.NAMEVALUE_TOKEN_STR);
		result.append(pieces[ENTITY_FIELD]);
		result.append(FormTags.TOKENIZER_STR);
		result.append(FormTags.ENTITY_TYPE_TAG);
		result.append(FormTags.NAMEVALUE_TOKEN_STR);
		result.append(pieces[ENTITY_TYPE]);
		// Attribute info
		result.append(FormTags.TOKENIZER_STR);
		result.append(FormTags.ATTRIBUTE_TABLE_TAG);
		result.append(FormTags.NAMEVALUE_TOKEN_STR);
		result.append(pieces[ATTRIBUTE_TABLE]);
		result.append(FormTags.TOKENIZER_STR);
		result.append(FormTags.ATTRIBUTE_FIELD_TAG);
		result.append(FormTags.NAMEVALUE_TOKEN_STR);
		result.append(pieces[ATTRIBUTE_FIELD]);
		result.append(FormTags.TOKENIZER_STR);
		result.append(FormTags.ATTRIBUTE_TYPE_TAG);
		result.append(FormTags.NAMEVALUE_TOKEN_STR);
		result.append(pieces[ATTRIBUTE_TYPE]);
		// Value info
		result.append(FormTags.TOKENIZER_STR);
		result.append(FormTags.VALUE_TABLE_TAG);
		result.append(FormTags.NAMEVALUE_TOKEN_STR);
		result.append(pieces[VALUE_TABLE]);
		result.append(FormTags.TOKENIZER_STR);
		result.append(FormTags.VALUE_FIELD_TAG);
		result.append(FormTags.NAMEVALUE_TOKEN_STR);
		result.append(pieces[VALUE_FIELD]);
		result.append(FormTags.TOKENIZER_STR);
		result.append(FormTags.VALUE_TYPE_TAG);
		result.append(FormTags.NAMEVALUE_TOKEN_STR);
		result.append(pieces[VALUE_TYPE]);
	}

	public static String[] parse(String pivot) {
		return pivot.split(FormTags.TOKENIZER_STR);
	}

}