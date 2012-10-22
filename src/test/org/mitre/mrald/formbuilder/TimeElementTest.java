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
import org.mitre.mrald.control.*;
import org.mitre.mrald.parser.*;

import java.util.Properties;
import org.mitre.mrald.formbuilder.TimeElement;

/**
 *  Description of the Class
 *
 *@author     jchoyt
 *@created    September 16, 2003
 */
public class TimeElementTest extends TestCase
{
    /*
     *  add tests here
     */
    MsgObject msg;
    MraldParser parser;

    /**
     *  The main program for the TimeElementTest class
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
        return new TestSuite( TimeElementTest.class );
    }


    /**
     *  A unit test for JUnit
     */
    public void testPreProcessEndDateFirst()
    {
        MsgObject msg = new MsgObject();
        try
        {
            msg.setValue( "FBTime1", "4/1/2003" );
            msg.setValue( "FBTime1~EndDate", "4/2/2003" );
            parser.execute(msg);
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        assertEquals("Should be one workingObject", 1, msg.getWorkingObjects().size());
        TimeElement te = (TimeElement)msg.getWorkingObjects().get(0);
        assertEquals("Checking value", "4/1/2003", te.getNameValues().getValue("Value")[0]);
        assertEquals("Checking EndDate", "4/2/2003", te.getNameValues().getValue("EndDate")[0]);
    }


    /**
     *  A unit test for JUnit
     */
    public void testPreProcessMainFirst()
    {
        MsgObject msg = new MsgObject();
        try
        {
            msg.setValue( "FBTime1~EndDate", "4/2/2003" );
            msg.setValue( "FBTime1", "4/1/2003" );
            parser.execute(msg);
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        assertEquals("Should be one workingObject", 1, msg.getWorkingObjects().size());
        TimeElement te = (TimeElement)msg.getWorkingObjects().get(0);
        assertEquals("Checking value", "4/1/2003", te.getNameValues().getValue("Value")[0]);
        assertEquals("Checking EndDate", "4/2/2003", te.getNameValues().getValue("EndDate")[0]);
    }


    /**
     *  The JUnit setup method
     */
    protected void setUp()
    throws Exception
    {
        super.setUp();
        parser = new MraldParser();
        Properties parserProps = new Properties();
        parserProps.setProperty("FBTime","org.mitre.mrald.formbuilder.TimeElement");
        MraldParser.setBuildables(parserProps);
    }
}


