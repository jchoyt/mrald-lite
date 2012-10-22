<%@ taglib uri="/WEB-INF/mrald.tld" prefix="mrald"%>
<%@ taglib uri="/WEB-INF/lattice.tld" prefix="lattice"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ page import="org.mitre.mrald.util.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<mrald:validate  doAdminCheck="yes" />
<html>
    <head>
        <title>Step #1: Describe the data source</title>
        <%=Config.getProperty( "CSS" )%>
    </head>
    <body>
        <mrald:validate doAdminCheck="yes" />
        <div id="header">
            <h1 class="headerTitle">
                <%=Config.getProperty( "TITLE" )%>
            </h1>
        </div>
        <div class="subHeader">
        <span class="doNotDisplay">Navigation:</span>
        MRALD - Finding the gems in your data |
        <a href="index.jsp" target="_top">Home</a>
        |
        <a href="mailto:mrald-dev-lists@lists.mitre.org">Contact MITRE</a>
        </div>
        <div class="faq" style="text-align:center">
            <form action="DataSourceWizard2.jsp" method="post">
            <h2>Describe the new data source:</h2>
            Name (no spaces)<font  color="#FF0000">*</font>
            <br />
            <input type="text" name="name" />
            <br />
            <br />
            Description<font  color="#FF0000">*</font>
            <br />
            <textarea name="description" rows="5" cols="50">Enter a short description here</textarea>
            <br />
            <br />
            Database Type<font  color="#FF0000">*</font>
            <br />
            <select name="dbType">
                <option value="other">User defined</option>
                <tags:jdbcTemplateList/>
            </select>
            <br />
            <br />
            <input type="Submit" value="Continue" /> <input type="button" value="Cancel" onclick="location='index_admin.jsp'"/>
            <br /><br />
            <font  color="#FF0000">*</font><b>Fields marked with an asterisk are required</b>
            </form>
        </div>
    </body>
</html>

