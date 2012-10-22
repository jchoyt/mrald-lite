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
import java.util.Iterator;
import java.util.List;
import junit.framework.*;
import org.mitre.mrald.control.MsgObject;
import org.mitre.mrald.ddlbuilder.InsertBuilder;
import org.mitre.mrald.ddlelements.InsertElement;
import org.mitre.mrald.ddlelements.InsertSequenceElement;
import org.mitre.mrald.parser.ParserElement;
import org.mitre.mrald.query.FilterElement;
import org.mitre.mrald.query.SelectElement;
import org.mitre.mrald.util.MraldException;

/**
 *  Description of the Class
 *
 *@author     jchoyt
 *@created    December 1, 2002
 */
public class InsertBuilderTest extends TestCase
{
    InsertBuilder builder;
    ArrayList<InsertElement> insertElements;
    MsgObject msg;
    ArrayList<ParserElement> parserElements;
    ArrayList<InsertElement> validElements;


    /**
     *  The main program for the InsertBuilderTest class
     *
     *@param  args  The command line arguments
     */
    public static void main(String[] args)
    {
        junit.textui.TestRunner.run(suite());
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
        return new TestSuite(InsertBuilderTest.class);
    }


    /**
     *  A unit test for JUnit
     */
    public void testBuildDdl()
    {
        try
        {
            String ddl[] = builder.buildDdl(validElements);
            String query0 = "Insert into TestTable ( FirstField, SecondField ) values ( 1, 'testing string' )";
            assertEquals("Query0 didn't match", query0, ddl[0]);
        } catch (MraldException e)
        {
            e.fillInStackTrace();
        }
    }



    /**
     *  A unit test for JUnit
     */
    public void testExecute()
    {
        try
        {
            builder.execute(msg);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        String query0 = "Insert into OtherTestTable ( NoField, sequence_field ) values ( 256.3652, 6 )";
        assertEquals("Query was not built and inserted into the MsgObject", query0, msg.getQuery()[0]);

        String query1 = "Insert into TestTable ( FirstField, SecondField ) values ( 1, 'testing string' )";
        assertEquals("Query was not built and inserted into the MsgObject", query1, msg.getQuery()[1]);

    }


    /**
     *  A unit test for JUnit
     */
    public void testExtractInsertElements()
    {
        List c = builder.extractInsertElements(parserElements);
        Object nextObject;
        Iterator iter = c.iterator();
        while (iter.hasNext())
        {
            nextObject = iter.next();
            if (!(nextObject instanceof InsertElement))
            {
                fail(nextObject.toString() + " was extracted, but isn't an InsertElement");
            }
        }
        iter = parserElements.iterator();
        /*while (iter.hasNext())
        {
            nextObject = iter.next();
            if (nextObject instanceof InsertElement)
            {
                fail("parserElements contents: " + parserElements.toString() + "\n" + nextObject.toString() + " was not extracted, but should have been because it is an InsertElement.   Type was: " + ((ParserElement) nextObject).getElementType());
            }
        }
        assertEquals("Didn't remove the InsertElements from the supplied ArrayList correctly", 3, parserElements.size());
        assertEquals("Didn't add the InsertElements into the return Collection correctly", 6, c.size());*/
    }


    /**
     *  A unit test for JUnit
     */
    public void testRemoveInvalidElements()
    {
        try
        {

            builder.removeInvalidElements(insertElements);
            assertEquals("It appears the invalid InsertElement was not extracted", 3, insertElements.size());
        } catch (MraldException e)
        {
            e.fillInStackTrace();
        }
    }


    /**
     *  The JUnit setup method
     */
    protected void setUp()
    throws Exception
    {
        super.setUp();
        builder = new InsertBuilder();
        parserElements = new ArrayList<ParserElement>();
        insertElements = new ArrayList<InsertElement>();
        validElements = new ArrayList<InsertElement>();
        try
        {
            InsertElement ele = new InsertElement();
            String[] input = {"Table:TestTable~Field:FirstField~Value:1~Order:1"};
            ele.process(input);
            parserElements.add(ele);
            insertElements.add(ele);
            validElements.add(ele);

            /*
             *  - causing problems - how to remove value for duplicates in buildFieldList AND buildValueList
             *  /add duplicate to one above
             *  ele = new InsertElement();
             *  ele.process( input );
             *  parserElements.add( ele );
             *  insertElements.add( ele );
             */
            ele = new InsertElement();
            input[0] = "Table:TestTable~Field:SecondField~Value:testing string~Type:String~Order:2";
            ele.process(input);
            parserElements.add(ele);
            insertElements.add(ele);
            validElements.add(ele);

            ele = new InsertElement();
            input[0] = "Table:TestTable~Field:ThirdField~Order:3";
            ele.process(input);
            parserElements.add(ele);
            insertElements.add(ele);

            ele = new InsertElement();
            input[0] = "Table:OtherTestTable~Field:ThirdField~Order:5";
            ele.process(input);
            parserElements.add(ele);
            insertElements.add(ele);

            ele = new InsertElement();
            input[0] = "Table:OtherTestTable~Field:NoField~Value:256.3652~Order:1";
            ele.process(input);
            parserElements.add(ele);

            SelectElement sel = new SelectElement();
            input[0] = "Table:ASQPARRGMT~Field:OAGATGDATETOD~Order:28";
            sel.process(input);
            parserElements.add(sel);

            sel = new SelectElement();
            input[0] = "Table:ETMSTZ~Field:TRACKPOSCORD~Order:42";
            sel.process(input);
            parserElements.add(sel);

            FilterElement fil = new FilterElement();
            input[0] = "Table:FLIGHT~Field:CARRIER~Operator:=~Value:none";
            fil.process(input);
            parserElements.add(fil);

            /* add an InsertSequenceElement - this is a valid object */
            InsertSequenceElement sele = new InsertSequenceElement();
            input[0] = "Table:OtherTestTable~Field:sequence_field~Type:Numeric~Value:6~Order:6";
            sele.process(input);
            parserElements.add(sele);
            insertElements.add(sele);
            validElements.add(sele);

            msg = new MsgObject();
            msg.setWorkingObjects(parserElements);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}


