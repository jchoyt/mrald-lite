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

import java.util.ArrayList;
import junit.framework.*;
import org.mitre.mrald.formbuilder.CheckFormJoinStructureStep;
import org.mitre.mrald.query.FilterElement;
import org.mitre.mrald.control.MsgObject;


/**
 *  Description of the Class
 *
 *@author     jchoyt
 *@created    September 22, 2003
 */
public class CheckFormJoinStructureStepTest extends TestCase
{
    MsgObject msg;
    FilterElement element1, element2, element3, element4;
    org.mitre.mrald.formbuilder.LinkElement link1, link2;
    CheckFormJoinStructureStep step;

    /**
     *  The main program for the CheckFormJoinStructureStepTest class
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
        return new TestSuite( CheckFormJoinStructureStepTest.class );
    }


    public void testConvertLinks()
    {
        step.setMsg(msg);
        step.convertLinks(msg);
        assertEquals("The LinkElement wasn't converted and stored properly", 1, msg.getLinks().size());
    }


    /**
     *  A unit test for JUnit
     */
    public void testReapTables()
    {
        step.setMsg(msg);
        ArrayList tables = step.reapTables(msg);
        assertEquals(2, tables.size());
        assertTrue(tables.contains("table"));
        assertTrue(tables.contains("OtherTable"));
    }


    /**
     *  The JUnit setup method
     */
    protected void setUp()
    throws Exception
    {
        super.setUp();
        msg = new MsgObject();
        element1 = new FilterElement();
        element2 = new FilterElement();
        element3 = new FilterElement();
        element4 = new FilterElement();
        link1 = new org.mitre.mrald.formbuilder.LinkElement();
        link2 = new org.mitre.mrald.formbuilder.LinkElement();
        String[] fodder1 = {"Table:table~Fie ld:field~Operator:=",
                "Value:value1~Type:Numeric"};
        String[] fodder2 = {"Table:OtherTable~Field:field~Operator:>=",
                "Value:value1~Type:String", "Synomyn:syn~value2"};
        String[] fodder3 = {"Table:table~Field:field~Operator:IS NULL",
                "Type:Date"};
        String[] fodder4 = {"Table:table~Field:field~Operator:IN",
                "Value:value1,v2,v3,v4~Type:Date"};
        String[] linkFodder = {"Table1:addictives~Field1:ex_date",  "Table2:algorithm~Field2:name", "Link:Full"};
        String[] linkFodder2 = {"Table1:addictives~Field1:ex_date",  "Table2:algorithm~Field2:name", "Link:Ignore"};
        try
        {
            element1.process( fodder1 );
            element2.process( fodder2 );
            element3.process( fodder3 );
            element4.process( fodder4 );
            link1.process(linkFodder);
            link2.process(linkFodder2);
            msg.getWorkingObjects().add(element1);
            msg.getWorkingObjects().add(element2);
            msg.getWorkingObjects().add(element3);
            msg.getWorkingObjects().add(element4);
            msg.getWorkingObjects().add(link1);
            msg.getWorkingObjects().add(link2);
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        step = new CheckFormJoinStructureStep();
    }
}


