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
import org.mitre.mrald.query.StatElement;

/**
 *  Description of the Class
 *
 *@author     jchoyt
 *@created    March 5, 2003
 */
public class StatElementTest extends TestCase
{
    ArrayList<String> fromStrings;
    ArrayList<String> groupByStrings;
    ArrayList<String> selectStrings;


    /**
     *  The main program for the StatElementTest class
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
        return new TestSuite( StatElementTest.class );
    }


    /**
     *  A unit test for JUnit
     *
     *@exception  Exception  Description of the Exception
     */
    public void testBasic()
        throws Exception
    {
        String[] filler = {"Table:table~Field:field~Function:Count", "Order:4"};
        runTestee(filler);
        assertEquals( "Table wasn't added", "table", fromStrings.get( 0 ) );
		// assertEquals( "From build incorrectly", "Count( table.field )~4", (String) selectStrings.get(0));
        // assertEquals( "From build incorrectly", "Count( CAST(table.field AS NUMERIC(4,2) ))~4", (String) selectStrings.get(0));
        // assertEquals( "From build incorrectly", "Count( CAST(table.field AS NUMERIC(4,1) ))~4", (String) selectStrings.get(0));
        assertEquals( "Shouldn't have added anything to the GroupBy Strings", 0, groupByStrings.size());
    }


    /**
     *  A unit test for JUnit
     */
    public void testSyn()
    throws Exception
    {
        String[] filler = {"Table:table~Field:field~Function:Count", "Synomyn:tab~Order:4"};
        runTestee(filler);
        assertEquals( "Table wasn't added", "table tab", fromStrings.get( 0 ) );
        // assertEquals( "Select built incorrectly", "Count( tab.field )~4", (String) selectStrings.get(0));
	    //assertEquals( "Select built incorrectly", "Count( CAST(tab.field AS NUMERIC(4,1) ))~4", (String) selectStrings.get(0));
        assertEquals( "Shouldn't have added anything to the GroupBy Strings", 0, groupByStrings.size());
    }


    /**
     *  A unit test for JUnit
     */
    public void testCountDistinct()
    throws Exception
    {
        String[] filler = {"Table:table~Field:field~Function:Count(DISTINCT", "Order:4"};
        runTestee(filler);
        assertEquals( "Table wasn't added", "table", fromStrings.get( 0 ) );
        //assertEquals( "Select build incorrectly", "Count(DISTINCT table.field )~4", (String) selectStrings.get(0));
        //assertEquals( "Select built incorrectly", "Count(DISTINCT CAST(table.field AS NUMERIC(4,1) ))~4", (String) selectStrings.get(0));

	assertEquals( "Shouldn't have added anything to the GroupBy Strings", 0, groupByStrings.size());
    }

    public void testFrequency()
        throws Exception
    {
        String[] filler = {"Field:FLIGHTNO~GroupSelect:ASQPDERIVEDTIME.CRSDEPLOC~Order:2",
            "Group:Hour~Table:ASQPDERIVEDTIME",
            "Function:Count"};
        runTestee(filler);
        assertEquals( "Table wasn't added", "ASQPDERIVEDTIME", fromStrings.get( 0 ) );
        assertEquals( "From build incorrectly", "TO_CHAR( ASQPDERIVEDTIME.CRSDEPLOC, 'YYYY-MM-DD HH24') Hour~2", selectStrings.get(0));
        assertEquals( "GroupBy built incorrectly", "TO_CHAR( ASQPDERIVEDTIME.CRSDEPLOC, 'YYYY-MM-DD HH24') ", groupByStrings.get(0));
    }

    /**
     *  A unit test for JUnit
     */
    public void testCountStar()
    throws Exception
    {
        String[] filler = {"Table:table~Field:field~Function:Count(*)", "Order:4~Table:moreTables"};
        runTestee(filler);
        assertTrue( "table wasn't added", fromStrings.contains("table"));
        assertTrue( "moreTables wasn't added", fromStrings.contains("moreTables"));
        assertEquals( "Select build incorrectly (" + selectStrings.get(0) + ")", "Count(*) AS All_Count~4", selectStrings.get(0));
        assertEquals( "Shouldn't have added anything to the GroupBy Strings", 0, groupByStrings.size());
    }

    public void testCountStarNoField()
    throws Exception
    {
        String[] filler = {"Table:table~Function:Count(*)", "Order:4~Table:moreTables"};
        runTestee(filler);
        assertTrue( "table wasn't added", fromStrings.contains("table"));
        assertTrue( "moreTables wasn't added", fromStrings.contains("moreTables"));
        assertEquals( "Select build incorrectly", "Count(*) AS All_Count~4", selectStrings.get(0));
        assertEquals( "Shouldn't have added anything to the GroupBy Strings", 0, groupByStrings.size());
    }

    public void testNoField()
    throws Exception
    {
        String[] filler = {"Table:table~Function:Count", "Synomyn:tab~Order:4"};
        runTestee(filler);
        assertEquals( "Shouldn't have added anything to the From Strings", 0, fromStrings.size());
        assertEquals( "Shouldn't have added anything to the Select Strings", 0, selectStrings.size());
        assertEquals( "Shouldn't have added anything to the GroupBy Strings", 0, groupByStrings.size());
    }


    public void testNoFunction()
    throws Exception
    {
        String[] filler = {"Table:table~Field:field", "Synomyn:tab~Order:4"};
        runTestee(filler);
        assertEquals( "Shouldn't have added anything to the From Strings", 0, fromStrings.size());
        assertEquals( "Shouldn't have added anything to the Select Strings", 0, selectStrings.size());
        assertEquals( "Shouldn't have added anything to the GroupBy Strings", 0, groupByStrings.size());
    }
    /**
     *  The JUnit setup method
     */
    protected void setUp()
    throws Exception
    {
        super.setUp();
        fromStrings = new ArrayList<String>();
        groupByStrings = new ArrayList<String>();
        selectStrings = new ArrayList<String>();
    }


    /**
     *  creates a stat element from the given String array, processes it, and
     *  runs it through all the builsdXxx() methods. Since the lists are reset
     *  for each test, the only thing left is to test the lists for includsion
     *  of the appropriate strings
     *
     *@param  filler  Description of the Parameter
     */
    protected void runTestee( String[] filler )
    throws Exception
    {
        StatElement testee = new StatElement();
        testee.process( filler );
        testee.buildFrom( fromStrings );
        testee.buildSelect( selectStrings );
        testee.buildGroupBy( groupByStrings );
    }
}


