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

import java.util.*;

import org.mitre.mrald.control.MsgObject;
import org.mitre.mrald.parser.MraldParserException;
import org.mitre.mrald.parser.ParserElement;
import org.mitre.mrald.util.Config;
import org.mitre.mrald.util.FormTags;
import org.mitre.mrald.util.MraldError;
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
public abstract class SqlElements extends ParserElement implements QueryBuildable
{//used in from clause
    /**
     *  Description of the Field
     */
    public final static String DEFAULT_SQL_NO = "0";
    /**
     *  Description of the Field
     */
    public String getOrder;
    /**
     *  This is a String name for the type of element this is.  The value used should be<br/>
     *  <ul><li>In FormTags</li>
     *  <li>In parser.props</li></ul>
     *
     */
    protected String elementType;
    //Sql No is used for multi queries

    /**
     *  Description of the Field
     */
    protected String outputType = null;
    /**
     *  Description of the Field
     */
    protected String sqlNo = DEFAULT_SQL_NO;
    protected String as;
    protected String field;
    protected String fromTableName;
    protected String operator;
    protected String order;//used in select and where clauses
    protected String synonym;
    protected String table;


    /**
     *  Constructor for the QueryElements object
     *
     *@since
     */
    public SqlElements()
    {
        nameValues = new MsgObject();
        elementType = "SQL";
    }


    /**
     *  Constructor for the QueryElements object
     *
     *@param  thisMsg  Description of Parameter
     *@since
     */
    public SqlElements( MsgObject thisMsg )
    {
        nameValues = thisMsg;
        elementType = "SQL";
    }


    /**
     *  Description of the Method
     *
     *@param  thisOutputType  The new outputType value
     *@since
     */
    public void setOutputType( String thisOutputType )
    {
        outputType = thisOutputType;
    }


    /**
     *  Description of the Method
     *
     *@param  thisSqlNo  The new sqlNo value
     *@since
     */
    public void setSqlNo( String thisSqlNo )
    {
        sqlNo = thisSqlNo;
    }


    /**
     *  Gets the as attribute of the SqlElements object
     *
     *@return    The as value
     */
    public String getAs()
    {
        return as;
    }


    /**
     *  Gets the ElementType attribute of the QueryElements object
     *
     *@return    The ElementType value
     *@since
     */
    public String getElementType()
    {
        return elementType;
    }


    /**
     *  Gets the field attribute of the SqlElements object
     *
     *@return    The field value
     */
    public String getField()
    {
        return field;
    }


    /**
     *  Gets the fromTableName attribute of the SqlElements object
     *
     *@return    The fromTableName value
     */
    public String getFromTableName()
    {
        return fromTableName;
    }


    /**
     *  Description of the Method
     *
     *@return    The nameValues value
     *@since
     */
    public MsgObject getNameValues()
    {
        return nameValues;
    }


    /**
     *  Gets the operator attribute of the SqlElements object
     *
     *@return    The operator value
     */
    public String getOperator()
    {
        return operator;
    }


    /**
     *  Gets the order attribute of the SqlElements object
     *
     *@return    The order value
     */
    public String getOrder()
    {
        return order;//used;
    }


    /**
     *  Description of the Method
     *
     *@return    The outputType value
     *@since
     */
    public String getOutputType()
    {
        return outputType;
    }


    /**
     *  Description of the Method
     *
     *@return    The sqlNo value
     *@since
     */
    public String getSqlNo()
    {
        return sqlNo;
    }


    /**
     *  Gets the synonym attribute of the SqlElements object
     *
     *@return    The synonym value
     */
    public String getSynonym()
    {
        return synonym;
    }


    /**
     *  Gets the table attribute of the SqlElements object
     *
     *@return    The table value
     */
    public String getTable()
    {
        return table;
    }


    /**
     *  Description of the Method
     *
     *@param  currentFromList            Description of Parameter
     *@return                            Description of the Returned Value
     *@exception  MraldException  Description of Exception
     *@since
     */
    public abstract ArrayList<String> buildFrom( ArrayList<String> currentFromList )
        throws MraldException;


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
     *@param  currentOrderBy             Description of Parameter
     *@return                            Description of the Returned Value
     *@exception  MraldException  Description of Exception
     *@since
     */
    public abstract ArrayList<String> buildOrderBy( ArrayList<String> currentOrderBy )
        throws MraldException;


    /**
     *  Description of the Method
     *
     *@param  currentSelectList          Description of Parameter
     *@return                            Description of the Returned Value
     *@exception  MraldException  Description of Exception
     *@since
     */
    public abstract ArrayList<String> buildSelect( ArrayList<String> currentSelectList )
        throws MraldException;


