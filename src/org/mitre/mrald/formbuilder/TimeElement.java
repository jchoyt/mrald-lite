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
import org.mitre.mrald.parser.ParserElement;
import org.mitre.mrald.util.DBMetaData;
import org.mitre.mrald.util.FBUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

/**
 *  Description of the Class
 *
 *@author     jchoyt
 *@created    September 16, 2003
 */
public class TimeElement extends ParserElement implements FormBuilderElement
{
    /**
     *  Constructor for the TimeElement object
     *
     *@since
     */
    public TimeElement() { }


    /**
     *  Gets the elementType of the ParserElement-derived object
     *
     *@return    The elementType value
     */
    public String getElementType()
    {
        return "Formbuilder Time Element";
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
        StringBuffer ret = new StringBuffer();

        ret.append( FBUtils.TABLE_START );
        ret.append( FBUtils.ROW_START );

        ret.append( "<TD WIDTH=\"24%\"><b>Start Date <i>(MM/DD/YYYY)</i></b></TD>\n" );
        ret.append( "<TD WIDTH=\"24%\"><INPUT NAME=\"FBTime1\" TYPE=\"text\" SIZE=\"25\"></TD>\n" );
        ret.append( "</TR>\n<TR>\n" );
        ret.append( "<TD WIDTH=\"24%\"><b>End Date <i>(MM/DD/YYYY)</i></b></TD>\n" );
        ret.append( "<TD WIDTH=\"24%\"><INPUT NAME=\"FBTime1~EndDate\" TYPE=\"text\" SIZE=\"25\"></TD>\n" );

        ret.append( FBUtils.ROW_END );
        ret.append( FBUtils.TABLE_END );

        return ret.toString();
    }


    /**
     *  Builds the Node to be added to the root node of an XML Document.<br>
     *  <br>
     *  Range should be of the form:<pre>
     *  <range>
     *
     *
     *  <table>
     *    GRIDMORA
     *  </table>
     *  <field>LONGITUDE</field> <label>Longitude</label> </range> </pre>
     *
     *@param  document  Description of the Parameter
     *@return           The fBNode value
     */
    public Node getFBNode( Document document )
    {
        String startDate = nameValues.getValue( "Value" )[0];
        String endDate = nameValues.getValue( "EndDate" )[0];

        if ( startDate.equals( "" ) || endDate.equals( "" ) )
        {
            return null;
        }

        Element ret = document.createElement( "time" );

        Element startElement = document.createElement( "default" );
        Text startText = document.createTextNode( startDate );
        startElement.appendChild( startText );
        ret.appendChild( startElement );

        Element endElement = document.createElement( "endBy" );
        Text endText = document.createTextNode( endDate );
        endElement.appendChild( endText );
        ret.appendChild( endElement );

        return ret;
    }


    /**
     *  For this element, in it's role as a FormBuilderElement, collect the
     *  EndDate and main Elements and make sure they all get processed together
     *  by.........................
     *
     *@param  msg          Description of the Parameter
     *@param  currentName  Description of the Parameter
     *@return              Description of the Return Value
     *@since
     */
    public String preProcess( MsgObject msg, String currentName )
    {
        String[] groupTags = {"EndDate"};
        collectElementGroup( msg, currentName, groupTags );
        return currentName;
    }

}

