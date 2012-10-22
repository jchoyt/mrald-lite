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
package org.mitre.mrald.control;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mitre.mrald.parser.ParserElement;
import org.mitre.mrald.query.LinkElement;
import org.mitre.mrald.util.Config;
import org.mitre.mrald.util.MiscUtils;
import org.mitre.mrald.util.MraldError;
import org.mitre.mrald.util.MraldOutFile;

/**
 *  The MsgObject Class is used to hold all of the information that is passed
 *  between the various methods and workflow systems.
 *
 *@author     jchoyt
 *@created    February 2, 2001
 */
public final class MsgObject extends Object implements Serializable
{
    private String[] query;
    private String contentType;
    private ArrayList<LinkElement> links;
    private HashMap<String,String[]> nvPairs;
    private PrintWriter outPrintWriter;
    private HttpServletRequest req;
    private HttpServletResponse res;
    private String userId;
    private String userUrl;
    private ArrayList<ParserElement> workingObjects;



    /**
     *  Constructor -> Restriction: must have a post req
     *
     *@param  req       Description of Parameter
     *@param  response  Description of Parameter
     *@since
     */

    @SuppressWarnings("unchecked")
    public MsgObject( HttpServletRequest req, HttpServletResponse response )
    {
        try
        {
            nvPairs = new HashMap<String,String[]>();
            // Assume that req maps a string to an array of strings.
            nvPairs.putAll( req.getParameterMap() );
            links = new ArrayList();
            userUrl = req.getRemoteAddr();
            query = new String[0];
            workingObjects = new ArrayList<ParserElement>();
            res = response;
            this.req = req;

            Cookie[] cookies = req.getCookies();
            if ( cookies == null )
            {
                cookies = new Cookie[1];
                cookies[0] = new Cookie( "useless", "cookie" );
            }

            findcookie :
            for ( int i = 0; i < cookies.length; i++ )
            {
                String name = cookies[i].getName();
                if ( name.equalsIgnoreCase( Config.getProperty( "cookietag" ) ) )
                {
                    userId = cookies[i].getValue();
                    break findcookie;
                }
            }
            if ( userId == null )
            {
                userId = "unknown";
            }
        }
        catch ( java.lang.IllegalArgumentException iae )
        {
            throw new RuntimeException( "caught in constructor: " + iae.getMessage() );
        }
    }


	public void copyFrom( MsgObject newMsg )
	{
		// this.queryset = newMsg.getQuerySet();
		this.nvPairs = newMsg.getNVPairs();
        this.workingObjects = newMsg.getWorkingObjects();
        this.userId = newMsg.getUserId();
        this.userUrl = newMsg.getUserUrl();
	}



    /**
     *  Constructor for the MsgObject object
     *
     *@since    1.2
     */
    public MsgObject()
    {
        nvPairs = new HashMap<String,String[]>();
        links = new ArrayList<LinkElement>();
        userUrl = null;
        outPrintWriter = null;
		query = new String[0];
        workingObjects = new ArrayList<ParserElement>();
        res = null;
        req = null;
    }


    /**
     *  Set the out printer according to whether Printwriter is required (Text
     *  and HTML)
     *
     *@exception  MsgObjectException  Description of Exception
     *@since                          1.2
     */
    public void SetOutPrintWriter()
        throws MsgObjectException
    {
        try
        {
            outPrintWriter = res.getWriter();
        }
        catch ( java.io.IOException ioe )
        {
            MsgObjectException moe = new MsgObjectException( ioe.getMessage() );
            throw moe;
        }
    }


    /**
     *  Constructor for the setContentType object
     *
     *@param  type  The new ContentType value
     *@since
     */
    public void setContentType( String type )
    {
        res.setContentType( type );
        contentType = type;
    }


    /**
     *  Constructor for the setHeader object
     *
     *@param  str1  The new Header value
     *@param  str2  The new Header value
     *@since
     */
    public void setHeader( String str1, String str2 )
    {
        res.setHeader( str1, str2 );
    }


    /**
     *  Gets the Links attribute of the MsgObject object
     *
     *@param  theseLinks  The new Links value
     *@since
     */
    public void setLinks( ArrayList<LinkElement> theseLinks )
    {
        links = theseLinks;
    }


    public void setQuery( String queryVal, int sqlThread )
    {
		query[sqlThread] = queryVal;
    }


    public void setQuery( String queryVal )
    {
        String[] test;
        test = query;
        int origLength;

        if ( query == null )
        {
            //no item was there
            query = new String[1];
            query[0] = queryVal;
        }
        else
        {
            //append to the Array
            origLength = Array.getLength( query );
            query = new String[origLength + 1];
            System.arraycopy( test, 0, query, 0, origLength );
            query[query.length - 1] = queryVal;
        }
    }


