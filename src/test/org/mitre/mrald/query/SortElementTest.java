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

import junit.framework.*;
import org.mitre.mrald.control.MsgObject;
import org.mitre.mrald.query.SortElement;


/**
 *  Description of the Class
 *
 *@author     jchoyt
 *@created    August 9, 2004
 */
public class SortElementTest extends TestCase
{
    /*
     *  add tests here
     */
    SortElement testee1, testee2, testee3, testee4, noValueTestee;


    /**
     *  The JUnit setup method
     */
    protected void setUp()
    throws Exception
    {
        super.setUp();
        testee1 = new SortElement();
        testee2 = new SortElement();
        testee3 = new SortElement();
        testee4 = new SortElement();
        String[] fodder1 = {"Table:table~Field:field~Operator:="};
        String[] fodder2 = {"Field:field"};
        String[] fodder3 = {"Table:table"};
        String[] fodder4 = {""};
        try
        {
            testee1.process( fodder1 );
            testee2.process( fodder2 );
            testee3.process( fodder3 );
            testee4.process( fodder4 );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }


    public void testIsActive()
    {
        testee1.postProcess( new MsgObject(), "dummy"  );
        testee2.postProcess( new MsgObject(), "dummy"  );
        testee3.postProcess( new MsgObject(), "dummy"  );
        testee4.postProcess( new MsgObject(), "dummy"  );
        assertTrue(testee1.getIsActive());
        assertFalse(testee2.getIsActive());
        assertFalse(testee3.getIsActive());
        assertFalse(testee4.getIsActive());
    }

    /**
     *  The main program for the SortElementTest class
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
        return new TestSuite( SortElementTest.class );
    }
}


