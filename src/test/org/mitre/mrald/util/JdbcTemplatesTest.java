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
import org.mitre.mrald.util.JdbcTemplates;


/**
 *  Description of the Class
 *
 *@author     jchoyt
 *@created    February 12, 2006
 */
public class JdbcTemplatesTest extends TestCase
{
    /*
     *  add tests here
     */
    /**
     *  A unit test for JUnit
     */
    public void testInit()
    {
        JdbcTemplates.init( System.getProperty( "antfile.dir" ) + "/config" );
        String configured = JdbcTemplates.getList();
        assertTrue( configured, configured.length() > 100 );
    }


    /**
     *  The JUnit setup method
     */
    protected void setUp() { }


    /**
     *  The JUnit teardown method
     */
    protected void tearDown() { }


    /**
     *  The main program for the JdbcTemplatesTest class
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
        return new TestSuite( JdbcTemplatesTest.class );
    }
}


