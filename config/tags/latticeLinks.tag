<%@ tag import="java.util.*,org.mitre.mrald.util.*,java.util.logging.*" %>

<%
    if( !Config.useLattice() )
    {
        return;
    }
%>

<h1>Lattice Administration</h1>
<p>
    <div id="LatticeAdminTools" style="display:block;">
        <br />
        <img alt="" src="images/spacer.gif" width="25" height="1" />
        <a href="LatticeManagement.jsp">Lattice Server Manager</a>
        <img alt="" src="images/spacer.gif" width="25" height="1" />
        <a href="lattice-help.html#latticeSecurity" target="_blank">
            <img alt="help" src="images/green-help.jpeg" />
        </a>
        <br />
        <img alt="" src="images/spacer.gif" width="25" height="1" />
        <a href="LabelFormBuilder.jsp">Create Data Labeller Form</a>
        <img alt="" src="images/spacer.gif" width="25" height="1" />
        <a href="lattice-help.html#labelFormBuild" target="_blank">
            <img alt="help" src="images/green-help.jpeg" />
        </a>
        <br />
        <img alt="" src="images/spacer.gif" width="25" height="1" />
        <a href="LabelTables.jsp">Add Labelling to Table Data</a>
        <img alt="" src="images/spacer.gif" width="25" height="1" />
        <a href="lattice-help.html#labelTables" target="_blank">
            <img alt="help" src="images/green-help.jpeg" />
        </a>
        <br />
    </div>
</p>

<%--
 * :mode=jsp:tabSize=4:indentSize=4:noTabs=true:
 * :folding=indent:collapseFolds=0:wrap=none:maxLineLen=80:
--%>
