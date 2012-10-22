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



package test.org.mitre.mrald.output;

import java.io.*;
import junit.framework.*;
import org.mitre.mrald.output.GenerateXMLOutput;
import org.mitre.mrald.output.XMLOutput;
import org.mitre.mrald.util.Config;

/**
 *  Description of the Class
 *
 *@author     JCHOYT
 *@created    June 4, 2003
 */
public class GenerateXMLOutputTest extends TestCase
{
    /**
    * Since we're not doing anything with the output of a class
    * we can simply redirect its output to /dev/null
    * with a null output stream this will be platform independant
    */
   public class NullOutputStream extends OutputStream {
       private boolean closed = false;
       public void write(int b) throws IOException {
          if (closed) throw new IOException("Write to closed stream");
       }
       public void write(byte[] data, int offset, int length)
                throws IOException {
          if (data == null) throw new NullPointerException("data is null");
          if (closed) throw new IOException("Write to closed stream");
       }
       public void close() {
           closed = true;
       }
   }


    /**
     *  The main program for the GenerateXMLOutputTest class
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
        return new TestSuite( GenerateXMLOutputTest.class );
    }


    /**
     *  A unit test for JUnit
     */
    public void testInitialization()
    {
        try
        {
//            boolean bStyled = false;
            Writer outFile = new PrintWriter( new NullOutputStream( ) );
            float mbLimit = 0.1f;
            int lineLimit = 100;


            /*GenerateXMLOutput xg = */new GenerateXMLOutput( outFile, lineLimit, mbLimit, new XMLOutput(), false );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }


    /*
     *  add tests here
     */
    /**
     *  The JUnit setup method
     */
    protected void setUp()
    throws Exception
    {
        super.setUp();
    }


    /**
     *  The JUnit teardown method
     */
    protected void tearDown()
    throws Exception
    {
        super.tearDown();
 //       File killit = new File( "delete.me" );
 //       if ( killit.exists() )
 //       {
 //           killit.delete();
 //       }
    }
}


