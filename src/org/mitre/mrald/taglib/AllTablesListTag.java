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

import org.mitre.mrald.util.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.mitre.mrald.util.DBMetaData;
import org.mitre.mrald.util.FormTags;
import org.mitre.mrald.util.TableMetaData;

/**
 *  For the formbuilder - provides a full list of tables and comments for a give
 *  datasource. Provides checkboxes so users can select which tables to use.
 *
 *@author     jchoyt
 *@created    October 9, 2002
 */
public class AllTablesListTag extends TagSupport
{
    String schema;
    String displayTables = "true";
    String datasource = "";


    /**
     *  Constructor for the AllTablesListTag object
     */
    public AllTablesListTag() { }


    /**
     *  Gets the body attribute of the BuildForm object
     *
     *@return                   The body value
     *@exception  SQLException  Description of the Exception
     */
    public String getBody()
        throws SQLException
    {
        DBMetaData md = MetaData.getDbMetaData( datasource );
//        String[] types = {"TABLE", "VIEW"};
        ArrayList tableList = new ArrayList();
        tableList = ( ArrayList ) md.getAllTableMetaData();
        /*
         *  Produce the table names with the appropriately labeled checkboxs
         */
        StringBuffer buffer = new StringBuffer();
        String tableName;
        String inputType = "hidden";
        if ( displayTables.equals( "true" ) )
        {
            buffer.append( "\n<tr><td colspan=\"3\">" );
            buffer.append( "<b>Select Tables to be Used:</b></td></tr>" );
            inputType = "checkbox";
        }

        for ( int i = 0; i < tableList.size(); i++ )
        {
            TableMetaData table = ( TableMetaData ) tableList.get( i );
            tableName = table.getName();

            if ( displayTables.equals( "true" ) )
            {
                buffer.append( "\n<tr>" );
                buffer.append( "<td width=7 align='center'>" );
            }
            buffer.append( "<input type='" + inputType + "' name='" + FormTags.TABLE_TAG + "' value='" + tableName + "'></td>" );
            if ( displayTables.equals( "true" ) )
            {
                buffer.append( "<td>" + tableName + "</td>" );
                buffer.append( "<td>" + table.getComments() + "</td>" );
                buffer.append( "</tr>" );
            }
        }

        return buffer.toString();
    }


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
            String tableList = getBody();
            pageContext.getOut().print( tableList );
            return 0;
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


    /**
     *  Gets the displayTables attribute of the AllTablesListTag object
     *
     *@return    The displayTables value
     */
    public String getDisplayTables()
    {
        return displayTables;
    }


    /**
     *  Sets the displayTables attribute of the AllTablesListTag object
     *
     *@param  displayTables  The new displayTables value
     */
    public void setDisplayTables( String displayTables )
    {
        this.displayTables = displayTables;
    }


    /**
     *  Sets the datasource attribute of the AllTablesListTag object
     *
     *@param  ds  The new datasource value
     */
    public void setDatasource( String ds )
    {
        datasource = ds;
    }

}

