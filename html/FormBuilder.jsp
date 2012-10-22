<%@ taglib uri="/WEB-INF/mrald.tld" prefix="mrald"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ page import="org.mitre.mrald.util.*" %>
<mrald:validate />
<%
    String action = WebUtils.getOptionalParameter(request, "action", "FormBuilder1.jsp");
    String isMultiDBStr = WebUtils.getOptionalParameter(request, "multiDB", "false");
    boolean isMultiDB = false;
    if (isMultiDBStr.equals("true"))
        isMultiDB = true;
%>
<!DOCTYPE html PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN'>
<html>
    <head>
    <% if (isMultiDB)
    	{
	%>
	<title>Step #1: Select all data sources that you wish to use
	<%
	}
	else
	{
	%>
        <title>Step #1: Select the data source to use</title>
<%}%>
	
	<%=Config.getProperty( "CSS" )%>
        <style>
        td
        {
            background-color: transparent;
            font-size: 100%;
            vertical-align: top;
        }
    </style>
    </head>
    <body>
        <mrald:validate />
        <div id="header">
            <h1 class="headerTitle">
                <jsp:expression>Config.getProperty( "TITLE" )</jsp:expression>
            </h1>
        </div>
        <div class="subHeader">
        <span class="doNotDisplay">Navigation:</span>
        MRALD - Finding the gems in your data |
        <a href="index.jsp" target="_top">Home</a>
        |
        <a href="mailto:mrald-dev-lists@lists.mitre.org">Contact MITRE</a>
        </div>
        <div class="faq" style="text-align: center">
            <form action="<%=action%>" method="post">
	<% if (isMultiDB)
    	{
	%>
		<h2>Select all data sources you wish to use for your new form:</h2>
              <% }else {%>
	      
                <h2>Choose a data source to use for your new form:</h2>
              <%}%>
		<center>
                    <table width="75%">
                        <tags:datasourceRadio />
                    </table>
                </center>
                <br></br>
                <input type="Submit" value="Continue" />
            </form>
        </div>
    </body>
</html>
