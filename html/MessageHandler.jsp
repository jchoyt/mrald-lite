<!DOCTYPE html PUBLIC "-//w3c//dtd html 3.2//en">

<%@ page import="org.mitre.mrald.util.Config" %>
<%@ page import="org.mitre.mrald.util.MraldMessage" %>
<%
     /*
      *  Pull out all necessary variables for a generic login request.
      *  (All of these must be included in any submitting source)
      */
     String trigger = request.getParameter( "DeleteMessages" );
     
     if ( ! ( trigger == null || trigger.equals( null ) || trigger.equals( "" ) || trigger.equals( "null" ) ) ){
     	
	String[] ids = request.getParameterValues( "selectedmessage" );
	if ( ids != null )
		MraldMessage.setAsArchive( ids );
   	response.sendRedirect( Config.getProperty( "URL" ) );	
	
     } 
     
%>
