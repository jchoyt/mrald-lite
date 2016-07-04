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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

import org.mitre.mrald.control.MsgObject;
import org.mitre.mrald.util.Config;
import org.mitre.mrald.util.DBMetaData;
import org.mitre.mrald.util.FormTags;
import org.mitre.mrald.util.MraldOutFile;
import org.mitre.mrald.util.TableMetaData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
/**
 *  The CrossLinkElement provides a way of letting user specify
 *  joins that span across databases
 *  It is similar to Link Element but can have values from any datasource
 *
 *@author     ghamilton
 *@created    August 28th 2007
 */
public class CrossLinkElement extends LinkElement
{
    private String fieldEnd = "</tr>\n";
    //<th width=\"13%\">Left Outer</th><th width=\"13%\">Right Outer</th></font></tr>\n";
    private String fieldStart = "<tr align=\"center\">\n";
    /**
     *  Ignore property.
     */
    private String tableEnd = "</table></center>\n";
    private String tableHeader = "<tr align=\"center\"><font size=\"-1\"><th width=\"25%\">Primary</th><th width=\"25%\">Foreign</th><th width=\"12%\">Ignore</th><th width=\"12%\">Full</th></tr>\n";

    private String tableStart = "<center><table cellspacing=\"0\" cellpadding=\"0\" border=\"1\">\n";


    /**
     *  Constructor for the CrossLinkElement object
     *
     *@since
     */
    public CrossLinkElement() { }


    /**
     *  Gets the elementType of the object. Since this is dervied vrom
     *
     *@return    The elementType value
     */
    public String getElementType()
    {
        return "FormBuilder Link Element";
    }


    /**
     *  This process takes multiple databases as input to build the
     *  form
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
    public String getFBHtml( ArrayList<DBMetaData> mds, int num , int thread)
    {

    	StringBuffer ret = new StringBuffer();

        if ( mds == null )
        {
            return "No Available Meta Data...";
        }

        if ( mds.size() == 0 )
        {
            return "No Available Meta Data...";
        }


        ret.append( tableStart );
        ret.append( tableHeader );

        /*
         *  three extra rows
         */
        StringBuffer dropdown;
        String counterStr = "";

        for ( int i = 0; i < 3; i++ )
        {

        	counterStr = i+"";
        	int uniqueCount = Integer.parseInt(counterStr);
            dropdown = new StringBuffer();
            dropdown.append( "<td>" );
            dropdown.append( "\n<select name=\"" );
            dropdown.append( "CrossLink" );
            dropdown.append( counterStr );
            dropdown.append( "\">" );
            dropdown.append( "<option></option>" );
            for (DBMetaData md : mds)
            {

                dropdown.append( buildTableFieldDropDown( md ).toString() );
                //ret.append("<input type=\"hidden\" name=\"CrossLink" + uniqueCount +"\" value=\"DBName:" + sidName +"~Schema:" + schema + "~SqlThread:-1\" />");

            }

            dropdown.append( "</select>" );
            dropdown.append( "</td>\n" );

            ret.append( fieldStart );

            ret.append( dropdown.toString().replaceAll( "Table" + FormTags.NAMEVALUE_TOKEN_STR , "Table1" + FormTags.NAMEVALUE_TOKEN_STR  ).replaceAll( "Field" + FormTags.NAMEVALUE_TOKEN_STR , "Field1" + FormTags.NAMEVALUE_TOKEN_STR  ).replaceAll("DBName" + FormTags.NAMEVALUE_TOKEN_STR , "DBName1" + FormTags.NAMEVALUE_TOKEN_STR ).replaceAll("Schema" + FormTags.NAMEVALUE_TOKEN_STR , "Schema1" + FormTags.NAMEVALUE_TOKEN_STR ));
            ret.append( dropdown.toString().replaceAll( "Table" + FormTags.NAMEVALUE_TOKEN_STR , "Table2" + FormTags.NAMEVALUE_TOKEN_STR  ).replaceAll( "Field" + FormTags.NAMEVALUE_TOKEN_STR , "Field2" + FormTags.NAMEVALUE_TOKEN_STR  ).replaceAll("DBName" + FormTags.NAMEVALUE_TOKEN_STR , "DBName2" + FormTags.NAMEVALUE_TOKEN_STR ).replaceAll("Schema" + FormTags.NAMEVALUE_TOKEN_STR , "Schema2" + FormTags.NAMEVALUE_TOKEN_STR ));
            ret.append( "<td width=\"25%\"><input type=\"radio\" name=\"CrossLink" + uniqueCount+ "\" value=\"Link" + FormTags.NAMEVALUE_TOKEN_STR +"Ignore~SqlThread" + FormTags.NAMEVALUE_TOKEN_STR +"-1\" CHECKED></td>\n" );
            ret.append( "<td width=\"25%\"><input type=\"radio\" name=\"CrossLink" + uniqueCount + "\" value=\"Link" + FormTags.NAMEVALUE_TOKEN_STR +"Full~SqlThread" + FormTags.NAMEVALUE_TOKEN_STR +"-1\" ></td>\n" );

            ret.append( fieldEnd );

        }//for
        ret.append( fieldStart );
        ret.append( "<br><font size=\"-1\">( Note: all tables included on this form must be joined to at least one other table )</font>" );
        ret.append( fieldEnd );
        ret.append( tableEnd );

