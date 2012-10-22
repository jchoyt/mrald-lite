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
package org.mitre.mrald.formbuilder;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import org.mitre.mrald.control.AbstractStep;
import org.mitre.mrald.control.MsgObject;
import org.mitre.mrald.control.WorkflowStepException;
import org.mitre.mrald.util.Config;
import org.mitre.mrald.util.FBUtils;
import org.mitre.mrald.util.FormTags;
import org.mitre.mrald.util.MraldError;
import org.mitre.mrald.util.MraldException;
import org.mitre.mrald.util.User;
import org.mitre.mrald.util.XSLTranslator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;


/**
 *  This class builds the xml file that describes an MRALD form. It also
 *  provides a way to extract a textual version of the Document for easy output
 *  to a File.
 *
 *@author     jchoyt
 *@created    August 30, 2003
 */
public class XmlFileBuilder extends AbstractStep
{
    protected String TITLE_TAG = "Title";
    protected Document document;
    protected MsgObject msg;
    protected Node root;


    /**
     *  Constructor for the XmlFileBuilder object
     */
    public XmlFileBuilder()
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

    }


    /**
     *  This class outputs an XML Document object to a Result destination. The
     *  output is indented 2 spaces per level.
     *
     *@param  destination         Destination the output should go to.
     *@param  document            Description of the Parameter
     *@exception  MraldException  Description of the Exception
     */
    public static void outputXml( Document document, Result destination )
        throws MraldException
    {
        TransformerFactory tfactory = TransformerFactory.newInstance();
        try
        {
            Transformer transformer = tfactory.newTransformer();
            transformer.setOutputProperty( "indent", "yes" );
            transformer.setOutputProperty( "{http://xml.apache.org/xslt}indent-amount", "2" );
//            Properties props = transformer.getOutputProperties();
            DOMSource source = new DOMSource( document );
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
     *  Adds a feature to the Fields attribute of the XmlFileBuilder object
     */
    public void addRoot()
    {

        String formType = msg.getValue( "formType" )[0];

        //If no formType is supplied then use the default
        if ( ( formType == null ) || formType.equals( "" ) )
        {
            formType = "Mrald";
        }

        root = document.createElement( formType + "Form" );
        document.appendChild( root );
    }


    /**
     *  Adds a feature to the Fields attribute of the XmlFileBuilder object
     */
    public void addFields()
    {

    }


    /**
     *  Adds a feature to the FormElementsNode attribute of the XmlFileBuilder
     *  object
     *
     *@return    Description of the Return Value
     */
    public Node addFormElementsNode()
    {
        Element formElementsNode = document.createElement( "formElements" );
        root.appendChild( formElementsNode );
        return formElementsNode;
    }


    /**
     *  Adds the title Node to the document.
     */
    public void addTitle()
    {
        String title = msg.getValue( TITLE_TAG )[0];
        Element titleElement = document.createElement( "title" );
        Text titleText = document.createTextNode( title );
        titleElement.appendChild( titleText );
        root.appendChild( titleElement );
    }


    /**
     *  Adds the datasource Node to the document.
     *  Add loop for case of multiple datasources
     */
    public void addDatasource()
    {
    	//MultiDB - May be more than one datasource
        String[] datasources = msg.getValue( FormTags.DATASOURCE_TAG );
        for (String datasource: datasources)
        {
	        Element datasourceElement = document.createElement( "datasource" );
	        Text datasourceText = document.createTextNode( datasource );
	        datasourceElement.appendChild( datasourceText );
	        root.appendChild( datasourceElement );
        }
    }

    /**
     *  Checks to see if this is a multi DB form
     *  If it is add an element
     */
    public void addMultiDb()
    {
    	//MultiDB - May be more than one datasource
        String multiDb = msg.getValue( FormTags.MULTI_DB )[0];
        //If there is no multiple databases, then just skip this
        if (multiDb.equals("")) return;

        Element multiDbElement = document.createElement( "multiDb" );
	    Text multiDbText = document.createTextNode( multiDb );
	    multiDbElement.appendChild( multiDbText );
	    root.appendChild( multiDbElement );

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
        if ( _msg.responseCommitted() )
        {
            return;
        }
        msg = _msg;
        addRoot();
        addTitle();
        addDatasource();

        /**MultiDb**/
        addMultiDb();

        /*
         *  add the form elements
         */
        Node formElements = addFormElementsNode();
        addStats( formElements );
        Object[] elements = msg.getWorkingObjects().toArray();
        for ( int i = 0; i < elements.length; i++ )
        {
            Node node = ( ( FormBuilderElement ) elements[i] ).getFBNode( document );
            if ( node != null )
            {
                formElements.appendChild( node );
            }
        }
        /*
         *  save to disk
         */
        User user = ( User ) msg.getReq().getSession().getAttribute( Config.getProperty( "cookietag" ) );
        if ( user == null )
        {
            throw new MraldError( "The user could not be found in the session.  Please pass this on to <a href=\"" + Config.getProperty( "MAILTO" ) + "\">the system administrator.</a>  It is likely the session timeout in your webserver or servlet engine is set too low.", msg );
        }
        String userid = user.getEmail();
        /*
         *  if a formid is specified, use that, otherwise, use System.currentTimeMillis() for the form id
         */
        String newformid = msg.getValue( "formid" )[0];
        String formType = msg.getValue( "formType" )[0];

        String formAccess = msg.getValue( "formaccess" )[0];

        if ( newformid.equals( "" ) )
        {
            newformid = Long.toString( System.currentTimeMillis() );
        }


        if ( formAccess.equals( "" ) )
        {
        	formAccess ="Personal";
        }

        if (formAccess.startsWith("Public"))
        {
        	userid="public";
        }
        String filename = userid + "_" + newformid + ".xml";
        File file = new File( Config.getProperty( "customForms" ), filename );
        StreamResult res = new StreamResult( file );
        try
        {
            outputXml( document, res );
        }
        catch ( MraldException e )
        {
            throw new MraldError( e, msg );
        }
        /*
         *  add to ColumnNames
         */
        File xslFile = new File( Config.getProperty( "BasePath" ) + "/WEB-INF/extractor.xsl" );
        StringBuffer newColumnNames = XSLTranslator.xslTransform( file, xslFile );
        Properties newProps = new Properties();
        InputStream stream = new ByteArrayInputStream( newColumnNames.toString().getBytes() );
        try
        {
            newProps.load( stream );
        }
        catch ( IOException ie )
        {
            throw new MraldError( ie, msg );
        }
        FBUtils.addColumnNames( newProps );
        /*
         *  redirect user
         */
       redirect(newformid, formType, formAccess);
       
    }

    protected void redirect(String newformid, String formType, String formAccess)
    {
    	 try
         {
             msg.setRedirect( Config.getProperty( "BaseUrl" ) + "/FormServer.jsp?formid=" + newformid + "&formType=" + formType + "&formAccess=" + formAccess );
         }
         catch ( IOException ie )
         {
             throw new MraldError( ie, msg );
         }
    }
    /**
     *  Adds a feature to the Stats attribute of the XmlFileBuilder object
     *
     *@param  node  The feature to be added to the Stats attribute
     */
    protected void addStats( Node node )
    {
        Element statsElement = document.createElement( "stat" );
        statsElement.setAttribute( "useStats", "yes" );
        node.appendChild( statsElement );
    }
}

