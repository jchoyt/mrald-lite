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
package org.mitre.mrald.formbuilder;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import org.mitre.mrald.parser.ParserElement;
import org.mitre.mrald.util.DBMetaData;
import org.mitre.mrald.util.FBUtils;
import org.mitre.mrald.util.FormTags;
import org.mitre.mrald.util.Link;
import org.mitre.mrald.util.MiscUtils;
import org.mitre.mrald.util.MraldOutFile;
import org.mitre.mrald.util.TableMetaData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *  Description of the Class
 *
 *@author     ghamilton
 *@created    Mar 11th, 2004
 */
public class FieldInsertElement extends ParserElement implements FormBuilderElement
{

    Document document;


    /**
     *  Constructor for the FieldElement object
     */
    public FieldInsertElement() { }


    /**
     *  Gets the elementType of the ParserElement-derived object
     *
     *@return    The elementType value
     */
    public String getElementType()
    {
        return "Formbuilder Field Insert Element";
    }


    /**
     *  Produces the HTML for inclusion on the second step of form building. The
     *  HTML returned should be self-supporting - i.e., only the guts of a
     *  &lt;div&gt; or a &lt;td&gt; tag. It should not be part of a larger table
     *  structure. It is used in building the second page of the form buliding
     *  process.
     *
     *@param  num  Which iteration this is. This should be used to create unique
     *      tag names
     *@param  md   Description of the Parameter
     *@return      The HTML for inclusion in the second form building page.
     */
    public String getFBHtml( DBMetaData md, int num, int thread )
    {

        Set formLinks = md.getLinkList();

        Link linkInfo;
//        int counter = 3;
        String fcolumn;
        Iterator iter = formLinks.iterator();

        HashMap<String,Link> fks = new HashMap<String,Link>();
        /*
         *  for each link . . .
         */
        while ( iter.hasNext() )
        {
            /*
             *  Get the columns with Foreign keys
             *
             */
            //If there is links to this table
            if ( md.getLinkList().size() > 0 )
            {
                linkInfo = ( Link ) iter.next();
                fcolumn = new String( linkInfo.getFcolumn() );

                fks.put( fcolumn, linkInfo );

            }
        }

        StringBuffer ret = new StringBuffer();
        String SECTION_CLOSE = "\n</table></td></tr></table>";

        String SECTION_OPENER = "\n<table summary=\"\" width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><tr><td class=\"bord\"><table summary=\"\" width=\"100%\" border=\"0\" cellspacing=\"1\" cellpadding=\"1\">";
        String TABLE_HEADER = "\n<!-- new table!! -->\n<tr align=\"left\"><font size=\"-1\">" +
            "<th>Ignore</th>" +
            "<th>Table</th>" +
            "<th>Column Name</th>" +
            "<th>Column Label</th>" +
            "<th>Order</th>" +
            "<th>Required</th>" +
            "<th>List Column</th></font>" +
            "<th>Comments</th></tr><tr>";

        String CHECK_ALL_ROW = "\n<th><input type=checkbox onclick=\"if(this.checked==false){ClearAll('Ignore:true');}else{CheckAll('Ignore:true');}\"></th><th>&nbsp;</th><th>&nbsp;</th><th>&nbsp;</th><th>&nbsp;</th><th>&nbsp;</th><th>&nbsp;</th></tr>";

        String FIELD_ROW = "\n<tr>" +
            "<td align=\"center\">" +
                "<input name=\"FBInsert<:orderNo:>\" value=\"Output" + FormTags.NAMEVALUE_TOKEN_STR + "true~Table" + FormTags.NAMEVALUE_TOKEN_STR + "<:tableName:>\" type=\"hidden\">" +
                "<:pkinfo:>" +
                "<input name=\"FBInsert<:orderNo:>\" value=\"Ignore" + FormTags.NAMEVALUE_TOKEN_STR + "true\" type=\"checkbox\"></td>" +
            "<td><:tableName:></td>" +
            "<td>" +
                "<input type=\"hidden\" name=\"FBInsert<:orderNo:>\" value=\"Field" + FormTags.NAMEVALUE_TOKEN_STR + "<:column_name:>~Type" + FormTags.NAMEVALUE_TOKEN_STR + "<:type:>\"><:column_name:></td>" +
            "<td>" +
                "<input name=\"FBInsert<:orderNo:>\" type=\"text\" size=\"15\" value=\"<:nice_column_name:>\"></td>" +
            "<td align=\"center\">" +
                "<input name=\"FBInsert<:orderNo:>\" type=\"text\" size=\"3\" value=\"<:orderNo:>\"></td>" +
            "<td align=\"center\">" +
                "<input name=\"FBInsert<:orderNo:>\" type=\"checkbox\" value=\"Required" + FormTags.NAMEVALUE_TOKEN_STR + "true\" <:required:>></td>" +
            "<td align=\"left\"><:listColumn:></td>" +
            "<td align=\"left\"><:comments:></td>";

        /*
         *  append the table heading
         */
        Collection tableMetaData = md.getAllTableMetaData();
        TableMetaData tableInfo;

        iter = tableMetaData.iterator();

        int orderNo = 1;
        ret.append( SECTION_OPENER );
        /*
         *  assuming only one table was chosen....
         *  if no metadata is available, return empty string
         */
        try
        {
            tableInfo = md.getTableMetaData( md.getOriginalTables()[0] );
            Collection fieldNames = tableInfo.getColumnNames();
            ret.append( TABLE_HEADER );
            ret.append( CHECK_ALL_ROW.replaceAll( "<:tableName:>", tableInfo.getName() ) );
            Iterator iter2 = fieldNames.iterator();
            /*
             *  and for each field, output a row of options
             */
//            int i = 0;
            String field;
            while ( iter2.hasNext() )
            {
                field = ( String ) iter2.next();

                /*
                 *  append the columns - one to a row
                 */
                String newRow = FIELD_ROW.replaceAll( "<:column_name:>", field );
                String niceName = FBUtils.getColumnName( tableInfo.getName() + "." + field );
                if ( niceName == null )
                {
                    newRow = newRow.replaceAll( "<:nice_column_name:>", field );
                }
                else
                {
                    newRow = MiscUtils.replace( newRow, "<:nice_column_name:>", niceName );
                }

                /*
                 *  Look for the Fk links
                 */
                if ( fks.containsKey( field ) )
                {
                    String pkTable = fks.get( field ).getPtable();
                    String pkColumn = fks.get( field ).getPcolumn();

                    if ( pkTable != tableInfo.getName() )
                    {
                        newRow = newRow.replaceAll( "<:listColumn:>", getDropDownList( pkTable, pkColumn, md, orderNo ) );
                    }
                    else
                    {
                        newRow = newRow.replaceAll( "<:listColumn:>", "" );
                    }
                }
                else
                {
                    newRow = newRow.replaceAll( "<:listColumn:>", "" );
                }
                Integer colType = tableInfo.getFieldType( field );
                if ( FBUtils.isDateType( colType ) )
                {
                    newRow = newRow.replaceAll( "<:type:>", "Date" );
                }
                else if ( FBUtils.isNumberType( colType ) )
                {
                    newRow = newRow.replaceAll( "<:type:>", "Numeric" );
                }
                else
                {
                    newRow = newRow.replaceAll( "<:type:>", "String" );
                }
                /*
                 *  add PK info
                 */
                if ( tableInfo.isPrimaryKey( field ) )
                {

                    newRow = newRow.replaceAll( "<:required:>", "checked=\"true\"" );
                    newRow = newRow.replaceAll( "<:pkinfo:>", "<input name=\"FBInsert<:orderNo:>\" value=\"PrimaryKey:true\" type=\"hidden\">" );
                }
                else
                {

                    newRow = newRow.replaceAll( "<:required:>", "" );
                    newRow = newRow.replaceAll( "<:pkinfo:>", "" );
                }
                /*
                 *  insert the comments
                 */
                String comments = tableInfo.getFieldComments( field );
                if ( comments == null )
                {
                    comments = "no comments for this field";
                }
                newRow = newRow.replaceAll( "<:comments:>", comments );
                newRow = newRow.replaceAll( "<:tableName:>", tableInfo.getName() );
                newRow = MiscUtils.replace( newRow, "<:orderNo:>", orderNo++ );
                ret.append( newRow + "</tr>" );
            }
        }
        catch ( IndexOutOfBoundsException e )
        {
            MraldOutFile.logToFile( e );
            /*
             *  do nothing - just log that there was a problem an close the section normally
             */
        }
        ret.append( SECTION_CLOSE );
        return ret.toString();
    }


