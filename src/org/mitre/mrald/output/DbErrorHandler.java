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
package org.mitre.mrald.output;

import java.sql.SQLException;

import org.mitre.mrald.util.Config;
import org.mitre.mrald.util.DomParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *  Description of the Class
 *
 *@author     jchoyt
 *@created    November 22, 2001
 */
public class DbErrorHandler
     extends DomParser
{
    /**
     *  Constructor for the DbErrorHandler object
     *
     *@since
     */
    public DbErrorHandler() { }


    /**
     *  Description of the Method
     *
     *@param  se    Description of Parameter
     *@param  html  Description of Parameter
     *@return       Description of the Return Value
     *@since
     */
    public static Exception handleException( SQLException se )
    {

        DbErrorHandler eh = new DbErrorHandler();
        NodeList errors = null;
        try
        {
            errors = eh.parseErrorFile( Config.getProperty( "dbErrFile" ) );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        String returnMessage = eh.buildMessage( errors, se.getMessage() );
        if ( returnMessage != null )
        {
            RuntimeException mt = new RuntimeException( returnMessage, se );

            return mt;
        }
        else
        {
            return se;
        }
    }


    /**
     *  Builds the Exception message if there is a error. Returns null if there
     *  is no matching error.
     *
     *@param  errors         Description of Parameter
     *@param  thrownMessage  Description of Parameter
     *@return                The Message value
     *@since
     */
    protected String buildMessage( NodeList errors, String thrownMessage )
    {
        Element thisElement;
        String number;
        String title;
        String message;
        /*
         *  process each <error> element:
         *  Check to see if the SQLException message contains the error/number string
         *  If it does, return the message and title appropriately formated
         *  if not, return null
         */
        for ( int i = 0; i < errors.getLength(); i++ )
        {
            thisElement = ( Element ) errors.item( i );
            number = getChildString( thisElement, "number" );
            //
            //
            if ( thrownMessage.indexOf( number ) != -1 )
            {
                title = getChildString( thisElement, "title" );
                message = getChildString( thisElement, "user_message" );
                return formatMessage( title, message );
            }
        }
        return null;
    }


    /**
     *  Given an error title and a user-useful message, this method constructs a
     *  String suitable for returning to the user
     *
     *@param  title    Description of Parameter
     *@param  message  Description of Parameter
     *@return          Description of the Returned Value
     *@since
     */
    protected String formatMessage( String title, String message )
    {
        String ret = "For this error (\"" + title + "\")<br /><br /> Please read the following " +
            "possible solution: " + message;
        return ret;
    }


    /**
     *  Description of the Method
     *
     *@param  filename                                           Description of
     *      the Parameter
     *@return                                                    Description of
     *      the Returned Value
     *@exception  org.mitre.mrald.output.OutputManagerException  Description of
     *      the Exception
     *@since
     */
    protected NodeList parseErrorFile( String filename )
        throws org.mitre.mrald.output.OutputManagerException
    {

        try
        {
            Document document = parseFile( filename );
            /*
             *  Check if root element has childNodes
             */
            Element rootElement = document.getDocumentElement();
            if ( !rootElement.hasChildNodes() )
            {
                throw new OutputManagerException( "\n The DbErrorHandler XML configuration file specified is invalid. The root element does not contain any child nodes. " );
            }
            /*
             *  Get all the <error> elements in a NodeList
             */
            return rootElement.getElementsByTagName( "error" );
        }
        catch ( org.mitre.mrald.util.MraldParseException e )
        {
            throw new OutputManagerException( e.getMessage() );
        }
    }
}

