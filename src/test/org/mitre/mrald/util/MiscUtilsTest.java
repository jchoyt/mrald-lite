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



package test.org.mitre.mrald.util;

import org.mitre.mrald.util.*;
import junit.framework.*;
import org.mitre.mrald.util.MiscUtils;
import java.util.*;


/**
 *  Description of the Class
 *
 *@author     jchoyt
 *@created    September 26, 2005
 */
public class MiscUtilsTest extends TestCase
{

    Map<String, String> stringsAndAnswer = new HashMap<String, String>();

    /*
     *  add tests here
     */
    /**
     *  Constructor for the testClearSemiColon object
     */
    public void testClearSemiColon()
    {
        String out;
        for(Map.Entry<String, String> e : stringsAndAnswer.entrySet())
        {
            out = MiscUtils.clearSemiColon( e.getKey() );
            assertEquals("Should be: " + e.getValue() + Config.NEWLINE, e.getValue(), out);
        }
    }


    /**
     *  The JUnit setup method
     */
    protected void setUp()
    {
        String query = "select * from people; delete from people;";
        String answer = "select * from people";
        stringsAndAnswer.put(query, answer);

        query = "select * from people, commaOnEnd;";
        answer = "select * from people, commaOnEnd";
        stringsAndAnswer.put(query, answer);

        query = "select * from people-- I'm a hacker";
        answer = "select * from people";
        stringsAndAnswer.put(query, answer);

        query = "select * from people -- I'm a hacker";
        answer = "select * from people ";
        stringsAndAnswer.put(query, answer);

        query = "select * from people; -- I'm a hacker";
        answer = "select * from people";
        stringsAndAnswer.put(query, answer);

        query = "select * from people-- I'm a hacker;delete * from people";
        answer = "select * from people";
        stringsAndAnswer.put(query, answer);

        query = "select * from people where hasColon like '%--';";
        answer = "select * from people where hasColon like '%--'";
        stringsAndAnswer.put(query, answer);

        query = "select * from people { comments here } -- I'm a hacker;delete * from people";
        answer = "select * from people ";
        stringsAndAnswer.put(query, answer);

        query = "select * from people /* comments here */;";
        answer = "select * from people ";
        stringsAndAnswer.put(query, answer);

        query = "select * from people where commentField = '{ comments here }' -- I'm a hacker;delete * from people";
        answer = "select * from people where commentField = '{ comments here }' ";
        stringsAndAnswer.put(query, answer);

        query = "select * from people where iHateMakingUpFieldNames = '/* comments here */'";
        answer = query;
        stringsAndAnswer.put(query, answer);

        query = "select * from escapeFromLiteral where field=''''";
        answer = query;
        stringsAndAnswer.put(query, answer);

        query = "select * from escapeFromLiteral where field=''''; --Then Truncate";
        //answer same as the last one
        stringsAndAnswer.put(query, answer);

        query = "select * from escapeFromLiteral where field='''' order by field";
        answer = query;
        stringsAndAnswer.put(query, answer);

    }


    /**
     *  The JUnit teardown method
     */
    protected void tearDown()
    {
    }


    /**
     *  The main program for the MiscUtilsTest class
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
        return new TestSuite( MiscUtilsTest.class );
    }
}


