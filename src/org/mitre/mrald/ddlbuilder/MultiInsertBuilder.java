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
package org.mitre.mrald.ddlbuilder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.mitre.mrald.control.MsgObject;
import org.mitre.mrald.control.WorkflowStepException;
import org.mitre.mrald.ddlelements.DdlElement;
import org.mitre.mrald.ddlelements.InsertElement;
import org.mitre.mrald.ddlelements.MultiInsertElementComparator;
import org.mitre.mrald.parser.ParserElement;
import org.mitre.mrald.util.MraldException;

/**
 *  Builds and Executes Multi Insert DDL statements form the InsertElements of
 *  ONE FORM stored in the MsgObject passed to the execute method.
 *
 *@author     tcornett
 *@created    June 30, 2004
 */
public class MultiInsertBuilder extends InsertBuilder
{
    MsgObject msg;


    /**
     *  Build the DDL statements (one for each table listed in the
     *  InsertElements) and store them in the MsgObject query field for the next
     *  workflow step to use
     *
     *@param  insertElements      Description of the Parameter
     *@return                     Description of the Return Value
     *@exception  MraldException  Description of the Exception
     */
    public String[] buildDdl( List<InsertElement> insertElements )
        throws MraldException
    {
        /*
         *  set up return ArrayList
         */
        ArrayList<String> returnDdl = new ArrayList<String>();
        ArrayList<InsertElement> commonElements = new ArrayList<InsertElement>();
        //ArrayList multiElements = new ArrayList();
        /*
         *  split up by table
         */
        ArrayList<InsertElement> elements = new ArrayList<InsertElement>();
        String ddl;
        boolean containsMultiSelection = false;
        InsertElement currentElement = null;
        int sqlThreadNo = 0;
        Iterator<InsertElement> iter = insertElements.iterator();
        for ( int i = 0; i < insertElements.size(); i++ )
        {
            currentElement = iter.next();

            /*
             *  grab all the common elements.  This only works because the elements
             *  are already sorted.  All negative values should be at the front of the list,
             *  so we use a negative SqlThread number to indicate a "common" part of a query
             */
            if ( Integer.parseInt( currentElement.getSqlThread() ) < 0 )
            {

                commonElements.add( currentElement );
                continue;
            }

            if ( sqlThreadNo != Integer.parseInt( currentElement.getSqlThread() ) )
            {
//                String oldNo = "" + sqlThreadNo;
                sqlThreadNo = Integer.parseInt( currentElement.getSqlThread() );
                /*
                 *  if this is the first run, just add it and go
                 */
                if ( elements.size() == 0 )
                {
                    elements.add( currentElement );
                    continue;
                }
                /*
                 *  else, you've switched queries - add the common stuff and build the query
                 *  Since we are adding the common stuff to the end, and the table name is
                 *  pulled from the first element, using a tablename of !All shouldn't mess
                 *  anything up
                 */
                elements.addAll( commonElements );
                //addToSqlThread( multiElements, elements, oldNo );
                if ( containsMultiSelection )
                {
                    ArrayList<String> newDDLs = new ArrayList<String>();
                    newDDLs = constructMultiDdl( elements );
                    returnDdl.addAll( newDDLs );
                }
                else
                {
                    ddl = constructSingleDdl( elements );
                    returnDdl.add( ddl );
                }
                elements = new ArrayList<InsertElement>();
                containsMultiSelection = false;
            }
            if ( currentElement.getElementType().equals( "MultiSelectionElement" ) )
            {
                //MraldOutFile.logToFile( Config.getProperty( "LOGFILE" ), "FOUND A MULTI SELECT ELEMENT" );
                containsMultiSelection = true;
            }
            elements.add( currentElement );
        }

        if ( elements.size() > 0 )
        {
            elements.addAll( commonElements );
            //addToSqlThread( multiElements, elements, currentElement.getSqlThread() );
            if ( containsMultiSelection )
            {
                ArrayList<String> newDDLs = new ArrayList<String>();
                newDDLs = constructMultiDdl( elements );
                returnDdl.addAll( newDDLs );
            }
            else
            {
                ddl = constructSingleDdl( elements );
                returnDdl.add( ddl );
            }
        }
        /*
         *  return the ArrayList of DDL statements
         */
        if ( returnDdl.size() == 0 )
        {
            RuntimeException rte = new RuntimeException( "No fields were chosen for insertion into any table.  No DDL statements were created. Try providing values for some fields and resubmit the request." );
            throw rte;
        }
        String[] returnArray = new String[returnDdl.size()];
        returnArray = returnDdl.toArray( returnArray );
        return returnArray;
    }


