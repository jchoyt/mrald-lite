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
package org.mitre.mrald.query;

import java.util.ArrayList;
import java.util.Iterator;

import org.mitre.mrald.control.AbstractStep;
import org.mitre.mrald.control.MsgObject;
import org.mitre.mrald.control.WorkflowStepException;
import org.mitre.mrald.util.MraldException;

/**
 *  This QueryBuilder class builds the query string from a list of QueryElement
 *  objects.
 *
 *@author     Brian Blake
 *@created    February 5, 2001
 *@version    1.0
 *@see        mrald.presentation.QueryElement
 */
public class UpdateBuilder extends AbstractStep
{
    private ArrayList<String> fromStrings = new ArrayList<String>();
    private MsgObject msg;
    private ArrayList<String> selectStrings = new ArrayList<String>();
    private ArrayList<String> whereAndStrings = new ArrayList<String>();

    /**
     *  Constructor for the QueryBuilder object
     *
     *@since
     */
    public UpdateBuilder() { }


    /**
     *  This method is part of the AbstractStep interface and it called from the
     *  workflow controller.
     *
     *@param  msgObject                                          Description of
     *      the Parameter
     *@exception  org.mitre.mrald.control.WorkflowStepException  Description of
     *      the Exception
     */
    public void execute( MsgObject msgObject )
        throws org.mitre.mrald.control.WorkflowStepException
    {
        try
        {
            msg = msgObject;
            buildQueryComponents( msgObject.getWorkingObjects() );
            String query = buildQuery();
            msgObject.setQuery( query );
        }
        catch ( MraldException e )
        {
            throw new WorkflowStepException( e, msg );
        }
    }

    /**
     *  This methods builds the appropriate SQL statment for the query, given
     *  the pre-generated query components
     *
     *@return    Description of the Return Value
     */
    private String buildQuery()
        throws MraldException
    {
        String finalQueryString = "UPDATE ";
        if ( fromStrings.size() > 1 )
        {
            // MraldOutFile.logToFile( "UpdateBuilder : buildQuery: first Table : " + fromStrings.get( 0 ) + " second table : " + fromStrings.get( 1 ) );
            throw new MraldException( "You may only update one table at a time" );
        }
        //Should only be one table
        String this_fstr = fromStrings.get( 0 );
        finalQueryString += this_fstr;
        finalQueryString += values( selectStrings, " SET" );
        if ( whereAndStrings.size() > 0 )
        {
            finalQueryString += " Where ";
            for ( int l = 0; l < whereAndStrings.size(); l++ )
            {
                String this_wstr = whereAndStrings.get( l );
                finalQueryString += this_wstr;
                if ( l < whereAndStrings.size() - 1 )
                {
                    finalQueryString += " AND ";
                }
            }
        }
        return finalQueryString;
    }


    /**
     *  This method builds the Query Components from an Arraylist of SqlElements
     *  - each sqlElement is responsible for adding the appropriate string to
     *  the different compnent type (from clause, select clause, group bys,
     *  etc.)
     *
     *@param  qe                  Description of the Parameter
     *@exception  MraldException  Description of the Exception
     */
    private void buildQueryComponents( ArrayList qe )
        throws MraldException
    {
        /*
         *  Check to see if the user has entered any output - if not throw exceptiom
         */
        if ( ( qe.size() ) == 0 )
        {
            throw new MraldException( "You must select output data to proceed. Please make selection from Output Data Selection list and resubmit." );
        }
        /*
         *  Each element can handle its portion of the query
         *  Add all query strings specific to Output data
         */
        SqlElements this_qe = null;
        for ( int k = 0; k < qe.size(); k++ )
        {
//            Object temp = qe.get( k );
            this_qe = ( SqlElements ) qe.get( k );
            /*
             *  If this object is null then move to the next element
             */
            if ( this_qe == null )
            {
                continue;
            }
            fromStrings = this_qe.buildFrom( fromStrings );
            whereAndStrings = this_qe.buildWhereAnd( whereAndStrings );
            selectStrings = this_qe.buildSelect( selectStrings );
        }
    }


    /**
     *  Returns the Contents of the vector as comma delimited String, preceded
     *  by the leadIn
     *
     *@param  selectStrings  Description of the Parameter
     *@param  leadIn         - The string to append to the beginning of the list
     *      of return String
     *@return                Description of the Return Value
     *@since
     */
    private String values( ArrayList selectStrings, String leadIn )
    {
        String returnString = leadIn + " ";
        for ( Iterator it = selectStrings.iterator(); it.hasNext();  )
        {
            returnString += it.next();
            if ( it.hasNext() )
            {
                returnString += ", ";
            }
        }
        return returnString;
    }
}

