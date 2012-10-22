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

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;

import org.mitre.mrald.util.GenerateXML;
import org.mitre.mrald.util.GenerateXMLException;
import org.mitre.mrald.util.MiscUtils;
import org.mitre.mrald.util.MraldException;

/**
 *  The GenerateXMLOutput class provides base level functionality to the export
 *  xml from a result set into the output stream.
 *
 *@author     Gail Hamilton
 *@created    March 7, 2001
 */
public class GenerateXMLOutput extends GenerateXML
{
    private int localLineLimit = 1000;
    private float localMbLimit = -1;
    private XMLOutput xmlOutput;

    protected final static String XML_COLUMN_TAG = "<";
    protected final static String XML_COLUMN_END_TAG = "</";
    protected final static String XML_ROWS_TAG = "<rows>";
    protected final static String XML_ROWS_END_TAG = "</rows>";
    protected final static String XML_ROW_TAG = "<row UniqueId = ";
    protected final static String XML_ROW_END_TAG = "</row>";
    protected boolean printQuery = false;
//    private final static int rowsIndent = 0;
    private final static int rowIndent = 1;
    private final static int columnIndent = 2;


    /**
     *  This constructor initializes the class by referencing an OutputStream.
     *
     *@param  oOutputStream             Description of the Parameter
     *@exception  GenerateXMLException  Description of the Exception
     *@since
     */
    public GenerateXMLOutput( Writer oOutputStream )
        throws GenerateXMLException
    {
        super( oOutputStream );
    }


    /**
     *  This constructor initializes the class by referencing an OutputStream.
     *
     *@param  oOutputStream             Description of the Parameter
     *@param  lineLimit                 Description of the Parameter
     *@param  mbLimit                   Description of the Parameter
     *@param  passedXmlOutput           Description of the Parameter
     *@param  printQuery                Description of the Parameter
     *@exception  GenerateXMLException  Description of the Exception
     *@since
     */
    public GenerateXMLOutput( Writer oOutputStream, int lineLimit, float mbLimit, XMLOutput passedXmlOutput, boolean printQuery )
        throws GenerateXMLException
    {
        super( oOutputStream );
        this.printQuery = printQuery;
        localLineLimit = lineLimit;
        localMbLimit = mbLimit;
        xmlOutput = passedXmlOutput;
        //establish the buffered data output stream
    }


    /**
     *  This method gets the URI from the Config file
     *
     *@return                          Description of the Return Value
     *@exception  java.io.IOException  Description of the Exception
     */
    public String GetDBName()
        throws java.io.IOException
    {
        //get the name of the database from the config file
        return null;
    }



    /**
     *  This method retrieves the data needed for exporting.
     *
     *@exception  MraldException  Description of the Exception
     *@since
     */
    public void RetrieveData()
        throws MraldException
    {

        try
        {
            /*
             *  write the query?
             */
            if ( printQuery )
            {
                Write( "<query>" );
                Write( MiscUtils.parseTextToXml( query ) );
                Write( "</query>" );
            }

            java.sql.ResultSet rs = xmlOutput.getQryResultsinRS();

            int iColCount = xmlOutput.dbNames.length;
            int iRow = 0;
            int fileSize = 0;
            String formattedString;

            while ( ( rs.next() ) &&
                    ( ( localLineLimit == -1 ) || ( iRow <= localLineLimit ) ) &&
                    ( ( localMbLimit == -1 ) || ( fileSize < localMbLimit ) ) )
            {
                iRow++;
                Indent( rowIndent );

                Write( XML_ROW_TAG + "'" + iRow + "'>" );
                NewLine();

                for ( int i = 0; i < iColCount; i++ )
                {
                    // System.out.println(rs.getString( i + 1 ));
                    if ( xmlOutput.classNames[i].equals( "Timestamp" ) )
                    {
                        formattedString = xmlOutput.getAndFormat( rs.getTimestamp( i + 1 ), xmlOutput.formats[i] );
                    }
                    else if ( xmlOutput.classNames[i].equals( "BigDecimal" ) )
                    {
                        formattedString = xmlOutput.getAndFormat( rs.getBigDecimal( i + 1 ), xmlOutput.formats[i] );
                    }
                    else
                    {
                        formattedString = rs.getString( i + 1 );
                        /*
                         *  if ( formattedString == null )
                         *  {
                         *  formattedString = "null";
                         *  }
                         */
                    }
                    ExportBody( columnIndent, xmlOutput.dbNames[i], formattedString );

                    try
                    {
                        fileSize += formattedString.length() +
                                xmlOutput.dbNames[i].length() + 85;//85 for tags, brackets, and newlines
                    }
                    catch ( NullPointerException npe )
                    {
                        fileSize += 85;
                    }
                }

                Indent( rowIndent );

                Write( XML_ROW_END_TAG );
                NewLine();
            }
        }
        catch ( IOException e )
        {
            throw new MraldException( e );
        }
        catch ( SQLException e )
        {
            throw new MraldException( e );
        }

    }


