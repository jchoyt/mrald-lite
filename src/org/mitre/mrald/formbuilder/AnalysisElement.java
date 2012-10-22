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

import org.mitre.mrald.parser.ParserElement;
import org.mitre.mrald.util.DBMetaData;
import org.mitre.mrald.util.FBUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class AnalysisElement extends ParserElement implements FormBuilderElement
{
    public AnalysisElement() { }

    public String getElementType()
    {
        return "Formbuilder Analysis Element";
    }

    public String getFBHtml( DBMetaData md, int num, int thread )
    {
        StringBuffer ret = new StringBuffer();

        ret.append( FBUtils.TABLE_START );
        ret.append( FBUtils.ROW_START );

        ret.append( "<td width=\"15%\" align=\"center\"><input name=\"FBAnalysis\" type=\"checkbox\" value=\"IncludeAnalysis\"></td>\n" );
        ret.append( "<td width=\"85%\">Add Analysis As An Output Option</td>\n" );

        ret.append( FBUtils.ROW_END );
        ret.append( FBUtils.TABLE_END );

        return ret.toString();
    }

    public Node getFBNode( Document document )
    {
        String algorithmName = nameValues.getValue( "Value" )[0];

        if ( algorithmName.equals( "None" ) || algorithmName.equals( "" ) )
        {
            return null;
        }

		//
		// Since we only to know whether or not the user WANTS analysis output as
		// an option, we dont need any more than simply the analysis node in the XML
		//
        Element ret = document.createElement( "analysis" );

        return ret;
    }

}

