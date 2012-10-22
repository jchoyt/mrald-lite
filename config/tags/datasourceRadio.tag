<%@ tag import="java.util.*,org.mitre.mrald.util.*,java.util.logging.*" %>

<%
	Logger log = Logger.getLogger("datasourceRadio.tag");
	log.setLevel(Level.FINEST);
    User user = (User) getJspContext().getAttribute(Config.getProperty("cookietag"), PageContext.SESSION_SCOPE);

   String isMultiDBStr = WebUtils.getOptionalParameter(request, "multiDB", "false");
   boolean isMultiDB = false;
    if (isMultiDBStr.equals("true"))
        isMultiDB = true;
        
    Set<String> datasourceNames = MetaData.getDatasourceNames();
    String displayName;
    String description;
    for(String name:datasourceNames)
    {
        displayName = WebUtils.getDatasourceDisplayName(name);
        if( displayName.equals("admin") && user.getTypeId() < 3 )
        {
            continue;
        }
        description = MetaData.getDbProperties( name ).getProperty( "DESCRIPTION" );
        if( description==null )
        {
            description="No description provided.";
        }
        %>
            <tr>
                <td><input type=
<% if (isMultiDB)
     {
 %>"checkbox"
 <% }
        else
        {
   %>"radio" 
   <%}%>
                name="datasource" value="<%=name%>"/></td>
                <td><%=displayName%></td>
                <td>&nbsp; <%=description%></td>
            </tr>
            <tr>
                <td colspan="3"><hr width="85%"/></td>
            </tr>
        <%
    }%>

    <%--
 * :mode=jsp:tabSize=4:indentSize=4:noTabs=true:
 * :folding=indent:collapseFolds=0:wrap=none:maxLineLen=80:
--%>

