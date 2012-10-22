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

import org.mitre.mrald.control.MsgObject;


/**
 *  An <code>MraldException</code> is a subclass of <code>Exception</code> that
 *  indicates either reasonable calling code might want to catch this exception,
 *  or the current thread's MsgObject is not available to the throwing method.
 *  <p>
 *
 *  This should only be used when the current thread's MsgObject is not
 *  available OR the calling code should handle the exception.<p>
 *
 *  An MraldError needs to be declared in a method's <code>throws</code> clause.
 *
 *@author     jchoyt
 *@created    August 28, 2003
 */
public class MraldException extends Exception
{
    protected MsgObject msg = null;


    /**
     *  Constructor for the MraldException object which sets the MsgObject.
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
    public MraldException( String message, Throwable cause, MsgObject msg )
    {
        super( message, cause );
        this.msg = msg;
    }


    /**
     *  Constructs a new exception with <code>null</code> as its detail message.
     *  The cause is not initialized, and may subsequently be initialized by a
     *  call to {@link #initCause}.
     */
    public MraldException()
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
    public MraldException( Throwable cause )
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
    public MraldException( Throwable cause, MsgObject msg )
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
    public MraldException( String message )
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
    public MraldException( String message, MsgObject msg )
    {
        super( message );
        this.msg = msg;
    }


    /**
     *  Constructor for the MraldException object which sets the MsgObject. The
     *  MsgObject is set to null.
     *
     *@param  message  the detail message (which is saved for later retrieval by
     *      the {@link #getMessage()} method).
     *@param  cause    the cause (which is saved for later retrieval by the
     *      {@link #getCause()} method). (A <tt>null</tt> value is permitted,
     *      and indicates that the cause is nonexistent or unknown.)
     */
    public MraldException( String message, Throwable cause )
    {
        super( message, cause );
        this.msg = null;
    }


    /**
     *  Sets the msg attribute of the MraldException object
     *
     *@param  msg  The new msg value
     */
    public void setMsg( MsgObject msg )
    {
        this.msg = msg;
    }


    /**
     *  Gets the msg attribute of the MraldException object
     *
     *@return    The msg value
     */
    public MsgObject getMsg()
    {
        return msg;
    }



    /**
     *  Returns a complete summary of the cause of the throwable (if known) and
     *  the MsgObject (if known)
     *
     *@return    summary of the exception and state of MRALD
     */
    public String toString()
    {
        return MiscUtils.formatThrowable(this);
        // StringBuffer ret = new StringBuffer();
        // ret.append( "~v~v~v~v~v~v~v~v~v~v~v~v~v~v~v~v~v~v~v~v~v~v~\n" );
        // ret.append( "Message: " );
        // ret.append( super.toString() );
        // ret.append( "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n" );
        // ret.append( "Root Cause: \n" );
        // if ( getCause() != null )
        // {
        //     StringWriter sw = new StringWriter();
        //     PrintWriter pw = new PrintWriter( sw );
        //     getCause().printStackTrace( pw );
        //     ret.append( "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n" );
        //     ret.append( sw.toString() );
        //     ret.append( "\n" );
        // }
        // if ( msg != null )
        // {
        //     ret.append( "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n" );
        //     ret.append( "State of this MRALD run's MsgObject: \n" );
        //     ret.append( msg.toString() );
        // }
        // ret.append( "\n~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~^~" );
        // return ret.toString();
    }
}