    /**
     *  Description of the Method
     *
     *@param  elements            Description of the Parameter
     *@return                     Description of the Return Value
     *@exception  MraldException  Description of the Exception
     */
    public ArrayList<String> constructMultiDdl( ArrayList<InsertElement> elements )
        throws MraldException
    {
        /*
         *  get the strings from the InsertElements
         */
        ArrayList<String> multiValueStrings = new ArrayList<String>();

        ArrayList<String> fieldStrings = new ArrayList<String>();
        ArrayList<String> valueStrings = new ArrayList<String>();

        ArrayList<String> returnDDLs = new ArrayList<String>();
        InsertElement currentElement;

        for ( int i = 0; i < elements.size(); i++ )
        {
            currentElement = elements.get( i );
            if ( currentElement.getElementType().equals( "MultiSelectionElement" ) )
            {
                multiValueStrings = currentElement.buildValueList( multiValueStrings );
                valueStrings.add( "P_H" );// Placeholder for the order integrity
            }
            else
            {
                valueStrings = currentElement.buildValueList( valueStrings );
            }
            fieldStrings = currentElement.buildFieldList( fieldStrings );
        }
        /*
         *  assemble the statement - do it in two halves, then append them
         */
        if ( multiValueStrings.size() == 0 )
        {
            RuntimeException rte = new RuntimeException( "No entries were chosen for the Multiple Selection Fields.  No associated DDL statements were created. Try providing values for this fields and resubmit the request." );
            throw rte;
        }

        for ( int j = 0; j < multiValueStrings.size(); j++ )
        {
            String tableName = elements.get( 0 ).getTable();
            StringBuffer start = new StringBuffer();
            StringBuffer end = new StringBuffer();

            start.append( "INSERT INTO " );
            start.append( tableName );
            start.append( " ( " );

            end.append( " ) VALUES ( " );

            for ( int i = 0; i < fieldStrings.size(); i++ )
            {
                start.append( fieldStrings.get( i ) );
                if ( valueStrings.get( i ).equals( "P_H" ) )
                {
                    end.append( multiValueStrings.get( j ) );
                }
                else
                {
                    end.append( valueStrings.get( i ) );
                }
                if ( i < fieldStrings.size() - 1 )
                {
                    start.append( ", " );
                    end.append( ", " );
                }
            }

            end.append( " )" );
            start.append( end );

            returnDDLs.add( start.toString() );
        }
        return returnDDLs;
    }


    /**
     *  This method is part of the AbstractStep interface and it called from the
     *  workflow controller. The expected interface is that the workingObjects
     *  ArrayList in the passed MsgObject will contain a group of ParserElement
     *  objects that need to be processed. The nvPairs object will hold all the
     *  other name/value pairs passed form the form. After execution, the query
     *  String of the passed MsgObject will contain the insert DDL statements
     *  (one for each table specidfied in teh passed InsertElements).
     *
     *@param  msgObject                  received from the previous step -
     *      contains all information to be processed
     *@exception  WorkflowStepException  Description of the Exception
     */
    public void execute( MsgObject msgObject )
        throws WorkflowStepException
    {
        try
        {
            msg = msgObject;
            /*
             *  extract the InsertElements from the parser elements and sort them by the DdlComparator
             */
            ArrayList<ParserElement> parserElements = msgObject.getWorkingObjects();
            List<InsertElement> c = extractInsertElements( parserElements );
            removeInvalidElements( c );
            Collections.sort( c, new MultiInsertElementComparator() );
            /*
             *  build the DDL statements (one for each table listed in the InsertElements)
             *  and store them in the MsgObject query field for the next workflow step to use
             */
            String ddl[] = buildDdl( c );
            for ( int i = 0; i < ddl.length; i++ )
            {
                msgObject.setQuery( ddl[i] );
                // MraldOutFile.logToFile( ddl[i] );
            }
        }
        catch ( MraldException e )
        {
            throw new WorkflowStepException( e.getMessage() );
        }
    }


    /**
     *  Removes the InsertElements from the supplied ArrayList and returns them
     *  in a separate Collection object.
     *
     *@param  parserElements  Description of the Parameter
     *@return                 Description of the Return Value
     */
    public List<InsertElement> extractInsertElements( ArrayList<ParserElement> parserElements )
    {
        ArrayList<InsertElement> returnList = new ArrayList<InsertElement>();
        ParserElement nextElement;
        for ( int i = 0; i < parserElements.size(); i++ )
        {
            nextElement = parserElements.get( i );
            if ( nextElement instanceof InsertElement )
            {
                /*
                 *  add it to the return ArrayList
                 */
                returnList.add( (InsertElement)parserElements.get( i ) );
            }
        }
        /*
         *  Remove all the elements in the returnList from the passed ArrayList
         */
        // parserElements.removeAll( returnList );
        return returnList;
    }


    /**
     *  Adds a feature to the ToSqlThread attribute of the MultiInsertBuilder
     *  object
     *
     *@param  commonElements  The feature to be added to the ToSqlThread
     *      attribute
     *@param  ddlElements     The feature to be added to the ToSqlThread
     *      attribute
     *@param  threadNo        The feature to be added to the ToSqlThread
     *      attribute
     */
    protected void addToSqlThread( ArrayList<InsertElement> commonElements, ArrayList<InsertElement> ddlElements, String threadNo )
    {
        InsertElement e;
        for ( int i = 0; i < commonElements.size(); i++ )
        {
            e = commonElements.get( i );
            String[] nums = e.getSqlThread().split( "," );
            for ( int j = 0; j < nums.length; j++ )
            {
                if ( nums[j].equals( threadNo ) )
                {
                    ddlElements.add( e );
                }
            }
        }
    }
}

/**
 *  Description of the Class
 *
 *@author     jchoyt
 *@created    July 7, 2004
 */
class OrderComparator implements Comparator
{


    /**
     *  Description of the Method
     *
     *@param  _de1  Description of the Parameter
     *@param  _de2  Description of the Parameter
     *@return       Description of the Return Value
     */
    public int compare( Object _de1, Object _de2 )
    {
        DdlElement de1 = ( DdlElement ) _de1;
        DdlElement de2 = ( DdlElement ) _de2;
        return ( int ) ( 100 * ( Float.parseFloat( de1.getOrder() ) - Float.parseFloat( de2.getOrder() ) ) );
    }
}

