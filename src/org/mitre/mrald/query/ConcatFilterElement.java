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

import org.mitre.mrald.control.MsgObject;
import org.mitre.mrald.util.Config;
import org.mitre.mrald.util.FormTags;
import org.mitre.mrald.util.MraldException;

/**
 *  Used to insert a filter on two concatenated fields. It automatically assumes
 *  that if you have more than one value, that you mean to 'OR' them over the
 *  same table and field name. See the buildWhere method for more detail. HTML
 *  tags should be of the form:<br>
 *  <code>Table1:OOOICOMMON~Field1:CARRIER~Syn1:oooi1~Table2:OAGCOMMON~Field2:CARRIER~Syn2:oag1~Operator:LIKE~Value:TWAUAL</code>
 *  <br>
 *  where the Syn and Operator (defaults to '=') tags are optional.
 *
 *@author     jchoyt
 *@created    July 11, 2002
 */
public class ConcatFilterElement extends FilterElement
{

//    private String filterOperator = " = ";


    /**
     *  Constructor for the QueryElements object
     *
     *@since
     */
    public ConcatFilterElement()
    {
        super();
        elementType = FormTags.CONCAT_FILTER_TAG;
    }


    /**
     *  Constructor for the QueryElements object
     *
     *@param  msg  Description of Parameter
     *@since
     */
    public ConcatFilterElement( MsgObject msg )
    {
        super( msg );
        elementType = FormTags.CONCAT_FILTER_TAG;
    }


    /**
     *  Builds a value String for a name value pair from the given parameters.
     *  Ex: <code>Table:ETMSCOMMON~Field:CARRIER~Operator:=~Value:UAS</code>
     *
     *@param  currentFromList            Description of the Parameter
     *@return                            A string suitable for a HTML tag in a
     *      form.
     *@exception  MraldException  Description of the Exception
     */
    /*
     *  public static String buildValue(String table, String synonym, String field, String operator, String value)
     *  {
     *  StringBuffer ret = new StringBuffer();
     *  ret.append(FormTags.TABLE_TAG);
     *  ret.append(":");
     *  ret.append(table);
     *  if (synonym != null)
     *  {
     *  ret.append("~");
     *  ret.append(FormTags.SYN_TAG);
     *  ret.append(":");
     *  ret.append(synonym);
     *  }
     *  ret.append("~");
     *  ret.append(FormTags.FIELD_TAG);
     *  ret.append(":");
     *  ret.append(field);
     *  ret.append("~");
     *  ret.append(FormTags.OPERATOR_TAG);
     *  ret.append(":");
     *  ret.append(operator);
     *  ret.append("~");
     *  ret.append(FormTags.VALUE_TAG);
     *  ret.append(":");
     *  ret.append(value);
     *  return ret.toString();
     *  }
     */
    /**
     *  Description of the Method
     *
     *@param  currentFromList            Description of Parameter
     *@return                            Description of the Returned Value
     *@exception  MraldException  Description of Exception
     *@since
     */
    public ArrayList<String> buildFrom( ArrayList<String> currentFromList )
        throws MraldException
    {
        /*
         *  add the first table
         */
        String newValue = nameValues.getValue( FormTags.TABLE_TAG + "1" )[0];
        String synValue = nameValues.getValue( FormTags.SYN_TAG + "1" )[0];
        if ( !synValue.equals( Config.EMPTY_STR ) )
        {
            newValue = newValue + " " + synValue;
        }
        if ( !newValue.equals( Config.EMPTY_STR ) && !currentFromList.contains( newValue ) )
        {
            currentFromList.add( newValue );
        }
        /*
         *  add the second table
         */
        newValue = nameValues.getValue( FormTags.TABLE_TAG + "2" )[0];
        synValue = nameValues.getValue( FormTags.SYN_TAG + "2" )[0];
        if ( !synValue.equals( Config.EMPTY_STR ) )
        {
            newValue = newValue + " " + synValue;
        }
        if ( !newValue.equals( Config.EMPTY_STR ) && !currentFromList.contains( newValue ) )
        {
            currentFromList.add( newValue );
        }
        /*
         *  return the list
         */
        return currentFromList;
    }


