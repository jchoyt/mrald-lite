
<%@ page import = "java.lang.String" %>
<%@ page import = "java.sql.Statement "%>
<%@ page import = "java.sql.ResultSet "%>
<%@ page import = "org.mitre.mrald.control.ThreadTracker" %>
<%@ page import = "org.mitre.mrald.control.UserThread" %>
<%@ page import = "org.mitre.mrald.util.Config" %>
<%@ page import = "org.mitre.mrald.util.MraldConnection" %>
<%@ page import = "org.mitre.mrald.util.MraldOutFile" %>
<%
    long startTime =Long.valueOf(request.getParameter("start")).longValue();
    ThreadTracker.killThread(startTime);
    response.sendRedirect(Config.getProperty("URL" ));
%>
