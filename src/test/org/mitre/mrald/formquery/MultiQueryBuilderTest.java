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



package test.org.mitre.mrald.formquery;

import java.util.ArrayList;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.mitre.mrald.control.MsgObject;
import org.mitre.mrald.multiquery.MultiQueryBuilder;
import org.mitre.mrald.parser.ParserElement;
import org.mitre.mrald.query.LinkElement;
import org.mitre.mrald.query.SelectElement;

/**
 *  Description of the Class
 *
 *@author     jchoyt
 *@created    December 1, 2002
 */
public class MultiQueryBuilderTest extends TestCase
{
    MultiQueryBuilder builder;
    MsgObject msg;
    ArrayList<ParserElement> parserElements;


    /**
     *  The main program for the MultiQueryBuilderTest class
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
        return new TestSuite( MultiQueryBuilderTest.class );
    }


    /**
     *  A unit test for JUnit
     */
    public void testExecute()
    {
        //System.out.println( msg.toString() );
        try
        {
            builder.execute( msg );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            fail();
        }
        String query1 = msg.getQuery()[0];
        /*
         *  test that only tables 1 and 3 are in query 1
         */
        assertTrue( query1.indexOf( "FirstTable" ) != -1 );
        assertTrue( query1.indexOf( "ThirdTable" ) != -1 );
        assertFalse( query1.indexOf( "SecondTable" ) != -1 );
        /*
         *  test that only the "FirstTable.id" link is in query 1
         */
        assertTrue( query1.indexOf( "FirstTable.id" ) != -1 );
        assertFalse( query1.indexOf( "SecondTable.id" ) != -1 );
        assertFalse( query1.indexOf( "SecondTable.other_id" ) != -1 );

        String query2 = msg.getQuery()[1];
        /*
         *  test that only tables 2 and 3 are in query 2
         */
        assertFalse( query2.indexOf( "FirstTable" ) != -1 );
        assertTrue( query2.indexOf( "ThirdTable" ) != -1 );
        assertTrue( query2.indexOf( "SecondTable" ) != -1 );
        /*
         *  test that only the "SecondTable.id" link is in query 2
         */
        assertFalse( query2.indexOf( "FirstTable.id" ) != -1 );
        assertTrue( query2.indexOf( "SecondTable.id" ) != -1 );
        assertFalse( query2.indexOf( "SecondTable.other_id" ) != -1 );
    }


    /**
     *  A unit test for JUnit
     */
    public void testOrderSqlElements()
    {
    }



    /**
     *  The JUnit setup method
     */
    protected void setUp()
    throws Exception
    {
        super.setUp();
        msg = new MsgObject();
        msg.setValue( "linking", "unlinked" );
        builder = new MultiQueryBuilder();
        parserElements = new ArrayList<ParserElement>();
        try
        {
            /*
             *  should show in query 1
             */
            SelectElement ele = new SelectElement();
            String[] input = {"Table:FirstTable~Field:attribute_id~Value:1~Order:1~SqlThread:1"};
            ele.process( input );
            parserElements.add( ele );
            /*
             *  should show in query 2
             */
            ele = new SelectElement();
            input[0] = "Table:SecondTable~Field:FirstField~Value:1~Order:1~SqlThread:2";
            ele.process( input );
            parserElements.add( ele );
            /*
             *  should show in all queries
             */
            ele = new SelectElement();
            input[0] = "Table:ThirdTable~Field:FirstField~Value:1~Order:1~SqlThread:all";
            ele.process( input );
            parserElements.add( ele );
            /*
             *  link between tables 1 and 2
             */
            LinkElement link1 = new LinkElement();
            try
            {
                link1.resetVariables( "PrimaryLink:FirstTable.id~SecondaryLink:ThirdTable.attribute_id~SqlThread:1" );
            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }
            msg.addLink( link1 );
            /*
             *  link between tables 2 and 3
             */
            link1 = new LinkElement();
            try
            {
                link1.resetVariables( "PrimaryLink:SecondTable.id~SecondaryLink:ThirdTable.FirstField~SqlThread:2" );
            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }
            msg.addLink( link1 );
            /*
             *  second link between tables 2 and three, but given only to thread 1 - shouldn't show in query 2
             */
            link1 = new LinkElement();
            try
            {
                link1.resetVariables( "PrimaryLink:SecondTable.other_id~SecondaryLink:ThirdTable.OtherField~SqlThread:1" );
            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }
            msg.addLink( link1 );

            msg.setWorkingObjects( parserElements );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            fail();
        }
    }
}


