<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import = "org.mitre.mrald.util.*" %><%@ page import = "java.io.*"%><%@ page import = "java.net.*"%><%
    response.setContentType( "text/html" );
%><%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %><%
    String sizeString = WebUtils.getOptionalParameter(request, "size", "10" );

    %>
<html>
<!--
 * :mode=jsp:tabSize=2:indentSize=2:noTabs=true:
 * :folding=indent:collapseFolds=0:wrap=none:maxLineLen=80:
-->
  <head>
    <meta http-equiv="CACHE-CONTROL" content="NO-CACHE" />
    <meta name="generator" content="HTML Tidy, see www.w3.org" />
    <title>User queries successfully executed</title>
    <%=Config.getProperty( "CSS" ) %>
  </head>
  <body>
    <div id="header">
    <% if( sizeString.equals("all") )
    {%>
      <h1 class="headerTitle">
        Query History - All your queries
      </h1>
    <%}
    else
    {%>
      <h1 class="headerTitle">
        Query History - Your last <%=sizeString%> queries
      </h1>
    <%}%>
    </div>
    <div class="subHeader">
    <span class="doNotDisplay">Navigation:</span>
    MRALD - Finding the gems in your data |
    <a href="index.jsp" target="_top">Home</a>
    |
    <a href="http://mitre.org">MITRE</a> | <a href="mailto:mrald-dev-list@lists.mitre.org">Contact Developers</a>
    </div>
    <br />
    <div class="floating-text">
      <form action="QueryHistory.jsp" method="post" enctype="x-www-form-urlencoded">
        <center>Show me the last
          <select name="size">
            <option value="10">10</option>
            <option value="25">25</option>
            <option value="50">50</option>
            <option value="all">all</option>
          </select> queries I ran. <input type="submit" value="Go"/>
        </center>
      </form>
      <tags:lastQueries />
      <br/>
      <form action="QueryHistory.jsp" method="post" enctype="x-www-form-urlencoded">
        <center>Show me the last
          <select name="size">
            <option value="10">10</option>
            <option value="25">25</option>
            <option value="50">50</option>
            <option value="all">all</option>
          </select> queries I ran. <input type="submit" value="Go"/>
        </center>
      </form>
    </div>
  </body>
</html>
