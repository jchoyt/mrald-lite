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
import org.mitre.mrald.formbuilder.FormBuilderElement;
import org.mitre.mrald.util.Config;
import org.mitre.mrald.util.DBMetaData;
import org.mitre.mrald.util.FBUtils;
import org.mitre.mrald.util.FormTags;
import org.mitre.mrald.util.MiscUtils;
import org.mitre.mrald.util.MraldException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *  Used to insert a simple clause into the WHERE clause of a query. It
 *  automatically assumes that if you have more than one value, that you mean to
 *  'OR' them over the same table and field name. See the buildWhere method for
 *  more detail.
 *
 *@author     Gail Hamilton
 *@created    February 17, 2001
 *@version    1.0
 *@see        java.lang.Object
 */
public class FilterElement extends SqlElements implements FormBuilderElement
{
    /**
     *  Description of the Field
     */
    protected int noOfOperators;
    /**
     *  Description of the Field
     */
    protected int noOfValues;
    /**
     *  Description of the Field
     */
    protected String type;


    /**
     *  Constructor for the QueryElements object
     *
     *@since
     */
    public FilterElement()
    {
        super();
        elementType = FormTags.FILTER_TAG;
    }


    /**
     *  Constructor for the QueryElements object
     *
     *@param  msg  Description of Parameter
     *@since
     */
    public FilterElement( MsgObject msg )
    {
        super( msg );
        elementType = FormTags.FILTER_TAG;
    }


    /**
     *  Builds a value String for a name value pair from the given parameters.
     *  Ex: <code>Table:ETMSCOMMON~Field:CARRIER~Operator:=~Value:UAS</code>
     *
     *@param  table     Table name
     *@param  synonym   Synonym name, if requried. If it is not required, pass a
     *      <code>null</code>.
     *@param  field     Field name in table
     *@param  operator  Description of the Parameter
     *@param  value     Description of the Parameter
     *@return           A string suitable for a HTML tag in a form.
     */
    public static String buildValue( String table, String synonym, String field, String operator, String value )
    {
        StringBuffer ret = new StringBuffer();
        ret.append( FormTags.TABLE_TAG );
        ret.append( FormTags.NAMEVALUE_TOKEN_STR );
        ret.append( table );
        if ( synonym != null )
        {
            ret.append( FormTags.TOKENIZER_STR );
            ret.append( FormTags.SYN_TAG );
            ret.append( FormTags.NAMEVALUE_TOKEN_STR );
            ret.append( synonym );
        }
        ret.append( FormTags.TOKENIZER_STR );
        ret.append( FormTags.FIELD_TAG );
        ret.append( FormTags.NAMEVALUE_TOKEN_STR );
        ret.append( field );
        ret.append( FormTags.TOKENIZER_STR );
        ret.append( FormTags.OPERATOR_TAG );
        ret.append( FormTags.NAMEVALUE_TOKEN_STR );
        ret.append( operator );
        ret.append( FormTags.TOKENIZER_STR );
        ret.append( FormTags.VALUE_TAG );
        ret.append( FormTags.NAMEVALUE_TOKEN_STR );
        ret.append( value );
        return ret.toString();
    }


    /**
     *  Produces the HTML for inclusion on the second step of form building. The
     *  HTML returned should be self-supporting - i.e., only the guts of a
     *  &lt;div&gt; or a &lt;td&gt; tag. It should not be part of a larger table
     *  structure. It is used in building the second page of the form buliding
     *  process.
     *
     *@param  num  Which iteration this is. This should be used to create unique
     *      tag names
     *@param  md   Description of the Parameter
     *@return      The HTML for inclusion in the second form building page.
     */
    public String getFBHtml( DBMetaData md, int num, int thread)
    {
        /*
         *  prebuild the drop down lists
         *  they'll be modified later for the specific drop downs
         */
        StringBuffer list = FBUtils.buildTableFieldDropDown( md, "Filter", num );
        /*
         *  build the operator list
         */
        StringBuffer opList = FBUtils.getOperatorList( "Filter" + num, Config.EMPTY_STR);

        StringBuffer ret = new StringBuffer();
        ret.append( FBUtils.TABLE_START );
        ret.append( FBUtils.ROW_START );
        ret.append( "<td><strong>Title:</strong></td>" );
        ret.append( "<td><input name=\"Filter" );
        ret.append( num );
        ret.append( FormTags.TOKENIZER_STR + FormTags.LABEL_TAG + "\" type=\"text\" size=\"30\"></td>" );
        ret.append( FBUtils.ROW_END );

        ret.append( FBUtils.ROW_START );
        ret.append( "<td><strong>Field:</strong></td>" );
        ret.append( "<td>" );
        ret.append( list.toString() );
        ret.append( "</td>" );
        ret.append( "<td><strong>Operator:</strong></td>" );
        ret.append( "<td>" );
        ret.append( opList.toString() );
        ret.append( "</td>" );

        ret.append( FBUtils.ROW_END );
        ret.append( FBUtils.ROW_START );
        ret.append( "<td><strong>Values:</strong></td>" );
        ret.append( "<td colspan=\"4\"><input name=\"Filter" );
        ret.append( num );
        ret.append( "\" type=\"text\" size=\"22\"> (Comma delmited list. No spaces you don't want in the values.)</td>" );

        ret.append( FBUtils.ROW_END );
        ret.append( FBUtils.TABLE_END );
        return ret.toString();
    }


