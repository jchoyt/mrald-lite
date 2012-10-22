
<%@ page import = "java.lang.String" %>
<%@ page import = "java.sql.Statement "%>
<%@ page import = "java.sql.ResultSet "%>
<%@ page import = "org.mitre.mrald.control.ThreadTracker" %>
<%@ page import = "org.mitre.mrald.control.UserThread" %>
<%@ page import = "org.mitre.mrald.util.Config" %>
<%@ page import = "org.mitre.mrald.util.MraldConnection" %>
<%@ page import = "org.mitre.mrald.util.MraldOutFile" %><%
    response.setContentType( "text/html" );
    response.setHeader( "Cache-Control", "no-cache" );
    ThreadTracker.clean();
%>
<%@ taglib uri="/WEB-INF/mrald.tld" prefix="mrald"%>
<mrald:validate />
<!DOCTYPE html PUBLIC '-//w3c//dtd html 3.2//en'>
<html>
  <head>
    <META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE">
    <meta name="generator" content="HTML Tidy, see www.w3.org">
    <title>
      Active queries submitted to the database
    </title>
    <%=Config.getProperty( "CSS" ) %>
  </head>
  <body>
    <div id="header">
      <h1 class="headerTitle">
        <%=Config.getProperty( "TITLE" ) %><br />Active User Queries
      </h1>
    </div>
    <h1>
    </h1>
    <script language="JavaScript1.2" type="text/javascript" src="<%=Config.getProperty( "BaseUrl" ) %>/navi.js"></script>
    <br>
    <table border='1' cellspacing='0' cellpadding='5' align="center">
      <tr>
        <th>
          User ID
        </th>
        <th>
          Started
        </th>
        <th>
          Elapsed time<br>(minutes)
        </th>
        <th>
          Query
        </th>
      </tr>
      <%
          String username="";
          Cookie[] cookies = request.getCookies();
          for ( int i = 0; i < cookies.length; i++)
          {
              String name = cookies[i].getName();
              if ( name.equalsIgnoreCase( Config.getProperty("cookietag") ) )
              {
                  username = cookies[i].getValue();
              }
          }
          UserThread[] threads = ThreadTracker.getUserThreads(username);
          for( int i=0; i<threads.length; i++)
          {
      %>
      <tr>
      <%=threads[i].toHtmlTableRow()%>
      </tr>
      <%
           }
      %>
    </table>
  </body>
</html>


