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
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import junit.framework.*;
import org.mitre.mrald.formbuilder.CategoricalFilterElement;
import org.mitre.mrald.util.MraldError;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *  Description of the Class
 *
 *@author     jchoyt
 *@created    December 21, 2004
 */
public class CategoricalFilterElementTest extends TestCase
{
    Document document;
    CategoricalFilterElement goodFilter;
    CategoricalFilterElement noListColumnFilter;


    /**
     *  A unit test for JUnit
     */
    public void testGetFBHtml()
    {
        assertTrue( true );
    }


    /**
     *  A unit test for JUnit
     */
    public void testGetFBNode()
    {
        Node node = noListColumnFilter.getFBNode( document );
        assertTrue( "the node with no list-column should not be build", node == null );
        node = goodFilter.getFBNode( document );
        assertFalse( goodFilter.toString(), node == null );
        NodeList children = node.getChildNodes();
        assertEquals( "goodFilter has bad node", 8, children.getLength() );
        ArrayList names = getNodeNames( children );
        String name = "list-column";
        assertTrue( name, names.contains( name ) );
        name = "label";
        assertTrue( name, names.contains( name ) );
        name = "table";
        assertTrue( name, names.contains( name ) );
        name = "operator";
        assertTrue( name, names.contains( name ) );
        name = "column";
        assertTrue( name, names.contains( name ) );
        name = "category-table";
        assertTrue( name, names.contains( name ) );
        name = "category-field";
        assertTrue( name, names.contains( name ) );
        name = "default-value";
        assertTrue( name, names.contains( name ) );
    }


    /**
     *  Gets the nodeNames attribute of the CategoricalFilterElementTest object
     *
     *@param  children  Description of the Parameter
     *@return           The nodeNames value
     */
    protected ArrayList<String> getNodeNames( NodeList children )
    {
        ArrayList<String> ret = new ArrayList<String>();
        for ( int i = 0; i < children.getLength(); i++ )
        {
            ret.add( ( children.item( i ) ).getNodeName() );
        }
        return ret;
    }


    /**
     *  The JUnit setup method
     *
     *@exception  Exception  Description of the Exception
     */
    protected void setUp()
        throws Exception
    {
        super.setUp();
        /*
         *  build the filters
         */
        goodFilter = new CategoricalFilterElement();
        noListColumnFilter = new CategoricalFilterElement();
        String[] filterFodder = {"DropDownLabel:dateString~label:Examination Date~Table:addictives~Field:ex_date~Operator:=",
                "CategoryTable:dateLookup~CategoryField:date~DefaultValue:today"};
        String[] filterFodder2 = {"DropDownLabel:~label:Examination Date~Table:addictives~Field:ex_date~Operator:=",
                "CategoryTable:dateLookup~CategoryField:date~DefaultValue:today"};
        try
        {
            goodFilter.process( filterFodder );
            noListColumnFilter.process( filterFodder2 );
            // MsgObject temp = new MsgObject();
            // goodFilter.postProcess( temp, "ignore me - I'm a useless parameter" );
            // noListColumnFilter.postProcess( temp, "ignore me - I'm a useless parameter" );

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder;
            try
            {
                builder = factory.newDocumentBuilder();
            }
            catch ( ParserConfigurationException e )
            {
                throw new MraldError( e );
            }
            document = builder.newDocument();
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


    /**
     *  The main program for the CategoricalFilterElementTest class
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
        return new TestSuite( CategoricalFilterElementTest.class );
    }
}