    /**
     *  Builds the Node to be added to the root node of an XML Document.<br>
     *  <br>
     *  OrFilter should be of the form:<pre>
     *&lt;filter&gt;
     *&lt;table&gt;gradient_orientation&lt;/table&gt;
     *&lt;column&gt;x&lt;/column&gt;
     *&lt;label&gt;X&lt;/label&gt;
     *&lt;operator&gt;=&lt;/operator&gt;
     *&lt;value&gt;1&lt;/value&gt;
     *&lt;value&gt;2&lt;/value&gt;
     *&lt;value&gt;3&lt;/value&gt;
     *&lt;value&gt;4&lt;/value&gt;
     *&lt;value&gt;5&lt;/value&gt;
     *&lt;/filter&gt;
     *</pre> the table, field, and operator values are populated by the
     *  SqlElements superclass
     *
     *@param  document  Description of the Parameter
     *@return           The fBNode value
     *@see              SqlElements
     */
    public Node getFBNode( Document document )
    {
        String value = nameValues.getValue( FormTags.VALUE_TAG )[0];
        String title = nameValues.getValue( FormTags.LABEL_TAG )[0];
        //String operator = nameValues.getValue( FormTags.OPERATOR_TAG )[0];
        Element ret = document.createElement( "filter" );
        if ( table.equals( Config.EMPTY_STR ) ||
                field.equals( Config.EMPTY_STR ) ||
                title.equals( Config.EMPTY_STR ) ||
                value.equals( Config.EMPTY_STR ) )
        {
            return null;
        }

        FBUtils.addTextNode( document, ret, "table", table );
        FBUtils.addTextNode( document, ret, "column", field );
        FBUtils.addTextNode( document, ret, "label", title );
        FBUtils.addTextNode( document, ret, "operator", operator );

        String[] values = value.split( "," );
        for ( int i = 0; i < values.length; i++ )
        {
            FBUtils.addTextNode( document, ret, "value", values[i] );
        }

        return ret;
    }


    /**
     *  Description of the Method
     *
     *@param  currentFromList     Description of Parameter
     *@return                     Description of the Returned Value
     *@exception  MraldException  Description of Exception
     *@since
     */
    @Override
    public ArrayList<String> buildFrom( ArrayList<String> currentFromList )
        throws MraldException
    {
        /*
         *  don't add any tables to this list than we have to
         */
        if ( nameValues.getValue( FormTags.VALUE_TAG )[0].equals( "" ) || field.equals( "" ) || operator.equals( "" ) )
        {
            return currentFromList;
        }
        if ( !fromTableName.equals( Config.EMPTY_STR ) && !currentFromList.contains( fromTableName ) )
        {
            currentFromList.add( fromTableName );
        }
        return currentFromList;
    }


    /**
     *  Doesn't do anything - simply returns the passed ArrayList
     *
     *@param  currentGroupByList  List of Group By parameters
     *@return                     Description of the Returned Value
     *@exception  MraldException  Description of Exception
     *@since
     */
    public ArrayList<String> buildGroupBy( ArrayList<String> currentGroupByList )
        throws MraldException
    {
        return currentGroupByList;
    }


