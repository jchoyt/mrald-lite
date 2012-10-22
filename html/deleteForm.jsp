
<%@ page import = "org.mitre.mrald.util.Config"%><%
    String formId = request.getParameter( "formid" );
    String formAccess = request.getParameter( "formAccess" );
    if ( (formAccess == null)  || formAccess.equals("") )
        formAccess="Personal";
%>
<%@ taglib uri="/WEB-INF/mrald.tld" prefix="mrald"%>
<mrald:validate />
<html>
  <head>
    <META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE">
    <meta name="generator" content="HTML Tidy, see www.w3.org">
    <title>
      Delete Form Confirmation
    </title>
    <%=Config.getProperty( "CSS" ) %>
  </head>
  <body>
    <form method="post" action="FormServer.jsp?action=delete&amp;formid=<%= formId%>&amp;formAccess=<%=formAccess%>">
      <center>
        <br>
        <br>
        <br>
        <br>
        <br>
        <br>
        <br>

        <table summary="" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td class="bord">
              <table summary="" width="100%" border="0" cellspacing="1" cellpadding="2">
                <tr>
                  <th>
                    D e l e t e &nbsp; F o r m
                  </th>
                </tr>
                <tr>
                  <td style="padding:1em">
                    If you are you SURE you want to delete this form, use the Delete Form button below.
                  </td>
                </tr>
              </table>
            </td>
          </tr>
        </table>
        <br>
        <input type="submit" value="Delete Form">
      </center>
    </form>
    <form method="post" action="index.jsp">
    <center>
      <input type="submit" value="No, go back to the index page">
      </center>
    </form>
  </body>
</html>


