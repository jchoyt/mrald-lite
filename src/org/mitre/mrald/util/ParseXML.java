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

import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.mitre.mrald.control.WorkFlow;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 *  THis object parsers through an XML file and uses to determine the object and
 *  method to call.
 *
 *@author     Gail Hamilton
 *@created    November 13, 2002
 */
public class ParseXML
{
    private static String ROOT_CHILD_TAG = "WfPath";
    private static String ROOT_TAG = "WorkFlow";
    private static String WF_METHOD_ORDER_ATTR = "Order";
    private static String WF_METHOD_TAG = "WfMethod";
    private static String WF_NAME_TAG = "WfName";
    private static String WF_OBJECT_NAME_TAG = "ObjectName";
    private static String WF_OBJECT_TAG = "WfObject";

    private String XMLFile = null;
    //declare the member variables here. Remember to initialize.
//    private boolean mbValidate = false;
    private String rootChildTag = ROOT_CHILD_TAG;
    private String rootTag = ROOT_TAG;

    private HashMap<Integer,Object> wfObjects = new HashMap<Integer,Object>();
    private String wfPathName = null;
    private String workFlowMethodOrder = WF_METHOD_ORDER_ATTR;
    private String workFlowMethodTag = WF_METHOD_TAG;
    private String workFlowNameTag = WF_NAME_TAG;
    private String workFlowObjectNameTag = WF_OBJECT_NAME_TAG;
    private String workFlowObjectTag = WF_OBJECT_TAG;


    /**
     *  Constructor for Import Object Takes the XML Input file as a parameter
     *  Using this object, the import will create the DataModel Object that will
     *  be used to import back into the DataBase.
     *
     *@param  sXMLFile  Description of the Parameter
     */
    public ParseXML( String sXMLFile )
    {
        XMLFile = sXMLFile;
    }


    /**
     *  Method to set the Second Tier Tag.
     */

    public void setRootChildTag()
    {
        rootChildTag = ROOT_CHILD_TAG;
    }


    /**
     *  Method to set the Root Tag.
     */

    public void setRootTag()
    {
        rootTag = ROOT_TAG;
    }


    /**
     *  Method to set the Second Tier Tag.
     */

    public void setWfNameTag()
    {
        workFlowNameTag = WF_NAME_TAG;
    }


    /**
     *  Method to set the Second Tier Tag.
     */

    public void setWfObjectTag()
    {
        workFlowObjectTag = WF_OBJECT_TAG;
    }


    /**
     *  Method to set the name of the Workflow path
     *
     *@param  workFlowPath  The new wfPath value
     */

    public void setWfPath( String workFlowPath )
    {
        wfPathName = workFlowPath;
    }


    /**
     *  THis method prints out the location of the error that occurred during
     *  parsing.
     *
     *@param  saxE  Description of the Parameter
     *@return       The locationString value
     */

    public String getLocationString( SAXParseException saxE )
    {
        String slocation = "public id: " + saxE.getPublicId() + " line number: " +
                saxE.getLineNumber();
        return slocation;
    }


    /**
     *  Method to set the Second Tier Tag.
     *
     *@param  thisNode                 Description of the Parameter
     *@return                          The nextElement value
     *@exception  MraldParseException  Description of the Exception
     */

    public Node getNextElement( Node thisNode )
        throws MraldParseException
    {
        while ( thisNode != null && thisNode.getNodeType() != Element.ELEMENT_NODE )
        {
            thisNode = thisNode.getNextSibling();
        }

        //If null then this is the last
        if ( thisNode == null )
        {

            MraldParseException parsingException = new MraldParseException( "\n No more Element Nodes found" );
            throw parsingException;
        }
        return thisNode;
    }


    /**
     *  Method to set the Second Tier Tag.
     *
     *@return    The rootChildTag value
     */

    public String getRootChildTag()
    {
        return rootChildTag;
    }


    /**
     *  Method to set the Root Tag.
     *
     *@return    The rootTag value
     */

    public String getRootTag()
    {
        return rootTag;
    }


    /**
     *  Method to set the Second Tier Tag.
     *
     *@return    The wfMethodTag value
     */

    public String getWfMethodTag()
    {
        return workFlowMethodTag;
    }


    /**
     *  Method to set the Second Tier Tag.
     *
     *@return    The wfNameTag value
     */

    public String getWfNameTag()
    {
        return workFlowNameTag;
    }


    /**
     *  Method to set the Second Tier Tag.
     *
     *@return    The wfObjectTag value
     */

    public String getWfObjectTag()
    {
        return workFlowObjectTag;
    }


    /**
     *  Method to get the name of the Workflow path
     *
     *@return    The wfPath value
     */

