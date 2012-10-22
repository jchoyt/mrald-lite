<%@ taglib uri="/WEB-INF/mrald.tld" prefix="mrald"%><%@ taglib uri="/WEB-INF/lattice.tld" prefix="lattice"%><%@ page import="org.mitre.mrald.util.Config" %>
<!DOCTYPE html PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN'>
<mrald:validate />
<HTML>
<HEAD>
  <META NAME="generator" CONTENT="HTML Tidy, see www.w3.org">
  <TITLE>MITRE Lattice Server</TITLE>
    <link rel=stylesheet href=lattice.css type=text/css>
</HEAD>
<BODY>
<center>
  <form ENCTYPE="x-www-form-urlencoded" ACTION="ResetServer.jsp" METHOD="POST" target="_parent">

    <tr align="center">
     <td align="center"><input type="Submit" name="action" value="Reset" action="Reset"></input></td>
     <td align="center"><input type="Submit" name="action" value="Show Sample" action="Example"></input></td>     
     </tr>
  </form>
 </center>
 </BODY>
</HTML>