    /**
     *  Constructor for the setContentType object
     *
     *@param  output                   The new Redirect value
     *@exception  java.io.IOException  Description of Exception
     *@since
     */
    public void setRedirect( String output )
        throws java.io.IOException
    {
        res.sendRedirect( output );
    }


    /**
     *  Sets the UserId attribute of the MsgObject object
     *
     *@param  userIdVal  The new UserId value
     *@since             1.2
     */
    public void setUserId( String userIdVal )
    {
        userId = userIdVal;
    }


    /**
     *  Sets the UserUrl attribute of the MsgObject object
     *
     *@param  userUrlVal  The new UserUrl value
     *@since              1.2
     */
    public void setUserUrl( String userUrlVal )
    {
        userUrl = userUrlVal;
    }


    /**
     *  * Adds a new name value pair to the internal hashmap. The form should be
     *  pre-parser, e.g., name=FilterAfterTheFact
     *  value=Table:thisTable~Field:dayOfWeek~Operator:!=~Value:Friday
     *
     *@param  name   The new Value value
     *@param  value  The new Value value
     *@since         1.2
     */
    public void setValue( String name, String value )
    {
        String[] test = nvPairs.get( name );
        String[] newValues;
        int origLength;

        if ( test == null )
        {
            //no item was there
            newValues = new String[1];

        //value = MiscUtils.checkApostrophe(value);

            newValues[0] = value;

        }
        else
        {
            //append to the Array
            origLength = Array.getLength( test );
            newValues = new String[origLength + 1];
            System.arraycopy( test, 0, newValues, 0, origLength );
            newValues[newValues.length - 1] = value;
        }

        nvPairs.put( name, newValues );
    }


    /**
     *  Sets the WorkingObjects attribute of the MsgObject object
     *
     *@param  veVal  The new WorkingObjects value
     *@since         1.2
     */
    public void setWorkingObjects( ArrayList<ParserElement> veVal )
    {
        workingObjects = veVal;
    }


    /**
     *  Gets the contentType attribute of the MsgObject object
     *
     *@return    The contentType value
     */
    public String getContentType()
    {
        return contentType;
    }


    /**
     *  Gets the Links attribute of the MsgObject object
     *
     *@return    The Links value
     *@since
     */
    public ArrayList<LinkElement> getLinks()
    {
        return links;
    }


    public HashMap<String,String[]> getNVPairs()
	{
		return nvPairs;
	}

    /**
     *  Gets the Names attribute of the MsgObject object
     *
     *@return    The Names value
     *@since     1.2
     */
    public Set<String> getNames()
    {
        return nvPairs.keySet();
    }


    /**
     *  Gets the Out attribute of the MsgObject object
     *
     *@return    The Out value
     *@since     1.2
     */
    public PrintWriter getOutPrintWriter()
    {
        return outPrintWriter;
    }


    /**
     *  Gets the outStreamWriter attribute of the MsgObject object
     *
     *@return    The outStreamWriter value
     */
    public OutputStream getOutStreamWriter()
    {
        try
        {
            return res.getOutputStream();
        }
        catch ( java.io.IOException ioe )
        {
            throw new MraldError( ioe, this );
        }
    }


    /**
     *  Gets the Props attribute of the MsgObject object
     *
     *@return    The Props value
     *@since     1.2
     */
    public String getProps()
    {
        return nvPairs.toString();
    }


    /**
     *  Gets the Query attribute of the MsgObject object
     *
     *@param  sqlThread  Description of Parameter
     *@return            The Query value
     *@since             1.2
     */
    public String getQuery( int sqlThread )
    {
        return query[sqlThread];
    }


    /**
     *  Gets the list of Queries in the MsgObject object
     *
     *@return    The Query value
     *@since     1.2
     */
    public String[] getQuery()
    {
        try
        {
            if ( query == null )
            {
                String[] temp = {org.mitre.mrald.util.Config.EMPTY_STR};
                query = temp;
            }
        }
        catch ( NullPointerException nll )
        {

            String[] temp = {org.mitre.mrald.util.Config.EMPTY_STR};
            query = temp;
        }
        return query;
    }


    /**
     *  Gets the request
     *
     *@return    The request
     */
    public HttpServletRequest getReq()
    {
        return req;
    }


    /**
     *  Gets the request
     *
     *@return    The request
     */
    public HttpServletResponse getRes()
    {
        return res;
    }


