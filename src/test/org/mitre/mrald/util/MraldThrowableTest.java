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



package test.org.mitre.mrald.util;
import junit.framework.*;

import org.mitre.mrald.control.*;
import org.mitre.mrald.util.*;

/**
 *  Description of the Class
 *
 *@author     jchoyt
 *@created    August 28, 2003
 */
public class MraldThrowableTest extends TestCase
{
    String ERROR_STRING = "errorstring";


    /**
     *  The main program for the MraldThrowableTest class
     *
     *@param  args  The command line arguments
     */
    public static void main( String[] args )
    {
        junit.textui.TestRunner.run( suite() );
    }


    /**
     *  A unit test suite for JUnit
     *
     *@return    The test suite
     */
    public static Test suite()
    {
        /*
         *  the dynamic way
         */
        return new TestSuite( MraldThrowableTest.class );
    }


    /**
     *  Description of the Method
     *
     *@param  depth  Description of the Parameter
     */
    public void errorRecursion( int depth )
    {
        depth++;
        if ( depth < 10 )
        {
            errorRecursion( depth );
        }
        else
        {
            throw new MraldError( "throwing from depth of 10", new Exception( "depth=" + depth ), new MsgObject() );
        }
    }


    /**
     *  A unit test for JUnit
     */
    public void testError()
    {
        try
        {
            throwError();
        }
        catch ( MraldError error )
        {
        }
        catch ( Throwable error )
        {
            fail( "wrong type of exception propagated." + error.toString() );
        }
    }


    /**
     *  A unit test for JUnit
     */
    public void testException()
    {
        try
        {
            throwException();
        }
        catch ( MraldException e )
        {
        }
        catch ( Throwable error )
        {
            fail( "wrong type of exception propagated." + error.toString() );
        }
    }


    /*
     *  add tests here
     */
    /**
     *  A unit test for JUnit
     */
    public void testExceptionToError()
    {
        boolean caught=false;
        try
        {
            throwExceptionToError();
        }
        catch ( Throwable error )
        {
            assertTrue( error.getMessage().equals( ERROR_STRING ) );
            assertTrue( error instanceof org.mitre.mrald.util.MraldError);
            assertTrue( error.getCause() instanceof org.mitre.mrald.util.MraldException);
            caught=true;
        }
        assertTrue(caught);
    }


    /**
     *  Description of the Method
     */
    public void throwError()
    {
        throwError1();
    }


    /**
     *  Description of the Method
     */
    public void throwError1()
    {
        throw new MraldError( "thrown error" );
    }


    /**
     *  Description of the Method
     *
     *@exception  MraldException  Description of the Exception
     */
    public void throwException()
        throws MraldException
    {
        throwException1();
    }


    /**
     *  Description of the Method
     *
     *@exception  MraldException  Description of the Exception
     */
    public void throwException1()
        throws MraldException
    {
        throw new MraldException( "thrown exception" );
    }


    /**
     *  Description of the Method
     */
    public void throwExceptionToError()
    {
        try
        {
            throwException();
        }
        catch ( MraldException e )
        {
            throw new MraldError( ERROR_STRING, e );
        }
    }


    /**
     *  The JUnit setup method
     */
    protected void setUp()
    throws Exception
    {
        super.setUp();

    }
}

