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
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.mitre.mrald.control.AbstractStep;
import org.mitre.mrald.control.MsgObject;
import org.mitre.mrald.control.WorkflowStepException;
import org.mitre.mrald.ddlelements.DdlElementComparator;
import org.mitre.mrald.ddlelements.EmptyInsertElement;
import org.mitre.mrald.ddlelements.InsertElement;
import org.mitre.mrald.parser.ParserElement;
import org.mitre.mrald.util.Config;
import org.mitre.mrald.util.FormTags;
import org.mitre.mrald.util.MraldException;
import org.mitre.mrald.util.MraldOutFile;
import org.mitre.mrald.util.Snoop;

/**
 *  Builds Insert DDL statements form the InsertElements stored in the MsgObject
 *  passed to the execute method.
 *
 *@author     jchoyt
 *@created    December 1, 2002
 */
public class InsertBuilder extends AbstractStep
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
        /*
         *  split up by table
         */
        ArrayList<InsertElement> elements = new ArrayList<InsertElement>();
        ArrayList<InsertElement> commonElements = new ArrayList<InsertElement>();
        String ddl;
        InsertElement currentElement;
        String tableName = "";
        Iterator iter = insertElements.iterator();
        for ( int i = 0; i < insertElements.size(); i++ )
        {
            currentElement = ( InsertElement ) iter.next();
            MraldOutFile.logToFile(Config.getProperty("LOGFILE"), "InsertBuilder buildDDl: size " + currentElement.toString() );
            
            /*
             *  grab all the common elements.  This only works because the elements
             *  are already sorted.  I've chosen the value !All for the table name so that
             *  they are all sorted to the front.  AFAIK, all tables must start with a letter
             *  since !All starts with !, they should all sort to the front of the list
             */
            if ( currentElement.getTable().equals( FormTags.INSERT_INTO_ALL_TABLES ) )
            {
                commonElements.add( currentElement );
                continue;
            }
            if ( !tableName.equals( currentElement.getTable() ) )
            {
                tableName = currentElement.getTable();
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
                 */
                elements.addAll( commonElements );
                ddl = constructSingleDdl( elements );
                returnDdl.add( ddl );
                elements = new ArrayList<InsertElement>();
            }
            elements.add( currentElement );
        }

        if ( elements.size() > 0 )
        {
            elements.addAll( commonElements );
            ddl = constructSingleDdl( elements );
            returnDdl.add( ddl );
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
    public String constructSingleDdl( ArrayList elements )
        throws MraldException
    {
        /*
         *  get the strings from the InsertElements
         */
        ArrayList<String> fieldStrings = new ArrayList<String>();
        ArrayList<String> valueStrings = new ArrayList<String>();
        InsertElement currentElement;
        for ( int i = 0; i < elements.size(); i++ )
        {
            currentElement = ( InsertElement ) elements.get( i );
            fieldStrings = currentElement.buildFieldList( fieldStrings );
            valueStrings = currentElement.buildValueList( valueStrings );
        }
        /*
         *  assemble the statement - do it in two halves, then append them
         */
        String tableName = ( ( InsertElement ) elements.get( 0 ) ).getTable();
        StringBuffer start = new StringBuffer();
        start.append( "Insert into " );
        start.append( tableName );
        start.append( " ( " );
        StringBuffer end = new StringBuffer();
        end.append( " ) values ( " );
        for ( int i = 0; i < fieldStrings.size(); i++ )
        {
            start.append( fieldStrings.get( i ) );
            end.append( valueStrings.get( i ) );
            if ( i < fieldStrings.size() - 1 )
            {
                start.append( ", " );
                end.append( ", " );
            }
        }
        end.append( " )" );

        start.append( end );
        return start.toString();
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
            
            Snoop.logParameters( msg);
            /*
             *  extract the InsertElements from the parser elements and sort them by the DdlComparator
             */
            ArrayList<ParserElement> parserElements = msgObject.getWorkingObjects();
            List<InsertElement> c = extractInsertElements( parserElements );
            MraldOutFile.logToFile(Config.getProperty("LOGFILE"), "InsertBuilder  Insert Elements: size " + c.size() );
        	
            removeInvalidElements( c );
            MraldOutFile.logToFile(Config.getProperty("LOGFILE"), "InsertBuilder after extract Insert Elements: size " + c.size() );
            
            // TreeSet sortedInsertElements = new TreeSet( new DdlElementComparator() );
            // sortedInsertElements.addAll( c );
            Collections.sort( c, new DdlElementComparator() );
            /*
             *  build the DDL statements (one for each table listed in the InsertElemetns)
             *  and store them in the MsgObject query field for the next workflow step to use
             */
            String ddl[] = buildDdl( c );
            for ( int i = 0; i < ddl.length; i++ )
            {
                msgObject.setQuery( ddl[i] );
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
        //String matchType = (new InsertElement()).getElementType();
        for ( int i = 0; i < parserElements.size(); i++ )
        {
            nextElement = parserElements.get( i );
            if ( nextElement instanceof InsertElement )
            {
                /*
                 *  add it to the return ArrayList
                 */
                if ( !returnList.contains( nextElement ) )
                {
                    returnList.add( (InsertElement)nextElement );
                }
            }
        }
        /*
         *  Remove all the elements in the returnList from the passed ArrayList
         */
        //parserElements.removeAll( returnList );
        return returnList;
    }


    /**
     *  Removes all invalid InsertElements from the passed Collection. An
     *  invalid InsertElement is one with no Value provided.
     *
     *@param  c                   A Collection of InsertElements
     *@exception  MraldException  Description of the Exception
     */
    public void removeInvalidElements( Collection<InsertElement> c )
        throws MraldException
    {

        Iterator iter = c.iterator();
        InsertElement currentElement;
        ArrayList<String> valueStrings = new ArrayList<String>();
        int lastSize = -1;
        while ( iter.hasNext() )
        {
        	 MraldOutFile.logToFile(Config.getProperty("LOGFILE"), "InsertBuilder removing Invalid Elements " + c.size() );
                     	
            currentElement = ( InsertElement ) iter.next();

            MraldOutFile.logToFile(Config.getProperty("LOGFILE"), "InsertBuilder removing Invalid Elements " + currentElement );
            
            valueStrings = currentElement.buildValueList( valueStrings );

            MraldOutFile.logToFile(Config.getProperty("LOGFILE"), "InsertBuilder removing Invalid Elements value string " + valueStrings);
            
            if ( valueStrings.size() == lastSize && !( currentElement instanceof EmptyInsertElement ) )
            {
            	 MraldOutFile.logToFile(Config.getProperty("LOGFILE"), "InsertBuilder removing Invalid Elements about to remove "  + currentElement);
                 
                iter.remove();

            }
            lastSize = valueStrings.size();
        }
    }
}

