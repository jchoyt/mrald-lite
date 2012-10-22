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
/**
 *  Provides a way to select a concatenation of two VARCHAR (String) fields.
 *  HTML tags should be of the form:<br>
 *  <code>Table1:OOOICOMMON~Field1:CARRIER~Syn1:oooi1~Table2:OAGCOMMON~Field2:CARRIER~Syn2:oag1~As:Carrier^2~Order:1</code>
 *  <br>
 *  where the Syn and As tags are optional.
 *
 *@author     jchoyt
 *@created    July 11, 2002
 */
public class ConcatElement extends SelectElement
{

//    private Integer order = null;


    /**
     *  Constructor for the QueryElements object
     *
     *@since
     */
    public ConcatElement()
    {
        super();
        elementType = FormTags.CONCAT_SELECT_TAG;
    }


    /**
     *  Adds the tables (with synonyms) to the FROM clause in the query.
     *
     *@param  currentFromList  Description of Parameter
     *@return                  Description of the Returned Value
     *@since
     */
    public ArrayList<String> buildFrom(ArrayList<String> currentFromList)
    {
        /*
         *  add the first table
         */
        String newValue = nameValues.getValue(FormTags.TABLE_TAG + "1")[0];
        String synValue = nameValues.getValue(FormTags.SYN_TAG + "1")[0];
        if (!synValue.equals(Config.EMPTY_STR))
        {
            newValue = newValue + " " + synValue;
        }
        if (!newValue.equals( Config.EMPTY_STR ) && !currentFromList.contains(newValue))
        {
            currentFromList.add(newValue);
        }
        /*
         *  add the second table
         */
        newValue = nameValues.getValue(FormTags.TABLE_TAG + "2")[0];
        synValue = nameValues.getValue(FormTags.SYN_TAG + "2")[0];
        if (!synValue.equals(Config.EMPTY_STR))
        {
            newValue = newValue + " " + synValue;
        }
        if (!newValue.equals( Config.EMPTY_STR ) && !currentFromList.contains(newValue))
        {
            currentFromList.add(newValue);
        }
        /*
         *  return the list
         */
        return currentFromList;
    }


    /**
     *  Builds a value String for a name value pair from the given parameters.
     *  Ex: <code>Table:ASQPARRGMT~Field:OAGATGDATETOD~Order:28</code>
     *
     *@param  currentSelectList  Description of the Parameter
     *@return                    A string suitable for a HTML tag in a form.
     */
    /*
     *  public static String buildValue(String table, String synonym, String field, String as, String order)
     *  {
     *  StringBuffer ret = new StringBuffer();
     *  ret.append(FormTags.TABLE_TAG);
     *  ret.append(":");
     *  ret.append(table);
     *  if (synonym != null)
     *  {
     *  ret.append("~");
     *  ret.append(FormTags.SYN_TAG);
     *  ret.append(":");
     *  ret.append(synonym);
     *  }
     *  ret.append("~");
     *  ret.append(FormTags.FIELD_TAG);
     *  ret.append(":");
     *  ret.append(field);
     *  if (as != null)
     *  {
     *  ret.append("~");
     *  ret.append(FormTags.AS_TAG);
     *  ret.append(":");
     *  ret.append(as);
     *  }
     *  ret.append("~");
     *  ret.append(FormTags.ORDER_TAG);
     *  ret.append(":");
     *  ret.append(order);
     *  return ret.toString();
     *  }
     */
    /**
     *  Adds select fields to the SELECT clause in the query.
     *
     *@param  currentSelectList  Description of Parameter
     *@return                    Description of the Returned Value
     *@since
     */
    public ArrayList<String> buildSelect(ArrayList<String> currentSelectList)
    {
        /*
         *  add the first part of the concatenation
         */
        String table = nameValues.getValue(FormTags.TABLE_TAG + "1")[0];
        String synValue = nameValues.getValue(FormTags.SYN_TAG + "1")[0];
        //if there is a synomyn - use this
        if (!synValue.equals(Config.EMPTY_STR))
        {
            table = synValue;
        }
        String newValue = table + "." + nameValues.getValue(FormTags.FIELD_TAG + "1")[0];
        /*
         *  add the second part of the concatenation
         */
        table = nameValues.getValue(FormTags.TABLE_TAG + "2")[0];
        synValue = nameValues.getValue(FormTags.SYN_TAG + "2")[0];
        //if there is a synomyn - use this
        if (!synValue.equals(Config.EMPTY_STR))
        {
            table = synValue;
        }
        newValue = newValue + "||" + table + "." + nameValues.getValue(FormTags.FIELD_TAG + "2")[0];

        String orderStr = nameValues.getValue(FormTags.ORDER_TAG)[0];
        String asValue = nameValues.getValue(FormTags.AS_TAG)[0];
        if (!asValue.equals(Config.EMPTY_STR))
        {
            newValue = newValue + " " + asValue;
        }
        if (!currentSelectList.contains(newValue))
        {
            currentSelectList.add(newValue + FormTags.TOKENIZER_STR + orderStr);
        }
        return currentSelectList;
    }

}

