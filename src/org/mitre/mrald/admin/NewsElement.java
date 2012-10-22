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
package org.mitre.mrald.admin;

import java.util.StringTokenizer;

import org.mitre.mrald.control.MsgObject;
import org.mitre.mrald.formbuilder.FormBuilderElement;
import org.mitre.mrald.parser.ParserElement;
import org.mitre.mrald.util.Config;
import org.mitre.mrald.util.DBMetaData;
import org.mitre.mrald.util.FormTags;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

/**
 *  Description of the Class
 *
 *@author     tcornett
 *@created    June 1, 2004
 */
public class NewsElement extends ParserElement implements FormBuilderElement
{
    protected final String NEWS_ARCHIVE_TAG = "Archive";
    protected final String NEWS_ITEM_TAG = "NewsArticle";
    protected final String NEWS_DAY_TAG = "NewsDay";
    protected final String NEWS_TITLE_TAG = "Title";
    protected final String NEWS_CONTENT_TAG = "Content";
    protected final String NEWS_DATE_TAG = "Date";

    /**
     *  Constructor for the NewsElement object
     *
     *@since
     */

    public NewsElement()
    {
    }

    /**
     *  Gets the elementType of the ParserElement-derived object
     *
     *@return    The elementType value
     */
    public String getElementType()
    {
        return "Formbuilder News Element";
    }


    /**
     *@param  num  Which iteration this is. This should be used to create unique
     *      tag names
     *@param  md   Description of the Parameter
     *@return      The HTML for inclusion in the second form building page.
     */
    public String getFBHtml( DBMetaData md, int num, int thread )
    {
        return "";
    }


    /**
     *  Builds the Node to be added to the root node of an XML Document.<br>
     *  <br>
     *@param  document  Description of the Parameter
     *@return           The fBNode value
     */
    public Node getFBNode( Document document )
    {
//        String empty = Config.EMPTY_STR;
	String itemNameLit = nameValues.getValue( "NewsItemLit" )[0];
	String titleValue = null;
        String contentValue = null;

        Element ret = document.createElement( "newsday" );
        ret.setAttribute( "date", nameValues.getValue( NEWS_DATE_TAG )[0] );
	ret.setAttribute( "archive", nameValues.getValue( NEWS_ARCHIVE_TAG )[0] );

	Integer stepCountInt = new Integer( nameValues.getValue( FormTags.COUNT_TAG)[0] );
	int stepCount = stepCountInt.intValue();

	stepout:for (int i= 0; i < stepCount; i++)
	{
	    String stepValue = itemNameLit + new Integer(i + 1).toString();

	    titleValue = nameValues.getValue( stepValue + FormTags.TOKENIZER_STR + NEWS_TITLE_TAG )[0];
	    contentValue = nameValues.getValue( stepValue + FormTags.TOKENIZER_STR + NEWS_CONTENT_TAG )[0];

	    Element newsitemElement = document.createElement( "newsitem" );
            Element titleElement = document.createElement( "title" );
	    Element contentElement = document.createElement( "body" );

            Text titleText = document.createTextNode( titleValue );
	    titleElement.appendChild( titleText );

            Text contentText = document.createTextNode( contentValue );
	    contentElement.appendChild( contentText );

	    newsitemElement.appendChild( titleElement);
	    newsitemElement.appendChild( contentElement);

            ret.appendChild( newsitemElement );
	}

        return ret;

    }

