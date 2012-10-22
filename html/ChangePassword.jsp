<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="org.mitre.mrald.util.User" %>
<%@ page import="org.mitre.mrald.util.Config" %>
<%
	String pageurl = request.getParameter( "pageurl" );
	User mraldUser = (User)session.getAttribute(Config.getProperty("cookietag"));
	String userStr= "";
	if (mraldUser != null)
		userStr = mraldUser.getEmail();
%>
<html>
  <head>
    <meta http-equiv="CACHE-CONTROL" content="NO-CACHE" />
    <meta name="generator" content="HTML Tidy, see www.w3.org" />
    <title>Change Password</title>
	<script language="Javascript" xml:space="preserve">
        function checkForm(form)
        {
            var userName = form.userName.value;
            var old = form.oldPassword.value;
            var newP = form.newPassword.value;
            var check = form.checkPassword.value;

            if( (!userName) )
            {
                alert ("\nYou must enter a valid email address to continue.")
                return false;
            }
            else
			if( (!old) )
            {
                alert ("\nYou must enter a valid password to continue.")
                return false;
            }
			else if( (!newP) )
            {
                alert ("\nYou must enter a new password continue.")
                return false;
            }
			else if( (!check) )
            {
                alert ("\nYou must reenter new password to continue.")
                return false;
            }
            return true;
        }
	</script>
    <%=Config.getProperty( "CSS" ) %>
  </head>
  <body>
    <div id="header">
      <h1 class="headerTitle">Change Password</h1>
    </div>
    <div class="subHeader">
    <span class="doNotDisplay">Navigation:</span>
    MRALD - Finding the gems in your data |
    <a href="index.jsp" target="_top">Home</a>
    |
    <a href="http://mitre.org">MITRE</a> | <a href="mailto:mrald-dev-list@lists.mitre.org">Contact Developers</a>
    </div>
    <form action="FormSubmit" method="post" enctype="x-www-form-urlencoded" onSubmit="return checkForm(this)">
      <center>
        <div class="floating-text" style="width:400px;">
		<table border="0">
		<tr>
		<td>Email Address:</td>
        <td><input type="text" name="userName" size="30" maxlength="80" value="<%= userStr %>" /></td>
		</tr>
		<tr>
        <td>Old Password:</td>
        <td><input type="Password" name="password" size="30" maxlength="80" value="" /></td>
		</tr>
		<tr>
        <td>New Password:</td>
        <td><input type="Password" name="newPassword" size="30" maxlength="80" value="" /></td>
		</tr>
		<tr>
        <td>Renter Password:</td>
        <td><input type="Password" name="checkPassword" size="30" maxlength="80" value="" /></td>
		</tr>
		<tr>
		<td align='center' colspan='2'>
		<input type="hidden" name="workflow" value="UserLogin" />
        <input type="hidden" name="loginAction" value="updateLogin" />
        <input type="submit" value="Change Password" />
		</td>
		</tr>
		</table>
        </div>
      </center>
    </form>
  </body>
</html>
