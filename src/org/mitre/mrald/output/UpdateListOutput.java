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
package org.mitre.mrald.output;

import java.sql.*;

import java.io.*;

import org.mitre.mrald.control.*;
import java.util.HashMap;
import org.mitre.mrald.util.*;


/**
 *  This Output class is the HTML output returned when a User clicks on the
 *  Retrieve Data button on the original Update form.
 *
 *@author     Gail Hamilton
 *@created    January 8, 2004
 *@version    1.0
 */

public class UpdateListOutput extends HTMLOutput
{
    private String keyTable = null;
    private String[] primaryKeyCols = null;
    private String datasource = null;
    private DBMetaData md = null;
    private HashMap fkInfo;
    private String successRedir;
    /**
     *  Constructor for the HTMLOutput object
     *
     *@since
     */
    public UpdateListOutput()
    {
        super();
    }


    /**
     *  This is method that is called from the Workflow. For this step, the
     *  limits are set and the database connection is established. The passed
     *  MsgObject is searched for special DB connection information (looking for
     *  a Datasource name with the value representing the name of the properties
     *  file that houses the connection information). If none is found, the
     *  default is used.
     *
     *@param  msg                                                Description of
     *      Parameter
     *@exception  org.mitre.mrald.control.WorkflowStepException  Description of
     *      Exception
     *@since                                                     1.2
     */
    public @Override void execute( MsgObject msg )
        throws org.mitre.mrald.control.WorkflowStepException
    {
        successRedir = msg.getValue("SuccessUrl")[0];
        //Get the value that contains the column Name, and table Name of the label column.
        keyTable = msg.getValue( "table" )[0];
        datasource = msg.getValue( "Datasource" )[0];
        keyTable = MiscUtils.replace( keyTable, "\"", "" );
        primaryKeyCols = msg.getValue( "PrimaryKey" );
        String datasource = msg.getValue( FormTags.DATASOURCE_TAG )[0];
        md = MetaData.getDbMetaData( datasource );
        TableMetaData tableInfo = null;
        //If the primary key is not specified then look up in the database
        if ( primaryKeyCols.length == 0 )
        {
            if ( keyTable != null )
            {
                tableInfo = md.getTableMetaData( keyTable );
                primaryKeyCols = ( String[] ) tableInfo.getPrimaryKeys().toArray();
            }
            else
            {
                throw new org.mitre.mrald.control.WorkflowStepException( "You must have a table specified to Update." );
            }

        }
        fkInfo = getFKInfo( msg );
        User user = ( User ) msg.getReq().getSession().getAttribute( Config.getProperty( "cookietag" ) );
        if ( user == null )
        {
            throw new org.mitre.mrald.control.WorkflowStepException( "You must login to perform this operation" );
        }
        super.execute( msg );
    }


