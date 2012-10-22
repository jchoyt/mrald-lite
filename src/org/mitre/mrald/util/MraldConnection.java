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

import javax.sql.rowset.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import org.mitre.mrald.control.MsgObject;
import org.mitre.mrald.util.Config;
import org.mitre.mrald.util.JdbcPropertyPublisher;
import org.mitre.mrald.util.MiscUtils;
import org.mitre.mrald.util.MraldOutFile;


/**
 *  The MraldConnection class coordinates all SQL calls to the database. It is the only way to obtain access to the
 *  database.
 *
 * @author     Gail Hamilton
 * @created    mid 2001
 */

public class MraldConnection
{
    private Connection moConnection;
    private Statement moStatement;
    private Properties props;


    /**
     *  This constructor initializes the class by establishing a connection to the database using the URL of the data
     *  source, a data source driver, a username, and a password.
     *
     * @param  sURL              Description of the Parameter
     * @param  sDriver           Description of the Parameter
     * @param  sUsername         Description of the Parameter
     * @param  sPassword         Description of the Parameter
     * @exception  SQLException  Description of the Exception
     * @deprecated               - only the constructor providing the datasource should be used
     */
    public MraldConnection(String sURL, String sDriver, String sUsername, String sPassword)
        throws SQLException
    {
        MraldConnection dammit = new MraldConnection(sURL, sDriver, sUsername, sPassword, new Properties(), null);
        this.moConnection = dammit.getConnection();
        this.moStatement = dammit.getStatement();
    }


    /**
     *  This constructor initializes the class by establishing a connection to the database using the URL of the data
     *  source, a data source driver, a username, and a password.
     *
     * @param  sURL              Description of the Parameter
     * @param  sDriver           Description of the Parameter
     * @param  sUsername         Description of the Parameter
     * @param  sPassword         Description of the Parameter
     * @param  info              Description of the Parameter
     * @exception  SQLException  Description of the Exception
     */
    public MraldConnection(String sURL, String sDriver, String sUsername, String sPassword, Properties info, HttpServletRequest request)
        throws SQLException
    {
//        Object[] o = {sURL, sDriver, sUsername, sPassword, info};

        // MsgObject localMsg = new MsgObject();
        // if(msg != null)
        // {
        //     localMsg.copyFrom( msg );
        // }
        info.put("user", sUsername);
        info.put("password", sPassword);
        props = info;
        JdbcPropertyPublisher.populateProperties(request, info);
        /*
         *  get a reference to the connection
         */
        moConnection = DriverManager.getConnection(sURL, info);
        moConnection.setAutoCommit(true);
        //get a reference to the statement
        moStatement = moConnection.createStatement();
    }


    /**
     *  Uses the connection strings in config.properties
     *
     * @exception  SQLException  Description of the Exception
     * @deprecated               - only the constructor providing the datasource should be used
     */
    public MraldConnection()
        throws SQLException
    {
        MraldConnection dammit = new MraldConnection(
                Config.getProperty("DBSERVER"),
                Config.getProperty("DBDRIVER"),
                Config.getProperty("DBLOGIN"),
                Config.getProperty("DBPASSWORD"),
                new Properties(),
                null );
        this.moConnection = dammit.getConnection();
        this.moStatement = dammit.getStatement();
    }


    /**
     *  Constructor for the MraldConnection object
     *
     * @param  propsKey  Description of the Parameter
     */
    public MraldConnection(String propsKey)
    {
        props = MetaData.getDbProperties(propsKey);
        if (props == null)
        {
            throw new RuntimeException("There is no data base connection configuration with the name " + propsKey);
        }
        try
        {
            MraldConnection dammit = new MraldConnection(
                    Config.replacements(props.getProperty("DBSERVER")),
                    props.getProperty("DBDRIVER"),
                    props.getProperty("DBLOGIN"),
                    props.getProperty("DBPASSWORD"),
                    props,
                    null );
            this.moConnection = dammit.getConnection();
            this.moStatement = dammit.createStatement();
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Error setting the connection to the database.", e);
        }

    }


