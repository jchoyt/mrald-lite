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
import org.mitre.mrald.util.WebUtils;

/**
 *  Description of the Class
 *
 *@author     jchoyt
 *@created    July 26, 2004
 */
public class WebUtilsTest extends TestCase
{
    /*
     *  add tests here
     */
    /**
     *  A unit test for JUnit
     */
    public void testIsValidEmailAddress()
    {
        String email = "mrald-dev-list@listsrv.mitre.org";
        assertTrue( email, WebUtils.isValidEmailAddress( email ) );

        email = "jchoyt@users.sourceforge.net";
        assertTrue( email, WebUtils.isValidEmailAddress( email ) );

        email = "me@yahoo.com";
        assertTrue( email, WebUtils.isValidEmailAddress( email ) );

        email = "give.me.gmail@gmail.com";
        assertTrue( email, WebUtils.isValidEmailAddress( email ) );

        email = "yahoo.com";
        assertFalse( email, WebUtils.isValidEmailAddress( email ) );

        email = "me@";
        assertFalse( email, WebUtils.isValidEmailAddress( email ) );

        email = "@yahoo.com";
        assertFalse( email, WebUtils.isValidEmailAddress( email ) );

        email = "mrald-dev-list@@listsrv.mitre.org";
        assertFalse( email, WebUtils.isValidEmailAddress( email ) );

        email = "me@yahoo";
        assertFalse( email, WebUtils.isValidEmailAddress( email ) );

        email = "@@@.";
        assertFalse( email, WebUtils.isValidEmailAddress( email ) );
    }


    /**
     *  The JUnit setup method
     */
    protected void setUp()
    throws Exception
    {
        super.setUp();

    }


    /**
     *  The main program for the WebUtilsTest class
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
        return new TestSuite( WebUtilsTest.class );
    }
}


