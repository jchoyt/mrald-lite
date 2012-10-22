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
package org.mitre.mrald.graphics;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;


/**
 *  methods originally from EssaiPostURLConnection
 *  found at jguru.com
 *
 *@author     jchoyt
 *@created    June 21, 2005
 */
public class AppletComm
{
    /**
     *  Creates a new instance of
     */
    public final static String boundary = "||--<<<---MrAlD-RoCkS---->>>---||";


    /**
     *  Constructor for the AppletComm object
     */
    public AppletComm() { }


    /**
     *  The main program for the AppletComm class
     *
     *@param  args  The command line arguments
     */
    public static void main( String[] args )
    {
//        AppletComm comm = new AppletComm();
        try
        {
            OutputStream out = new FileOutputStream( "settings.xml" );
            uploadFile( new File( args[0] ), "FileUtil.jsp" );
            out.close();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }



    /**
     *  Description of the Method
     *
     *@param  fileToUpload  Description of the Parameter
     */
    public static void uploadFile( File fileToUpload , String fileFwd)
    {
        try
        {
            URL targetUrl = AppletComm.class.getResource("/"+fileFwd);
            URLConnection conn = targetUrl.openConnection();
            conn.setDoOutput( true );
            conn.setDoInput( true );
            conn.setUseCaches( false );
            conn.setRequestProperty( "Content-type", "multipart/form-data; boundary=" + boundary );
            conn.setRequestProperty( "Referrer", "a friend" );
            conn.setRequestProperty( "Cache-Control", "no-cache" );

            OutputStream out = conn.getOutputStream();
            out.write( ("--" + boundary).getBytes() );
            writeParam( "fileName", "settings.xml", out );
            writeFile( "props", fileToUpload, out, conn );
            out.write( "--".getBytes() );
            out.flush();
            out.close();

        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
    }

    /**
     *  Description of the Method
     *
     *@param  fileToUpload  Description of the Parameter
     */
    public static void uploadFile( ByteArrayOutputStream bos , URL baseURL, String fileFwd, String fileName)
    {
        try
        {
        	System.out.println("AppletComm: upload File");

        	if (baseURL == null)
        		baseURL =new URL("http://127.0.0.1:8080/");

        	URL targetUrl = new URL(baseURL + "/"+fileFwd);

        	System.out.println("AppletComm : upload File : URL: " + targetUrl.toString());

        	//URL targetUrl = new URL("http://127.0.0.1:8080/uploadTest.jsp");
            if (targetUrl == null)
            {
            	System.out.println("Cannot find the Resource :" + fileFwd);
            }
            else
            {
            	System.out.println("Found the Resource :" + fileFwd);
            	System.out.println("Found the Resource URL :" + targetUrl);

            }
            URLConnection conn = targetUrl.openConnection();
            System.out.println("upload File - 1");

            conn.setDoOutput( true );
            conn.setDoInput( true );
            conn.setUseCaches( false );
            conn.setRequestProperty( "Content-type", "multipart/form-data; boundary=" + boundary );
            conn.setRequestProperty( "Referrer", "a friend" );
            conn.setRequestProperty( "Cache-Control", "no-cache" );

            System.out.println("upload File - 2");

            OutputStream out = conn.getOutputStream();
            out.write( ("--" + boundary).getBytes() );
            writeParam( "fileName", fileName, out );
            writeFile( "settings", fileName, bos, out );
            out.write( "--".getBytes() );
            out.flush();

            System.out.println("upload File. Outputstream : " + out.toString());

            out.close();


            InputStream in = conn.getInputStream();
            BufferedInputStream inBuf = new BufferedInputStream(in);

            StringWriter outString = new StringWriter();
            int i=0;
            while((i=inBuf.read()) != -1)
            {
            	outString.write(i);
            }
            System.out.println("IN Buf: " + outString.toString());
            inBuf.close();
            System.out.println("upload File Complete- 3");


        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
    }

    /**
     *  Zips a file and writes it to the OutputStream provided.
     *
     *@param  name        Description of the Parameter
     *@param  out         Description of the Parameter
     *@param  uploadFile  Description of the Parameter
     */
    private static void writeFile( String name, String fileName, ByteArrayOutputStream bos,OutputStream out )
    {
        try
        {

            out.write( ( "\r\nContent-Disposition: form-data; name=\"" + name + "\"; filename=\""
                 + fileName+ "\"" ).getBytes() );
            out.write( ( "\r\nContent-type: application/octet-stream" + "\r\n\r\n" ).getBytes() );
            System.out.println( "bos size: 1: " + bos.size() );

            bos.writeTo( out );
            System.out.println( "bos output : " + bos.toString() );

            bos.close();
            System.out.println( "bos size: 2: " + bos.size() );
            out.write( ( "\r\n" + "--" + boundary ).getBytes() );
            System.out.println("Finished");
        }
        catch ( IOException e )
        {
        	System.out.println("Error:" + e.getMessage());
            e.printStackTrace();
        }
    }
    /**
     *  Zips a file and writes it to the OutputStream provided.
     *
     *@param  name        Description of the Parameter
     *@param  out         Description of the Parameter
     *@param  uploadFile  Description of the Parameter
     */
    private static void writeFile( String name, File uploadFile, OutputStream out,URLConnection conn )
    {
        try
        {
            if ( !uploadFile.exists() )
            {
                System.out.println( "The file does not exist." );
            }
            out.write( ( "\r\nContent-Disposition: form-data; name=\"" + name + "\"; filename=\""
                 + uploadFile.getName() + "\"" ).getBytes() );
            out.write( ( "\r\nContent-type: application/octet-stream" + "\r\n\r\n" ).getBytes() );
            long size = uploadFile.length();
            FileInputStream fis = new FileInputStream( uploadFile );
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            //GZIPOutputStream zout = new GZIPOutputStream( bos );
            byte[] buffer = new byte[1024];
            long read = 0;
            int amountRead = fis.read( buffer );
            while ( amountRead != -1 )
            {
                read += amountRead;
                bos.write( buffer, 0, amountRead );
                if (bos.size()>0) System.out.println( "Percent Done: " + ( double ) read / size +
                        " amount read: " + read);
                /* if ( read > 10e6 )
                {
                    try{Thread.sleep(10*1000);}catch(Exception e){}
                } */
                // if (bos.size()>0) System.out.println( " bos size: " + bos.size() );
                amountRead = fis.read( buffer );
                bos.writeTo( out );
                out=conn.getOutputStream();
                bos.reset();
            }

            // System.out.println( "bos size: " + bos.size() );
            bos.writeTo( out );
            bos.close();
            // System.out.println( "bos size: " + bos.size() );
            fis.close();
            out.write( ( "\r\n" + "--" + boundary ).getBytes() );
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
    }

    /**
     *  Description of the Method
     *
     *@param  name   Description of the Parameter
     *@param  value  Description of the Parameter
     *@param  out    Description of the Parameter
     */
    public static void writeParam( String name, String value, OutputStream out )
    {
        try
        {
            out.write( ( "\r\nContent-Disposition: form-data; name=\"" + name + "\"\r\n\r\n" ).getBytes() );
            out.write( value.getBytes() );
            out.write( ( "\r\n" + "--" + boundary ).getBytes() );
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
    }
}

