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

import org.mitre.mrald.util.*;

/**
 *  The OutputControllerException class handles exceptions that are unique to
 *  the output part of the system . The OutputControllerException class is a
 *  part of the org.mitre.mrald.output package.
 *
 *@author     Gail Hamilton
 *@created    February 3, 2006
 */

public class OutputControllerException extends MraldException
{
    /**
     *  Constructor for the OutputControllerException object
     *
     *@param  sMessage  Description of the Parameter
     */
    public OutputControllerException( String sMessage )
    {
        super( sMessage );
    }


    /**
     *  Constructs a new exception with the specified cause and a detail message
     *  of <tt>(cause==null ? null : cause.getMessage())</tt> . MsgObject is set
     *  to null.
     *
     *@param  cause  the cause (which is saved for later retrieval by the {@link
     *      #getCause()} method). (A <tt>null</tt> value is permitted, and
     *      indicates that the cause is nonexistent or unknown.)
     *@since         1.4
     */
    public OutputControllerException( Throwable cause )
    {
        super( cause );
        msg = null;
    }
}

