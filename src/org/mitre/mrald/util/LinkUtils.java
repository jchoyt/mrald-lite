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
import java.io.IOException;
import java.io.Writer;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
//import org.mitre.mrald.util.MraldOutFile;

public class LinkUtils
{


     /**
      *  Gets the tableHtml attribute of the HtmlKeywordSearch object - Used by Moogle.
      *
      *@param  tableInfo           Description of the Parameter
      *@param  md                  Description of the Parameter
      *@param  rs                  Description of the Parameter
      *@param  lineLimitSize       Description of the Parameter
      *@param  mbLimitSize         Description of the Parameter
      *@return                     The tableHtml value
      *@exception  MraldException  Description of the Exception
      */
     public static String getLinkTableHtml( DBMetaData md, TableMetaData tableInfo, ResultSet rs, int lineLimitSize, float mbLimitSize )
         throws MraldException
     {
         ArrayList pkLinks = ( ArrayList ) md.getPKLinkData( tableInfo.getName() );

         try
         {

             StringBuffer strOut = new StringBuffer( "" );
//             boolean firstTime = true;

             ResultSetMetaData rsmd = rs.getMetaData();
             int colCount = rsmd.getColumnCount();
             String[] fieldNames = new String[colCount];
             for ( int i = 0; i < colCount; i++ )
             {
                 fieldNames[i] = rsmd.getColumnName( i + 1 );
             }
             int row_count = 0;
             int fileSize = 0;
             while ( ( rs.next() ) &&
                 ( ( lineLimitSize == -1 ) || ( row_count < lineLimitSize ) ) &&
                 ( ( mbLimitSize == -1 ) || ( fileSize < mbLimitSize ) ) )
             {
                 row_count++;

                 String rowString = getRowDetails( md, colCount, fieldNames, tableInfo, pkLinks, rs );
                 fileSize += rowString.length();

                 strOut.append( rowString );
             }
             return strOut.toString();
         }
         catch ( SQLException e )
         {
             MraldException se = new MraldException( e );
             throw se;
         }
     }



    /**
     *  Gets the tableHtml attribute of the HtmlKeywordSearch object
     *
     *@param  md                  DBMetaData - typically pulled from the cached database metadata stored in org.mitre.mrald.util.MetaData
     *@param  rs                  ResultSet to process
     *@param  out                 Writer to send the output to
     *@param  pkLinks             All the necessary primary-foreign key links for the tables invovled
     *@param  tables              List of tableMetadata linkged to the
     *@param  lineLimitSize       Description of the Parameter
     *@param  mbLimitSize         Description of the Parameter
     *@exception  MraldException  Description of the Exception
     */
    public static void getLinkTableHtml( Writer out, DBMetaData md, ArrayList <Link> pkLinks, ArrayList <TableMetaData> tables, ResultSet rs, int lineLimitSize, float mbLimitSize )
        throws MraldException
    {
        try
        {
//            TableMetaData tableInfo;
            ResultSetMetaData rsmd = rs.getMetaData();
            int colCount = rsmd.getColumnCount();
            String[] fieldNames = new String[colCount];
            for ( int i = 0; i < colCount; i++ )
            {
                fieldNames[i] = rsmd.getColumnName( i + 1 );
                // MraldOutFile.appendToFile( fieldNames[i] + " mapped to " + tables.get(i).getName() );
            }

            int row_count = 0;
            int fileSize = 0;
            while ( ( rs.next() ) &&
                ( ( lineLimitSize == -1 ) || ( row_count < lineLimitSize ) ) &&
                ( ( mbLimitSize == -1 ) || ( fileSize < mbLimitSize ) ) )
            {
                row_count++;

                String rowString = getRowDetails( md, colCount, fieldNames, tables, pkLinks, rs );
                fileSize += rowString.length();

                out.write( rowString );
            }

        }
        catch ( SQLException e )
        {
            MraldException se = new MraldException( e );
            throw se;
        }
        catch ( IOException e )
        {
            MraldException se = new MraldException( e );
            throw se;
        }
    }


