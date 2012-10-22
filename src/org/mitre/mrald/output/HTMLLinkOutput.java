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

import java.util.Collections;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import org.mitre.mrald.control.MsgObject;
import org.mitre.mrald.parser.ParserElement;
import org.mitre.mrald.query.SelectElement;
import org.mitre.mrald.query.SelectElementComparatorByOrder;
import org.mitre.mrald.util.*;
/**
 *  This HTMLOutput Class specializes the OutputManager class. It specifically
 *  gathers information pertaining to the output columns desired by the user. As
 *  the query is constructed, this class adds output information and updates the
 *  columns and joins necessary to obtain this output information
 *
 *@author     Gail Hamilton
 *@created    February 6, 2005
 *@version    1.0
 */

public class HTMLLinkOutput extends HTMLOutput
{
    String datasource = "";
    /**  the fields from the working objects */
    private List<String> fieldNames = new ArrayList<String>();
    /**  the tables from the working objects - these need to be directly tied to
     *   the fieldNames ArrayList so that tableNames.get(i) is the table for
     *   fieldNames.get(i)
     */
    private List<String> tableNames = new ArrayList<String>();

     /**
	 *  Constructor for the HTMLOutput object
	 *
	 *@since
	 */
	public HTMLLinkOutput()
	{
		super();
    }

    /**
     *  This method prepares the output file for the HTML format data.  In this
     *  case, that means setting up the hash map that maps field names to table names.
     *
     *@param  msg                         Description of the Parameter
     *@exception  ServletException        Description of the Exception
     *@exception  IOException             Description of the Exception
     *@exception  SQLException            Description of the Exception
     *@exception  OutputManagerException  Description of the Exception
     *@since
     */
    public void printStart( MsgObject msg )
        throws ServletException, IOException, SQLException, OutputManagerException
    {
	      ArrayList<ParserElement> wkObjects = msg.getWorkingObjects();
          /* Identify the data source */
          datasource = msg.getValue( FormTags.DATASOURCE_TAG )[0];
          /* get all the SelectElements out and put them in a list */
          List<SelectElement> list = new ArrayList<SelectElement>();
	      for (int i=0; i < wkObjects.size(); i++)
	      {
	    	    ParserElement wk = wkObjects.get(i);
                if (wk instanceof SelectElement) {
                    list.add((SelectElement)wk);
                }
	      }
          /*
           *  Sort the SqlElements - it is essential that the fieldNames,
           *  tableNames, and later the tableInfos be put in the same order as the
           *  felds in the query.
           */
          Collections.sort( list, new SelectElementComparatorByOrder() );
          /*
           *  Build the field and table name lists in the same order as in the query
           */
          for(int i = 0; i < list.size(); i++)
          {
              fieldNames.add( list.get(i).getField().toLowerCase() );
              tableNames.add( list.get(i).getTable() );
          }
	      super.printStart();
    }


	/**
	 *  Gets the tableHtml attribute of the HtmlKeywordSearch object
	 *
	 *@param  rs                  Description of the Parameter
	 *@return                     The tableHtml value
	 *@exception  MraldException  Description of the Exception
	 */
	public void printBody()  throws IOException, SQLException
	{
			DBMetaData md = MetaData.getDbMetaData( datasource );
			TableMetaData tableInfo;

			ArrayList <Link> allPkLinks = new ArrayList<Link>();
			ArrayList pkLinks = new ArrayList();
			ArrayList <TableMetaData> tableInfos= new ArrayList<TableMetaData>();

//			ResultSetMetaData rsmd = rs.getMetaData();
//			int colCount = rsmd.getColumnCount();
            out.print("<table border=\"1\" cellpadding=\"3\"><tbody class=\"output\">");

            /*
             *  For each column in the query, gather the TableMetaData and Primary-
             *  Foreign key information.
             */
			for(int i = 0; i < fieldNames.size(); i++)
            {
                //get metaData from staged metadata IF CANNOT FIND then tableInfo = null
				tableInfo = md.getTableMetaData(tableNames.get(i));

                //it is null
				if (tableInfo == null)
                {
					throw new NullPointerException( "Can't find metadata for table " + tableNames.get(i) );
                }

                /*
                 *  add new metaData into new ArrayList - this should keep tableInfos in synched to fieldNames and tableNames
                 */
				tableInfos.add(tableInfo);
                pkLinks = (ArrayList <Link>) md.getPKLinkData(tableInfo.getName());
                //add all link to allPkLinks
				for (int k = 0; k < pkLinks.size(); k++)
				{
					if (!allPkLinks.contains(pkLinks.get(k)))
					{
						Link link = (Link)pkLinks.get(k);

						//Check to see if the primary and foreign key columns are actually in the result set.
						//If the columns are not part of the result sset then no link can be created to the table.
						if ( fieldNames.contains( link.getPcolumn().toLowerCase() )  || fieldNames.contains( link.getFcolumn().toLowerCase()) )
                        {
                            allPkLinks.add(link);
                        }
					}
				}
			}
        try
        {
            //if found any table which PK link to them, then display with Link
			if (allPkLinks.size() != 0){
                //print Header
                StringBuffer headerRow = new StringBuffer("<tr>");
                for (int p = 0; p < niceNames.length; p++)
                {
                    headerRow.append("<th>" + niceNames[p] + "</th>");
                }
                headerRow.append("</tr>");
                out.println(headerRow.toString());
                //print table body with links
			    LinkUtils.getLinkTableHtml(
                    out,
                    md,
                    allPkLinks /* ArrayList <Link> */,
                    tableInfos /* ArrayList <TableMetaData> */,
                    rs,
                    lineLimitSize,
                    mbLimitSize);
            }else{
                // MraldOutFile.appendToFile( "Degrading to HtmlOutput" );
                super.printBody();

            }
             out.println("</tbody></table>");
            //return nothing should remove
        }
        catch (MraldException e)
        {
            throw new MraldError(e, msg);
        }
	}//method

}//class
