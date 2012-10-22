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
package org.mitre.mrald.util;

import org.mitre.mrald.control.AbstractStep;
import org.mitre.mrald.control.MsgObject;

/**
 *  This class was created as a workflow step to take a pre-existing query and
 *  add partition limiting clauses to increase database performance.
 *
 *@author     JCHOYT
 *@created    August 21, 2001
 */
public class StateCheckStep extends AbstractStep
{

    /**
     *  Constructor for the StateCheckStep object. This constructor loads the
     *  properties file from permanent storage into memory.
     *
     *@since
     */
    public StateCheckStep() { }


    /**
     *  Entry point for the WorkflowStep interface. This method is the one
     *  called from the main control object. Assumes that the query String array
     *  has already been populated from previous steps in the workflow.
     *
     *@param  msgObject                                          MsgObject that
     *      gets passed from previous workflow steps
     *@exception  org.mitre.mrald.control.WorkflowStepException  Standard
     *      exception for a WorkflowStep
     *@since
     */
    public void execute( MsgObject msgObject )
        throws org.mitre.mrald.control.WorkflowStepException
    {
        MraldOutFile.appendToFile( msgObject.toString() );
    }
}

