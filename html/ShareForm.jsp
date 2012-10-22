<%@ taglib uri="/WEB-INF/mrald.tld" prefix="mrald"%><%-- This jsp copies a form to a different user.   --%>

<%@ page import = "org.mitre.mrald.util.User, org.mitre.mrald.util.Config"%>
<!DOCTYPE html PUBLIC "-//w3c//dtd html 3.2//en">
<html>
  <head>
    <META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE">
    <title>
      Share document with the following user
    </title>
    <meta name="generator" content="HTML Tidy, see www.w3.org">
    <%=Config.getProperty( "CSS" ) %>
  </head>
  <body>
    <mrald:validate />
    <%
        String[] formIds = request.getParameterValues( "formid" );
        User mraldUser = (User)session.getAttribute(Config.getProperty( "cookietag" ));
        mraldUser.setFormIds( formIds);
    %>
    <form action="CopyForm.jsp" method="post" enctype="x-www-form-urlencoded">
      <center>
        <br>
        <br>
        <table summary="" width=50% border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td class="bord">
              <table summary="" width="100%" border="0" cellspacing="1" cellpadding="2">
                <tr>
                  <th>
                    S h a r e &nbsp; F o r m
                  </th>
                </tr>
                <tr>
                  <td style="padding:1em">
                    <mrald:allUsers />
                  </td>
                </tr>
              </table>
            </td>
          </tr>
        </table>
        <br>
        <input type="submit" value="Share form with user">
      </center>
    </form>
  </body>
</html>



