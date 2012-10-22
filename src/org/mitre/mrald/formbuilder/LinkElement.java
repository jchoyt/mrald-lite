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
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import org.mitre.mrald.control.MsgObject;
import org.mitre.mrald.util.Config;
import org.mitre.mrald.util.DBMetaData;
import org.mitre.mrald.util.FBUtils;
import org.mitre.mrald.util.FormTags;
import org.mitre.mrald.util.Link;
import org.mitre.mrald.util.MraldOutFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
/**
 *  Description of the Class
 *
 *@author     jchoyt
 *@created    April 1, 2003
 */
public class LinkElement extends org.mitre.mrald.query.LinkElement implements FormBuilderElement
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
     *  Constructor for the LinkElement object
     *
     *@since
     */
    public LinkElement() { }


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
    public String getFBHtml( DBMetaData md, int num , int thread)
    {
        StringBuffer ret = new StringBuffer();

        if ( md == null )
        {
            return "No Avaliable Meta Data...";
        }

        Set formLinks = md.getLinkList();

        if ( formLinks == null )
        {
            return "No Avaliable Links...";
        }

        Iterator iter = formLinks.iterator();

        ret.append( tableStart );
        ret.append( tableHeader );

        Link linkInfo;
        int counter = 3;
        String pcolumn;
        String ptable;
        String fcolumn;
        String ftable;
        String fullLink;

        Properties props = md.getDbProps();

        String schema = props.getProperty("SCHEMA");
        String sidName = props.getProperty("DBNAME");
         /*
         *  for each link . . .
         */
        while ( iter.hasNext() )
        {
            linkInfo = ( Link ) iter.next();
            ptable = linkInfo.getPtable();
            pcolumn = linkInfo.getPcolumn();
            ftable = linkInfo.getFtable();
            fcolumn = linkInfo.getFcolumn();

            //MultiDB - Added DbName and schema
            fullLink =  "DBName:" + sidName +"~Schema:" + schema + "~Table1:" + ptable + "~Field1:" + pcolumn + "~Table2:" + ftable + "~Field2:" + fcolumn + "~SqlThread:"+ thread;

            ret.append( fieldStart );
            ret.append( "<td align=\"left\" width=\"25%\">" + ptable + "." + pcolumn + "</td>\n" );
            ret.append( "<td align=\"left\" width=\"25%\">" + ftable + "." + fcolumn + "</td>\n" );
            ret.append( "<td width=\"25%\"><input type=\"radio\" name=\"FBLink" + counter +  thread + "\" value=\"" + fullLink + "~Link:Ignore \" ></td>\n" );
            ret.append( "<td width=\"25%\"><input type=\"radio\" name=\"FBLink" + counter +  thread + "\" value=\"" + fullLink + "~Link:Full \" CHECKED></td>\n" );
            //ret.append( "<td width=\"13%\"><input type=\"radio\" name=\"FBLink" + counter + "\" value=\"" + fullLink + "~Link:LeftOuter\" ></td>\n" );
            //ret.append( "<td width=\"13%\"><input type=\"radio\" name=\"FBLink" + counter + "\" value=\"" + fullLink + "~Link:RightOuter\" ></td>\n" );
            ret.append( fieldEnd );

            counter++;
        }

        /*
         *  three extra rows
         */
        StringBuffer dropdown;
        String counterStr = "";
        for ( int i = 0; i < 3; i++ )
        {

        	counterStr = i + "" + thread;
        	int uniqueCount = Integer.parseInt(counterStr);
            dropdown = new StringBuffer();
            dropdown.append( "<td>" );
            dropdown.append( FBUtils.buildTableFieldDropDown( md, "FBLink", uniqueCount ).toString() );
            dropdown.append( "</td>\n" );

            ret.append( fieldStart );
            ret.append("<input type=\"hidden\" name=\"FBLink" + uniqueCount +"\" value=\"DBName:" + sidName +"~Schema:" + schema + "~SqlThread:"+ thread + "\" />");

            ret.append( dropdown.toString().replaceAll( "Table:", "Table1:" ).replaceAll( "Field:", "Field1:" ) );
            ret.append( dropdown.toString().replaceAll( "Table:", "Table2:" ).replaceAll( "Field:", "Field2:" ) );
            ret.append( "<td width=\"25%\"><input type=\"radio\" name=\"FBLink" + uniqueCount+ "\" value=\"Link:Ignore~SqlThread:"+ thread + "\" CHECKED></td>\n" );
            ret.append( "<td width=\"25%\"><input type=\"radio\" name=\"FBLink" + uniqueCount + "\" value=\"Link:Full~SqlThread:"+ thread + "\" ></td>\n" );
            //ret.append( "<td width=\"13%\"><input type=\"radio\" name=\"FBLink" + i + "\" value=\"Link:LeftOuter\" ></td>\n");
            //ret.append( "<td width=\"13%\"><input type=\"radio\" name=\"FBLink" + i + "\" value=\"Link:RightOuter\" ></td>\n");
            ret.append( fieldEnd );
        }//for
        ret.append( fieldStart );
        ret.append( "<br><font size=\"-1\">( Note: all tables included on this form must be joined to at least one other table )</font>" );
        ret.append( fieldEnd );
        ret.append( tableEnd );

        return ret.toString();
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
    	String multiDb = nameValues.getValue( FormTags.MULTI_DB )[0];
    	String table1 = nameValues.getValue( FormTags.PRIMARY_TABLE )[0];
        String field1 = nameValues.getValue( FormTags.PRIMARY_FIELD )[0];
        String table2 = nameValues.getValue( FormTags.FOREIGN_TABLE )[0];
        String field2 = nameValues.getValue( FormTags.FOREIGN_FIELD )[0];
        String link = nameValues.getValue( FormTags.LINK_TYPE )[0];

        /**GH: MultiDb: Add checks for database name and schema name for multiDb querying**/

        String thread = nameValues.getValue(FormTags.SQL_THREAD_NUM_TAG)[0];

	  //If this is not a multiple database form
    	if (!multiDb.equals(""))
    	{

    		 String dbName = nameValues.getValue(FormTags.DB_NAME_TAG)[0];
    		 String schemaName = nameValues.getValue(FormTags.SCHEMA_TAG)[0];

			if (!dbName.equals("") && (!schemaName.equals("")))
			{
				table1 = dbName +"." + schemaName + "." + table1;
				table2 = dbName +"." + schemaName + "." + table2;
	            MraldOutFile.logToFile(Config.getProperty("LOGFILE"), "LinkElement: getFBNode: table1 :" + table1);
			}
        /** GH: End MultiDb **/
    	}

        if ( !link.equals( "Ignore" ) )
        {

            Element ret = document.createElement( "link" );

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


