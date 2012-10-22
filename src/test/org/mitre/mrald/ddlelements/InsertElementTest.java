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

import java.util.ArrayList;
import java.util.Properties;
import junit.framework.*;
import org.mitre.mrald.control.MsgObject;
import org.mitre.mrald.ddlelements.InsertElement;
import org.mitre.mrald.parser.MraldParser;
import org.mitre.mrald.util.MraldException;

/**
 *  Description of the Class
 *
 *@author     jchoyt
 *@created    November 27, 2002
 */
public class InsertElementTest extends TestCase
{
    /*
     *  add tests here
     */
    ArrayList<String> currentFieldList;
    InsertElement one, two, three, four;


    /**
     *  A unit test for JUnit
     */
    public void testMulitpleTables()
    {
        MsgObject msg = new MsgObject();
        msg.setValue( "InsertBValue11 ", "Table:gradient_bvalue, gradient_orientation,gradient_orientation , gradient_orientation ,gradient_orientation,gradient_orientation,gradient_orientation,gradient_orientation,gradient_orientation,gradient_orientation~Field:bvalue~Order:2~Type:Numeric~SqlThread:21,113,123,133,143,153,163,173,183,193" );

        MraldParser parser = new MraldParser();
        Properties parserProps = new Properties();
        parserProps.put( "Insert", "org.mitre.mrald.ddlelements.InsertElement" );
        parserProps.put( "Sequence", "org.mitre.mrald.ddlelements.InsertSequenceElement" );
        MraldParser.setBuildables( parserProps );
        try
        {
            parser.execute( msg );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        assertEquals( 10, msg.getWorkingObjects().size() );
        int bvalue = 0;
        int orientation = 0;
        InsertElement ele;
        for ( int i = 0; i < msg.getWorkingObjects().size(); i++ )
        {
            ele = ( InsertElement ) msg.getWorkingObjects().get( i );
            if ( ele.getTable().equals( "gradient_bvalue" ) )
            {
                bvalue++;
            }
            else if ( ele.getTable().equals( "gradient_orientation" ) )
            {
                orientation++;
            }
        }
        assertEquals( "" + bvalue, 1, bvalue );
        assertEquals( "" + orientation, 9, orientation );
    }


    /**
     *  The main program for the InsertElementTest class
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
        return new TestSuite( InsertElementTest.class );
    }


    /**
     *  A unit test for JUnit
     */
    public void testBuildFieldList()
    {
        /*
         *  TODO: this needs to eventaully drop the ~1 and be handled by the comparator
         */
        String expectedString = "FirstField";
        assertFalse( expectedString + " already in there", currentFieldList.contains( expectedString ) );
        one.buildFieldList( currentFieldList );
        assertEquals( expectedString + " isn't in there", expectedString, currentFieldList.get( 0 ) );

        //test for input 2
        expectedString = "SecondField";
        assertFalse( expectedString + " already in there", currentFieldList.contains( expectedString ) );
        two.buildFieldList( currentFieldList );
        assertEquals( expectedString + " isn't in there", expectedString, currentFieldList.get( 1 ) );

        //test for input 3 - no value
        assertEquals( "There should be two strings in here.", 2, currentFieldList.size() );
        three.buildFieldList( currentFieldList );
        assertEquals( "Three added a string and it shouldn't have - there was no value given", 2, currentFieldList.size() );
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
            expectedString = "'testing string'";
            assertFalse( expectedString + " already in there", currentFieldList.contains( expectedString ) );
            currentFieldList = two.buildValueList( currentFieldList );
            assertEquals( expectedString + " isn't in there", expectedString, currentFieldList.get( 1 ) );

            //test for input 3 - no value
            assertEquals( "There should be two strings in here.", 2, currentFieldList.size() );
            currentFieldList = three.buildValueList( currentFieldList );
            assertEquals( "Three added a string and it shouldn't have - there was no value given", 2, currentFieldList.size() );
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
        try
        {
            one = new InsertElement();
            String[] input1 = {"Table:TestTable~Field:FirstField~Value:1~Order:1"};
            one.process( input1 );

            two = new InsertElement();
            String[] input2 = {"Table:TestTable~Field:SecondField~Value:testing string~Type:String~Order:2"};
            two.process( input2 );

            three = new InsertElement();
            String[] input3 = {"Table:TestTable~Field:ThirdField~Order:3"};
            three.process( input3 );

            four = new InsertElement();
            four.process( input1 );

            currentFieldList = new ArrayList<String>();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }
}


