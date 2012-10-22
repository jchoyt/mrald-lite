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
import org.mitre.mrald.query.OrFilterElement;

/**
 *  Description of the Class
 *
 *@author     jchoyt
 *@created    March 5, 2003
 */
public class OrFilterElementTest extends TestCase
{
    ArrayList<String> fromList, whereList;
    OrFilterElement testee1, testee2, testee3, testee4, testee5;


    /**
     *  The main program for the OrFilterElementTest class
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
        return new TestSuite( OrFilterElementTest.class );
    }


    /**
     *  A unit test for JUnit
     */
    public void testTestee1()
    throws Exception
    {
        testee1.buildFrom( fromList );
        assertEquals( "Table wasn't added properly", "ETMSCOMMON", fromList.get( 0 ) );
        testee1.buildWhere( whereList );
        assertEquals( "Where built wrong", "( ETMSCOMMON1.DEPTAIRPORT = 'UAS' OR ETMSCOMMON2.ARRAIRPORT = 'UAS' )", whereList.get( 0 ) );
    }


    /**
     *  A unit test for JUnit
     */
    public void testTestee2()
    throws Exception
    {
        testee2.buildFrom( fromList );
        assertEquals( "Table wasn't added properly", "ETMSCOMMON", fromList.get( 0 ) );
        testee2.buildWhere( whereList );
        assertEquals( "Where built wrong", "( ETMSCOMMON.DEPTAIRPORT = 'UAS' OR ETMSCOMMON.ARRAIRPORT IS NULL )", whereList.get( 0 ) );
    }


    public void testTestee5()
    throws Exception
    {
        testee5.buildFrom( fromList );
        assertEquals( "Table wasn't added properly", "ETMSCOMMON", fromList.get( 0 ) );
        testee5.buildWhere( whereList );
        assertEquals( "Where built wrong", "( ETMSCOMMON.DEPTAIRPORT IS NOT NULL OR ETMSCOMMON.ARRAIRPORT = 'UAS' )", whereList.get( 0 ) );
    }


    public void testIsActive()
    {
        testee1.postProcess( new MsgObject(), "dummy"  );
        testee2.postProcess( new MsgObject(), "dummy"  );
        assertTrue(testee1.getIsActive());
        assertTrue(testee2.getIsActive());
        testee3.postProcess( new MsgObject(), "dummy"  );
        testee4.postProcess( new MsgObject(), "dummy"  );
        assertTrue(testee3.getIsActive());
        assertFalse(testee4.getIsActive());
        testee5.postProcess( new MsgObject(), "dummy"  );
        assertTrue(testee5.getIsActive());
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
        testee1 = new OrFilterElement();
        testee2 = new OrFilterElement();
        testee3 = new OrFilterElement();
        testee4 = new OrFilterElement();
        testee5 = new OrFilterElement();
        fromList = new ArrayList<String>();
        whereList = new ArrayList<String>();
        String[] fodder1 = {"Table1:ETMSCOMMON~Syn1:ETMSCOMMON1~Field1:DEPTAIRPORT~Operator1:=~Value1:UAS~Table2:ETMSCOMMON~Syn2:ETMSCOMMON2~Field2:ARRAIRPORT~Operator2:=~Value2:UAS" };
        String[] fodder2 = {"Table1:ETMSCOMMON~Field1:DEPTAIRPORT~Operator1:=~Value1:UAS~Table2:ETMSCOMMON~Field2:ARRAIRPORT~Operator2:IS NULL" };
        String[] fodder3 = {"Table1:ETMSCOMMON~Field1:DEPTAIRPORT~Operator1:=~Value:UAS~Table2:ETMSCOMMON~Field2:ARRAIRPORT~Operator2:=" };
        String[] fodder4 = {"Table1:ETMSCOMMON~Field1:DEPTAIRPORT~Operator1:=~Value1:UAS~Table2:ETMSCOMMON~Field2:ARRAIRPORT~Operator2:=" };
        String[] fodder5 = {"Table1:ETMSCOMMON~Field1:DEPTAIRPORT~Operator1:IS NOT NULL~Value2:UAS~Table2:ETMSCOMMON~Field2:ARRAIRPORT~Operator2:=" };
        try
        {
            testee1.process( fodder1 );
            testee2.process( fodder2 );
            testee3.process( fodder3 );
            testee4.process( fodder4 );
            testee5.process( fodder5 );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }
}


