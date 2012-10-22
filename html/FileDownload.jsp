<%@ page import="org.mitre.mrald.util.Config" %>
<%@ page import="org.mitre.mrald.util.MraldError" %>
<%
  	String sessionID = request.getParameter( "SID" );
  	if ( sessionID == null || sessionID.equals( "null" ) || sessionID.equals( null ) )
		throw new MraldError("Invalid Page Request.  Go Back To Home Page and Try Again.");

  	String messageID = request.getParameter( "MID" );
  	if ( messageID == null || messageID.equals( "null" ) || messageID.equals( null ) )
		throw new MraldError("Invalid Page Request.  Go Back To Home Page and Try Again.");

%>
<html>
<head>
  <TITLE><%=Config.getProperty( "TITLE" ) %></TITLE>
  <META HTTP-EQUIV="REFRESH" CONTENT="1;URL='FormSubmit?download=file&workflow=Output Data&SID=<%=sessionID %>&MID=<%=messageID %>'">
  <%=Config.getProperty( "CSS" ) %>
</head>
<body>
    <div id="header">
		<h1 class="headerTitle"><%=Config.getProperty( "TITLE" ) %></h1>
    </div>
    <div class="subHeader">
		<span class="doNotDisplay">Navigation:</span>
		MRALD - Finding the gems in your data |
		<a href="index.jsp" target="_top">Home</a>  |
		<a href="http://mitre.org">MITRE</a> |
		<a href="mailto:mrald-dev-list@lists.mitre.org">Contact Developers</a>
    </div>
	<CENTER>
        <div class="floating-text" style="width:600px;">
		<p>If your download does not start momentarily, click to <a href="FormSubmit?download=file&workflow=Output Data&SID=<%=sessionID %>&MID=<%=messageID %>'">download the requested file</a></p>
        </div>
		<br></br>
	</CENTER>
</body>
</html>