    public String getWfPath()
    {
        return wfPathName;
    }


    /**
     *  THis method prints out the location of the error that occurred during
     *  parsing.
     *
     *@param  workFlow                 Description of the Parameter
     *@return                          The workFlowPath value
     *@exception  MraldParseException  Description of the Exception
     */

    public Element getWorkFlowPath( Element workFlow )
        throws MraldParseException
    {
        //Get the  list of nodes with <WfName> tag
        NodeList nameList = workFlow.getElementsByTagName( workFlowNameTag );
        Node currentNode = workFlow;

        //If no more <WfName> then this workflow path has not been defined in XML
        //There should only be 1 to conform to DTD
        if ( nameList.getLength() < 1 || nameList == null)
        {
            MraldParseException parsingException = new MraldParseException( "\n The document is invalid. " +
                    "The required tag " + workFlowNameTag + " is not contained within " + getRootChildTag() );
            throw parsingException;
        }
        //There should only be 1 to conform to DTD
        if ( nameList.getLength() > 1 )
        {

            MraldParseException parsingException = new MraldParseException( "\n The document is invalid. The node " +
                    workFlow.getTagName() + " contains multiple names. " );
            throw parsingException;
        }

        if ( !isWfPath( nameList.item( 0 ).getFirstChild().getNodeValue() ) )
        {
            //Get the next node
            currentNode = getNextElement( nameList.item( 0 ).getParentNode().getNextSibling() );
            if(currentNode==null)
            {
                MraldParseException parsingException = new MraldParseException( "\n Could not find the workflow path " + wfPathName + " in this XML document." );
                throw parsingException;
            }
            return getWorkFlowPath( ( Element ) currentNode );
        }
        else
        {
            //Have found matching node
            return ( Element ) nameList.item( 0 );
        }

    }


    /**
     *  Method to set the name of the Workflow path
     *
     *@param  workFlowPath  Description of the Parameter
     *@return               The wfPath value
     */

