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
import java.util.Calendar;

import org.mitre.mrald.control.MsgObject;
import org.mitre.mrald.util.Config;
import org.mitre.mrald.util.FormTags;
import org.mitre.mrald.util.MraldException;
import org.mitre.mrald.util.TimeFormatException;
import org.mitre.mrald.util.TimeFormatter;

/**
 *  The SqlElement class is the abstract class for all of the query elements
 *  templates needed to populate a complete query string. This class contains
 *  the common functionality for building query strings in general
 *
 *@author     Gail Hamilton
 *@created    February 17, 2001
 *@version    1.0
 *@see        java.lang.Object
 */
public class TimeElement extends SqlElements
{
	 private boolean endDateSelected = false;
	 private boolean startDateSelected = false;


    /**
     *  Constructor for the QueryElements object
     *
     *@since
     */
    public TimeElement()
    {
        super();
        elementType =  FormTags.TIME_TAG;

    }


    /**
     *  Description of the Method
     *
     *@param  currentFromList  Description of Parameter
     *@return                  Description of the Returned Value
     *@since
     */
    public ArrayList<String> buildFrom( ArrayList<String> currentFromList )
    {

        String newValue = nameValues.getValue( FormTags.TABLE_TAG )[0];
        String synValue = nameValues.getValue( FormTags.SYN_TAG )[0];
        if ( !synValue.equals( Config.EMPTY_STR ) )
        {
            newValue = newValue + " " + synValue;
        }
        if ( !currentFromList.contains( newValue ) )
        {
            currentFromList.add( newValue );
        }
        return currentFromList;
    }



    /**
     *  Description of the Method
     *
     *@param  currentGroupByList  List of Group By parameters
     *@return                     Description of the Returned Value
     *@exception  MraldException  Description of Exception
     *@since
     */
    public ArrayList<String> buildGroupBy( ArrayList<String> currentGroupByList )
        throws MraldException
    {
        return currentGroupByList;
    }


    /**
     *  Description of the Method
     *
     *@param  currentOrderBy      Description of Parameter
     *@return                     Description of the Returned Value
     *@exception  MraldException  Description of Exception
     *@since
     */
    public ArrayList<String> buildOrderBy( ArrayList<String> currentOrderBy )
        throws MraldException
    {
        return currentOrderBy;
    }


    /**
     *  Description of the Method
     *
     *@param  currentSelectList   Description of Parameter
     *@return                     Description of the Returned Value
     *@exception  MraldException  Description of Exception
     *@since
     */
    public ArrayList<String> buildSelect( ArrayList<String> currentSelectList )
        throws MraldException
    {

        return currentSelectList;
    }


    /**
     *  Description of the Method
     *
     *@param  currentWhereList    Description of the Parameter
     *@return                     Description of the Returned Value
     *@exception  MraldException  Description of Exception
     *@since
     */
    public ArrayList<String> buildWhere( ArrayList<String> currentWhereList )
        throws MraldException
    {
        //Build the Where clause using items in the
        try
        {


            String newValue = " ";
            int noOfValues = nameValues.getValue( FormTags.VALUE_TAG ).length;

            if ( noOfValues == 0 )
            {
                return currentWhereList;
            }

            //table = nameValues.getValue(FormTags.TABLE_TAG)[0].toString();
            String field = nameValues.getValue( FormTags.FIELD_TAG )[0].toString();
            String synValue = nameValues.getValue( FormTags.SYN_TAG )[0];

//            String thisOperator = null;
            String startValue = null;
            String endValue = null;

            int i = 0;
            for ( i = 0; i < noOfValues; i++ )
            {

                String startDate = nameValues.getValue( FormTags.STARTDATE )[i];
                String startTime = nameValues.getValue( FormTags.STARTTIME )[i];

                startValue = TimeFormatter.formatDateTime( nameValues.getValue( FormTags.STARTDATE )[i] + " " + nameValues.getValue( FormTags.STARTTIME )[i], false ).toString();

                //If the endDate was selected then use this as the end date.
                //Otherwise calculate the endDate
                if ( endDateSelected )
                {
                    endValue = TimeFormatter.formatDateTime( nameValues.getValue( FormTags.ENDDATE )[i] + " " + nameValues.getValue( FormTags.ENDTIME )[i], false ).toString();
                }
                else if (startDateSelected)
                {

                	String endDate = nameValues.getValue( FormTags.ENDDATE )[i];
                    String endTime = nameValues.getValue( FormTags.ENDTIME )[i];

                 	endValue = TimeFormatter.formatDateTime( endDate + " " + endTime, false ).toString();

                	Calendar startValueCal = buildTimeStart( i, endDate + " " + endTime );

                    //Get the string value in the correct format
                    startValue = TimeFormatter.getTimeAsString( startValueCal );

                }
                else
                {

                    Calendar endValueCal = buildTimeEnd( i, startDate + " " + startTime );

                    //Get the string value in the correct format
                    endValue = TimeFormatter.getTimeAsString( endValueCal );

                }
                synValue = nameValues.getValue( FormTags.SYN_TAG )[0];
                if ( !synValue.equals( Config.EMPTY_STR ) )
                {
                    field = synValue + "." + nameValues.getValue( FormTags.FIELD_TAG )[i];

                }
                else
                {
                    field = nameValues.getValue( FormTags.TABLE_TAG )[i] + "." + nameValues.getValue( FormTags.FIELD_TAG )[i];

                }
                newValue = newValue + " ( " + field + " >= to_date('" + startValue
                         + "', '" + TimeFormatter.ORACLE_SQL_TIME_FORMAT + "')"
                         + " AND " +
                        field + " <= to_date('" + endValue
                         + "', '" + TimeFormatter.ORACLE_SQL_TIME_FORMAT + "') ) ";
            }

            if ( !currentWhereList.contains( newValue ) )
            {
                currentWhereList.add( newValue );
            }

            return currentWhereList;
        }
        /*
         *  catch( MsgObjectException e)
         *  {
         *  MraldException mess = new MraldException( e.getMessage());
         *  throw mess;
         *  }
         */
        catch ( TimeFormatException e )
        {
            MraldException mess = new MraldException( e.getMessage() );
            throw mess;
        }
    }


