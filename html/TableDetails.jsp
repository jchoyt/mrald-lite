<%@ taglib uri="/WEB-INF/mrald.tld" prefix="mrald"%><%@ page import="org.mitre.mrald.util.*,java.util.*,java.net.*" %>
<html>
    <head>
        <%=Config.getProperty( "CSS" ) %><%
          Map params = request.getParameterMap();
          String ds = WebUtils.getRequiredParameter(request, "datasource");
          Iterator iterKeys = params.keySet().iterator();
          String title="";
          while (iterKeys.hasNext())
          {
              String param = iterKeys.next().toString();
              String paramVal = "";

              if (!param.equals("Table") && !param.equals("datasource"))
                  if (  ( request.getParameter(param) != null ) || (!request.getParameter(param).equals("")) )
                  {
                      paramVal =  request.getParameter(param);
                      if (paramVal != null)
                        paramVal = URLEncoder.encode(paramVal, "UTF-8");
                      title= title + "<br />" +  param + " = " + paramVal;
                   }
          }
          %>
        <div id="header">
            <h1 class="headerTitle">
                <%=Config.getProperty( "TITLE" ) %>
            </h1>
        </div>
<script language="JavaScript" src="navi.js">
</script>
        <title><%=Config.getProperty( "TITLE" ) %>Link to data. This contains a listing of all data referenced <%=title %></title>
    </head>
    <body>
        <div class="floating-text">
            <h1><%=Config.getProperty( "TITLE" ) %>Link to data.</h1>
            <p>This contains a listing of all tables that reference the data
            <b>
                <%=title %>
            </b>
            </p>
        </div>
        <mrald:DisplayTable datasource="<%=ds%>"/>
    </body>
</html>


