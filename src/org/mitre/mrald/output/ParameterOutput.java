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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.ServletException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.mitre.mrald.control.MsgObject;
import org.mitre.mrald.util.Config;
import org.mitre.mrald.util.MraldError;
import org.mitre.mrald.util.MraldException;
import org.mitre.mrald.util.MraldParseException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public abstract class ParameterOutput extends HTMLOutput
{
	protected Set<String>[] categories;
	protected String algorithmName;
	protected String dataFile;
	protected int maxCategories;
	protected char dataFileDelimiter;

    public ParameterOutput()
	{
        super();
		maxCategories = Integer.parseInt( Config.getProperty("MAXCATEGORIES") );
	}

    public @Override void formatOutput()
        throws MraldException
    {
		try
        {
            algorithmName = ( msg.getValue( "Format" )[0] );
            dataFile = makeDataFile( msg );
        }
        catch(ServletException e)
        {
            throw new MraldException(e, msg);
        }
        catch(IOException e)
        {
            throw new MraldException(e, msg);
        }
        catch(SQLException e)
        {
            throw new MraldException(e, msg);
        }
        super.formatOutput();
    }

    protected @Override void prepareHeaders()
    {
        msg.setContentType( "text/html" );
        msg.setHeader( "Content-Disposition", "inline;" );
    }


	protected String makeDataFile( MsgObject msg )
		throws ServletException, IOException, SQLException, OutputManagerException
 	{
		String fileLocation = msg.getUserId() + System.currentTimeMillis() + ".data";
		File dir = new File( System.getProperty( "java.io.tmpdir" ) );
		if (!dir.exists())
			dir.mkdir();

		FileOutputStream fout = new FileOutputStream( new File( System.getProperty( "java.io.tmpdir" ) + "/" + fileLocation ) );

        int row_count = 0;
        float fileSize = 0;
        StringBuffer formattedString;
		String currentValue;

		initializeSets( niceNames.length );

        while ( ( rs.next() ) &&
                ( ( lineLimitSize == -1 ) || ( row_count < lineLimitSize ) ) &&
                ( ( mbLimitSize == -1 ) || ( fileSize < mbLimitSize ) ) )
        {

			formattedString = new StringBuffer("");

            for ( int i = 0; i < niceNames.length; i++ )
            {
				if ( ! formattedString.toString().equals("") )
					formattedString.append( dataFileDelimiter );
				//
				// Finding correct formatting of the output
				//
                if ( classNames[i].equals( "Timestamp" ) )
                    currentValue = getAndFormat( rs.getTimestamp( i + 1 ), formats[i] );

                else if ( classNames[i].equals( "BigDecimal" ) )
                    currentValue = getAndFormat( rs.getBigDecimal( i + 1 ), formats[i] );

                else if ( classNames[i].equals( "String" ) )
                    currentValue = "\"" + rs.getString( i + 1 ) + "\"";

                else
                    currentValue = rs.getString( i + 1 );

				categories[i].add( rs.getString( i + 1 ) );
				formattedString.append( currentValue );
				//
				// Finding the correct size to be added to the returned file size
				//
                try
				{
                    fileSize += formattedString.length() + 9; // 9 for tags
				}
                catch ( NullPointerException npe )
				{
                    fileSize += 13; // 13 for tags and "null"
				}
            }

            fout.write( formattedString.toString().getBytes() );
            fout.write( Config.NEWLINE.getBytes() );

            row_count++;
            fileSize += 10;

        }
        recordsReturned = row_count;
        bytesReturned = fileSize;
		fout.close();

		return System.getProperty( "java.io.tmpdir" ) + "/" + fileLocation;
	}

	public  @Override void printBody()
        throws IOException
	{
		try
		{
			out.println( "<form action=\"FormSubmit\" method=\"POST\">" );
			out.println( "<input type=\"hidden\" name=\"workflow\" value=\"Analysis\">" );
			out.println( "<input type=\"hidden\" name=\"Format\" value=\"" + algorithmName + "\">" );
			out.println( "<input type=\"hidden\" name=\"dataFile\" value=\"" + dataFile + "\">" );
			printHeader(  );
			out.println( "<br><br>" );
			printFieldsHTML(  );
			out.println( "<br><br>" );
			printParametersHTML(  );
			out.println( "<br><br>" );
			out.println( "<input type=\"submit\" name=\"submit\" value=\"Start Analysis\">" );
			out.println( "<br><br>" );
			out.println( "<input type=\"reset\" name=\"reset\" value=\"Reset Form\">" );
			out.println( "<br><br>" );
			out.println( "</form>" );
		}
		catch ( DOMException dome )
		{
			throw new MraldError( dome );
		}
		catch ( ParserConfigurationException pce )
		{
			throw new MraldError( pce );
		}
		catch ( MraldParseException mpe )
		{
			throw new MraldError( mpe );
		}
	}

	protected void printFieldsHTML( )
	{
		boolean canBeInt = false;

		printFieldsTableHeader(  );

        StringBuffer headerRow = new StringBuffer();
		String tempNiceNames;
        for ( int p = 0; p < niceNames.length; p++ )
        {
			tempNiceNames = niceNames[p].replace( ' ', '_' );
			headerRow.append( "<tr>\n<td>\n" );
			headerRow.append( tempNiceNames + "\n" );
			headerRow.append( "<input type=\"hidden\" name=\"Analysis" + p + "\" value=\"Order:" + p + "~Field:" + tempNiceNames + "\">\n" );
			headerRow.append( "</td>\n<td>\n" + classNames[p] + "\n</td>\n" );

			headerRow.append( "<td>\n" );

			if ( categories[p].contains( "null" ) || categories[p].contains( "null" ) )
				canBeInt = false;
			else
				canBeInt = true;

			if ( categories[p].size() <= maxCategories )
			{
				if ( ! canBeInt )
					headerRow.append( categories[p].size() + " (but contains NULL)\n" );
				else
					headerRow.append( categories[p].size() + "\n" );

				headerRow.append( printSetContents( p ) );
			}
			else
			{
				headerRow.append( "Count Too High For Analysis (" + categories[p].size() + ")\n" );
			}

			headerRow.append( "</td>\n" );

            headerRow.append( "<td>\n" );
			headerRow.append( "<select name=\"Analysis" + p + "\">\n" );
			headerRow.append( getDropDown( classNames[p], p, categories[p].size(), canBeInt ) );
			headerRow.append( "</select>\n" );
			headerRow.append( "</td>\n" );
			headerRow.append( "</tr>" );

        }
        out.println( headerRow.toString() );

		printFieldsTableFooter(  );

	}

	protected abstract String getDropDown( String classType, int instanceNum, int count, boolean canBeInt );


	// PM: Needed to create an array of hashsets.
	@SuppressWarnings("unchecked")
	protected void initializeSets( int setCount )
	{
		categories = new HashSet[ setCount ];
		for ( int i = 0; i < setCount; i++ )
		{
			categories[i] = new HashSet<String>();
		}
	}

	protected String printSetContents( int setNumber )
	{
        Iterator iter = categories[ setNumber ].iterator();
		StringBuffer ret = new StringBuffer();

		String currentValue;
		while ( iter.hasNext() )
		{
            currentValue = ( String ) iter.next();
			ret.append( "<input type=\"hidden\" name=\"Analysis" + setNumber + "\" value=\"" + currentValue + "\">\n" );
		}

		return ret.toString();
	}

	protected void printParametersHTML()
		throws IOException, DOMException, ParserConfigurationException, MraldParseException
	{
		printParameterTableHeader( );
		//
		// Read in the XML and output the appropriate parameters
		//
		processXML( );
		out.println( "</table>" );
	}

	protected String getFieldDropDown()
	{
        StringBuffer ret = new StringBuffer();
        for ( int p = 0; p < niceNames.length; p++ )
        {
            ret.append( "<option value=\"" + p + "\">" + niceNames[p] + "</option>\n" );
		}
		return ret.toString();
	}

	protected void printFieldsTableHeader( )
	{
		out.println( "<table border='0' width='70%'>" );
		out.println( "<tr><th colspan=\"4\">Template Configuration</th></tr>" );
		out.println( "<tr>" );
		out.println( "<th>Field Name</th>" );
		out.println( "<th>Field Type</th>" );
		out.println( "<th>Category Count</th>" );
		out.println( "<th>Analysis Type</th>" );
		out.println( "</tr>" );
	}

	protected void printFieldsTableFooter()
	{
		out.println( "</table>" );
	}

	protected void printParameterTableHeader( )
	{
		out.println( "<table border='0' width='70%'>" );
		out.println( "<tr><th colspan=\"3\">Algorithm Parameters</th></tr>" );
		out.println( "<tr>" );
		out.println( "<th width=\"25%\">Name</th>" );
		out.println( "<th width=\"15%\">Default</th>" );
		out.println( "<th>Value</th>" );
		out.println( "</tr>" );
	}

	protected void printHeader( )
	{
		out.println( "<table cellspacing='0' cellpadding='10' border='0' width='70%'>" );
		out.println( "<tr><th>Description</th></tr>" );
		out.println( "<tr><td><br>" );
		out.println( "The information provided below will help guide you through the next step" );
		out.println( "in the data analysis portion of MRALD. Because of the nature of data analysis," );
		out.println( "the configuration and preparation of the accompanying data and template files" );
		out.println( "are integral to the validation and accuracy of the anaylsis results.  Please" );
		out.println( "modify and review the information below as it will be used to create the template" );
		out.println( "file for the anaylsis as well as define the parameters for the selected algorithm." );
		out.println( "<br><br></td></tr>" );
		out.println( "</table>" );
	}

	protected void processXML( )
        throws IOException, DOMException, ParserConfigurationException, MraldParseException
    {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try
        {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse( Config.getProperty( "ANALYSISXML" ) );
            Element rootElement = document.getDocumentElement();
            if ( !rootElement.hasChildNodes() )
            {
                MraldParseException parsingException = new MraldParseException( "The document is invalid. It does not contain any child nodes." );
                throw parsingException;
            }
			//
			// Getting all modes matching the Format value in the request
			//
			NodeList algorithmList = rootElement.getElementsByTagName( algorithmName );
			if ( algorithmList == null || algorithmList.getLength() != 1 )
			{
                MraldParseException parsingException = new MraldParseException( "The document is invalid. There must be at least and at most one node with the name, " + algorithmName + "." );
                throw parsingException;
			}
			//
			// Checking to see if the current node has any associated parameters
			//
			if ( algorithmList.getLength() > 0 && ! ( algorithmList.item( 0 ).hasChildNodes() ) )
			{
				out.println( "<tr>\n<td align=\"center\" colspan=\"3\"><br>No Parameters Associated with this Algorithm<br><br></td>\n</tr>\n" );
			}
			else
			{
				NodeList parameterList = algorithmList.item( 0 ).getChildNodes();
				for ( int i = 0; i < parameterList.getLength(); i++ )
				{
					if ( parameterList.item( i ).getNodeType() != Node.TEXT_NODE )
					{
						Element currentParameter = ( Element ) parameterList.item( i );
						out.println( "<tr>\n" );
						out.println( "<td>" + currentParameter.getElementsByTagName( "name" ).item( 0 ).getFirstChild().getNodeValue() + "</td>\n" );

						if ( currentParameter.getElementsByTagName( "default" ).getLength() > 0 )
							out.println( "<td align=\"center\">" + currentParameter.getElementsByTagName( "default" ).item( 0 ).getFirstChild().getNodeValue() + "</td>\n" );
						else
							out.println( "<td></td>\n" );

						if ( currentParameter.getElementsByTagName( "type" ).getLength() > 0 )
						{
							if ( currentParameter.getElementsByTagName( "type" ).item( 0 ).getFirstChild().getNodeValue().equals( "FieldDropDown" ) )
							{
								out.print( "<td>" );
								out.println( "<select name=\"" + currentParameter.getElementsByTagName( "fieldname" ).item( 0 ).getFirstChild().getNodeValue() + "\">" );
								out.print( getFieldDropDown() );
								out.println( "</select>" );
								out.println( "</td>" );
							}
							else if ( currentParameter.getElementsByTagName( "type" ).item( 0 ).getFirstChild().getNodeValue().equals( "textfield" ) )
							{
								out.print( "<td>" );
								out.print( "<input type=\"textfield\" "  );
								out.print( " size=\"" + currentParameter.getElementsByTagName( "size" ).item( 0 ).getFirstChild().getNodeValue() + "\"");
								out.print( " name=\"" + currentParameter.getElementsByTagName( "fieldname" ).item( 0 ).getFirstChild().getNodeValue() + "\"" );
								out.print( " value=\"" + currentParameter.getElementsByTagName( "default" ).item( 0 ).getFirstChild().getNodeValue() + "\"" );
								out.print( ">" );
								out.println( "</td>" );
							}
							else if ( currentParameter.getElementsByTagName( "type" ).item( 0 ).getFirstChild().getNodeValue().equals( "checkbox" ) )
							{
								out.print( "<td>" );
								out.print( "<input type=\"checkbox\" "  );
								out.print( " name=\"" + currentParameter.getElementsByTagName( "fieldname" ).item( 0 ).getFirstChild().getNodeValue() + "\"" );
								out.print( " CHECKED " );
								out.print( ">" );
								out.println( "</td>" );
							}
						}

						out.println( "</tr>\n" );
					}
				}
			}
		}
        catch ( SAXParseException spe )
        {
            spe.printStackTrace();
            // Error generated by the parser
            MraldParseException parsingException = new MraldParseException( "\n** Parsing error"
                     + ", line " + spe.getLineNumber()
                     + ", uri " + spe.getSystemId()
                     + spe.getMessage() );
            // Use the contained exception, if any
//            Exception x = spe;
            if ( spe.getException() != null )
            {
//                x = spe.getException();
            }
            throw parsingException;
        }
        catch ( SAXException spe )
        {
            // Error generated by the parser
            MraldParseException parsingException = new MraldParseException( spe.getMessage() );
            throw parsingException;
        }
        catch ( DOMException de )
        {
            throw de;
        }
        catch ( IOException ioe )
        {
            throw ioe;
        }
        catch ( ParserConfigurationException pce )
        {
            throw pce;
        }
    }
}
