<!DOCTYPE HTML PUBLIC "-//W3C//DTD html 4.0 transitional//EN">
<%@ taglib uri="/WEB-INF/mrald.tld" prefix="mrald"%><%@ page import="org.mitre.mrald.util.Config" %>
<html>
    <head>
        <meta http-equiv="CACHE-CONTROL" content="NO-CACHE" />
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
        <title>MRALD Setup</title>
        <link rel="stylesheet" href="mrald.css" type="text/css" />
        <link rel="icon" href="favicon.ico" type="image/x-icon">
        <link rel="shortcut icon" href="favicon.ico" type="image/x-icon">
    </head>
    <body>
        <div class="floating-text">
            <h1>MRALD Initial Setup</h1>
            <ol>
                <li>
                <a href="MraldLogin.jsp">Login</a>
                as the default admin user. User email is admin@mitre.org, password is admin.</li>
                <li>
                <a href="AdminConfig.jsp">Configure</a>
                a few local settings. This allows you to make changes to the main MRALD configuration settings. Admin users can change these later from the admin tab on the main page - edit the config.properties file. Here's a description of the properties and what they are for:
                <ul>
                    <li>
                    <b>BasePath</b>
                    - This is the file system path to your installation. MRALD should automatically detect this and set it for you. No changes will be necessary.</li>
                    <li>
                    <b>BaseUrl</b>
                    - If you want users from other computers to be able to access this installation, change the BaseUrl property to the ip address of your server (e.g., http://server14:8080/mrald). It is critical that this property is correct before you leave this page. The rest of the properties can be re-configured from within the application, however fixing this one requires manual editing of the config.properties file in WEB-INF/props and a restart of the servlet container.</li>
                    <li><b>CSS</b> - This links to the default stylesheet and icon for the entire site.  To change the look and feel, you can either change the listed mrald.css file, or use this property to point to a different css file.</li>
                    <li>
                    <b>DB* and SCHEMA</b>
                    - The properties that start with DB and the SCHEMA property can be updated to reflect the settings for your database, if you want a database to be considered your "main" database. Other databases can be configured with a wizard linked to below. If you want to configure a database here, please refer to the list of JDBC strings linked to from the text above the configuration area. Note also that you will have to acquire and load the JDBC driver for that other databases. See the database supplier for details.</li>
                    <li>
                    <b>SMTPHOST and MAILTO</b>
                    - These properties provide a link to a mail server so that MRALD may notify the admin if there's a problem that requires immediate attention. The system does not require a valid SMTPHOST to operate, however users will be referred to the MAILTO address when they need help from the system administrator, so a valid email address is necessary. There are only a few, rare instances when the system sends an email - most problems are simply logged.</li>
                    <li><b>TITLE</b> - This is the title that will show on most of the pages.  Change this to reflect the intent of your installation.</li>
                </ul>
                </li>
                <%-- put in warning if can't find com.sun.rowset.* classes --%>
                <%
                    try
                    {
                        Class.forName(Config.getProperty("JdbcRowSet"));
                        Class.forName(Config.getProperty("JoinRowSet"));
                        Class.forName(Config.getProperty("WebRowSet"));
                    }
                    catch (ClassNotFoundException e)
                    {
                        %>
                        <li><span style="color:red;" />**WARNING** Implementations of the interfaces JdbcRowSet, JoinRowSet, and WebRowSet are needed for full operability, hoever one or more of these are not configured properly in standard.properties.  Most likely this has occurred becuase you aren't using a Sun JVM or Sun moved their rowset implementations out of the com.sun.rowset package in a new release.  Implementations of the JDBC 3 RowSet interfaces can be found:
                            <ul>
                            <li>in the sun JVM (as of Java 1.5 this was in the package com.sun.rowset)</li>
                            <li>in the WebLogic server (in package weblogic.jdbc.rowset)</li>
                            <li>in the Oracle <a href="http://www.oracle.com/technology/software/tech/java/sqlj_jdbc/index.html">jdbc driver</a> (in package oracle.jdbc.rowset)</li>
                            <li>in Sun's <a href="http://java.sun.com/products/jdbc/download.html">reference implementation</a> (in package com.sun.rowset))</li>
                            </ul>
                            The Oracle implementations can be used even if you aren't using an Oracle Database.  Please fix this by acquiring one of the implementations above, putting the jar file in the WEB-INF/lib directory, and modifying the appropriate properties in <a href="AdminConfig.jsp?filename=standard.properties"> standard.properties</a> before you try to use any of the cross DB join facilities or XML output (XML output will degrade to a much less robust format whereas cross DB joins just won't work).</span>
                        </li>
                        <%
                    }
                %>


                <li>
                <a href="DataSourceWizard.jsp" target="_blank">Add</a>
                new databases.  This wizard is also available from the admin tab on the main page after login, however it is provided here to facilitate inital database setup.  <i>This link opens up in a new window - close the window when you are done to return here and continue with the setup.</i></li>
                <li>
                <a href="RegistrationForm.jsp?pageurl=FirstRun.jsp">Register</a>
                a new user for yourself. The admin user is provided as a bootstrap and shouldn't be used routinely.</li>
                <li>
                <a href="PeopleManager.jsp" target="_blank">Edit the new user</a>
                (optional) to be an admin. Use the linked form to search for the user you just created and change the user peopletypeid from 1 (normal user) to 3 (admin user).  <i>This link opens up in a new window - close the window when you are done to return here and continue with the setup.</i></li>
                <li>
                <a href="PeopleManager.jsp" target="_blank">Delete the admin user</a>
                or
                <a href="ChangePassword.jsp" target="_blank">change the admin user password</a>
                (optional). The default admin user should be deleted or at a minimum have the password chagned.  <i>These links open up in a new window - close the window when you are done to return here and continue with the setup.</i></li>
                <li>
                <a href="logout.jsp">Logout</a>
                and log in as your new user before you begin using the system.</li>
            </ol>
            <input type="button" value="Done! Go to main page" onclick="location='WipeFirstRun.jsp';" />
        </div>
    </body>
</html>

