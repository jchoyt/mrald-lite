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

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import junit.framework.*;
import org.mitre.mrald.control.MsgObject;
import org.mitre.mrald.parser.MraldParser;
import org.mitre.mrald.parser.ParserElement;
import org.mitre.mrald.query.TimeElement;
import org.mitre.mrald.util.FormTags;

/**
 *  Description of the Class
 *
 *@author     jchoyt
 *@created    December 22, 2004
 */
public class ParserElementTest extends TestCase
{

    protected MsgObject msg;
    protected TimeElement ele;


    /**
     *  A unit test for JUnit
     */
    public void testCollectElementGroup()
    {
        String currentName = "Time1~EndTime";
        String[] groupTags = {
                FormTags.DAY_TAG,
                FormTags.ENABLETIME,
                FormTags.ENDDATE,
                FormTags.ENDTIME,
                FormTags.FUNCTION,
                FormTags.HOUR_TAG,
                FormTags.MINUTE_TAG,
                FormTags.MONTH_TAG,
                FormTags.SECOND_TAG,
                FormTags.STARTDATE,
                FormTags.STARTTIME,
                FormTags.TIME_TAG
                };
        assertEquals( "Checking number of name/value pairs", 10, msg.getSize() );
        ele.collectElementGroup( msg, currentName, groupTags );
        assertEquals( msg.toString(), 9, msg.getSize() );
        assertEquals( 0, ele.getNameValues().getSize() );
        String[] collected = msg.getValue( "Time1" );
        List list = Arrays.asList( collected );
        assertTrue( msg.toString(), list.contains( "Table:subject~Field:initial_exam" ) );
        assertTrue( msg.toString(), list.contains( "EndTime:23:59" ) );

        currentName = "Time1~EndDate";
        ele.collectElementGroup( msg, currentName, groupTags );
        assertEquals( msg.toString(), 8, msg.getSize() );
        assertEquals( 0, ele.getNameValues().getSize() );
        collected = msg.getValue( "Time1" );
        list = Arrays.asList( collected );
        assertTrue( msg.toString(), list.contains( "Table:subject~Field:initial_exam" ) );
        assertTrue( msg.toString(), list.contains( "EndDate:1/1/2001" ) );
        assertTrue( msg.toString(), list.contains( "EndTime:23:59" ) );

        currentName = "Time1";
        ele.collectElementGroup( msg, currentName, groupTags );
        assertEquals( msg.toString(), 1, msg.getSize() );
        collected = msg.getValue( "Time1" );
        list = Arrays.asList( collected );
        assertTrue( msg.toString(), list.contains( "Table:subject~Field:initial_exam" ) );
        assertTrue( msg.toString(), list.contains( "EndTime:23:59" ) );

        String nvs = ele.getNameValues().nameValuesToString();
        assertEquals( 2, ele.getNameValues().getSize() );
        assertTrue( nvs.indexOf( "1/1/1997" ) != -1 );
        assertTrue( nvs.indexOf( "StartDate" ) != -1 );
        assertTrue( nvs.indexOf( "00:00" ) != -1 );
        assertTrue( nvs.indexOf( "StartTime" ) != -1 );
    }


    /**
     *  The JUnit setup method
     */
    protected void setUp()
    {
        Properties parserTargets = new Properties();
        parserTargets.put( "Time", "org.mitre.mrald.query.TimeElement" );
        MraldParser.setBuildables( parserTargets );
        ele = new TimeElement();
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


    public void testSpacedTableNames()
    {
        ParserElement ele = new Pe();
        String[] input = {"Table:Test Table~Field:FirstField~Value:1~Order:1"};
        try
        {
            ele.process(input);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        String nvs = ele.getNameValues().nameValuesToString();
        assertEquals( 4, ele.getNameValues().getSize() );
        assertTrue( nvs, nvs.indexOf( "\"Test Table\"" ) != -1 );
    }

    class Pe extends ParserElement
    {
        public String getElementType()
        {
            return "test";
        }
    }

    /**
     *  The JUnit teardown method
     */
    protected void tearDown()
    {
    }


    /**
     *  The main program for the ParserElementTest class
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
        return new TestSuite( ParserElementTest.class );
    }
}

