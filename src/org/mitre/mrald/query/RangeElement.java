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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.StringTokenizer;


/**
 * The SqlElement class is the abstract class for all of the query elements
 * templates needed to populate a complete query string. This class contains
 * the common functionality for building query strings in general
 *
 * @author Gail Hamilton
 * @version 1.0
 *
 * @see java.lang.Object
 */
public class RangeElement extends SqlElements implements FormBuilderElement
{
/**
     *  Constructor for the QueryElements object
     *
     *@since
     */
    public RangeElement(  )
    {
        super(  );
        elementType = FormTags.RANGE_TAG;
    }

/**
     *  Constructor for the QueryElements object
     *
     *@param  msg  Description of the Parameter
     *@since
     */
    public RangeElement( MsgObject msg )
    {
        super( msg );
        elementType = FormTags.RANGE_TAG;
    }

    /**
     * Produces the HTML for inclusion on the second step of form
     * building. The HTML returned should be self-supporting - i.e., only the
     * guts of a &lt;div&gt; or a &lt;td&gt; tag. It should not be part of a
     * larger table structure. It is used in building the second page of the
     * form buliding process.
     *
     * @param md Description of the Parameter
     * @param num Which iteration this is. This should be used to create unique
     *        tag names
     *
     * @return The HTML for inclusion in the second form building page.
     */
    public String getFBHtml( DBMetaData md, int num, int thread)
    {
        StringBuffer ret = new StringBuffer(  );
        ret.append( "<strong>Field :</strong>" );
        ret.append( FBUtils.buildTableFieldDropDown( md, "Range", num ) );
        ret.append( "&nbsp; &nbsp; <strong>Desired Field Label :</strong>" );
        ret.append( "<input name=\"Range" );
        ret.append( num );
        ret.append( "\" type=\"text\" size=\"22\">" );

        return ret.toString(  );
    }

    /**
     * Builds the Node to be added to the root node of an XML Document.<br>
     * <br>
     * Range should be of the form:<pre> <range>  <table>   GRIDMORA </table>
     *  <field>LONGITUDE</field> <label>Longitude</label> </range> </pre>
     *
     * @param document Description of the Parameter
     *
     * @return The fBNode value
     */
    public Node getFBNode( Document document )
    {
        String table = nameValues.getValue( FormTags.TABLE_TAG )[0];
        String field = nameValues.getValue( FormTags.FIELD_TAG )[0];
        String label = nameValues.getValue( FormTags.VALUE_TAG )[0];

        if ( table.equals( Config.EMPTY_STR ) || field.equals( Config.EMPTY_STR ) || label.equals( Config.EMPTY_STR ) )
        {
            return null;
        }

        Element ret = document.createElement( "range" );

        Element tableElement = document.createElement( "table" );
        Text tableText = document.createTextNode( table );
        tableElement.appendChild( tableText );
        ret.appendChild( tableElement );

        Element fieldElement = document.createElement( "column" );
        Text fieldText = document.createTextNode( field );
        fieldElement.appendChild( fieldText );
        ret.appendChild( fieldElement );

        Element labelElement = document.createElement( "label" );
        Text labelText = document.createTextNode( label );
        labelElement.appendChild( labelText );
        ret.appendChild( labelElement );

        return ret;
    }

    /**
     * Description of the Method
     *
     * @param currentFromList Description of Parameter
     *
     * @return Description of the Returned Value
     *
     * @since
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
     * Description of the Method
     *
     * @param currentGroupByList List of Group By parameters
     *
     * @return Description of the Returned Value
     *
     * @exception MraldException Description of Exception
     *
     * @since
     */
    public ArrayList<String> buildGroupBy( ArrayList<String> currentGroupByList ) throws MraldException
    {
        return currentGroupByList;
    }

