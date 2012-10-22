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

import java.io.File;
import java.io.FilenameFilter;

/**
 *  Description of the Class
 *
 *@author     jchoyt
 *@created    October 18, 2005
 */
public class DbPropsFilter implements FilenameFilter
{
    /**
     *  Description of the Method
     *
     *@param  dir   Description of the Parameter
     *@param  name  Description of the Parameter
     *@return       Description of the Return Value
     */
    public boolean accept( File dir, String name )
    {
        boolean ret = name.endsWith( ".props" ) || name.endsWith( ".properties" );
        ret = ret && name.startsWith( "db_" );
        return ret;
    }
}


