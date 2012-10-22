<%@ taglib uri="/WEB-INF/mrald.tld" prefix="mrald"%>
<%@ taglib uri="/WEB-INF/lattice.tld" prefix="lattice"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ page import="org.mitre.mrald.util.*,java.io.*,java.util.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<mrald:validate  doAdminCheck="yes" />
<%
    Properties props = new Properties();
    String name = WebUtils.getRequiredParameter( request, "name");
    String description = WebUtils.getRequiredParameter( request, "description");
    String dbserver = WebUtils.getRequiredParameter( request, "url");
    String dblogin = WebUtils.getRequiredParameter( request, "login");
    String dbpassword = WebUtils.getRequiredParameter( request, "pd");
    String dbdriver = WebUtils.getRequiredParameter( request, "driver");
    String schema = WebUtils.getRequiredParameter( request, "schema");
    String db_name = WebUtils.getRequiredParameter( request, "db-name");

    props.setProperty("SCHEMA", schema);
    props.setProperty("DBSERVER",dbserver);
    props.setProperty("DBLOGIN",dblogin);
    props.setProperty("DBDRIVER",dbdriver);
    props.setProperty("DBPASSWORD",dbpassword);
    props.setProperty("DESCRIPTION",description);
    
    props.setProperty("DBNAME",db_name);//GH for MultiDB
    //TODO - add DEFER to defer metadata loading

    File outputFile = new File(Config.getProperty("BasePath") + "/WEB-INF/props", "db_" + name + ".props" );
    OutputStream os = new FileOutputStream( outputFile );
    props.store( os, name + " datasource - created:" );

    String another = WebUtils.getRequiredParameter( request, "another" );
    if( another.equals("true") )
    {
        response.sendRedirect( "DataSourceWizard.jsp" );
    }
    else
    {
        MetaData.reload();
        response.sendRedirect( "index_admin.jsp" );
    }
%>

