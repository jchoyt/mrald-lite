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
package org.mitre.mrald.multiquery;
import java.util.ArrayList;
import java.util.HashMap;

import org.mitre.mrald.control.AbstractStep;
import org.mitre.mrald.control.MsgObject;
import org.mitre.mrald.control.WfController;
import org.mitre.mrald.control.WfControllerException;
import org.mitre.mrald.control.WorkflowStepException;
import org.mitre.mrald.output.FilterOutput;
import org.mitre.mrald.output.OutputManager;
import org.mitre.mrald.query.SqlElements;
import org.mitre.mrald.query.UpdateBuilder;
import org.mitre.mrald.util.Config;
import org.mitre.mrald.util.FormTags;
import org.mitre.mrald.util.MiscUtils;
import org.mitre.mrald.util.MraldException;
import org.mitre.mrald.util.MraldOutFile;


/**
 *  The MultiQueryBuilder provides the ability to run the system using a multi
 *  query structure. It is part of the org.mitre.mrald.formquery package.
 *
 *@author     Gail Hamilton
 *@created    Feb 13th, 2007
 */
// PM: Like MultiQuery, this class is dangerously not type-safe.
@SuppressWarnings("unchecked")
public class MultiUpdateBuilder extends AbstractStep
{

    private MsgObject msg = new MsgObject();
//    private HashMap sqlObjects = new HashMap();


    /**
     *  Constructor for the MultiQueryBuilder object
     *
     *@since
     */
    public MultiUpdateBuilder() { }


    /**
     *  Constructor for the MultiQueryBuilder object which assigns the parameter
     *  to the class MsgObject variable
     *
     *@param  thisMessage  Description of the Parameter
     */
    public MultiUpdateBuilder( MsgObject thisMessage )
    {
        msg = thisMessage;
    }


    /**
     *  THis methof takes the name of the output from the message object and
     *  creates the output type
     *
     *@param  msg                        Description of the Parameter
     *@return                            The output value
     *@exception  MraldException         Description of the Exception
     */
    public OutputManager getOutput( MsgObject msg )
        throws MraldException
    {
        ArrayList sqlElements = msg.getWorkingObjects();
        OutputManager output = null;
        String outputManager = null;
        for ( int i = 0; i < sqlElements.size(); i++ )
        {
            SqlElements woElement = ( SqlElements ) sqlElements.get( i );
            String thisOutputManager = woElement.getOutputType();

            MraldOutFile.appendToFile(Config.getProperty("LOGFILE") + "MQB: Output Type: "  + thisOutputManager );

            if ( thisOutputManager == null )
            {
                //MraldOutFile.appendToFile(Config.getProperty("LOGFILE") "MQB: Output Type:  is null."  );

                continue;
            }
            outputManager = thisOutputManager;
        }

        //MraldOutFile.appendToFile(Config.getProperty("LOGFILE") "MQB: Output Type: If no line before this - is null."  );

        if ( ( outputManager == null ) )
        {
            return new FilterOutput();
        }
        if ( outputManager.equals( Config.EMPTY_STR ) )
        {
            return new FilterOutput();
        }
        try
        {
            msg.setValue( FormTags.FORMAT_TAG, "org.mitre.mrald.output." + outputManager );
            output = ( OutputManager ) WfController.createObject( "org.mitre.mrald.output." + outputManager );
            return output;
        }
        catch ( WfControllerException wexp )
        {
            MraldException qe = new MraldException( wexp.getMessage() );
            throw qe;
        }

    }


    /**
     *  This method is a part of the AbstractStep which is used by the
     *  WfController to execute the workflow step.
     *
     *@param  msgObject                  Description of the Parameter
     *@exception  WorkflowStepException  Description of the Exception
     */
    public void execute( MsgObject msgObject )
        throws WorkflowStepException
    {
        try
        {
            msg = msgObject;
            HashMap orderedElements = orderSqlElements( msg.getWorkingObjects() );
            Integer sqlThreadCount = null;
             //i is the # of iterations
            int i = 0;
//            int j = 0;
            MsgObject subMsg = new MsgObject();

            //elementCount is the index being used to count the
            //sqlThread # of the elements
            int elementCount = 0;
            //If the SQL statements are linked together

            while ( i < orderedElements.size() )
            {
                elementCount++;
                //Emergency halt during development
                if ( elementCount > 50 )
                {
                    throw new WorkflowStepException( "MQB: The count has exceeded maximum:" );
                }
                i++;

                sqlThreadCount = new Integer( elementCount );
                boolean keyValExists = orderedElements.containsKey( sqlThreadCount.toString() );
                //Only go through processing if the next key value exists
                if ( !keyValExists )
                {
                    //Decrease count as there was no elements
                    i--;
                    continue;
                }
                subMsg = new MsgObject();
                UpdateBuilder ub = new UpdateBuilder();
                ArrayList sqlElements = ( ArrayList ) orderedElements.get( sqlThreadCount.toString() );
                 /*
                 *  find all the links that belong to this sqlThread
                 */
                subMsg.setWorkingObjects( sqlElements );

                ub.execute( subMsg );

                msg.setQuery( MiscUtils.clearSemiColon( subMsg.getQuery()[0] ) );


                subMsg.clearNvPairs();
                subMsg.closeOut();

            }//end of loop for noOfSql

        }
        catch ( MraldException e )
        {
            throw new WorkflowStepException( e.getMessage() );
        }
    }