    /**
     *  Doesn't do anything - simply returns the passed ArrayList
     *
     *@param  currentOrderBy      Description of Parameter
     *@return                     Description of the Returned Value
     *@exception  MraldException  Description of Exception
     *@since
     */
    public ArrayList<String> buildOrderBy( ArrayList<String> currentOrderBy )
        throws MraldException
    {
        return currentOrderBy;
    }


    /**
     *  Doesn't do anything - simply returns the passed ArrayList
     *
     *@param  currentSelectList   Description of Parameter
     *@return                     Description of the Returned Value
     *@exception  MraldException  Description of Exception
     *@since
     */
    public ArrayList<String> buildSelect( ArrayList<String> currentSelectList )
        throws MraldException
    {
        return currentSelectList;
    }


    /**
     *  This method is the 'special case' It will cycle through all the values
     *  with the same tag, with the assumption that this is to have an 'OR' tag
     *  The assumption is made that this will always be for the same field and
     *  table, and will always have the same operator. Assumption - OR's can
     *  only span the same field in the database e.g ALLOWED: SELECT * FROM BLAH
     *  WHERE (BLAH.TYPE = 'O' OR BLAH.TYPE = 'P') NOT ALLOWED: SELECT * FROM
     *  BLAH WHERE (BLAH.TYPE = 'O' OR BLAH.ID = '10')
     *
     *@param  currentWhereList    Description of Parameter
     *@return                     Updated List of where clauses input by user
     *@exception  MraldException  Exception
     *@since
     */
    public ArrayList<String> buildWhere( ArrayList<String> currentWhereList )
        throws MraldException
    {
//        String value = null;
        noOfValues = ( nameValues.getValue( FormTags.VALUE_TAG ) ).length;
        noOfOperators = ( nameValues.getValue( FormTags.OPERATOR_TAG ) ).length;

        if ( field.equals( Config.EMPTY_STR ) || table.equals( Config.EMPTY_STR ) )
        {
            //if no field, do not filter
            return currentWhereList;
        }
        setType();
        StringBuffer newValue = new StringBuffer();
        buildWhereString( newValue );
        String returnString = newValue.toString();
        if ( !returnString.equals( Config.EMPTY_STR ) && !currentWhereList.contains( returnString ) )
        {
            currentWhereList.add( " ( " + returnString + " )" );
        }
        return currentWhereList;
    }


    /**
     *  For this element, in it's role as a FormBuilderElement, collect the
     *  Label and main Elements and make sure they all get processed together by
     *  grabbing the Label version or by adding the label to the main version
     *  still in the MsgObject.
     *
     *@param  msg          Description of the Parameter
     *@param  currentName  Description of the Parameter
     *@return              Description of the Return Value
     *@since
     */
    public String preProcess( MsgObject msg, String currentName )
    {
        String[] groupTags = {"label"};
        collectElementGroup( msg, currentName, groupTags );
        return currentName;
    }


    /**
     *  This element is invalid if there are no Values associated with it.
     *  Therefore in the postprocessing we check to make sure we have a valid
     *  Value. If there isn't one, set this element's isActive to false.
     *  (unless, of course, the operator is IS NULL or IS NOT NULL)
     *
     *@param  msg          Description of the Parameter
     *@param  currentName  Description of the Parameter
     *@return              Description of the Return Value
     *@since
     */
    public String postProcess( MsgObject msg, String currentName )
    {
        String[] values = nameValues.getValue( FormTags.VALUE_TAG );
        if ( values.length == 1 &&
                values[0].equals( Config.EMPTY_STR ) &&
                operator.toUpperCase().indexOf( "NULL" ) == -1 )
        {

            isActive = false;
        }
        return currentName;
    }


    /**
     *  Sets the type attribute of the FilterElement object
     */
    protected void setType()
    {
        type = nameValues.getValue( FormTags.TYPE_TAG )[0];
    }


    /**
     *  Description of the Method
     *
     *@param  ret  Description of the Parameter
     */
    protected void buildWhereString( StringBuffer ret )
    {
        if ( ( operator.toUpperCase().indexOf( "NULL" ) ) != -1 )
        {
            buildWhereStringNull( ret );
            return;
        }
        else if ( operator.toUpperCase().equals( "IN" ) || operator.toUpperCase().equals( "NOT IN" ) )
        {
            buildWhereStringIn( ret );
            return;
        }
        else if ( noOfValues > 2 && ( operator.equals("=") || operator.equals( "!=" ) ) )
        {
            buildConvertToInListString( ret );
        }
        else
        {
            buildWhereStringNormal( ret );
        }
    }


