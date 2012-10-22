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
import java.util.StringTokenizer;

import org.mitre.mrald.control.MraldEntry;
import org.mitre.mrald.control.MsgObject;
import org.mitre.mrald.util.Config;
import org.mitre.mrald.util.FormTags;
import org.mitre.mrald.util.MiscUtils;
import org.mitre.mrald.util.MraldException;
/**
 *  Class to build sub queries. Tags are exected of the form:<br>
 *  Outer-Table:table~Field:field||Select1-Table:table~Field:field~Order:#||
 *  etc.<br>
 *  where each XXX-YYYY needs to be a valid name/value pair that would be passed
 *  if the information came from another form, and || is the separating String.
 *  <br>
 *  This is fundamentally different from the other SqlElement tags as you have
 *  to incorporate their data within this single tag.
 *
 *@author     jchoyt
 *@created    May 24, 2002
 */
public class SubQueryElement extends SqlElements
{

    MsgObject subQueryMsg;
    MsgObject outer;


    /**
     *  Constructor for the QueryElements object
     *
     *@since
     */
    public SubQueryElement()
    {
        super();
        elementType = FormTags.SUBQUERY_TAG;
        subQueryMsg = new MsgObject();
        outer = new MsgObject();
    }


    /**
     *  Constructor for the QueryElements object
     *
     *@param  msg  Description of Parameter
     *@since
     */
    public SubQueryElement(MsgObject msg)
    {
        super(msg);
        elementType = FormTags.SUBQUERY_TAG;
        subQueryMsg = new MsgObject();
        outer = new MsgObject();
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
     *
     *@param  currentSelectList          Description of Parameter
     *@return                            Description of the Returned Value
     *@exception  MraldException  Description of Exception
     *@since
     */
    public ArrayList<String> buildSelect(ArrayList<String> currentSelectList)
        throws MraldException
    {
        return currentSelectList;
    }


    /**
     *  Description of the Method
     *
     *@param  currentFromList            Description of Parameter
     *@return                            Description of the Returned Value
     *@exception  MraldException  Description of Exception
     *@since
     */
    public ArrayList<String> buildFrom(ArrayList<String> currentFromList)
        throws MraldException
    {
        /*
         *  need to add the outer table name to the list
         */
        String newValue = outer.getValue(FormTags.TABLE_TAG)[0];
        /*
         *  need to add the synonym for the outer table name to the list
         */
        String synValue = outer.getValue(FormTags.SYN_TAG)[0];

        if (!synValue.equals(Config.EMPTY_STR))
        {
            newValue = newValue + " " + synValue;
        }

        if (!currentFromList.contains(newValue))
        {
            currentFromList.add(newValue);
        }
        return currentFromList;
    }


    /**
     *  Instead of focusing on creating a single MsgObject, we must create two -
     *  one for the outer fields and one for the contents of the subquery.<p>
     *
     *  Need to overwrite the super class process method because of special case
     *  tags. Because we need to encode much more data, there is an extra level
     *  of "de-tokenizing" that needs to be done.<br>
     *
     *
     *@param  valueList                Description of Parameter
     *@exception  SqlElementException  Description of Exception
     */
    public void process(String[] valueList)
        throws SqlElementException
    {
        /*
         *  split up the n/v pairs between the ||'s and add them to a new MsgObject
         */
        int numOfArrayLists = valueList.length;
        StringTokenizer valueTokens = null;
        String valueArray = null;
        String name = null;
        String value = null;
        //If there is more than one Array List with the same name
        //then these will be parsed up and added to the same
        //Object.
        for (int i = 0; i < numOfArrayLists; i++)
        {
            valueArray = valueList[i];
            valueTokens = new StringTokenizer(valueArray, "||");
            if ( !valueTokens.hasMoreTokens() )
            {
                isActive = false;
            }            while (valueTokens.hasMoreTokens())
            {
                isActive = true;
                //Use the : as a token to get the name value pair.
                StringTokenizer nameValuePair = new StringTokenizer(valueTokens.nextToken(), "-");
                {
                    //Parse out the = and add the name value pair to the sub query MsgObject
                    name = nameValuePair.nextToken();
                    /*
                     *  some databases allow spaces in a table name.
                     *  Check here and put double quotes (") around any table name
                     *  Check to make sure the quotes aren't already there, as well
                     */
                    if ( name.indexOf( " " ) > 0 && name.charAt( 0 ) != '"' )
                    {
                        name = "\"" + name + "\"";
                    }
                    value = nameValuePair.nextToken();
                }
                if (name.equals("Outer"))
                {
                    outer = MiscUtils.parseNVPairs(value);
                }
                else
                {
                    subQueryMsg.setValue(name, value);
                }
            }
        }
        /*
         *  try
         *  {
         *  super.process(valueList);
         *  }
         *  catch (org.mitre.mrald.parser.MraldParserException e)
         *  {
         *  throw new SqlElementException(e.getMessage());
         *  }
         */
        //Set the sqlNumber
        String sqlNo = nameValues.getValue(FormTags.SQL_THREAD_NUM_TAG)[0];
        if (!sqlNo.equals(Config.EMPTY_STR))
        {
            setSqlNo(sqlNo);
        }
        String outputManager = nameValues.getValue(FormTags.OUTPUT_MANAGER)[0];
        if (!outputManager.equals(Config.EMPTY_STR))
        {
            setOutputType(outputManager);
        }
    }


    /**
     *@param  currentWhereList           Description of Parameter
     *@return                            Updated List of where clauses input by
     *      user
     *@exception  MraldException  Exception
     *@since
     */
    public ArrayList<String> buildWhere(ArrayList<String> currentWhereList)
        throws MraldException
    {
        /*
         *  finish the MsgObject and submit it to MRALD to build the sub query
         */
        subQueryMsg.setUserId(getClass().getName());
        MraldEntry go = new MraldEntry(subQueryMsg);
        String subquery;
        try
        {
            subquery = go.buildQuery();
        }
        catch (org.mitre.mrald.control.WorkflowStepException e)
        {
            throw new MraldException(e.getMessage());
        }
        /*
         *  use built query and OutField info to build the new clause
         */
        String outerTable = outer.getValue("Table")[0].toString();//FormTags.OUTER_TABLE_TAG )[0].toString();
        String outerField = outer.getValue("Field")[0].toString();//FormTags.OUTER_FIELD_TAG )[0].toString();
        boolean doesntContain = outer.getValue("Contains")[0].toString().toLowerCase().equals("no");
        StringBuffer ret = new StringBuffer();
        ret.append(outerTable);
        ret.append(".");
        ret.append(outerField);
        if (doesntContain)
            ret.append(" NOT IN (");
            else
        ret.append(" IN (");
        ret.append(subquery);
        ret.append(")");
        String newValue = ret.toString();
        /*
         *  add the new clause to the currentWhereList and return
         */
        if (!currentWhereList.contains(newValue))
        {
            currentWhereList.add(newValue);
        }
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

