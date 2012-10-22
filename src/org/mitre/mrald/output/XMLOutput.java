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
import java.sql.SQLException;
import javax.sql.rowset.WebRowSet;
import org.mitre.mrald.util.*;
import org.mitre.mrald.util.MraldException;
import org.mitre.mrald.util.RowSetFactory;
import org.mitre.mrald.query.*;


/**
 *  This XMLOutput Class specializes the OutputManager class. It specifically
 *  gathers information pertaining to the output columns desired by the user. As
 *  the query is constructed, this class adds output information and updates the
 *  columns and joins necessary to obtain this output information
 *
 *@author     Brian Blake
 *@created    February 2, 2001
 *@version    1.0
 */

public class XMLOutput extends OutputManager
{

    protected String[] dbNames;


    /**
     *  Constructor for the XMLOutput object
     *
     *@since
     */
    public XMLOutput()
    {
        super();
    }


    /**
     *  This method prepares the file for XML formatted output
     *
     *@param  msg                 Description of the Parameter
     *@exception  MraldException  Description of the Exception
     *@since
     */
    public void printBody( )
        throws IOException, SQLException
    {
        boolean bStyled = false;
        float mbLimit = getMbLimitSize();
        try
        {
            if ( ( msg.getValue( "Format" )[0] ).equals( "XmlStyled" ) )
            {
                bStyled = true;
            }
            String[] queries = msg.getQuery();

            WebRowSet wrs;
            for ( int i = 0; i < queries.length; i++ )
            {
                try
                {
                    wrs = RowSetFactory.createWebRowSet();
                    wrs.writeXml(rs, out);
                }
                catch( MraldException e)
                {
                    out.write("<!--Error!  The WebRowSet specified in standard.properties was not found - falling back to non-standard version-->");
                    wrs = null;
                    GenerateXMLOutput xmlGenerate = new GenerateXMLOutput( out, getLineLimitSize(), mbLimit, this, printQuery );
                    xmlGenerate.GenerateDocument( queries[i], bStyled );
                }
            }
        }
        catch ( GenerateXMLException e )
        {
            throw new MraldError( e, msg );
        }
        catch ( MraldException e )
        {
            throw new MraldError( e, msg );
        }
    }


    /**
     *  This method outputs the headers for the file so that the file is read as
     *  an XML file
     *
     *@param  msg                         Description of the Parameter
     *@exception  OutputManagerException  Description of the Exception
     */
    protected void prepareHeaders()
    {
        msg.setContentType( "text/xml" );
    }


    /**
     *  Description of the Method Overwrites the place holder - to set up the DB
     *  formats
     */
    protected void setDBFormats()
    {
        dbNames = new String[niceNames.length];
        System.arraycopy( niceNames, 0, dbNames, 0, niceNames.length );

    }
}

