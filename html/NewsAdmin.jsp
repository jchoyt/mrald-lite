<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%@ page import="org.mitre.mrald.util.Config" %>
<%@ taglib uri="/WEB-INF/mrald.tld" prefix="mrald"%>
<%@ page import="org.mitre.mrald.util.Config" %>
<%
    String file_name = request.getParameter( "filename" );

    if ( file_name == null || file_name.equals( "" ) )
  {
       file_name = Config.PROPS_FILE;
  }
%>
<html>
  <head>
    <meta name="generator" content="HTML Tidy, see www.w3.org">
    <META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE">
    <title>
      News Adiministration
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
   <form action="FormSubmit" method="POST">
    <input type='hidden' name='form' value='News Admin'>
    <input type='hidden' name='workflow' value='News Admin'>
        
      <div class="faq" style="text-align: center">
        <CENTER>
          <h2>News Administration</H2>
	  Below you will find the News Item for your system.  This file is needed<BR>
          display the news items on the main page of your deployment.   Please be sure to be careful<BR>
          of the entries in this file.  If you have any questions or concerns, please read the instructions<BR>
          for the MRALD system.<BR><BR>
          
          <TABLE CELLSPACING="0" CELLPADDING="2" BORDER="0">
           <TR>
             
           </TR>
          <mrald:NewsAdmin/>
           
          </TABLE><BR>
        <INPUT TYPE="SUBMIT" NAME="SubmitAdmin" VALUE="Commit Changes">
        <INPUT TYPE="SUBMIT" NAME="AddMoreSteps" VALUE="Add More Items?">
        <INPUT TYPE="RESET" VALUE="Reset Form">
        <BR><BR>
        </CENTER>
      </div>
    </form>
  </body>
</html>


