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

import junit.framework.*;
import org.mitre.mrald.query.StaticSelectElement;
import java.util.ArrayList;

public class StaticSelectElementTest extends TestCase
{
    /*
     * add tests here
     */
     StaticSelectElement testee, testee2;

    /**
     *  The JUnit setup method
     */
    protected void setUp()
    throws Exception
    {
        super.setUp();
        String[] valueList = {"Table:TableName~Value:right into select", "Order:1"};
        testee = new StaticSelectElement();
        String[] valueList2 = {"Table:TableName~Value:null~As:filler", "Order:1"};
        testee2 = new StaticSelectElement();
        try
        {
            testee.process( valueList );
            testee2.process( valueList2 );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }

    public void testBuildSelect()
    {
        ArrayList<String> receiver = new ArrayList<String>();
        testee.buildSelect(receiver);
        assertTrue(receiver.contains("right into select~1"));
        receiver = new ArrayList<String>();
        testee2.buildSelect(receiver);
        assertTrue(receiver.contains("null AS \"filler\"~1"));
    }

    public void testBuildFrom()
    {
        ArrayList<String> receiver = new ArrayList<String>();
        testee.buildFrom(receiver);
        assertTrue(receiver.contains("TableName"));
    }


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
        return new TestSuite( StaticSelectElementTest.class );
    }
}


