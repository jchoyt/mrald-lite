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
package org.mitre.mrald.parser;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.TreeSet;

import org.mitre.mrald.control.AbstractStep;
import org.mitre.mrald.control.MsgObject;
import org.mitre.mrald.control.MsgObjectException;
import org.mitre.mrald.control.WorkflowStepException;
import org.mitre.mrald.util.Config;
import org.mitre.mrald.util.MiscUtils;
import org.mitre.mrald.util.MraldOutFile;

/**
 *  The MraldParser class collects all name and value information from the
 *  passed MsgObject and builds a list of appropriately cast ParserElement
 *  objects.
 *
 *@author     Jeffrey Hoyt
 *@created    May 20, 2002
 *@version    4.3
 */
public class MraldParser extends AbstractStep
{
    /**
     *  Properties defining how to cast the Elements coming in in the MsgObject
     */
    private static Properties buildables;
    private static ArrayList<Object> keys = new ArrayList<Object>();
    private static PropertyChangeListener pcl;
    private ArrayList<ParserElement> qeList;


    /**
     *  Constructor for the StagedPageParser object
     *
     *@since
     */
    public MraldParser()
    {
        qeList = new ArrayList<ParserElement>();
    }


    /**
     *  Description of the Method
     */
    protected static void init()
    {
        loadProperties();
        pcl = (
            new PropertyChangeListener()
            {
                public void propertyChange( PropertyChangeEvent evt )
                {
                    loadProperties();
                }
            } );
    }


    /**
     *  Loads the properties from permanent storage. Uses a call to the MRALD
     *  global configuration to get the location of this file. After the
     *  properties are loaded, the keys are stored in a Vector to make retrieval
     *  easier and faster.
     *
     *@since
     */
    protected static void loadProperties()
    {
        buildables = MiscUtils.loadProperties( Config.getProperty( "parserProps" ) );
        Enumeration eKeys = buildables.propertyNames();
        while ( eKeys.hasMoreElements() )
        {
            keys.add( eKeys.nextElement() );
        }
    }


    /**
     *  Sets the buildables attribute of the MraldParser object
     *
     *@param  buildables  The new buildables value
     */
    public static void setBuildables( Properties buildables )
    {
        MraldParser.buildables = buildables;
        Enumeration eKeys = buildables.propertyNames();
        while ( eKeys.hasMoreElements() )
        {
            keys.add( eKeys.nextElement() );
        }
    }


    /**
     *  Gets the buildables attribute of the MraldParser object
     *
     *@return    The buildables value
     */
    public Properties getBuildables()
    {
        return buildables;
    }


    /**
     *  The execute method needed to satisfy the AbstractStep interface. This is
     *  the default method that executes when this step in the workflow is
     *  called. Think of it as <i>main</i> for working in the MRALD framework.
     *
     *@param  msgObject                  Container for all information passed
     *      from the last step, and will carry the information to the next step.
     *@exception  WorkflowStepException  Required to satisfy the AbstractStep
     *      interface. All Exceptions not handled should be rethrown to this.
     *@since
     */
    public void execute( MsgObject msgObject )
        throws WorkflowStepException
    {
        try
        {
            /*
             *  Lazy instantiation
             */
            if ( buildables == null )
            {
                MraldParser.init();
                Config.addPropertyChangeListener( MraldParser.pcl );
            }
            processNvPairs( msgObject );
            qeList.addAll( msgObject.getWorkingObjects() );
            msgObject.setWorkingObjects( qeList );
        }
        catch ( MraldParserException e )
        {
            try
            {
                msgObject.SetOutPrintWriter();
                msgObject.setContentType( "text/html" );
                MiscUtils.handleException( msgObject, e, true );
            }
            catch ( MsgObjectException msge )
            {
                throw new WorkflowStepException( msge.getMessage() );
            }
        }
    }


    /**
     *  Returns the fully qualified class name for a given name.
     *
     *@param  currentName               Name obtained from the MsgObject
     *      workingObjects HashTable. This should start with some value given in
     *      the buildables Properties object.
     *@return                           The fullly qualified class name
     *      associated with the submitted name.
     *@exception  MraldParserException  Description of Exception
     */
    private static String getElementType( String currentName )
        throws MraldParserException
    {
        for ( int i = 0; i < keys.size(); i++ )
        {
            if ( currentName.startsWith( ( String ) keys.get( i ) ) )
            {
                return ( String ) buildables.get( keys.get( i ) );
            }
        }
        return null;
    }


    /**
     *  Based on the passed in class name, uses Reflection to create the new
     *  ParserElement objects for inclusion in the MsgObject workingObjects
     *  HashTable.
     *
     *@param  currentName               Fully qualified class name
     *@return                           ParserElement of the class defined by
     *      the input String
     *@exception  MraldParserException  Description of Exception
     */
    public static ParserElement castElement( String currentName )
        throws MraldParserException
    {
        String className = getElementType( currentName );
        try
        {
            if ( className == null )
            {
                return null;
            }
            Class classDefinition = Class.forName( className );
            ParserElement parserElement = ( ParserElement ) classDefinition.newInstance();
            return parserElement;
        }
        catch ( InstantiationException wfe )
        {
            MraldParserException ce = new MraldParserException( "InstantiationException" + wfe.getMessage() );
            throw ce;
        }
        catch ( ClassNotFoundException cne )
        {
            MraldOutFile.logToFile( cne );
        }
        catch ( IllegalAccessException iae )
        {
            MraldParserException ce = new MraldParserException( "Illegal access exception" + iae.getMessage() );
            throw ce;
        }
        return null;
    }


    /**
     *  Runs through all the name/value pairs and extracts the recognizable ones
     *  for addition to the MsgObject workingObjects HashTable. Unrecognized
     *  pairs are left in the nvNames structure for later processing steps.
     *
     *@param  msg                       MsgObject passed to execute. Contains
     *      the name/value pairs to be processed in it workingObjects HashTable.
     *@exception  MraldParserException  Description of Exception
     */
    private void processNvPairs( MsgObject msg )
        throws MraldParserException
    {
        String currentName;

        try
        {
            Iterator<String> nvNames;
            newpass :
            while ( true )
            {
                /*
                 *  using a TreeSet here allows the form developer to specify the order in which
                 *  ParserElements are processed by making them sort the keys in order by alpha.  Note
                 *  this means you need to pad lower numbers if there is a chance of longer number occuring.
                 *  i.e., Use Filter02, not Filter2 so Filter02 comes before Filter10.
                 */
                nvNames = new TreeSet<String>( msg.getNames() ).iterator();
                /*
                 *  For each enumeration of the parameter elements create a query element
                 */
                while ( nvNames.hasNext() )
                {
                    currentName = nvNames.next();
                    ParserElement parserElement = castElement( currentName );

                    if ( ( parserElement != null ) )
                    {
                        currentName = parserElement.preProcess( msg, currentName );
                        parserElement.process( msg.getValue( currentName ) );
                        currentName = parserElement.postProcess( msg, currentName );
                        msg.removeValue( currentName );
                        if ( parserElement.getIsActive() )
                        {
                            qeList.add( parserElement );
                        }
                        continue newpass;
                    }
                }
                break;
            }
        }
        catch ( MraldParserException e )
        {
            MraldParserException thisException = new MraldParserException( e.getMessage() );
            throw thisException;
        }
    }
}

