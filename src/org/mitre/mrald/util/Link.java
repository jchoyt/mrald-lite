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

/**
 *  Describes a join between two tables in a RDBMS.
 *
 *@author     jchoyt
 *@created    January 1, 2003
 */
public class Link
{

    String fcolumn;
    String ftable;
    String pcolumn;
    String ptable;

    private int hashCache = 0;


    /**
     *  Constructor for the Link object
     *
     *@param  ptab  Description of the Parameter
     *@param  pcol  Description of the Parameter
     *@param  ftab  Description of the Parameter
     *@param  fcol  Description of the Parameter
     */
    public Link( String ptab, String pcol, String ftab, String fcol )
    {
        ptable = ptab;
        pcolumn = pcol;
        ftable = ftab;
        fcolumn = fcol;
    }


    /**
     *  Constructor for the Link object
     *
     *@param  linkString  Description of the Parameter
     */
    public Link( String linkString )
    {
        int start = linkString.indexOf( "." );
        ptable = linkString.substring( 0, start );
        int end = linkString.indexOf( "=" );
        pcolumn = linkString.substring( start + 1, end );
        start = end + 1;
        end = linkString.indexOf( ".", start );
        ftable = linkString.substring( start, end );
        fcolumn = linkString.substring( end + 1 );
    }


    /**
     *  Sets the fcolumn attribute of the Link object
     *
     *@param  fcolumn  The new fcolumn value
     */
    public void setFcolumn( String fcolumn )
    {
        this.fcolumn = fcolumn;
    }


    /**
     *  Sets the ftable attribute of the Link object
     *
     *@param  ftable  The new ftable value
     */
    public void setFtable( String ftable )
    {
        this.ftable = ftable;
    }


    /**
     *  Sets the pcolumn attribute of the Link object
     *
     *@param  pcolumn  The new pcolumn value
     */
    public void setPcolumn( String pcolumn )
    {
        this.pcolumn = pcolumn;
    }


    /**
     *  Sets the ptable attribute of the Link object
     *
     *@param  ptable  The new ptable value
     */
    public void setPtable( String ptable )
    {
        this.ptable = ptable;
    }


    /**
     *  Gets the fcolumn attribute of the Link object
     *
     *@return    The fcolumn value
     */
    public String getFcolumn()
    {
        return fcolumn;
    }


    /**
     *  Gets the ftable attribute of the Link object
     *
     *@return    The ftable value
     */
    public String getFtable()
    {
        return ftable;
    }


    /**
     *  Gets the link attribute of the Link object
     *
     *@return    The link value
     */
    public String getLink()
    {
        return ptable + "." + pcolumn + "=" + ftable + "." + fcolumn;
    }


    /**
     *  Gets the pcolumn attribute of the Link object
     *
     *@return    The pcolumn value
     */
    public String getPcolumn()
    {
        return pcolumn;
    }


    /**
     *  Gets the ptable attribute of the Link object
     *
     *@return    The ptable value
     */
    public String getPtable()
    {
        return ptable;
    }


    /**
     *  Compares this object to another. Note hashCode is overwritten in the
     *  parent class.
     *
     *@param  o  Description of the Parameter
     *@return    Description of the Return Value
     */
    public boolean equals( Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( o instanceof Link )
        {
            return ( o.hashCode() == hashCode() );
        }
        return false;
    }


    /**
     *  Description of the Method
     *
     *@return    Description of the Return Value
     */
    public int hashCode()
    {
        if ( hashCache == 0 )
        {
            hashCache =
                    getFcolumn().hashCode() ^
                    getFtable().hashCode() ^
                    getPcolumn().hashCode() ^
                    getPtable().hashCode();
        }
        return hashCache;
    }


    /**
     *  Description of the Method
     *
     *@return    Description of the Return Value
     */
    public String toString()
    {
        StringBuffer ret = new StringBuffer();
        ret.append( ptable );
        ret.append( "." );
        ret.append( pcolumn );
        ret.append( "=" );
        ret.append( ftable );
        ret.append( "." );
        ret.append( fcolumn );
        return ret.toString();
    }
}

