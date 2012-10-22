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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.mitre.mrald.control.AbstractStep;
import org.mitre.mrald.control.MsgObject;
import org.mitre.mrald.parser.ParserElement;
import org.mitre.mrald.util.Config;
import org.mitre.mrald.util.FormTags;
import org.mitre.mrald.util.MiscUtils;
import org.mitre.mrald.util.MraldException;
import org.mitre.mrald.util.MraldOutFile;
/**
 *  This QueryBuilder class builds the query string from a list of QueryElement
 *  objects.
 *
 *@author     Brian Blake
 *@created    February 5, 2001
 *@version    1.0
 *@see        mrald.presentation.QueryElement
 */
public class QueryBuilder extends AbstractStep
{
    private ArrayList<String> fromStrings = new ArrayList<String>();
    private ArrayList<String> groupByStrings = new ArrayList<String>();
    private MsgObject msg;
    private ArrayList<String> orderByStrings = new ArrayList<String>();
//    private int queryLimit = -1;
//SQLComponents
    private ArrayList<String> selectStrings = new ArrayList<String>();
    private ArrayList<String> whereAndStrings = new ArrayList<String>();
    //private ArrayList whereOrStrings = new ArrayList();

    /**
     *  Constructor for the QueryBuilder object
     *
     *@since
     */
    public QueryBuilder()
    {
    }

    public QueryBuilder(MsgObject msg)
    {
        this.msg = msg;
    }


    /**
     *  This method is part of the AbstractStep interface and is called from the
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
            setQueryLimit();
            buildQueryComponents( msgObject.getWorkingObjects() );
            msgObject.setQuery( buildQuery() );
        }
        catch ( MraldException e )
        {
            throw new org.mitre.mrald.control.WorkflowStepException( e );
        }
    }


    /**
     *  This method sets the Query output limit for referencing the output formatting.
     *
     *@exception  MraldException  Description of the Exception
     */
    private void setQueryLimit()
        throws MraldException
    {
        try
        {
            String[] qSize = msg.getValue( "outputSize" );
            if ( qSize[0].startsWith( "line" ) )
            {
//                String[] limit = msg.getValue( "outputLinesCount" );
//                queryLimit = Integer.parseInt( limit[0] ) + 1;
            }
        }
        catch ( Exception nfe )
        {
            MraldException otherException = new MraldException( nfe.getMessage() );
            throw otherException;
        }
    }

    /**
     *  This methods builds the appropriate SQL statment for the query, given
     *  the pre-generated query components
     *
     *@return                     Description of the Return Value
     *@exception  MraldException  Description of the Exception
     */
    public String buildQuery()
        throws MraldException
    {
        String finalQueryString = "";
        try
        {
            String start = "SELECT ";
            /*
             *  find out if the user wants duplicate entries or not
             *  if not, add DISTINCT to the query
             */
            if ( !msg.getValue( "showDuplicates" )[0].equals( "true" ) )
            {
                start += "DISTINCT ";
            }
            finalQueryString = order( selectStrings, start );
            /*
             *  need to run this here to see if any new tables need to be added
             *  to fromStrings.  Any table mentioned in the WHERE clause needs to be
             *  in the FROM clause as well.  runDijkstra will add tables to fromStrings.
             *  The link string will be appended to the query below after the WhereOrStrings
             *  are appended.
             */
            MraldDijkstra links = new MraldDijkstra( msg );
            ArrayList<LinkElement> linkArray = msg.getLinks();
            MraldOutFile.logToFile(Config.getProperty("LOGFILE"), "QueryBuilder : execute: get number of Links " + linkArray.size());

            for (LinkElement test:linkArray)
            {
            	MraldOutFile.logToFile(Config.getProperty("LOGFILE"), "QueryBuilder : execute: get Links " + test.toString());
            }
            String linkString = links.runDijkstra( fromStrings );
            finalQueryString += " From ";
            for ( int l = 0; l < fromStrings.size(); l++ )
            {
                String this_fstr = fromStrings.get( l );
                finalQueryString += this_fstr;
                if ( l < fromStrings.size() - 1 )
                {
                    finalQueryString += ", ";
                }
            }
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
            /*
             *  for ( int l = 0; l < whereOrStrings.size(); l++ )
             *  {
             *  String this_wstr = ( String ) whereOrStrings.get( l );
             *  finalQueryString += this_wstr;
             *  }
             */
            if ( !linkString.equals( "" ) )
            {
                if ( ( finalQueryString.indexOf( " Where " ) != -1 ) )
                {
                    finalQueryString += " AND " + linkString;
                }
                else
                {
                    finalQueryString += " Where " + linkString;
                }
            }
            finalQueryString += order( groupByStrings, " GROUP BY " );
            finalQueryString += order( orderByStrings, " ORDER BY " );
            finalQueryString = MiscUtils.clearSemiColon( finalQueryString );
            //strip off everything after a ';' or '--' to prevent SQL insertion attacks
            MraldOutFile.logToFile(Config.getProperty("LOGFILE"), "QueryBuilder: query "  + finalQueryString);

            return finalQueryString;
        }
        catch ( MraldException cde )
        {
        	// for ( int l = 0; l < fromStrings.size(); l++ )
            // {
        	// 	MraldOutFile.logToFile("From Strings : " + fromStrings.get(l));
            // }
            MraldException e = new MraldException( "In QueryBuilder.buildQuery(): " + cde.getMessage() + "\nQuery so far is: " + finalQueryString );
            throw e;
        }
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
    public void buildQueryComponents( ArrayList qe )
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
            //whereOrStrings = this_qe.buildWhereOr( whereOrStrings );
            selectStrings = this_qe.buildSelect( selectStrings );
            orderByStrings = this_qe.buildOrderBy( orderByStrings );
            groupByStrings = this_qe.buildGroupBy( groupByStrings );
            // MraldOutFile.appendToFile( this_qe.buildValue() );
        }
    }


    /**
     *  Returns the Contents of the vector as comma delimited String, preceded
     *  by the leadIn
     *
     *@param  orderByStrings  - must contain only Strings
     *@param  leadIn          - The string to append to the beginning of the
     *      list of return String
     *@return                 Description of the Return Value
     *@since
     */
    private String order( ArrayList orderByStrings, String leadIn )
    {
        String returnString = "";
        if ( orderByStrings.size() != 0 )
        {
            StringTokenizer thisField;
            TreeMap<Float,String> orderList = new TreeMap<Float,String>();
            Float selectPos = new Float( 0 );
            String fieldName;
            //GH HACK ALERT!!!! +50 because unlikely that there will be
            //this many output selections - jch: until ETMS - now 100
            int lastString = orderByStrings.size() + 300;
            returnString += leadIn;
            for ( int h = 0; h < orderByStrings.size(); h++ )
            {
                thisField = new StringTokenizer( orderByStrings.get( h ).toString(), FormTags.TOKENIZER_STR );
                fieldName = thisField.nextToken();
                selectPos =
                            thisField.hasMoreTokens()
                            ? new Float( thisField.nextToken() )
                            : new Float( ++lastString );
                orderList.put( selectPos, fieldName );
            }
            Collection sortedValues = orderList.values();
            for ( Iterator it = sortedValues.iterator(); it.hasNext();  )
            {
                returnString += it.next();
                if ( it.hasNext() )
                {
                    returnString += ", ";
                }
            }
        }
        return returnString;
    }
}

