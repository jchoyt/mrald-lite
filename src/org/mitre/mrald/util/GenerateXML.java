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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.mitre.mrald.control.AbstractStep;
import org.mitre.mrald.control.MsgObject;
import org.mitre.mrald.control.WorkflowStepException;

/**
 *  The ExportXML class provides base level functionality to the export xml from
 *  a result set into a default XML Schema.
 *
 *@author     Gail Hamilton
 *@created    March 7, 2001
 */
public abstract class GenerateXML extends AbstractStep
{//implements IGenerateXML
    private boolean bStyleSheet = false;
    private Writer dataOutputStream;
//constant for the preprocessor line
    protected final static String XML_OUT_PREPROCESSOR_LINE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
//constant for the new line char(s)
    protected final static String XML_OUT_NEWLINE = System.getProperty( "line.separator" );
//constants for the biztalk header and footer
    protected final static String XML_HEADER = "<Body>";
//    protected final static String XML_STYLESHEET = "<?xml-stylesheet type=\"text/xsl\" href=\" " + Config.getProperty("XSLFILE") + "\"?>";
    protected final static String XML_STYLESHEET = "";
    protected final static String XML_FOOTER = "</Body>";
//the cdata section begin text
    protected final static String XML_CDATA_SECTION_BEGIN = "<![CDATA[";
//the cdata section end text
    protected final static String XML_CDATA_SECTION_END = "]]>";
    protected final static String XML_START_TAG = "<";
    protected final static String XML_END_TAG = "</";
    protected static String query = "";


    /**
     *  Empty constructor
     *
     *@exception  GenerateXMLException  Description of Exception
     *@since
     */
    public GenerateXML()
        throws GenerateXMLException { }


    /**
     *  This constructor initializes the class by referencing an OutputStream.
     *
     *@param  oOutputStream             Description of Parameter
     *@exception  GenerateXMLException  Description of Exception
     *@since
     */
    public GenerateXML( Writer oOutputStream )
        throws GenerateXMLException
    {
//establish the buffered data output stream
        dataOutputStream = oOutputStream;

    }



    /**
     *  Description of the Method
     *
     *@param  msgObject                  Description of Parameter
     *@exception  WorkflowStepException  Description of the Exception
     *@since
     */
    public void execute( MsgObject msgObject )
        throws WorkflowStepException
    {

        try
        {
            GenerateDocument();
        }
        catch ( MraldException e )
        {
            throw new MraldError( e, msgObject );
        }
    }


    /**
     *  This method gets the URI from the Config file
     *
     *@return                          Description of the Returned Value
     *@exception  java.io.IOException  Description of Exception
     *@since
     */
    public String GetDBName()
        throws java.io.IOException
    {
        /*
         *  get the name of the database from the config file
         */
        return null;
    }


    /**
     *  This method gets the URI from the Config file
     *
     *@return                          Description of the Returned Value
     *@exception  java.io.IOException  Description of Exception
     *@since
     */
    public String GetUri()
        throws java.io.IOException
    {
        /*
         *  get the URI from the config file
         */
        return "temp";
    }


    /**
     *  This method gets the Stylesheet from the config file
     *
     *@return                          Description of the Returned Value
     *@exception  java.io.IOException  Description of Exception
     *@since
     */
    public String GetStyleSheet()
        throws java.io.IOException
    {
        /*
         *  get the URI from the config file
         */
        return null;
    }


    /**
     *  This method retrieves the data needed for exporting.
     *
     *@param  sSQLStatement       Description of Parameter
     *@param  bStylesheet         Description of Parameter
     *@exception  MraldException  Description of the Exception
     *@since
     */
    public void GenerateDocument( String sSQLStatement, boolean bStylesheet )
        throws MraldException
    {
        try
        {
            ExportHeader( bStylesheet );
            RetrieveData();
            ExportFooter();
            //Close();
        }
        catch ( IOException e )
        {
            throw new MraldException( e );
        }
    }


    /**
     *  This method retrieves the data needed for exporting.
     *
     *@exception  MraldException  Description of the Exception
     *@since
     */
    public void GenerateDocument()
        throws MraldException
    {
        ExportHeader( bStyleSheet );
        RetrieveData();

        try
        {
            ExportFooter();
            //Close();
        }
        catch ( IOException e )
        {
            throw new MraldException( e );
        }
    }


