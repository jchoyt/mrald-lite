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
import java.util.StringTokenizer;

import org.mitre.mrald.control.MsgObject;
import org.mitre.mrald.util.Config;
import org.mitre.mrald.util.FormTags;
import org.mitre.mrald.util.MraldError;
import org.mitre.mrald.util.MraldException;
import org.mitre.mrald.util.MraldOutFile;

/**
 *  The SqlElement class is the abstract class for all of the query elements
 *  templates needed to populate a complete query string. This class contains
 *  the common functionality for building query strings in general
 *
 *@author     Gail Hamilton
 *@created    February 17, 2001
 *@version    1.0
 *@see        java.lang.Object
 */
public class LinkElement extends SqlElements implements Cloneable
{
//    private String fieldEnd = "</tr>\n";
//    private String fieldStart = "<tr align=\"center\">\n";
    private boolean needed = false;
    protected String primaryField;
    protected String primaryTable;
    protected String secondaryField;
    protected String secondaryTable;
    protected String outerJoin="";
//    private String tableEnd = "</table></center>\n";
//    private String tableHeader = "<tr align=\"center\"><font size=\"-1\"><th width=\"150\">Primary</th><th width=\"150\">Foreign</th><th width=\"100\">Ignore</th><th width=\"100\">Full</th><th width=\"100\">Left Outer</th><th width=\"100\">Right Outer</th></font></tr>\n";
//    private String tableStart = "<center><table cellspacing=\"0\" cellpadding=\"0\" border=\"1\">\n";


    /**
     *  Constructor for the LinkElement object
     *
     *@since
     */
    public LinkElement()
    {
        elementType = FormTags.LINK_TAG;
    }


    /**
     *  Builds a value String for a name value pair from the given parameters.
     *  Ex: <code>PrimaryLink:ASQPCOMMON.ASQPDATAID~SecondaryLink:ASQPARRGMT.ASQPDATAID</code>
     *
     *@param  primaryTable    Description of Parameter
     *@param  primaryField    Description of Parameter
     *@param  secondaryTable  Description of Parameter
     *@param  secondaryField  Description of Parameter
     *@return                 Description of the Returned Value
     */
    public static String buildValue( String primaryTable, String primaryField,
            String secondaryTable, String secondaryField )
    {
        StringBuffer ret = new StringBuffer();
        ret.append( FormTags.PRIMARY_LINK_TAG );
        ret.append( ":" );
        ret.append( primaryTable );
        ret.append( "." );
        ret.append( primaryField );
        ret.append( "~" );
        ret.append( FormTags.SECONDARY_LINK_TAG );
        ret.append( ":" );
        ret.append( secondaryTable );
        ret.append( "." );
        ret.append( secondaryField );
        return ret.toString();
    }


    /**
     *  Sets the Needed attribute of the LinkElement object
     *
     *@param  neededVal  The new Needed value
     *@since
     */
    public void setNeeded( boolean neededVal )
    {
        needed = neededVal;
    }


    /**
     *  Gets the Needed attribute of the LinkElement object
     *
     *@return    The Needed value
     *@since
     */
    public boolean getNeeded()
    {
        return needed;
    }


    /**
     *  Gets the PrimaryField attribute of the LinkElement object
     *
     *@return    The PrimaryField value
     *@since
     */
    public String getPrimaryField()
    {
        return primaryField;
    }


    /**
     *  Gets the PrimaryTable attribute of the LinkElement object
     *
     *@return    The PrimaryTable value
     *@since
     */
    public String getPrimaryTable()
    {
        return primaryTable;
    }


