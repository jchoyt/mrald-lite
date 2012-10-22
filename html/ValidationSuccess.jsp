<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page errorPage="ErrorHandler.jsp" %>
<%@ page import="org.mitre.mrald.util.Config" %>
<%@ taglib uri="/WEB-INF/mrald.tld" prefix="mrald"%>
<META HTTP-EQUIV="REFRESH" CONTENT="5;URL=index.jsp">
<html>
  <head>
    <meta name="generator" content="HTML Tidy, see www.w3.org">
    <META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE">
    <title>
      Account Validation Completed!
    </title>
          <%=Config.getProperty( "CSS" ) %>
 </head>
  <body>
  <br></br>
  <br></br>
  <br></br>
  <br></br>
         <CENTER>
      <table summary="" width="650" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td class="bord">
            <table summary="" width="100%" border="0" cellspacing="1" cellpadding="2">
              <tr>
                <th>Account Validation Completed!</th></tr>
           <TR><td style="padding:1em" align="center">
           Your account has been validated successfully and you are being redirected to the Log-In page. 
	   You will also receive an informative email explaining most of the important features of our system
	   as well as some helpful pointers for using the system effective.
             </td></TR></table></td></tr></table><br>

        </CENTER>
  </body>
</html>


