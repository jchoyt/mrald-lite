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
package org.mitre.mrald.taglib;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.mitre.mrald.control.MsgObject;
import org.mitre.mrald.util.Config;
import org.mitre.mrald.util.DBMetaData;
import org.mitre.mrald.util.FBUtils;
import org.mitre.mrald.util.MetaData;
import org.mitre.mrald.util.MraldConnection;
import org.mitre.mrald.util.MraldOutFile;
import org.mitre.mrald.util.TableMetaData;

/**
 *  Description of the Class
 *
 *@author     jchoyt
 *@created    October 9, 2002
 */
public class ListUpdateValuesTag extends BodyTagSupport
{
    /**
     *  Description of the Field
     */
    protected ResultSet rs;
    /**
     *  Description of the Field
     */
    protected String schema;
    /**
     *  Description of the Field
     */
    protected String action;


    /**
     *  Constructor for the AllTablesListTag object
     */
    public ListUpdateValuesTag() { }


    /**
     *  Description of the Method
     *
     *@return                   Description of the Return Value
     *@exception  JspException  Description of the Exception
     */
    public int doStartTag()
        throws JspException
    {
        try
        {
        	//MraldOutFile.logToFile(Config.getProperty("LOGFILE"), "List Update values; doStartTag: Start " );

            ServletRequest request = pageContext.getRequest();
            String tableName = request.getParameter( "tableName" );
            String datasource = request.getParameter( "datasource" );

            if (datasource.equals(""))
    		    datasource= "main";

            DBMetaData md = MetaData.getDbMetaData( datasource );
            String tempTableName = tableName;

			TableMetaData tableInfo = md.getTableMetaDataNoCase(tempTableName);
			if (tableInfo == null)
			{
				// MraldOutFile.logToFile( "List Update values; doStartTag: Table: " + tableName + " not found in Database. " );

				throw new JspException("Table: " + tableName + " not found in Database. ");
			}

			Collection<String> primaryKeys = tableInfo.getPrimaryKeys();

			//MraldOutFile.logToFile(Config.getProperty("LOGFILE"), "List Update values; doStartTag: Got tableMetaData " + tableInfo );

            //add quotes around tablename if it has spaces
            String queryTableName = new String( tableName );
            if ( queryTableName.indexOf( ' ' ) != -1 )
            {
                queryTableName = "\"" + queryTableName + "\"";
            }
            int i = 1;
            //List of columns that are linked to Fk
            //fks is the column in the current table that contains a link to a fk
            String[] fks = request.getParameterValues( "fKey" + i );
            // MraldOutFile.logToFile("ListUpdateValuesTag : doStartTag: fkLinks: " + fks );
            HashMap<String, String[]> linkInfo = new HashMap<String, String[]>();
            while ( i < 100 )
            {
                if ( ( fks == null ) || ( fks.length == 0 ) )
                {
                    i++;
                    fks = request.getParameterValues( "fKey" + i );
                    continue;
                }
                String filterTable = request.getParameter( "fKeyFilterTable" + i );
                String filterColumn= request.getParameter( "fKeyFilterColumn" + i );
                String filterValue= request.getParameter( "fKeyFilterValue" + i );
                String dropDownDataSource= request.getParameter( "fKeyDataSource" + i );
                if (dropDownDataSource== null)
                	dropDownDataSource="main";

                if (filterTable == null)
                {
                	filterTable="";
                	filterColumn="";
                	filterValue="";

                }

                String[] fklink = new String[]{
                        request.getParameter( "fKeyTable" + i ),
                        request.getParameter( "fKeyId" + i ),
                        request.getParameter( "fKeyList" + i ),
                         dropDownDataSource ,
                        filterTable, filterColumn, filterValue};

                linkInfo.put( fks[0].toUpperCase(), fklink );
                i++;
                fks = request.getParameterValues( "fKey" + i );
            }

            // MraldOutFile.logToFile(Config.getProperty("LOGFILE"), "List Update values; doStartTag: Number of foreign Links: " +  linkInfo.size());
			//MraldOutFile.logToFile(Config.getProperty("LOGFILE"), "List Update values; doStartTag: Got all Foreign Key Data " );

            Enumeration names = request.getParameterNames();
            StringBuffer whereClause = new StringBuffer();
            ArrayList<String> filters = new ArrayList<String>();
            ArrayList<String> pks = new ArrayList<String>();
            ArrayList<String> pksLowerCase = new ArrayList<String>();
            String whereAppend = " ";
            while ( names.hasMoreElements() )
            {
                String colName = names.nextElement().toString();
    			//MraldOutFile.logToFile(Config.getProperty("LOGFILE"), "List Update values; doStartTag: Getting data for column " + colName );

                if ( colName.equals( "tableName" ) || colName.startsWith( "fKey" ) || colName.equals( "datasource" ) || colName.equals( "SuccessUrl" ))
                {
                    continue;
                }


                //MraldOutFile.logToFile(Config.getProperty("LOGFILE"), "List Update values; doStartTag: Check if column " + colName + " is a primary Key " );

                String newColName = colName;
                boolean isPrimaryKey = tableInfo.isPrimaryKeyNoCase(newColName);
                //MraldOutFile.logToFile(Config.getProperty("LOGFILE"), "List Update values; doStartTag: is column " + colName + " a primary Key?  " + isPrimaryKey );

                //Check To see if the column is a primary Key

                pks.add(colName.toLowerCase());

                //Additional check to make sure that all primary keys are
                //specified at least once
                if (isPrimaryKey)
                {
                	//Make sure that each Primary Key is only added once despite case differences
                	if (!pksLowerCase.contains(colName.toLowerCase()))
                		pksLowerCase.add( colName.toLowerCase() );
                }

            	//MraldOutFile.logToFile(Config.getProperty("LOGFILE"), "List Update values; doStartTag: About to get value for Column " + colName + " " );

                String val = request.getParameter( colName );

                //MraldOutFile.logToFile(Config.getProperty("LOGFILE"), "List Update values; doStartTag:Value for Column " + colName + " " +val );

                //Ignore any columns listed that are not in the table
                if (checkHasColumn(tableInfo, colName))
                {
                	whereClause.append( whereAppend );
                	//MraldOutFile.logToFile(Config.getProperty("LOGFILE"), "List Update values; column " + colName + " found in " + tableName );
                    whereAppend = " AND ";

                	whereClause.append( colName + "='" + val + "'" );
                	String pkStr = "Table:" + tableName + "~Field:" + colName + "~Value:" + val;
                	filters.add( pkStr );

                }

            }

            //Make sure that all unique parameters are specified to stop more data being returned than should be
            if (pksLowerCase.size() < primaryKeys.size())
            {
            	// MraldOutFile.logToFile(Config.getProperty("LOGFILE"), "List Update values; doStartTag: Not all Primary Key Values have been specified " );
            	MraldOutFile.logToFile(Config.getProperty("LOGFILE"), "List Update values; doStartTag: Should have " +primaryKeys.size() +    " but actually have " + pks.size() );

            	throw new JspException("Not all Primary Key Values have been specified ");
            }
//	    Remove DISTINCT as this creates problems for complex datatypes.
            //String selectClause = "Select DISTINCT * from " + queryTableName;
            String selectClause = "Select * from " + queryTableName;
            if ( whereClause.length() > 0 )
            {
                selectClause = selectClause + " WHERE " + whereClause;
            }
            // MraldOutFile.logToFile(Config.getProperty("LOGFILE"), "List Update values; doStartTag: selectClause " + selectClause );

            //String valuesList = outputResults( datasource, selectClause, tableName, pks, linkInfo );
            String valuesList = outputResults( datasource, selectClause, tableName, pks, linkInfo );
            String filtersList = outputFilters( filters );
            pageContext.getOut().print( valuesList );
            pageContext.getOut().print( filtersList );
            return EVAL_BODY_AGAIN;
        }
        catch ( SQLException e )
        {
            throw new JspException( e );
        }
        catch ( IOException e )
        {
            throw new JspException( e );
        }
    }


