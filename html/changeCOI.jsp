<%@ taglib uri="/WEB-INF/mrald.tld" prefix="mrald"%>
<%@ taglib uri="/WEB-INF/lattice.tld" prefix="lattice"%>

<%@ page import="org.mitre.mrald.util.*" %>
<mrald:validate doCoiCheck="yes" />
<!DOCTYPE html PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN'>
    <meta name="generator" content="HTML Tidy, see www.w3.org">
    <title>
      <%=Config.getProperty( "TITLE" ) %> Change My COI
    </title>
   <%=Config.getProperty( "CSS" ) %>
    <div id="header">
      <h1 class="headerTitle">
        <%=Config.getProperty( "TITLE" ) %> Change My COI
      </h1>
    </div>
    <div class="subHeader">
    <span class="doNotDisplay">Navigation:</span>
    MRALD - Finding the gems in your data |
    <a href="index.jsp" target="_top">Home</a>
    |
    <a href="http://mitre.org">MITRE</a> | <a href="mailto:mrald-dev-list@lists.mitre.org">Contact Developers</a>
    </div>
    <form action="FormSubmit" method="post" enctype="x-www-form-urlencoded" onSubmit="return checkForm(this)">
	<%
        User mraldUser = (User)session.getAttribute(Config.getProperty("cookietag"));
    %>
        <input type="hidden" name="userName"  value="<%=mraldUser.getEmail()%>"/>
        <input type="hidden" name="loginAction" value="changeCOI"/> 
    <center>
      <br>
      <table width="600" border="1">
       <tr>
          <th>Change My COI</th>
		</tr>
		<tr>
		<td align="center">
		<lattice:groupList listOnly='true'/>
		</td>
	   </tr>
	  </table>
      <br/>
      <center>
	  <input type="hidden" name="workflow" value="UserLogin" />
	  <input type="submit" value="Change COI" />
	  </center>
      <br/>
      <lattice:showCOI />
    </center>
   </form>
</meta>

 

