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
    
    <%=Config.getProperty( "CSS" ) %>
  </head>
  <body>
    <div id="header">
      <h1 class="headerTitle">
        <%=Config.getProperty( "TITLE" ) %> Work Flow Configuration
      </h1>
    </div>
    <div class="subHeader">
    <span class="doNotDisplay">Navigation:</span>
    MRALD - Finding the gems in your data |
    <a href="index.jsp" target="_top">Home</a>
    |
    <a href="http://mitre.org">MITRE</a> | <a href="mailto:mrald-dev-list@lists.mitre.org">Contact Developers</a>
    </div>
   <form action="FormSubmit" method="POST">
    <input type='hidden' name='form' value='Work Flow Admin'>
    <input type='hidden' name='workflow' value='Workflow Admin'>
        
      <div class="faq" style="text-align: center" width="400">
        <CENTER>
          <h2>Work Flow Configuration</H2>
	  Below you will find the values in the Work Flow file.  This file is pivotal<BR>
          to the efficient and truthful operation of the MRALD system.   Please be sure to be careful<BR>
          of the entries in this file.  If you have any questions or concerns, please read the instructions<BR>
          for the MRALD system.<BR><BR>
          
          <TABLE CELLSPACING="0" CELLPADDING="2" BORDER="0">
           <TR>
             
           </TR>
          <mrald:WorkFlowAdmin/>
           
          </TABLE><BR>
        <INPUT TYPE="SUBMIT" NAME="SubmitAdmin" VALUE="Commit Changes">
        <INPUT TYPE="SUBMIT" NAME="AddMoreSteps" VALUE="Add More Steps?">
        <INPUT TYPE="RESET" VALUE="Reset Form">
        <BR><BR>
        </CENTER>
      </div>
    </form>
  </body>
</html>