    /**
     *  Preprocessor - carriers out any additional processing required.
     *
     *@param  msg          Description of the Parameter
     *@param  currentName  Description of the Parameter
     *@return              Description of the Return Value
     *@since
     */
    public String preProcess( MsgObject msg, String currentName )
    {
        if ( msg.getValue( FormTags.ENABLETIME )[0].equals( FormTags.ENDTIME ) )
        {
            endDateSelected = true;
        }
        else if ( msg.getValue( FormTags.ENABLETIME )[0].equals( FormTags.STARTTIME ))
        {
        	 startDateSelected = true;
        }
        String[] groupTags = {
                FormTags.DAY_TAG,
                FormTags.ENABLETIME,
                FormTags.ENDDATE,
                FormTags.ENDTIME,
                FormTags.FUNCTION,
                FormTags.HOUR_TAG,
                FormTags.MINUTE_TAG,
                FormTags.MONTH_TAG,
                FormTags.SECOND_TAG,
                FormTags.STARTDATE,
                FormTags.STARTTIME,
                FormTags.TIME_TAG
                };
        collectElementGroup( msg, currentName, groupTags );
        return currentName;
    }


    /**
     *  Description of the Method
     *
     *@param  i                   Description of the Parameter
     *@param  startTimeVal        Description of the Parameter
     *@return                     Description of the Returned Value
     *@exception  MraldException  Description of Exception
     *@since
     */
    private Calendar buildTimeEnd( int i, String startTimeVal )
        throws MraldException
    {
        try
        {

            String empty = Config.EMPTY_STR;
            Calendar startTime = ( Calendar ) TimeFormatter.formatDateTime( startTimeVal, true );

            //Month, day, hour, minutes, seconds
            String monthAdd = nameValues.getValue( FormTags.MONTH_TAG )[i];
            String dayAdd = nameValues.getValue( FormTags.DAY_TAG )[i];
            String hourAdd = nameValues.getValue( FormTags.HOUR_TAG )[i];
            String minuteAdd = nameValues.getValue( FormTags.MINUTE_TAG )[i];
            String secondsAdd = nameValues.getValue( FormTags.SECOND_TAG )[i];

            //Check to see if the user has selected a value to add
            //to the startDate
            if ( ( monthAdd.equals( empty ) ) && ( dayAdd.equals( empty ) ) && ( hourAdd.equals( empty ) ) && ( minuteAdd.equals( empty ) ) &&
                    ( secondsAdd.equals( empty ) ) )
            {
                MraldException noTime = new MraldException( "You must select a date to add." );
                throw noTime;
            }

            if ( !monthAdd.equals( empty ) )
            {
                startTime.add( Calendar.MONTH, new Integer( monthAdd ).intValue() );
            }
            if ( !dayAdd.equals( empty ) )
            {
                startTime.add( Calendar.DAY_OF_MONTH, new Integer( dayAdd ).intValue() );
            }
            if ( !hourAdd.equals( empty ) )
            {
                startTime.add( Calendar.HOUR, new Integer( hourAdd ).intValue() );
            }
            if ( !minuteAdd.equals( empty ) )
            {
                startTime.add( Calendar.MINUTE, new Integer( minuteAdd ).intValue() );
            }
            if ( !secondsAdd.equals( empty ) )
            {
                startTime.add( Calendar.SECOND, new Integer( secondsAdd ).intValue() );
            }

            // Subtract one millisecond from the end time

            startTime.set( Calendar.MILLISECOND, 0 );
            startTime.add( Calendar.MILLISECOND, -1 );

            return startTime;
        }
        catch ( TimeFormatException e )
        {
            MraldException qbe = new MraldException( e.getMessage() );
            throw qbe;
        }
    }