    /**
     *  Preprocess will group the objects according to the
     * workflow that they belong to
     * e.g. WfPath1, WfPath2 etc,,
     * The values will then be
     * Step1~WfObjectName
     * Step1~WfObjectOrder
     *
     *@param  msg          Description of the Parameter
     *@param  currentName  Description of the Parameter
     *@return              Description of the Return Value
     *@since
     */
    public String preProcess( MsgObject msg, String currentName )
    {
	StringTokenizer currentNameToken = new StringTokenizer(currentName ,FormTags.TOKENIZER_STR );
	boolean emptyDate = false;

	currentName = currentNameToken.nextToken();
	String stepName = currentName;

	if (currentNameToken.countTokens() > 2)
	{
		currentName = currentNameToken.nextToken();
	}

	//Need  to get the "WfStep" text
	stepName = stepName.substring(0, stepName.length() - 1);

        String empty = Config.EMPTY_STR;

        //Group the steps together into one WorkflowStepElement
	//e.g. WfPath1~WfStep1~ObjectName
	//	WfPath1~WfStep1~Order
	//	WfPath1~WfStep2~ObjectName
	//	WfPath1~WfStep2~Order
	// will all be grouped under WfPath1
	// The WfCount keeps track of how many Steps were taken in
	// a particular WfPath

	//First get the number of WorkflowSteps associated with this WfPath

	String newStr = currentName + FormTags.TOKENIZER_STR + FormTags.COUNT_TAG;
	String valueList = msg.getValue( newStr )[0];

	int stepCount = 0;

        if ( ! valueList.equals(empty) ) {
		stepCount = new Integer( valueList ).intValue();
		nameValues.setValue( FormTags.COUNT_TAG, valueList );
	}
	msg.removeValue( newStr );

	// Get the Date of the NewsDay#
	newStr = currentName + FormTags.TOKENIZER_STR + NEWS_DATE_TAG;
	valueList = msg.getValue( newStr )[0];
	if ( valueList == null || valueList.equals( empty ) ) {
		isActive = false;
		emptyDate = true;
		msg.removeValue( newStr );
		newStr = currentName + FormTags.TOKENIZER_STR + NEWS_ARCHIVE_TAG;
		msg.removeValue( newStr );
	} else {
		isActive = true;
		nameValues.setValue( NEWS_DATE_TAG,  valueList);
		msg.removeValue( newStr );

		// Get the Archive Status of the NewsDay#
		newStr = currentName + FormTags.TOKENIZER_STR + NEWS_ARCHIVE_TAG;
		valueList = msg.getValue( newStr )[0];
		if (valueList.equals( "true" ) )
			nameValues.setValue( NEWS_ARCHIVE_TAG,  "yes");
		else
			nameValues.setValue( NEWS_ARCHIVE_TAG,  "no");
		msg.removeValue( newStr );
	}

	//Set the literal string for the "News Article" . e.g. "newsitem"
	nameValues.setValue( "NewsItemLit",  "NewsArticle");

	// Grab everything for each one of the NewsArticles
	moveon:for (int i = 0; i < stepCount; i++)
	{
		String stepValue = "NewsArticle" + new Integer(i + 1).toString();

		if ( !emptyDate ) {
			newStr = currentName + FormTags.TOKENIZER_STR + stepValue + FormTags.TOKENIZER_STR + NEWS_TITLE_TAG;
			valueList = msg.getValue( newStr )[0].trim();
//			org.mitre.mrald.util.MraldOutFile.logToFile( org.mitre.mrald.util.Config.getProperty("LOGFILE") , "NewsElement: " + newStr + " : <" + valueList + ">");
			if ( valueList == null || valueList.equals( empty ) ) {
				newStr = currentName + FormTags.TOKENIZER_STR + stepValue + FormTags.TOKENIZER_STR + NEWS_TITLE_TAG;
				msg.removeValue( newStr );

				newStr = currentName + FormTags.TOKENIZER_STR + stepValue + FormTags.TOKENIZER_STR + NEWS_CONTENT_TAG;
				msg.removeValue( newStr );

				int temp = new Integer( nameValues.getValue( FormTags.COUNT_TAG )[0] ).intValue();
				nameValues.removeValue( FormTags.COUNT_TAG );
				temp = temp - 1;
				nameValues.setValue( FormTags.COUNT_TAG, temp + "" );
			} else {
				nameValues.setValue( stepValue + FormTags.TOKENIZER_STR + NEWS_TITLE_TAG, valueList );
				msg.removeValue( newStr );

				newStr = currentName + FormTags.TOKENIZER_STR + stepValue + FormTags.TOKENIZER_STR + NEWS_CONTENT_TAG;
				valueList = msg.getValue( newStr )[0].trim();
				nameValues.setValue( stepValue + FormTags.TOKENIZER_STR + NEWS_CONTENT_TAG, valueList );
				msg.removeValue( newStr );
			}

		} else {
			newStr = currentName + FormTags.TOKENIZER_STR + stepValue + FormTags.TOKENIZER_STR + NEWS_TITLE_TAG;
			msg.removeValue( newStr );

			newStr = currentName + FormTags.TOKENIZER_STR + stepValue + FormTags.TOKENIZER_STR + NEWS_CONTENT_TAG;
			msg.removeValue( newStr );
		}
	}

        return currentName;

    }


}

