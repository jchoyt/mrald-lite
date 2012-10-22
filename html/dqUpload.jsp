<?xml version="1.0"?>
<%@ page import = "org.mitre.mrald.util.Config"%><%@ page import = "org.mitre.mrald.util.MraldOutFile"%><%@ page import = "java.io.BufferedReader"%><%@ page import = "java.io.StringReader"%><%@ taglib uri="/WEB-INF/mrald.tld" prefix="mrald"%>
<html>
  <head>
    <meta name="generator" content="HTML Tidy, see www.w3.org" />
    <title><%=Config.getProperty( "TITLE" ) %>
    Direct Query Upload Page</title>
    <meta http-equiv="Content-Type" content="text/html;charset=ISO-8859-1" />
    <%=Config.getProperty( "CSS" ) %>
  </head>
  <body>
    <mrald:validate />
    <div id="header">
      <h1 class="headerTitle">
        <%=Config.getProperty( "TITLE" ) %>  Multiple Query File Upload Form
      </h1>
    </div>
    <div class="subHeader">
    <span class="doNotDisplay">Navigation:</span>
    MRALD - Finding the gems in your data |
    <a href="index.jsp" target="_top">Home</a>
    |
    <a href="http://mitre.org">MITRE</a> | <a href="mailto:mrald-dev-list@lists.mitre.org">Contact Developers</a>
    </div>
    <center>
    <div class="floating-text" style="width:250px;background-color:#ff6666">
    This form is broken - someone needs to add a way to deal with multiple databases and uploaded queries
    </div>
      <div class="floating-text" style="width:400px;">
        <form method="POST" action="processDqUpload.jsp" enctype="multipart/form-data">
        <strong>Query File:</strong>
        <input name="queryFile" type="file" />
        <br />
        <br />
        <strong>Usage:</strong>
        To use this form, enter the location of a local text file containing queries you would like to submit directly to the database. Format is one query per line, with the ending semicolon optional. After you submit this form, the next page will contain a listing of all the queries, with checkboxes so you can skip certain queries if you want.
        <div style="text-align: center">
          <input value="Retrieve Data" type="submit" />
        </div>
        </form>
      </div>
    </center>
<!-- This document saved from http://crs.mitre.org/dqUpload.html -->
  </body>
</html>
