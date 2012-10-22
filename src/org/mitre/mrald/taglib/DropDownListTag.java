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
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.mitre.mrald.control.MsgObject;
import org.mitre.mrald.util.Config;
import org.mitre.mrald.util.DBMetaData;
import org.mitre.mrald.util.Link;
import org.mitre.mrald.util.MetaData;
import org.mitre.mrald.util.MraldConnection;
import org.mitre.mrald.util.MraldException;
import org.mitre.mrald.util.MraldOutFile;
/**
 *  Used to create a drop down list for the values in a table. Note that this
 *  always hits a database to create the drop down list, so it's best to use a
 *  lookup table for this, not some big-*** table. Use it like: <pre>
 *  &lt;select name="FilterCat1"&gt;
 *    &lt;mrald:categoricalfilter table="orientation" listColumn="orientation" pkColumn="orientation" value="Patient Anterior to Superior"/&gt;
 *  &lt;/select&gt;
 *  &lt;input type="hidden" value="Table:scan_protocol~Field:zorientation~Operator:=~Type:String" name="FilterCat1"&gt;
 *  </pre> Where the filter will be on scan_protocol.zorientation field, but the
 *  drop down list will be created from the values in the
 *  orientation.orientation field (here, the orientation table is a lookup table
 *  and the orientation field has the same set of values as in
 *  scan_protocol.zorientation).<br />
 *  <br />
 *  The pkColumn value is the primary key (restricted to single column key) to
 *  be used to create the list and will be the value used as the Value in the
 *  SqlElement. The listColumn is column that has the values to be used on the
 *  form - it can be the same as the pkColumn, or different to make for a more
 *  user friendly list. The value attribute is an optional setting to choose the
 *  default value in the resulting drop down - this is compared to the pkColumn
 *  values to determine the value which is to be the default.<br />
 *  <br />
 *  This tag is capable of using more than one column as a label in the
 *  resulting drop down box, however only one value will be used in the filter.
 *  The **columns** that will be used to populate the user-visible lables in the
 *  drop down list are represented by the listColumn attribute. Attribute values
 *  of the form "column1, columns" are perfectly valid - the result will be that
 *  the user will see "column1 - column2" in the drop down. The value passed to
 *  the query will still only be the column defined by the pkColumn value below.
 *
 *@author     Gail Hamilton
 *@created    October 9, 2004
 */
public class DropDownListTag extends TagSupport
{
    private String table = "";
    private String listColumn = "";
    private String pkColumn = "";
    private String value = "";
	private String filterColumnValue="";
	private String filterColumn="";
	private String filterTable;
    private String datasource = "";



    /**
     *  Constructor for the AllTablesListTag object
     */
    public DropDownListTag()
    {
	    init();
    }


    /**
     *  Standard starting point in the TagSupport class. Overridden to create
     *  custom functionality.
     *
     *@return                   Description of the Return Value
     *@exception  JspException  Description of the Exception
     */
    public int doStartTag()
        throws JspException
    {
        try
        {
            pageContext.getOut().print( getDropDown() );
            return EVAL_BODY_INCLUDE;
        }
        catch ( IOException e )
        {
            JspException se = new JspException( e );
            throw se;
        }
    }

    public void init(){}