    /**
     *  Gets the rowDetails attribute of the MiscUtils object
     *
     *@param  colCount            Number of Columns
     *@param  fieldNames          Fields
     *@param  tables              Description of the Parameter
     *@param  pkLinks             Description of the Parameter
     *@param  rs                  Description of the Parameter
     *@param  md                  Description of the Parameter
     *@return                     The rowDetails value
     *@exception  MraldException  Description of the Exception
     */
    private static String getRowDetails( DBMetaData md, int colCount, String[] fieldNames, ArrayList <TableMetaData> tables, ArrayList <Link> pkLinks, ResultSet rs )
        throws MraldException
    {
        try
        {
            StringBuffer strOut = new StringBuffer();
            strOut.append( "<tr>\n\t" );
            TableMetaData tableInfo = null;

            for ( int i = 0; i < colCount; i++ )
            {
                String value = rs.getString( i + 1 );

                tableInfo = tables.get( i );
                if ( value == null )
                {
                    value = "";
                }
                // MraldOutFile.appendToFile( "Table: "+tableInfo.getName() + " with primary keys: "+tableInfo.getPrimaryKeys() +  "Field: "+fieldNames[i]);

                String fieldName = fieldNames[i];
                Link link;

                if ( tableInfo.isPrimaryKey( fieldName ) )
                {

                    if ( ( pkLinks != null ) && ( pkLinks.size() > 0 ) )
                    {
                    	String pkout = formatPKOutput( MetaData.getDataSource(md), fieldName, value, pkLinks, rs );
                         if ( pkout != null )
                        {
                            strOut.append( pkout );
                        }
                    }
                    else
                    {
                    	String fkLink = formatFKOutput( MetaData.getDataSource(md), fieldName, value, pkLinks );
                        strOut.append( fkLink );
                    }
                }
                else if ( ( link = md.getFKLinkData( tableInfo.getName(), fieldName ) ) != null )
                {
                	String fkLink = formatFKOutput( MetaData.getDataSource(md), fieldName, value, link );
                    if ( fkLink != null )
                    {
                        strOut.append( fkLink );
                    }
                }
                else
                {
                    strOut.append( "<td>&nbsp;" + value + "</td>" );
                }
            }
            strOut.append( "</tr>" );
            return strOut.toString();
        }
        catch ( SQLException e )
        {

            MraldException se = new MraldException( e );
            throw se;
        }
    }


    /**
     *  Gets the rowDetails attribute of the MiscUtils object
     *
     *@param  colCount            Description of the Parameter
     *@param  fieldNames          Description of the Parameter
     *@param  pkLinks             Description of the Parameter
     *@param  rs                  Description of the Parameter
     *@param  md                  Description of the Parameter
     *@param  tableInfo           Description of the Parameter
     *@return                     The rowDetails value
     *@exception  MraldException  Description of the Exception
     */
    private static String getRowDetails( DBMetaData md, int colCount, String[] fieldNames, TableMetaData tableInfo, ArrayList pkLinks, ResultSet rs )
        throws MraldException
    {
        try
        {
            StringBuffer strOut = new StringBuffer();
            strOut.append( "<tr>" );
            strOut.append( "\n\t" );

            for ( int i = 0; i < colCount; i++ )
            {

                String value = rs.getString( i + 1 );

                if ( value == null )
                {
                    value = "";
                }

                String fieldName = fieldNames[i];
                Link link;

                if ( tableInfo.isPrimaryKey( fieldName ) )
                {

                    if ( ( pkLinks != null ) && ( pkLinks.size() > 0 ) )
                    {
                        String pkout = formatPKOutput( MetaData.getDataSource(md), fieldName, value, pkLinks, rs );
                        if ( pkout != null )
                        {
                            strOut.append( pkout );
                        }
                    }
                    else
                    {
                        String fkLink = formatFKOutput( MetaData.getDataSource(md), fieldName, value, pkLinks );
                        strOut.append( fkLink );
                    }
                }
                else if ( ( link = md.getFKLinkData( tableInfo.getName(), fieldName ) ) != null )
                {
                    String fkLink = formatFKOutput( MetaData.getDataSource(md), fieldName, value, link );
                    if ( fkLink != null )
                    {
                        strOut.append( fkLink );
                    }
                }
                else
                {
                    strOut.append( "<td>&nbsp;" + value + "</td>" );
                }
            }
            strOut.append( "</tr>" );
            return strOut.toString();
        }
        catch ( SQLException e )
        {
            MraldException se = new MraldException( e );
            throw se;
        }
    }


