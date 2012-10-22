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
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.mitre.mrald.control.AbstractStep;
import org.mitre.mrald.control.MsgObject;
import org.mitre.mrald.control.WorkflowStepException;
import org.mitre.mrald.parser.ParserElement;
import org.mitre.mrald.query.MraldDijkstra;
import org.mitre.mrald.util.Config;
import org.mitre.mrald.util.FormTags;
import org.mitre.mrald.util.MraldError;
import org.mitre.mrald.util.MraldException;
import org.mitre.mrald.util.MraldOutFile;

/*
 *  An Abstract Class Used to Implement the MsgObject class.
 *
 *  @author	jchoyt
 *  @date	April 11, 2001
 *  @param	msg	Description of Parameter
 *
 */
/**
 *  Description of the Class
 *
 *@author     jchoyt
 *@created    September 22, 2003
 */
public class CheckFormJoinStructureStep
         extends AbstractStep
{
    MsgObject msg;


    /**
     *  Sets the msg attribute of the CheckFormJoinStructureStep object
     *
     *@param  msg  The new msg value
     */
    public void setMsg( MsgObject msg )
    {
        this.msg = msg;
    }


    /**
     *  Gets the msg attribute of the CheckFormJoinStructureStep object
     *
     *@return    The msg value
     */
    public MsgObject getMsg()
    {
        return msg;
    }



    /**
     *  Converts the org.mitre.mrald.formbuilder.LinkElement to
     *  org.mitre.mrald.query.LinkElement and populates the MsgObject links
     *  field with them so that the MraldDijkstra methods will work properly.
     */
    public void convertLinks(MsgObject msgObject)
    {
        Object currentObject;
        org.mitre.mrald.formbuilder.LinkElement currentElement;
        /*
         *  run through every ParserElement in workingObjects and grab every Table
         *  you can find.  This won't work on every element sub type, but it will
         *  work on all the FieldElements, so it should grab every table used
         */
        for ( int i = 0; i < msgObject.getWorkingObjects().size(); i++ )
        {
            currentObject = msgObject.getWorkingObjects().get( i );
            if ( currentObject instanceof org.mitre.mrald.formbuilder.LinkElement )
            {
                currentElement = ( org.mitre.mrald.formbuilder.LinkElement ) currentObject;
                if ( !currentElement.ignore() )
                {
                    //currentElement.resetVariables( "this doesn't do anything - it's just so we override the method in omm.query.LinkElement" );
                	msgObject.addLink( currentElement );
                }
            }
        }
        MraldOutFile.logToFile( Config.getProperty( "LOGFILE" ), "CheckFormJoinStructure: Convert Links: New MSG Object:no of links " + msgObject.getLinks().size() );

    }


    /**
     *  Description of the Method
     *
     *@param  msg                        Description of the Parameter
     *@exception  WorkflowStepException  Description of the Exception
     */
    public void execute( MsgObject msg )
        throws WorkflowStepException
    {
        this.msg = msg;


        try
        {

	        HashMap<String,ArrayList<ParserElement>> sortedElements =  ParserElement.orderElements(msg.getWorkingObjects());

	        MraldOutFile.logToFile( Config.getProperty( "LOGFILE" ), "CheckFormJoinStructure: No of threads sorted " + sortedElements.size());

	        //Loop once for each thread
	        for (ArrayList<ParserElement> sortedElems: sortedElements.values())
	        {
	        	MraldOutFile.logToFile( Config.getProperty( "LOGFILE" ), "CheckFormJoinStructure: Next" );

	        	for (ParserElement parseElem: sortedElems)
	        	{
	        		MraldOutFile.logToFile( Config.getProperty( "LOGFILE" ), "CheckFormJoinStructure: " +
	        				 "ElementType" + parseElem.getElementType() +
	        				 "Thread Number " + parseElem.getThreadNumber());
	        	}
	        	MsgObject newMsg = new MsgObject();
	        	newMsg.setWorkingObjects(sortedElems);
	        	convertLinks(newMsg);

//		        int noOfLinks = newMsg.getLinks().size();
		        ArrayList tables = reapTables(newMsg);
		        MraldOutFile.logToFile( Config.getProperty( "LOGFILE" ), "CheckFormJoinStructure: NO OF TABLES: " + tables.size() );
		        if (tables.size() ==0) continue;

		        MraldDijkstra joins = new MraldDijkstra( newMsg );
		        int joinValidity = joins.testJoinPath( tables );
		        //if it's a bad structure, send the user to badForm.jsp with the error code
		        if ( joinValidity != MraldDijkstra.JOIN_PATH_OK )
		        {
		            try
		            {
		                msg.setRedirect( "badForm.jsp?errorCode=" + joinValidity );
		            }
		            catch ( IOException e )
		            {
		                throw new MraldError( e, msg );
		            }
		        }
	        }
        }
        catch ( MraldException e )
        {
            throw new MraldError( e, msg );
        }
    }


    /**
     *  Runs through all the workingObjects in msg and pulls out the table names
     *  stored.
     *
     *@return    Description of the Return Value
     */
    public ArrayList<String> reapTables(MsgObject msgObject)
    {
        ArrayList<String> tables = new ArrayList<String>();
        ParserElement currentElement;
        String newTable;
        /*
         *  run through every ParserElement in workingObjects and grab every Table
         *  you can find.  This won't work on every element sub type, but it will
         *  work on all the FieldElements, so it should grab every table used
         */
        for ( int i = 0; i < msgObject.getWorkingObjects().size(); i++ )
        {
            currentElement = msgObject.getWorkingObjects().get( i );
            newTable = currentElement.getNameValues().getValue( FormTags.TABLE_TAG )[0];
            if ( !newTable.equals( Config.EMPTY_STR ) && !tables.contains( newTable ) )
            {
            	MraldOutFile.logToFile( Config.getProperty( "LOGFILE" ), "CheckFormJoinStructure: reapTables : Adding " + newTable );

                tables.add( newTable );
            }
        }
        return tables;
    }


}

