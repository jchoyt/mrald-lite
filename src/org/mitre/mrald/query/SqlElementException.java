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

import org.mitre.mrald.parser.MraldParserException;

/******************************************************************************
 * The QueryElementsException class handles exceptions that are unique to the
 * QueryElements class.  The QueryElementsException class is a part of the org.mitre.mrald.CAASDUtilities
 * package.  The following code demonstrates the use of this
 * class:
 * <PRE>
 * QueryElementsException QueryElementsException = new QueryElementsException("This is an error message.");
 * throw o;
 * </PRE>
 *
 * @author Gail Hamilton
 *****************************************************************************/

public class SqlElementException extends MraldParserException
{
    public SqlElementException(String sMessage)
    {
        super(sMessage);
    }
}
