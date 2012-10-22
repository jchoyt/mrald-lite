<%@ page import="java.util.*,org.mitre.mrald.util.*,java.util.logging.*" %>
<%@ taglib uri="/WEB-INF/mrald.tld" prefix="mrald"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<!-- End of side bars -->
<!-- Main content -->

    <div class="leftSideBar">
    <p class="sideBarTitle">Private Information
        <a href="help/index-help.html#personal" target="_blank"><img alt="help" src="images/green-help.jpeg" /></a>
    </p>
    <a href="QueryHistory.jsp">Query History</a><br />
    <%
    if (Config.usingSecurity)
    {
        %>
        <a href="ChangePassword.jsp?pageurl=index.jsp">Change password</a>
        <%
    }
    %>
    <br />
    <a href="FormBuilder.jsp">Build a new form</a><br/>
    <a href="FormBuilder.jsp?multiDB=true&action=MultiDbFormBuilder1.jsp">Build a new Multi Database Form</a>
    <%-- <br />
    <a href="uploadForm.jsp?action=upload">Upload a new form</a> --%>
    <br />
    <a href="personalizedFormsLegend.jsp" onclick="window.open('personalizedFormsLegend.jsp', 'Legend', 'width=300,height=300,status=yes,scrollbars=yes,toolbar=yes,resizable' );return false;">Icon Legend</a>
    <br />
    <mrald:FormsList formType="Personal" />
    <br />
    <tags:personalInfo/>
    <br/>
    <p class="sideBarTitle">
    Tutorial
    </p>
    <p>
        <a href="demo/demo1-simple.jsp">Form Tutorial</a>
    </p>
    <br />
    <p class="sideBarTitle">
    Documentation
    <a href="help/index-help.html#documentation" target="_blank"><img alt="help" src="images/green-help.jpeg" /></a>
    </p>
    <p>
        <a href="UserGuide/UserGuide.html">User Guide</a>
        <br />
        <a href="help/lattice-help.html"> User Guide for COI and Data Sharing</a>
	<br/>
	<a href="legal/index.html">License</a>
        <br />
        <a href="legal/acknowledgements.html">Acknowledgments</a>
      </p>
     </div>


