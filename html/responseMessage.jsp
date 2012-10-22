<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN">
<%@ page isErrorPage="true"%><%@ page import="java.io.*, org.mitre.mrald.util.*, java.util.*" %>
<%

    String message = (String)session.getAttribute("responseMessage");
    if( message == null )
    {
        message = "No message provided.";
    }
    else
    {
        message.replaceAll("\n", "\n<br />");
    }
    session.removeAttribute("responseMessage");
%>
<html>
  <head>
    <meta http-equiv="CACHE-CONTROL" content="NO-CACHE" />
    <title>
      Ooops!
    </title>
    <%=Config.getProperty( "CSS" ) %>
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
    <a href="http://mitre.org">Contact MITRE</a>
    </div>
    <meta name="generator" content="HTML Tidy, see www.w3.org">
    <META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE">
  <center>
        <div class="floating-text" style="margin-top:10em;width:550px;text-align:center">
            <%-- <p>In processing your request, an error has occurred.</p> --%>
            <p style="font-style:italic;"><%=message%></p>
        </div>
    </center>
  </body>
</html>


