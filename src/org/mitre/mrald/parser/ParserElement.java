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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.mitre.mrald.control.MsgObject;
import org.mitre.mrald.query.SqlElements;
import org.mitre.mrald.util.Config;
import org.mitre.mrald.util.FormTags;
import org.mitre.mrald.util.MraldException;

/**
 *  Common parent interface for all objects that can be parsed by the
 *  MraldParser.
 *
 *@author     jchoyt
 *@created    May 21, 2002
 */
public abstract class ParserElement
{
    /**
     *  Description of the Field
     */
    protected boolean isActive = true;
    /**
     *  Object holding all the name/value pairs parsed from the tag on the form
     */
    protected MsgObject nameValues;

    //Added for multiDB work
    protected String threadNumber =DEFAULT_THREAD_NO;
    public final static String DEFAULT_THREAD_NO = "0";
    /**
     *  Constructor for the QueryElements object
     *
     *@since
     */
    public ParserElement()
    {
        nameValues = new MsgObject();
    }


    /**
     *  Sets the isActive attribute of the ParserBuildable object
     *
     *@param  isActive  The new isActive value
     */
    public void setIsActive( boolean isActive )
    {
        this.isActive = isActive;
    }


    /**
     *  Gets the elementType of the ParserElement-derived object
     *
     *@return    The elementType value
     */
    public abstract String getElementType();


    /**
     *  Gets the isActive attribute of the ParserBuildable object
     *
     *@return    The isActive value
     */
    public boolean getIsActive()
    {
        return isActive;
    }


    /**
     *  Gets the nameValues attribute of the ParserElement object
     *
     *@return    The nameValues value
     */
    public MsgObject getNameValues()
    {
        return nameValues;
    }


    /**
     *  PostProcessor - carriers out any additional processing required. Things
     *  that must be done after process() is called should be added here.
     *
     *@param  msg                       Description of Parameter
     *@param  currentName               The name of the element currently being
     *      processed. This was found in the <code>name=""</code> attribute in
     *      the HTML.
     *@return                           The name of the element that should be
     *      removed from the main MsgObject Hashmap of elements to process. This
     *      usually is the same as the currentName paramenter passed in.
     *@exception  MraldParserException  Description of Exception
     *@since
     */
    public String postProcess( MsgObject msg, String currentName )
        throws MraldParserException
    {
        return currentName;
    }


    /**
     *  Preprocessor - carriers out any additional processing required. Things
     *  that must be done before process() is called should be added here.
     *
     *@param  msg                       Description of Parameter
     *@param  currentName               The name of the element currently being
     *      processed. This was found in the <code>name=""</code> attribute in
     *      the HTML.
     *@return                           The name of the element that should be
     *      removed from the main MsgObject Hashmap of elements to process. This
     *      usually is the same as the currentName paramenter passed in.
     *@exception  MraldParserException  Description of Exception
     *@since
     */
    public String preProcess( MsgObject msg, String currentName )
        throws MraldParserException
    {
        return currentName;
    }

    /**
     * Add the Thread number to allow for Multiple Database querying
     * Each ParserElement will need to be assigned to the correct database.
     * Each database is associated with a thread
     * @return
     */
    public String getThreadNumber()
    {
    	return threadNumber;
    }

    public void setThreadNumber(String thisThreadNumber)
    {
    	threadNumber= thisThreadNumber;
    }

