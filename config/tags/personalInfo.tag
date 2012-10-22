<%@ tag import="org.mitre.mrald.util.*,javax.servlet.jsp.*" %>

<%
    User user = (User) getJspContext().getAttribute(Config.getProperty("cookietag"), PageContext.SESSION_SCOPE);
%>
<p style="text-align:right;">You are logged in as <b><%=user.getEmail()%></b><br />
If this is wrong, please <a href="logout.jsp">log&nbsp;out</a>
</p>
<%--
 * :mode=jsp:tabSize=4:indentSize=4:noTabs=true:
 * :folding=indent:collapseFolds=0:wrap=none:maxLineLen=80:
--%>