    /**
     *  Retrieve the contents of the result set and send them to out as the guts
     *  of an HTML table. This includes only the header row with the field names
     *  as column titles and the each query result as table rows. The
     *  <table>
     *    , <HTML>, <body>, <title>and <style>tags are not included.
     *
     *@param  out                   the output stream
     *@exception  ServletException  Description of the Exception
     *@exception  IOException       Description of the Exception
     *@exception  SQLException      Description of the Exception
     *@since                        1.2
     */
    public @Override void printBody( )
        throws IOException, SQLException
    {
        out.println( "<table><tr>" );
        out.println( "<form enctype='x-www-form-urlencoded' action='FormSubmit' method='POST' name=\"o\">" );
        if( datasource != null && !datasource.equals(Config.EMPTY_STR) )
        {
            out.println( "<input type=\"hidden\" name=\"Datasource\" value=\"" + datasource + "\" />");
        }
        out.println( "<input name=\"Schema\" type=\"hidden\" value=\"public\"><input name=\"form\" type=\"hidden\" value=\"Update Table " + keyTable + "\"><input value='UpdateRow' name='workflow' type='hidden'>" );
        /*
         *  niceNames[] contains the column names, either from the db field name or specified in the HTML form
         */
        StringBuffer headerRow = new StringBuffer( "<tr>" );
        for ( int p = 0; p < niceNames.length; p++ )
        {
            headerRow.append( "<th>" + niceNames[p] + "</th>" );
        }
        headerRow.append( "<th>Update</th></tr>" );
        out.println( headerRow.toString() );

        /*
         *  Iterate through the ResultSet and extract the rows
         *  if a custom format exists, use it, otherwise print the column name
         */
        int row_count = 0;
        float fileSize = 0;
        String formattedString;
        ResultSetMetaData rsmd = rs.getMetaData();

        String fkValues = "";

        while ( ( rs.next() ) &&
            ( ( lineLimitSize == -1 ) || ( row_count < lineLimitSize ) ) &&
            ( ( mbLimitSize == -1 ) || ( fileSize < mbLimitSize ) ) )
        {

            out.print( "<tr>" );
            boolean isPk = false;
//            boolean isFk = false;
            String pkValues = "";

            for ( int i = 0; i < niceNames.length; i++ )
            {
                String colName = rsmd.getColumnName( i + 1 );
                for ( int j = 0; j < primaryKeyCols.length; j++ )
                {
                    if ( primaryKeyCols[j].equals( colName ) )
                    {
                        isPk = true;
                        pkValues = pkValues + "&" + colName + "=";
                    }
                }

                if ( fkInfo.containsKey( colName ) )
                {
                    fkValues = fkValues + ( String ) fkInfo.get( colName );
                }

                /*
                 *  Custom formatting here
                 */
                if ( classNames[i].equals( "Timestamp" ) )
                {
                    formattedString = getAndFormat( rs.getTimestamp( i + 1 ), formats[i] );

                    if ( isPk )
                    {
                        pkValues = pkValues + rs.getTimestamp( i + 1 );
                    }
                }
                else if ( classNames[i].equals( "BigDecimal" ) )
                {
                    formattedString = getAndFormat( rs.getBigDecimal( i + 1 ), formats[i] );
                    if ( isPk )
                    {
                        pkValues = pkValues + rs.getBigDecimal( i + 1 );
                    }
                }
                else
                {
                    if ( isPk )
                    {
                        pkValues = pkValues + rs.getString( i + 1 );
                    }

                    formattedString = rs.getString( i + 1 );
                }
                out.print( "<td>" + formattedString + "</td>" );
                try
                {
                    fileSize += formattedString.length() + 9;
                    //9 for tags
                }
                catch ( NullPointerException npe )
                {
                    fileSize += 13;
                    //13 for tags and "null"
                }
                isPk = false;
            }
            // ENDFOR

            //Add the checkbox with the associated information
            String updateInfo = "<td><input type='hidden' name='updateRow" + row_count +
                "' value=\"Table" + FormTags.NAMEVALUE_TOKEN_STR  + keyTable +
                "~Column" + FormTags.NAMEVALUE_TOKEN_STR  + primaryKeyCols[0] +
                "~Value" + FormTags.NAMEVALUE_TOKEN_STR  + rs.getString( 1 ) + "\">";
            out.println( updateInfo );
            updateInfo = "<input type='button' name='updateRow" +
                    row_count +
                    "' value='Update'  onclick=\"dest='Update.jsp?datasource=" + datasource +
                    "&tableName=" +
                    keyTable +
                    MiscUtils.checkApostrophe( pkValues ) +
                    fkValues +
                    "&SuccessUrl=" + successRedir +
                    "'; location=dest\">";
            out.println( updateInfo );
            String deleteInfo = "<input type='button' name='deleteRow" + row_count +
                "' value='Delete'  onclick=\"location='Delete.jsp?datasource=" + datasource +
                    "&tableName=" + keyTable +
                MiscUtils.checkApostrophe( pkValues ) + "';\"></td>";
            out.println( deleteInfo );
            out.println( "</tr>" );
            row_count++;
            fileSize += 10;
            //for row tags
            // for very large result sets, break it up in several tables
            if ( row_count / 1000.0 == ( int ) ( row_count / 1000.0 ) )
            {
                out.println( "</table><table border='1' cellpadding='3'>" + headerRow.toString() );
            }
        }

        recordsReturned = row_count;
        bytesReturned = fileSize;
    }