    /**
     *  This method exports the XML header information such as the preprocessor
     *  line.
     *
     *@param  bStylesheet         Description of the Parameter
     *@exception  MraldException  Description of the Exception
     *@since
     */
    public void ExportHeader( boolean bStylesheet )
        throws MraldException
    {
        try
        {//export the preprocessor line
            Write( XML_OUT_PREPROCESSOR_LINE );
            NewLine();
            if ( bStylesheet )
            {
                Write( XML_STYLESHEET );
                NewLine();
            }
            /*
             *  determine if the user desires to export with an xml stylesheet
             */
            if ( GetStyleSheet() != null )
            {
                Write( "<?xml-stylesheet type=\"text/xsl\" href=\"" +
                        GetStyleSheet() + "\"?>" );
                NewLine();
            }

            /*
             *  determine if the user desires to export a DTD definition language statement
             */
            if ( IsDefinitionLanguageDTD() )
            {
                Write( "<!DOCTYPE " + GetRootElementName() + " SYSTEM \"" +
                        GetUri() + "\">" );
                NewLine();
            }

            /*
             *  if we are exporting a biztalk instance, export the biztalk header
             *  /determine if the user desires to export a DTD definition language statement
             */
            if ( IsDefinitionLanguageBiz() )
            {
                Write( XML_HEADER );
                NewLine();
            }
            WriteRootElementName();

            Flush();
        }
        catch ( IOException e )
        {
            throw new MraldException( e );
        }
    }


    /**
     *  This method exports the XML body.
     *
     *@param  iIndentLevel        Description of the Parameter
     *@param  sTag                Description of the Parameter
     *@param  sValue              Description of the Parameter
     *@exception  MraldException  Description of the Exception
     *@since
     */
    public void ExportBody( int iIndentLevel, String sTag, String sValue )
        throws MraldException
    {
        if ( sValue == null )
        {
            sValue = "null";
        }//String tagLabel = sTag.replace(' ', '_');
        try
        {
            Indent( iIndentLevel );

            Write( XML_COLUMN_TAG + sTag + ">" );
            NewLine();
            Indent( iIndentLevel + 1 );
            //Write( XML_CDATA_SECTION_BEGIN + sValue + XML_CDATA_SECTION_END );
            Write( sValue );
            NewLine();
            Indent( iIndentLevel );
            Write( XML_COLUMN_END_TAG + sTag + ">" );
            NewLine();
        }
        catch ( IOException e )
        {
            throw new MraldException( e );
        }
    }



    /**
     *  This method writes the name of the root xml element.
     *
     *@exception  java.io.IOException  Description of the Exception
     */
    protected void WriteRootElementName()
        throws java.io.IOException
    {
        Write( XML_ROWS_TAG );
        NewLine();
    }


    /**
     *  This method writes the name of the root xml element.
     *
     *@exception  java.io.IOException  Description of the Exception
     */
    protected void WriteEndRootElementName()
        throws java.io.IOException
    {
        Write( XML_ROWS_END_TAG );
        NewLine();
    }

}