    private boolean checkHasColumn(TableMetaData tableInfo,String colName )
    {
		// MraldOutFile.logToFile(Config.getProperty("LOGFILE"), "List Update values; checkHasColumn for " + colName );

    	Iterator<String> colIter = tableInfo.getColumnNames().iterator();
    	String lowerCaseColumn = colName.toLowerCase();
    	while (colIter.hasNext())
    	{
    		String listColumn = colIter.next().toLowerCase();
    		if (lowerCaseColumn.equals(listColumn))
    			return true;
    	}
    	return false;
    }

    /**
     *  SetAction
     *
     *@param  action  The new action value
     */
    public void setAction( String action )
    {
        this.action = action;
    }


    /**
     *  SetAction
     */
    // public String getAction()
    // {
    // return this.action;
    // }
    /**
     *  Initializes the streams and database connections.
     *
     *@param  selectClause      Description of the Parameter
     *@param  tableName         Description of the Parameter
     *@param  pks               Description of the Parameter
     *@param  fks               Description of the Parameter
     *@return                   Description of the Return Value
     *@exception  SQLException  Description of the Exception
     *@exception  JspException  Description of the Exception
     */
    public String outputResults( String datasource, String selectClause, String tableName, ArrayList pks, HashMap fks )
        throws JspException, SQLException
    {
        /*
         *  Opening a connection to the Database
         */

    	// MraldOutFile.logToFile(Config.getProperty("LOGFILE"), "List Update values; outputResults. Start "  );

        Connection conn = new MraldConnection(
            datasource,
            new MsgObject((HttpServletRequest)pageContext.getRequest(),
                (HttpServletResponse)pageContext.getResponse() ) )
            .getConnection();
        DBMetaData md = MetaData.getDbMetaData( datasource );

        String noValue="";
        /*
         *  Setting and executing the query with the database connection
         */
        schema = md.getDbProps().getProperty( "SCHEMA" );
        if ( schema == null || schema.equals( Config.EMPTY_STR ) )
        {
            RuntimeException e = new RuntimeException( "A SCHEMA value was not provided in the database configuration file." );
            throw e;
        }


        rs = conn.createStatement().executeQuery( selectClause );

        String CALENDAR = "\n<SCRIPT LANGUAGE=\"JavaScript\">var cal<:orderNo:> = new CalendarPopup();</SCRIPT>\n<A HREF=\"#\" onClick=\"cal<:orderNo:>.select(document.FormUpdate.<:name:>[0],'anchor<:orderNo:>','MM/dd/yyyy'); return false;\" TITLE=\"cal<:orderNo:>.select(document.FormUpdate.<:name:>[0],'anchor<:orderNo:>','MM/dd/yyyy'); return false; \"  NAME=\"anchor<:orderNo:>\" ID=\"anchor<:orderNo:>\"><img src=\"images/cal.gif\" width=\"17\" height=\"17\" border=\"0\" alt=\"Click Here to Pick up the timestamp\"></A>\n";
        ResultSetMetaData rsmd = rs.getMetaData();
        int colCount = rsmd.getColumnCount();
        /*
         *  Produce the table names with the appropriately labeled checkboxs
         */
        StringBuffer buffer = new StringBuffer();
        buffer.append( "\n<tr><td colspan=\"2\">" );
        buffer.append( "<b>" + action + " Values:</b></td></tr>" );
        while ( rs.next() )
        {
            for ( int i = 0; i < colCount; i++ )
            {
                String value = rs.getString( i + 1 );
                if( value == null || value.equals("null") )
                {
                    value = noValue;
                }
                String colName = rsmd.getColumnName( i + 1 );
                int type = rsmd.getColumnType( i + 1 );
                String niceName = FBUtils.getColumnName(tableName + "." + colName);
                if (niceName == null)
				{
                	niceName=colName;
				}
                boolean isDate = false;
                String calendar;
                if ( ( type == Types.TIME ) || ( type == Types.DATE ) || ( type == Types.TIMESTAMP ) )
                {
                    isDate = true;
                }
                buffer.append( "\n<tr><td><b>" + niceName + "</b></td><td>" );
                int textSize = 0;
                if ( value != null )
                {
                    textSize = value.length();
                }
                else
                {
                }
                //If the value is greater than 100 make a text area
                if ( textSize > 100 )
                {
                    buffer.append( "<textarea cols='100' rows='" + ( textSize / 100 + 1 ) + "' name='" + action + ( i + 1 ) + "'>" );
                    buffer.append( value + "</textarea>" );
                }
                else
                {

                	// MraldOutFile.logToFile(Config.getProperty("LOGFILE"), "List Update values; outputResults. Checking if fkeys contain  " + colName  );

                	String upperCaseColName = colName.toUpperCase();
                	if ( fks.containsKey( upperCaseColName ) )
                    {

                    	// MraldOutFile.logToFile(Config.getProperty("LOGFILE"), "List Update values; outputResults. Fkeys do contain  " + colName  );

                        DropDownListTag ddl = new DropDownListTag();
                        buffer.append( "<select" );
                        buffer.append( " name='" + action + ( i + 1 ) + "'>" );
                        String[] fkInfo = ( String[] ) fks.get( upperCaseColName );
                        ddl.setTable( fkInfo[0] );
                        ddl.setPkColumn( fkInfo[1] );
                        ddl.setListColumn( fkInfo[2] );
                        ddl.setValue( value );
                        ddl.setDatasource( fkInfo[3] );

                        if (!fkInfo[4].equals(""))
                        {
                          	ddl.setFilterTable(fkInfo[4]);
                          	ddl.setFilterColumn(fkInfo[5]);
                          	ddl.setFilterColumnValue(fkInfo[6]);

                        }

                        MraldOutFile.logToFile(Config.getProperty("LOGFILE"), "List Update values; outputResults. About to get Drop down data for : " +  colName  );

                        buffer.append( ddl.getDropDown() + "</select>\n" );
                        MraldOutFile.logToFile(Config.getProperty("LOGFILE"), "List Update values; outputResults. Finished Drop Down Retrieval for: " + colName  );

                    }
                    else if ( pks.contains( colName.toLowerCase() ) )
                    {

                        buffer.append( value + "</td><input type=\"hidden\" name='" + action + ( i + 1 ) + "' value=\"" + value + "\">" );
                    }
                    else
                    {
                        buffer.append( "<input type='text' size='" + textSize );
                        buffer.append( "' name='" + action + ( i + 1 ) + "' " );
                        if ( isDate )
                        {
                        	if (!value.equals(noValue))
                        	{
                        		SimpleDateFormat df = new SimpleDateFormat( "yyyy-mm-dd"  );
                        		java.util.Date date;
                        		try
                        		{
                        			date = df.parse( value );

                        			df.applyPattern( "mm/dd/yyyy" );
                        			String dateVal = df.format( date );
                        			buffer.append( "value=\"" + dateVal + "\">" );
                        		} catch (ParseException e) {
                        			// TODO Auto-generated catch block
                        			RuntimeException re = new RuntimeException( "A date value could not be formatted." );
                        			throw re;
                        		}
                        		calendar = CALENDAR.replaceAll( "<:name:>", action + ( i + 1 ) );
                        		calendar = calendar.replaceAll( "<:orderNo:>", ( new Integer( i + 1 ) ).toString() );
                        		buffer.append( calendar );
                        	}
                        }
                        else
                        {
                        	 buffer.append( "value=\"" + value + "\">" );

                        }
                        buffer.append( "</td>" );
                    }
                }
                String typeName = FBUtils.isDateType(type)   ? "Date" :
								  FBUtils.isNumberType(type) ? "Numeric" :
								  	FBUtils.isBinaryType(type) ? "Binary" :
									  						      "String";



                buffer.append( "<input type='hidden' name='" + action + ( i + 1 ) + "' value=\"Table:" + tableName + "~Field:" + colName + "~Type:" + typeName + "\"></tr>" );
            }
        }
        rs.close();
        conn.close();
        return buffer.toString();
    }


    /**
     *  Initializes the streams and database connections.
     *
     *@param  filters           Description of the Parameter
     *@return                   Description of the Return Value
     *@exception  SQLException  Description of the Exception
     */
    public String outputFilters( ArrayList filters )
        throws SQLException
    {
        /*
         *  Produce the table names with the appropriately labeled checkboxs
         */
        StringBuffer buffer = new StringBuffer();
        for ( int i = 0; i < filters.size(); i++ )
        {
            buffer.append( "\n<input type='hidden' name='Filter" + ( i + 1 ) + "' value=\"" + filters.get( i ) + "\" >" );
        }
        return buffer.toString();
    }
}

