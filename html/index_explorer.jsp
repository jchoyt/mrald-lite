<%@ taglib uri="/WEB-INF/mrald.tld" prefix="mrald"%>
<%@ taglib uri="/WEB-INF/lattice.tld" prefix="lattice"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ page import="org.mitre.mrald.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<mrald:validate />
<%
	String pageurl = request.getParameter( "pageurl" );
	User mraldUser = (User)session.getAttribute(Config.getProperty("cookietag"));
%>
<html>
    <head>
        <meta http-equiv="CACHE-CONTROL" content="NO-CACHE" />
        <meta name="generator" content="HTML Tidy, see www.w3.org" />
        <title>
            <%=Config.getProperty( "TITLE" ) %>
        </title>
        <%=Config.getProperty( "CSS" ) %>
        <link rel="stylesheet" type="text/css" href="./gila-print.css" media="print" />
        <script language="JavaScript" type="text/javascript" src="<%=Config.getProperty( "BaseUrl" ) %>/tree.js"></script>
    </head>
    <body>
        <div id="header">
            <h1 class="headerTitle">
                <%=Config.getProperty( "TITLE" ) %>
            </h1>
        </div>
        <div class="subHeader">
        <span class="doNotDisplay">Navigation:</span>
        MRALD - Finding the gems in your data |
        <a href="index.jsp" target="_top">Home</a>
        |
        <a href="http://mitre.org">MITRE</a>
        |
        <a href="mailto:mrald-dev-list@lists.mitre.org">Contact Developers</a>
        </div>
        <jsp:include page="header.jsp" />
        <jsp:include page="rightSideBar.jsp" />
        <jsp:include page="leftSideBar.jsp" />
        <div id="main-copy">
            <h1 id="introduction" style="border-top: none; padding-top: 0;">Database Stucture
                <a href="help/index-help.html#database" target="_blank"><img alt="help" src="images/green-help.jpeg" /></a>
            </h1>
            <% for( String ds : MetaData.getDatasourceNames() )
            {
                if( ds.equals( MetaData.ADMIN_DB ) && mraldUser.getTypeId() != User.ADMIN_USER )
                {
                    continue;
                }
                String displayName = WebUtils.getDatasourceDisplayName(ds);
                %>
                <hr />
                <%=displayName%> - <%=MetaData.getDbMetaData(ds).getDbVersion()%>
                <br />
                <mrald:DisplayDB datasource="<%=ds%>" />
                <img alt="" src="images/spacer.gif" width="30" height="1" />
                <a href="displayDB.jsp?datasource=<%=ds%>" target="_blank">Show tree in new screen</a> |
                <input type="button" value="Visualize" onclick="location='FormSubmit?datasource=<%=ds%>&workflow=Output%20DB'"/>
                <br />
            <%}%>
            <h1>Keyword Search
            <a href="help/index-help.html#keyword" target="_blank">
                <img alt="help" src="images/green-help.jpeg" />
            </a>
            </h1>
            <br />
            <form method="POST" action="moogle.jsp" enctype="application/x-www-form-urlencoded">
                <strong>Search the database <select name="Datasource"><tags:datasourceList/></select> for the term:</strong>
                <input name="term" type="text" value="" />
                <br />
                Limit No of Lines Per Table to
                <input name="lineLimit" type="text" size="6" value="100" />
                Lines (Leave blank for no line limit.)
                <br />
                <br />
            <input name="submitBtn" type="submit" value="Search" />
            </form>
            <%-- <h1>Visualize Database
            <a href="help/index-help.html#database" target="_blank">
                <img alt="help" src="images/green-help.jpeg" />
            </a>
            </h1>
            <br />
            <form method="POST" action="FormSubmit" enctype="application/x-www-form-urlencoded">
                <input type="hidden" name="workflow" value="Output DB" />
                <input name="submitBtn" type="submit" value="Vizualize" />
            </form> --%>
            <%--
                if ( Config.getLatticeFactory().getUsingLatticeSecurityModel() )
                {
                %>
                <h1>Current COI
                <a href="help/lattice-help.html#coiDef" target="_blank">
                    <img alt="help" src="images/green-help.jpeg" />
                </a>
                </h1>
                <br />
                <!--center>
                    <lattice:showCOI />
                    <input type="button" value="Change COI" onclick="location='changeCOI.jsp'"/>
                </center-->
                <%
                }
            --%>
        </div>
        <!-- End of center column content -->
    </body>
</html>

