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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 *  Global location for configurations. Also contains some useful constants.
 *
 *@author     jchoyt
 *@created    January 2, 2001
 */
public class Config
{

    /**
     *  Description of the Field
     */
    public static final String EMPTY_STR = "";

    /**
     *  The new line symbol(s) for this OS - either \r, \n, or \r\n
     */
    public static final String NEWLINE = System.getProperty( "line.separator" );

    /**
     *  The file separator for this OS - either / or \
     *  Always use / as the file separator because:
     *  1) Postgres interprets \ as an escape character.
     *  2) Windows can handle / or \ as a file separator.
     */
    public final static String FILE_SEPARATOR = "/"; // System.getProperty( "file.separator" );

    /**
     *  The properties that are likely to be modified
     */
    public static final String PROPS_FILE = "config.properties";

    /**
     *  The properties that are not likely to be modified
     */
    public static final String STANDARD_PROPS_FILE = "standard.properties";
    /**
     *  Description of the Field
     */
    protected static String finalPropertiesLocation;

    /**
     *  This is the global boolean value that determines whether or not the
     *  system is currently using the Password/Security.<br>
     *  <br>
     *  True = Passwords are IN EFFECT <br>
     *  False = No Passwords needed for the system
     */
    public static final boolean usingSecurity = true;

    /**
     *  This is the global boolean value that determines whether or not the
     *  system is currently using the Email Validation system. True = Validation
     *  IS GOING and EMAIL ARE SENT False = No Validation is in the system
     */
    public final static boolean usingValidation = false && usingSecurity;

    /**
     *  Description of the Field
     */
    private static final Properties config = new Properties();
    private static PropertyChangeSupport pcs = new PropertyChangeSupport( config );

    /**
     *  Sets the finalPropertiesLocation attribute of the Config class
     *
     *@param  _finalPropertiesLocation  The new finalPropertiesLocation value
     */
    public static void setFinalPropertiesLocation( String _finalPropertiesLocation )
    {
        finalPropertiesLocation = _finalPropertiesLocation;
    }


    /**
     *  Sets the property attribute of the Config class
     *
     *@param  key    The new property value
     *@param  value  The new property value
     */
    public static void setProperty( String key, String value )
    {
        config.setProperty( key, value );
    }


    /**
     *  Gets the finalPropertiesLocation attribute of the Config class
     *
     *@return    The finalPropertiesLocation value
     */
    public static String getFinalPropertiesLocation()
    {
        return finalPropertiesLocation;
    }


    /**
     *  Gets the Property attribute of the Config class. If the internal
     *  Properties object is null, it looks to initialize a new one from a file
     *  in WEB-INF (assuems tomcat installation - 6 levels up from the location
     *  of the Config.class file) named <code>config.properties</code>. If the
     *  key isn't found, the default value is returned.
     *
     *@param  key           Description of the Parameter
     *@param  defaultValue  Description of the Parameter
     *@return               The property value
     */
    public static String getProperty( String key, String defaultValue )
    {
        if ( finalPropertiesLocation == null )
        {
            init();
        }
        String temp = config.getProperty( key, defaultValue );
        String base_path = config.getProperty( "BasePath" );
        String base_url = config.getProperty( "BaseUrl" );
        temp = MiscUtils.replace( temp, "\\[PATH\\]", base_path );
        temp = MiscUtils.replace( temp, "\\[BASE_URL\\]", base_url );
        if ( temp.endsWith( ".xml" ) )
        {
            temp = "file://" + MiscUtils.replace( temp, "^\\w:", "" );//gets rid of Windows drive letter
        }
        return temp;
    }


    /**
     *  Gets the Property attribute of the Config class. If the internal
     *  Properties object is null, it looks to initialize a new one from a file
     *  in WEB-INF (assuems tomcat installation - 6 levels up from the location
     *  of the Config.class file) named <code>config.properties</code> .
     *
     *@param  key  Description of Parameter
     *@return      The Property value
     *@since
     */
    public static String getProperty( String key )
    {
        if ( finalPropertiesLocation == null )
        {
            init();
        }
        String temp = config.getProperty( key );
        if ( temp == null )
        {
            throw new NullPointerException( "Property " + key + " was not found in config." );
        }
        return replacements( temp );
    }



    /**
     *  Description of the Method
     *
     *@param  in  Description of the Parameter
     *@return     Description of the Return Value
     */
    public static String replacements( String in )
    {
        String base_path = config.getProperty( "BasePath" );
        String base_url = config.getProperty( "BaseUrl" );
        in = MiscUtils.replace( in, "[PATH]", base_path );
        in = MiscUtils.replace( in, "[BASE_URL]", base_url );
        if ( in.endsWith( ".xml" ) )
        {
            int colon = in.indexOf( ':' );
            if ( colon != -1 )
            {
                in = in.substring( colon + 1 );
            }
            in = "file://" + in;//, "^\\w:", "" );//gets rid of Windows drive letter
        }
        return in;
    }