    /**
     *  Constructor for the MraldConnection object
     *
     * @param  propsKey  Description of the Parameter
     */
    public MraldConnection(String propsKey, MsgObject msg)
    {
        props = MetaData.getDbProperties(propsKey);

        if (props == null)
        {
            throw new RuntimeException("There is no data base connection configuration with the name " + propsKey);
        }
        try
        {
            MraldConnection dammit = new MraldConnection(
                    Config.replacements(props.getProperty("DBSERVER")),
                    props.getProperty("DBDRIVER"),
                    props.getProperty("DBLOGIN"),
                    props.getProperty("DBPASSWORD"),
                    props,
                    msg.getReq());
            this.moConnection = dammit.getConnection();
            this.moStatement = dammit.createStatement();
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Error setting the connection to the database.", e);
        }

    }


    public MraldConnection(String propsKey, HttpServletRequest request)
    {
        props = MetaData.getDbProperties(propsKey);

        if (props == null)
        {
            throw new RuntimeException("There is no data base connection configuration with the name " + propsKey);
        }
        try
        {
            MraldConnection dammit = new MraldConnection(
                    Config.replacements(props.getProperty("DBSERVER")),
                    props.getProperty("DBDRIVER"),
                    props.getProperty("DBLOGIN"),
                    props.getProperty("DBPASSWORD"),
                    props,
                    request);
            this.moConnection = dammit.getConnection();
            this.moStatement = dammit.createStatement();
        }
        catch (SQLException e)
        {
            throw new RuntimeException("Error setting the connection to the database.", e);
        }

    }


    /**
     *  This method sets whether the connection automatically commits SQL statements individually or as grouped
     *  transactions that are terminated by either a call to Rollback() or Commit(). By default, new connections
     *  automatically commit SQL statements individually.
     *
     * @param  bAutoCommit                The new autoCommit value
     * @exception  java.sql.SQLException  Description of the Exception
     */
    public void setAutoCommit(boolean bAutoCommit)
        throws java.sql.SQLException
    {
        moConnection.setAutoCommit(bAutoCommit);
    }


    /**
     *  This method returns the existing connection to the database.
     *
     * @param  oStatement  The new statement value
     * @deprecated         - this should not be directly accessible. Changing getStatement so you get a new statement
     */
    public void setStatement(java.sql.Statement oStatement)
    {
        moStatement = oStatement;
    }


    /**
     *  This method returns the existing connection to the database.
     *
     * @return    The connection value
     */
    public Connection getConnection()
    {
        return moConnection;
    }


    /**
     *  This method returns the existing connection to the database.
     *
     * @return        The statement value
     * @deprecated    Only a synonym for createStatement now
     */
    public Statement getStatement()
    {
        return createStatement();
    }


