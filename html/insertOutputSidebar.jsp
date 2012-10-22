<%@ page import="org.mitre.mrald.util.Config" %>
<%@ taglib uri="/WEB-INF/lattice.tld" prefix="lattice"%>
<%
	if ( Config.getLatticeFactory().getUsingLatticeSecurityModel() )
	{
%>
  <div class="leftSideBar">
    <p class="sideBarTitle">Current COI</p>    
       <center>
       <lattice:showCOI />
       </center>
       <br/>
       </div>
<%
	}
%>	   
       <div class="leftSideBar">
      <p class="sideBarTitle">Insert Data Form</p>
      <p class="sideBarText">This form allows a user to insert new data within a table. Only one row of data can be inserted at a time. </a></p>
    </div>
    <div class="leftSideBar" style="text-align:left;">
    </div>
