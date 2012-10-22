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
import org.mitre.mrald.util.Config;

/**
 *  This class redirects the user to a success url page.
 *
 *@author     Gail hamilton
 *@created    February 2, 2004
 *@version    1.0
 */

public class UpdateOutput extends HTMLOutput
{
    /**
     *  This method prepares the output file for the HTML format data
     *
     *@param  msg                         Description of the Parameter
     *@exception  IOException             Description of the Exception
     *@exception  SQLException            Description of the Exception
     *@since
     */
    public @Override void printBody( )
        throws IOException
    {
        String urlToGet = "SuccessUrl";
        String redirectURL = msg.getValue( urlToGet )[0];
        if ( redirectURL.equals( Config.EMPTY_STR ) )
        {
            redirectURL = "success.jsp";
        }
        msg.setRedirect( redirectURL );
    }


    /**
     *  This method outputs the header so that the file is recognized as an HTML
     *  file
     *
     *@param  msg                         Description of the Parameter
     *@exception  OutputManagerException  Description of the Exception
     *@since
     */
    protected @Override void prepareHeaders( )
    {
        msg.setContentType( "text/html" );
        msg.setHeader( "Content-Disposition", "inline;" );
    }

    protected @Override void getFormatInfo(  ) {}
}
