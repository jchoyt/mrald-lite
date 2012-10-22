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


package test.org.mitre.mrald.ddlelements;

import com.mockobjects.sql.*;
import java.util.*;
import junit.framework.*;
import org.mitre.mrald.control.*;
import org.mitre.mrald.ddlelements.InsertSequenceElement;
import org.mitre.mrald.util.*;

/**
 *  Description of the Class
 *
 *@author     jchoyt
 *@created    November 27, 2002
 */
public class InsertSequenceElementTest extends TestCase
{
    /*
     *  add tests here
     */
    ArrayList<String> currentFieldList;
    InsertSequenceElement one, two, three, four;


    /**
     *  The main program for the InsertSequenceElementTest class
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
        return new TestSuite( InsertSequenceElementTest.class );
    }


    /**
     *  A unit test for JUnit
     */
    public void testBuildFieldList()
    {
        String expectedString = "FirstField";
        assertFalse( expectedString + " already in there", currentFieldList.contains( expectedString ) );
        one.buildFieldList( currentFieldList );
        assertEquals( expectedString + " isn't in there", expectedString, currentFieldList.get( 0 ) );

        //test for input 2
        expectedString = "SecondField";
        assertFalse( expectedString + " already in there", currentFieldList.contains( expectedString ) );
        two.buildFieldList( currentFieldList );
        assertEquals( expectedString + " isn't in there", expectedString, currentFieldList.get( 1 ) );
    }


    /**
     *  A unit test for JUnit
     */
    public void testBuildValueList()
    {
        try
        {
            String expectedString = "1";
            assertFalse( expectedString + " already in there", currentFieldList.contains( expectedString ) );
            currentFieldList = one.buildValueList( currentFieldList );
            assertEquals( expectedString + " isn't in there", expectedString, currentFieldList.get( 0 ) );

            //test for input 2
            expectedString = "7";
            assertFalse( expectedString + " already in there", currentFieldList.contains( expectedString ) );
            currentFieldList = two.buildValueList( currentFieldList );
            assertEquals( expectedString + " isn't in there", expectedString, currentFieldList.get( 1 ) );
        }
        catch ( MraldException e )
        {
        }
    }


    /**
     *  A unit test for JUnit
     */
    public void testEquals()
    {
        /*
         *  It is reflexive: for any reference value x, x.equals(x) should return true.
         *  It is symmetric: for any reference values x and y, x.equals(y) should return true if and only if y.equals(x) returns true.
         *  It is transitive: for any reference values x, y, and z, if x.equals(y) returns true and y.equals(z) returns true, then x.equals(z) should return true.
         *  It is consistent: for any reference values x and y, multiple invocations of x.equals(y) consistently return true or consistently return false, provided no information used in equals comparisons on the object is modified.
         *  For any non-null reference value x, x.equals(null) should return false.
         */
        assertTrue( "for any reference value x, x.equals(x) should return true.", one.equals( one ) );
        assertTrue( "for any reference values x and y, x.equals(y) should return true if and only if y.equals(x) returns true.  one.equals(four) was " + one.equals( four ) + " and four.equals(one) was " + four.equals( one ), one.equals( four ) && four.equals( one ) );
        assertTrue( "for any reference values x and y, multiple invocations of x.equals(y) consistently return true or consistently return false, provided no information used in equals comparisons on the object is modified.", one.equals( four ) );
        assertFalse( "For any non-null reference value x, x.equals(null) should return false.", one.equals( null ) );
    }


    /**
     *  A unit test for JUnit
     */
    public void testHashCode()
    {
        /*
         *  Whenever it is invoked on the same object more than once during an execution of a Java application, the hashCode method must consistently return the same integer, provided no information used in equals comparisons on the object is modified. This integer need not remain consistent from one execution of an application to another execution of the same application.
         *  If two objects are equal according to the equals(Object) method, then calling the hashCode method on each of the two objects must produce the same integer result.
         *  It is not required that if two objects are unequal according to the equals(java.lang.Object) method, then calling the hashCode method on each of the two objects must produce distinct integer results. However, the programmer should be aware that producing distinct integer results for unequal objects may improve the performance of hashtables.
         */
        assertEquals( "Whenever it is invoked on the same object more than once during an execution of a Java application, the hashCode method must consistently return the same integer, provided no information used in equals comparisons on the object is modified. This integer need not remain consistent from one execution of an application to another execution of the same application.", one.hashCode(), one.hashCode() );
        assertEquals( "If two objects are equal according to the equals(Object) method, then calling the hashCode method on each of the two objects must produce the same integer result.", one.hashCode(), four.hashCode() );
        assertFalse( "It is not required that if two objects are unequal according to the equals(java.lang.Object) method, then calling the hashCode method on each of the two objects must produce distinct integer results. However, the programmer should be aware that producing distinct integer results for unequal objects may improve the performance of hashtables.", one.hashCode() == two.hashCode() );
    }


    /**
     *  The JUnit setup method
     */
    protected void setUp()
    throws Exception
    {
        super.setUp();
        setupMockDatabase();
        MsgObject msg = new MsgObject();
        try
        {
            one = new InsertSequenceElement();
            String[] input1 = {"Table:TestTable~Field:FirstField~Order:1~Datasource:main"};
            one.process( input1 );
            one.postProcess( msg, "dummyname" );

            two = new InsertSequenceElement();
            String[] input2 = {"Table:TestTable~Field:SecondField~Order:2~Datasource:main"};
            two.process( input2 );
            two.postProcess( msg, "dummyname" );

            three = new InsertSequenceElement();
            String[] input3 = {"Table:TestTable~Field:SecondField~Order:2~SqlThread:2,5~Datasource:main"};
            three.process( input3 );
            three.postProcess( msg, "dummyname" );

            four = new InsertSequenceElement();
            four.process( input1 );
            four.postProcess( msg, "dummyname" );

            currentFieldList = new ArrayList<String>();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }


    /**
     *  A unit test for JUnit
     */
    public void testIsActive()
    {
        assertTrue( one.getIsActive() );
        assertFalse( three.getIsActive() );
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
        String queryThree = "select max(SecondField)+1 from TestTable";
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
        String[] results3 = {"7"};
        MockSingleRowResultSet rsThree = new MockSingleRowResultSet();
        rsThree.addExpectedIndexedValues( results3 );
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
        mockStatement.addExpectedExecuteQuery( queryThree, rsThree );
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


        /*
         *  populate the database metadata
         */
        Properties mainProps = new Properties();
        mainProps.put( "DBLOGIN", Config.getProperty( "DBLOGIN" ) );
        mainProps.put( "DBPASSWORD", Config.getProperty( "DBPASSWORD" ) );
        mainProps.put( "DBDRIVER", Config.getProperty( "DBDRIVER" ) );
        mainProps.put( "DBSERVER", Config.getProperty( "DBSERVER" ) );
        // mainProps.put( "SCHEMA", Config.getProperty( "SCHEMA" ) );
        //load the driver
        try {
            Class.forName( Config.getProperty( "DBDRIVER" ) );
        }

        catch ( ClassNotFoundException e ) {
            e.printStackTrace();
        }

        DBMetaData main = new DBMetaData( mainProps );
        MetaData.putDbProperties( "main", main );

    }
}


