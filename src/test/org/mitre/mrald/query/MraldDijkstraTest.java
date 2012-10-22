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
import org.mitre.mrald.query.*;

/**
 *  Description of the Class
 *
 *@author     jchoyt
 *@created    September 4, 2003
 */
public class MraldDijkstraTest extends TestCase
{
    MsgObject backJoinedStructure;
    MsgObject baseStructure;
    MsgObject circularStructure;
    MsgObject complexStructure;
    MsgObject lollipopStructure;
    MsgObject racetrackStructure;
    MsgObject simpleStructure;
    MsgObject unlinkedStructure;


    /**
     *  The main program for the MraldDijkstraTest class
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
        return new TestSuite( MraldDijkstraTest.class );
    }



    /**
     *  A unit test for JUnit
     */
    public void testComplexStrutureWithDoubleJoin()
    {
        ArrayList<String> fromStrings = new ArrayList<String>();
        fromStrings.add( "One" );
        fromStrings.add( "Two" );
        MraldDijkstra links = new MraldDijkstra( complexStructure );
        try
        {
            String linkString = links.runDijkstra( fromStrings );
            assertFalse( linkString, linkString.indexOf( "One.key=Two.fkey" ) == -1 );
            assertFalse( linkString, linkString.indexOf( " AND " ) == -1 );
            assertFalse( linkString, linkString.indexOf( "One.other_key=Two.other_fkey" ) == -1 );
            assertEquals( linkString, 16 + 5 + 28, linkString.length() );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }


    /**
     *  A unit test for JUnit
     */
    public void testComplexStrutureWithDoubleJoinMissingCenterTable()
    {
        ArrayList<String> fromStrings = new ArrayList<String>();
        fromStrings.add( "One" );
        fromStrings.add( "Three" );
        MraldDijkstra links = new MraldDijkstra( complexStructure );
        try
        {
            String linkString = links.runDijkstra( fromStrings );
            assertFalse( linkString, linkString.indexOf( "One.key=Two.fkey" ) == -1 );
            assertFalse( linkString, linkString.indexOf( " AND " ) == -1 );
            assertFalse( linkString, linkString.indexOf( "Two.key=Three.fkey" ) == -1 );
            assertFalse( linkString, linkString.indexOf( "One.other_key=Two.other_fkey" ) == -1 );
            assertEquals( linkString, 16 + 2 * 5 + 18 + 28, linkString.length() );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }


    /**
     *  A unit test for JUnit
     */
    public void testSimpleStrutureWithThreeTables()
    {
        ArrayList<String> fromStrings = new ArrayList<String>();
        fromStrings.add( "One" );
        fromStrings.add( "Two" );
        fromStrings.add( "Three" );
        MraldDijkstra links = new MraldDijkstra( simpleStructure );
        try
        {
            String linkString = links.runDijkstra( fromStrings );
            assertFalse( linkString, linkString.indexOf( "One.key=Two.fkey" ) == -1 );
            assertFalse( linkString, linkString.indexOf( " AND " ) == -1 );
            assertFalse( linkString, linkString.indexOf( "Two.key=Three.fkey" ) == -1 );
            assertEquals( linkString, 16 + 5 + 18, linkString.length() );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }


    /**
     *  A unit test for JUnit
     */
    public void testSimpleStrutureWithThreeTablesAndCenterTableMissing()
    {
        ArrayList<String> fromStrings = new ArrayList<String>();
        fromStrings.add( "One" );
        fromStrings.add( "Three" );
        MraldDijkstra links = new MraldDijkstra( simpleStructure );
        try
        {
            String linkString = links.runDijkstra( fromStrings );
            assertFalse( linkString, linkString.indexOf( "One.key=Two.fkey" ) == -1 );
            assertFalse( linkString, linkString.indexOf( " AND " ) == -1 );
            assertFalse( linkString, linkString.indexOf( "Two.key=Three.fkey" ) == -1 );
            assertEquals( linkString, 16 + 5 + 18, linkString.length() );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }


    /**
     *  A unit test for JUnit
     */
    public void testSimpleStrutureWithTwoTables()
    {
        ArrayList<String> fromStrings = new ArrayList<String>();
        fromStrings.add( "One" );
        fromStrings.add( "Two" );
        MraldDijkstra links = new MraldDijkstra( simpleStructure );
        try
        {
            String linkString = links.runDijkstra( fromStrings );
            assertEquals( "One.key=Two.fkey", linkString );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }


    /**
     *  Testing the testCompleteJoinPath() method
     */
    public void testTestCompleteJoinPath()
    {
        ArrayList<String> fromStrings = new ArrayList<String>();
        fromStrings.add( "One" );
        fromStrings.add( "Two" );
        /*
         *  test for "back-joined" structure - should be OK
         */
        MraldDijkstra links = new MraldDijkstra( backJoinedStructure );
        assertEquals( MraldDijkstra.JOIN_PATH_OK, links.testCompleteJoinPath( fromStrings ) );
        fromStrings.add( "Three" );
        /*
         *  test simple strucutre links
         */
        links = new MraldDijkstra( simpleStructure );
        assertEquals( MraldDijkstra.JOIN_PATH_OK, links.testCompleteJoinPath( fromStrings ) );
        /*
         *  test complex strucutre links
         */
        links = new MraldDijkstra( complexStructure );
        assertEquals( MraldDijkstra.JOIN_PATH_OK, links.testCompleteJoinPath( fromStrings ) );
        /*
         *  test circular strucutre links  - this should fail the unique join test, but pass this one just fine.  A circular structure should NOT hold up testing for a complete path
         */
        links = new MraldDijkstra( circularStructure );
        assertEquals( MraldDijkstra.JOIN_PATH_OK, links.testCompleteJoinPath( fromStrings ) );
        fromStrings.add( "Four" );
        /*
         *  test unlinked strucutre links - should get path-not-complete value back
         */
        links = new MraldDijkstra( unlinkedStructure );
        assertEquals( MraldDijkstra.JOIN_PATH_NOT_COMPLETE, links.testCompleteJoinPath( fromStrings ) );
        /*
         *  test for "lollipop" structure - should be OK
         */
        links = new MraldDijkstra( lollipopStructure );
        assertEquals( MraldDijkstra.JOIN_PATH_OK, links.testCompleteJoinPath( fromStrings ) );
        /*
         *  test for "racetrack" structure - should be OK
         */
        links = new MraldDijkstra( racetrackStructure );
        assertEquals( MraldDijkstra.JOIN_PATH_OK, links.testCompleteJoinPath( fromStrings ) );
        /*
         *  test for table in from list but not in a link element- should get path-not-complete value back
         */
        fromStrings.add( "Five" );
        links = new MraldDijkstra( simpleStructure );
        assertEquals( MraldDijkstra.JOIN_PATH_NOT_COMPLETE, links.testCompleteJoinPath( fromStrings ) );
    }


    /**
     *  A unit test for JUnit
     */
    public void testTestJoinPath()
    {
        ArrayList<String> fromStrings = new ArrayList<String>();
        fromStrings.add( "One" );
        fromStrings.add( "Two" );
        /*
         *  test for "back-joined" structure - should be NOT_UNIQUE
         */
        MraldDijkstra links = new MraldDijkstra( backJoinedStructure );
        assertEquals( MraldDijkstra.JOIN_PATH_NOT_UNIQUE, links.testJoinPath( fromStrings ) );
        fromStrings.add( "Three" );
        /*
         *  test simple strucutre links - should be OK
         */
        links = new MraldDijkstra( simpleStructure );
        assertEquals( MraldDijkstra.JOIN_PATH_OK, links.testJoinPath( fromStrings ) );
        /*
         *  test complex strucutre links - should be OK
         */
        links = new MraldDijkstra( complexStructure );
        assertEquals( MraldDijkstra.JOIN_PATH_OK, links.testJoinPath( fromStrings ) );
        /*
         *  test circular strucutre links  - should be NOT_UNIQUE
         */
        links = new MraldDijkstra( circularStructure );
        assertEquals( MraldDijkstra.JOIN_PATH_NOT_UNIQUE, links.testJoinPath( fromStrings ) );
        fromStrings.add( "Four" );
        /*
         *  test unlinked strucutre links - should get path-not-complete value back
         */
        links = new MraldDijkstra( unlinkedStructure );
        assertEquals( MraldDijkstra.JOIN_PATH_NOT_COMPLETE, links.testJoinPath( fromStrings ) );
        /*
         *  test for "lollipop" structure - should be NOT_UNIQUE
         */
        links = new MraldDijkstra( lollipopStructure );
        assertEquals( MraldDijkstra.JOIN_PATH_NOT_UNIQUE, links.testJoinPath( fromStrings ) );
        /*
         *  test for "racetrack" structure - should be NOT_UNIQUE
         */
        links = new MraldDijkstra( racetrackStructure );
        assertEquals( MraldDijkstra.JOIN_PATH_NOT_UNIQUE, links.testJoinPath( fromStrings ) );
        /*
         *  test for table in from list but not in a link element- should be NOT_COMPLETE_OR_UNIQUE
         */
        fromStrings.add( "Five" );
        links = new MraldDijkstra( lollipopStructure );
        assertEquals( MraldDijkstra.JOIN_PATH_NOT_COMPLETE_OR_UNIQUE, links.testJoinPath( fromStrings ) );
    }


    /**
     *  A unit test for JUnit
     */
    public void testTestUniqueJoinPath()
    {
        /*
         *  test simple strucutre links
         */
        MraldDijkstra links = new MraldDijkstra( simpleStructure );
        assertEquals( MraldDijkstra.JOIN_PATH_OK, links.testUniqueJoinPath() );
        /*
         *  test complex strucutre links
         */
        links = new MraldDijkstra( complexStructure );
        assertEquals( MraldDijkstra.JOIN_PATH_OK, links.testUniqueJoinPath() );
        /*
         *  test circular strucutre links  - this should fail the unique join test.
         */
        links = new MraldDijkstra( circularStructure );
        assertEquals( MraldDijkstra.JOIN_PATH_NOT_UNIQUE, links.testUniqueJoinPath() );
        /*
         *  test unlinked strucutre links - should get OK
         */
        links = new MraldDijkstra( unlinkedStructure );
        assertEquals( MraldDijkstra.JOIN_PATH_OK, links.testUniqueJoinPath() );
        /*
         *  test for table in from list but not in a link element- should get OK
         */
        links = new MraldDijkstra( simpleStructure );
        assertEquals( MraldDijkstra.JOIN_PATH_OK, links.testUniqueJoinPath() );
        /*
         *  test for "lollipop" structure - should get NOT_UNIQUE
         */
        links = new MraldDijkstra( lollipopStructure );
        assertEquals( MraldDijkstra.JOIN_PATH_NOT_UNIQUE, links.testUniqueJoinPath() );
        /*
         *  test for "racetrack" structure - should get NOT_UNIQUE
         */
        links = new MraldDijkstra( racetrackStructure );
        assertEquals( MraldDijkstra.JOIN_PATH_NOT_UNIQUE, links.testUniqueJoinPath() );
        /*
         *  test for "back-joined" structure - should get NOT_UNIQUE
         */
        links = new MraldDijkstra( backJoinedStructure );
        assertEquals( MraldDijkstra.JOIN_PATH_NOT_UNIQUE, links.testUniqueJoinPath() );
    }


    /**
     *  The JUnit setup method
     */
    protected void setUp()
    throws Exception
    {
        super.setUp();
        simpleStructure = createSimpleStructureMsgObject();
        assertTrue( simpleStructure.getLinks().size() == 2 );
        complexStructure = createComplexStructure();
        assertTrue( complexStructure.getLinks().size() == 3 );
        baseStructure = createBaseMsgObject();
        assertTrue( baseStructure.getLinks().size() == 1 );
        circularStructure = createCircularStructure();
        assertTrue( circularStructure.getLinks().size() == 3 );
        unlinkedStructure = createUnlinkedStructure();
        assertTrue( unlinkedStructure.getLinks().size() == 2 );
        lollipopStructure = createLollipopStructure();
        assertTrue( lollipopStructure.getLinks().size() == 4 );
        racetrackStructure = createRacetrackStructure();
        assertTrue( racetrackStructure.getLinks().size() == 4 );
        backJoinedStructure = createBackJoinedStructure();
        assertTrue( backJoinedStructure.getLinks().size() == 2 );
    }


    /**
     *  Helper for setUp(). Simulated table structure looks like <code>T1=T2</code>
     *  where the PK-FK lines are reversed for the two joins between One and Two
     *
     *@return    a MsgObject with two joins between two tables named One and Two
     *      and one join between table Two and a table, Three
     */
    protected MsgObject createBackJoinedStructure()
    {
        MsgObject msg = createBaseMsgObject();
        LinkElement link1 = new LinkElement();
        try
        {
            link1.resetVariables( "PrimaryLink:Two.key~SecondaryLink:One.fkey" );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        msg.addLink( link1 );

        return msg;
    }


    /**
     *  Helper for setUp(). Simulated table structure looks like <code>T1-T2</code>
     *
     *@return    a MsgObject with a single join between two tables named One and
     *      Two
     */
    protected MsgObject createBaseMsgObject()
    {
        MsgObject msg = new MsgObject();
        LinkElement link1 = new LinkElement();
        try
        {
            link1.resetVariables( "PrimaryLink:One.key~SecondaryLink:Two.fkey" );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        msg.addLink( link1 );
        return msg;
    }


    /**
     *  Helper for setUp(). Simulated table structure looks like <code>T1-T2-T3-T1</code>
     *  . This is OK from a completion standpoint, but should fail the
     *  uniqueness criteria of testJoinPath().
     *
     *@return    a MsgObject with joins from One to Two to Three to One.
     *@return    Description of the Return Value
     */
    protected MsgObject createCircularStructure()
    {
        MsgObject msg = createSimpleStructureMsgObject();
        LinkElement link1 = new LinkElement();
        try
        {
            link1.resetVariables( "PrimaryLink:Three.key~SecondaryLink:One.fkey" );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        msg.addLink( link1 );

        return msg;
    }


    /**
     *  Helper for setUp(). Simulated table structure looks like <code>T1=T2-T3</code>
     *
     *@return    a MsgObject with two joins between two tables named One and Two
     *      and one join between table Two and a table, Three
     */
    protected MsgObject createComplexStructure()
    {
        MsgObject msg = createSimpleStructureMsgObject();
        LinkElement link1 = new LinkElement();
        try
        {
            link1.resetVariables( "PrimaryLink:One.other_key~SecondaryLink:Two.other_fkey" );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        msg.addLink( link1 );

        return msg;
    }


    /**
     *  Helper for setUp(). Simulated table structure looks like <code>T0-T1-T2-T3-T1</code>
     *  - it should look like a lollipop :o). This is OK from a completion
     *  standpoint, but should fail the uniqueness criteria of testJoinPath().
     *
     *@return    a MsgObject two pairs of tables, with no link between the
     *      pairs.
     */
    protected MsgObject createLollipopStructure()
    {
        MsgObject msg = createCircularStructure();
        LinkElement link1 = new LinkElement();
        try
        {
            link1.resetVariables( "PrimaryLink:Four.key~SecondaryLink:One.fkey" );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        msg.addLink( link1 );
        return msg;
    }


    /**
     *  Helper for setUp(). Simulated table structure looks like <code>T1-T2-T3-T4-T1</code>
     *  . Bascially a big oval.
     *
     *@return    creates a MsgObject with a single join between two tables named
     *      One and Two and one joing between table Two and a new table, Three
     */
    protected MsgObject createRacetrackStructure()
    {
        MsgObject msg = createSimpleStructureMsgObject();
        LinkElement link1 = new LinkElement();
        try
        {
            link1.resetVariables( "PrimaryLink:Three.key~SecondaryLink:Four.fkey" );
            msg.addLink( link1 );
            link1 = new LinkElement();
            link1.resetVariables( "PrimaryLink:Four.key~SecondaryLink:One.fkey" );
            msg.addLink( link1 );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

        return msg;
    }


    /**
     *  Helper for setUp(). Simulated table structure looks like <code>T1-T2-T3</code>
     *
     *@return    creates a MsgObject with a single join between two tables named
     *      One and Two and one joing between table Two and a new table, Three
     */
    protected MsgObject createSimpleStructureMsgObject()
    {
        MsgObject msg = createBaseMsgObject();
        LinkElement link1 = new LinkElement();
        try
        {
            link1.resetVariables( "PrimaryLink:Two.key~SecondaryLink:Three.fkey" );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        msg.addLink( link1 );

        return msg;
    }


    /**
     *  Helper for setUp(). Simulated table structure looks like <code>T1-T2 T3-T4</code>
     *  (there is no link between the pairs of tables).
     *
     *@return    a MsgObject two pairs of tables, with no link between the
     *      pairs.
     */
    protected MsgObject createUnlinkedStructure()
    {
        MsgObject msg = createBaseMsgObject();
        LinkElement link1 = new LinkElement();
        try
        {
            link1.resetVariables( "PrimaryLink:Three.key~SecondaryLink:Four.fkey" );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        msg.addLink( link1 );
        return msg;
    }
}


