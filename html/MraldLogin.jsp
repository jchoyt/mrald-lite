<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="org.mitre.mrald.util.*" %>
<%
    String badLogin = WebUtils.getOptionalParameter( request, "badLogin" );
    User mraldUser = (User)session.getAttribute(Config.getProperty("cookietag"));
    String userStr= "";
    Cookie[] cookies = request.getCookies();

    for ( int i = 0; cookies!=null && i < cookies.length; i++)
    {
        String name = cookies[i].getName();
        if ( name.equalsIgnoreCase( Config.getProperty("cookietag") ) )
        {
            userStr = cookies[i].getValue();
        }
    }
    if (mraldUser != null)
        userStr = mraldUser.getEmail();
%>
<html>
  <head>
    <meta http-equiv="CACHE-CONTROL" content="NO-CACHE" />
    <title><%=Config.getProperty( "TITLE" ) %> Log In </title>
	<script language="JavaScript1.2" type="text/javascript">
        function checkForm(form)
        {
            var userName = form.userName.value;
            var passWord = form.password.value;
          if( (!userName) )
            {
                alert ("\nYou must enter a valid email address to continue.")
                return false;
            }
        <% if ( Config.usingSecurity ) { %>
          else if( (!passWord) )
          {
            alert ("\nYou must enter a password.")
            return false;
          }
        <% } %>
            else return true;
        }
	</script>
    <link rel="stylesheet" href="mrald.css" type="text/css" />
    <link rel="icon" href="favicon.ico" type="image/x-icon">
    <link rel="shortcut icon" href="favicon.ico" type="image/x-icon">
  </head>
  <body onload="document.form.userName.focus();">
    <div id="header">
      <h1 class="headerTitle">Login</h1>
    </div>
    <div class="subHeader">
    <span class="doNotDisplay">Navigation:</span>
    MRALD - Finding the gems in your data |
    <a href="index.jsp" target="_top">Home</a>
    |
    <a href="http://mitre.org">MITRE</a> | <a href="mailto:mrald-dev-list@lists.mitre.org">Contact Developers</a>
    </div>
    <form action="FormSubmit" method="post" enctype="x-www-form-urlencoded" onSubmit="return checkForm(this)" name="form">
	  <input type="hidden" name="workflow" value="UserLogin" />
      <input type="hidden" name="loginAction" value="checkUser" />
      <center>
        <div class="floating-text" style="width:400px;" name="floatingbox">
        <% if( badLogin.equals("true") ) {%>
            <div style="background:#ff8f8f;padding:.5em;text-align:center;">Username and password do not match any registered user.</div>
        <%}%>
		<table border="0">
		<tr>
		<td>Email Address:</td>
		<td><input type="text" name="userName" size="30" maxlength="80" value="<%= userStr %>" /></td>
		</tr>
        <%  if ( Config.usingSecurity ) {  %>
		<tr>
        <td>Password:</td>
        <td><input type="password" name="password" size="30" maxlength="80" value="" /></td>
		</tr>
		</table>
        <br />
        Not a registered user?
        <a href="RegistrationForm.jsp">Register here</a>
        <br />
        Forgotten your password?
        <a href="RetrievePassword.jsp?pageurl=<%=Config.getProperty( "BaseUrl" )%>"> Request a new one</a>
        <% }
        else
        { %>
		</table>
        <% } %><%-- <br />For more information on our use of cookies, you may review <a href="cookie.jsp">our cookie policy</a>. --%></div>
        <input type="submit" value="Login" />
      </center>
    </form>
  </body>
</html>