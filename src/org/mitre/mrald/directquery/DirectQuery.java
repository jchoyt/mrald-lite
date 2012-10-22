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
package org.mitre.mrald.directquery;

import org.mitre.mrald.control.AbstractStep;
import org.mitre.mrald.control.MsgObject;
import org.mitre.mrald.util.Config;
import org.mitre.mrald.util.MiscUtils;

/**
 *  The DirectQuery class provides the user the ability to construct their own
 *  queries and get a direct response from the Database. It is a part of the
 *  org.mitre.mrald.directquery package.
 *
 *@author     jchoyt
 *@created    January 10, 2001
 */
public class DirectQuery extends AbstractStep
{
    MsgObject msg;


    /**
     *  Pulls the query from the MsgObject, checks to verify the user is
     *  submitting a query, then sets the query in the MsgObject
     *
     *@param  _msg                                               Description of
     *      Parameter
     *@exception  org.mitre.mrald.control.WorkflowStepException  Description of
     *      Exception
     *@since
     */
    public void execute(MsgObject _msg)
        throws org.mitre.mrald.control.WorkflowStepException
    {
        msg = _msg;
        String query[] = msg.getValue("query");

        for (int i = 0; i < query.length; i++)
        {
            if (query[i].toLowerCase().startsWith("select ") || query[i].toLowerCase().startsWith("exec "))
            {
                msg.setQuery(MiscUtils.clearSemiColon(query[i]));
            }
            else
            {
                throw new org.mitre.mrald.control.WorkflowStepException(Config.NEWLINE + "You may only perform queries through this form." + Config.NEWLINE);
            }
        }
    }
}

