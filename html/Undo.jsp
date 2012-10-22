<%@ taglib uri="/WEB-INF/mrald.tld" prefix="mrald"%><%@ page import="org.mitre.mrald.util.Config" %>
<!DOCTYPE html PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN'>
<mrald:validate />
<HTML>
<HEAD>
  <META NAME="generator" CONTENT="HTML Tidy, see www.w3.org">
  <TITLE>MITRE Lattice Server</TITLE>
    <%=Config.getProperty( "CSS" ) %>
</HEAD>
<BODY>
<center>
  <form ENCTYPE="x-www-form-urlencoded" ACTION="LatticeServerUtils.jsp" METHOD="POST" target="_parent">

    <tr align="center">
     <td align="center"><input type="Submit" name="action" value="Undo Last Change" action="Undo"></input></td>
    </tr>
  </form>
 </center>
 </BODY>
</HTML>
