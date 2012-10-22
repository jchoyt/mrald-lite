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
 *  The StatElement is responsible for adding statistical functions to queries.
 *
 *@author     Gail Hamilton
 *@created    February 17, 2001
 *@version    1.0
 *@see        java.lang.Object
 */
public class StatElement extends SqlElements
{
    boolean complete = false;
    String function;


    /**
     *  Constructor for the QueryElements object
     *
     *@since
     */
    public StatElement()
    {
        super();
        elementType = FormTags.STAT_TAG;
    }


    /**
     *  Adds the table names to the FROM clause, if necessary
     *
     *@param  currentFromList            Description of Parameter
     *@return                            Description of the Returned Value
     *@exception  MraldException  Description of Exception
     *@since
     */
    public ArrayList<String> buildFrom( ArrayList<String> currentFromList )
        throws MraldException
    {
        if ( !complete )
        {
            return currentFromList;
        }
        // if count(*), add all tables, then return
//        String statValue = nameValues.getValue( FormTags.STAT_TAG )[0];
        if ( function.indexOf( '*' ) != -1 )
        {
            addAllTables( currentFromList );
            return currentFromList;
        }
        //add normal table
        if ( !currentFromList.contains( fromTableName ) )
        {
            currentFromList.add( fromTableName );
        }
        //check to see if additional tables are needed for GROUP_SELECT_STR
        String nextVal = nameValues.getValue( FormTags.GROUP_SELECT_STR )[0];
        if ( nextVal.equals( Config.EMPTY_STR ) )
        {
            //nope, no more needed :o)
            return currentFromList;
        }
        nextVal = nextVal.substring( 0, nextVal.indexOf( "." ) );
        if ( !currentFromList.contains( nextVal ) )
        {
            currentFromList.add( nextVal );
        }
        return currentFromList;
    }


    /**
     *  Description of the Method
     *
     *@param  currentGroupByList  List of Group By parameters
     *@return                     Description of the Returned Value
     *@since
     */
    public ArrayList<String> buildGroupBy( ArrayList<String> currentGroupByList )
    {
        if ( !complete )
        {
            return currentGroupByList;
        }
        String groupStr = nameValues.getValue( FormTags.GROUP_STR )[0];
        if ( groupStr.equals( FormTags.HOUR_TAG ) )
        {
            String newValue = "TO_CHAR( " + nameValues.getValue( FormTags.GROUP_SELECT_STR )[0] + ", 'YYYY-MM-DD HH24') ";
            if ( !currentGroupByList.contains( newValue ) )
            {
                currentGroupByList.add( newValue );
            }
        }
        else if ( groupStr.equals( FormTags.DAY_TAG ) )
        {
            String newValue = "TO_CHAR( " + nameValues.getValue( FormTags.GROUP_SELECT_STR )[0] + ", 'YYYY-MM-DD') ";
            if ( !currentGroupByList.contains( newValue ) )
            {
                currentGroupByList.add( newValue );
            }
        }
        else if ( groupStr.equals( FormTags.MONTH_TAG ) )
        {
            String newValue = "TO_CHAR( " + nameValues.getValue( FormTags.GROUP_SELECT_STR )[0] + ", 'YYYY-MM') ";
            if ( !currentGroupByList.contains( newValue ) )
            {
                currentGroupByList.add( newValue );
            }
        }
        return currentGroupByList;
    }


