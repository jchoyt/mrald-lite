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
package org.mitre.mrald.control;
//import org.mitre.mrald.formquery.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.mitre.mrald.util.MraldException;

/**
 *  The WfController class is responsible for the processing the workflow.xml file
 *  which determines the desired path for the
 *
 * @author     jchoyt
 * @created    April 3, 2001
 */
public class WfController
{

    private MsgObject messageObject = new MsgObject();
    private HashMap wfObjects = new HashMap();

    /**
     *  Constructor for the WfController object
     *
     */
    public WfController()
    {
    }

    /**
     *  Constructor for the WfController object which assigns the given MsgObject to the class variable
     *
     */
    public WfController( MsgObject thisMessage )
    {
        messageObject = thisMessage;
    }

    /**
     *  Sets the MsgObject attribute of the WfController object
     *
     */
    public void setMsgObject( MsgObject thisMessageObject )
        throws WfControllerException
    {
        messageObject = thisMessageObject;
    }

    /**
     *  Sets the WfObjects attribute of the WfController object
     */
    public void setWfObjects( HashMap thisWfObjects )
        throws WfControllerException
    {
        wfObjects = thisWfObjects;
    }

    /**
     *  Gets the MsgObject attribute of the WfController object
     *
     */
    public MsgObject getMsgObject()
        throws WfControllerException
    {
        return messageObject;
    }

    /**
     *  Gets the WfObjects attribute of the WfController object
     */
    public HashMap getWfObjects()
        throws WfControllerException
    {
        return wfObjects;
    }

    /**
     *  This method is the power of the class.  It is responsible for processing the entire
     *  workflow path.
     *
     */
    public void processWorkFlow()
        throws MraldException
    {
        WorkFlow wfObject = null;
        String methodName;
        int methodSize = 0;

        //MraldOutFile.appendToFile(Config.getProperty("LOGFILE") "WC: About to cycle " + wfObjects.size());
        //long startTime = System.currentTimeMillis();
        //long elapsedTime;
        for ( int i = 1; i <= wfObjects.size(); i++ )
        {
            wfObject = ( WorkFlow )wfObjects.get( new Integer( i ) );
            if(wfObject==null)
            {
                MraldException e = new MraldException("Can't find a workflow step with an Order of " + i
                    + ".  Chances are your workflow configuration file needs to be corrected.", messageObject);
                throw e;
            }
            String objectName = wfObject.getWfObject();
            /*If the object does not have any associated methods then
            execute*/
            if ( !wfObject.hasMethods() )
            {
                callMethod( objectName );
            }

            else
            {
                methodSize = wfObject.getMethodSize();
                int lastNumber = 0;
                for ( int k = 1; k <= methodSize; k++ )
                {
                    lastNumber++;
                    //Loop until next method found
                    while ( !wfObject.containsKey( lastNumber ) )
                    {
                        lastNumber++;
                    }
                    methodName = wfObject.getWfMethod( lastNumber );
                    callMethod( objectName, methodName );
                }
            }
            //elapsedTime = System.currentTimeMillis()-startTime;
            //startTime = System.currentTimeMillis();
            //MraldOutFile.appendToFile( "timing.log", "step"+i+" ("+objectName+") took "+elapsedTime+" ms");
        }
    }


    //Method to call method using Message Object.
    /**
     *  This method is used to call each of the steps in the workflow paths.
     *  It utilizes the AbstractStep class.
     *
     */
    public void callMethod( String objectName )
        throws MraldException
    {
        AbstractStep mraldObject = ( AbstractStep )createObject( objectName );
        mraldObject.execute( messageObject );
        mraldObject = null;
    }


    /**
     *  This method is used to call a given method with given parameters.
     *
     */
    public void callMethod( String objectName, String methodName, Object[] parameters, Class[] parameterTypes )
        throws MraldException
    {
        try
        {
            Class mraldObject = ( Class )Class.forName( objectName ).newInstance();
            Method method = mraldObject.getMethod( methodName, parameterTypes );
            method.invoke( mraldObject, parameters );
        }
        catch ( ClassNotFoundException e )
        {
            throw new MraldException(e, messageObject);
        }
        catch ( NoSuchMethodException e )
        {
            throw new MraldException(e, messageObject);
        }
        catch ( IllegalAccessException e )
        {
            throw new MraldException(e, messageObject);
        }
        catch ( InstantiationException e )
        {
            throw new MraldException(e, messageObject);
        }
        catch ( InvocationTargetException e )
        {
            throw new MraldException(e, messageObject);
        }
    }

    /**
     * This method is used to call Object methods without any parameters
     */
    public void callMethod( String objectName, String methodName )
        throws MraldException
    {
        try
        {
            Class mraldObject = ( Class )Class.forName( objectName ).newInstance();
            Method method = mraldObject.getMethod( methodName );
            method.invoke( mraldObject );
        }
        catch ( NoSuchMethodException e )
        {
            throw new MraldException(e, messageObject);
        }
        catch ( ClassNotFoundException e )
        {
            throw new MraldException(e, messageObject);
        }
        catch ( IllegalAccessException e )
        {
            throw new MraldException(e, messageObject);
        }
        catch ( InstantiationException e )
        {
            throw new MraldException(e, messageObject);
        }
        catch ( InvocationTargetException e )
        {
            throw new MraldException(e, messageObject);
        }
    }

    /**
     *  This method creates the appropriate object in the workflow.
     */
    public static Object createObject( String className )
        throws WfControllerException
    {
        Object object = null;
        try
        {
            Class classDefinition = Class.forName( className );
            object = classDefinition.newInstance();
        }
        catch ( InstantiationException e )
        {
            WfControllerException controlException = new WfControllerException( e.getMessage() );
            throw controlException;
        }
        catch ( IllegalAccessException e )
        {
            WfControllerException controlException = new WfControllerException( e.getMessage() );
            throw controlException;
        }
        catch ( ClassNotFoundException e )
        {
            WfControllerException controlException = new WfControllerException( e.getMessage() );
            throw controlException;
        }
        return object;
    }
}

