<%@ tag import="java.util.*,org.mitre.fmae.*,java.util.logging.*" %>


<%
    StringBuilder ret = new StringBuilder(  );
    for( String key : JdbcTemplates.getTemplateList() )
    {
        ret.append( "<option value=\"" );
        ret.append( key );
        ret.append( "\">" );
        ret.append( key );
        ret.append( "</option>\n");
    }
%>

<%=ret.toString()%>
<%--
 * :mode=jsp:tabSize=4:indentSize=4:noTabs=true:
 * :folding=indent:collapseFolds=0:wrap=none:maxLineLen=80:
--%>

