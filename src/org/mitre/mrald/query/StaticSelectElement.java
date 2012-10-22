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
 *  This element allows for the addition of a specified string to the SELECT
 *  clause. This is useful when you need to deviate from the TABLE.FIELD format
 *  of the normal SelectElement.
 *
 *@author     jchoyt
 *@created    August 30, 2003
 */
public class StaticSelectElement extends SqlElements
{

    //private Integer order = null;

    /**
     *  Constructor for the StaticSelectElement. Sets the elementType to SELECT.
     *
     *@since
     */
    public StaticSelectElement()
    {
        super();
        elementType = FormTags.STATIC_SELECT_TAG;
    }


    /**
     *  Adds the table to the FROM clause. If a synonym is used, it is added as
     *  well.
     *
     *@param  currentFromList  Description of Parameter
     *@return                  Description of the Returned Value
     *@since
     */
    public ArrayList<String> buildFrom( ArrayList<String> currentFromList )
    {
        String newValue = nameValues.getValue( FormTags.TABLE_TAG )[0];
        String synValue = nameValues.getValue( FormTags.SYN_TAG )[0];
        if ( !synValue.equals( Config.EMPTY_STR ) )
        {
            newValue = newValue + " " + synValue;
        }
        if ( !currentFromList.contains( newValue ) )
        {
            currentFromList.add( newValue );
        }
        return currentFromList;
    }


    /**
     *  Does nothing. Added to satisfy the SqlElements interface.
     *
     *@param  currentGroupByList  List of Group By parameters
     *@return                     Description of the Returned Value
     *@since
     */
    public ArrayList<String> buildGroupBy( ArrayList<String> currentGroupByList )
    {
        return currentGroupByList;
    }


    /**
     *  Does nothing. Added to satisfy the SqlElements interface.
     *
     *@param  currentOrderBy  Description of Parameter
     *@return                 Description of the Returned Value
     *@since
     */
    public ArrayList<String> buildOrderBy( ArrayList<String> currentOrderBy )
    {
        return currentOrderBy;
    }


    /**
     *  Adds a section to a query's SELECT clause.
     *
     *@param  currentSelectList  Description of Parameter
     *@return                    Description of the Returned Value
     *@since
     */
    public ArrayList<String> buildSelect( ArrayList<String> currentSelectList )
    {
        String newValue = nameValues.getValue( FormTags.VALUE_TAG )[0];
        String orderStr = nameValues.getValue( FormTags.ORDER_TAG )[0];
        String asValue = nameValues.getValue(FormTags.AS_TAG)[0];
        if (!asValue.equals(Config.EMPTY_STR))
        {
            newValue = newValue + " AS \"" + asValue + "\"";
        }
        if ( !currentSelectList.contains( newValue ) )
        {
            currentSelectList.add( newValue + FormTags.TOKENIZER_STR + orderStr );
        }
        return currentSelectList;
    }


    /**
     *  Does nothing. Added to satisfy the SqlElements interface.
     *
     *@param  currentWhereList  Description of Parameter
     *@return                   Description of the Returned Value
     *@since
     */
    public ArrayList<String> buildWhere( ArrayList<String> currentWhereList )
    {
        return currentWhereList;
    }
}