    /**
     *  Gets the dropDown attribute of the DropDownListTag object
     *
     *@return                   The dropDown value
     *@exception  JspException  Description of the Exception
     */
    public String getDropDown()
        throws JspException
    {

        try
        {

           	MraldConnection conn;

           	//GH Need to comment out for now, as this breaks the DropDowns
           /*	if (pageContext != null)
           	{
           		HttpServletRequest req = (HttpServletRequest)pageContext.getRequest();
           		HttpServletResponse res = (HttpServletResponse)pageContext.getResponse();

           		//  Get the table metadata

           		if ((res == null) || (req == null))
           		{
           			conn = new MraldConnection(datasource);
           		}
           		else
           		{

           			conn = new MraldConnection(datasource,
           					new MsgObject((HttpServletRequest)pageContext.getRequest(),
           							(HttpServletResponse)pageContext.getResponse() ) );

           		}
           	}
           	else
           	{*/
           		conn = new MraldConnection(datasource);
           //	}


            /*
             *  some databases allow spaces in a table name.
             *  Check here and put double quotes (") around any table name
             *  Check to make sure the quotes aren't already there, as well
             */
            if ( table == null) return "";
            if ( table.indexOf( " " ) > 0 && table.charAt( 0 ) != '"' )
            {
                table = "\"" + table + "\"";
            }

            String tableList = table;
            String whereClause = "";

            if (!filterColumn.equals("") && !filterColumnValue.equals(""))
            {
            	String linkStr ="";
            	whereClause += " Where " + filterTable + "." + filterColumn + " = '" + filterColumnValue + "'";
            	if (!table.equals(filterTable))
            	{
            		linkStr = getLink(table, filterTable);
            		if (!linkStr.equals(""))
            			whereClause += " AND " + linkStr;
            			tableList += ", " + filterTable + " ";
            	}
            }
            String select = "Select DISTINCT " + table + "." + pkColumn + " as id," + table + "." + listColumn + " from " + tableList + whereClause + " order by " + listColumn;

            MraldOutFile.logToFile(Config.getProperty("LOGFILE"), "DropDownListTag: getDropDown: select " + select);


	    	select =  appendToQuery(select);

            ResultSet rs = conn.executeQuery( select );
            ResultSetMetaData rsmd = rs.getMetaData();
            StringBuffer buffer = new StringBuffer();
            while ( rs.next() )
            {

                String entry = rs.getString( 1 );
                if( entry==null )
                {
                    continue;
                }
                buffer.append( "\n<option value=\"" + entry + "\"" );
                if ( String.valueOf(entry).equals( value ) )
                {
                    buffer.append( " selected=\"selected\"" );
                }
                buffer.append( ">" );
                for ( int i = 2; i < ( rsmd.getColumnCount() + 1 ); i++ )
                {
                    buffer.append( rs.getString( i ) );
                    if ( i != rsmd.getColumnCount() )
                    {
                        buffer.append( " - " );
                    }
                }
                buffer.append( "</option>" );
            }
            rs.close();
            conn.close();
            return buffer.toString();
        }
        catch ( SQLException e )
        {

            JspException se = new JspException( e );
            throw se;
        }

        catch ( MraldException e )
        {
        	JspException se = new JspException( e );
            throw se;
        }
    }


    //IF the two tables aren;t the same, need to work out how to join
    private String getLink(String table1, String table2) throws MraldException
    {
        DBMetaData md = MetaData.getDbMetaData( datasource );

    	//Check case
    	if ( (table1.toUpperCase()).equals((table2).toUpperCase()) )
    		return "";
    	try
    	{
    	ArrayList links = md.getLinks(table1, table2);
    	String rtnStr= "";
    	for (int i=0; i < links.size(); i++)
    	{
    		Link link = (Link)links.get(i);
    		if (i > 0)
    			rtnStr += " AND ";

    		rtnStr += link.toString();

    	}


    	return rtnStr;
    	}
    	catch(MraldException me)
    	{
    		return "";
    	}
    }
    /**
     *@param  tableName  The new table value
     */
    public void setTable( String tableName )
    {
        this.table = tableName;
    }

 /**
     *@param  tableName  To allow for any additional processing
     */
    protected String appendToQuery( String query )  throws MraldException
    {
        return query;
    }


    /**
     *@param  value  The new value value
     */
    public void setValue( String value )
    {
        this.value = value;
    }

    /**
     *@param  value  The new value value
     */
    public void setFilterColumnValue( String filterColumnValue)
    {
        this.filterColumnValue = filterColumnValue;
    }

    /**
     *@param  value  The new value value
     */
    public void setFilterColumn( String filterColumn)
    {
        this.filterColumn = filterColumn;
    }

    /**
     *@param  value  The new value value
     */
    public void setFilterTable( String filterTable)
    {
        this.filterTable = filterTable;
    }
    /**
     *@param  listColumn  The new listColumn value
     */
    public void setListColumn( String listColumn )
    {
        this.listColumn = listColumn;
    }


    /**
     *@param  pkColName  The new pkColumn value
     */
    public void setPkColumn( String pkColName )
    {
        this.pkColumn = pkColName;
    }


    public void setDatasource( String ds )
    {
        this.datasource = ds;
    }
}