    /**
     *  Gets the qual attribute of the LinkElement object
     *
     *@return    The qual value
     */
    public String getQual()
    {
        StringBuffer ret = new StringBuffer();
        //Need to Parse out the Syn table Name
        boolean tableSpace=false;

	//StringTokenizer tableName = new StringTokenizer( primaryTable, " " );

	//If there is quotes around the table name then there is a space in the table Name
        if ( (primaryTable.indexOf("\"") == 0) && (primaryTable.lastIndexOf("\"") == primaryTable.length()) )
		tableSpace =true;

	String[] tableComps =  primaryTable.split(" ");

	//If 3 then table has space name and syn
	if (tableComps.length == 3)
		table = tableComps[2];
	else if (tableComps.length == 2)
	{
		//Check to see if there is an incidence of "-indicating table with space
		if (tableSpace)
			table = tableComps[0] + " " + tableComps[1];
		else //Use the synomyn
			table = tableComps[1];
	}
	else //Just use the full string
		table =tableComps[0];

	//org.mitre.mrald.util.MraldOutFile.logToFile( org.mitre.mrald.util.Config.getProperty("LOGFILE") , "LinkElement: getQual. TableName: " + table);
	/*String table = tableName.nextToken();
        // use the synonym if it has one
        if ( tableName.hasMoreTokens() )
        {
            table = tableName.nextToken();
        }*/
        ret.append( table );
        ret.append( "." );
        ret.append( primaryField );

//      Temporary Code :GH This will only work for Oracle
        if (outerJoin.equals(FormTags.OUTER_JOIN_LEFT))
        	ret.append( "(+) ");

        ret.append( "=" );

        //Need to Parse out the Syn table Name
	if ( (secondaryTable.indexOf("\"") == 0) && (secondaryTable.lastIndexOf("\"") == secondaryTable.length()-1) )
		tableSpace =true;
	else
		tableSpace = false;

	tableComps =  secondaryTable.split(" ");

	//If 3 then table has space name and syn
	if (tableComps.length == 3)
		table = tableComps[2];
	else if (tableComps.length == 2)
	{
		//Check to see if there is an incidence of "-indicating table with space
		if (tableSpace)
			table = tableComps[0] + " " + tableComps[1];
		else //Use the synomyn
			table = tableComps[1];
	}
	else //Just use the full string
		table =tableComps[0];


        ret.append( table );
        ret.append( "." );
        ret.append( secondaryField );

        if (outerJoin.equals(FormTags.OUTER_JOIN_RIGHT))
        	ret.append( "(+) ");


        MraldOutFile.logToFile(Config.getProperty("LOGFILE"), "LinkElement: getQual: " + ret.toString());
        return ret.toString();
    }


    /**
     *  Gets the SecondaryField attribute of the LinkElement object
     *
     *@return    The SecondaryField value
     *@since
     */
    public String getSecondaryField()
    {
        return secondaryField;
    }


    /**
     *  Gets the SecondaryTable attribute of the LinkElement object
     *
     *@return    The SecondaryTable value
     *@since
     */
    public String getSecondaryTable()
    {
        return secondaryTable;
    }


    /**
     *  This is handled by other classes (MraldDijkstra)
     *
     *@param  currentFromList            Description of Parameter
     *@return                            Description of the Returned Value
     *@exception  MraldException  Description of Exception
     *@since
     */
    public ArrayList<String> buildFrom( ArrayList<String> currentFromList )
        throws MraldException
    {
        return currentFromList;
    }


    /**
     *  Description of the Method
     *
     *@param  currentGroupByList         Description of Parameter
     *@return                            Description of the Returned Value
     *@exception  MraldException  Description of Exception
     *@since
     */
    public ArrayList<String> buildGroupBy( ArrayList<String> currentGroupByList )
        throws MraldException
    {
        return currentGroupByList;
    }


    /**
     *  Description of the Method
     *
     *@param  currentOrderBy             Description of Parameter
     *@return                            Description of the Returned Value
     *@exception  MraldException  Description of Exception
     *@since
     */
    public ArrayList<String> buildOrderBy( ArrayList<String> currentOrderBy )
        throws MraldException
    {
        return currentOrderBy;
    }


    /**
     *  Description of the Method
     *
     *@param  currentSelectList          Description of Parameter
     *@return                            Description of the Returned Value
     *@exception  MraldException  Description of Exception
     *@since
     */
    public ArrayList<String> buildSelect( ArrayList<String> currentSelectList )
        throws MraldException
    {
        return currentSelectList;
    }


    /**
     *  This is handled by other classes (MraldDijkstra) *
     *
     *@param  currentWhereList           Description of Parameter
     *@return                            Description of the Returned Value
     *@exception  MraldException  Description of Exception
     *@since
     */
    public ArrayList<String> buildWhere( ArrayList<String> currentWhereList )
        throws MraldException
    {
        return currentWhereList;
    }


    /**
     *  Description of the Method
     *
     *@return                                 Description of the Returned Value
     *@exception  CloneNotSupportedException  Description of Exception
     *@since
     */
    public Object clone()
        throws CloneNotSupportedException
    {
        return super.clone();
    }


    /**
     *  Set this item inactive - don't want to add it to the qeList in
     *  MraldParser since we are storing them separately in the main MsgObject
     *
     *@param  msg          Description of the Parameter
     *@param  currentName  Description of the Parameter
     *@return              Description of the Return Value
     *@see                 #preProcess
     */
    public String postProcess( MsgObject msg, String currentName )
    {
        isActive = false;
        return currentName;
    }


