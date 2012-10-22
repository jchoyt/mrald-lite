<!DOCTYPE html PUBLIC "-//w3c//dtd html 3.2//en">

<%@ page import="org.mitre.mrald.util.Config" %>
<%@ page import="org.mitre.mrald.util.MraldMessage" %>
<%@ page import="org.mitre.mrald.util.MraldException" %>
<%
     /*
      *  Pull out all necessary variables for a generic login request.
      *  (All of these must be included in any submitting source)
      */
     String message_id = request.getParameter( "id" );
     String[] read_ids;
     String[] message_form;
     
     if ( message_id == null || message_id.equals( null ) || message_id.equals( "" ) || message_id.equals( "null" ) ) {
     
     	throw new MraldException( "No Message ID provided.  Invalid/Incorrect Request" );
     
     } else {
      	// This is simply to pull out the ID in a string array so it can be processed
	// by MraldMessage.setAsRead
	read_ids = request.getParameterValues( "id" );
	MraldMessage.setAsRead( read_ids );
	
	// This is needed to simply get the message title and body.
	// message_form[0] = id
	// message_form[1] = title
	// message_form[2] = body
	try {
		message_form = MraldMessage.getMessage( Integer.parseInt( message_id ) );
	} catch ( Exception e ) {
		e.fillInStackTrace();
		throw e;
	}

%>

<html>
  <head>
    <meta name="generator" content="HTML Tidy, see www.w3.org">
    <title>
      <%=message_form[1]%>
    </title>
    <%=Config.getProperty( "CSS" ) %>
  </head>
  <body>
    <center>
      <br>
      <br>
      <br>

      <table summary="" width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td class="bord">
            <table summary="" width="100%" border="0" cellspacing="1" cellpadding="2">
              <tr>
                <th>
		<%=message_form[1]%>
                </th>
              </tr>
              <tr>
                <td style="padding:1em">
		<%=message_form[2]%>
                </td>
              </tr>
            </table>
          </td>
        </tr>
      </table>
    </center>
  </body>
</html>

<%

      } 
     
%>

