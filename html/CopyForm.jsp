<!DOCTYPE html PUBLIC "-//IETF//DTD HTML 2.0//EN">
<%-- This jsp adds a cookie to the client machine containing their username and
inserts the user into the database.   --%>

<%@ taglib uri="/WEB-INF/mrald.tld" prefix="mrald"%>
<%@ page import="org.mitre.mrald.util.FormUtils" %>
<%@ page import = "org.mitre.mrald.util.User, org.mitre.mrald.util.Config"%>
<mrald:validate />
<%
  String[] shareuserId = request.getParameterValues( "shareUserId" );
  User mraldUser = (User)session.getAttribute(Config.getProperty( "cookietag" ));
  FormUtils fu = new FormUtils();
  int count = mraldUser.getFormIds().length;
  fu.copyForm( mraldUser.getEmail(), mraldUser.getFormIds()[0], shareuserId);
  response.sendRedirect( Config.getProperty("URL" ) );
%>

