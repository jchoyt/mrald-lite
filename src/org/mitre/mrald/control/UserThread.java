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

//import org.mitre.mrald.MraldOutFile;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.mitre.mrald.util.MraldOutFile;

/**
 *  Description of the Class
 *
 *@author     jchoyt
 *@created    April 10, 2002
 */
public class UserThread extends java.lang.Object
{
    //implements Serializable

    /**
     *  Description of the Field
     */
    protected String user;
    /**
     *  Description of the Field
     */
    protected long started;
    /**
     *  Description of the Field
     */
    protected Statement stmt;
    /**
     *  Description of the Field
     */
    protected String query;
    /**
     *  Description of the Field
     */
    protected Thread thread;


    /**
     *  Constructor for the UserThread object
     */
    public UserThread() { }


    /**
     *  Constructor for the UserThread object
     *
     *@param  passed_user       Description of Parameter
     *@param  passed_query      Description of Parameter
     *@param  passed_timestamp  Description of Parameter
     *@param  passed_stmt       Description of Parameter
     *@param  passed_thread     Description of Parameter
     */
    public UserThread(String passed_user, String passed_query, long passed_timestamp, Statement passed_stmt, Thread passed_thread)
    {
        user = passed_user;
        query = passed_query;
        started = passed_timestamp;
        stmt = passed_stmt;
        thread = passed_thread;
    }


    /**
     *  Gets the user attribute of the UserThread object
     *
     *@return    The user value
     */
    public String getUser()
    {
        return user;
    }


    /**
     *  Gets the thread attribute of the UserThread object
     *
     *@return    The thread value
     */
    public Thread getThread()
    {
        return thread;
    }


    /**
     *  Gets the started attribute of the UserThread object
     *
     *@return    The started value
     */
    public long getStarted()
    {
        return started;
    }


    /**
     *  Gets the stmt attribute of the UserThread object
     *
     *@return    The stmt value
     */
    public Statement getStmt()
    {
        return stmt;
    }


    /**
     *  Gets the query attribute of the UserThread object
     *
     *@return    The query value
     */
    public String getQuery()
    {
        return query;
    }


    /**
     *  Description of the Method
     *
     *@return    Description of the Returned Value
     */
    public String toString()
    {
        StringBuffer ret = new StringBuffer();
        ret.append("User: ");
        ret.append(user);
        ret.append("\nStarted: ");
        ret.append(started);
        ret.append("\nQuery: ");
        ret.append(query);
        ret.append("\nstmt: ");
        ret.append(stmt);
        return ret.toString();
    }


    /**
     *  Description of the Method
     *
     *@return    Description of the Returned Value
     */
    public String toOneLineString()
    {
        StringBuffer ret = new StringBuffer();
        ret.append("User: ");
        ret.append(user);
        ret.append("; Started: ");
        ret.append(started);
        ret.append("; Query: ");
        ret.append(query);
        ret.append("; stmt: ");
        ret.append(stmt);
        return ret.toString();
    }


    /**
     *  Description of the Method
     *
     *@return    Description of the Returned Value
     */
    public String toHtmlTableRow()
    {
        //System.out.println(stmt.toString());
        StringBuffer ret = new StringBuffer();
        ret.append("<td>");
        ret.append(user);
        ret.append("</td><td>");
        SimpleDateFormat dFormat = new SimpleDateFormat("d MMM yy, HH:mm:ss");
        ret.append(dFormat.format(new Date(started)));
        ret.append("</td><td>");
        DecimalFormat format = new DecimalFormat("0.0");
        ret.append(format.format((System.currentTimeMillis() - started) / 1000 / 60.0));
        //elapsed time in minutes
        ret.append("</td><td>");
        ret.append(query);
        ret.append("</td><td>");
        ret.append("<a href=\"KillThread.jsp?start=");
        ret.append(started);
        ret.append("\">Kill</a>");
        ret.append("</td>");
        return ret.toString();
    }


    /**
     *  Description of the Method
     */
    public void kill()
    {
        try
        {
            if (thread.isAlive())
            {
                stmt.close();
                MraldOutFile.logToFile( "Query killed\n" + this.toString());
            }
            else
            {
                MraldOutFile.logToFile( "User tried to kill query, but it was already done\n" + this.toString());
            }
        }
        catch (SQLException e)
        {
            MraldOutFile.logToFile( "Tried to kill a java.sql.Statment that was already dead (or some other problem).\n" + e.toString());
        }
    }
}

