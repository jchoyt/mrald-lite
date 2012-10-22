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
package org.mitre.mrald.output;

public class WekaParameterOutput extends ParameterOutput
{

    public WekaParameterOutput()
	{
        super();
		dataFileDelimiter = ',';
	}

	protected String getDropDown( String classType, int instanceNum, int count, boolean canBeInt )
	{

		StringBuffer ret = new StringBuffer();

		if ( classType.equals( "BigDecimal" )  && canBeInt )
			ret.append( "<option value=\"Type:Numeric\">Numeric</option>\n" );

		if ( count <= maxCategories )
			ret.append( "<option value=\"Type:Nominal\">Nominal Specification (Class)</option>\n" );

		ret.append( "<option value=\"Type:String\">String</option>\n" );

		if ( classType.equals( "Timestamp" ) )
			ret.append( "<option value=\"Type:Timestamp~Format:" + formats[ instanceNum ] + "\">Timestamp " + formats[ instanceNum ] + "</option>\n" );

		return ret.toString();
	}

}