    /**
     *  Need to get the values for the Table Name of the label , and the column
     *  name e.g. In format "Table:VolumeLabel~Column:VolumeId"
     *
     *@param  msg  Description of the Parameter
     *@return      The javaScript value
     *@since       1.2
     */
    private HashMap<String,String> getFKInfo( MsgObject msg )
    {
        int i = 1;
        String[] fks = msg.getValue( "fKey" + i );

        HashMap<String,String> linkInfo = new HashMap<String,String>();

        String fKeyInfo;

        while ( i < 100 )
        {
            if ( ( fks == null ) || ( fks.length == 0 ) || ( fks[0].equals( "" ) ) )
            {
                i++;
                fks = msg.getValue( "fKey" + i );
                continue;
            }

            fKeyInfo = "&" + "fKey" + i + "=" + fks[0];
            fKeyInfo = fKeyInfo + "&" + "fKeyTable" + i + "=" + msg.getValue( "fKeyTable" + i )[0];
            fKeyInfo = fKeyInfo + "&" + "fKeyId" + i + "=" + msg.getValue( "fKeyId" + i )[0];
            fKeyInfo = fKeyInfo + "&" + "fKeyList" + i + "=" + msg.getValue( "fKeyList" + i )[0];

            linkInfo.put( fks[0], fKeyInfo );
            i++;

            fks = msg.getValue( "fKey" + i );

        }

        return linkInfo;
    }


    /**
     *  Need to get the values for the Table Name of the label , and the column
     *  name e.g. In format "Table:VolumeLabel~Column:VolumeId"
     *
     *@param  md         Description of the Parameter
     *@param  tableInfo  Description of the Parameter
     *@return            The javaScript value
     *@since             1.2
     */
    /* PM: Never used!
    private HashMap getFKInfo( DBMetaData md, TableMetaData tableInfo )
    {
        StringBuffer fKeyInfo = new StringBuffer();
        HashMap linkInfo = new HashMap();
        String[] columns = ( String[] ) tableInfo.getColumnNames().toArray();

        String tableName = tableInfo.getName();

        for ( int i = 0; i < columns.length; i++ )
        {

            fKeyInfo = new StringBuffer();
            Link link = md.getFKLinkData( tableName, columns[i] );
            fKeyInfo.append( "&" + "fKey" + i + "=" + columns[i] );
            fKeyInfo.append( "&" + "fKeyTable" + i + "=" + link.getFtable() );
            fKeyInfo.append( "&" + "fKeyId" + i + "=" + link.getPcolumn() );
            fKeyInfo.append( "&" + "fKeyList" + i + "=" + link.getFcolumn() );

            linkInfo.put( columns[i], fKeyInfo.toString() );
        }

        return linkInfo;
    }*/


    /**
     *  Need to get the values for the Table Name of the label , and the column
     *  name e.g. In format "Table:VolumeLabel~Column:VolumeId"
     *
     *@return    The javaScript value
     *@since     1.2
     */
    /* PM: Never used!
    private String getJavaScript()
    {
        //String js = "<script type=\"text/javascript\">
        String js = "\n function CheckAll() { \n alert('In CheckAll'); \n var len = form.elements.length;\n" +
            " for (var i = 0; i < len; i++){ \n var e = form.elements[i]; \n if (e.type==\"checkbox\") { e.checked = true;} \n } \n } \n" +
            " function ClearAll() { \n var ml = document.o; \n var len = ml.elements.length;\n" +
            " for (var i = 0; i < len; i++){ \n var e = form.elements[i];\n if (e.type==\"checkbox\") { e.checked = false;} \n } \n } \n ";
        //</script>\n

        return js;
    }*/
}

