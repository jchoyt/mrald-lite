<html>
  <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="org.mitre.mrald.util.Config" %>
<%@ page import="org.mitre.mrald.util.User" %>
<mrald:validate />
<head>
    <title>
    </title>
  </head>
  <body>
    <center>
    <applet code="org.mitre.mrald.graphics.AppletDBViewer.class" codebase='<%=Config.getProperty("BaseUrl") + "graphics"%>' archive="prefuse.jar,prefusex.jar,postgresql-jdbc.jar,servlet.jar,graphics-util.jar" alt="You can't run this applet - you need Java 1.4 or later" height="600" width="800">
      <param name="CODE" value="org.mitre.mrald.graphics.AppletDBViewer.class">
      <param name="type" value="application/x-java-applet;version=1.4">
      <param name="configFile" value="<%=Config.getProperty("BasePath")  + "graphics/"%>">
      <!--%=Config.getProperty("BaseUrl")%-->
      Browser is ignoring the applet tag
    </applet> <br>
    Now launching the DB viewer.  Use the browser back button when you are done or head <a href="/">home</a>
    </center><br>
    <br>
   </body>
</html>

