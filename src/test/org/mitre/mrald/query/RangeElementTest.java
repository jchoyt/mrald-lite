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

import java.io.ByteArrayOutputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamResult;
import junit.framework.*;
import org.mitre.mrald.control.MsgObject;
import org.mitre.mrald.formbuilder.XmlFileBuilder;
import org.mitre.mrald.query.RangeElement;
import org.mitre.mrald.util.Config;
import org.w3c.dom.Document;

/**
 *  Description of the Class
 *
 *@author     jchoyt
 *@created    January 28, 2003
 */
public class RangeElementTest extends TestCase
{
    MsgObject msg;
    RangeElement testee;


    /**
     *  The main program for the RangeElementTest class
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
        return new TestSuite( RangeElementTest.class );
    }


    /**
     *  Constructor for the testGetFBNode object
     */
    public void testGetFBNode()
    {
//        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + Config.NEWLINE  +
//                "<range>" + Config.NEWLINE  +
//                "  <table>TableName</table>" + Config.NEWLINE  +
//                "  <column>FieldName</column>" + Config.NEWLINE  +
//                "  <label>LabelName</label>" + Config.NEWLINE  +
//                "</range>"+ Config.NEWLINE;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try
        {
            builder = factory.newDocumentBuilder();
        }
        catch ( ParserConfigurationException e )
        {
            RuntimeException re = new RuntimeException( e.getMessage() );
            re.fillInStackTrace();
            throw re;
        }
        Document document = builder.newDocument();

        document.appendChild( testee.getFBNode( document ) );

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        StreamResult destination = new StreamResult( out );
        try
        {
            XmlFileBuilder.outputXml( document, destination );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        String output = out.toString();
        assertTrue( output.indexOf( "<range>" + Config.NEWLINE ) != -1);
        assertTrue( output.indexOf( "<table>TableName</table>" + Config.NEWLINE  ) != -1);
        assertTrue( output.indexOf( "<column>FieldName</column>" + Config.NEWLINE ) != -1);
        assertTrue( output.indexOf( "<label>LabelName</label>" + Config.NEWLINE ) != -1);
        assertTrue( output.indexOf( "</range>"+ Config.NEWLINE ) != -1);
    }


    /**
     *  The JUnit setup method
     */
    protected void setUp()
    throws Exception
    {
        super.setUp();
        String[] valueList = {"Table:TableName~Field:FieldName", "LabelName"};
        testee = new RangeElement();
        try
        {
            testee.process( valueList );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }
}


