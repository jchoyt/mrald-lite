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




package test.org.mitre.mrald.admin;

import java.util.*;
import junit.framework.*;
import org.mitre.mrald.admin.ErrFileFilter;


public class ErrFileFilterTest extends TestCase
{
    HashMap<String,Boolean> names = new HashMap<String,Boolean>();

    /**
     *  The JUnit setup method
     */
    protected void setUp()
    throws Exception
    {
        super.setUp();
        names.put("mrald1324.err", new Boolean(true));
        names.put("mrald.err", new Boolean(true));
        names.put("mrald106898131436478319.err", new Boolean(true));
        names.put("mral1324.err", new Boolean(false));
        names.put("mrald13m24.err", new Boolean(false));
        names.put("mrald1324.er", new Boolean(false));
        names.put("mrald1324.error", new Boolean(false));
        names.put("mrald1324.txt", new Boolean(false));
        names.put("falbergasted.oi!", new Boolean(false));
    }

    public void testAccept()
    {
        String key;
        Boolean value;
        ErrFileFilter testee = new ErrFileFilter();
        Iterator iter = names.keySet().iterator();
        while ( iter.hasNext() )
        {
            key = ( String ) iter.next();
            value = names.get(key);
            assertEquals(value.booleanValue(), testee.accept(null, key));
        }
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
        return new TestSuite( ErrFileFilterTest.class );
    }
}


