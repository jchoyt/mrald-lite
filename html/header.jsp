<%@ page import="org.mitre.mrald.util.*, java.util.*" %><%
    /* 
     *  This jsp creates a header with colored tabs according to the name name 
     *  of the file it is included in
     *
     *  Assumption!  Name of all files this header works for are of the form indexXXX.jsp
     *  where XXX is an entry in the tabs array below.
     */
    String[] tabs = { "", "_admin", "_explorer" };
    String[] names = { "Main", "Admin", "Explorer" };
    /*
     *  tabs listed in this list are considered admin-only tabs and will not be shown
     *  to other users
     */
    List adminTabs = new ArrayList();
    adminTabs.add( "_admin" );
    String url = request.getRequestURI();
    User user = ( User ) pageContext.getSession().getAttribute( Config.getProperty( "cookietag" ) );
    int usertype = user.getTypeId();
    int w = 100/tabs.length;
    /*
     *  get it down to just the name of the file (index, index_admin, etc.) 
     */
    String pageExt = url.substring(url.lastIndexOf('/') + 6, url.lastIndexOf('.'));
%>
    <table width="100%"><tr>

    <% for(int i=0; i<tabs.length; i++)
    {
        if( adminTabs.contains(tabs[i]) && usertype != User.ADMIN_USER )
            continue;
        if(pageExt.equals(tabs[i]))
        {
            out.print("\t<td align=\"center\" class=\"navitabs-selected\" width=\"" + w + "%\" onclick=\"location.href='index" + tabs[i] +".jsp'\">");
        }
        else 
        {
            out.print("<td align=\"center\" class=\"navitabs\" width=\"" + w +  "%\" onclick=\"location.href='index" + tabs[i] +".jsp'\">");
        }
        out.println("<a href=\"index" + tabs[i] +".jsp\">" + names[i] + "</a></td>");
    }
    %>
    </tr>
    </table>
    

