<%@ tag import="java.io.*,java.util.*,org.mitre.mrald.taglib.*,org.mitre.mrald.util.*,java.util.logging.*,org.mitre.mrald.admin.*" %><%
    StringBuilder ret = new StringBuilder();
    String base_path = Config.getProperty("BasePath");
    File config_dir = new File( base_path, "WEB-INF/props" );
    String[] files = config_dir.list( new PropsFilter() );
    Arrays.sort( files );
    %>
<h1 id="introduction" style="border-top: none; padding-top: 0;">Configuration</h1>
<div id="AdminList" style="display:block;margin:5px 25px;">
    <%
    for ( int i = 0; i < files.length; i++ ){%>
        <a href="AdminConfig.jsp?filename=<%=files[i]%>">
            <%=files[i]%>
        </a>
        <br />
    <%}
        //Add the workflow Admin. ALthough as this does not go through the AdminConfig.jsp
        //so requires addition.
    %>
    <a href="WorkflowAdmin.jsp">Work Flow</a>
    <br />
</div>

<h1>Backups</h1>
<div id="Backups" style="display:block;margin:5px 25px;">
    <a href="AdminControl.jsp?action=createBackups">Create Backups</a>
    <br />
    <%
    for(AdminUtil.BackupType type : AdminUtil.BackupType.values() )
    {
        String zipFileName = AdminUtil.getBackupFile( type );
        File file = new File( zipFileName );
        String justFileName = file.getName();
        if( file.exists() )
        {
            %>
            &nbsp; - <a href="<%=justFileName%>"><%=justFileName%> (<%=file.length()/1024%> kB, <%=new Date(file.lastModified())%>)</a>
            <br />
            <%
        }
    }
    %>
</div>

<h1>Administrator Tools</h1>
<div id="AdminTools" style="display:block;margin:5px 25px;">
    <a href="NewsAdmin.jsp">News Editor</a>
    <br />
    <a href="PeopleManager.jsp">People Manager</a>
    <br />
    <a href="publishForms.jsp">Publish Forms</a>
    <br />
    <a href="publicForms.jsp">Manage Public Forms</a>
    <br />
    <br />
    <!--link to the Lattice Server -->
    <a href="FormBuilder.jsp?action=InsertFormBuilder.jsp">Insert Form Builder</a>
    <br />
    <a href="FormBuilder.jsp?action=UpdateFormBuilder.jsp">Update Form Builder</a>
    <br />
    <!--add a links to manage databases -->
    <br />
    <a href="DataSourceWizard.jsp">Add Data Source</a>
    <br />
    <a href="DeleteDatasource.jsp">Remove Data Source</a>
    <br />
    <a href="AdminControl.jsp?action=reloadDatabases">Reload All Database Metadata</a>
    <br />
    <!--add a link to the logs directory -->
    <br />
    <a href="<%=Config.getProperty( "LOGURL" ) %>">Manage Log Files</a>
    <br />
    <!--link to the Tomcat Manager Interface -->
    <a href="manager/html/index.jsp">Tomcat Manager Interface (if on)</a><br />
    <a href="axis2-web">Axis2 Manager Interface</a><br/>
</div>
<!--
 :mode=jsp:tabSize=4:indentSize=4:noTabs=true:
 :folding=indent:collapseFolds=0:wrap=none:maxLineLen=80:
-->

