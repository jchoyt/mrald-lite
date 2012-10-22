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
package org.mitre.mrald.servlets;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mitre.mrald.control.MsgObject;
import org.mitre.mrald.control.WfController;
import org.mitre.mrald.util.Config;
import org.mitre.mrald.util.MiscUtils;
import org.mitre.mrald.util.MraldError;
import org.mitre.mrald.util.ParseXML;
import org.mitre.mrald.util.Snoop;

/**
 *  This PqmServlet Class is the main entry point to the Output processors.
 *  Staged forms access the doPost method in this class as the entry point to
 *  the automated query processing functionality
 *
 *@author     Jeffrey Hoyt
 *@created    January 18, 2001
 *@version    1.0
 *@see        javax.servlet.http
 */

public class PqmServlet extends HttpServlet
{

    /**
     *  Constructor for the PqmServlet object
     *
     *@since
     */
    public PqmServlet()
    {
        super();
    }


    /**
     *  This function overrides the HttpServlets doGet functionality
     *
     *@param  req                   Description of Parameter
     *@param  res                   Description of Parameter
     *@exception  ServletException  Description of Exception
     *@exception  IOException       Description of Exception
     *@since
     */
    public void doGet( HttpServletRequest req, HttpServletResponse res )
        throws ServletException, IOException
    {
        doPost( req, res );
    }


    /**
     *  This function overrides the HttpServlets doPost functionality
     *
     *@param  req                   User request information object
     *@param  res                   Object for returning the response
     *@exception  ServletException  Description of Exception
     *@exception  IOException       Description of Exception
     *@since
     */
    public void doPost( HttpServletRequest req, HttpServletResponse res )
        throws ServletException, IOException
    {
        MsgObject msg = new MsgObject( req, res );
		// msg.setMsgObjectName( System.currentTimeMillis() + "" );

        // Snoop.logUserInfo( msg );
         Snoop.logParameters( msg );

        try
        {
            ParseXML mraldParser = new ParseXML( Config.getProperty( "XMLFILE" ) );
            String workflow = msg.getValue( "workflow" )[0];
            if ( workflow == null )
            {
                workflow = "Building SQL";
            }
            //Log.always(this, workflow);
            MiscUtils.logWorkFlow( msg.getUserUrl(), workflow );
            mraldParser.setWfPath( workflow );
            HashMap list = mraldParser.ProcessXML();
            WfController controller = new WfController( msg );
            controller.setWfObjects( list );
            controller.processWorkFlow();
            //msg.closeOut();
        }
        catch ( Exception e )
        {
            throw new MraldError( e, msg );
        }
    }
}

