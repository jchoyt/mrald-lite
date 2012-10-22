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

import java.util.ArrayList;
import junit.framework.*;
import org.mitre.mrald.query.SqlElements;

/**
 *  Description of the Class
 *
 *@author     jchoyt
 *@created    March 31, 2003
 */
public class SqlElementsTest extends TestCase
{

    /**
     *  The main program for the SqlElementsTest class
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
        return new TestSuite( SqlElementsTest.class );
    }


    /*
     *  add tests here
     */
    /**
     *  A unit test for JUnit
     *
     *@exception  Exception  Description of the Exception
     */
    public void testInit()
        throws Exception
    {
        String[] filler = {"Order:2~Synomyn:syn~As:dude~Operator:>~Field:FLIGHTNO~Table:ASQPDERIVEDTIME" };
        ConcreteSqlElement testee = new ConcreteSqlElement();
        testee.process( filler );
        assertEquals("table wasn't set properly", "syn", testee.getTable());
        assertEquals("field wasn't set properly", "FLIGHTNO", testee.getField());
        assertEquals("as wasn't set properly", "dude", testee.getAs());
        assertEquals("operator wasn't set properly", ">", testee.getOperator());
        assertEquals("order wasn't set properly", "2", testee.getOrder());
        assertEquals("synonym wasn't set properly", "syn", testee.getSynonym());
        assertEquals("fromTableName wasn't set properly", "ASQPDERIVEDTIME syn", testee.getFromTableName());
    }


    public void testBuildValue()
        throws Exception

        {
        String[] filler = {"Order:2~Synomyn:syn~As:dude~Operator:>~Field:FLIGHTNO~Table:ASQPDERIVEDTIME~Value:explicit", "implicit" };
        ConcreteSqlElement testee = new ConcreteSqlElement();
        testee.process( filler );
        String backToForm = testee.buildValue();
        assertTrue(backToForm, backToForm.contains( "Order:2" ) );
        assertTrue(backToForm, backToForm.contains( "Synomyn:syn" ) );
        assertTrue(backToForm, backToForm.contains( "As:dude" ) );
        assertTrue(backToForm, backToForm.contains( "Operator:>" ) );
        assertTrue(backToForm, backToForm.contains( "Field:FLIGHTNO" ) );
        assertTrue(backToForm, backToForm.contains( "As:dude" ) );
        assertTrue(backToForm, backToForm.contains( "Table:ASQPDERIVEDTIME" ) );
        assertTrue(backToForm, backToForm.contains( "Value:explicit" ) );
        assertTrue(backToForm, backToForm.contains( "Value:implicit" ) );
        assertTrue(backToForm, backToForm.contains( "ElementType:" ) );
    }

    /**
     *  The JUnit setup method
     */
    protected void setUp()
    throws Exception
    {
        super.setUp();
    }
}

/**
 *  Description of the Class
 *
 *@author     jchoyt
 *@created    March 31, 2003
 */
class ConcreteSqlElement extends SqlElements
{

    public ConcreteSqlElement()
    {
        elementType = "ConcreteSqlElement";
    }

    /**
     *  Doesn't do anything - simply returns the passed ArrayList
     *
     *@param  currentFromList            Description of the Parameter
     *@return                            Description of the Return Value
     *@exception  QueryBuilderException  Description of the Exception
     */
    public ArrayList<String> buildFrom( ArrayList<String> currentFromList )
    {
        return currentFromList;
    }


    /**
     *  Doesn't do anything - simply returns the passed ArrayList
     *
     *@param  currentGroupByList         List of Group By parameters
     *@return                            Description of the Returned Value
     *@exception  QueryBuilderException  Description of Exception
     *@since
     */
    public ArrayList<String> buildGroupBy( ArrayList<String> currentGroupByList )
    {
        return currentGroupByList;
    }


    /**
     *  Doesn't do anything - simply returns the passed ArrayList
     *
     *@param  currentOrderBy             Description of Parameter
     *@return                            Description of the Returned Value
     *@exception  QueryBuilderException  Description of Exception
     *@since
     */
    public ArrayList<String> buildOrderBy( ArrayList<String> currentOrderBy )
    {
        return currentOrderBy;
    }


    /**
     *  Doesn't do anything - simply returns the passed ArrayList
     *
     *@param  currentSelectList          Description of Parameter
     *@return                            Description of the Returned Value
     *@exception  QueryBuilderException  Description of Exception
     *@since
     */
    public ArrayList<String> buildSelect( ArrayList<String> currentSelectList )
    {
        return currentSelectList;
    }


    /**
     *  Doesn't do anything - simply returns the passed ArrayList
     *
     *@param  currentWhereList           Description of the Parameter
     *@return                            Description of the Return Value
     *@exception  QueryBuilderException  Description of the Exception
     */
    public ArrayList<String> buildWhere( ArrayList<String> currentWhereList )
    {
        return currentWhereList;
    }
}

