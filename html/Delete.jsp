<%@ taglib uri="/WEB-INF/mrald.tld" prefix="mrald"%>
<%@ page import="org.mitre.mrald.util.*" %>
<%@ page import="java.util.*" %>
<mrald:validate />
<%
    String tableName = request.getParameter( "tableName" );
    String datasource = WebUtils.getRequiredParameter( request, "datasource" );
%>
<!DOCTYPE html PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN'>
<html>
  <head>
    <META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE">
    <meta name="generator" content="HTML Tidy, see www.w3.org">
    <title>
      Update Table <%=tableName %>
    </title>
    <%=Config.getProperty( "CSS" ) %>
<script type="text/javascript">
</script>
  </head>
  <body>
    <center>
    <div id="header">
      <h1 class="headerTitle">
        <%=Config.getProperty( "TITLE" ) %>
      </h1>
    </div>
            <script language="JavaScript1.2" type="text/javascript" src="<%=Config.getProperty( "BaseUrl" ) %>/navi.js"></script>
      <a href="javascript:NewWindow('help/step1.html','acepopup','640','480','center','front');">Help With This Form?</a>
      <!-- Start Form and Workflow data -->
      <br>
      <form method='POST' action='FormSubmit' enctype='x-www-form-urlencoded' onsubmit="return checkForm( this )">
        <input type="hidden" name="form" value="test"/>
        <input type="hidden" name="Datasource" value="<%=datasource%>"/>
        <input value="Delete SQL" name="workflow" type="hidden"/>
        <input type="hidden" name="Format" value="delete" ></input>
        <table cellpadding="0" cellspacing="0" border="0" width="95%">
          <tr>
            <td class="bord">
              <table cellpadding="5" cellspacing="1" border="0" width="100%">
                <tr>
                  <th class="title" COLSPAN="3">
                    Form Details.
                  </th>
		  <tr>
                </tr>
                <tr>
		  <th class="title" COLSPAN="3">
                    ----Warning! Deletion of this data may result in deletion of dependant data.--------
                  </th>
                </tr>
                <mrald:updateList action='Delete'/>
              </table>
            </td>
          </tr>
        </table>
        <br>
         <input type='submit' value='Delete'><br>
        <br>
         <input type='reset' value='Reset Form'>
      </form>
    </center>
  </body>
</html>