    /**
     *  Description of the Method
     *
     * @return    Description of the Return Value
     */
    public Statement createStatement()
    {
        try
        {
            return moConnection.createStatement();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }


    /**
     *  Description of the Method
     *
     * @return    Description of the Return Value
     */
    public Statement resetStatement()
    {
        try
        {
        	moStatement.close();
        	moStatement =moConnection.createStatement();
            return moStatement;
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }
    /**
     *  This method will either return the connection to the pool or close the open connection and statement of the
     *  database. This method is used to free up resources.
     *
     * @exception  java.sql.SQLException  Description of the Exception
     */
    public void close()
        throws java.sql.SQLException
    {
        if (moStatement != null)
        {
            moStatement.close();
            moStatement = null;
        }
        if (moConnection != null)
        {
            moConnection.close();
            moConnection = null;
        }
    }


    /**
     *  This method commits the statements of the current transaction. This method should only be used when auto commit
     *  has been disabled.
     *
     * @exception  java.sql.SQLException  Description of the Exception
     */
    public void commit()
        throws java.sql.SQLException
    {
        moConnection.commit();
    }


    /**
     *  This method executes the Query SQL statement (usually a SELECT) that is passed as a parameter and returns a
     *  java.sql.ResultSet. Repeated calls to this method delete previous ResultSets. The close method of the Resultset
     *  should be invoked when the user has finished using the Resultset.
     *
     * @param  sSQL                       Description of the Parameter
     * @return                            Description of the Return Value
     * @exception  java.sql.SQLException  Description of the Exception
     */
    public ResultSet executeQuery( String sSQL )
        throws java.sql.SQLException
    {
        sSQL = MiscUtils.clearSemiColon( sSQL );
        return (moStatement.executeQuery( sSQL ) );
    }

    public JdbcRowSet getRowSet(String query)
        throws java.sql.SQLException
    {
        String clearedQuery = MiscUtils.clearSemiColon( query );
        JdbcRowSet jrs;
        try
        {
            jrs = RowSetFactory.createJdbcRowSet();
        }
        catch(MraldException e)
        {
            throw new RuntimeException( "Sorry, the facilities for joining across databases are not configured correctly.  Please ask your administrator to correct the configuration", e);
        }
        jrs.setCommand( clearedQuery );
        jrs.setUrl( props.getProperty("DBSERVER") );
        jrs.setUsername( props.getProperty("DBLOGIN") );
        jrs.setPassword( props.getProperty("DBPASSWORD") );
        jrs.execute();
        return jrs;
    }


    /**
     *  This method executes an updating SQL statement (usually an UPDATE, INSERT, or DELETE) that is passed as a
     *  parameter and returns an int that represents either the row count or 0 for SQL statements that don't have return
     *  values.
     *
     * @param  sSQL                       Description of the Parameter
     * @return                            Description of the Return Value
     * @exception  java.sql.SQLException  Description of the Exception
     */
    public int executeUpdate(String sSQL)
        throws java.sql.SQLException
    {
        sSQL = MiscUtils.clearSemiColon(sSQL);
        //execute the updating SQL
        int iRowCount = moStatement.executeUpdate(sSQL);
        return (iRowCount);
    }

    /**
     *  This method executes an updating SQL statement (usually an UPDATE, INSERT, or DELETE) that is passed as a
     *  parameter and returns an int that represents either the row count or 0 for SQL statements that don't have return
     *  values.
     *
     * @param  sSQL                       Description of the Parameter
     * @return                            Description of the Return Value
     * @exception  java.sql.SQLException  Description of the Exception
     */
    public ResultSet executeUpdate(String sSQL, int returnGenerated)
        throws java.sql.SQLException
    {
        sSQL = MiscUtils.clearSemiColon(sSQL);

        //execute the updating SQL
        //int iRowCount =
        moStatement.executeUpdate(sSQL, returnGenerated);
       return moStatement.getGeneratedKeys();
    }

    /**
     *  This method rolls back the statements of the current transaction. This method should only be used when auto
     *  commit has been disabled.
     *
     * @exception  java.sql.SQLException  Description of the Exception
     */
    public void rollback()
        throws java.sql.SQLException
    {
        moConnection.rollback();
    }


    /**
     *  Description of the Method
     *
     * @return                   true if all tables were created successfully.
     * @exception  SQLException  Description of the Exception
     */
    public static boolean createPeopleTable()
        throws SQLException
    {
        try
        {
            //latticegroup table first
            int qNum = 0;
            MraldConnection conn = new MraldConnection(MetaData.ADMIN_DB);

            //finally people
            String query = "CREATE TABLE people ( ";
            query += " email          varchar(100) not null,";
            query += " peopletypeid   integer null,";
            query += " password       varchar(100) null,";
            query += " firstname      varchar(50) null,";
            query += " lastname       varchar(75) null,";
            query += " company        varchar(100) null,";
            query += " department     varchar(50) null,";
            query += " address		  varchar(300) null,";
            query += " city           varchar(50) null,";
            query += " state          varchar(50) null,";
            query += " zip            varchar(10) null,";
            query += " country        varchar(50) null,";
            query += " validationcode varchar(12) null,";
            query += " validated	  varchar(1) null,";
            query += " primary key (email),";
            query += " foreign key (latticegroupid)";
            query += " references latticegroup (latticegroupid),";
            query += " check ((email is not null))";
            query += " )";
            MraldOutFile.logToFile(Config.getProperty("LOGFILE"), "MraldConnection: createPeopleTable() - Query " + qNum++ + ":" + query);
            //i =
            conn.executeUpdate(query);
            query = "insert into people (email, peopletypeid, latticegroupid, password, validationcode, validated) values ('admin@mitre.org', 3, 'Public', 'XPERsFUdQRgrVI/si2Fv1ROAJ3X4BnOredragQ==', 'still fake', 'Y')";
            MraldOutFile.logToFile(Config.getProperty("LOGFILE"), "MraldConnection: createPeopleTable() - Query " + qNum++ + ":" + query);
            //i =
            conn.executeUpdate(query);

            return true;
        }
        catch (SQLException sqe)
        {
            MraldOutFile.logToFile( sqe );
            return false;
        }
    }
}

