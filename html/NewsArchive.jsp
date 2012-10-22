<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%@ page import="org.mitre.mrald.util.Config" %>
<%@ taglib uri="/WEB-INF/mrald.tld" prefix="mrald"%>
<%@ page import="org.mitre.mrald.util.Config" %>
<%
    String file_name = request.getParameter( "filename" );

    if ( file_name == null || file_name.equals( "" ) )
  {
       file_name = Config.PROPS_FILE;
  }
%>
<html>
  <head>
    <meta name="generator" content="HTML Tidy, see www.w3.org">
    <META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE">
    <title>
      News Archive
    </title>
    <%=Config.getProperty( "CSS" ) %>
  </head>
  <body>
    <div id="header">
      <h1 class="headerTitle">
        <%=Config.getProperty( "TITLE" ) %>
      </h1>
    </div>
	<script language="JavaScript1.2" type="text/javascript" src="<%=Config.getProperty( "BaseUrl" ) %>/navi.js"></script>
      <div class="faq" style="text-align: center">
        <CENTER>
          <h2>News Archive</H2>
          <TABLE CELLSPACING="0" CELLPADDING="2" BORDER="0" WIDTH="600">
		  <mrald:NewsDisplay newsArchive="yes" />
          </TABLE><BR>
        </CENTER>
      </div>
  </body>
</html>


