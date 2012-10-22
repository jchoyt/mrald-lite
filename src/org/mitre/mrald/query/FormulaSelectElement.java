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
/**
 *  Allows for the use of formulas in SQL queries.
 *  <ul>
 *    <li> Tag names must start with 'FormulaSelect'
 *    <li> The values attribute of the tag should be of the form:
 *    <ul>
 *      <li> value ="Table:VHFNAVAIDS~Table:NDBNAVAIDS~Field:(VHFNAVAIDS.VORLONGITUDE+NDBNAVAIDS.LONGITUDE)/2"
 *
 *      <li> Where every Table used in the formula has a Table:XXXX name/value
 *      pair.
 *    </ul>
 *
 *    <li> To use synonyms, enter table names like : "Table:TableName Synonym"
 *    and use the synonyms in the formula
 *    <li> Follow your database's rules for creating formulas. <br>
 *
 *    <li> Only tested in Oracle.
 *  </ul>
 *
 *
 *@author     Jeffrey Hoyt
 *@created    1/7/2002
 *@version    1.0
 *@see        org.mitre.mrald.query.SqlElements
 */
public class FormulaSelectElement extends SqlElements
{

    /**
     *  Constructor for the QueryElements object
     *
     *@since
     */
    public FormulaSelectElement()
    {
        super();
        elementType = FormTags.FORMULA_SELECT_TAG;
    }


    /**
     *  Returns the input ArrayList unchanged. No changes are required for this
     *  SqlElement.
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
     *  Adds the formula from the original HTML tag. This does not append the
     *  table name to the beginning - the form creator is responsible for adding
     *  the appropriate table names.
     *
     *@param  currentSelectList  Description of Parameter
     *@return                    Description of the Returned Value
     *@since
     */
    public ArrayList<String> buildSelect( ArrayList<String> currentSelectList )
    {
        String newValue = nameValues.getValue( FormTags.FORMULA_TAG )[0];
        String orderStr = nameValues.getValue( FormTags.ORDER_TAG )[0];
        String asValue = nameValues.getValue( FormTags.AS_TAG )[0];
        if ( !asValue.equals( Config.EMPTY_STR ) )
        {
            newValue = newValue + " " + asValue;
        }
        if ( !currentSelectList.contains( newValue ) )
        {
            currentSelectList.add( newValue + FormTags.TOKENIZER_STR + orderStr );
        }
        return currentSelectList;
    }


    /**
     *  Adds all tables from the Table:XXXXX tags listed in the original HTML
     *  tag
     *
     *@param  currentFromList            Description of Parameter
     *@return                            Description of the Returned Value
     *@exception  MraldException  Description of Exception
     *@since
     */
    public ArrayList<String> buildFrom( ArrayList<String> currentFromList )
        throws MraldException
    {
        String[] newValue = nameValues.getValue( FormTags.TABLE_TAG );
        for ( int i = 0; i < newValue.length; i++ )
        {
            if ( !currentFromList.contains( newValue[i] ) )
            {
                currentFromList.add( newValue[i] );
            }
        }
        return currentFromList;
    }


    /**
     *  Returns the input ArrayList unchanged. No changes are required for this
     *  SqlElement.
     *
     *@param  currentWhereList           Description of Parameter
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
     *  Returns the input ArrayList unchanged. No changes are required for this
     *  SqlElement.
     *
     *@param  currentWhereOrList         Description of Parameter
     *@return                            Description of the Returned Value
     *@exception  MraldException  Description of Exception
     *@since
     */
    /*
    public ArrayList buildWhereOr( ArrayList currentWhereOrList )
        throws MraldException
    {
        return currentWhereOrList;
    }
    */

    /**
     *  Returns the input ArrayList unchanged. No changes are required for this
     *  SqlElement.
     *
     *@param  currentOrderBy             Description of Parameter
     *@return                            Description of the Returned Value
     *@exception  MraldException  Description of Exception
     *@since
     */
    public ArrayList<String> buildOrderBy( ArrayList<String> currentOrderBy )
        throws MraldException
    {
        return currentOrderBy;
    }
}

