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
package org.mitre.mrald.formbuilder;


import org.mitre.mrald.control.MsgObject;
import org.mitre.mrald.query.FilterElement;
import org.mitre.mrald.query.SqlElements;
import org.mitre.mrald.util.Config;
import org.mitre.mrald.util.DBMetaData;
import org.mitre.mrald.util.FBUtils;
import org.mitre.mrald.util.FormTags;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *  A categorical filter is one where the values are from a small, known set.
 *  This filter will grab the values from a database and present them to the
 *  user in the form of a checkbox (or other representation according to the
 *  stylesheet). This is much more convenient than forcing the users to know the
 *  range of valid values. 90% of the funcationlity is identical to that of the
 *  FilterElement - this class is needed for integration into the formbuilder.
 *
 *@author     jchoyt
 *@created    December 25, 2004
 */
public class CategoricalFilterElement extends FilterElement
{


    /**
     *  Constructor for the QueryElements object
     *
     *@since
     */
    public CategoricalFilterElement()
    {
        super();
        elementType = "CATEGORICAL-FILTER";
    }


    /**
     *  Constructor for the QueryElements object
     *
     *@param  msg  Description of Parameter
     *@since
     */
    public CategoricalFilterElement( MsgObject msg )
    {
        super( msg );
        elementType = "FILTER";
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
        StringBuffer list = FBUtils.buildTableFieldDropDown( md, "CategoricalFilter", num );
        StringBuffer opList = FBUtils.getOperatorList( "CategoricalFilter" + num, Config.EMPTY_STR );

        StringBuffer ret = new StringBuffer();
        ret.append( "<p class=\"filter\">" );
        ret.append( "<strong>Title:</strong>" );
        ret.append( "<input name=\"CategoricalFilter" + num + FormTags.TOKENIZER_STR + FormTags.LABEL_TAG + "\" type=\"text\" size=\"3" + num + "\" />" );
        ret.append( "<br />" );
        ret.append( "<br />" );
        ret.append( "<strong>Filter on:</strong>" );
        ret.append( "<br />" );
        ret.append( "Field:" );
        ret.append( list );
        ret.append( "<img alt=\"\" src=\"images/spacer.gif\" width=\"25\" height=\"1\"/>" );
        ret.append( "Operator:" );
        ret.append( opList );
        ret.append( "<br />" );
        ret.append( "<br />" );
        ret.append( "<strong>Values from:</strong>" );
        ret.append( "<br />" );
        ret.append( "Table: <input name=\"CategoricalFilter" + num + FormTags.TOKENIZER_STR + "CategoryTable\" type=\"text\" size=\"22\" /><img alt=\"\" src=\"images/spacer.gif\" width=\"33\" height=\"1\"/>" );
        ret.append( "Field: <input name=\"CategoricalFilter" + num + FormTags.TOKENIZER_STR + "CategoryField\" type=\"text\" size=\"22\" /><br />" );
        ret.append( "Label: <input name=\"CategoricalFilter" + num + FormTags.TOKENIZER_STR + "DropDownLabel\" type=\"text\" size=\"22\" /><img alt=\"\" src=\"images/spacer.gif\" width=\"33\" height=\"1\"/>" );
        ret.append( "<a href=\"displayDB.jsp\" target=\"_blank\">Show me a list of all tables and columns</a>" );
        ret.append( "</p>" );
        return ret.toString();
    }


    /**
     *  Builds the Node to be added to the root node of an XML Document.<br>
     *  <br>
     *  CategoricalFilter should be of the form:<pre>
     *&lt;categoricalfilter&gt;
     *&lt;table&gt;&lt;/table&gt;
     *&lt;column&gt;x&lt;/column&gt;
     *&lt;label&gt;&lt;/label&gt;
     *&lt;operator&gt;&lt;/operator&gt;
     *&lt;category-table&gt;&lt;/category-table&gt;
     *&lt;category-field&gt;&lt;/category-field&gt;
     *&lt;list-column&gt;one or more&lt;/list-column&gt;
     *&lt;default-value&gt;optional&lt;/default-value&gt;
     *&lt;/categoricalfilter&gt;
     *</pre> the table, field, and operator values are populated by the
     *  SqlElements superclass
     *
     *@param  document  Description of the Parameter
     *@return           The fBNode value
     *@see              SqlElements
     */
    public Node getFBNode( Document document )
    {
        String[] labels = nameValues.getValue( "DropDownLabel" );
        String title = nameValues.getValue( FormTags.LABEL_TAG )[0];
        Element ret = document.createElement( "categoricalfilter" );





        if ( table.equals( Config.EMPTY_STR ) ||
                field.equals( Config.EMPTY_STR ) ||
                title.equals( Config.EMPTY_STR ) ||
                labels[0].equals( Config.EMPTY_STR ) )
        {

            return null;
        }
        FBUtils.addTextNode( document, ret, "table", table );
        FBUtils.addTextNode( document, ret, "column", field );
        FBUtils.addTextNode( document, ret, "label", title );
        FBUtils.addTextNode( document, ret, "operator", operator );

        String catTable = nameValues.getValue( "CategoryTable" )[0];

        if ( catTable.equals( Config.EMPTY_STR ) )
        {
            catTable = table;
        }
        FBUtils.addTextNode( document, ret, "category-table", catTable );

        String catField = nameValues.getValue( "CategoryField" )[0];

        if ( catField.equals( Config.EMPTY_STR ) )
        {
            catField = table;
        }
        FBUtils.addTextNode( document, ret, "category-field", catField );

        String defaultValue = nameValues.getValue( "DefaultValue" )[0];

        if ( !defaultValue.equals( Config.EMPTY_STR ) )
        {
            FBUtils.addTextNode( document, ret, "default-value", defaultValue );
        }

        for ( int i = 0; i < labels.length; i++ )
        {
            FBUtils.addTextNode( document, ret, "list-column", labels[i] );
        }

        return ret;
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

        String[] groupTags = {FormTags.LABEL_TAG, "CategoryTable", "CategoryField", "DropDownLabel"};
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
/*         String[] values = nameValues.getValue( FormTags.VALUE_TAG );
        if ( values.length == 1 &&
                values[0].equals( Config.EMPTY_STR ) &&
                operator.toUpperCase().indexOf( "NULL" ) == -1 )
        {

            isActive = false;
        }
 */        return currentName;
    }
}
