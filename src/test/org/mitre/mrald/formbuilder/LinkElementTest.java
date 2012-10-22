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



package test.org.mitre.mrald.formbuilder;
import junit.framework.*;

import org.mitre.mrald.control.MsgObject;
import org.mitre.mrald.formbuilder.LinkElement;

/**
 *  Description of the Class
 *
 *@author     jchoyt
 *@created    October 7, 2003
 */
public class LinkElementTest extends TestCase
{
    LinkElement link1, link2;


    /**
     *  The main program for the LinkElementTest class
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
        return new TestSuite( LinkElementTest.class );
    }


    /**
     *  A unit test for JUnit
     */
    public void testGetters()
    {
        assertEquals( "addictives", link1.getPrimaryTable() );
        assertEquals( "ex_date", link1.getPrimaryField() );
        assertEquals( "algorithm", link1.getSecondaryTable() );
        assertEquals( "name", link1.getSecondaryField() );
    }


    /**
     *  A unit test for JUnit
     */
    public void testIgnore()
    {
        assertFalse( link1.ignore() );
        assertTrue( link2.ignore() );
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
        /*
         *  build the links
         */
        link1 = new org.mitre.mrald.formbuilder.LinkElement();
        link2 = new org.mitre.mrald.formbuilder.LinkElement();
        String[] linkFodder = {"Table1:addictives~Field1:ex_date", "Table2:algorithm~Field2:name", "Link:Full"};
        String[] linkFodder2 = {"Table1:addictives~Field1:ex_date", "Table2:algorithm~Field2:name", "Link:Ignore"};
        try
        {
            link1.process( linkFodder );
            link2.process( linkFodder2 );
            MsgObject temp = new MsgObject();
            link1.postProcess( temp, "ignore me - I'm a useless parameter" );
            link2.postProcess( temp, "ignore me - I'm a useless parameter" );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }
}


