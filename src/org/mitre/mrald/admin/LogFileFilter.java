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
package org.mitre.mrald.admin;

import java.io.File;
import java.io.FilenameFilter;


/**
 *  Accepts only files that end with .log
 *
 * @author     jchoyt
 * @created    May 8, 2006
 */
public class LogFileFilter implements FilenameFilter
{
    /**
     *  The one needed method for a FilenameFilter
     *
     * @param  dir   the directory in which the file was found
     * @param  name  the name of the file
     * @return       whether or not to include the file
     */
    public boolean accept( File dir, String name )
    {
        return name.toLowerCase().endsWith( ".log" ) ;
    }
}