    /**
     *  Adds class listening for property changes on Config. A property change
     *  event will be fired whenever the properties are loaded.
     *
     *@param  listener  The feature to be added to the PropertyChangeListener
     *      attribute
     */
    public static void addPropertyChangeListener( PropertyChangeListener listener )
    {
        pcs.addPropertyChangeListener( listener );
    }


    /**
     *  Fires a property change event.
     */
    public static void fireChangeEvent()
    {
        pcs.firePropertyChange( "reloaded props", 0, 1 );

    }


    /**
     *  Description of the Method
     *
     *@param  propertiesFileLocation  Description of Parameter
     *@since
     */
    public static void init( String propertiesFileLocation )
    {

        finalPropertiesLocation = propertiesFileLocation;

        Config.init();
    }


    /**
     *  Itinializes the properties in Config. If finalPropertiesLocation is
     *  null, it tries to find a location by looking in the System properties
     *  for a testProps property. By default, Config is populated by two files -
     *  config.properties and standard.properties. If it pulls the location from
     *  System properties, it pulls only from the file specified by the
     *  property.
     */
    public static void init()
    {
        if ( finalPropertiesLocation == null )
        {
            //finalPropertiesLocation = "/devel/gail/digilib/tomcat/webapps/ROOT/WEB-INF/props/config.properties";
            finalPropertiesLocation = System.getProperty( "testProps" );
            if ( finalPropertiesLocation == null )
            {
                throw new NullPointerException( "Can't initialize config.  Can't find a config.properties file.  Try specifying one explicitly by using init(String) or setting it using the setFinalPropertiesLocation(String) method" );
            }
            try
            {
                InputStream in = new FileInputStream( finalPropertiesLocation );
                config.load( in );
                in.close();
                fireChangeEvent();
                updateProxy();
                return;
            }
            catch ( java.io.FileNotFoundException e )
            {
                throw new org.mitre.mrald.util.MraldError( "Could find the partition configuration file at " + finalPropertiesLocation, e );
            }
            catch ( java.io.IOException e )
            {
                throw new org.mitre.mrald.util.MraldError( "IO Error reading from " + finalPropertiesLocation, e );
            }
        }
        String toRead = "";
        try
        {
            toRead = finalPropertiesLocation + PROPS_FILE;
            InputStream in = new FileInputStream( toRead );
            config.load( in );
            in.close();
            try
            {
                toRead = finalPropertiesLocation + STANDARD_PROPS_FILE;
                in = new FileInputStream( toRead );
                config.load( in );
                in.close();
            }
            catch ( java.io.FileNotFoundException e )
            {
                //no problem - this is for backward compatibility
            }
            fireChangeEvent();
            updateProxy();
        }
        catch ( java.io.FileNotFoundException e )
        {
            throw new org.mitre.mrald.util.MraldError( "Could find the configuration file at " + toRead, e );
        }
        catch ( java.io.IOException e )
        {
            throw new org.mitre.mrald.util.MraldError( "IO Error reading from " + toRead, e );
        }
    }


    /**
     *  Description of the Method
     */
    public static void updateProxy()
    {
        try
        {
            if ( config.getProperty( "ProxyHost" ) != null && !config.getProperty( "ProxyHost" ).equals( "" ) )
            {
                System.getProperties().put( "http.proxyHost", config.getProperty( "ProxyHost" ) );
                System.getProperties().put( "http.proxyPort", config.getProperty( "ProxyPort" ) );
    //			MraldOutFile.logToFile( Config.getProperty( "LOGFILE" ), "Config :: Setting the proxy to : " + System.getProperty( "http.proxyHost" ) );
            }
            else
            {
                System.getProperties().remove( "http.proxyHost" );
                System.getProperties().remove( "http.proxyPort" );
    //			MraldOutFile.logToFile( Config.getProperty( "LOGFILE" ), "Config :: Removing the proxy" );
            }
        }
        catch (Exception e)
        {
            MraldOutFile.logToFile(
                new MraldException( "Likely a security exception - you must allow modification to system properties if you want to use the proxy",
                e ) );
        }
    }


    /**
     *  Description of the Method
     *
     *@param  dummy  Description of Parameter
     *@return        Description of the Returned Value
     *@since
     */
    public static String toString( boolean dummy )
    {
        return config.toString().replace( ',', '\n' );
    }
}

