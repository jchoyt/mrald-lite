<%@ taglib uri="/WEB-INF/mrald.tld" prefix="mrald"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ page import="org.mitre.mrald.util.*" %>
<mrald:validate doAdminCheck="yes" />
<!DOCTYPE html PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN'>
<html>
    <head>
        <title>Delete Data Source</title>
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
            <form action="DeleteDatasourceAction.jsp" method="post">
                <h2>Choose a data source to delete:</h2>
                <center>
                    <table width="75%">
                        <tags:datasourceRadio />
                    </table>
                </center>
                <br></br>
                <input type="Submit" value="Delete" /><br />
                <input type="checkbox" name="imsure" value="yes" />Yes, I'm sure.
            </form>
        </div>
    </body>
</html>
