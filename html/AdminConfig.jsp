<?xml version="1.0"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="org.mitre.mrald.util.Config" %><%@ taglib uri="/WEB-INF/mrald.tld" prefix="mrald"%><%@ page import="org.mitre.mrald.util.Config" %><%
    String file_name = request.getParameter( "filename" );
    if ( file_name == null || file_name.equals( "" ) )
    {
       file_name = Config.PROPS_FILE;
    }
%>
<html>
  <head>
    <meta http-equiv="CACHE-CONtrOL" content="NO-CACHE" />
    <title>
      <%=Config.getProperty( "TITLE" ) %>
    </title>
    <%=Config.getProperty( "CSS" ) %>
  </head>
  <body>
    <mrald:validate doAdminCheck="yes" />
    <div id="header">
      <h1 class="headerTitle"><%=Config.getProperty( "TITLE" ) %> Administration Configuration</h1>
    </div>
    <div class="subHeader">
    <span class="doNotDisplay">Navigation:</span>
    MRALD - Finding the gems in your data |
    <a href="index.jsp" target="_top">Home</a>
    |
    <a href="http://mitre.org">MITRE</a> | <a href="mailto:mrald-dev-list@lists.mitre.org">Contact Developers</a>
    </div>
    <form action="ConfigServlet" method="POST">
      <center>
        <div class="floating-text">
        <h1 style="border-top: none;margin-top:0em;margin-bottom:.5em">MRALD Properties File Configuration</h1>
        Below you will find the values in the '<%=file_name%>' file. This file is essential to the proper operation of the MRALD system. Please be sure to be careful of the entries in this file. Whitespace will be trimmed from the beginning and end of all property names and values.  If you have any questions or concerns, please read the instructions for the MRALD system.
        <br />
        <br />
        <%
                            if ( file_name.equals( Config.PROPS_FILE ) )
                            { %>
		We have provided an <a href="jdbcStrings.html" target="_blank"><b>example list of JDBC connection strings</b></a> for your convenience.
        It is not complete and MRALD has not been tested with all these. Any feedback on bad connection strings or database compatibility is appreciated. Please send feedback to use at
        <a href="mailto:mrald-dev-list@lists.mitre.org">mrald-dev-list@lists.mitre.org</a>
		<br />
		<br />
		We also provide the ability to outline a Proxy Host and Proxy Port.  If no proxy is needed, simply leave these two properties blank and they will be ignored.
        <br />
        <br />
        <%
                }
                %>
        <center>
          <table cellspacing="0" cellpadding="2" border="0">
            <tr>
              <th>Name</th>
              <th>Value</th>
              <th>Delete?</th>
            </tr>
            <mrald:ListConfig />
            <tr>
              <td align='right'>
              <input type="text" name="AddedFieldName1" size="20" />
              =</td>
              <td>
                <input type="text" name="AddedFieldValue1" size="60" />
              </td>
              <td>
              </td>
            </tr>
            <tr>
              <td align='right'>
              <input type="text" name="AddedFieldName2" size="20" />
              =</td>
              <td>
                <input type="text" name="AddedFieldValue2" size="60" />
              </td>
              <td>
              </td>
            </tr>
            <tr>
              <td align='right'>
              <input type="text" name="AddedFieldName3" size="20" />
              =</td>
              <td>
                <input type="text" name="AddedFieldValue3" size="60" />
              </td>
              <td>
              </td>
            </tr>
            <tr>
              <td align='right'>
              <input type="text" name="AddedFieldName4" size="20" />
              =</td>
              <td>
                <input type="text" name="AddedFieldValue4" size="60" />
              </td>
              <td>
              </td>
            </tr>
          </table>
          <br />
          <input type="SUBMIT" name="SubmitConfig" value="Commit Changes" />
          <input type="SUBMIT" name="AddMoreTuples" value="Add More Rows?" />
          <input type="RESET" value="Reset Form" />
        </center>
        <br />
        <br />
        </div>
      </center>
    </form>
  </body>
</html>
