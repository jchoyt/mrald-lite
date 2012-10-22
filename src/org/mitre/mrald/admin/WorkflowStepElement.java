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
 *@author     ghamilton
 *@created    October 10, 2003
 */
public class WorkflowStepElement extends ParserElement
	 implements FormBuilderElement
{
	/**
	 *  Constructor for the WorkFlowStepElement object
	 *
	 *@since
	 */

	public WorkflowStepElement() { }


	/**
	 *  Gets the elementType of the ParserElement-derived object
	 *
	 *@return    The elementType value
	 */
	public String getElementType()
	{
		return "Formbuilder Workflow Step Element";
	}


	/**
	 *@param  num  Which iteration this is. This should be used to create unique
	 *      tag names
	 *@param  md   Description of the Parameter
	 *@return      The HTML for inclusion in the second form building page.
	 */
	public String getFBHtml(DBMetaData md, int num, int thread)
	{
		return "";
	}


	/**
	 *  Builds the Node to be added to the root node of an XML Document.<br>
	 *  <br>
	 *
	 *
	 *@param  document  Description of the Parameter
	 *@return           The fBNode value
	 */
	public Node getFBNode(Document document)
	{
		String empty = Config.EMPTY_STR;
		String wfNameStr = nameValues.getValue("WfName")[0];
		String stepNameLit = nameValues.getValue("WfStepLit")[0];
		if (wfNameStr.equals("")) return null;

		String stepNameValue = null;
		String order = null;

		Element ret = document.createElement("WfPath");

		Element wfNameElement = document.createElement("WfName");
		Text wfName = document.createTextNode(wfNameStr);
		wfNameElement.appendChild(wfName);
		ret.appendChild(wfNameElement);

		Integer stepCountInt = new Integer(nameValues.getValue(FormTags.COUNT_TAG)[0]);
		int stepCount = stepCountInt.intValue();

		stepout :
		for (int i = 0; i < stepCount; i++)
		{
			String stepValue = stepNameLit + new Integer(i + 1).toString();

			stepNameValue = nameValues.getValue(stepValue + FormTags.TOKENIZER_STR + FormTags.OBJECT_NAME)[0];

			if (stepNameValue.equals(empty))
			{
				continue stepout;
			}
			order = nameValues.getValue(stepValue + FormTags.TOKENIZER_STR + FormTags.ORDER_TAG)[0];

			Element wfStepElement = document.createElement("WfObject");

			Element wfObjectElement = document.createElement("ObjectName");
			Element wfOrderElement = document.createElement("Order");

			Text wfStepText = document.createTextNode(stepNameValue);
			wfObjectElement.appendChild(wfStepText);

			Text wfOrderText = document.createTextNode(order);

			wfOrderElement.appendChild(wfOrderText);

			wfStepElement.appendChild(wfObjectElement);
			wfStepElement.appendChild(wfOrderElement);

			ret.appendChild(wfStepElement);
		}
		return ret;
	}


	/**
	 *  Preprocess will group the objects according to the workflow that they
	 *  belong to e.g. WfPath1, WfPath2 etc,, The values will then be
	 *  Step1~WfObjectName Step1~WfObjectOrder
	 *
	 *@param  msg          Description of the Parameter
	 *@param  currentName  Description of the Parameter
	 *@return              Description of the Return Value
	 *@since
	 */
	public String preProcess(MsgObject msg, String currentName)
	{
		StringTokenizer currentNameToken = new StringTokenizer(currentName, FormTags.TOKENIZER_STR);

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

		String valueList = msg.getValue(newStr)[0];
		int stepCount = 0;
		if (!valueList.equals(empty))
		{
			stepCount = new Integer(valueList).intValue();
			nameValues.setValue(newStr, valueList);
			msg.removeValue(newStr);
		}

		//Get the name of the WorkflowCount
		newStr = currentName + FormTags.TOKENIZER_STR + "WfName";
		valueList = msg.getValue(newStr)[0];

		nameValues.setValue("WfName", valueList);
		msg.removeValue(newStr);

		//Set the literal string for the "Work flow Step" . e.g. "WfStep"
		nameValues.setValue("WfStepLit", "WfObject");

		int removedNo = 1;
		moveon :
		for (int i = 0; i < stepCount; i++)
		{
			String stepValue = "WfObject" + new Integer(i + 1).toString();
			newStr = currentName + FormTags.TOKENIZER_STR + stepValue + FormTags.TOKENIZER_STR + "Ignore";

			//clean up
			if (msg.getValue(newStr)[0].equals("true"))
			{
				removedNo++;
				msg.removeValue(newStr);
				//remove the name node
				newStr = currentName + FormTags.TOKENIZER_STR + stepValue + FormTags.TOKENIZER_STR + FormTags.OBJECT_NAME;
				msg.removeValue(newStr);
				//remove the order node
				newStr = currentName + FormTags.TOKENIZER_STR + stepValue + FormTags.TOKENIZER_STR + FormTags.ORDER_TAG;
				msg.removeValue(newStr);
				continue moveon;
			}
			newStr = currentName + FormTags.TOKENIZER_STR + stepValue + FormTags.TOKENIZER_STR + FormTags.OBJECT_NAME;
			valueList = msg.getValue(newStr)[0];
			nameValues.setValue(stepValue + FormTags.TOKENIZER_STR + FormTags.OBJECT_NAME, valueList);
			msg.removeValue(newStr);

			newStr = currentName + FormTags.TOKENIZER_STR + stepValue + FormTags.TOKENIZER_STR + FormTags.ORDER_TAG;
			valueList = msg.getValue(newStr)[0];
			nameValues.setValue(stepValue + FormTags.TOKENIZER_STR + FormTags.ORDER_TAG, valueList);
			msg.removeValue(newStr);
		}

		nameValues.setValue(FormTags.COUNT_TAG, new Integer(stepCount).toString());
		if (!nameValues.getValue("WfName")[0].equals(Config.EMPTY_STR))
		{
			//If all the workflow objects have been removed, then remove the Elenet, and
			//hence the workflow path
			if (removedNo < stepCount)
			{
				isActive = true;
			}

			newStr = currentName + FormTags.TOKENIZER_STR + "Ignore";
			valueList = msg.getValue(newStr)[0];

			//If the Ignore checkbox is checked. Then do not adda.
			if (valueList.equals("true"))
			{
				isActive = false;
			}
			msg.removeValue(newStr);
		}
		return currentName;
	}

}

