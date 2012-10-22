<%@ taglib uri="/WEB-INF/mrald.tld" prefix="mrald"%><%@ taglib uri="/WEB-INF/lattice.tld" prefix="lattice"%><%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %><%@ page import="org.mitre.mrald.util.*" %>
<html>
    <%
            String dbType = WebUtils.getOptionalParameter( request, "dbType");
            String server_name = WebUtils.getOptionalParameter( request, "server-name");
            String port = WebUtils.getOptionalParameter( request, "port");
            String db_name = WebUtils.getOptionalParameter( request, "db-name");
            String url = Config.EMPTY_STR;
            String driver = Config.EMPTY_STR;
            String defaultSchema = Config.EMPTY_STR;
            if( !dbType.equals("other") )
            {
                url = JdbcTemplates.getServerString( dbType );
                if(!server_name.equals(Config.EMPTY_STR)) url = MiscUtils.replace( url, "[host]", server_name );
                if(!port.equals(Config.EMPTY_STR)) url = MiscUtils.replace( url, "[port]", port );
                if(!db_name.equals(Config.EMPTY_STR)) url = MiscUtils.replace( url, "[database]", db_name );
                driver = JdbcTemplates.getDriverClass( dbType );
                if(!server_name.equals(Config.EMPTY_STR)) driver = MiscUtils.replace( driver, "[host]", server_name );
                if(!port.equals(Config.EMPTY_STR)) driver = MiscUtils.replace( driver, "[port]", port );
                if(!db_name.equals(Config.EMPTY_STR)) driver = MiscUtils.replace( driver, "[database]", db_name );
                defaultSchema = JdbcTemplates.getSchema( dbType );
            }
        %>
    <head>
        <title>Step #3: Finish configuration</title>
        <%=Config.getProperty( "CSS" )%>
    </head>
    <body>
        <mrald:validate doAdminCheck="yes" />
        <div id="header">
            <h1 class="headerTitle">
                <%=Config.getProperty( "TITLE" )%>
            </h1>
        </div>
        <div class="subHeader">
        <span class="doNotDisplay">Navigation:</span>
        MRALD - Finding the gems in your data |
        <a href="index.jsp" target="_top">Home</a>
        |
        <a href="mailto:mrald-dev-lists@lists.mitre.org">Contact MITRE</a>
        </div>
        <div class="faq" style="text-align:center">
            <form action="DataSourceWizard4.jsp" method="post">
            <mrald:carryParams />
            <h2>Add login info and verify the connection strings:</h2>
            Login
            <br />
            <input type="text" name="login" size="75" />
            <br />
            <br />
            Password
            <br />
            <input type="password" name="pd" size="75" />
            <br />
            <br />
            URL
            <br />
            <input type="text" name="url" value="<%=url%>" size="75" />
            <br />
            <br />
            Driver
            <br />
            <input type="text" name="driver" value="<%=driver%>" size="75" />
            <br />
            <br />
            Schema
            <br />
            <input type="text" name="schema" value="<%=defaultSchema%>" size="75" />
            <br />
            <br />
            <input type="Submit" value="Check Configuration" />  <input type="button" value="Cancel" onclick="location='index_admin.jsp'"/>
            </form>
            <br />
            <br />
            <center>
                <div class="floating-text" style="width:50%;">
                    <ul>
                        <% if( dbType.equals("other") ) { %>
                        <li>It is essential that the connection strings are accurate. You can find JDBC driver vendor information
                        <a href="http://www.sqlsummit.com/JDBCVend.htm" target="_blank">here</a>
                        . If you submit the driver information to us
                        <a href="mailto:mrald-dev-list@lists.mitre.org">here</a>
                        , we'll add it to the template list. You can add it to your local version yourself as well by adding it to the jdbcStrings.xml file in the WEB-INF directory.</li>
                        <%}%>
                        <li>The value for schema is database and installation dependent. Any default given above is just a guess - contact your DBA if they it is not correct.</li>
                        <li>If you have not already loaded the JDBC driver, you will have to do that manually. Download the driver's JAR file and place it in the WEB-INF/lib directory and restart the application server (e.g., Tomcat). You can find drivers
                        <a href="http://www.sqlsummit.com/JDBCVend.htm" target="_blank">here</a>
                        . You can find more information on JDBC drivers in general
                        <a href="http://java.sun.com/products/jdbc/" target="_blank">here</a>
                        .</li>
                    </ul>
                </div>
            </center>
        </div>
    </body>
</html>

