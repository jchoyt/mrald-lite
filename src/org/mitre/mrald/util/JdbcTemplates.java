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
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *  Description of the Class
 *
 *@author     jchoyt
 *@created    February 12, 2006
 */
public class JdbcTemplates
{
    public final static String XML_FILENAME = "jdbcStrings.xml";
    public final static String DATASOURCE_TAG = "datasource";
    private static Map<String,DataSource> templates = new HashMap<String,DataSource>();


    public static void init( String configDirectory)
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try
        {
            builder = factory.newDocumentBuilder();
            File inputFile = new File( configDirectory, XML_FILENAME );
            Document document = builder.parse( inputFile );
            Element root = document.getDocumentElement();
            NodeList list = root.getElementsByTagName( DATASOURCE_TAG );
            for( int i = 0; i<list.getLength(); i++)
            {
                DataSource s = new DataSource( (Element)list.item( i ) );
                templates.put( s.getName(), s );
            }

        }
        catch ( Exception e )
        {
            MraldOutFile.logToFile( e );
        }
    }

    private JdbcTemplates() {
    }


    public static TreeSet<String> getTemplateList()
    {
        return new TreeSet<String>( templates.keySet() );
    }


    public static String getPort(String key)
    {
        return templates.get(key).getPort();
    }

     public static String getServerString(String key)
    {
        return templates.get(key).getServerString();
    }

    public static String getDriverClass(String key)
    {
        return templates.get(key).getDriverClass();
    }

    public static String getUrl(String key)
    {
        return templates.get(key).getUrl();
    }


    public static String getSchema(String key)
    {
        return templates.get(key).getSchema();
    }


    /**
     *  Converts to a String representation of the object.
     *
     *@return    A string representation of the object.
     */
    public static String getList()
    {
        StringBuilder ret = new StringBuilder( "Datasources configured:" );
        ret.append( Config.NEWLINE );
        for ( DataSource s : templates.values() )
        {
            ret.append( Config.NEWLINE );
            ret.append( s.toString() );
        }
        return ret.toString();
    }
}


/**
 *  Description of the Class
 *
 *@author     jchoyt
 *@created    February 12, 2006
 */
class DataSource
{
    /**
     *  Port property.
     */
    protected String port = null;

    /**
     *  Name property.
     */
    protected String name = null;

    /**
     *  Database property.
     */
    protected String database = null;

    /**
     *  DriverClass property.
     */
    protected String driverClass = null;

    /**
     *  ServerString property.
     */
    protected String serverString = null;
    protected String url = null;
    protected final String NAME = "name";
    protected final String DRIVER = "driver_class";
    protected final String SERVER = "server_string";
    protected final String PORT = "default_port";
    protected final String URL = "url";
    protected final String SCHEMA = "schema";

  /**
   * Schema property.
   */
  protected String schema = null;

  /**
   * Get schema property.
   *
   *@return Schema property.
   */
  public String getSchema() {
  	return this.schema;
  }


    /**
     *  Constructor for the DataSource object
     */
    DataSource( Element datasourceNode )
    {
        NodeList iCare = datasourceNode.getElementsByTagName( NAME );
        name = iCare.item(0).getFirstChild().getNodeValue();
        iCare = datasourceNode.getElementsByTagName( DRIVER );
        driverClass = iCare.item(0).getFirstChild().getNodeValue();
        iCare = datasourceNode.getElementsByTagName( SERVER );
        serverString = iCare.item(0).getFirstChild().getNodeValue();
        try
        {
            iCare = datasourceNode.getElementsByTagName( PORT );
            port = iCare.item(0).getFirstChild().getNodeValue();
        }
        catch(NullPointerException e)
        {
            //this one is OK to be missing
            port = Config.EMPTY_STR;
        }
        try
        {
            iCare = datasourceNode.getElementsByTagName( SCHEMA );
            schema = iCare.item(0).getFirstChild().getNodeValue();
        }
        catch(NullPointerException e)
        {
            //this one is OK to be missing
            schema = Config.EMPTY_STR;
        }
        iCare = datasourceNode.getElementsByTagName( URL );
        url = iCare.item(0).getFirstChild().getNodeValue();
    }


    /**
     *  Converts to a String representation of the object.
     *
     *@return    A string representation of the object.
     */
    public String toString()
    {
        StringBuilder ret = new StringBuilder();
        ret.append( "Name: " );
        ret.append( name );
        ret.append( Config.NEWLINE );
        ret.append( "Driver class: " );
        ret.append( driverClass );
        ret.append( Config.NEWLINE );
        ret.append( "Server string: " );
        ret.append( serverString );
        ret.append( Config.NEWLINE );
        if ( port != null )
        {
            ret.append( "Default port: " );
            ret.append( port );
            ret.append( Config.NEWLINE );
        }
        ret.append( "Drvier URL: " );
        ret.append( url );
        ret.append( Config.NEWLINE );
        return ret.toString();
    }


    /**
     *  Get port property.
     *
     *@return    Port property.
     */
    public String getPort()
    {
        return this.port;
    }


    public String getUrl()
    {
        return this.url;
    }


    /**
     *  Get name property.
     *
     *@return    Name property.
     */
    public String getName()
    {
        return this.name;
    }


    /**
     *  Get database property.
     *
     *@return    Database property.
     */
    public String getDatabase()
    {
        return this.database;
    }


    /**
     *  Get driverClass property.
     *
     *@return    DriverClass property.
     */
    public String getDriverClass()
    {
        return this.driverClass;
    }


    /**
     *  Get serverString property.
     *
     *@return    ServerString property.
     */
    public String getServerString()
    {
        return this.serverString;
    }

}

