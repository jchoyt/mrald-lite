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
package org.mitre.mrald.taglib;

import org.mitre.mrald.util.Config;

/**
 *  This object will be used to take the WorkFlow.xml document
 * and convert to an Html document via a Transform function and an
 * xsl Stylesheet.
 * This will allow the user to edit the Workflow itslef.
 *
 *@author     ghamilton
 *@created    October 10 2003
 */
public class WorkFlowAdminTag extends XslTransformTag
{
   /**
      *
     *  Constructor for the WorkFlowAdminTag object
     */
    public WorkFlowAdminTag()
    {
        super();
    //Remove the file:// that gets added from Config.getProperty
    String xmlTemp = Config.getProperty("XMLFILE").substring(7);

    setXsl(Config.getProperty("WFXSLFILE"));
    setXml(xmlTemp);
   }


}