    /**
     * Description of the Method
     *
     * @param currentOrderBy Description of Parameter
     *
     * @return Description of the Returned Value
     *
     * @exception MraldException Description of Exception
     *
     * @since
     */
    public ArrayList<String> buildOrderBy( ArrayList<String> currentOrderBy ) throws MraldException
    {
        return currentOrderBy;
    }

    /**
     * Description of the Method
     *
     * @param currentSelectList Description of Parameter
     *
     * @return Description of the Returned Value
     *
     * @exception MraldException Description of Exception
     *
     * @since
     */
    public ArrayList<String> buildSelect( ArrayList<String> currentSelectList ) throws MraldException
    {
        return currentSelectList;
    }

    /**
     * Description of the Method
     *
     * @param currentWhereList Description of the Parameter
     *
     * @return Description of the Returned Value
     *
     * @since
     */
    public ArrayList<String> buildWhere( ArrayList<String> currentWhereList )
    {
        //Build the Where clause using items in the
        String newValue = " ";
        int noOfValues = nameValues.getValue( FormTags.VALUE_TAG ).length;

        if ( noOfValues == 0 )
        {
            return currentWhereList;
        }

        String table = nameValues.getValue( FormTags.TABLE_TAG )[0].toString(  );
        String field = nameValues.getValue( FormTags.FIELD_TAG )[0].toString(  );
        String synValue = nameValues.getValue( FormTags.SYN_TAG )[0];

        if ( !synValue.equals( Config.EMPTY_STR ) )
        {
            table = synValue;
        }

        String maxValue = nameValues.getValue( FormTags.MAXTAG )[0].toString(  );
        String minValue = nameValues.getValue( FormTags.MINTAG )[0].toString(  );
//        String thisOperator = null;

        if ( !minValue.equals( "" ) )
        {
            if ( !maxValue.equals( "" ) )
            {
                newValue = newValue + " ( " + table + "." + field + " >= \"" + minValue + "\" AND " + table + "." + field + " <= \"" + maxValue + "\" ) ";
            }
            else
            {
                newValue = table + "." + field + " >= \"" + minValue + "\"";
            }
        }
        else if ( !maxValue.equals( "" ) )
        {
            newValue = table + "." + field + " <= \"" + maxValue + "\"";
        }
        else
        {
            return currentWhereList;
        }

        if ( !currentWhereList.contains( newValue ) )
        {
            currentWhereList.add( newValue );
        }

        return currentWhereList;
    }

    /**
     * Description of the Method
     *
     * @param msg Description of Parameter
     * @param currentName DOCUMENT ME!
     *
     * @return Description of the Returned Value
     *
     * @since
     */

    /*
       public ArrayList buildWhereOr( ArrayList currentWhereOrList )
           throws MraldException
       {
           return currentWhereOrList;
       }
     */
    /**
     * Preprocessor - carriers out any additional processing required.
     *
     * @param msg Description of the Parameter
     * @param currentName Description of the Parameter
     *
     * @return Description of the Return Value
     *
     * @since
     */
    public String preProcess( MsgObject msg, String currentName )
    {
        //Get the Label infront of the ~
        //Filter1~StartTime - only want the Filter1 -then look for all
        //other items with Filter1 - add them to nvValues and remove form msgObjects
        //to make sure that duplicate objects do not get created.
        StringTokenizer currentNameToken = new StringTokenizer( currentName, FormTags.TOKENIZER_STR );

        //Set this item active
        currentName = currentNameToken.nextToken(  );

        //Just get the value itself
        //String valueList = msg.getValue(newStr)[0];
        String newStr = currentName + FormTags.TOKENIZER_STR + FormTags.MINTAG;
        String valueList = msg.getValue( newStr )[0];

        nameValues.setValue( FormTags.MINTAG, valueList );
        msg.removeValue( newStr );

        newStr = currentName + FormTags.TOKENIZER_STR + FormTags.MAXTAG;
        valueList = msg.getValue( newStr )[0];
        nameValues.setValue( FormTags.MAXTAG, valueList );
        msg.removeValue( newStr );

        return currentName;
    }
}

