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



package test.org.mitre.mrald.ddlbuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.mitre.mrald.control.MsgObject;
import org.mitre.mrald.ddlbuilder.MultiInsertBuilder;
import org.mitre.mrald.ddlelements.InsertElement;
import org.mitre.mrald.ddlelements.MultiInsertElementComparator;
import org.mitre.mrald.parser.MraldParser;
import org.mitre.mrald.parser.ParserElement;
import org.mitre.mrald.query.FilterElement;
import org.mitre.mrald.query.SelectElement;
import org.mitre.mrald.util.Config;
import org.mitre.mrald.util.MraldException;

import com.mockobjects.sql.MockConnection2;
import com.mockobjects.sql.MockDriver;
import com.mockobjects.sql.MockSingleRowResultSet;
import com.mockobjects.sql.MockStatement;

/**
 *  Description of the Class
 *
 *@author     jchoyt
 *@created    July 8, 2004
 */
public class MultiInsertBuilderTest extends TestCase
{
    /*
     *  add tests here
     */
    MultiInsertBuilder builder;
    ArrayList<InsertElement> insertElements;
    MsgObject msg;
    ArrayList<ParserElement> parserElements;
    ArrayList<InsertElement> validElements;


    /**
     *  A unit test for JUnit
     */
    public void testBuildDdl() throws MraldException
    {
        Collections.sort( validElements, new MultiInsertElementComparator() );
        String ddl[] = builder.buildDdl( validElements );
        String query0 = "Insert into TestTable ( FirstField, SecondField, inAll ) values ( 1, 'testing string', 1.618 )";
        assertEquals( "Query0 is : " + query0 + "\nBuilt query is: " + ddl[0], query0, ddl[0] );
    }