    /**
     *  Gets the Props attribute of the MsgObject object
     *
     *@return    The Props value
     *@since     1.2
     */
    public int getSize()
    {
        return nvPairs.size();
    }


    /**
     *  Gets the UserId attribute of the MsgObject object
     *
     *@return    The UserId value
     *@since     1.2
     */
    public String getUserId()
    {
        return userId;
    }


    /**
     *  Gets the UserUrl attribute of the MsgObject object
     *
     *@return    The UserUrl value
     *@since     1.2
     */
    public String getUserUrl()
    {
        return userUrl;
    }


    /**
     *  Gets the Value attribute of the MsgObject object
     *
     *@param  name  Description of Parameter
     *@return       The Value value
     *@since        1.2
     */
    public String[] getValue( String name )
    {
        String[] returnVal = null;
        try
        {
            returnVal = nvPairs.get( name );
            if ( returnVal == null )
            {
                String[] temp = {Config.EMPTY_STR};
                returnVal = temp;
            }
        }
        catch ( NullPointerException nll )
        {

            String[] temp = {Config.EMPTY_STR};
            returnVal = temp;
        }

        return returnVal;
    }


    /**
     *  Gets the WorkingObjects attribute of the MsgObject object
     *
     *@return    The WorkingObjects value
     *@since     1.2
     */
    public ArrayList<ParserElement> getWorkingObjects()
    {
        return workingObjects;
    }


    /**
     *  Adds a feature to the Link attribute of the MsgObject object
     *
     *@param  newLinkElement  The feature to be added to the Link attribute
     *@since
     */
    public void addLink( LinkElement newLinkElement )
    {
        links.add( newLinkElement );
    }


    /**
     *  This method clears the name-value pairs from the MsgObject
     *
     *@since
     */
    public void clearNvPairs()
    {
        nvPairs.clear();
    }


    /**
     *  Gets the Out attribute of the MsgObject object
     *
     *@exception  MsgObjectException  Description of Exception
     *@since                          1.2
     */
    public void closeOut()
        throws WorkflowStepException
    {
		try
        {
            if ( outPrintWriter != null )
            {
                outPrintWriter.flush();
                outPrintWriter.close();
            }
        }
        catch ( Exception e )
        {
            MraldOutFile.logToFile(  "Exception while closing out the MsgObject.  No biggie, but you might want to track this down eventually: " + Config.NEWLINE + toString());
        }
    }


    /**
     *  Description of the Method
     *
     *@return    Description of the Return Value
     */
    public String nameValuesToString()
    {
        StringBuffer ret = new StringBuffer();
        Iterator iter = nvPairs.keySet().iterator();
        while ( iter.hasNext() )
        {
            String key = ( String ) iter.next();
			if ( key.toLowerCase().indexOf( "password" ) == -1 )
            {
				String[] values = nvPairs.get( key );
				ret.append( "\n\t" );
				ret.append( key );
				ret.append( "=" );
				for ( int i = 0; i < values.length; i++ )
				{
					if ( i > 0 )
					{
						ret.append( "; " );
					}
					ret.append( values[i] );
				}
			}
        }
        return ret.toString();
    }


    /**
     *  Removes a name/value pair from nvPairs
     *
     *@param  key  The key of the pair to be removed
     *@since       1.2
     */
    public void removeValue( String key )
    {
        nvPairs.remove( key );
    }


    /**
     *  Description of the Method
     *
     *@return    Description of the Return Value
     */
    public boolean responseCommitted()
    {
        return res.isCommitted();
    }


    /**
     *  Lists, in text form, the critical information stored
     *
     *@return    Text representation of the critical contents
     */
    public String toString()
    {
        StringBuffer ret = new StringBuffer();
        ret.append( "Content Type: " + contentType );
        if ( query.length == 0 )
        {
            ret.append( "\nNo queries stored." );
        }
        else
        {
            ret.append( "\nQueries stored: " );
            for ( int i = 0; i < query.length; i++ )
            {
                ret.append( "\n\t" );
                ret.append( ret.append( query[i] ) );
            }
        }
        ret.append( "\nLinks stored:" );
        MiscUtils.appendCollectionContents( links, ret );
        ret.append( "\nWorking Objects stored:" );
        MiscUtils.appendCollectionContents( workingObjects, ret );
        ret.append( "\nName.Value pairs stored:" );
        if ( nvPairs.size() == 0 )
        {
            ret.append( "none." );
        }
        else
        {
            ret.append( nameValuesToString() );
        }
        return ret.toString();
    }
}
