<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page errorPage="ErrorHandler.jsp" %>
<%@ page import="org.mitre.mrald.util.Config" %>
<%
    String key = request.getParameter( "key" );
    if ( key == null || key.equals( "null" ) || key.equals( null ) )
		key = "";
%>
<html>
  <head>
    <META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE">
    <meta name="generator" content="HTML Tidy, see www.w3.org">
    <title>
      Account Validation
    </title>
    <SCRIPT LANGUAGE="Javascript">
        function checkForm(form)
        {
            var userName = form.userName.value;
            var old = form.password.value;
            var key = form.key.value;

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
	    else if( (!key) )
            {
                alert ("\nYou must enter a validation key to continue.")
                return false;
            }
            return true;
        }
    </SCRIPT>
    <%=Config.getProperty( "CSS" ) %>
  </head>
  <body>
    <form action="FormSubmit" method="post" enctype="x-www-form-urlencoded" onSubmit="return checkForm(this)">
     <br><br>             
    <CENTER>
	<table cellpadding="0" cellspacing="0" border="0" width="35%">
    <tr>
    <td class="bord">
           <table cellspacing="1" cellpadding="3" border="0" width="100%">
           <tr>
           <th>
                Account Validation
           </th>
           </tr>
		   <tr>
           <td valign="top">
           	<table width="100%">
                <tr><td align='center'>Email Address: </td><td><input type="text" name="userName" size="30" maxlength="80" value=""></td></tr>
                <tr><td align='center'>Password: </td><td><input type="Password" name="password" size="30" maxlength="80" value=""></td></tr>
                <tr><td align='center'>Key: </td><td><input type="text" name="key" size="30" maxlength="80" value="<%=key %>"></td></tr>
			</table>
          </td>
		  </tr>
		  </table>
	</td>
	</tr>
	</table>
	</CENTER>
	<CENTER>
	<br>
	<input type="hidden" name="workflow" value="UserRegistration">
	<input type="hidden" name="loginAction" value="validateUser">
	<input type="submit" value="Validate Me As A User">
	</CENTER>
	</form>
  </body>
</html>


