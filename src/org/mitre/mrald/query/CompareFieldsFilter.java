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
 *  Used to create a filter that compares one field to another field.
 *  Code copied from OrFilter and modified.
 *
 *@author     pmork
 *@created    Sep. 18, 2006
 */
public class CompareFieldsFilter extends FilterElement implements FormBuilderElement
{
	public static final String FILTER_NAME = FormTags.COMPARE_FIELDS_FILTER;
	public static final String TAG_NAME = "compareFieldsFilter";

    /**
     */
    public CompareFieldsFilter()
    {
        super();
        elementType = FILTER_NAME;
    }


    /**
     *@param  msg  Description of Parameter
     */
    public CompareFieldsFilter( MsgObject msg )
    {
        super( msg );
        elementType = FILTER_NAME;
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
        StringBuffer list = FBUtils.buildTableFieldDropDown( md, FILTER_NAME, num );
        /*
         *  build the operator list
         */
        StringBuffer ret = new StringBuffer();
        ret.append( FBUtils.TABLE_START );

        ret.append( FBUtils.ROW_START );
        ret.append( "<td><strong>Title:</strong></td>" );
        ret.append( "<td><input name=\"" ).append( FILTER_NAME );
        ret.append( num );
        ret.append( FormTags.TOKENIZER_STR + FormTags.LABEL_TAG + "\" type=\"text\" size=\"30\"></td>" );
        ret.append( FBUtils.ROW_END );

        { // Curly-braces to help with code layout only.
	        ret.append( FBUtils.ROW_START );

	        ret.append( "<td><strong>First Field:</strong></td>" );
	        ret.append( "<td>" );
	        ret.append( list.toString().replaceAll( "Table" + FormTags.NAMEVALUE_TOKEN_STR , "Table1" + FormTags.NAMEVALUE_TOKEN_STR  ).replaceAll( "Field" + FormTags.NAMEVALUE_TOKEN_STR , "Field1" + FormTags.NAMEVALUE_TOKEN_STR  ) );
	        ret.append( "</td>" );

	        ret.append( "<td><strong>Operator:</strong></td>" );
	        ret.append( "<td>" );
	        ret.append( FBUtils.getSimpleOperatorList(FILTER_NAME + num, "" ) );
	        ret.append( "</td>" );

	        ret.append( "<td><strong>Second Field:</strong></td>" );
	        ret.append( "<td>" );
	        ret.append( list.toString().replaceAll( "Table" + FormTags.NAMEVALUE_TOKEN_STR , "Table2" + FormTags.NAMEVALUE_TOKEN_STR  ).replaceAll( "Field" + FormTags.NAMEVALUE_TOKEN_STR , "Field2" + FormTags.NAMEVALUE_TOKEN_STR  ) );
	        ret.append( "</td>" );

	        ret.append( FBUtils.ROW_END );
        }

        ret.append( FBUtils.TABLE_END );

        return ret.toString();
    }


    /**
     *  Builds the Node to be added to the root node of an XML Document.<br>
     *  <br>
     *  CompareFieldsFilter should be of the form:<code>
     *&lt;fieldcomparisonfilter&gt;
     *&lt;table1&gt;&lt;/table1&gt;
     *&lt;field1&gt;&lt;/field1&gt;
     *&lt;operator&gt;&lt;/operator&gt;
     *&lt;table2&gt;&lt;/table2&gt;
     *&lt;field2&gt;&lt;/field2&gt;
     *&lt;/fieldcomparisonfilter&gt;
     *</code>
     *
     *@param  document  Description of the Parameter
     *@return           The fBNode value
     */
    public Node getFBNode( Document document )
    {
        String field1 = nameValues.getValue( FormTags.FIELD_TAG + "1" )[0];
        String table1 = nameValues.getValue( FormTags.TABLE_TAG + "1" )[0];
        String operator = nameValues.getValue( FormTags.OPERATOR_TAG )[0];
        String field2 = nameValues.getValue( FormTags.FIELD_TAG + "2" )[0];
        String table2 = nameValues.getValue( FormTags.TABLE_TAG + "2" )[0];
        String label = nameValues.getValue( FormTags.LABEL_TAG )[0];
        if ( table1.equals( Config.EMPTY_STR ) ||
                field1.equals( Config.EMPTY_STR ) ||
                label.equals( Config.EMPTY_STR ) ||
                table2.equals( Config.EMPTY_STR ) ||
                field2.equals( Config.EMPTY_STR ) )
        {
            //            System.out.println( "returning NULL" );
            return null;
        }

        Element ret = document.createElement( TAG_NAME );
        FBUtils.addTextNode( document, ret, "table1", table1 );
        FBUtils.addTextNode( document, ret, "column1", field1 );
        FBUtils.addTextNode( document, ret, "operator", operator );
        FBUtils.addTextNode( document, ret, "table2", table2 );
        FBUtils.addTextNode( document, ret, "column2", field2 );
        FBUtils.addTextNode( document, ret, "label", label );
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
    @Override
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
     *  This method compares field one to field two.
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
        String operator = null;

        String table1 = nameValues.getValue( "Table1" )[0].toString();
        String table2 = nameValues.getValue( "Table2" )[0].toString();

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
            /* MraldOutFile.logToFile( Config.getProperty("LOGFILE"), "One of the table or field values in one of the FieldComparisonFilterElements "+
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
        operator = nameValues.getValue( "Operator" )[0];
        if ( operator.equals( Config.EMPTY_STR ) )
        {
            operator = "=";
        }

        String where = table1 + "." + field1 + operator + table2 + "." + field2;
        currentWhereList.add(where);

        return currentWhereList;
    }

    public String postProcess( MsgObject msg, String currentName ) {
    	if (nameValues.getNames().contains("LookForCheck")) {
    		String check = nameValues.getValue("checked")[0];
    		if (check == null || check.length() == 0) {
        		isActive = false;
    		}
    	}
    	return currentName;
    }
}