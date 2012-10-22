<%@ taglib uri="/WEB-INF/mrald.tld" prefix="mrald"%>
<%@ taglib uri="/WEB-INF/lattice.tld" prefix="lattice"%>
<%@ page import="org.mitre.mrald.util.Config" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<mrald:validate />
<html>
  <head>
    <meta http-equiv="CACHE-CONTROL" content="NO-CACHE" />
    <meta name="generator" content="HTML Tidy, see www.w3.org" />
    <title>
      <%=Config.getProperty( "TITLE" ) %>
    </title>
    <%=Config.getProperty( "CSS" ) %>
    <link rel="stylesheet" type="text/css" href="./gila-print.css" media="print" />
    <SCRIPT LANGUAGE="JavaScript1.2" TYPE="text/javascript" SRC="<%=Config.getProperty( "BaseUrl" ) %>/tree.js"></SCRIPT>
    
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
    <a href="http://mitre.org">MITRE</a> | <a href="mailto:mrald-dev-list@lists.mitre.org">Contact Developers</a>
    </div>
    <jsp:include page="header.jsp" />
    <jsp:include page="rightSideBar.jsp" />
    <jsp:include page="leftSideBar.jsp" />
    <div id="main-copy">
      <br />
      <h1 id="introduction" style="border-top: none; padding-top: 0;">Messages</h1>
      
       </div>
<!-- End of center column content -->
  </body>
</html>
