<%@ taglib uri="/WEB-INF/mrald.tld" prefix="mrald"%>
<%@ taglib uri="/WEB-INF/lattice.tld" prefix="lattice"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ page import="org.mitre.mrald.util.*" %>

    <%
        String name = WebUtils.getRequiredParameter( request, "name");
        String description = WebUtils.getRequiredParameter( request, "description");
        String dbType = WebUtils.getRequiredParameter( request, "dbType");
        if( dbType.equals("other") || dbType.equals("HypersonicSQL - file") )
        {
    %>
    	    <input type="hidden" name="db-name"  value="<%=name%>" /> 
            <jsp:forward page="DataSourceWizard3.jsp" />
	  
            <!-- note, the parameters carry automatically -->
    <%
        }
        String defaultPort = JdbcTemplates.getPort( dbType );
    %>
    <head>
        <title>Step #2: Configure the datasource</title>
        <%=Config.getProperty( "CSS" )%>
    </head>
    <body>
        <mrald:validate  doAdminCheck="yes" />
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
            <form action="DataSourceWizard3.jsp" method="post">
            <mrald:carryParams />
            <h2>Provide the connection information:</h2>
            Name or IP address of server
            <br />
            <input type="text" name="server-name" />
            <br />
            <br />
            Database name/SID
            <br />
            <input type="text" name="db-name" />
            <br />
            <br />
            Port
            <br />
            <input type="text" name="port" value="<%=defaultPort%>"/>
            <br />
            <br />
            <input type="Submit" value="Continue" /> <input type="button" value="Cancel" onclick="location='index_admin.jsp'"/>
            </form>
        </div>
    </body>
</html>