    /**
     *  This method parses out the information stored in the HTML forms into a
     *  set of name/value pairs and stores them in the nameValues MsgObject.
     *
     *@param  valueList                 Description of Parameter
     *@exception  MraldParserException  Description of Exception
     */
    public void process( String[] valueList )
        throws MraldParserException
    {
        int numOfArrayLists = valueList.length;


        StringTokenizer valueTokens = null;
        String valueArray = null;
        String name = null;
        String value = null;


         /*
         *  If there is more than one Array List with the same name
         *  then these will be parsed up and added to the same Object.
         */
        for ( int i = 0; i < numOfArrayLists; i++ )
        {
//        MraldOutFile.logToFile( Config.getProperty("LOGFILE") , "ParserElement : process: " +  valueList[i] );

            valueArray = valueList[i];
            valueTokens = new StringTokenizer( valueArray, FormTags.TOKENIZER_STR );
            /*
             *  Parse out the ~ and loop
             */
            while ( valueTokens.hasMoreTokens() )
            {
                String nvp = valueTokens.nextToken();
                /*
                 *  Use the : as a token to get the name value pair.
                 */
                int splitPoint = nvp.indexOf( FormTags.NAMEVALUE_TOKEN_STR );
                if ( splitPoint == -1 )
                {
                    /*
                     *  Only one token - this is a value
                     *  Parse out the : and add the name value pair to the Hashmap
                     */
                    name = FormTags.VALUE_TAG;
                    value = nvp;
                }
                else
                {
                    name = nvp.substring( 0, splitPoint );
                    value = nvp.substring( splitPoint + FormTags.NAMEVALUE_TOKEN_STR.length() );
                    /*
                     *  some databases allow spaces in a table name.
                     *  Check here and put double quotes (") around any table name
                     *  Check to make sure the quotes aren't already there, as well
                     */
                    if ( value.length() > 0 && name.indexOf( FormTags.TABLE_TAG ) > -1 && value.charAt( 0 ) != '"'  && value.indexOf(' ') > -1 )
                    {
                        value = "\"" + value + "\"";
                    }
                }

                nameValues.setValue( name, value );
            }

            /* Set the thread with which to associate the object  */
            String sqlNo = nameValues.getValue( FormTags.SQL_THREAD_NUM_TAG )[0];

            if ( !sqlNo.equals( Config.EMPTY_STR ) )
            {
                setThreadNumber( sqlNo );
            }

        }
    }


    /**
     *  Description of the Method
     *
     *@return    Description of the Return Value
     */
    public String toString()
    {
        StringBuffer ret = new StringBuffer();
        ret.append( "\nThis is a(n) " );
        ret.append( getElementType() );
        ret.append( " element" );
        ret.append( "\nIt is " );
        if ( !isActive )
        {
            ret.append( "not " );
        }
        ret.append( "active." );
        ret.append( "\nIts type and memory location are " + super.toString() );
        ret.append( "\nIt contains the following in its nameValues: " );
        ret.append( nameValues.nameValuesToString() );
        return ret.toString();
    }
    /**
     *  This utility method is used to consolidate distributed MRALD tags in a
     *  submitted HTML form. For an extreme example, the TimeElement information
     *  is distributed across 10 HTML tag names. This kind of tag looks like:
     *  <pre>
     *Time1
     *Time1~EndDate
     *Time1~StartTime
     etc.
     *</pre> Where all HTML tags that start with Time1 must be processed
     *  together as one ParserElement. There may be another set of tags that
     *  start with Time2, which should be processed separately, so this method
     *  must grab everything that starts with Time1 - you can't rely on the
     *  parser.props file, and it's TimeElement starting string (Time).<br />
     *  <br />
     *  See the source of the {@link org.mitre.mrald.query.TimeElement#preProcess(MsgObject,
     *  String) TimeElement.preProcess()} and {@link
     *  org.mitre.mrald.query.FilterElement#preProcess(MsgObject, String)
     *  FilterElement.preProcess()} methods for examples.
     *
     *@param  msg          The workflow MsgObject
     *@param  currentName  The HTML tag name of the element to be processed
     *@param  groupTags    A String array of extensions to be processed
     *      together. For the example above, the groupTags would be { "EndDate",
     *      "StartTime" }.
     */
    public void collectElementGroup( MsgObject msg, String currentName, String[] groupTags )
    {
        /*
         *  check if you have the main element or not
         */
        String[] names = currentName.split( FormTags.TOKENIZER_STR );
        String mainName = names[0];
        if ( currentName.equals( mainName ) )
        {
            /*
             *  found the main one - go grab the rest - if you get an empty string
             *  back, you either pre-processed it already and you'll get the value in
             *  regular processing, or it had no value in which case you can ignore it.
             */
            String labelName;
            /*
             *  found the main one - go grab the rest - if you get an empty string
             *  back, you either pre-processed it already and you'll get the value in
             *  regular processing, or it had no value in which case you can ignore it.
             */
            String value;
            for ( int i = 0; i < groupTags.length; i++ )
            {
                labelName = mainName + FormTags.TOKENIZER_STR + groupTags[i];
                value = msg.getValue( labelName )[0];
                if ( value.equals( "" ) )
                {
                    /*
                     *  ignore it
                     */
                }
                else
                {
                    nameValues.setValue( groupTags[i], value );
                }
                msg.removeValue( labelName );
            }
        }
        else
        {
            String extension = names[1];
            /*
             *  just append the value to the main Element
             */
            String value = msg.getValue( currentName )[0];
            msg.setValue( mainName, extension + ":" + value );
            msg.removeValue( currentName );
        }
    }