    /**
     *  This method reorganises the SQLElements into a HashMap according to the
     *  sqlNo
     *
     *@param  qe                         Description of the Parameter
     *@return                            Description of the Return Value
     *@exception  MraldException         Description of the Exception
     */
    public HashMap orderSqlElements( ArrayList qe )
        throws MraldException
    {
        HashMap orderedElements = new HashMap();
        SqlElements thisElement = null;
        ArrayList elements = new ArrayList();
        Integer maxNo = new Integer( 0 );
        Integer thisNo = new Integer( 0 );
        //Have to distinguish beween the two as thisNo is 'counting'
        Integer newNo = new Integer( 0 );
        for ( int i = 0; i < qe.size(); i++ )
        {
            //Put into a HashMap according to sqlNo
            thisElement = ( SqlElements ) qe.get( i );
            String sqlNo = thisElement.getSqlNo();
            if ( sqlNo.equals( "all" ) )
            {
                thisNo = new Integer( "-1" );
                sqlNo = "-1";
                thisElement.setSqlNo( sqlNo );
            }
            else
            {
                thisNo = new Integer( sqlNo );
            }
            //If maxNo < thisNo
            if ( maxNo.compareTo( thisNo ) < 0 )
            {
                maxNo = thisNo;
            }
            if ( orderedElements.containsKey( sqlNo ) )
            {
                elements = ( ArrayList ) orderedElements.get( thisElement.getSqlNo() );
            }
            else
            {
                elements = new ArrayList();
            }
            elements.add( thisElement );
            orderedElements.put( thisElement.getSqlNo(), elements );
        }
        maxNo = new Integer( maxNo.intValue() + 1 );
        //Any remaining objects did not have a sqlNo -
        //by default these are the final sqlElements
        //used in the Ordinary output
        ArrayList elementsWithAll = ( ArrayList ) orderedElements.get( "-1" );
        if ( elementsWithAll != null )
        {
            //Put all the Elements with "all"in them into all sqlThreads
            for ( int k = 0; k < elementsWithAll.size(); k++ )
            {

                //Put into a HashMap according to sqlNo
                thisElement = ( SqlElements ) elementsWithAll.get( k );
//                String sqlNo = thisElement.getSqlNo();

                //MraldOutFile.appendToFile(Config.getProperty("LOGFILE") "MQB: This element: found an 'all'");

                for ( int j = 0; j < maxNo.intValue(); j++ )
                {
                    //MraldOutFile.appendToFile(Config.getProperty("LOGFILE") "MQB: This element: Creating");

                    newNo = new Integer( j );
                    thisElement.setSqlNo( newNo.toString() );

                    elements = ( ArrayList ) orderedElements.get( thisElement.getSqlNo() );

                    if ( elements == null )
                    {
                        //MraldOutFile.appendToFile(Config.getProperty("LOGFILE") "MQB: No elements found for item " + j);
                        continue;
                    }
                    //Only addif there are other elements in this SQlthread
                    //Otherwise the user hasn't selectede any objects from
                    //this sqlTHread
                    if ( elements.size() > 0 )
                    {
                        elements.add( thisElement );
                        orderedElements.put( thisElement.getSqlNo(), new ArrayList( elements ) );
                    }
                }
            }
            orderedElements.remove( "-1" );

        }
        //MraldOutFile.appendToFile(Config.getProperty("LOGFILE") "MQB: Max No " + maxNo );

        if ( orderedElements.get( SqlElements.DEFAULT_SQL_NO ) == null )
        {
            //MraldOutFile.appendToFile(Config.getProperty("LOGFILE") "MQB: Not found for key " +SqlElements.DEFAULT_SQL_NO );

            return orderedElements;
        }
        else
        {
            //MraldOutFile.appendToFile(Config.getProperty("LOGFILE") "MQB: Found for key " +SqlElements.DEFAULT_SQL_NO );
            elements = ( ArrayList ) orderedElements.get( SqlElements.DEFAULT_SQL_NO );

            //MraldOutFile.appendToFile(Config.getProperty("LOGFILE") "MQB: Size of Array for default " + elements.size() );

            orderedElements.put( maxNo.toString(), orderedElements.get( SqlElements.DEFAULT_SQL_NO ) );
        }
        //MraldOutFile.appendToFile(Config.getProperty("LOGFILE") "MQB: Size after " + orderedElements.size() );

        orderedElements.remove( SqlElements.DEFAULT_SQL_NO );

        //MraldOutFile.appendToFile(Config.getProperty("LOGFILE") "MQB: Size of Array for moved default " + elements.size() );

        //MraldOutFile.appendToFile(Config.getProperty("LOGFILE") "MQB: Size after " + orderedElements.size() );
        return orderedElements;
    }
}

