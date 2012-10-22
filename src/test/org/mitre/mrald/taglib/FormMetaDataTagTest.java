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


package test.org.mitre.mrald.taglib;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.mitre.mrald.util.Config;

import com.mockobjects.sql.MockConnection2;
import com.mockobjects.sql.MockDriver;
import com.mockobjects.sql.MockSingleRowResultSet;
import com.mockobjects.sql.MockStatement;

/**
 *  Description of the Class
 *
 *@author     jchoyt
 *@created    June 29, 2004
 */
public class FormMetaDataTagTest extends TestCase
{
    /*
     *  add tests here
     */

    /**
     *  The JUnit setup method
     */
    protected void setUp()
    throws Exception
    {
        super.setUp();
    }


    /**
     *  The main program for the FormMetaDataTagTest class
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
        return new TestSuite( FormMetaDataTagTest.class );
    }


    /**
     *  A unit test for JUnit
     */
    public void testNothing()
    {
        assertTrue( true );
    }


    /**
     *  Description of the Method
     */
    protected void setupMockDatabase()
    {
        /*
         *  Define the queries that we expect to have sent
         */
        String queryOne = "select max(FirstField)+1 from TestTable";
        String queryTwo = "select max(SecondField)+1 from TestTable";
        String queryFour = new String( queryOne );
        /*
         *  Define the result sets that will be returned for each of those queries
         */
        String[] results = {"1"};
        MockSingleRowResultSet rsOne = new MockSingleRowResultSet();
        rsOne.addExpectedIndexedValues( results );
        String[] results2 = {"7"};
        MockSingleRowResultSet rsTwo = new MockSingleRowResultSet();
        rsTwo.addExpectedIndexedValues( results2 );
        String[] results4 = {"8"};
        MockSingleRowResultSet rsFour = new MockSingleRowResultSet();
        rsFour.addExpectedIndexedValues( results4 );
        /*
         *  Set up the mock result set . . .
         */
        MockStatement mockStatement = new MockStatement();
        /*
         *  . . .  and match up queries with ResultSets
         */
        mockStatement.addExpectedExecuteQuery( queryOne, rsOne );
        mockStatement.addExpectedExecuteQuery( queryTwo, rsTwo );
        mockStatement.addExpectedExecuteQuery( queryFour, rsFour );
        /*
         *  match up the Statement with the Connection
         */
        MockConnection2 mockConnection = new MockConnection2();
        mockConnection.setupStatement( mockStatement );
        /*
         *  match up the Connection with the Driver
         */
        MockDriver.myDriver.setupConnect( mockConnection );
        /*
         *  set up Config
         */
        Config.setProperty( "DBDRIVER", "com.mockobjects.sql.MockDriver" );
        /*
         *  the below are filler - it doesn't matter what we put here, but we
         *  need to put SOMETHING in here or Config will barf
         */
        Config.setProperty( "DBLOGIN", "here" );
        Config.setProperty( "DBPASSWORD", "now" );
        Config.setProperty( "DBSERVER", "jdbc:mockdriver:null" );
    }
}


