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
package org.mitre.mrald.control;

import org.mitre.mrald.util.MraldException;

/**
 *  The WorkflowStepException class handles exception specifically thrown by the
 *  WorkflowStep class. It is a part of the org.mitre.mrald.control package.
 *
 *@author     jchoyt
 *@created    April 11, 2001
 */
public class WorkflowStepException extends MraldException
{
    /**
     *  Constructor for the WorkflowStepException object which sets the MsgObject.
     *
     *@param  message  the detail message (which is saved for later retrieval by
     *      the {@link #getMessage()} method).
     *@param  cause    the cause (which is saved for later retrieval by the
     *      {@link #getCause()} method). (A <tt>null</tt> value is permitted,
     *      and indicates that the cause is nonexistent or unknown.)
     *@param  msg      the MsgObject for this run of MRALD. Passed so the
     *      internal structure of the MsgObject can be discoverd. A null value
     *      is permitted if the MsgObject is not yet created, or if it is out of
     *      scope at the time the Excpetion is thrown. In this case, the
     *      Exception should be passed up the stack until the MsgObjec is within
     *      scope.
     */
    public WorkflowStepException( String message, Throwable cause, MsgObject msg )
    {
        super( message, cause );
        this.msg = msg;
    }


    /**
     *  Constructs a new exception with <code>null</code> as its detail message.
     *  The cause is not initialized, and may subsequently be initialized by a
     *  call to {@link #initCause}.
     */
    public WorkflowStepException()
    {
        super();
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
    public WorkflowStepException( Throwable cause )
    {
        super( cause );
        msg = null;
    }


    /**
     *  Constructs a new exception with the specified cause and MsgObject and a
     *  detail message of <tt>(cause==null ? null : cause.getMessage())</tt> .
     *
     *@param  cause  the cause (which is saved for later retrieval by the {@link
     *      #getCause()} method). (A <tt>null</tt> value is permitted, and
     *      indicates that the cause is nonexistent or unknown.)
     *@param  msg    Description of the Parameter
     *@since         1.4
     */
    public WorkflowStepException( Throwable cause, MsgObject msg )
    {
        super( cause );
        this.msg = msg;
    }


    /**
     *  Constructs a new exception with the specified detail message. The cause
     *  is not initialized, and may subsequently be initialized by a call to
     *  {@link #initCause}.
     *
     *@param  message  the detail message. The detail message is saved for later
     *      retrieval by the {@link #getMessage()} method.
     */
    public WorkflowStepException( String message )
    {
        super( message );
    }


    /**
     *  Constructs a new exception with the specified detail message. The cause
     *  is not initialized, and may subsequently be initialized by a call to
     *  {@link #initCause}.
     *
     *@param  message  the detail message. The detail message is saved for later
     *      retrieval by the {@link #getMessage()} method.
     *@param  msg      Description of the Parameter
     */
    public WorkflowStepException( String message, MsgObject msg )
    {
        super( message );
        this.msg = msg;
    }


    /**
     *  Constructor for the WorkflowStepException object which sets the MsgObject. The
     *  MsgObject is set to null.
     *
     *@param  message  the detail message (which is saved for later retrieval by
     *      the {@link #getMessage()} method).
     *@param  cause    the cause (which is saved for later retrieval by the
     *      {@link #getCause()} method). (A <tt>null</tt> value is permitted,
     *      and indicates that the cause is nonexistent or unknown.)
     */
    public WorkflowStepException( String message, Throwable cause )
    {
        super( message, cause );
        this.msg = null;
    }

}

