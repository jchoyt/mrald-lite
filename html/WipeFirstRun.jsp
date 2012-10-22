<%@ page import="org.mitre.mrald.util.*,java.io.*" %><%
    String base_path = Config.getProperty( "BasePath" );
    //
    // Changing around the configuration files
    //
    File admin_jsp = new File( base_path, "Redirect.jsp" );
    if ( admin_jsp.exists() )
    {
        // Delete the old file, FirstRun.jsp
        File old_admin = new File( base_path, "FirstRun.jsp" );
        old_admin.delete();
        // Create a new copy of the FirstRun.jsp
        File new_file = new File( base_path, "FirstRun.jsp" );
        // Changing the name of the Redirect to the FirstRun.jsp
        admin_jsp.renameTo( new_file );
        // Changing the modification date so Tomcat will recompile it
        new_file.setLastModified( System.currentTimeMillis() + 1000000 );
    }
    response.sendRedirect(Config.getProperty("URL" ));
%>