    /**
     *  Removed the LinkElements from the main msgObject and put them into the
     *  special link holder for processing during the querybuilding phase.
     *
     *@param  msg          Description of Parameter
     *@param  currentName  Description of Parameter
     *@return              Description of the Returned Value
     *@since
     */
    public String preProcess( MsgObject msg, String currentName )
    {
        /*
         *  Add items into the MsgObject.links ArrayList
         *  These will be retrieved and processed after all the other
         *  SqlElements are processed.  They will be removed from
         *  MsgObject.nvPairs by the MraldParser
         */
        try
        {
            String[] valueList = msg.getValue( currentName );
            for ( int i = 0; i < valueList.length; i++ )
            {
                /*
                 *  set the current LinkElement's Table and Field variables,
                 *  then add a copy of this object to the main MsgObject
                 */
                resetVariables( valueList[i] );
                MraldOutFile.logToFile(Config.getProperty("LOGFILE"), "LinkElement : preProcess: adding a link " + this.toString());

                msg.addLink( (LinkElement)this.clone() );
            }
            return currentName;
        }
        catch ( CloneNotSupportedException e )
        {
            throw new MraldError( e, msg );
        }
        catch ( MraldException e )
        {
            throw new MraldError( e, msg );
        }
    }


    /**
     *  Resets the primary and secondary Table and Field Variables of the
     *  LinkElement object (primaryTable, primaryField, secondaryTable,
     *  secondaryField) and resets the nameValues MsgObject
     *
     *@param  linkString          Description of Parameter
     *@exception  MraldException  Description of the Exception
     *@since
     */
    public void resetVariables( String linkString )
        throws MraldException
    {
        String var;
        StringTokenizer tableFieldPair;

        String[] temp = new String[1];
        temp[0] = linkString;
        /*
         *  clear nameValues (a msgObject defined in the parent class),
         *  then parses out the n/v pairs into nameValues
         */
        nameValues.clearNvPairs();
        try
        {
            process( temp );
        }
        catch ( SqlElementException e )
        {
            throw new MraldException( e );
        }
        var = nameValues.getValue( FormTags.SECONDARY_LINK_TAG )[0];
        tableFieldPair = new StringTokenizer( var, "." );
        /*
         *  deconstructing the passed string into table and field
         *  to account for where we need to specify the schema, have to look at the case
         *  where there are three tokens i.e., schema.table.field
         */
        /**MultiDB: There may be as many as 4 countTokens dbName.schemaName.TableName.FieldName**/
        if ( tableFieldPair.countTokens() == 4 )
        {

        	secondaryTable =  tableFieldPair.nextToken() +"."+tableFieldPair.nextToken() + "." + tableFieldPair.nextToken() ;//set primary link schema.table

        }
        else if ( tableFieldPair.countTokens() == 3 )
        {
            secondaryTable = tableFieldPair.nextToken() + "." + tableFieldPair.nextToken();//set secondary link schema.table
        }
        else
        {
            secondaryTable = tableFieldPair.nextToken();//set secondary link table
        }
        secondaryField = tableFieldPair.nextToken();//set secondary link field
        var = nameValues.getValue( FormTags.PRIMARY_LINK_TAG )[0];
        tableFieldPair = new StringTokenizer( var, "." );

        /**MultiDB: There may be as many as 4 countTokens dbName.schemaName.TableName.FieldName**/
        if ( tableFieldPair.countTokens() == 4 )
        {

        	primaryTable =  tableFieldPair.nextToken() +"."+tableFieldPair.nextToken() + "." + tableFieldPair.nextToken() ;//set primary link schema.table

        }
        else if ( tableFieldPair.countTokens() == 3 )
        {
            primaryTable = tableFieldPair.nextToken() + "." + tableFieldPair.nextToken();//set primary link schema.table
        }
        else
        {
            primaryTable = tableFieldPair.nextToken();//set primary link table
        }
        primaryField = tableFieldPair.nextToken();//set primary link field

        MraldOutFile.logToFile(Config.getProperty("LOGFILE"), "LinkElement: reset variables:Primary Table " + primaryTable + " primary field : " + primaryField);

        String outerVar = nameValues.getValue( FormTags.OUTER_JOIN )[0];
        if (outerVar != null)
        	if (!outerVar.equals(""))
        		outerJoin = outerVar;
    }
}