    public boolean isWfPath( String workFlowPath )
    {
        if ( workFlowPath.equalsIgnoreCase( wfPathName ) )
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    /**
     *  Object that will process the Input file and create a DataModel Object.
     *  This takes a InputSource as input for the parser.
     *
     *@return                                   Description of the Return Value
     *@exception  IOException                   Description of the Exception
     *@exception  DOMException                  Description of the Exception
     *@exception  ParserConfigurationException  Description of the Exception
     *@exception  MraldParseException           Description of the Exception
     */

    public HashMap ProcessXML()
        throws IOException, DOMException, ParserConfigurationException, MraldParseException
    {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try
        {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse( XMLFile );
            Element rootElement = document.getDocumentElement();
            String tagName = rootElement.getTagName();
            if ( !rootElement.hasChildNodes() )
            {
                MraldParseException parsingException = new MraldParseException( "\n The document is invalid. The node " +
                        tagName + " does not contain any child nodes. " );
                throw parsingException;
            }
            Node childNode = rootElement.getFirstChild();
            childNode = getNextElement( childNode );
            if ( !( ( Element ) childNode ).getTagName().equals( rootChildTag ) )
            {
                System.out.println( "RootChildTag: " + rootChildTag );
                MraldParseException parsingException = new MraldParseException( "\n The document is invalid. The node " +
                        ( ( Element ) childNode ).getTagName() + " is not a valid child Node for " + getRootTag() );
                throw parsingException;
            }
            //If no child nodes
            if ( !childNode.hasChildNodes() )
            {
                MraldParseException parsingException = new MraldParseException( "\n The document is invalid. The node " +
                        tagName + " does not contain any child nodes. " );
                throw parsingException;
            }
            //This new ChildElement should be the <WfName> element
            childNode = getWorkFlowPath( ( Element ) childNode );
            processWorkFlow( ( Element ) childNode );
            return wfObjects;
        }
        catch ( SAXParseException spe )
        {
            spe.printStackTrace();
            // Error generated by the parser
            MraldParseException parsingException = new MraldParseException( "\n** Parsing error"
                     + ", line " + spe.getLineNumber()
                     + ", uri " + spe.getSystemId()
                     + spe.getMessage() );
            // Use the contained exception, if any
//            Exception x = spe;
            if ( spe.getException() != null )
            {
//                x = spe.getException();
            }
            throw parsingException;
        }
        catch ( SAXException spe )
        {
            // Error generated by the parser
            MraldParseException parsingException = new MraldParseException( spe.getMessage() );
            // Use the contained exception, if any
//            Exception x = spe;
            if ( spe.getException() != null )
            {
//                x = spe.getException();
            }
            throw parsingException;
        }
        catch ( DOMException de )
        {
            throw de;
        }
        catch ( IOException ioe )
        {
            throw ioe;
        }
        catch ( ParserConfigurationException pce )
        {
            throw pce;
        }
    }


    /**
     *  THis method prints out the location of the error that occurred during
     *  parsing.
     *
     *@param  wfObject                 The feature to be added to the WfObject
     *      attribute
     *@param  order                    The feature to be added to the WfObject
     *      attribute
     *@exception  MraldParseException  Description of the Exception
     */

    public void addWfObject( Object wfObject, Integer order )
        throws MraldParseException
    {

        wfObjects.put( order, wfObject );
    }


    /**
     *  THis method prints out the location of the error that occurred during
     *  parsing.
     *
     *@param  workFlow                 Description of the Parameter
     *@exception  MraldParseException  Description of the Exception
     */

    public void processWorkFlow( Element workFlow )
        throws MraldParseException
    {
        NodeList workFlowObjects = ( ( Element ) workFlow.getParentNode() ).getElementsByTagName( getWfObjectTag() );
        Node wfObjectNode = null;
        Node wfOrderNode = null;
        Integer Order = null;
//        String ObjectName = null;
        WorkFlow wfObject = new WorkFlow();

        //Check that there are child objects
        if ( workFlowObjects.getLength() < 1 )
        {

            // Error generated by the parser
            MraldParseException parsingException = new MraldParseException( "No nodes for " + workFlow.getParentNode().getNodeName() + " of tag name " + getWfObjectTag() );

            throw parsingException;
        }

        //Cycle through each of the WorkFlowObjects and get the associated name
        for ( int k = 0; k < workFlowObjects.getLength(); k++ )
        {

            //Get the name of the object

            wfObjectNode = getNextElement( workFlowObjects.item( k ).getFirstChild() );

            if ( !wfObjectNode.getNodeName().equalsIgnoreCase( workFlowObjectNameTag ) )
            {

                MraldParseException parsingException = new MraldParseException( "\nXML Document invalid. " + workFlowObjectNameTag + " is a required node for node " + workFlowObjects.item( k ).getNodeName() );
                throw parsingException;
            }

            if ( wfObjectNode.getFirstChild().getNodeType() != Node.TEXT_NODE )
            {

                // Error generated by the parser
                MraldParseException parsingException = new MraldParseException( "\nXML Document invalid. Name is required for Object." + wfObjectNode.getNodeName() );
                throw parsingException;
            }

            wfObject = new WorkFlow( wfObjectNode.getFirstChild().getNodeValue() );

            //Check if there are any methods associated with
            //this object
            processWorkFlowMethod( wfObject, wfObjectNode.getParentNode() );

            //Get the order of the Object
            wfOrderNode = getNextElement( wfObjectNode.getNextSibling() );

            if ( wfOrderNode.getFirstChild().getNodeType() != Node.TEXT_NODE )
            {

                // Error generated by the parser
                MraldParseException parsingException = new MraldParseException( "\nXML Document invalid. Order is required for Object." + wfOrderNode.getNodeName() );
                throw parsingException;
            }

            Order = new Integer( wfOrderNode.getFirstChild().getNodeValue() );

            //Add the WorkFlow object to the treeMap
            addWfObject( wfObject, Order );
        }
    }


    /**
     *  THis checks to see if there are any method associated with the object.
     *
     *@param  thisWfObject             Description of the Parameter
     *@param  workFlowOrder            Description of the Parameter
     *@exception  MraldParseException  Description of the Exception
     */

    public void processWorkFlowMethod( WorkFlow thisWfObject, Node workFlowOrder )
        throws MraldParseException
    {
        NodeList workFlowMethods = ( ( Element ) workFlowOrder ).getElementsByTagName( getWfMethodTag() );
        String methodName = null;
        Integer methodOrder = null;
        Element wfObject = null;

        //Check that there are child objects

        //This is not mandatory
        if ( workFlowMethods.getLength() < 1 )
        {
            thisWfObject.setHasMethods( false );
            return;
        }

        //This object has methods
        thisWfObject.setHasMethods( true );

        //Cycle through each of the WorkFlowObjects and get the associated name
        for ( int k = 0; k < workFlowMethods.getLength(); k++ )
        {

            wfObject = ( Element ) workFlowMethods.item( k );
            //Get the name of the object
            methodName = wfObject.getFirstChild().getNodeValue();
            methodOrder = new Integer( wfObject.getAttributeNode( workFlowMethodOrder ).getValue() );
            thisWfObject.addWfMethod( methodName, methodOrder );
        }
    }
}

