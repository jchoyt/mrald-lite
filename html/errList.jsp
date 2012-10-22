<?xml version="1.0"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="org.mitre.mrald.util.Config" %><%@ taglib uri="/WEB-INF/mrald.tld" prefix="mrald"%><%@ page import="org.mitre.mrald.util.Config" %>
<html>
  <head>
    <meta http-equiv="CACHE-CONTROL" content="NO-CACHE" />
    <title><%=Config.getProperty( "TITLE" ) %>- Error Files</title>
    <%=Config.getProperty( "CSS" ) %>
  </head>
  <body>
    <div id="header">
      <h1 class="headerTitle">Error File Manager</h1>
    </div>
    <div class="subHeader">
    <span class="doNotDisplay">Navigation:</span>
    MRALD - Finding the gems in your data |
    <a href="index.jsp" target="_top">Home</a>
    |
    <a href="http://mitre.org">MITRE</a> | <a href="mailto:mrald-dev-list@lists.mitre.org">Contact Developers</a>
    </div>
    <form action="AdminControl.jsp?action=deleteErrFiles" method="POST">
      <center>
        <div class="floating-text">Below is a list of all the error files MRALD has dumped. The files are listed in order by date, starting with the newest files.
        <br />
        <br />
        To view the contents of a file, click on the link. To delete a file or set of files, check the Delete checkbox and use the Submit button below: any checked files will be deleted and you will be returned to the home page.
        <i>Note that files older than one week are automatically checked for deletion.</i>
        <hr width="75%" />
        <center>
          <mrald:errFileList />
          <br />
          <input type="SUBMIT" name="Submit" value="Submit" />
          <input type="RESET" value="Reset Form" />
        </center>
        </div>
      </center>
    </form>
  </body>
</html>
