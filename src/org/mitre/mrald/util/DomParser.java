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

import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *  Description of the Class
 *
 *@author     jchoyt
 *@created    November 23, 2001
 */
public class DomParser extends java.lang.Object
{

    /**
     *  Constructor for the DomParser object
     *
     *@since
     */
    public DomParser() { }


    /**
     *  Gets the value of a child node - assumes that there is only one child
     *  node with that name and that it has a node value
     *
     *@param  parent     Description of Parameter
     *@param  childName  Description of Parameter
     *@return            The ChildString value
     *@since
     */
    protected String getChildString( Element parent, String childName )
    {
        NodeList childElements = parent.getElementsByTagName( childName );
        Node currNode = childElements.item( 0 );
        return currNode.getFirstChild().getNodeValue();
    }


    /**
     *  NOT YET IMPLMENTED! Returns all the ChildNodes of the passed node in a
     *  NodeList.
     *
     *@param  parent      Description of Parameter
     *@return             The ChildNodes value
     *@since
     */
    protected NodeList getChildNodes( Element parent )
    {
        return null;
    }


    /**
     *  NOT YET IMPLMENTED! Returns the values of all the child nodes of the
     *  passed nodes. Values are returned in a Property object and can be
     *  accessed by:<br>
     *  Property.getValue("[elementName]");
     *
     *@param  parent      Description of Parameter
     *@return             The ChildValues value
     *@since
     */
    protected Properties getChildValues( Element parent )
    {
        return null;
    }


    /**
     *  NOT YET IMPLMENTED! Returns the specified ChildNode passed node
     *
     *@param  childName   Description of Parameter
     *@param  parent      Description of Parameter
     *@return             The ChildNode value
     *@since
     */
    protected Node getChildNode( String childName, Element parent )
    {
        return null;
    }


    /**
     *  Description of the Method
     *
     *@param  filename                 Description of Parameter
     *@return                          Description of the Returned Value
     *@exception  MraldParseException  Description of Exception
     *@since
     */
    protected Document parseFile( String filename )
        throws MraldParseException
    {
        try
        {
            String XMLFile = filename;
            if ( XMLFile == null )
            {
                throw new MraldParseException( "\n The XML file, " + filename + " could not be found. " );
            }
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse( XMLFile );
        }
        catch ( javax.xml.parsers.ParserConfigurationException e )
        {
            throw new MraldParseException( e.getMessage() );
        }
        catch ( org.xml.sax.SAXException e )
        {
            throw new MraldParseException( e.getMessage() );
        }
        catch ( java.io.IOException e )
        {
            throw new MraldParseException( e.getMessage() );
        }
    }
}

