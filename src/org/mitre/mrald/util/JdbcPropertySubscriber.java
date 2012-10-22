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

import javax.servlet.http.HttpServletRequest;

import java.util.Properties;

/**
 *  Implementations of this interface are used to add MRALD deployment or
 *  database specific properties when creating a Connection to the database.
 *
 *@author     jchoyt
 *@created    January 16, 2004
 */
public interface JdbcPropertySubscriber
{

    /*
     *  Important: The constructor for implementations of this class should add
     *  themselves to the JdbcPropertyPublisher class in this package.
     */

    /**
     *  The implementation of this method should add properties to the
     *  Properties arguement that are to be passed to the JDBC Connection object
     *  via the MraldConnection constructor used in the OutputManager class.<p>
     *
     *  Note that this method is intended to change the Properties object passed
     *  to it by the calling class.
     *
     *@param  msg   The MsgObject from the current workflow
     *@param  prop  The Properties object to be passed to the MraldConnection
     *      constructor
     */
    public void notify( HttpServletRequest request, Properties prop );


    /**
     *  This method is called when the Subscriber is forcefully unsubscribed
     *  from the Publisher it is attached to. This is primarily so the class can
     *  re-subscribe immediately. If this is not necessary, an empty
     *  implementation is recommended.
     */
    public void unsubscribed();

}

