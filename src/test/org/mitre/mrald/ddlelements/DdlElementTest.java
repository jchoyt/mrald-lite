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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.mitre.mrald.ddlelements.InsertElement;


public class DdlElementTest extends TestCase
{
    /*
     * add tests here
     */
    ArrayList currentFieldList;
    InsertElement one, two, three, four;

    public void testGetTable()
    {
        String val = one.getTable();
        assertEquals("TestTable", val);

        val = two.getTable();
        assertEquals("\"Test Table\"", val);

        val = three.getTable();
        assertEquals("gradient-bvalue,\"gradient orientation\"", val);
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
            String[] input2 = {"Table:Test Table~Field:SecondField~Value:testing string~Type:String~Order:2"};
            two.process( input2 );

            three = new InsertElement();
            String[] input3 = {"Table:gradient-bvalue, gradient orientation~Field:bvalue~Order:2~Type:Numeric~SqlThread:21,113"};
            three.process( input3 );

            currentFieldList = new ArrayList();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }

    /**
     *  The JUnit teardown method
     */
    protected void tearDown()
    {
    }


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
        return new TestSuite( DdlElementTest.class );
    }
}


