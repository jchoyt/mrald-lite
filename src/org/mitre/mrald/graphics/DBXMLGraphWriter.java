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
package org.mitre.mrald.graphics;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.jsp.JspException;

import org.mitre.mrald.control.AbstractStep;
import org.mitre.mrald.control.MsgObject;
import org.mitre.mrald.control.WorkflowStepException;
import org.mitre.mrald.util.Config;
import org.mitre.mrald.util.DBMetaData;
import org.mitre.mrald.util.FBUtils;
import org.mitre.mrald.util.Link;
import org.mitre.mrald.util.MetaData;
import org.mitre.mrald.util.MraldException;
import org.mitre.mrald.util.TableMetaData;
/**
 * Writes out a graph to an XGMML-format XML file. See
 * <a href="http://www.cs.rpi.edu/~puninj/XGMML/">www.cs.rpi.edu/~puninj/XGMML/</a>
 * for a description of the XGMML format.
 * @author ghamilton
 * @version 1.0
 */
public class DBXMLGraphWriter extends AbstractStep{

	public static final String NODE   = "node";
	public static final String EDGE   = "edge";
	public static final String ATT    = "att";
	public static final String ID     = "id";
	public static final String LABEL  = "label";
	public static final String SOURCE = "source";
	public static final String TARGET = "target";
	public static final String WEIGHT = "weight";
	public static final String NAME   = "name";
	public static final String VALUE  = "value";
	public static final String LIST   = "list";
	public static final String GRAPH  = "graph";
	public static final String DIRECTED = "directed";
	public static final String PKEY = "pkey";
	public static final String FKEY = "fkey";
	public static final String TABLE = "table";
	public static final String COLUMN= "column";
	public static final String TYPE= "type";

	public static final String NODE_ATTR[] = {ID, LABEL, WEIGHT};
	public static final String EDGE_ATTR[] = {LABEL, WEIGHT	};
    private String datasource;
	private static String tempDir="";

	public DBXMLGraphWriter()
	{
		tempDir = Config.getProperty( "BasePath" ) + "/";

	}

	/**
     *  This method calls the AirRegistration algorithm. That is used to
     *  co-register all MRI images to one template.
     *
     *@param  msg                        Description of the Parameter
     *@exception  WorkflowStepException  Required to satisfy the AbstractStep
     *      interface. All Exceptions not handled should be rethrown to this.
     *@since
     */
    public void execute( MsgObject msg )
        throws WorkflowStepException
    {
        try
        {

//            String action = msg.getValue( "action" )[0];
            String fileName= msg.getValue( "fileName" )[0];
            datasource = msg.getValue( "datasource" )[0];
	    if ((fileName== null) || (fileName.equals("") ) )
		    fileName  = "temp.txt";
                writeXML(tempDir + fileName);
	   msg.getRes().sendRedirect( "dbViz.jsp" );


        }
        catch ( MraldException s )
        {

            throw new WorkflowStepException( s.getMessage() );
        }
         catch ( java.io.IOException s )
        {

            throw new WorkflowStepException( s.getMessage() );
        }

    }

	public void writeXML(String fileName) throws MraldException
	{
		try
		{
			DBMetaData md = MetaData.getDbMetaData( datasource );

//			String filename = tempDir+ fileName;
			PrintWriter out = new PrintWriter( new BufferedOutputStream(new FileOutputStream(fileName)) );
			out.print( displayTables(md));
			out.flush();
			out.close();
		}
		catch(java.io.FileNotFoundException fe)
		{
			throw new MraldException( fe.getMessage()) ;
		}
	}


	/**
	 *  Description of the Method
	 *
	 *@return                   Description of the Return Value
	 *@exception  JspException  Description of the Exception
	 */
	private String displayTables(DBMetaData md)
		 throws MraldException
	{

//		String width = "25";
		StringBuffer strOut = new StringBuffer();

		strOut.append("<graph directed=\"0\">");
		ArrayList tables = (ArrayList) md.getAllTableMetaData();
		Iterator iter = tables.iterator();
		HashMap keys = DBMetaData.setLinks(md);

		while (iter.hasNext())
		{
			TableMetaData table = (TableMetaData) iter.next();
			String tableName = table.getName();

			strOut.append("  <"+NODE);
			for ( int i = 0; i < NODE_ATTR.length; i++ )
			{
				String key = NODE_ATTR[i];
				if ( tableName != null )
					strOut.append(" "+key+"=\""+tableName+"\"");
			}
			strOut.append(">"  + "\n");

			strOut.append(displayColumns(table, keys));

			strOut.append("  </"+NODE + ">\n");

		}

		//Print out the Source and Target
		Collection links = md.getLinkList();

		Iterator linksIter = links.iterator();

		while (linksIter.hasNext())
		{

			Link link = (Link)linksIter.next();
			strOut.append( displayKeyDetails(link));
		}
		strOut.append("</graph>");
		return strOut.toString();
	}



	/**
	 *  Description of the Method
	 *
	 *@param  link              Description of the Parameter
	 *@return                   Description of the Return Value
	 *@exception  JspException  Description of the Exception
	 */
	private String displayKeyDetails(Link link)
	{
		StringBuffer strOut = new StringBuffer();

		strOut.append("  <"+EDGE);
		strOut.append(" "+SOURCE+"=\""+link.getPtable() +"\"");
		strOut.append(" "+TARGET+"=\""+link.getFtable() +"\"");
		strOut.append("/>" + "\n");
		return strOut.toString();
	}

	/**
	 *  Description of the Method
	 *
	 *@param  table             Description of the Parameter
	 *@return                   Description of the Return Value
	 *@exception  JspException  Description of the Exception
	 */
	private String displayColumns(TableMetaData table, HashMap keys)
		 throws MraldException
	{
//		String width = "50";

		StringBuffer strOut = new StringBuffer();

		Collection columns = table.getColumnNames();
		Iterator iter = columns.iterator();

		int i=0;
		int fkeyCount = 0;
		while (iter.hasNext())
		{

			i++;
			String columnName = (String) iter.next();

			strOut.append("  <"+ATT);
			strOut.append(" name=\""+COLUMN+i + "\" value=\""+columnName+"\"");
			strOut.append("/>" + "\n");

			String type = FBUtils.getSqlType(table.getFieldType(columnName).intValue());
			strOut.append("  <"+ATT);
			strOut.append(" name=\""+TYPE+ i + "\" value=\""+type+"\"");
			strOut.append("/>" + "\n");

			boolean isFk = DBMetaData.isFkey(table.getName(), columnName, keys);
			if (isFk)
			{
					fkeyCount++;
					strOut.append("  <"+ATT);
					strOut.append(" name=\""+FKEY+ fkeyCount + "\" value=\""+columnName+"\"");
					strOut.append("/>" + "\n");
			}
		}

		Collection pKeys = table.getPrimaryKeys();
		iter = pKeys.iterator();
		i=0;
		while (iter.hasNext())
		{

			i++;
			String primaryKey = (String) iter.next();
			strOut.append("  <"+ATT);
			strOut.append(" name=\""+PKEY+ i + "\" value=\""+primaryKey+"\"");
			strOut.append("/>" + "\n");

		}

		return strOut.toString();
	}


	/* PM: Never used!
	private String getValue(String id, String key)
	{
		return "";
	}*/
} // end of class XMLGraphWriter
