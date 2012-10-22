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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;

import org.mitre.mrald.control.AbstractStep;
import org.mitre.mrald.control.MsgObject;
import org.mitre.mrald.control.WorkflowStepException;
import org.mitre.mrald.util.Config;
import org.mitre.mrald.util.MraldError;
import org.mitre.mrald.util.MiscUtils;
import org.mitre.mrald.util.MraldOutFile;


public class OutputController extends AbstractStep
{
    protected static ArrayList<Object> keys = new ArrayList<Object>();
    protected static Properties currentClasses;
    protected static PropertyChangeListener pcl;
	protected static String propertyName = "";
    protected MsgObject messageObject = new MsgObject();

    public OutputController()
	{
		propertyName = "outputProps";
	}

    public OutputController( MsgObject thisMessage )
    {
		propertyName = "outputProps";
        messageObject = thisMessage;
    }

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
        currentClasses = MiscUtils.loadProperties( Config.getProperty( propertyName ) );
        Enumeration eKeys = currentClasses.propertyNames();
        while ( eKeys.hasMoreElements() )
        {
            keys.add( eKeys.nextElement() );
        }
    }

    public void execute( MsgObject msgObject )
        throws WorkflowStepException
    {
        /*
         *  Lazy instantiation
         */
        if ( currentClasses == null )
        {
            OutputController.init();
            Config.addPropertyChangeListener( OutputController.pcl );
        }
        try
        {
            messageObject = msgObject;
            processOutput();
        }
        catch ( OutputControllerException e )
        {
            MiscUtils.handleException( msgObject, e, true );
        }
    }

    /**
     *  This method processes the information in the MsgObject prior to the
     *  Output
     *
     *@exception  OutputControllerException  Description of Exception
     *@exception  WorkflowStepException      Description of Exception
     *@since
     */
    public void processOutput()
        throws WorkflowStepException, OutputControllerException
    {
//        String objectName = null;
        String[] returnFormatType = messageObject.getValue( "Format" );
        String returnFormat = returnFormatType[0];
        OutputManager outputType = castElement( returnFormat );
        outputType.execute( messageObject );
    }


     /**
     *  Based on the passed in class name, uses Reflection to create the new
     *  ParserElement objects for inclusion in the MsgObject workingObjects
     *  HashTable.
     *
     *@param  currentName                    Fully qualified class name
     *@return                                ParserElement of the class defined
     *      by the input String
     *@exception  OutputControllerException  Description of Exception
     */
    protected OutputManager castElement( String currentName )
        throws OutputControllerException
    {
        String className = null;
        try
        {
			className = getElementType( currentName );
            if ( className == null )
            {
                return null;
            }
            Class classDefinition = Class.forName( className );
            OutputManager outputType = ( OutputManager ) classDefinition.newInstance();

	      return outputType;
        }
        catch ( InstantiationException wfe )
        {
            throw new OutputControllerException( wfe );
        }
        catch ( ClassNotFoundException cne )
        {
            MraldOutFile.logToFile( cne );
            // throw new OutputControllerException( cne );
        }
        catch ( IllegalAccessException iae )
        {
            throw new OutputControllerException( iae );
        }
        throw new MraldError("Couldn't find an OutputManager mapped to " + currentName );
    }

    /**
     *  Returns the fully qualified class name for a given name.
     *
     *@param  	outputType			Name obtained from the MsgObject workingObjects HashTable. This should start with some value given in the buildables Properties object.
     *@return                                The fullly qualified class name
     *      associated with the submitted name.
     *@exception  OutputControllerException  Description of Exception
     */

    protected String getElementType( String classType )
		throws OutputControllerException
    {
	 for ( int i = 0; i < keys.size(); i++ )
        {
            if ( classType.startsWith( ( String ) keys.get( i ) ) )
            {

                return ( String ) currentClasses.get( keys.get( i ) );
            }
        }
        throw new NullPointerException("An class type of " + classType + " is not recognized.  Please check the class type in the form and the appropriate properties file.");
    }

}

