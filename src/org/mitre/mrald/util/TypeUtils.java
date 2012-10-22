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
package org.mitre.mrald.util;

import java.sql.Types;
import java.util.ArrayList;

/**
 *  Description of the Class
 *
 *@author     jchoyt
 *@created    February 3, 2003
 */
public class TypeUtils extends java.lang.Object
{


    /**
     *  Array of Integers that represent date/time types as returned by a JDBC
     *  driver
     */
    protected final static ArrayList<Integer> dateTypes = new ArrayList<Integer>();
    /**
     *  Array of Integers that represent numeric types as returned by a JDBC
     *  driver
     */
    protected final static ArrayList<Integer> numberTypes = new ArrayList<Integer>();
    protected final static ArrayList<Integer> booleanTypes = new ArrayList<Integer>();

    static
    {
        numberTypes.add( new Integer( Types.BIGINT ) );
        numberTypes.add( new Integer( Types.DECIMAL ) );
        numberTypes.add( new Integer( Types.DOUBLE ) );
        numberTypes.add( new Integer( Types.FLOAT ) );
        numberTypes.add( new Integer( Types.INTEGER ) );
        numberTypes.add( new Integer( Types.NUMERIC ) );
        numberTypes.add( new Integer( Types.SMALLINT ) );
        numberTypes.add( new Integer( Types.TINYINT ) );
	booleanTypes.add(new Integer(Types.BOOLEAN) );
        booleanTypes.add(new Integer(Types.BIT) );
        dateTypes.add( new Integer( Types.DATE ) );
        dateTypes.add( new Integer( Types.TIME ) );
        dateTypes.add( new Integer( Types.TIMESTAMP ) );
    }

    /**
     *  Gets the sqlType attribute of the FBUtils class
     *
     *@param  type  Description of the Parameter
     *@return       The sqlType value
     */
    public static String getSqlType( int type )
    {
        switch ( type )
        {
            case Types.ARRAY:
                return "ARRAY ";
            case Types.BIGINT:
                return "BIGINT ";
            case Types.BINARY:
                return "BINARY";
            case Types.BIT:
                return "BIT ";
            case Types.BLOB:
                return "BLOB";
            case Types.BOOLEAN:
                return "BOOLEAN ";
            case Types.CHAR:
                return "CHAR";
            case Types.CLOB:
                return "CLOB";
            case Types.DATALINK:
                return "DATALINK ";
            case Types.DATE:
                return "DATE ";
            case Types.DECIMAL:
                return "DECIMAL";
            case Types.DISTINCT:
                return "DISTINCT";
            case Types.DOUBLE:
                return "DOUBLE ";
            case Types.FLOAT:
                return "FLOAT ";
            case Types.INTEGER:
                return "INTEGER";
            case Types.JAVA_OBJECT:
                return "JAVA_OBJECT ";
            case Types.LONGVARBINARY:
                return "LONGVARBINARY";
            case Types.LONGVARCHAR:
                return "LONGVARCHAR";
            case Types.NULL:
                return "NULL";
            case Types.NUMERIC:
                return "NUMERIC";
            case Types.OTHER:
                return "OTHER";
            case Types.REAL:
                return "REAL ";
            case Types.REF:
                return "REF ";
            case Types.SMALLINT:
                return "SMALLINT	";
            case Types.STRUCT:
                return "STRUCT";
            case Types.TIME:
                return "TIME ";
            case Types.TIMESTAMP:
                return "TIMESTAMP";
            case Types.TINYINT:
                return "TINYINT";
            case Types.VARBINARY:
                return "VARBINARY";
            case Types.VARCHAR:
                return "VARCHAR";
            default:
                return "VARCHAR";
        }
    }

    /**
     *  Gets the dateType attribute of the FBUtils class
     *
     *@param  _type  Description of the Parameter
     *@return        The dateType value
     */
    public static boolean isDateType( int _type )
    {
        Integer type = new Integer( _type );
        return dateTypes.contains( type );
    }

     /**
     *  Gets the dateType attribute of the FBUtils class
     *
     *@param  _type  Description of the Parameter
     *@return        The dateType value
     */
    public static boolean isBooleanType( int _type )
    {
        Integer type = new Integer( _type );
        return booleanTypes.contains( type );
    }


    /**
     *  Gets the dateType attribute of the FBUtils class
     *
     *@param  type  Description of the Parameter
     *@return       The dateType value
     */
    public static boolean isDateType( Integer type )
    {
        return dateTypes.contains( type );
    }


    /**
     *  Gets the numberType attribute of the FBUtils class
     *
     *@param  _type  Description of the Parameter
     *@return        The numberType value
     */
    public static boolean isNumberType( int _type )
    {
        Integer type = new Integer( _type );
        return numberTypes.contains( type );
    }


    /**
     *  Gets the numberType attribute of the FBUtils class
     *
     *@param  type  Description of the Parameter
     *@return       The numberType value
     */
    public static boolean isNumberType( Integer type )
    {
        return numberTypes.contains( type );
    }

    /**  Gets the numberType attribute of the FBUtils class
     *
     *@param  type  Description of the Parameter
     *@return       The numberType value
     */
    public static boolean isBooleanType( Integer type )
    {
        return booleanTypes.contains( type );
    }

}

