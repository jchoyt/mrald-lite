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
import junit.framework.*;
import org.mitre.mrald.ddlelements.*;

/**
 *  Description of the Class
 *
 *@author     jchoyt
 *@created    December 1, 2002
 */
public class DdlElementComparatorTest extends TestCase
{
    DdlElementComparator comp;
    ArrayList currentFieldList;
    InsertElement one, two, three, four, five;


    /**
     *  The main program for the DdlElementComparatorTest class
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
        return new TestSuite( DdlElementComparatorTest.class );
    }


    /**
     *  A unit test for JUnit
     */
    public void testCompare()
    {
        /*
         *  Compares its two arguments for order. Returns a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second.
         *  The implementor must ensure that sgn(compare(x, y)) == -sgn(compare(y, x)) for all x and y. (This implies that compare(x, y) must throw an exception if and only if compare(y, x) throws an exception.)
         *  The implementor must also ensure that the relation is transitive: ((compare(x, y)>0) && (compare(y, z)>0)) implies compare(x, z)>0.
         */
        assertTrue( "Order=1 should come before order=2", comp.compare( one, two ) < 0 );
        assertTrue( "Order=2 should come after order=1", comp.compare( two, one ) > 0 );
        assertTrue( "Identical items should return a 0", comp.compare( three, three ) == 0 );
        assertTrue( "Equal items should return a 0", comp.compare( one, four ) == 0 );
        assertTrue( "Table OtherTestTable should come before TestTable, regardless of Order", comp.compare( five, one ) < 0 );
    }


    /**
     *  A unit test for JUnit
     */
    public void testEquals()
    {
        /*
         *  see testEquals() in InsertElementTest
         */
    }


    /**
     *  The JUnit setup method
     */
    protected void setUp()
    throws Exception
    {
        super.setUp();
        comp = new DdlElementComparator();
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

            five = new InsertElement();
            String[] input5 = {"Table:OtherTestTable~Field:ThirdField~Order:5"};
            five.process( input5 );

            currentFieldList = new ArrayList();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }
}