    /**
     *  Doesn't do anything - simply returns the passed ArrayList
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


    /**
     *  Description of the Method
     *
     *@param  currentSelectList  Description of Parameter
     *@return                    Description of the Returned Value
     *@since
     */
    public ArrayList<String> buildSelect( ArrayList<String> currentSelectList )
    {
//	String typeStr = nameValues.getValue(FormTags.TYPE_TAG)[0];
	// MraldOutFile.appendToFile( Config.getProperty( "LOGFILE" ), "StatElement: buildSelect: Field :" + field + "  Data type " + typeStr+ "\r\n"  );

        if ( !complete )
        {
            return currentSelectList;
        }
        String groupStr = nameValues.getValue( FormTags.GROUP_STR )[0];
        if ( !groupStr.equals( "" ) )
        {
            addGroupString( currentSelectList, groupStr );
        }
        String newValue = null;
        if ( function.indexOf( '*' ) != -1 )
        {
            newValue = "Count(*)" + " AS All_Count" + FormTags.TOKENIZER_STR + order;
        }
        else
        {
            String myColumn = function;
            //create synomym name for function
            int index = myColumn.toUpperCase().indexOf("DISTINCT");

            if ( index != -1){
                myColumn = myColumn.substring(0,index-1) + "_Distinct";
            }

            if ( function.toUpperCase().indexOf( "DISTINCT" ) == -1 )
            {
                newValue =   function + "( " + field + ") AS \"" + myColumn + " " + field + "\"" + FormTags.TOKENIZER_STR + order;
            }
            else {
                //distinct occur here
                newValue =   function + "( " + field + ")) AS \"" + myColumn + " " + field + "\"" + FormTags.TOKENIZER_STR + order;
            }
        }
        if ( !currentSelectList.contains( newValue ) )
        {
            currentSelectList.add( newValue );
        }
        return currentSelectList;
    }


    /**
     *  Doesn't do anything - simply returns the passed ArrayList
     *
     *@param  currentWhereList           Description of Parameter
     *@return                            Description of the Returned Value
     *@exception  MraldException  Description of Exception
     *@since
     */
    public ArrayList<String> buildWhere( ArrayList<String> currentWhereList )
        throws MraldException
    {
	  if ((table == null) || (table.equals("")))
		  return currentWhereList;

//	 String typeStr = nameValues.getValue(FormTags.TYPE_TAG)[0];

//	 String addWhere = "";
     return currentWhereList;
    }


    /**
     *  Constructor for the addAllTables object
     *
     *@param  currentFromList  Description of the Parameter
     */
    protected void addAllTables( ArrayList<String> currentFromList )
    {
        String[] tableNames = nameValues.getValue( FormTags.TABLE_TAG );
        for ( int i = 0; i < tableNames.length; i++ )
        {
            if ( !currentFromList.contains( tableNames[i] ) )
            {
                currentFromList.add( tableNames[i] );
            }
        }
    }


    /**
     *  Adds a feature to the GroupString attribute of the StatElement object
     *
     *@param  currentSelectList  The feature to be added to the GroupString
     *      attribute
     *@param  groupStr           The feature to be added to the GroupString
     *      attribute
     */
    protected void addGroupString( ArrayList<String> currentSelectList, String groupStr )
    {
        String groupSelectString = nameValues.getValue( FormTags.GROUP_SELECT_STR )[0];
        String dateFormat = null;
        if ( groupStr.equals( FormTags.HOUR_TAG ) )
        {
            dateFormat = ", 'YYYY-MM-DD HH24') ";
        }
        else if ( groupStr.equals( FormTags.DAY_TAG ) )
        {
            dateFormat = ", 'YYYY-MM-DD') ";
        }
        else if ( groupStr.equals( FormTags.MONTH_TAG ) )
        {
            dateFormat = ", 'YYYY-MM') ";
        }
        String newValue = "TO_CHAR( " + groupSelectString + dateFormat + groupStr + FormTags.TOKENIZER_STR + order;
        if ( !currentSelectList.contains( newValue ) )
        {
            currentSelectList.add( newValue );
        }
    }


    /**
     *  Description of the Method
     */
    protected void init()
    {
        super.init();
        function = nameValues.getValue( FormTags.FUNCTION )[0];
        complete = ( !table.equals( "" ) &&
                !function.equals( "" ) &&
                ( !field.equals( "" ) || function.indexOf( '*' ) != -1 ) );
    }
}

