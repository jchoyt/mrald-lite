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
package org.mitre.mrald.admin;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.mitre.mrald.control.AbstractStep;
import org.mitre.mrald.control.MsgObject;
import org.mitre.mrald.control.WorkflowStepException;
import org.mitre.mrald.util.Config;
import org.mitre.mrald.util.MraldError;
import org.mitre.mrald.util.MraldException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 *  This class builds the xml file that describes an News xml document.
 * It will be used to process the html nvPairs that exist in the
 * NewsElements, to create the xml document
 *
 *
 *@author     tcornett
 *@created    June 1, 2004
 */
public class NewsBuilder extends AbstractStep
{
    // String TITLE_TAG = "Title";
    Document document;
    Node root;
    MsgObject msg;

    /**
     *  Constructor for the NewsBuilder object
     */
    public NewsBuilder()
    {
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
        root = document.createElement( "news" );
        document.appendChild( root );

    }


    /**
     *  This class outputs an XML Document object to a Result destination. The
     *  output is indented 2 spaces per level.
     *
     *@param  destination      Destination the output should go to.
     *@param  document         Description of the Parameter
     *@exception  IOException  Description of the Exception
     */
    public  void outputXml( Document document, Result destination )
        throws MraldException
    {
       	//Get the NewsElements from msg
	//for each of these elements
	//Build the associated xmldocument node from
	//the Element itself
	TransformerFactory tfactory = TransformerFactory.newInstance();
        try
        {
            Transformer transformer = tfactory.newTransformer();
            transformer.setOutputProperty( "indent", "yes" );
            transformer.setOutputProperty( "{http://xml.apache.org/xslt}indent-amount", "2" );

	    DOMSource source = new DOMSource( document );
	    //Add the nodes from each element here
	    Object[] elements = msg.getWorkingObjects().toArray();

	    for ( int i = 0; i < elements.length; i++ )
	    {
		Node node = ( ( NewsElement ) elements[i] ).getFBNode( document );
		if ( node != null )
		{
			root.appendChild( node );
		}
	    }
            transformer.transform( source, destination );
        }
        catch ( TransformerConfigurationException tce )
        {
            throw new MraldException( tce );
        }
        catch ( TransformerException te )
        {
            throw new MraldException( te );
        }


    }


       /**
     *  This method is part of the AbstractStep interface and it called from the
     *  workflow controller. The expected interface is that the workingObjects
     *  ArrayList in the passed MsgObject will contain a group of
     *  FormBuilderElement objects that need to be processed. The nvPairs object
     *  will hold all the other name/value pairs passed from the form. After
     *  this step, a new xml file representing the form just built will be in
     *  the location specified by the customForms property in Config, and the
     *  user will be redirected to the new form.
     *
     *@param  _msg                       Description of the Parameter
     *@exception  WorkflowStepException  Description of the Exception
     */
    public void execute( MsgObject _msg )
        throws WorkflowStepException
    {
        if(_msg.responseCommitted())
        {
            return;
        }
        msg = _msg;
		String xmlTemp = Config.getProperty("NEWSXML").substring(7);

        File file = new File( xmlTemp );

        StreamResult res = new StreamResult( file );
        try
        {
            outputXml( document, res );
        }
        catch ( MraldException e )
        {
            throw new MraldError(e, msg);
        }
	/*
         *  redirect user
         */

        try
        {
            msg.setRedirect( Config.getProperty( "URL" ) );
        }
        catch ( IOException ie )
        {
            throw new MraldError(ie, msg);
        }
    }

}