    /**
     *  This method exports the XML footer information.
     *
     *@exception  java.io.IOException  Description of Exception
     *@since
     */
    public void ExportFooter()
        throws java.io.IOException
    {
        WriteEndRootElementName();
//if we are exporting a biztalk instance, export the biztalk footer
        if ( IsDefinitionLanguageBiz() )
        {
            Write( XML_FOOTER );
            NewLine();
        }

        Flush();
    }


    /**
     *  This method exports the XML header information such as the preprocessor
     *  line.
     *
     *@param  bStylesheet         Description of Parameter
     *@exception  MraldException  Description of the Exception
     *@since
     */
    public void ExportHeader( boolean bStylesheet )
        throws MraldException
    {
        try
        {
            //export the preprocessor line
            Write( XML_OUT_PREPROCESSOR_LINE );
            NewLine();

            if ( bStylesheet )
            {
                Write( XML_STYLESHEET );
                NewLine();
            }

            //determine if the user desires to export with an xml stylesheet
            if ( GetStyleSheet() != null )
            {
                Write( "<?xml-stylesheet type=\"text/xsl\" href=\"" +
                        GetStyleSheet() + "\"?>" );
                NewLine();
            }

            //determine if the user desires to export a DTD definition language statement
            if ( IsDefinitionLanguageDTD() )
            {
                Write( "<!DOCTYPE " + GetRootElementName() + " SYSTEM \"" +
                        GetUri() + "\">" );
                NewLine();
            }

            //if we are exporting a biztalk instance, export the biztalk header
            //determine if the user desires to export a DTD definition language statement
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
     *  This method gets the Stylesheet from the config file
     *
     *@return                          Description of the Returned Value
     *@exception  java.io.IOException  Description of Exception
     *@since
     */
    public boolean IsDefinitionLanguageBiz()
        throws java.io.IOException
    {
//get whether this is to be a BizTalk Schema from the config file
        return false;
    }


    /**
     *  This method gets the Stylesheet from the config file
     *
     *@return                          Description of the Returned Value
     *@exception  java.io.IOException  Description of Exception
     *@since
     */
    public boolean IsDefinitionLanguageDTD()
        throws java.io.IOException
    {
//get whether this is to be a DTD from the config file
        return false;
    }


    /**
     *  This method returns the current date and time in ISO 8601 format
     *  (2088-04-07T18:39:09-08:00)
     *
     *@return    Description of the Returned Value
     *@since
     */
    protected String GetCurrentDatetime()
    {
        String sDatetime;

        try
        {
            int iMillisecondsPerMinute = ( 60 * 1000 );
            int iMillisecondsPerYear = ( 60 * iMillisecondsPerMinute );

//get the current date and time
            Date oDate = new Date();
//setup date and time formatting; the formatter returns a
//text representation of the time zone, so we can't use it
            SimpleDateFormat oSimpleDateFormat = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss" );
            sDatetime = oSimpleDateFormat.format( oDate );

//determine the time zone in hours and minutes from GMT
            Calendar oCalendar = oSimpleDateFormat.getCalendar();
            int iMillisecondsFromGMT = oCalendar.get( Calendar.ZONE_OFFSET ) + oCalendar.get( Calendar.DST_OFFSET );
            String sHoursFromGMT = TwoDigitString( iMillisecondsFromGMT / iMillisecondsPerYear );
            String sMinutesFromGMT = TwoDigitString( Math.abs( iMillisecondsFromGMT % iMillisecondsPerYear ) / iMillisecondsPerMinute );
            sDatetime = sDatetime + sHoursFromGMT + ":" + sMinutesFromGMT;
        }
        catch ( Exception e )
        {
            sDatetime = "unknown";
        }

        return sDatetime;
    }


    /**
     *  This method returns the name of the root xml element.
     *
     *@return    Description of the Returned Value
     *@since
     */
    protected String GetRootElementName()
    {
        return null;
    }


    /**
     *  This method exports the string sXML to the buffered OutputStream.
     *
     *@param  sXML                     Description of Parameter
     *@exception  java.io.IOException  Description of Exception
     *@since
     */
    protected void Write( String sXML )
        throws java.io.IOException
    {
        dataOutputStream.write( sXML );
    }


    /**
     *  This method exports the string sXML to the buffered OutputStream.
     *
     *@param  sXML                     Description of Parameter
     *@param  origXML                  Description of the Parameter
     *@return                          Description of the Return Value
     *@exception  java.io.IOException  Description of Exception
     *@since
     */
    protected String Write( String origXML, String sXML )
        throws java.io.IOException
    {
        return origXML + sXML;
    }


    /**
     *  This method exports the the new line char(s) to the buffered
     *  OutputStream.
     *
     *@exception  java.io.IOException  Description of Exception
     *@since
     */
    protected void NewLine()
        throws java.io.IOException
    {
        dataOutputStream.write( XML_OUT_NEWLINE );
//dataOutputStream.write(XML_OUT_NEWLINE);
    }


    /**
     *  This method exports the correct number of 'space' characters needed to
     *  indent to the level iLevel.
     *
     *@param  iLevel                   Description of Parameter
     *@exception  java.io.IOException  Description of Exception
     *@since
     */
    protected void Indent( int iLevel )
        throws java.io.IOException
    {
        for ( int i = 0; i < iLevel; i++ )
        {
            Write( "  " );
        }
    }


    /**
     *  This method flushes the buffered OutputStream.
     *
     *@exception  java.io.IOException  Description of Exception
     *@since
     */
    protected void Flush()
        throws java.io.IOException
    {
        dataOutputStream.flush();
    }


    /**
     *  This method exports the string sComment as an xml comment.
     *
     *@param  sComment                 Description of Parameter
     *@exception  java.io.IOException  Description of Exception
     *@since
     */
    protected void Comment( String sComment )
        throws java.io.IOException
    {
//export a comment
        Write( "<!-- " + sComment + " -->" );
    }


    /**
     *  This method returns the string sValue with all spaces removed.
     *
     *@param  sValue  Description of Parameter
     *@return         Description of the Returned Value
     *@since
     */
    protected String RemoveSpaces( String sValue )
    {
        StringBuffer sbNoSpaces = new StringBuffer();

        for ( int i = 0; i < sValue.length(); i++ )
        {
            char c = sValue.charAt( i );
//append all non space chars
            if ( c != ' ' )
            {
                sbNoSpaces.append( c );
            }
        }

        return sbNoSpaces.toString();
    }


    /**
     *  This method closes the Writer
     *
     *@exception  java.io.IOException  Description of Exception
     *@since
     */
/*     protected void Close()
        throws java.io.IOException
    {
        dataOutputStream.close();
    }
 */

    /**
     *  This method returns a two digit or more string that represents the value
     *  iValue.
     *
     *@param  oOutputStream             The new dataOutput value
     *@exception  GenerateXMLException  Description of the Exception
     *@since
     */
    public void setDataOutput( Writer oOutputStream )
        throws GenerateXMLException
    {
//establish the buffered data output stream
        dataOutputStream = oOutputStream;
    }


    /*
     *  Provide a way to access the output stream
     *
     *  @param  iValue  Description of Parameter
     *  @return         Description of the Returned Value
     *  @since
     */
    /**
     *  Gets the dataOutput attribute of the GenerateXML object
     *
     *@return                           The dataOutput value
     *@exception  GenerateXMLException  Description of the Exception
     */
    public Writer getDataOutput()
        throws GenerateXMLException
    {
//establish the buffered data output stream
        return dataOutputStream;
    }


    /**
     *  This method returns a two digit or more string that represents the value
     *  iValue.
     *
     *@param  iValue  Description of Parameter
     *@return         Description of the Returned Value
     *@since
     */
    private String TwoDigitString( int iValue )
    {
        String sTwoDigit;

        if ( iValue == 0 )
        {
            sTwoDigit = "00";
        }
        else if ( iValue < 10 && iValue > 0 )
        {
            sTwoDigit = "0" + iValue;
        }
        else if ( iValue > -10 && iValue < 0 )
        {
            sTwoDigit = "-0" + Math.abs( iValue );
        }
        else
        {
            sTwoDigit = "" + iValue;
        }

        return sTwoDigit;
    }


    /**
     *  This method retrieves the data needed for exporting.
     *
     *@exception  MraldException  Description of the Exception
     *@since
     */
    public abstract void RetrieveData()
        throws MraldException;


    /**
     *  This method writes the name of the root xml element.
     *
     *@exception  java.io.IOException  Description of Exception
     *@since
     */
    protected abstract void WriteRootElementName()
        throws java.io.IOException;


    /**
     *  This method writes the name of the root xml element.
     *
     *@exception  java.io.IOException  Description of Exception
     *@since
     */
    protected abstract void WriteEndRootElementName()
        throws java.io.IOException;


    /**
     *  This method exports the XML body.
     *
     *@param  iIndentLevel        Description of Parameter
     *@param  sTag                Description of Parameter
     *@param  sValue              Description of Parameter
     *@exception  MraldException  Description of the Exception
     *@since
     */
    public abstract void ExportBody( int iIndentLevel, String sTag, String sValue )
        throws MraldException;

}

