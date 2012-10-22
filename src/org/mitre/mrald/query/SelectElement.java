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
import org.mitre.mrald.util.Config;
import org.mitre.mrald.util.FormTags;
import org.mitre.mrald.util.MraldException;
import org.mitre.mrald.util.MraldOutFile;
/**
 *  The SqlElement class is the abstract class for all of the query elements
 *  templates needed to populate a complete query string. This class contains
 *  the common functionality for building query strings in general
 *
 *  CrossDataBase 8/10/07: GH Add the databaseName and the schema name to the select clause
 *
 *@author     Gail Hamilton
 *@created    February 17, 2001
 *@version    1.0
 *@see        java.lang.Object
 */
public class SelectElement extends SqlElements
{

    //private Integer order = null;


    /**
     *  Constructor for the QueryElements object
     *
     *@since
     */
    public SelectElement()
    {
        super();
        elementType = FormTags.SELECT_TAG;
    }


    /**
     *  Builds a value String for a name value pair from the given parameters.
     *  Ex: <code>Table:ASQPARRGMT~Field:OAGATGDATETOD~Order:28</code>
     *
     *@param  table    Table name
     *@param  synonym  Synonym name, if requried. If it is not required, pass a
     *      <code>null</code>.
     *@param  field    Field name in table
     *@param  order    Order string. This must be unique in the form.
     *@param  as       Synonym for the field name.  The column returned will have this name.
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
     *@param  currentGroupByList         List of Group By parameters
     *@return                            Description of the Returned Value
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
     *  CrossDataBase: Add the databaseName and the schema name to the select clause
     *
     *@param  currentSelectList  Description of Parameter
     *@return                    Description of the Returned Value
     *@since
     */
    public ArrayList<String> buildSelect(ArrayList<String> currentSelectList)
    {
    	String dbName = nameValues.getValue(FormTags.DB_NAME_TAG)[0];
    	String schema = nameValues.getValue(FormTags.SCHEMA_TAG)[0];
    	String table = nameValues.getValue(FormTags.TABLE_TAG)[0];
        String synValue = nameValues.getValue(FormTags.SYN_TAG)[0];
        //if there is a synomyn - use this
        if (!synValue.equals(Config.EMPTY_STR))
        {
            table = synValue;
        }
        else //In this case the database, schema name are required to completely specify the
        	//column being referenced
        {
        	if (!dbName.equals(Config.EMPTY_STR) && (!schema.equals(Config.EMPTY_STR)) )
        		table = dbName + "." + schema + "." + table;
        }

        String newValue = table + "." + nameValues.getValue(FormTags.FIELD_TAG)[0];
        String orderStr = nameValues.getValue(FormTags.ORDER_TAG)[0];
        String asValue = nameValues.getValue(FormTags.AS_TAG)[0];
        if (!asValue.equals(Config.EMPTY_STR))
        {
            newValue = newValue + " AS \"" + asValue + "\"";
        }
        if (!currentSelectList.contains(newValue))
        {
            currentSelectList.add(newValue + FormTags.TOKENIZER_STR + orderStr);
        }
        return currentSelectList;
    }

    /**
     *  Description of the Method
     *
     *  CrossDataBase 8/10/07: GH Add the databaseName and the schema name to the select clause
     *@param  currentFromList  Description of Parameter
     *@return                  Description of the Returned Value
     *@since
     */
    public ArrayList<String> buildFrom(ArrayList<String> currentFromList)
    {
    	String dbName = nameValues.getValue(FormTags.DB_NAME_TAG)[0];
    	String schema = nameValues.getValue(FormTags.SCHEMA_TAG)[0];

        String newValue = nameValues.getValue(FormTags.TABLE_TAG)[0];
        String synValue = nameValues.getValue(FormTags.SYN_TAG)[0];

        if (!dbName.equals(Config.EMPTY_STR) && (!schema.equals(Config.EMPTY_STR)) )
        {
        	newValue = dbName + "." + schema + "." + newValue;
        	MraldOutFile.logToFile(Config.getProperty("LOGFILE"), "SelectElement 1: buildFrom: from Table Name is :"  + newValue);

        }

        if (!synValue.equals(Config.EMPTY_STR))
        {
            newValue = newValue + " " + synValue;
        }
        MraldOutFile.logToFile(Config.getProperty("LOGFILE"), "SelectElement: buildFrom: from Table Name is :"  + newValue);

        if (!currentFromList.contains(newValue))
        {
            currentFromList.add(newValue);
        }
        return currentFromList;
    }


    /**
     *  Description of the Method
     *
     *@param  currentWhereList           Description of Parameter
     *@return                            Description of the Returned Value
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
     *@param  currentWhereOrList         Description of Parameter
     *@return                            Description of the Returned Value
     *@exception  MraldException  Description of Exception
     *@since
     */
/*     public ArrayList buildWhereOr(ArrayList currentWhereOrList)
        throws MraldException
    {
        return currentWhereOrList;
    }
 */

    /**
     *  Description of the Method
     *
     *@param  currentOrderBy             Description of Parameter
     *@return                            Description of the Returned Value
     *@exception  MraldException  Description of Exception
     *@since
     */
    public ArrayList<String> buildOrderBy(ArrayList<String> currentOrderBy)
        throws MraldException
    {
        return currentOrderBy;
    }
}