    /**
     *  Description of the Method
     *
     *@param  ret  Description of the Parameter
     */
    protected void buildWhereStringIn( StringBuffer ret )
    {
//	    StringBuffer retVal = new StringBuffer();
        for ( int i = 0; i < noOfValues; i++ )
        {
            String value = nameValues.getValue( FormTags.VALUE_TAG )[i];
            if ( value.equals( "" ) )
            {
                continue;
            }

            ret.append( table );
            ret.append( "." );
            ret.append( field );
            ret.append( " " );
            ret.append( operator );
            ret.append( " ( " );
            ret.append(  value );
            ret.append( " )" );
            if ( i < noOfValues - 1 )
            {
                ret.append( " OR " );
            }
        }
    }


    /**
     *  The normal Where clause - table.field operator value
     *
     *@param  ret  Description of the Parameter
     */
    protected void buildWhereStringNormal( StringBuffer ret )
    {
        String opToAdd = operator;

        if (operator.equals( FormTags.STARTS_WITH_TAG ) )
        {
        	opToAdd =FormTags.LIKE_TAG;
        }
        if (operator.equals( FormTags.NOT_STARTS_WITH_TAG ) )
        {
        	opToAdd =FormTags.NOT_LIKE_TAG ;
        }
        if ( operator.equals(FormTags.PG_CONTAINS_IGNORE_CASE) )
        {
            opToAdd = "~*";
        }
        for ( int i = 0; i < noOfValues; i++ )
        {
            String value = nameValues.getValue( FormTags.VALUE_TAG )[i];
            value = MiscUtils.checkApostrophe( value );

            if ( value.equals( Config.EMPTY_STR ) )
            {
                continue;
            }
            else if ( operator.equals( FormTags.LIKE_TAG ) )
            {
                value = "%" + value + "%";
            }
            else if ( operator.equals( FormTags.NOT_LIKE_TAG ) )
            {
                value = "%" + value + "%";
            }
            else if ( operator.equals( FormTags.STARTS_WITH_TAG ) )
            {
                value = value + "%";
            }
            else if ( operator.equals( FormTags.NOT_STARTS_WITH_TAG ) )
            {
                value = value + "%";
            }
            ret.append( table );
            ret.append( "." );
            ret.append( field );
            ret.append( " " );
            ret.append( opToAdd );
            if ( type.equals( "String" ) || type.equals( "Date" ) || type.equals( "" ) )
            {
                ret.append( " '" );
                ret.append( value );
                ret.append( "'" );
            }
            else
            {
                ret.append( " " );
                ret.append( value );
            }
            if ( i < noOfValues - 1 )
            {
                ret.append( " OR " );
            }
        }
    }

    /**
     *  Description of the Method
     *
     *@param  ret  Description of the Parameter
     */
    protected void buildConvertToInListString( StringBuffer ret )
    {
        String opToAdd;
        char QUOTE = '\'';
        String SEPARATOR= ", ";
        if ( operator.equals( "=" ) )
        {
        	opToAdd = " IN ";
        }
        else if ( operator.equals( "!=" ) )
        {
        	opToAdd = " NOT IN ";
        }
        else
            throw new RuntimeException("shouldn't be in here, then");

        /* Add the 'table.field in ( ' */
        ret.append( table );
        ret.append( "." );
        ret.append( field );
        ret.append( opToAdd );
        ret.append( "( " );
        for ( int i = 0; i < noOfValues; i++ )
        {
            String value = nameValues.getValue( FormTags.VALUE_TAG )[i];
            value = MiscUtils.checkApostrophe( value );

            if ( value.equals( Config.EMPTY_STR ) )
            {
                continue;
            }
            if ( type.equals( "String" ) || type.equals( "Date" ) || type.equals( "" ) )
            {
                ret.append( QUOTE );
                ret.append( value );
                ret.append( QUOTE );
            }
            else
            {
                ret.append( value );
            }
            if ( i < noOfValues - 1 )
            {
                ret.append( SEPARATOR );
            }
        }
        ret.append( " )" );
    }




    /**
     *  Description of the Method
     *
     *@param  ret  Description of the Parameter
     */
    protected void buildWhereStringNull( StringBuffer ret )
    {
        ret.append( table );
        ret.append( "." );
        ret.append( field );
        ret.append( " " );
        ret.append( operator );
    }
}

