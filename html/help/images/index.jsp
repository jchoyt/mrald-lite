<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="org.mitre.mrald.util.Config" %>
<html>
  <head>
    <meta name="generator" content="HTML Tidy, see www.w3.org">
    <title>
      Access Denied - MRALD
    </title>
    <%=Config.getProperty( "CSS" ) %>
  </head>
  <body>
    <center>
      <p>
        &nbsp;
      </p>
      <p>
        &nbsp;
      </p>
      <table summary="" width="70%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td class="bord">
            <table summary="" width="100%" border="0" cellspacing="1" cellpadding="2">
              <tr>
                <th>
                  <font size="+3"><b>Access Denied</b></font>
                </th>
              </tr>
              <tr>
                <td style="padding:2em">
                  You do not have access to the requested resource or file. Please use the BACK button on your browser to navigate to the previous page. If the problem is unexpected or persists with intervention, please contact your <a href="mailto:<%=Config.getProperty( "MAILTO" )%>">site administrator</a></td>.<br>
                </td>
              </tr>
            </table>
          </td>
        </tr>
      </table>
    </center>
  </body>
</html>


