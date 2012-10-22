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



package test.org.mitre.mrald.parser;

import java.util.ArrayList;
import java.util.Properties;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.mitre.mrald.control.MsgObject;
import org.mitre.mrald.control.WorkflowStepException;
import org.mitre.mrald.parser.MraldParser;
import org.mitre.mrald.query.SqlElements;
import org.mitre.mrald.query.TimeElement;

/**
 *  Description of the Class
 *
 *@author     jchoyt
 *@created    December 22, 2004
 */
public class MraldParserTest extends TestCase
{

    protected MsgObject msg;


    /**
     *  A unit test for JUnit
     */
    public void testProcessNvPairs()
    {
        MraldParser parser = new MraldParser();
        try
        {
            parser.execute( msg );
        }
        catch ( WorkflowStepException e )
        {
            e.printStackTrace();
        }
        ArrayList workingObjects = msg.getWorkingObjects();
        assertEquals( "Should be one here", 1, workingObjects.size() );
        assertEquals( "The element should be a TimeElement",
                new TimeElement().getElementType(),
                ( ( SqlElements ) workingObjects.get( 0 ) ).getElementType() );
        String toStringValue = workingObjects.get( 0 ).toString();
        assertFalse( toStringValue.indexOf( "StartTime=00:00" ) == -1 );
        assertFalse( toStringValue.indexOf( "StartDate=1/1/1997" ) == -1 );
        assertFalse( toStringValue.indexOf( "Table=subject" ) == -1 );
        assertFalse( toStringValue.indexOf( "Field=initial_exam" ) == -1 );
    }


    /**
     *  The JUnit setup method
     */
    protected void setUp()
    {
        Properties parserTargets = new Properties();
        parserTargets.put( "Time", "org.mitre.mrald.query.TimeElement" );
        MraldParser.setBuildables( parserTargets );

        msg = new MsgObject();
        msg.setValue( "Time1", "Table:subject~Field:initial_exam" );
        msg.setValue( "Time1~Day", "" );
        msg.setValue( "Time1~EndDate", "1/1/2001" );
        msg.setValue( "Time1~EndTime", "23:59" );
        msg.setValue( "Time1~Hour", "" );
        msg.setValue( "Time1~Minute", "" );
        msg.setValue( "Time1~Month", "" );
        msg.setValue( "Time1~Second", "" );
        msg.setValue( "Time1~StartDate", "1/1/1997" );
        msg.setValue( "Time1~StartTime", "00:00" );
    }


    /**
     *  The JUnit teardown method
     */
    protected void tearDown()
    {
    }


    /**
     *  The main program for the MraldParserTest class
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
        return new TestSuite( MraldParserTest.class );
    }
}

