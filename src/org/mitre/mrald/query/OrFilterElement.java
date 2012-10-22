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
import org.mitre.mrald.util.MraldException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *  Used to create a pair of WHERE clauses separated by and 'OR' instead of an
 *  'AND'. Tags are of the form <code>Table1:ETMSCOMMON~Syn1:ETMSCOMMON1~Field1:DEPTAIRPORT~Operator1:=~Value1:UAS~Table2:ETMSCOMMON~Syn2:ETMSCOMMON2~Field2:ARRAIRPORT~Operator2:=~Value2:UAS</code>
 *  If there is no Value1 or Value2 name/value pair, the default
 *  FormTags.VALUE_TAG will be used.
 *
 *@author     jchoyt
 *@created    June 24, 2002
 */
public class OrFilterElement extends FilterElement implements FormBuilderElement
{
//    private String filterOperator = " = ";


    /**
     */
    public OrFilterElement()
    {
        super();
        elementType = FormTags.ORFILTER_TAG;
    }


    /**
     *@param  msg  Description of Parameter
     */
    public OrFilterElement( MsgObject msg )
    {
        super( msg );
        elementType = FormTags.ORFILTER_TAG;
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
    public String getFBHtml( DBMetaData md, int num, int thread )
    {
        /*
         *  prebuild the drop down lists
         *  they'll be modified later for the specific drop downs
         */
        StringBuffer list = FBUtils.buildTableFieldDropDown( md, "OrFilter", num );
        /*
         *  build the operator list
         */
        StringBuffer ret = new StringBuffer();
        ret.append( FBUtils.TABLE_START );
        ret.append( FBUtils.ROW_START );
        ret.append( "<td><strong>Title:</strong></td>" );
        ret.append( "<td><input name=\"OrFilter" );
        ret.append( num );
        ret.append( FormTags.TOKENIZER_STR + FormTags.LABEL_TAG + "\" type=\"text\" size=\"30\"></td>" );
        ret.append( FBUtils.ROW_END );

        ret.append( FBUtils.ROW_START );
        ret.append( "<td><strong>First Field:</strong></td>" );
        ret.append( "<td>" );
        ret.append( list.toString().replaceAll( "Table:", "Table1:" ).replaceAll( "Field:", "Field1:" ) );
        ret.append( "</td>" );
        ret.append( "<td><strong>First Operator:</strong></td>" );
        ret.append( "<td>" );
        ret.append(  FBUtils.getOperatorList("OrFilter" + num, "1" ) );
        ret.append( "</td>" );
        ret.append( FBUtils.ROW_END );

        ret.append( FBUtils.ROW_START );
        ret.append( "<td><strong>Second Field:</strong></td>" );
        ret.append( "<td>" );
        ret.append( list.toString().replaceAll( "Table:", "Table2:" ).replaceAll( "Field:", "Field2:" ) );
        ret.append( "</td>" );

        ret.append( "<td><strong>Second Operator:</strong></td>" );
        ret.append( "<td>" );
        ret.append(  FBUtils.getOperatorList("OrFilter" + num, "2" ) );
        ret.append( "</td>" );
        ret.append( FBUtils.ROW_END );

        ret.append( FBUtils.ROW_START );
        ret.append( "<td><strong>Value:</strong></td>" );
        ret.append( "<td colspan=\"3\"><input name=\"OrFilter" );
        ret.append( num );
        ret.append( "\" type=\"text\" size=\"22\"></td>" );
        ret.append( FBUtils.ROW_END );
        ret.append( FBUtils.TABLE_END );

        return ret.toString();
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
        boolean value1Empty = nameValues.getValue( "Value1" )[0].equals(Config.EMPTY_STR);
        boolean value2Empty = nameValues.getValue( "Value2" )[0].equals(Config.EMPTY_STR);
        String op1 = nameValues.getValue( "Operator1" )[0];
        String op2 = nameValues.getValue( "Operator2" )[0];
        String[] values = nameValues.getValue( FormTags.VALUE_TAG );
        boolean valueEmpty = values.length == 1 && values[0].equals( Config.EMPTY_STR );
        /* check each value/op pair for emtpy value and operator is not IS NULL or IS NOT NULL */
        if( ( valueEmpty && value1Empty &&
            op1.toUpperCase().indexOf( "NULL" ) == -1 ) ||
            ( valueEmpty && value2Empty &&
            op2.toUpperCase().indexOf( "NULL" ) == -1 ) )
        {
            isActive = false;
        }

        return currentName;
    }

    /**
     *  Builds the Node to be added to the root node of an XML Document.<br>
     *  <br>
     *  OrFilter should be of the form:<code>
     *&lt;orfilter&gt;
     *&lt;table1&gt;&lt;/table1&gt;
     *&lt;field1&gt;&lt;/field1&gt;
     *&lt;operator1&gt;&lt;/operator1&gt;
     *&lt;table2&gt;&lt;/table2&gt;
     *&lt;field2&gt;&lt;/field2&gt;
     *&lt;operator2&gt;&lt;/operator2&gt;
     *&lt;value&gt;&lt;/value&gt;
     *&lt;/orfilter&gt;
     *</code>
     *
     *@param  document  Description of the Parameter
     *@return           The fBNode value
     */
    public Node getFBNode( Document document )
    {
        String field1 = nameValues.getValue( FormTags.FIELD_TAG + "1" )[0];
        String table1 = nameValues.getValue( FormTags.TABLE_TAG + "1" )[0];
        String operator1 = nameValues.getValue( FormTags.OPERATOR_TAG + "1" )[0];
        String field2 = nameValues.getValue( FormTags.FIELD_TAG + "2" )[0];
        String table2 = nameValues.getValue( FormTags.TABLE_TAG + "2" )[0];
        String operator2 = nameValues.getValue( FormTags.OPERATOR_TAG + "2" )[0];
        String value = nameValues.getValue( FormTags.VALUE_TAG )[0];
        String label = nameValues.getValue( FormTags.LABEL_TAG )[0];
        if ( table1.equals( Config.EMPTY_STR ) ||
                field1.equals( Config.EMPTY_STR ) ||
                label.equals( Config.EMPTY_STR ) ||
                table2.equals( Config.EMPTY_STR ) ||
                field2.equals( Config.EMPTY_STR ) ||
                value.equals( Config.EMPTY_STR ) )
        {
            //            System.out.println( "returning NULL" );
            return null;
        }

        Element ret = document.createElement( "orFilter" );
        FBUtils.addTextNode( document, ret, "table1", table1 );
        FBUtils.addTextNode( document, ret, "column1", field1 );
        FBUtils.addTextNode( document, ret, "operator1", operator1 );
        FBUtils.addTextNode( document, ret, "table2", table2 );
        FBUtils.addTextNode( document, ret, "column2", field2 );
        FBUtils.addTextNode( document, ret, "operator2", operator2 );
        FBUtils.addTextNode( document, ret, "label", label );
        String[] values = value.split( "," );
        for ( int i = 0; i < values.length; i++ )
        {
            FBUtils.addTextNode( document, ret, "value", values[i] );
        }
        //        System.out.println( "returning: " + ret );
        return ret;
    }


    /**
     *  Adds tables to the FROM clause in the query
     *
     *@param  currentFromList            Description of Parameter
     *@return                            Description of the Returned Value
     *@exception  MraldException  Description of Exception
     *@since
     */
    public ArrayList<String> buildFrom( ArrayList<String> currentFromList )
        throws MraldException
    {
        /*
         *  need to add table1 name to the list
         */
        String newValue = nameValues.getValue( FormTags.TABLE_TAG + "1" )[0];
        String synValue = nameValues.getValue( FormTags.SYN_TAG + "1" )[0];

        if ( !synValue.equals( Config.EMPTY_STR ) )
        {
            newValue = newValue + " " + synValue;
        }

        if ( !newValue.equals( Config.EMPTY_STR ) && !currentFromList.contains( newValue ) )
        {
            currentFromList.add( newValue );
        }

        /*
         *  need to add table2 name to the list
         */
        newValue = nameValues.getValue( FormTags.TABLE_TAG + "2" )[0];
        synValue = nameValues.getValue( FormTags.SYN_TAG + "2" )[0];

        if ( !synValue.equals( Config.EMPTY_STR ) )
        {
            newValue = newValue + " " + synValue;
        }

        if ( !newValue.equals( Config.EMPTY_STR ) && !currentFromList.contains( newValue ) )
        {
            currentFromList.add( newValue );
        }

        return currentFromList;
    }


    /**
     *  This method is the 'special case' and picks up where the normal
     *  FilterElement left off, putting an OR between values of two different
     *  table/field pairs. e.g. NOW ALLOWED: SELECT * FROM BLAH WHERE (BLAH.TYPE
     *  = 'O' OR BLAH.ID = '10')
     *
     *@param  currentWhereList           Description of Parameter
     *@return                            Updated List of where clauses input by
     *      user
     *@exception  MraldException  Exception
     *@since
     */
    public ArrayList<String> buildWhere( ArrayList<String> currentWhereList )
        throws MraldException
    {
        String newValue = Config.EMPTY_STR;
        String operator = null;
        String value = null;

        String table1 = nameValues.getValue( "Table1" )[0].toString();
        String table2 = nameValues.getValue( "Table2" )[0].toString();

        //Get whether the link between the statements is AND or OR - a bit misleading given the name of
        //this element. BUt this element can also be used for example in the case of minVal < x < maxValue
        String linkType = nameValues.getValue("LinkType")[0].toString();
        if (linkType.equals( Config.EMPTY_STR ) )
        {
            linkType = "OR";
        }
        //If there are synomyns - use them instead in building the clause
        String synValue = nameValues.getValue( "Syn1" )[0].toString();
        if ( !synValue.equals( Config.EMPTY_STR ) )
        {
            table1 = synValue;
        }
        synValue = nameValues.getValue( "Syn2" )[0].toString();
        if ( !synValue.equals( Config.EMPTY_STR ) )
        {
            table2 = synValue;
        }
        String field1 = nameValues.getValue( "Field1" )[0].toString();
        String field2 = nameValues.getValue( "Field2" )[0].toString();
        if ( field1.equals( Config.EMPTY_STR ) ||
                field2.equals( Config.EMPTY_STR ) ||
                table1.equals( Config.EMPTY_STR ) ||
                table2.equals( Config.EMPTY_STR )
                 )
        {//if incomplete, do not filter
            /* MraldOutFile.logToFile( Config.getProperty("LOGFILE"), "One of the table or field values in one of the OrFilterElements "+
                "is empty!! Not adding a filter to the WHERE clause"+
                "\n\tTable1="+table1 +
                "\n\tTable2="+table2 +
                "\n\tField1="+field1 +
                "\n\tField2="+field2 ); */
            return currentWhereList;
        }
        /*
         *  build the first half
         */
        operator = nameValues.getValue( "Operator1" )[0];
        /*
         *  If the Operator is "IS NULL" or "IS NOT NULL" do not need a value here
         */
//        int index = operator.toUpperCase().indexOf( "NULL" );
        if ( operator.toUpperCase().indexOf( "NULL" ) < 0 )
        {
            value = nameValues.getValue( "Value1" )[0];
            if ( value.equals( "" ) )
            {
                value = nameValues.getValue( FormTags.VALUE_TAG )[0];
            }
            if ( value.equals( "" ) )
            {//if incomplete, do not filter
                // MraldOutFile.logToFile( Config.getProperty("LOGFILE"), "Neither Value1 and Value were present in one of the OrFilterElements!! Not adding a filter to the WHERE clause");
                return currentWhereList;
            }
        }
        if ( operator.equals( Config.EMPTY_STR ) )
        {
            operator = "=";
        }
        else if ( operator.equals( FormTags.LIKE_TAG ) )
        {
            value = "%" + value + "%";
        }
        else if ( operator.equals( FormTags.STARTS_WITH_TAG ) )
        {
            operator = FormTags.LIKE_TAG;
            value = value + "%";
        }
        newValue = "( ";
        /*
         *  if operator has 'null' in it, assume you are looking for IS NULL or
         *  IS NOT NULL and build appropriate value, otherwise, use default
         */
        if ( operator.toUpperCase().indexOf( "NULL" ) != -1 )
        {
            newValue = newValue + table1 + "." + field1 + " " + operator;
        }
        else
        {
            newValue = newValue + table1 + "." + field1 + " " + operator + " '" + value + "'";
        }
        newValue += " " + linkType + " ";
        /*
         *  build the second half
         */
        operator = nameValues.getValue( "Operator2" )[0];
        /*
         *  If the Operator is "IS NULL" or "IS NOT NULL" do not need a value here
         */
//        index = operator.toUpperCase().indexOf( "NULL" );

        if ( operator.toUpperCase().indexOf( "NULL" ) < 0 )
        {
        	value = nameValues.getValue( "Value2" )[0];
        	if ( value.equals( "" ) )
        	{
        		value = nameValues.getValue( FormTags.VALUE_TAG )[0];
        	}
        	if ( value.equals( "" ) )
        	{//if incomplete, do not filter
        		return currentWhereList;
        	}
        }

        if ( operator.equals( Config.EMPTY_STR ) )
        {
            operator = "=";
        }
        else if ( operator.equals( FormTags.LIKE_TAG ) )
        {
            value = "%" + value + "%";
        }
        else if ( operator.equals( FormTags.STARTS_WITH_TAG ) )
        {
            operator = FormTags.LIKE_TAG;
            value = value + "%";
        }
        /*
         *  if operator has 'null' in it, assume you are looking for IS NULL or
         *  IS NOT NULL and build appropriate value, otherwise, use default
         */
        if ( operator.toUpperCase().indexOf( "NULL" ) != -1 )
        {
            newValue = newValue + table2 + "." + field2 + " " + operator;
        }
        else
        {
            newValue = newValue + table2 + "." + field2 + " " + operator + " '" + value + "'";
        }
        newValue += " )";
        if ( !currentWhereList.contains( newValue ) )
        {
            currentWhereList.add( newValue );
        }
        return currentWhereList;
    }
}

