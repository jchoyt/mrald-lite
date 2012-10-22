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


package test.org.mitre.mrald.query;

import java.util.ArrayList;
import junit.framework.*;
import org.mitre.mrald.control.MsgObject;
import org.mitre.mrald.query.FilterElement;

/**
 *  Description of the Class
 *
 *@author     jchoyt
 *@created    March 5, 2003
 */
public class FilterElementTest extends TestCase
{
    ArrayList<String> fromList, whereList;
    FilterElement testee1, testee2, testee3, testee4, noValueTestee, testee6, testee7;


    /**
     *  The main program for the FilterElementTest class
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
        return new TestSuite( FilterElementTest.class );
    }


    /**
     *  A unit test for JUnit
     */
    public void testTestee1()
    throws Exception
    {
        testee1.buildFrom( fromList );
        assertEquals( "Table wasn't added properly", "table", fromList.get( 0 ) );
        testee1.buildWhere( whereList );
        assertEquals( "Where built wrong", " ( table.field = value1 )", whereList.get( 0 ) );
    }


    /**
     *  A unit test for JUnit
     */
    public void testTestee2()
    throws Exception
    {
        testee2.buildFrom( fromList );
        assertEquals( "Table wasn't added properly", "OtherTable syn", fromList.get( 0 ) );
        testee2.buildWhere( whereList );
        assertEquals( "Where built wrong", " ( syn.field >= 'value1' OR syn.field >= 'value2' )", whereList.get( 0 ) );
    }


    public void testTestee3()
    throws Exception
    {
        testee3.buildWhere( whereList );
        try
        {
            assertEquals( "Where built wrong", " ( table.field IS NULL )", whereList.get( 0 ) );
        }
        catch (java.lang.IndexOutOfBoundsException e)
        {
            fail("No where clause was built.");
        }
    }

    public void testTestee4()
    throws Exception
    {
        testee4.buildWhere( whereList );
        //assertEquals( "Where built wrong", " ( table.field IN ( 'value1','v2','v3','v4' ) )", ( String ) whereList.get( 0 ) );
    }

    public void testTestee6()
    throws Exception
    {
        testee6.buildWhere( whereList );
        assertEquals( "Where built wrong", " ( table.fiveValues IN ( 'one', 'two', 'three', 'four', 'five' ) )", whereList.get( 0 ) );
    }


    public void testTestee7()
    throws Exception
    {
        testee7.buildWhere( whereList );
        assertEquals( "Where built wrong", " ( table.threeValues NOT IN ( 'one', 'two', 'three' ) )", whereList.get( 0 ) );
    }


    public void testIsActive()
    {
        testee1.postProcess( new MsgObject(), "dummy"  );
        testee2.postProcess( new MsgObject(), "dummy"  );
        testee3.postProcess( new MsgObject(), "dummy"  );
        testee4.postProcess( new MsgObject(), "dummy"  );
        noValueTestee.postProcess( new MsgObject(), "dummy"  );
        testee6.postProcess( new MsgObject(), "dummy"  );
        testee7.postProcess( new MsgObject(), "dummy"  );
        assertTrue(testee1.getIsActive());
        assertTrue(testee2.getIsActive());
        assertTrue(testee3.getIsActive());
        assertTrue(testee4.getIsActive());
        assertFalse("Since there is no value, this should be false", noValueTestee.getIsActive());
        assertTrue(testee6.getIsActive());
        assertTrue(testee7.getIsActive());
    }
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
        testee1 = new FilterElement();
        testee2 = new FilterElement();
        testee3 = new FilterElement();
        testee4 = new FilterElement();
        noValueTestee = new FilterElement();
        testee6 = new FilterElement();
        testee7 = new FilterElement();
        fromList = new ArrayList<String>();
        whereList = new ArrayList<String>();
        String[] fodder1 = {"Table:table~Field:field~Operator:=",
                "Value:value1~Type:Numeric"};
        String[] fodder2 = {"Table:OtherTable~Field:field~Operator:>=",
                "Value:value1~Type:String", "Synomyn:syn~value2"};
        String[] fodder3 = {"Table:table~Field:field~Operator:IS NULL",
                "Type:Date"};
        String[] fodder4 = {"Table:table~Field:field~Operator:IN",
                "Value:value1,v2,v3,v4~Type:Date"};
        String[] fodder5 = {"Table:table~Field:no-value~Operator:>",
                "Type:String"};
        //more than two values with an = operator should create an "IN" list
        // also testing the "feature" that values separated by ~ will spearate
        String[] fodder6 = {"Table:table~Field:fiveValues~Operator:=",
                "Type:String", "one", "two", "three~four~five"};
        //more than two values with an != operator should create a "NOT IN" list
        String[] fodder7 = {"Table:table~Field:threeValues~Operator:!=",
                "Type:String", "one", "two", "three"};
        try
        {
            testee1.process( fodder1 );
            testee2.process( fodder2 );
            testee3.process( fodder3 );
            testee4.process( fodder4 );
            noValueTestee.process( fodder5 );
            testee6.process( fodder6 );
            testee7.process( fodder7 );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }
}