    /**
     *  This method should return the formatted list of columns that can be
     *  selected from this table
     *
     *@param  tableName  Description of the Parameter
     *@param  md         Description of the Parameter
     *@param  cnt        Description of the Parameter
     *@param  pkColumn   Description of the Parameter
     *@return            The dropDownList value
     */
    private String getDropDownList( String tableName, String pkColumn, DBMetaData md, int cnt )
    {

        return FBUtils.buildTableFieldDropDown( md, "FBInsert", cnt, tableName, pkColumn, "listTable", "listIdCol", "listColumn" ).toString();
    }


    /**
     *  This method should build a Node object (or object that inherits from
     *  Node) for inclusion in the xml representation of the MRALD form. Unless
     *  otherwise noted, it is assumed that this will be added to the root node.
     *  It is used in buliding the XML file. Stuture should be:<code>
     *<field output='yes' checked='yes' filter='yes' stats='yes' groupby='yes' sort='yes'>
     *<column>FORCASTISSUE</column>
     *  <table>
     *    CCFPVIEW
     *  </table>
     *  <label>Forcast Issue</label> <type>DATE</type> <format>MM/dd/yyyy
     *  hh:mm:ss a</format> <order>1</order> </field> </code>
     *
     *@param  document  The Document object the return Node will be added to
     *@return           A Node object for inclusion in the Document being built.
     */
    public Node getFBNode( Document document )
    {
        this.document = document;
        /*
         *  chance to opt out
         */
        if ( nameValues.getValue( "Ignore" )[0].equals( "true" ) )
        {
            return null;
        }
        Element ret = document.createElement( "field" );

        String output = nameValues.getValue( "Output" )[0];
        String checked = nameValues.getValue( "Default" )[0];
        String filter = nameValues.getValue( FormTags.FILTER_TAG )[0];
        String stats = nameValues.getValue( FormTags.STAT_TAG )[0];
        String groupby = nameValues.getValue( FormTags.GROUP_STR )[0];
        String sort = nameValues.getValue( FormTags.SORT_TAG )[0];
        addAttribute( ret, "output", output );
        addAttribute( ret, "checked", checked );
        addAttribute( ret, "filter", filter );
        addAttribute( ret, "stats", stats );
        addAttribute( ret, "groupby", groupby );
        addAttribute( ret, "sort", sort );

        /*
         *  child nodes
         */
        String type = nameValues.getValue( "Type" )[0];
        String format = nameValues.getValue( FormTags.FORMAT_TAG )[0];
        String table = nameValues.getValue( FormTags.TABLE_TAG )[0];
        String field = nameValues.getValue( FormTags.FIELD_TAG )[0];
        String[] values = nameValues.getValue( FormTags.VALUE_TAG );
        String isPk = nameValues.getValue( "PrimaryKey" )[0];
        String reqd = nameValues.getValue( "Required" )[0];
        String listTable = nameValues.getValue( "listTable" )[0];
        String listColumn = nameValues.getValue( "listColumn" )[0];
        String listIdColumn = nameValues.getValue( "listIdCol" )[0];

        String label;
        String order;
        if ( isNumber( values[0] ) )
        {
            order = values[0];
            label = values[1];
        }
        else
        {
            label = values[0];
            order = values[1];
        }
        FBUtils.addTextNode( document, ret, "column", field );
        FBUtils.addTextNode( document, ret, "table", table );
        FBUtils.addTextNode( document, ret, "label", label );
        FBUtils.addTextNode( document, ret, "type", type );
        if( !isPk.equals( "" ) )
        {
            FBUtils.addTextNode( document, ret, "pkey", isPk );
        }
        if( !reqd.equals( "" ) )
        {
            FBUtils.addTextNode( document, ret, "required", reqd );
        }
        if ( !listTable.equals( "" ) )
        {
            FBUtils.addTextNode( document, ret, "listTable", listTable );
            FBUtils.addTextNode( document, ret, "listColumn", listColumn );
            FBUtils.addTextNode( document, ret, "listIdCol", listIdColumn );
        }
        if ( !format.equals( "" ) )
        {
            FBUtils.addTextNode( document, ret, "format", format );
        }
        FBUtils.addTextNode( document, ret, "order", order );
        return ret;
    }


    /**
     *  Gets the number attribute of the FieldElement object
     *
     *@param  testee  Description of the Parameter
     *@return         The number value
     */
    public boolean isNumber( String testee )
    {
        return testee.matches( "\\d*\\.*\\d*" );
    }


    /**
     *  Adds a feature to the Attribute attribute of the FieldElement object
     *
     *@param  ret    The feature to be added to the Attribute attribute
     *@param  name   The feature to be added to the Attribute attribute
     *@param  value  The feature to be added to the Attribute attribute
     */
    public void addAttribute( Element ret, String name, String value )
    {
        if ( value.equals( "true" ) )
        {
            ret.setAttribute( name, "yes" );
        }
        else
        {
            ret.setAttribute( name, "no" );
        }
    }

}