    /**
     *  This method is the 'special case' It will cycle through all the values
     *  with the same tag name, with the assumption that this is to have an 'OR'
     *  tag The assumption is made that this will be only for the concatinated
     *  fields. Assumption - OR's can only span the same field in the database
     *
     *@param  currentWhereList           Description of Parameter
     *@return                            Updated List of where clauses input by
     *      user
     *@exception  MraldException  Exception
     *@since
     */
    public ArrayList<String> buildWhere( ArrayList<String> currentWhereList )
        throws MraldException
    {
        String newValue = Config.EMPTY_STR;
        String thisOperator = null;
        String value = null;
        /*
         *  build the concatField
         */
        /*
         *  add the first part of the concatenation
         */
        String table = nameValues.getValue( FormTags.TABLE_TAG + "1" )[0];
        String synValue = nameValues.getValue( FormTags.SYN_TAG + "1" )[0];
        //if there is a synomyn - use this
        if ( !synValue.equals( Config.EMPTY_STR ) )
        {
            table = synValue;
        }
        String concatField = table + "." + nameValues.getValue( FormTags.FIELD_TAG + "1" )[0];
        /*
         *  add the second part of the concatenation
         */
        table = nameValues.getValue( FormTags.TABLE_TAG + "2" )[0];
        synValue = nameValues.getValue( FormTags.SYN_TAG + "2" )[0];
        //if there is a synomyn - use this
        if ( !synValue.equals( Config.EMPTY_STR ) )
        {
            table = synValue;
        }
        concatField = concatField + "||" + table + "." + nameValues.getValue( FormTags.FIELD_TAG + "2" )[0];

        int noOfValues = ( nameValues.getValue( FormTags.VALUE_TAG ) ).length;
        if ( concatField.equals( Config.EMPTY_STR ) || noOfValues == 0 )
        {
            //if no field, do not filter
            return currentWhereList;
        }
        thisOperator = nameValues.getValue( FormTags.OPERATOR_TAG )[0];
        for ( int i = 0; i < noOfValues; i++ )
        {
            Iterator iter = nameValues.getNames().iterator();
            String name;
            while ( iter.hasNext() )
            {
                name = ( String ) iter.next();
                value = nameValues.getValue( name )[0];
            }
            value = nameValues.getValue( FormTags.VALUE_TAG )[i];
            if ( value.equals( "" ) && thisOperator.toUpperCase().indexOf( "NULL" ) == -1 )
            {
                continue;
            }
            if ( thisOperator.equals( Config.EMPTY_STR ) )
            {
                thisOperator = "=";
            }
            else if ( thisOperator.equals( FormTags.LIKE_TAG ) )
            {
                value = "%" + value + "%";
            }
            else if ( thisOperator.equals( FormTags.STARTS_WITH_TAG ) )
            {
                thisOperator = FormTags.LIKE_TAG;
                value = value + "%";
            }
            if ( i == 0 )
            {
                newValue = "( ";
            }
            /*
             *  if operator has 'null' in it, assume you are looking for IS NULL or
             *  IS NOT NULL and build appropriate value, otherwise, use default
             */
            if ( thisOperator.toUpperCase().indexOf( "NULL" ) != -1 )
            {
                newValue = newValue + concatField + " " + thisOperator;
            }
            else
            {
                newValue = newValue + concatField + " " + thisOperator + " '" + value + "'";
            }
            if ( i < noOfValues - 1 )
            {
                newValue += " OR ";
            }
        }
        if ( ! newValue.equals( "" ) )
        {
            newValue += " )";
        }
        if ( !newValue.equals( Config.EMPTY_STR ) && !currentWhereList.contains( newValue ) )
        {
            currentWhereList.add( newValue );
        }

        return currentWhereList;
    }
}

