<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%@ page import = "org.mitre.mrald.util.User"%>
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
    <META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE">
    <meta name="generator" content="HTML Tidy, see www.w3.org">
    <title>
      Register a new user
    </title>
    <SCRIPT LANGUAGE="Javascript">
        function checkForm(form)
        {
            var userName = form.userName.value;
            var old = form.password.value;
            var check = form.verifyPassword.value;

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
	    else if( (!check) )
            {
                alert ("\nYou must enter a new password continue.")
                return false;
            }
	    else if( old != check )
            {
                alert ("\nYou must enter matching passwords.")
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
    <CENTER><table cellpadding="0" cellspacing="0" border="0" width="45%">
    <tr>
    <td class="bord">
           <table cellspacing="1" cellpadding="3" border="0" width="100%">
           <tr>
           <th>
               User Registration
           </th>
           </tr>
		   <tr>
           <td valign="top">
           <table width="100%">
		   <CENTER>
                <tr><td>Email Address:<font  color="#FF0000">*</font></td><td><input type="text" name="userName" size="30" maxlength="80" value=""></input></td></tr>
                <tr><td>Password:<font  color="#FF0000">*</font></td><td><input type="Password" name="password" size="30" maxlength="80" value=""></input></td></tr>
                <tr><td>Renter Password:<font  color="#FF0000">*</font></td><td><input type="Password" name="verifyPassword" size="30" maxlength="80" value=""></input></td></tr>
				<tr><td colspan="2" align="center"><hr width="80%"></td></tr>
                <tr><td>First Name: </td><td><input type="text" name="firstName" size="30" maxlength="80" value=""></input></td></tr>
                <tr><td>Last Name: </td><td><input type="text" name="lastName" size="30" maxlength="80" value=""></input></td></tr>
                <tr><td>Company: </td><td><input type="text" name="company" size="30" maxlength="80" value=""></input></td></tr>
                <tr><td>Department: </td><td><input type="text" name="department" size="30" maxlength="80" value=""></input></td></tr>
				<tr><td>Address: </td><td><textarea name="address" cols="35" rows="5"></textarea></td></tr>
                <tr><td>City: </td><td><input type="text" name="city" size="30" maxlength="80" value=""></input></td></tr>
                <tr><td>State: </td><td><input type="text" name="state" size="30" maxlength="80" value=""></input></td></tr>
                <tr><td>Zip: </td><td><input type="text" name="zip" size="30" maxlength="80" value=""></input></td></tr>
                <tr><td>Country: </td><td><input type="text" name="country" size="30" maxlength="80" value=""></input></td></tr>
				<tr><td colspan="2"><BR><BR>For more information on our use of cookies, you may review <a href="cookie.jsp">our cookie policy</a>. You will receive a confirmation email after completing this process.  It will contain a key that will be necessary to confirm and use this system.</td></tr>
				<tr><td colspan="2"><font  color="#FF0000">*</font>Fields marked with an asterisk are required</td></tr>
				</CENTER>
          </td>
         </tr>
       </table>
       </td>
       </tr>
       </table>
       </td>
       </tr>
       </table>
       </CENTER>
      <div>
      <CENTER>
	  <input type="hidden" name="workflow" value="UserRegistration" />
	  <input type="hidden" name="pageurl" value="<%=pageurl%>">
	  <input type="hidden" name="loginAction" value="addUser">
	  <input type="submit" value="Register">
	  </CENTER>
     </div>          
    </form>
  </body>
</html>


