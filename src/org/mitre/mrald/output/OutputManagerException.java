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

import org.mitre.mrald.util.MraldException;

/**
 *  The OutputManagerException class handles exceptions that are unique to the
 *  OutputManager class. The OutputManagerException class is a part of the
 *  org.mitre.mrald.output package. The following code demonstrates the use of
 *  this class: <PRE>
 * OutputManagerException oOutputManagerException = new OutputManagerException("This is an error message.");
 * throw o;
 * </PRE>
 *
 *@author     Gail Hamilton
 *@created    December 4, 2002
 */

public class OutputManagerException extends MraldException
{
    /**
     *  Constructor for the OutputManagerException object
     *
     *@param  sMessage  Description of the Parameter
     */
    public OutputManagerException( String sMessage )
    {
        super( sMessage );
    }


    /**
     *  Constructor for the OutputManagerException object
     *
     *@param  e  Description of the Parameter
     */
    public OutputManagerException( Exception e )
    {
        super( e );
    }
}

