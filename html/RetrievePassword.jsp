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
      Retrieve New Password
    </title>
    <SCRIPT LANGUAGE="Javascript">
        function checkForm(form)
        {
            var userName = form.userName.value;

            if( (!userName) )
            {
                alert ("\nYou must enter a valid email address to continue.")
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
    <CENTER><table cellpadding="0" cellspacing="0" border="0" width="35%">
    <tr>
    <td class="bord">
    	<table cellspacing="1" cellpadding="3" border="0" width="100%">
           <tr>
           <th>
                Retrieve Password
           </th>
           </tr>
		   <tr>
           <td valign="top">
		   <table width="100%">
                <tr><td><center>Email Address: </td><td><input type="text" name="userName" size="30" maxlength="80" value=""></input></td></center></tr>
				<tr><td colspan="2"><BR><BR>For security purposes, enter your email that you register with the system.<BR>A new password will be generated and sent to you via email.</td></tr>
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
	 <input type="hidden" name="workflow" value="UserLogin" />
	 <input type="hidden" name="loginAction" value="retrievePassword" />
     <input type="submit" value="Send Me A New Password" />
	 </CENTER>
     </div>          
    </form>
  </body>
</html>