    /**
     *  Description of the Method
     *
     *@param  columnName          Description of the Parameter
     *@param  value               Description of the Parameter
     *@param  links               Description of the Parameter
     *@param  rs                  Description of the Parameter
     *@return                     Description of the Return Value
     *@exception  MraldException  Description of the Exception
     */
    private static String formatPKOutput( String datasource, String columnName, String value, ArrayList links, ResultSet rs )
        throws MraldException
    {
        try
        {

            StringBuffer strOut = new StringBuffer();
            StringBuffer listParams = new StringBuffer();
            boolean noValues = true;

            for ( int i = 0; i < links.size(); i++ )
            {

                Link link = ( Link ) links.get( i );
                if ( i > 0 )
                {
                    listParams.append( "&" );
                }

                if ( link.getFtable() != null )
                {

                    String pCol = link.getPcolumn();
                    String fCol = link.getFcolumn();
                    String val = rs.getString( pCol );

                    if ( val != null )
                    {
                        val = URLEncoder.encode( val, "UTF-8" );
                    }
                    listParams.append( "Table=" + link.getFtable() );

                    listParams.append( "&" + fCol + "=" + val );

                    noValues = false;
                }
            }

            if ( noValues )
            {
                return "<td>" + value + "</td>";
            }

            strOut.append( "<td align=\"right\" width=\"15%\">" );
            strOut.append( "<a href=\"TableDetails.jsp?datasource=" + datasource + "&" + listParams.toString() + "\">" );
            strOut.append( "&nbsp;" + value + "</a></td>" );
            return strOut.toString();
        }
        catch ( SQLException e )
        {

            MraldException se = new MraldException( e );
            throw se;
        }
        catch ( java.io.UnsupportedEncodingException e )
        {

            MraldException se = new MraldException( e );
            throw se;
        }
    }


    /**
     *  Description of the Method
     *
     *@param  columnName          Description of the Parameter
     *@param  value               Description of the Parameter
     *@param  links               Description of the Parameter
     *@return                     Description of the Return Value
     *@exception  MraldException  Description of the Exception
     */
    private static String formatFKOutput( String datasource, String columnName, String value, ArrayList links )
        throws MraldException
    {
        try
        {
            StringBuffer strOut = new StringBuffer();
            StringBuffer listParams = new StringBuffer();
            boolean noValues = true;

            String urlVal = value;
            if ( value != null )
            {
                urlVal = URLEncoder.encode( value, "UTF-8" );
            }

            for ( int i = 0; i < links.size(); i++ )
            {

                Link link = ( Link ) links.get( i );

                if ( link.getPtable() == null )
                {
                    continue;
                }

//                String pTable = link.getPtable();

                listParams.append( "Table=" + link.getPtable() );
                listParams.append( "&" + link.getPcolumn() + "=" + urlVal );
                noValues = false;

            }
            if ( noValues )
            {
                return "<td>" + value + "</td>";
            }

            strOut.append( "<td align=\"right\" width=\"15%\">" );
            strOut.append( "<a href=\"TableDetails.jsp?datasource=" + datasource + "&" + listParams.toString() + "\">" );

            return strOut.toString();
        }
        catch ( java.io.UnsupportedEncodingException e )
        {

            MraldException se = new MraldException( e );
            throw se;
        }
    }


    /**
     *  Description of the Method
     *
     *@param  columnName          Description of the Parameter
     *@param  value               Description of the Parameter
     *@param  link                Description of the Parameter
     *@return                     Description of the Return Value
     *@exception  MraldException  Description of the Exception
     */
    private static String formatFKOutput( String datasource, String columnName, String value, Link link )
        throws MraldException
    {
        try
        {
            StringBuffer strOut = new StringBuffer();

            String urlVal = value;
            if ( value != null )
            {
                urlVal = URLEncoder.encode( value, "UTF-8" );
            }

            if ( link.getPtable() == null )
            {
                return "<td>" + value + "</td>";
            }

            String pTable = link.getPtable();
            String pColumn = link.getPcolumn();
            strOut.append( "<td align=\"right\" width=\"15%\">" );
            strOut.append( "<a href=\"TableDetails.jsp?datasource=" + datasource + "&Table=" + pTable + "&" + pColumn + "=" + urlVal + "\">" + value + "</a>" );

            return strOut.toString();
        }
        catch ( java.io.UnsupportedEncodingException e )
        {

            MraldException se = new MraldException( e );
            throw se;
        }
    }



}