    /**
     *  A unit test for JUnit
     */
    public void testExecute()
    {
        try
        {
            builder.execute( msg );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        assertEquals( 3, msg.getQuery().length );

        String query0 = "Insert into TestTable ( FirstField, SecondField, in14, inAll ) values ( 1, 'testing string', 256.3652, 1.618 )";
        assertEquals( "Query was not built and inserted into the MsgObject", query0, msg.getQuery()[0] );

        String query2 = "Insert into OtherTestTable ( last_field, inAll ) values ( 'This should be last', 1.618 )";
        assertEquals( msg.getQuery()[1], query2, msg.getQuery()[1] );

        String query3 = "Insert into OtherTestTable ( in14, inAll ) values ( 256.3652, 1.618 )";
        assertEquals( msg.getQuery()[2], query3, msg.getQuery()[2] );

    }


    /**
     *  A unit test for JUnit
     */
    public void testExtractInsertElements()
    {
        List c = builder.extractInsertElements( parserElements );
        Object nextObject;
        Iterator iter = c.iterator();
        while ( iter.hasNext() )
        {
            nextObject = iter.next();
            if ( !( nextObject instanceof InsertElement ) )
            {
                fail( nextObject.toString() + " was extracted, but isn't an InsertElement" );
            }
        }
        iter = parserElements.iterator();
    }


    /**
     *  The JUnit setup method
     */
    protected void setUp()
    throws Exception
    {
        super.setUp();
        setupMockDatabase();
        msg = new MsgObject();
        builder = new MultiInsertBuilder();
        parserElements = new ArrayList<ParserElement>();
        insertElements = new ArrayList<InsertElement>();
        validElements = new ArrayList<InsertElement>();
        try
        {
            InsertElement ele = new InsertElement();
            String[] input = {"Table:TestTable~Field:FirstField~Value:1~Order:1~SqlThread:1"};
            ele.process( input );
            parserElements.add( ele );
            insertElements.add( ele );
            validElements.add( ele );

            ele = new InsertElement();
            input[0] = "Table:TestTable~Field:SecondField~Value:testing string~Type:String~Order:2~SqlThread:1";
            ele.process( input );
            parserElements.add( ele );
            insertElements.add( ele );
            validElements.add( ele );

            ele = new InsertElement();
            input[0] = "Table:TestTable~Field:ThirdField~Order:3~SqlThread:1";
            ele.process( input );
            parserElements.add( ele );
            insertElements.add( ele );

            ele = new InsertElement();
            input[0] = "Table:OtherTestTable~Field:ThirdField~Order:5~SqlThread:2";
            ele.process( input );
            parserElements.add( ele );
            insertElements.add( ele );

            ele = new InsertElement();
            input[0] = "Table:TestTable,OtherTestTable~Field:in14~Value:256.3652~Order:4~SqlThread:1,4";
            ele.preProcess( msg, "dummy" );
            ele.process( input );
            ele.postProcess( msg, "dummy" );

            SelectElement sel = new SelectElement();
            input[0] = "Table:ASQPARRGMT~Field:OAGATGDATETOD~Order:28~SqlThread:1";
            sel.process( input );
            parserElements.add( sel );

            sel = new SelectElement();
            input[0] = "Table:ETMSTZ~Field:TRACKPOSCORD~Order:42~SqlThread:2";
            sel.process( input );
            parserElements.add( sel );

            FilterElement fil = new FilterElement();
            input[0] = "Table:FLIGHT~Field:CARRIER~Operator:=~Value:none~SqlThread:1";
            fil.process( input );
            parserElements.add( fil );

            /*
             *  add an InsertElement with a negative SqlThread number - this is a valid object
             */
            ele = new InsertElement();
            input[0] = "Table:OtherTestTable~Field:inAll~Type:Numeric~Value:1.618~Order:8~SqlThread:-1";
            ele.process( input );
            ele.postProcess( msg, "dummy for currentName" );
            parserElements.add( ele );
            insertElements.add( ele );
            validElements.add( ele );

            /*
             *  add a third query and check the ordering
             */
            ele = new InsertElement();
            input[0] = "Table:OtherTestTable~Field:last_field~Type:String~Value:This should be last~Order:9~SqlThread:3";
            ele.process( input );
            parserElements.add( ele );
            insertElements.add( ele );
            validElements.add( ele );

            ArrayList<ParserElement> working = msg.getWorkingObjects();
            working.addAll( parserElements );
            msg.setWorkingObjects( working );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }


    /**
     *  Description of the Method
     */
    protected void setupMockDatabase()
    {
        /*
         *  Define the queries that we expect to have sent
         */
        String queryOne = "select max(in123)+1 from OtherTestTable";
        /*
         *  Define the result sets that will be returned for each of those queries
         */
        String[] results = {"6"};
        MockSingleRowResultSet rsOne = new MockSingleRowResultSet();
        rsOne.addExpectedIndexedValues( results );
        /*
         *  Set up the mock result set . . .
         */
        MockStatement mockStatement = new MockStatement();
        /*
         *  . . .  and match up queries with ResultSets
         */
        mockStatement.addExpectedExecuteQuery( queryOne, rsOne );
        /*
         *  match up the Statement with the Connection
         */
        MockConnection2 mockConnection = new MockConnection2();
        mockConnection.setupStatement( mockStatement );
        /*
         *  match up the Connection with the Driver
         */
        MockDriver.myDriver.setupConnect( mockConnection );
        /*
         *  set up Config
         */
        Config.setProperty( "DBDRIVER", "com.mockobjects.sql.MockDriver" );
        /*
         *  the below are filler - it doesn't matter what we put here, but we
         *  need to put SOMETHING in here or Config will barf
         */
        Config.setProperty( "DBLOGIN", "here" );
        Config.setProperty( "DBPASSWORD", "now" );
        Config.setProperty( "DBSERVER", "jdbc:mockdriver:null" );
		Config.setProperty( "BasePath", "dummy" );
        Config.setProperty( "SCHEMA", "public" );
        // Config.setProperty( "LOGFILE", "unitTest.log" );
        // Config.setProperty( "MAILTO", "jchoyt@mitre.org" );
        // Config.setProperty( "SMTPHOST", "localhost" );
        // MetaData.reload();
    }

    /**
     *  The main program for the MultiInsertBuilderTest class
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
        return new TestSuite( MultiInsertBuilderTest.class );
    }


    /**
     *  A unit test for JUnit
     */
    public void testMiserableMsgObject()
    {
        MsgObject msg = buildMiserableMsgObject();
        MraldParser parser = new MraldParser();
        Properties parserProps = new Properties();
        parserProps.put( "Insert", "org.mitre.mrald.ddlelements.InsertElement" );
        parserProps.put( "Sequence", "org.mitre.mrald.ddlelements.InsertSequenceElement" );
        MraldParser.setBuildables( parserProps );
        try
        {
            parser.execute( msg );
            builder.execute( msg );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        /*
         *  the expected queries
         */
        ArrayList<String> expectedQueries = new ArrayList<String>();
        expectedQueries.add( "Insert into gradient_orientation ( bvalue, orientation_no, x, y, z, gradient_name ) values ( 1000, 2, 46, 465, 465, 'test21' )" );
        expectedQueries.add( "Insert into gradient_orientation ( bvalue, orientation_no, x, y, z, gradient_name ) values ( 1000, 3, 987, 654, 321, 'test21' )" );
        expectedQueries.add( "Insert into gradient_orientation ( bvalue, orientation_no, x, y, z, gradient_name ) values ( 1000, 4, 132, 54, 465, 'test21' )" );
        expectedQueries.add( "Insert into gradient_orientation ( bvalue, orientation_no, x, y, z, gradient_name ) values ( 1000, 5, 879, 879654, 31, 'test21' )" );
        expectedQueries.add( "Insert into gradient_orientation ( bvalue, orientation_no, x, y, z, gradient_name ) values ( 1000, 6, 132, 132, 546, 'test21' )" );
        expectedQueries.add( "Insert into gradient_orientation ( bvalue, orientation_no, x, y, z, gradient_name ) values ( 1000, 7, 465, 879, 465, 'test21' )" );
        expectedQueries.add( "Insert into gradient_orientation ( bvalue, orientation_no, x, y, z, gradient_name ) values ( 1000, 8, 321, 213, 213, 'test21' )" );
        expectedQueries.add( "Insert into gradient_orientation ( bvalue, orientation_no, x, y, z, gradient_name ) values ( 1000, 9, 546, 879, 465, 'test21' )" );
        expectedQueries.add( "Insert into gradient_orientation ( bvalue, orientation_no, x, y, z, gradient_name ) values ( 1000, 10, 132, 213, 4, 'test21' )" );
        expectedQueries.add( "Insert into gradient_bvalue ( bvalue, strength, gradient_name ) values ( 1000, 2, 'test21' )" );
        assertEquals( 10, msg.getQuery().length );
        for ( int i = 0; i < msg.getQuery().length; i++ )
        {
            assertTrue( msg.getQuery()[i], expectedQueries.contains( msg.getQuery()[i] ) );
        }
    }

    public MsgObject buildMiserableMsgObject()
    {
        MsgObject msg = new MsgObject();
        msg.setValue( "InsertBValue11", "Table:gradient_bvalue,gradient_orientation,gradient_orientation,gradient_orientation,gradient_orientation,gradient_orientation,gradient_orientation,gradient_orientation,gradient_orientation,gradient_orientation~Field:bvalue~Order:2~Type:Numeric~SqlThread:21,113,123,133,143,153,163,173,183,193");
        msg.setValue( "InsertBValue11", "1000" );
        msg.setValue( "Insert114", "46" );
        msg.setValue( "Insert114", "Table:gradient_orientation~Field:x~Order:4~Type:Numeric~SqlThread:113" );
        msg.setValue( "Insert115", "465" );
        msg.setValue( "Insert115", "Table:gradient_orientation~Field:y~Order:5~Type:Numeric~SqlThread:113" );
        msg.setValue( "Insert116", "465" );
        msg.setValue( "Insert116", "Table:gradient_orientation~Field:z~Order:6~Type:Numeric~SqlThread:113" );
        msg.setValue( "Insert124", "987" );
        msg.setValue( "Insert124", "Table:gradient_orientation~Field:x~Order:4~Type:Numeric~SqlThread:123" );
        msg.setValue( "Insert125", "654" );
        msg.setValue( "Insert125", "Table:gradient_orientation~Field:y~Order:5~Type:Numeric~SqlThread:123" );
        msg.setValue( "Insert126", "321" );
        msg.setValue( "Insert126", "Table:gradient_orientation~Field:z~Order:6~Type:Numeric~SqlThread:123" );
        msg.setValue( "Insert134", "132" );
        msg.setValue( "Insert134", "Table:gradient_orientation~Field:x~Order:4~Type:Numeric~SqlThread:133" );
        msg.setValue( "Insert135", "54" );
        msg.setValue( "Insert135", "Table:gradient_orientation~Field:y~Order:5~Type:Numeric~SqlThread:133" );
        msg.setValue( "Insert136", "465" );
        msg.setValue( "Insert136", "Table:gradient_orientation~Field:z~Order:6~Type:Numeric~SqlThread:133" );
        msg.setValue( "Insert144", "879" );
        msg.setValue( "Insert144", "Table:gradient_orientation~Field:x~Order:4~Type:Numeric~SqlThread:143" );
        msg.setValue( "Insert145", "879654" );
        msg.setValue( "Insert145", "Table:gradient_orientation~Field:y~Order:5~Type:Numeric~SqlThread:143" );
        msg.setValue( "Insert146", "31" );
        msg.setValue( "Insert146", "Table:gradient_orientation~Field:z~Order:6~Type:Numeric~SqlThread:143" );
        msg.setValue( "Insert154", "132" );
        msg.setValue( "Insert154", "Table:gradient_orientation~Field:x~Order:4~Type:Numeric~SqlThread:153" );
        msg.setValue( "Insert155", "132" );
        msg.setValue( "Insert155", "Table:gradient_orientation~Field:y~Order:5~Type:Numeric~SqlThread:153" );
        msg.setValue( "Insert156", "546" );
        msg.setValue( "Insert156", "Table:gradient_orientation~Field:z~Order:6~Type:Numeric~SqlThread:153" );
        msg.setValue( "Insert164", "465" );
        msg.setValue( "Insert164", "Table:gradient_orientation~Field:x~Order:4~Type:Numeric~SqlThread:163" );
        msg.setValue( "Insert165", "879" );
        msg.setValue( "Insert165", "Table:gradient_orientation~Field:y~Order:5~Type:Numeric~SqlThread:163" );
        msg.setValue( "Insert166", "465" );
        msg.setValue( "Insert166", "Table:gradient_orientation~Field:z~Order:6~Type:Numeric~SqlThread:163" );
        msg.setValue( "Insert174", "321" );
        msg.setValue( "Insert174", "Table:gradient_orientation~Field:x~Order:4~Type:Numeric~SqlThread:173" );
        msg.setValue( "Insert175", "213" );
        msg.setValue( "Insert175", "Table:gradient_orientation~Field:y~Order:5~Type:Numeric~SqlThread:173" );
        msg.setValue( "Insert176", "213" );
        msg.setValue( "Insert176", "Table:gradient_orientation~Field:z~Order:6~Type:Numeric~SqlThread:173" );
        msg.setValue( "Insert184", "546" );
        msg.setValue( "Insert184", "Table:gradient_orientation~Field:x~Order:4~Type:Numeric~SqlThread:183" );
        msg.setValue( "Insert185", "879" );
        msg.setValue( "Insert185", "Table:gradient_orientation~Field:y~Order:5~Type:Numeric~SqlThread:183" );
        msg.setValue( "Insert186", "465" );
        msg.setValue( "Insert186", "Table:gradient_orientation~Field:z~Order:6~Type:Numeric~SqlThread:183" );
        msg.setValue( "Insert194", "132" );
        msg.setValue( "Insert194", "Table:gradient_orientation~Field:x~Order:4~Type:Numeric~SqlThread:193" );
        msg.setValue( "Insert195", "213" );
        msg.setValue( "Insert195", "Table:gradient_orientation~Field:y~Order:5~Type:Numeric~SqlThread:193" );
        msg.setValue( "Insert196", "4" );
        msg.setValue( "Insert196", "Table:gradient_orientation~Field:z~Order:6~Type:Numeric~SqlThread:193" );
        msg.setValue( "Insert311", "Table:gradient_orientation~Field:orientation_no~Order:3~Type:Numeric~SqlThread:113~Value:2" );
        msg.setValue( "Insert312", "Table:gradient_orientation~Field:orientation_no~Order:3~Type:Numeric~SqlThread:123~Value:3" );
        msg.setValue( "Insert313", "Table:gradient_orientation~Field:orientation_no~Order:3~Type:Numeric~SqlThread:133~Value:4" );
        msg.setValue( "Insert314", "Table:gradient_orientation~Field:orientation_no~Order:3~Type:Numeric~SqlThread:143~Value:5" );
        msg.setValue( "Insert315", "Table:gradient_orientation~Field:orientation_no~Order:3~Type:Numeric~SqlThread:153~Value:6" );
        msg.setValue( "Insert316", "Table:gradient_orientation~Field:orientation_no~Order:3~Type:Numeric~SqlThread:163~Value:7" );
        msg.setValue( "Insert317", "Table:gradient_orientation~Field:orientation_no~Order:3~Type:Numeric~SqlThread:173~Value:8" );
        msg.setValue( "Insert318", "Table:gradient_orientation~Field:orientation_no~Order:3~Type:Numeric~SqlThread:183~Value:9" );
        msg.setValue( "Insert319", "Table:gradient_orientation~Field:orientation_no~Order:3~Type:Numeric~SqlThread:193~Value:10" );
        msg.setValue( "InsertStrength11", "Table:gradient_bvalue~Field:strength~Order:3~Type:Numeric~SqlThread:21" );
        msg.setValue( "InsertStrength11", "2" );
        msg.setValue( "InsertName ", "test21" );
        msg.setValue( "InsertName ", "Table:gradient~Field:gradient_name~Type:String~Order:1~SqlThread:-1" );
        return msg;
    }

}

