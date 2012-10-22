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

import java.util.ArrayList;
import java.util.Properties;

/**
 *  Description of the Class
 *
 *@author     jchoyt
 *@created    January 16, 2004
 */
public class JdbcPropertyPublisher
{
    /**
     *  Description of the Field
     */
    private static ArrayList<JdbcPropertySubscriber> subscribers = new ArrayList<JdbcPropertySubscriber>();


    /**
     *  Adds a new subscriber to the list of those to be notified.
     *
     *@param  newSub  The feature to be added to the Subscriber attribute
     */
    public static void addSubscriber( JdbcPropertySubscriber newSub )
    {
        subscribers.add( newSub );
    }


    /**
     *  Removed a subscriber from the list of those to be notified.
     *
     *@param  killSub  Description of the Parameter
     */
    public static void remove( JdbcPropertySubscriber killSub )
    {
        subscribers.remove( killSub );
    }


    /**
     *  Gets an ArrayList of subscribers to be notified.
     *
     *@return    The subscribers value
     */
    public static ArrayList getSubscribers()
    {
        return subscribers;
    }


    /**
     *  Description of the Method
     */
    public static void resetSubscribers()
    {
        ArrayList oldSubscribers = subscribers;
        subscribers = new ArrayList<JdbcPropertySubscriber>();
        for ( int i = 0; i < oldSubscribers.size(); i++ )
        {
            ( ( JdbcPropertySubscriber ) oldSubscribers.get( i ) ).unsubscribed( );
        }
    }


    /**
     *  Workhorse method. Each subscriber, in turn, is permitted to add their
     *  properties to the properties object provided.
     *
     *@param  msg  Object that may contain important information needed by the
     *      subscribers
     *@param  p    The Properties object the subscribers will add to
     */
    public static void populateProperties( HttpServletRequest request, Properties p )
    {
        for ( int i = 0; i < subscribers.size(); i++ )
        {
            subscribers.get( i ).notify( request, p );
        }
    }
}

