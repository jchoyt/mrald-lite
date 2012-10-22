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
package org.mitre.mrald.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.jsp.JspException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 *  Command line class to convert MRALD XML form files to HTML forms
 *
 *@author     jchoyt
 *@created    July 15, 2002
 */
public class FormConverter extends java.lang.Object
{
    File[] arrayToProcess = null;
    String xmlFileName = null;
    File xslFile = null;
    String xslFileName = null;


    /**
     *  Constructor for the FormConverter object
     */
    public FormConverter() { }


    /**
     *  Constructor for the FormConverter object
     *
     *@param  args  Description of the Parameter
     */
    public FormConverter( String[] args )
    {
        /*
         *  process args
         */
        processArgs( args );
        /*
         *  get the XSL file
         */
        readXsl( xslFileName );
        /*
         *  build the array of files to process
         */
        if ( xmlFileName == null )
        {
            // build all XML files in current directory
            File parentDir = new File( Config.getProperty( "customForms" ) );
            arrayToProcess = parentDir.listFiles();
        }
        else
        {
            //build only one file or the directory
            if ( xmlFileName.endsWith( "/" ) || xmlFileName.endsWith( "\\" ) )
            {
                //build the entire directory
                File temp = new File( xmlFileName );
                if ( !temp.exists() )
                {
                    throw new MraldError( "Could not find XML file " + temp.getName() );
                }
                arrayToProcess = temp.listFiles();
            }
            else
            {
                //build only one file
                File temp = new File( xmlFileName );
                if ( !temp.exists() )
                {
                    throw new MraldError( "Could not find XML file " + temp.getName() );
                }
                arrayToProcess = new File[1];
                arrayToProcess[0] = temp;
            }
        }
        /*
         *  process the array and build the html files
         */
        writeJsp();
    }


    /**
     *  Description of the Method
     */
    protected void printUsage()
    {
        System.out.println( "To use the FormConverter:" );
        System.out.println( "\tjava -cp <path to FormConverter.class> [options]" );
        System.out.println( "\tOptions include:" );
        System.out.println( "\t\t-usage -> this message" );
        System.out.println( "\t\t-xsl [path to file] -> use this XSL file (path to XSL file must be next arg)" );
        System.out.println( "\t\t-xml [path to file] -> process just this XML file or directory of xml files" );
        System.exit( 1 );
    }


    /**
     *  The main program for the FormConverter class
     *
     *@param  args  The command line arguments
     */
    public static void main( String[] args )
    {
        try
        {
            /*FormConverter ugh = */new FormConverter( args );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }


    /**
     *  Processes the arguements<br>
     *  -xsl [path to file] -> use this XSL file (path to XSL file must be next
     *  arg)<br>
     *  -xml [path to file] -> process just this XML file or directory of xml
     *  files<br>
     *
     *
     *@param  args              Description of the Parameter
     *@exception  JspException  Description of the Exception
     */
    protected void processArgs( String[] args )
    {
        String arg;
        try
        {
            for ( int i = 0; i < args.length; i++ )
            {
                arg = args[i];
                if ( arg.equals( "-xsl" ) )
                {
                    i++;
                    xslFileName = args[i];
                }
                else if ( arg.equals( "-xml" ) )
                {
                    i++;
                    xmlFileName = args[i];
                }
                else if ( arg.equals( "-usage" ) || arg.equals( "-?" ) )
                {
                    printUsage();
                }
            }
        }
        catch ( ArrayIndexOutOfBoundsException e )
        {
            throw new MraldError( e );
        }
    }


    /**
     *  Description of the Method
     *
     *@param  xslLocation       Description of the Parameter
     *@exception  JspException  Description of the Exception
     */
    protected void readXsl( String xslLocation )
    {
        xslFile = new File( xslFileName );
        if ( !xslFile.exists() )
        {
            throw new MraldError( "Could not find XSL file " + xslFile.getName() );
        }
    }


    /**
     *  Description of the Method
     *
     *@exception  JspException  Description of the Exception
     */
    protected void writeJsp()
    {
        for ( int i = 0; i < arrayToProcess.length; i++ )
        {
            if ( !( arrayToProcess[i].getName().endsWith( ".xml" ) ) )
            {
                continue;
            }
            xslTransform( arrayToProcess[i] );
        }
    }


    /**
     *  Description of the Method styel
     *
     *@param  formXml           public String convertAndStoreXml( int userid,
     *      String title ) Description of Parameter
     *@since
     */
    protected void xslTransform( File formXml )
    {
        Document document;
        DocumentBuilderFactory factory =
                DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware( false );
        factory.setValidating( false );
        /*
         *  TODO: fix thsi file name to remove the .xml
         */
        String jspFileName = formXml.toString().substring( 0,
                formXml.toString().indexOf( ".xml" ) ) + ".jsp";
        try
        {
            BufferedReader in = new BufferedReader( new FileReader( formXml ) );
            InputSource datafile = new InputSource( in );
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse( datafile );
            // Use a Transformer for output
            TransformerFactory tFactory =
                    TransformerFactory.newInstance();
            // use precreated xslFile object (File object referring to XSL file)
            StreamSource stylesource = new StreamSource( xslFile );
            Transformer transformer = tFactory.newTransformer( stylesource );
            DOMSource source = new DOMSource( document );
            PrintWriter ret = new PrintWriter( new FileWriter( jspFileName ) );
            StreamResult result = new StreamResult( ret );
            transformer.transform( source, result );
            ret.close();
        }
        catch ( TransformerConfigurationException e )
        {
            throw new MraldError( e );
        }
        catch ( TransformerException e )
        {
            throw new MraldError( e );
        }
        catch ( SAXException e )
        {
            throw new MraldError( e );
        }
        catch ( ParserConfigurationException e )
        {
            throw new MraldError( e );
        }
        catch ( IOException e )
        {
            throw new MraldError( e );
        }
    }
}