    /**
     *  Description of the Method
     *
     *@param  currentWhereList           Description of Parameter
     *@return                            Description of the Returned Value
     *@exception  MraldException  Description of Exception
     *@since
     */
    public abstract ArrayList<String> buildWhere( ArrayList<String> currentWhereList )
        throws MraldException;


    /**
     *  Description of the Method
     *
     *@param  currentWhereAndList        Description of Parameter
     *@return                            Description of the Returned Value
     *@exception  MraldException  Description of Exception
     *@since
     */
    public ArrayList<String> buildWhereAnd( ArrayList<String> currentWhereAndList )
        throws MraldException
    {
        return buildWhere( currentWhereAndList );
    }


    /**
     *  Preprocessor - carriers out any additional processing required.
     *
     *@param  msg                      Description of Parameter
     *@param  currentName              Description of Parameter
     *@return                          Description of the Returned Value
     *@exception  SqlElementException  Description of Exception
     *@since
     */
    public String preProcess( MsgObject msg, String currentName )
        throws SqlElementException
    {
        return currentName;
    }



    /**
     *  Description of the Method
     *
     *@param  valueList                Description of Parameter
     *@exception  SqlElementException  Description of Exception
     *@since
     */
    public void process( String[] valueList )
        throws SqlElementException
    {
        try
        {
            super.process( valueList );
        }
        catch (MraldParserException e)
        {
            throw new MraldError(e);
        }
        /*
         *  Set the sqlNumber
         */
        String sqlNo = nameValues.getValue( FormTags.SQL_THREAD_NUM_TAG )[0];
        if ( !sqlNo.equals( Config.EMPTY_STR ) )
        {
            setSqlNo( sqlNo );
        }
        String outputManager = nameValues.getValue( FormTags.OUTPUT_MANAGER )[0];
        if ( !outputManager.equals( Config.EMPTY_STR ) )
        {
            setOutputType( outputManager );
        }
        init();
    }


    /**
     *  Sets the as attribute of the SqlElements object
     */
    protected void setAs()
    {
        as = nameValues.getValue( FormTags.AS_TAG )[0];
    }


    /**
     *  Sets the field attribute of the FilterElement object
     */
    protected void setField()
    {
        field = nameValues.getValue( FormTags.FIELD_TAG )[0];
    }


    /**
     *  Setter for Operator
     *
     *@since
     */
    protected void setOperator()
    {
        operator = nameValues.getValue( FormTags.OPERATOR_TAG )[0];
        if ( operator.equals( Config.EMPTY_STR ) )
        {
            operator = "=";
        }
    }


    /**
     *  Sets the order attribute of the SqlElements object
     */
    protected void setOrder()
    {
        order = nameValues.getValue( FormTags.ORDER_TAG )[0];
    }


    /**
     *  Sets the table, synonym, and fromTableName variables appropriately
     *  GH 08/10/07 CrossDataBase enhancements. Look for DB Name and Schema Name
     */
    protected void setTable()
    {
        //If there is a synomyn - use this instead - in building the select statement
        synonym = nameValues.getValue( FormTags.SYN_TAG )[0].toString();
        fromTableName = nameValues.getValue( FormTags.TABLE_TAG )[0].toString();

        /***CrossDataBase GH**/

        String dbName = nameValues.getValue( FormTags.DB_NAME_TAG )[0];

        String schemaName = nameValues.getValue( FormTags.SCHEMA_TAG )[0];

        //Need both values to run query using database name
        if (!dbName.equals( Config.EMPTY_STR ) && !schemaName.equals( Config.EMPTY_STR ))
        {
        	fromTableName = dbName + "." + schemaName + "." + fromTableName;
        }
        /***CrossDataBase end**/

        if ( !synonym.equals( Config.EMPTY_STR ) )
        {
            table = synonym;
            fromTableName = fromTableName + " " + synonym;
        }
        else
        {
            table = fromTableName;
        }

    }


    /**
     *  Description of the Method
     */
    protected void init()
    {
        setTable();
        setField();
        setOperator();
        setAs();
        setOrder();
    }

    /**
     *  translates the nvPairs info back into the String format from the forms
     */
    public String buildValue()
    {
        StringBuilder ret = new StringBuilder();
        String key;
        ret.append( "ElementType:" + elementType );
        ret.append( FormTags.TOKENIZER_STR );
        for (Iterator it = nameValues.getNames().iterator() ; it.hasNext() ; )
        {
            key = (String) it.next();
            String[] values = nameValues.getValue( key );
            /*
             *  add up to n-1 name/value pairs with a tilda after it.  Outside the loop, add the last pair
             */
            for ( int i = 0; i < values.length; i++ )
            {
                ret.append( key.toString() + FormTags.NAMEVALUE_TOKEN_STR + values[i] );
                ret.append( FormTags.TOKENIZER_STR );
            }
        }
        return ret.toString();
    }
}

