<%@ taglib uri="/WEB-INF/mrald.tld" prefix="mrald"%>
<%@ taglib uri="/WEB-INF/lattice.tld" prefix="lattice"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ page import="org.mitre.mrald.util.*,java.sql.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<mrald:validate doAdminCheck="yes" />

<%
    String name = WebUtils.getRequiredParameter( request, "name");
    String description = WebUtils.getRequiredParameter( request, "description");
    String dbserver = WebUtils.getRequiredParameter( request, "url");
    String dblogin = WebUtils.getRequiredParameter( request, "login");
    String dbpassword = WebUtils.getRequiredParameter( request, "pd");
    String dbdriver = WebUtils.getRequiredParameter( request, "driver");
    String schema = WebUtils.getRequiredParameter( request, "schema");
    String db_name = WebUtils.getOptionalParameter( request, "db-name");
    Exception thrownException = null;
    MraldConnection mconn = null;
    try
    {
        Class.forName( dbdriver );
        mconn = new MraldConnection( dbserver, dbdriver, dblogin, dbpassword );
    }
    catch( Exception e )
    {
        mconn = null;
        thrownException = e;
    }
    //check metadata
    StringBuilder ret = new StringBuilder(  );
    boolean tablesFound = false;
    if( mconn!=null )
    {
            DatabaseMetaData dbmd = mconn.getConnection().getMetaData();
            /* Get the empty instance */
            String[] types = { "TABLE", "VIEW" };
            ResultSet rs = dbmd.getTables( null, schema, null, types );
            int i = 0;
            while ( rs.next() && i++ < 10 )
            {
                tablesFound = true;
                ret.append( "<li>" );
                ret.append( rs.getString( 3 ) );
                ret.append( "</li>" );
                ret.append( Config.NEWLINE );
            }
    }

%>


<html>
    <head>
        <title>Step #4: Confirmation (or lack thereof)</title>
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
        <center>
        <div class="floating-text" >
            <h2 style="text-align:center">Test results:</h2>
            <% if(thrownException == null){%>
            The database was successfully contacted.
                <% if( tablesFound ) {%>
                    Here are a few tables that were found in the schema <%=schema%>:
                    <ul>
                    <%=ret.toString()%>
                    </ul>
                <%} else {%>
                    However, no tables were found.  If there should be tables there,
                    the most likely cause is the Schema value is incorrect.  Use the
                    browser back button or the Go Back button below to try a different Schema.
                <%}%>
            <%} else if (thrownException instanceof ClassNotFoundException) {%>
            The JDBC driver is not yet available.  Save the configuration, add the
            JDBC driver jar file to the WEB-INF/lib directory, and restart the
            application server (e.g., Tomcat).  If the metadata load fails, an email
            will be sent to the address configured in config.properties, and the error
            will be logged to the debug.log file in the logs directory.  You can then use the
            standard props file editing tool at the top of the admin page to fix any
            errors.
            <%} else {%>
            There was some other problem accessing the database.  The error report is below.
            If you can't resolve this on your own, <a href-"mailto:mrald-dev-list@lists.mitre.rog">contact us</a>.
            <br />
            <%
                String stackTrace = MiscUtils.formatThrowable(thrownException);
                stackTrace = stackTrace.replaceAll( Config.NEWLINE, "<br />");
                stackTrace = stackTrace.replaceAll( "\t", "<img src=\"images/spacer.gif\" width=\"10px\"/>");
                out.write(stackTrace);
            }%>
            <br /><br />
            <form action="DataSourceWizard5.jsp" method="post">
            <mrald:carryParams />
            <input type="hidden" name="another" value="false"/>
            <input type="submit" value="Accept Data Source"/>
            <input type="submit" value="Accept and Add Another" onclick="another.value='true'"/>
            <input type="button" value="Go Back" onclick="history.go(-1)"/>
            <input type="button" value="Cancel" onclick="location='index_admin.jsp'"/>
            </form>
        </div></center>
    </body>
</html>

