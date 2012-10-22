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
        <%=Config.getProperty( "TITLE" ) %><br/> Database Vizualizer
      </h1>
    </div>
    <div class="subHeader">
    <span class="doNotDisplay">Navigation:</span>
    MRALD - Finding the gems in your data |
    <a href="index.jsp">Home</a>
    |
    <a href="http://mitre.org">MITRE</a> | <a href="mailto:mrald-dev-list@lists.mitre.org">Contact Developers</a>
    </div>
    <%-- <jsp:include page="header.jsp" />
    <jsp:include page="leftSideBar.jsp" /> --%>
    <!-- side bars -->

    <form method="post" action="FormServer.jsp">

     <center>
       <div id="applet" align="center">
       <table cellpadding="1" cellspacing="0" bgcolor="#000000">

       <applet code="org.mitre.mrald.graphics.DBViewerApplet.class" codebase='<%=Config.getProperty("BaseUrl")%>/graphics/'
		archive="prefuse.jar, graphics-util.jar,servlet.jar" width="1000" height="700">
		<param name="fileName" value="<%=Config.getProperty("BaseUrl")%>/temp.txt"/>
		<param name="configFile" value="<%=Config.getProperty("BasePath")%>/WEB-INF/props/"/>
		<param name="baseURL" value="<%=Config.getProperty( "BaseUrl" ) %>"/>
		<param name="type" value="application/x-java-applet;version=1.5">
     
        <param name="datasource" value="${datasource}" />
		If you can read this text, the applet is not working. Perhaps you don't
		have the Java 5.0 (or later) web browser plug-in installed?
		</applet>

</table>
</div>
       </center>
    </form>
<!-- End of center column content -->
  </body>
</html>
