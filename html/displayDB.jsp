<%@ taglib uri="/WEB-INF/mrald.tld" prefix="mrald"%>

<%@ page import="org.mitre.mrald.util.Config" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<mrald:validate />
<html>
  <head>
  <meta http-equiv="CACHE-CONTROL" content="NO-CACHE" />
    <meta name="generator" content="HTML Tidy, see www.w3.org" />
    <div id="header">
      <h1 class="headerTitle">
        <%=Config.getProperty( "TITLE" ) %> Display Database Structure
      </h1>
    </div>
    <div class="subHeader">
    <span class="doNotDisplay">Navigation:</span>
    MRALD - Finding the gems in your data |
    <a href="index.jsp" target="_top">Home</a>
    |
    <a href="http://mitre.org">MITRE</a> | <a href="mailto:mrald-dev-list@lists.mitre.org">Contact Developers</a>
    </div>
    <%=Config.getProperty( "CSS" ) %>
    <script language="javascript1.2" type="text/javascript" src="<%=Config.getProperty( "BaseUrl" ) %>/tree.js"></script>
    </head>
  <body>
  <mrald:DisplayDB open="true" datasource="${param.datasource}"/>
  </body>
  </html>