        return ret.toString();
    }
    /**
     *
     *
     *@param  num  Which iteration this is. This should be used to create unique
     *      tag names
     *@param  md   Description of the Parameter
     *@return      The HTML for inclusion in the second form building page.
     */
    public String getFBHtml( DBMetaData md, int num , int thread)
    {
        StringBuffer ret = new StringBuffer();


        return ret.toString();
    }

    /**
     *  Description of the Method
     *   Dummy method to implement - but not really used
     *
     *@param  md           Description of the Parameter
     *@param  num          Description of the Parameter
     *@param  elementName  Description of the Parameter
     *@return              Description of the Return Value
     */
    public static StringBuffer buildTableFieldDropDown( DBMetaData md )
    {
        StringBuffer ret = new StringBuffer();
        Properties props = md.getDbProps();
        String schema = props.getProperty("SCHEMA");
        String sidName = props.getProperty("DBNAME");

        Collection tableMetaData = md.getAllTableMetaData();
        TableMetaData tableInfo;
        Iterator iter = tableMetaData.iterator();
        while ( iter.hasNext() )
        {
            tableInfo = ( TableMetaData ) iter.next();
            Collection fieldNames = tableInfo.getColumnNames();
            String field;
            Iterator iter2 = fieldNames.iterator();
            /*
             *  and for each field, output an option
             */
            while ( iter2.hasNext() )
            {
            	String tableName = tableInfo.getName();
            	/*if (!sidName.equals("") && !schema.equals("") )
            	{
            		tableName = sidName + "." + schema + "." + tableName;
            	}*/
                field = ( String ) iter2.next();
                ret.append( "<option value=\"Table" + FormTags.NAMEVALUE_TOKEN_STR  );
                ret.append( tableName );
                ret.append( "~Field" + FormTags.NAMEVALUE_TOKEN_STR  );
                ret.append( field );
                ret.append( "~DBName" + FormTags.NAMEVALUE_TOKEN_STR  );
                ret.append( sidName );
                ret.append( "~Schema" + FormTags.NAMEVALUE_TOKEN_STR  );
                ret.append( schema );
                ret.append( "\">" );
                ret.append( tableInfo.getName() );
                ret.append( "." );
                ret.append( field );
                ret.append( "</option>" );
            }
        }

        return ret;
    }

    /**
     *  Builds the Node to be added to the root node of an XML Document.<br>
     *  <br>
     *  Range should be of the form:<pre>
     *  <range>
     *
     *
     *
     *
     *  <table>
     *    GRIDMORA
     *  </table>
     *  <field>LONGITUDE</field> <label>Longitude</label> </range> </pre>
     *
     *@param  document  Description of the Parameter
     *@return           The fBNode value
     */
    public Node getFBNode( Document document )
    {
    	 MraldOutFile.logToFile(Config.getProperty("LOGFILE"), "CrossLinkElement: getFBNode: start");
        String table1 = nameValues.getValue( FormTags.PRIMARY_TABLE )[0];
        String field1 = nameValues.getValue( FormTags.PRIMARY_FIELD )[0];
        String table2 = nameValues.getValue( FormTags.FOREIGN_TABLE )[0];
        String field2 = nameValues.getValue( FormTags.FOREIGN_FIELD )[0];
        String link = nameValues.getValue( FormTags.LINK_TYPE )[0];

        String dbName1= nameValues.getValue( FormTags.DB_NAME_TAG + "1" )[0];
        String dbName2= nameValues.getValue( FormTags.DB_NAME_TAG + "2" )[0];
        String schema1 = nameValues.getValue( FormTags.SCHEMA_TAG + "1" )[0];
        String schema2 = nameValues.getValue( FormTags.SCHEMA_TAG + "2" )[0];

        /**GH: MultiDb: Add checks for database name and schema name for multiDb querying
         * As this is a CrossDBLink - each component of the link will have different databases**/
        String thread = nameValues.getValue(FormTags.SQL_THREAD_NUM_TAG)[0];

		if (!dbName1.equals("") && (!schema1.equals("")))
		{
			table1 = dbName1 +"." + schema1 + "." + table1;
            MraldOutFile.logToFile(Config.getProperty("LOGFILE"), "CrossLinkElement: getFBNode: table1 :" + table1);
		}

		if (!dbName2.equals("") && (!schema2.equals("")))
		{
			table2 = dbName2 +"." + schema2 + "." + table2;
            MraldOutFile.logToFile(Config.getProperty("LOGFILE"), "CrossLinkElement: getFBNode: table2 :" + table2);
		}
        /** GH: End MultiDb **/

        if ( !link.equals( "Ignore" ) )
        {

            Element ret = document.createElement( "crossLink" );

            Element table1Element = document.createElement( "primarytable" );
            Text table1Text = document.createTextNode( table1 );
            table1Element.appendChild( table1Text );
            ret.appendChild( table1Element );

            Element field1Element = document.createElement( "primaryfield" );
            Text field1Text = document.createTextNode( field1 );
            field1Element.appendChild( field1Text );
            ret.appendChild( field1Element );

            Element table2Element = document.createElement( "secondarytable" );
            Text table2Text = document.createTextNode( table2 );
            table2Element.appendChild( table2Text );
            ret.appendChild( table2Element );

            Element field2Element = document.createElement( "secondaryfield" );
            Text field2Text = document.createTextNode( field2 );
            field2Element.appendChild( field2Text );
            ret.appendChild( field2Element );

            /**MultiDB: Add the SqlThread**/
            if (!thread.equals("") )
            {
	            Element threadElement = document.createElement( "sqlThread" );
	            Text threadText = document.createTextNode( thread );
	            threadElement.appendChild( threadText );
	            ret.appendChild( threadElement );
            }
            return ret;
        }
        else
        {
            return null;
        }
    }


    /**
     *  Checks to see whether we should ignore this element or not.<br>
     *  NOTE: this only works AFTER the process method is called.
     *
     *@return    Description of the Return Value
     */
    public boolean ignore()
    {
        return nameValues.getValue( FormTags.LINK_TYPE )[0].equals( "Ignore" );
    }


    /**
     *  Set this back to active - we need this processed just like the other
     *  ParserElements in the main MsgObject
     *
     *@param  msg          Description of the Parameter
     *@param  currentName  Description of the Parameter
     *@return              Description of the Return Value
     */
    public String postProcess( MsgObject msg, String currentName )
    {
        /*
         *  set the current LinkElement's Table and Field variables,
         */
        resetVariables( "dummy" );
        isActive = true;
        return currentName;
    }


    /**
     *  Override the parent class's preProcess() method to do nothing.
     *
     *@param  msg          Description of the Parameter
     *@param  currentName  Description of the Parameter
     *@return              Description of the Return Value
     */
    public String preProcess( MsgObject msg, String currentName )
    {
        return currentName;
    }


    /**
     *  Resets the primary and secondary Table and Field Variables of the
     *  LinkElement object (primaryTable, primaryField, secondaryTable,
     *  secondaryField) and resets the nameValues MsgObject
     *
     *@param  dummyString  Description of the Parameter
     *@since
     */
    public void resetVariables( String dummyString )
    {
        primaryField = nameValues.getValue( FormTags.FIELD_TAG + "1" )[0];
        primaryTable = nameValues.getValue( FormTags.TABLE_TAG + "1" )[0];
        secondaryField = nameValues.getValue( FormTags.FIELD_TAG + "2" )[0];
        secondaryTable = nameValues.getValue( FormTags.TABLE_TAG + "2" )[0];
    }
}


