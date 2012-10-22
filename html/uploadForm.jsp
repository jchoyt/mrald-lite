<%@ page import="org.mitre.mrald.util.Config" %>
<!DOCTYPE html PUBLIC "-//w3c//dtd html 3.2//en">

<%@ taglib uri="/WEB-INF/mrald.tld" prefix="mrald"%>
<mrald:validate />
<html>
  <head>
    <META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE">
    <meta name="generator" content="HTML Tidy, see www.w3.org">
    <title>
      Upload Form
    </title>
    <%=Config.getProperty( "CSS" ) %>
  </head>
  <%    String formId = request.getParameter( "formid" );  %>
  <body>
    <form enctype='multipart/form-data' method="post" action="FormServer.jsp?action=upload&formid=<%=formId%>">
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
                    F o r m &nbsp; U p l o a d
                  </th>
                </tr>
                <tr>
                  <td style="padding:1em">
                    Please select the file that you wish to upload to the server: <input type="file" name="test">
                  </td>
                </tr>
              </table>
            </td>
          </tr>
        </table>
        <br>
         <input type="submit" value="upload">
      </center>
    </form>
  </body>
</html>


