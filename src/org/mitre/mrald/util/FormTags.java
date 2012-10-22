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

/**
 *  Global location for configurations
 *
 *@author     Gail Hamilton
 *@created    Feb. 18th, 2001
 */
public class FormTags
{
    //The names for the tags in the form.
    //This can be changed or overwritten as required by forms.

    public final static String OBJECT_NAME = "Name";
    public final static String COUNT_TAG = "Count";

    public final static String AS_TAG = "As";
    public final static String ATTRIBUTE_TAG = "Attribute";
    public final static String COMPARE_FIELDS_FILTER="CompareFieldsFilter";
    public final static String CONCAT_FILTER_TAG="ConcatFilter";
    public final static String CONCAT_SELECT_TAG="ConcatSelect";
    public final static String DATASOURCE_TAG = "Datasource";
    public final static String DB_ACTION_TAG = "DBAction";
    public final static String ENTITY_TAG = "Entity";
    public final static String DELETE_TAG = "Delete";
    public final static String FIELD_TAG = "Field";
    public final static String FILTER_TAG = "Filter";
    public final static String FORMAT_TAG = "Format";
    public final static String FORMULA_SELECT_TAG = "FormulaSelect";
    public final static String FORMULA_TAG = "Formula";
    public final static String GROUP_SELECT_STR = "GroupSelect";
    public final static String GROUP_STR = "Group";
    public final static String LABEL_TAG = "label";
    public final static String LIKE_TAG = "like";
    public final static String LINK_TAG = "Link";
    public final static String NOT_LIKE_TAG = "not like";
    public final static String NOT_STARTS_WITH_TAG = "not starts";
    public final static String PG_CONTAINS_IGNORE_CASE = "pg contains";
    public final static String OPERATOR_TAG = "Operator";
    public final static String ORFILTER_TAG = "OrFilter";
    public final static String ORDER_TAG = "Order";
    public final static String ORDER_TYPE_TAG = "OrderType";
    public final static String PRIMARY_LINK_TAG = "PrimaryLink";
    public final static String RANGE_TAG = "Range";
    public final static String ROLE_TAG = "Role";
    public final static String SCHEMA_TAG = "Schema";
    public final static String DB_NAME_TAG = "DBName";
    public final static String SECONDARY_LINK_TAG = "SecondaryLink";
    public final static String SELECT_TAG = "Select";
    public final static String SORT_TAG = "Sort";
    public final static String SQL_THREAD_NUM_TAG = "SqlThread";
    public final static String STARTS_WITH_TAG = "starts";
    public final static String STAT_TAG = "Stat";
    public final static String STATIC_SELECT_TAG = "FixedSelect";
    public final static String SUBQUERY_TAG = "SubQuery";
    public final static String SYN_TAG = "Synomyn";
    public final static String TABLE_TAG = "Table";
    public final static String TYPE_TAG = "Type";
    public final static String VALUE_TAG = "Value";
    public final static String UPDATE_TAG = "Update";

    public final static String ENTITY_TABLE_TAG = ENTITY_TAG + TABLE_TAG;
    public final static String ENTITY_FIELD_TAG = ENTITY_TAG + FIELD_TAG;
    public final static String ENTITY_TYPE_TAG = ENTITY_TAG + TYPE_TAG;
    public final static String ATTRIBUTE_TABLE_TAG = ATTRIBUTE_TAG + TABLE_TAG;
    public final static String ATTRIBUTE_FIELD_TAG = ATTRIBUTE_TAG + FIELD_TAG;
    public final static String ATTRIBUTE_TYPE_TAG = ATTRIBUTE_TAG + TYPE_TAG;
    public final static String VALUE_TABLE_TAG = VALUE_TAG + TABLE_TAG;
    public final static String VALUE_FIELD_TAG = VALUE_TAG + FIELD_TAG;
    public final static String VALUE_TYPE_TAG = VALUE_TAG + TYPE_TAG;

    public final static String HAVING_TAG = "Having";
    public final static String HAVING_OPERATOR_TAG = HAVING_TAG + OPERATOR_TAG;
    public final static String HAVING_VALUE_TAG = HAVING_OPERATOR_TAG + OPERATOR_TAG;

    public final static String TOKENIZER_STR = "~";
    public final static String NAMEVALUE_TOKEN_STR = ":";

    //May also need TimeRange
    public final static String DAY_TAG = "Day";
    public final static String ENABLETIME = "EnableTime";
    public final static String ENDDATE = "EndDate";
    public final static String ENDTIME = "EndTime";
    public final static String FUNCTION = "Function";
    public final static String HOUR_TAG = "Hour";
    public final static String MINUTE_TAG = "Minute";
    public final static String MONTH_TAG = "Month";
    public final static String SECOND_TAG = "Second";
    public final static String STARTDATE = "StartDate";
    public final static String STARTTIME = "StartTime";
    public final static String TIME_TAG = "Time";

    //For RangeElement
    public final static String MAXTAG = "Max";
    public final static String MINTAG = "Min";

    // Tags for Output Manager
    public final static String OUTPUT_FIELD_TAG = "fieldname";//name for DB table column
    public final static String OUTPUT_FORMAT_PATTERN = "formatpattern";//the format to use (See java.text.DecimalFormat and java.text.SimpleDateFormat)
    public final static String OUTPUT_FORMAT_TYPE = "type";// Date or number
    public final static String OUTPUT_MANAGER = "outputManager";
    public final static String OUTPUT_NICENAME_TAG = "nicename";//User friendly name to use instead of hostile DB field name
    public final static String OUTPUT_TAG = "outputformat";//HTML name

    public final static String TABLE_DEPTH = "tableDepth";

   //For DDL
    public final static String INSERT_INTO_ALL_TABLES="!All";

    //for Link Element

    public final static String PRIMARY_TABLE = "Table1";
    public final static String PRIMARY_FIELD = "Field1";
    public final static String FOREIGN_TABLE = "Table2";
    public final static String FOREIGN_FIELD = "Field2";
    public final static String LINK_TYPE = "Link";
    public final static String OUTER_JOIN = "OuterJoin";
    public final static String OUTER_JOIN_RIGHT = "Right";
    public final static String OUTER_JOIN_LEFT = "Left";

    //for Multi Querying

    public final static String LINKING_TYPE_TAG = "linking";
    public final static String UNLINKED = "unlinked";
    public final static String LINKED = "linked";

    //For multiple databases
    public final static String MULTI_DB = "multiDb";

}