    /**
     *  Description of the Method
     *
     *@param  i                   Description of the Parameter
     *@param  startTimeVal        Description of the Parameter
     *@return                     Description of the Returned Value
     *@exception  MraldException  Description of Exception
     *@since
     */
    private Calendar buildTimeStart( int i, String startTimeVal )
        throws MraldException
    {
        try
        {

            String empty = Config.EMPTY_STR;
            Calendar startTime = ( Calendar ) TimeFormatter.formatDateTime( startTimeVal, true );

            //Month, day, hour, minutes, seconds
            String monthAdd = nameValues.getValue( FormTags.MONTH_TAG )[i];
            String dayAdd = nameValues.getValue( FormTags.DAY_TAG )[i];
            String hourAdd = nameValues.getValue( FormTags.HOUR_TAG )[i];
            String minuteAdd = nameValues.getValue( FormTags.MINUTE_TAG )[i];
            String secondsAdd = nameValues.getValue( FormTags.SECOND_TAG )[i];

            //Check to see if the user has selected a value to add
            //to the startDate
            if ( ( monthAdd.equals( empty ) ) && ( dayAdd.equals( empty ) ) && ( hourAdd.equals( empty ) ) && ( minuteAdd.equals( empty ) ) &&
                    ( secondsAdd.equals( empty ) ) )
            {
                MraldException noTime = new MraldException( "You must select a date to add." );
                throw noTime;
            }

            int negInt = -1;
            if ( !monthAdd.equals( empty ) )
            {

                startTime.add( Calendar.MONTH, new Integer( monthAdd ).intValue() * negInt );
            }
            if ( !dayAdd.equals( empty ) )
            {
                startTime.add( Calendar.DAY_OF_MONTH, new Integer( dayAdd ).intValue() * negInt);
            }
            if ( !hourAdd.equals( empty ) )
            {
                startTime.add( Calendar.HOUR, new Integer( hourAdd ).intValue() * negInt );
            }
            if ( !minuteAdd.equals( empty ) )
            {
                startTime.add( Calendar.MINUTE, new Integer( minuteAdd ).intValue() * negInt );
            }
            if ( !secondsAdd.equals( empty ) )
            {
                startTime.add( Calendar.SECOND, new Integer( secondsAdd ).intValue() * negInt );
            }

            // Subtract one millisecond from the end time

            startTime.set( Calendar.MILLISECOND, 0 );
            startTime.add( Calendar.MILLISECOND, 1 );

            return startTime;
        }
        catch ( TimeFormatException e )
        {
            MraldException qbe = new MraldException( e.getMessage() );
            throw qbe;
        }
    }


    /**
     *  This element is invalid if there are no time field associated with it.
     *  Therefore in the postprocessing we check to make sure we have a valid
     *  Table and Field. If there isn't one, set this element's isActive to
     *  false. (unless, of course, the operator is IS NULL or IS NOT NULL)
     *
     *@param  msg          Description of the Parameter
     *@param  currentName  Description of the Parameter
     *@return              Description of the Return Value
     *@since
     */
    public String postProcess( MsgObject msg, String currentName )
    {
        String[] table = nameValues.getValue( FormTags.TABLE_TAG );
        String[] field = nameValues.getValue( FormTags.FIELD_TAG );
        if ( table[0].equals(Config.EMPTY_STR) || field[0].equals(Config.EMPTY_STR)  )
        {
            isActive = false;
        }
        return currentName;
    }
}

