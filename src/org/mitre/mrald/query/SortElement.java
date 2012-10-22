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
import org.mitre.mrald.util.Config;
import org.mitre.mrald.util.FormTags;
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
public class SortElement extends SqlElements
{

    private Integer order = null;


    /**
     *  Constructor for the QueryElements object
     *
     *@since
     */
    public SortElement()
    {
        super();
        elementType = FormTags.SORT_TAG;

    }


    /**
     *  Description of the Method
     *
     *@param  currentFromList  Description of Parameter
     *@return                  Description of the Returned Value
     *@since
     */

    public ArrayList<String> buildFrom( ArrayList<String> currentFromList )
    {
        String newValue = nameValues.getValue( FormTags.TABLE_TAG )[0];

        if ( newValue.equals( Config.EMPTY_STR ) )
        {
            return currentFromList;
        }

        String synValue = nameValues.getValue( FormTags.SYN_TAG )[0];

        if ( !synValue.equals( Config.EMPTY_STR ) )
        {
            newValue = newValue + " " + synValue;
        }
//        String orderStr = nameValues.getValue( FormTags.ORDER_TAG )[0];

        if ( !currentFromList.contains( newValue ) )
        {
            currentFromList.add( newValue );
        }

        return currentFromList;
    }


    /**
     *  Description of the Method
     *
     *@param  currentGroupByList         List of Group By parameters
     *@return                            Description of the Returned Value
     *@exception  MraldException  Description of Exception
     *@since
     */
    public ArrayList<String> buildGroupBy( ArrayList<String> currentGroupByList )
        throws MraldException
    {
        return currentGroupByList;
    }



    /**
     *  Description of the Method
     *
     *@param  currentOrderBy  Description of Parameter
     *@return                 Description of the Returned Value
     *@since
     */
    public ArrayList<String> buildOrderBy( ArrayList<String> currentOrderBy )
    {
        String synValue = nameValues.getValue( FormTags.SYN_TAG )[0];
        String newValue = null;

        if ( !synValue.equals( Config.EMPTY_STR ) )
        {
            newValue = synValue + "." + nameValues.getValue( FormTags.FIELD_TAG )[0];

        }
        else if ( nameValues.getValue( FormTags.TABLE_TAG )[0].equals( Config.EMPTY_STR ) )
        {
            newValue = nameValues.getValue( FormTags.FIELD_TAG )[0];
        }
        else
        {

            newValue = nameValues.getValue( FormTags.TABLE_TAG )[0] + "." + nameValues.getValue( FormTags.FIELD_TAG )[0];
        }

        String orderType = nameValues.getValue( FormTags.ORDER_TYPE_TAG )[0];
        order = new Integer( nameValues.getValue( FormTags.ORDER_TAG )[0] );

        if ( orderType.equals( "" ) )
        {
            orderType = "ASC";
        }

        newValue = newValue + " " + orderType + FormTags.TOKENIZER_STR + order;
        if ( !currentOrderBy.contains( newValue ) )
        {
            currentOrderBy.add( newValue );
        }

        return currentOrderBy;
    }


    /**
     *  Description of the Method
     *
     *@param  currentSelectList          Description of Parameter
     *@return                            Description of the Returned Value
     *@exception  MraldException  Description of Exception
     *@since
     */
    public ArrayList<String> buildSelect( ArrayList<String> currentSelectList )
        throws MraldException
    {
        return currentSelectList;
    }


    /**
     *  Description of the Method
     *
     *@param  currentWhereList           Description of the Parameter
     *@return                            Description of the Returned Value
     *@exception  MraldException  Description of Exception
     *@since
     */
    public ArrayList<String> buildWhere( ArrayList<String> currentWhereList )
        throws MraldException
    {
        return currentWhereList;
    }


    /**
     *  This element is invalid if there are not a table name and field name
     *  associated with it. Therefore in the postprocessing we check to make
     *  sure we have a valid Value. If there isn't one, set this element's
     *  isActive to false.
     *
     *@param  msg          Description of the Parameter
     *@param  currentName  Description of the Parameter
     *@return              Description of the Return Value
     *@since
     */
    public String postProcess( MsgObject msg, String currentName )
    {
        String[] tables = nameValues.getValue( FormTags.TABLE_TAG );
        String[] fields = nameValues.getValue( FormTags.FIELD_TAG );
        if ( tables.length == 1 &&
                tables[0].equals( Config.EMPTY_STR ) )
        {
            isActive = false;
        }
        else if ( fields.length == 1 &&
                fields[0].equals( Config.EMPTY_STR ) )
        {
            isActive = false;
        }
        return currentName;
    }
}
