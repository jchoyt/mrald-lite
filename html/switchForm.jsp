<%@ page import="org.mitre.mrald.util.*"
%><%-- this jsp should automatically switch between the simple and advanced
version of a form - with the assumption that simple forms are named exactly like
advanced forms, but have a -simple inserted before the .jsp--%><%
    MraldOutFile.appendToFile( Config.getProperty("LOGFILE"), request.getContextPath() );
    MraldOutFile.appendToFile( Config.getProperty("LOGFILE"), request.getServletPath() );
    String form = request.getHeader("referer");
    /* example form: http://127.0.0.1:8080/mrald//custom/jchoyt@mitre.org_1104728215476.jsp */
    String relativePath = form.substring(Config.getProperty("BaseUrl").length());
    StringBuffer newUrl = new StringBuffer(Config.getProperty("BaseUrl"));
    MraldOutFile.logToFile( Config.getProperty("LOGFILE"), "form:" + form);
    MraldOutFile.logToFile( Config.getProperty("LOGFILE"), "relativePath:" + relativePath);
    int simpleloc = relativePath.indexOf("-simple.jsp");
    if( simpleloc == -1 )
    {
        /* it's an advanced form  */
        int dotloc = relativePath.lastIndexOf('.');
        newUrl.append(relativePath.substring(0,dotloc));
        newUrl.append("-simple");
        newUrl.append(relativePath.substring(dotloc));
    }
    else
    {
        /* it's a simple form */
        newUrl.append(relativePath.substring(0,simpleloc));
        newUrl.append(".jsp");
    }
    MraldOutFile.logToFile( Config.getProperty("LOGFILE"), "newUrl:" + newUrl);
    //pageContext.forward(newUrl.toString());
    response.sendRedirect(newUrl.toString());
%>