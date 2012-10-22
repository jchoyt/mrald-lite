<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%@ page import="org.mitre.mrald.util.Config" %>
<%@ taglib uri="/WEB-INF/mrald.tld" prefix="mrald"%>
<META HTTP-EQUIV="REFRESH" CONTENT="3;URL=index.jsp">
<html>
  <head>
    <meta name="generator" content="HTML Tidy, see www.w3.org">
    <META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE">
    <title>
      <%=Config.getProperty( "TITLE" ) %>
    </title>
          <%=Config.getProperty( "CSS" ) %>
 </head>
  <body>
     <div id="header">
      <h1 class="headerTitle">
        <%=Config.getProperty( "TITLE" ) %>
      </h1>
    </div>
   <script language="JavaScript1.2" type="text/javascript" src="<%=Config.getProperty( "BaseUrl" ) %>/navi.js"></script>
         <CENTER>
      <table summary="" width="650" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td class="bord">
            <table summary="" width="100%" border="0" cellspacing="1" cellpadding="2">
              <tr>
                <th>Insert Successful</th></tr>
           <TR><td style="padding:1em" align="center">
           Row has been created successfully.  Redirecting to the home page. . . .
             </td></TR></table></td></tr></table><br>

        </CENTER>
  </body>
</html>


