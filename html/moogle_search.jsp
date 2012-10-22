
<%@ taglib uri="/WEB-INF/mrald.tld" prefix="mrald"%>
<%@ page import="org.mitre.mrald.util.Config" %>
<?xml version="1.0"?>
<html>
  <head>
    <meta name="generator" content="HTML Tidy, see www.w3.org" />
    <title>Moogle Page</title>
    <meta http-equiv="Content-Type" content="text/html;charset=ISO-8859-1" />
    <link rel="stylesheet" href="mrald.css" type="text/css" />
  </head>
  <body>
    <center>
      <div class="floating-text" style="width:400px;">
        <form method="POST" action="moogle.jsp" enctype="application/x-www-form-urlencoded">
        <strong>Moogle the Baseball Database For the Term:</strong>
        <input name="term" type="text" value="aaron" />
        <br />
        <br />
        <div style="text-align: center">
          <mrald:tableList displayTables="false" />
	  </div>
        </div>
        </form>
      </div>
    </center>
  </body>
</html>