    /**
     *  This method reorganises the SQLElements into a HashMap according to the
     *  sqlNo
     * GH 8/13/0: Add ability to use e.g, 1,2 to assign an element to some, but not all threads
     *@param  qe                         Description of the Parameter
     *@return                            Description of the Return Value
     *@exception  MraldException         Description of the Exception
     */
    public static HashMap<String, ArrayList<ParserElement>> orderElements( ArrayList<ParserElement> qe )
        throws MraldException
    {
        HashMap<String, ArrayList<ParserElement>> orderedElements = new HashMap<String, ArrayList<ParserElement>>();
        ParserElement thisElement = null;
        ArrayList<ParserElement> elements = new ArrayList<ParserElement>();
        Integer maxNo = new Integer( 0 );
        Integer thisNo = new Integer( 0 );
        //Have to distinguish beween the two as thisNo is 'counting'
        Integer newNo = new Integer( 0 );

         for ( int i = 0; i < qe.size(); i++ )
        {
            //Put into a HashMap according to sqlNo
            thisElement = qe.get( i );

            String threadNo = thisElement.getThreadNumber();

            if ( threadNo.equals( "all" ) )
            {
            	thisNo = new Integer( "-1" );
            	threadNo = "-1";
                thisElement.setThreadNumber( threadNo );
            }
            else if (threadNo.contains(","))
            {
            	String[] nums = threadNo.split( "," );
                for ( int j = 0; j < nums.length; j++ )
                {
                	thisNo = new Integer( nums[j] );
                	 //If maxNo < thisNo
                	String thisNoStr = thisNo.toString();
                    if ( maxNo.compareTo( thisNo ) < 0 )
                    {
                        maxNo = thisNo;
                    }
                    if ( orderedElements.containsKey( thisNoStr ) )
                    {
                        elements = orderedElements.get( thisNoStr );
                    }
                    else
                    {
                        elements = new ArrayList<ParserElement>();
                    }
                    thisElement.setThreadNumber(thisNoStr);

                    elements.add( thisElement );

                    orderedElements.put( thisNoStr, elements );

                }
            }
            else
            {
                thisNo = new Integer( threadNo );
            }

            if (!threadNo.contains(","))
            {
	            //If maxNo < thisNo
	            if ( maxNo.compareTo( thisNo ) < 0 )
	            {
	                maxNo = thisNo;
	            }
	            if ( orderedElements.containsKey( threadNo ) )
	            {
	                elements = orderedElements.get( thisElement.getThreadNumber() );
	            }
	            else
	            {
	                elements = new ArrayList<ParserElement>();
	            }
	             elements.add( thisElement );

	            orderedElements.put( thisElement.getThreadNumber(), elements );
            }
        }

        maxNo = new Integer( maxNo.intValue() + 1 );
        //Any remaining objects did not have a sqlNo -
        //by default these are the final sqlElements
        //used in the Ordinary output
        ArrayList elementsWithAll = orderedElements.get( "-1" );
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
                    thisElement.setThreadNumber( newNo.toString() );

                    elements = orderedElements.get( thisElement.getThreadNumber() );

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
                        orderedElements.put( thisElement.getThreadNumber(), new ArrayList<ParserElement>( elements ) );
                    }
                }
            }
            orderedElements.remove( "-1" );

        }


        if ( orderedElements.get( DEFAULT_THREAD_NO ) == null )
        {
            //MraldOutFile.appendToFile(Config.getProperty("LOGFILE") "MQB: Not found for key " +SqlElements.DEFAULT_SQL_NO );

            return orderedElements;
        }
        else
        {
            //MraldOutFile.appendToFile(Config.getProperty("LOGFILE") "MQB: Found for key " +SqlElements.DEFAULT_SQL_NO );
            elements = orderedElements.get( DEFAULT_THREAD_NO );

            //MraldOutFile.appendToFile(Config.getProperty("LOGFILE") "MQB: Size of Array for default " + elements.size() );

            orderedElements.put( maxNo.toString(), orderedElements.get( DEFAULT_THREAD_NO ) );
        }
        //MraldOutFile.appendToFile(Config.getProperty("LOGFILE") "MQB: Size after " + orderedElements.size() );

        orderedElements.remove( DEFAULT_THREAD_NO );

        //MraldOutFile.appendToFile(Config.getProperty("LOGFILE") "MQB: Size of Array for moved default " + elements.size() );

        //MraldOutFile.appendToFile(Config.getProperty("LOGFILE") "MQB: Size after " + orderedElements.size() );
        return orderedElements;
    }
}

