<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>

    <%@ page import="org.mitre.mrald.util.Config" %>
    <%@ taglib uri= "/WEB-INF/mrald.tld" prefix="mrald"%>
    <%=Config.getProperty("CSS")%>


    <SCRIPT LANGUAGE="JavaScript1.2" TYPE="text/javascript" SRC="<%=Config.getProperty( "BaseUrl" ) %>/tree.js"></SCRIPT>


<title>Salaries</title>

</head>
<body>
<div id="header">
<h1 class="headerTitle">Salaries</h1>
</div>
<div class="subHeader">
<span class="doNotDisplay">Navigation:</span>
      MRALD - Finding the gems in your data |


      <a href="<%=Config.getProperty("BaseUrl")%>/demo1-simple.jsp">Simple Form</a> |
      <a href="<%=Config.getProperty("BaseUrl")%>/index.jsp" target="_top">Home</a>


    |
    <a href="http://mitre.org">MITRE</a> | <a href="mailto:mrald-dev-list@lists.mitre.org">Contact Developers</a>
</div>

          <form method="POST" action="<%=Config.getProperty("BaseUrl")%>/FormSubmit" target="_BLANK" enctype="x-www-form-urlencoded">



      <div id ="outer" class="rightSideBar">
        <p class="sideBarTitle" style="margin-top:0;">Include in Results
<!--
		<a href="<%=Config.getProperty("BaseUrl")%>/UserGuide/UserGuide.html#UseStep5" target="_blank">
        <img alt="help" src="<%=Config.getProperty("BaseUrl")%>/images/green-help.jpeg" />
-->
        </a>
        </p>
        <p class="sideBarText">
        <input value="true" name="showQuery" type="checkbox" checked="checked" />
        The query
        <br />
        <input type="checkbox" name="showDuplicates" value="true" />
        Duplicate rows</p>
        <p class="sideBarTitle">Output size limit</p>
        <p class="sideBarText">
        	<input type="radio" value="none" name="outputSize" />
			None
			<br />
			<input type="radio" value="lines" name="outputSize" checked="checked" />
			<input name="outputLinesCount" type="text" size="6" value="500" />
			Lines
			<br />
			<input type="radio" value="mb" name="outputSize" />
			<input name="outputMBSize" type="text" size="5" value="1" />
			MB
		</p>
        <p class="sideBarTitle">Format</p>
        <p class="sideBarText">
        <strong>Browser</strong>
        <br />
        <input type="radio" name="Format" value="browserHtml" checked="checked" />
        HTML
	    <br />
        <input type="radio" name="Format" value="browserLinks" />
        HTML with Links
        <br />
        <input type="radio" name="Format" value="browserCsv" />
        CSV (
        <input name="browserFormatDelimiter" type="text" value="," size="2" />
        )
        <br />
        <input type="radio" name="Format" value="browserText" />
        Text (tab delimited)
        <br />
        <input type="radio" name="Format" value="XmlRaw" />
        XML
		<br />


	 </p>
	 <p class="sideBarTitle">Destination</p>

        <p class="sideBarText">
        <strong>File</strong>
        <br />
        <input type="radio" name="Format" value="fileCsv" />
        CSV (
        <input name="fileFormatDelimiter" type="text" value="," size="2" />
        )
        <br />
        <input type="radio" name="Format" value="fileText" />
        Text (tab delimited)</p>
        <p class="sideBarText">
        <input type="radio" name="Format" value="excel" />
        Excel</p>
      </div>


<input name="Schema" type="hidden" value="PUBLIC"/>
<input name="form" type="hidden" value="Salaries"/>
<input type="hidden" name="workflow" value="Building SQL"/>
<input type="hidden" name="Datasource" value="db_baseball.props"/>

<div class="leftSideBar">
<p class="sideBarTitle">Advanced Form</p>
        <p class="sideBarText">This form gives the user much more control than the simple version, however it is more complex to use.  For a simpler version, use the <a href="<%=Config.getProperty("BaseUrl")%>/switchForm.jsp"> simple version of this form</a></p>
      </div>

<div class="leftSideBar">

<p class="sideBarTitle">Include in output


        <a href="<%=Config.getProperty("BaseUrl")%>/UserGuide/UserGuide.html#UseStep3" target="_blank">
        <img alt="help" src="<%=Config.getProperty("BaseUrl")%>/images/green-help.jpeg" />
        </a>
      </p>

<input name="Select1" type="checkbox" value="Table:SALARIES~Field:YEARID~Order:1" onMouseOver="if (EnableStats.value == 1) {}" onFocus="if (EnableStats.value == 1) {this.click(), this.blur()}" onDblClick="if (EnableStats.value == 1) {this.click(), this.blur()}">Year</input>
<br/>
<input name="Select2" type="checkbox" value="Table:TEAMSFRANCHISES~Field:FRANCHNAME~Order:46" onMouseOver="if (EnableStats.value == 1) {}" onFocus="if (EnableStats.value == 1) {this.click(), this.blur()}" onDblClick="if (EnableStats.value == 1) {this.click(), this.blur()}" checked="">Team</input>
<br/>
<input name="Select3" type="checkbox" value="Table:TEAMS~Field:PARK~Order:47" onMouseOver="if (EnableStats.value == 1) {}" onFocus="if (EnableStats.value == 1) {this.click(), this.blur()}" onDblClick="if (EnableStats.value == 1) {this.click(), this.blur()}" >Park</input>
<br/>
<input name="Select4" type="checkbox" value="Table:PLAYERS~Field:NAMEFIRST~Order:56" onMouseOver="if (EnableStats.value == 1) {}" onFocus="if (EnableStats.value == 1) {this.click(), this.blur()}" onDblClick="if (EnableStats.value == 1) {this.click(), this.blur()}" checked="">First Name</input>
<br/>
<input name="Select5" type="checkbox" value="Table:PLAYERS~Field:NAMELAST~Order:57" onMouseOver="if (EnableStats.value == 1) {}" onFocus="if (EnableStats.value == 1) {this.click(), this.blur()}" onDblClick="if (EnableStats.value == 1) {this.click(), this.blur()}" checked="">Last Name</input>
<br/>
<input name="Select6" type="checkbox" value="Table:PLAYERS~Field:NAMENICK~Order:58" onMouseOver="if (EnableStats.value == 1) {}" onFocus="if (EnableStats.value == 1) {this.click(), this.blur()}" onDblClick="if (EnableStats.value == 1) {this.click(), this.blur()}" >Nick Name</input>
<br/>
<input name="Select7" type="checkbox" value="Table:PLAYERS~Field:BIRTHYEAR~Order:59" onMouseOver="if (EnableStats.value == 1) {}" onFocus="if (EnableStats.value == 1) {this.click(), this.blur()}" onDblClick="if (EnableStats.value == 1) {this.click(), this.blur()}">Birth Year</input>
<br/>
<input name="Select8" type="checkbox" value="Table:SALARIES~Field:SALARY~Order:60" onMouseOver="if (EnableStats.value == 1) {}" onFocus="if (EnableStats.value == 1) {this.click(), this.blur()}" onDblClick="if (EnableStats.value == 1) {this.click(), this.blur()}" checked="">Salary</input>
<br/>
<br/>


<p class="sideBarTitle">Sort Criteria


        <a href="<%=Config.getProperty("BaseUrl")%>/UserGuide/UserGuide.html#UseStep3" target="_blank">
        <img alt="help" src="<%=Config.getProperty("BaseUrl")%>/images/green-help.jpeg" />
        </a>
      </p>

      Sort by
      <select name="Sort1">
<option/>
<option value="Table:SALARIES~Field:YEARID~Order:1">Year</option>
<option value="Table:TEAMSFRANCHISES~Field:FRANCHNAME~Order:1">Team</option>
<option value="Table:TEAMS~Field:PARK~Order:1">Park</option>
<option value="Table:PLAYERS~Field:NAMEFIRST~Order:1">First Name</option>
<option value="Table:PLAYERS~Field:NAMELAST~Order:1">Last Name</option>
<option value="Table:PLAYERS~Field:NAMENICK~Order:1">Nick Name</option>
<option value="Table:PLAYERS~Field:BIRTHYEAR~Order:1">Birth Year</option>
<option value="Table:SALARIES~Field:SALARY~Order:1">Salary</option>
</select>
<input name="Sort1" type="checkbox" value="OrderType:DESC"/>Desc<br/>
      then by
      <select name="Sort2">
<option/>
<option value="Table:SALARIES~Field:YEARID~Order:2">Year</option>
<option value="Table:TEAMSFRANCHISES~Field:FRANCHNAME~Order:2">Team</option>
<option value="Table:TEAMS~Field:PARK~Order:2">Park</option>
<option value="Table:PLAYERS~Field:NAMEFIRST~Order:2">First Name</option>
<option value="Table:PLAYERS~Field:NAMELAST~Order:2">Last Name</option>
<option value="Table:PLAYERS~Field:NAMENICK~Order:2">Nick Name</option>
<option value="Table:PLAYERS~Field:BIRTHYEAR~Order:2">Birth Year</option>
<option value="Table:SALARIES~Field:SALARY~Order:2">Salary</option>
</select>
<input name="Sort2" type="checkbox" value="OrderType:DESC"/>Desc<br/>
      then by
      <select name="Sort3">
<option/>
<option value="Table:SALARIES~Field:YEARID~Order:3">Year</option>
<option value="Table:TEAMSFRANCHISES~Field:FRANCHNAME~Order:3">Team</option>
<option value="Table:TEAMS~Field:PARK~Order:3">Park</option>
<option value="Table:PLAYERS~Field:NAMEFIRST~Order:3">First Name</option>
<option value="Table:PLAYERS~Field:NAMELAST~Order:3">Last Name</option>
<option value="Table:PLAYERS~Field:NAMENICK~Order:3">Nick Name</option>
<option value="Table:PLAYERS~Field:BIRTHYEAR~Order:3">Birth Year</option>
<option value="Table:SALARIES~Field:SALARY~Order:3">Salary</option>
</select>
<input name="Sort3" type="checkbox" value="OrderType:DESC"/>Desc<br/>
</div>

<div id="main-copy">

<input name="Link1" type="hidden" value="PrimaryLink:PLAYERS.PLAYERID~SecondaryLink:SALARIES.PLAYERID"/>
<input name="Link2" type="hidden" value="PrimaryLink:TEAMS.TEAMID~SecondaryLink:SALARIES.TEAMID"/>
<input name="Link3" type="hidden" value="PrimaryLink:TEAMS.YEARID~SecondaryLink:SALARIES.YEARID"/>
<input name="Link5" type="hidden" value="PrimaryLink:TEAMS.FRANCHID~SecondaryLink:TEAMSFRANCHISES.FRANCHID"/>
<input name="Link4" type="hidden" value="PrimaryLink:TEAMS.LGID~SecondaryLink:SALARIES.LGID"/>
<input name="outputFormat1" type="hidden" value="fieldname:SALARY~nicename:Salary~type:Numeric~formatpattern:$#,##0.00"/>
<input name="outputFormat21" type="hidden" value="fieldname:YEARID~nicename:Year~type:Numeric"/>
<input name="outputFormat22" type="hidden" value="fieldname:FRANCHNAME~nicename:Team~type:String"/>
<input name="outputFormat23" type="hidden" value="fieldname:PARK~nicename:Park~type:String"/>
<input name="outputFormat24" type="hidden" value="fieldname:NAMEFIRST~nicename:First Name~type:String"/>
<input name="outputFormat25" type="hidden" value="fieldname:NAMELAST~nicename:Last Name~type:String"/>
<input name="outputFormat26" type="hidden" value="fieldname:NAMENICK~nicename:Nick Name~type:String"/>
<input name="outputFormat27" type="hidden" value="fieldname:BIRTHYEAR~nicename:Birth Year~type:Numeric"/>
<h1 style="border-top: none; padding-top: 0;margin-top:0em;">Filters

        <img alt="help" src="<%=Config.getProperty("BaseUrl")%>/images/green-help.jpeg" />
      </h1>
<br/>

<div id ="help" class="helpBox">
<p class="helpBoxTitle">Tutorial - Slide 6 : Drop Down Filters (Categorical Filters)</p>
<p class="helpBoxText">Categorical Filters allow the Users to filter data according to a value within a list.</p>
<p class="helpBoxText">Select the highlighted <i>League</i> list. Two values 'AL', American League or 'NL', National League, can be selected.
<p class="helpBoxText">In this case, select 'AL' from the list.</p>
<p class="helpBoxText">Now the clicking on <i>Retrieve Data</i> will show only salaries for players in the  AL League.</p>
<center><p class="helpBoxText"><button type="button" onclick="location='demo4.jsp'"><b>Previous Slide</b></button><button type="button" onclick="location='demo6.jsp'"><b>Next Slide</b></button></p></center>

</div>



<p class="filter">
<div class="highlight">
<strong>League</strong>
<br/>
<input type="hidden" name="FilterCategorical1" value="Table:SALARIES~Field:LGID~Operator:=~Type:"/>
<select name="FilterCategorical1">
<option value=""/>
            <mrald:dropDownList datasource="db_baseball.props" table="SALARIES" pkColumn="lgid" listColumn="lgid" /></select>
</div>
</p>


<p class="filter">

<strong>Salary greater than</strong>
<br/>
<input type="checkbox" name="Filter1" value="Value:1000000"/>$1,000,000
          <img alt="" src="<%=Config.getProperty("BaseUrl")%>/images/spacer.gif" width="10" height="1"/>
        <input type="checkbox" name="Filter1" value="Value: 2500000"/> $2,500,000
          <img alt="" src="<%=Config.getProperty("BaseUrl")%>/images/spacer.gif" width="10" height="1"/>
			<input type="checkbox" name="Filter1" value="Value: 10000000" checked=""/> $10,000,000
			<img alt="" src="<%=Config.getProperty("BaseUrl")%>/images/spacer.gif" width="10" height="1"/>
        <br/>
<input type="hidden" name="Filter1" value="Table:SALARIES~Field:SALARY~Operator:&gt;~Type:Numeric"/>
<br/>Others:
<input type="text" name="Filter1" size="5"/>
<input type="text" name="Filter1" size="5"/>
<input type="text" name="Filter1" size="5"/>

</p>

<strong>General Filters</strong> - Select field, operator, and filter value<br/>
<select name="Filter2">
<option/>
<option value="Table:SALARIES~Field:YEARID~Type:Numeric">Year</option>
<option value="Table:TEAMSFRANCHISES~Field:FRANCHNAME~Type:String">Team</option>
<option value="Table:TEAMS~Field:PARK~Type:String">Park</option>
<option value="Table:PLAYERS~Field:NAMEFIRST~Type:String">First Name</option>
<option value="Table:PLAYERS~Field:NAMELAST~Type:String">Last Name</option>
<option value="Table:PLAYERS~Field:NAMENICK~Type:String">Nick Name</option>
<option value="Table:PLAYERS~Field:BIRTHYEAR~Type:Numeric">Birth Year</option>
<option value="Table:SALARIES~Field:SALARY~Type:Numeric">Salary</option>
</select>
<select name="Filter2">
<option/>
<option value="Operator:=">=
      </option>
<option value="Operator:!=">Not equal (!=)
      </option>
<option value="Operator:&lt;">&lt;
      </option>
<option value="Operator:&gt;">&gt;
      </option>
<option value="Operator:&lt;=">&lt;=
      </option>
<option value="Operator:&gt;=">&gt;=
      </option>
<option value="Operator:like">Contains
      </option>
<option value="Operator:starts">Starts With
      </option>
<option value="Operator:IN">IN
      </option>
<option value="Operator:NOT IN">NOT IN
      </option>
<option value="Operator:IS NULL">IS NULL
      </option>
<option value="Operator:IS NOT NULL">IS NOT NULL
      </option>
</select>
<input name="Filter2" type="text" size="22"/>
<br/>
<select name="Filter3">
<option/>
<option value="Table:SALARIES~Field:YEARID~Type:Numeric">Year</option>
<option value="Table:TEAMSFRANCHISES~Field:FRANCHNAME~Type:String">Team</option>
<option value="Table:TEAMS~Field:PARK~Type:String">Park</option>
<option value="Table:PLAYERS~Field:NAMEFIRST~Type:String">First Name</option>
<option value="Table:PLAYERS~Field:NAMELAST~Type:String">Last Name</option>
<option value="Table:PLAYERS~Field:NAMENICK~Type:String">Nick Name</option>
<option value="Table:PLAYERS~Field:BIRTHYEAR~Type:Numeric">Birth Year</option>
<option value="Table:SALARIES~Field:SALARY~Type:Numeric">Salary</option>
</select>
<select name="Filter3">
<option/>
<option value="Operator:=">=
      </option>
<option value="Operator:!=">Not equal (!=)
      </option>
<option value="Operator:&lt;">&lt;
      </option>
<option value="Operator:&gt;">&gt;
      </option>
<option value="Operator:&lt;=">&lt;=
      </option>
<option value="Operator:&gt;=">&gt;=
      </option>
<option value="Operator:like">Contains
      </option>
<option value="Operator:starts">Starts With
      </option>
<option value="Operator:IN">IN
      </option>
<option value="Operator:NOT IN">NOT IN
      </option>
<option value="Operator:IS NULL">IS NULL
      </option>
<option value="Operator:IS NOT NULL">IS NOT NULL
      </option>
</select>
<input name="Filter3" type="text" size="22"/>
<br/>
<select name="Filter4">
<option/>
<option value="Table:SALARIES~Field:YEARID~Type:Numeric">Year</option>
<option value="Table:TEAMSFRANCHISES~Field:FRANCHNAME~Type:String">Team</option>
<option value="Table:TEAMS~Field:PARK~Type:String">Park</option>
<option value="Table:PLAYERS~Field:NAMEFIRST~Type:String">First Name</option>
<option value="Table:PLAYERS~Field:NAMELAST~Type:String">Last Name</option>
<option value="Table:PLAYERS~Field:NAMENICK~Type:String">Nick Name</option>
<option value="Table:PLAYERS~Field:BIRTHYEAR~Type:Numeric">Birth Year</option>
<option value="Table:SALARIES~Field:SALARY~Type:Numeric">Salary</option>
</select>
<select name="Filter4">
<option/>
<option value="Operator:=">=
      </option>
<option value="Operator:!=">Not equal (!=)
      </option>
<option value="Operator:&lt;">&lt;
      </option>
<option value="Operator:&gt;">&gt;
      </option>
<option value="Operator:&lt;=">&lt;=
      </option>
<option value="Operator:&gt;=">&gt;=
      </option>
<option value="Operator:like">Contains
      </option>
<option value="Operator:starts">Starts With
      </option>
<option value="Operator:IN">IN
      </option>
<option value="Operator:NOT IN">NOT IN
      </option>
<option value="Operator:IS NULL">IS NULL
      </option>
<option value="Operator:IS NOT NULL">IS NOT NULL
      </option>
</select>
<input name="Filter4" type="text" size="22"/>
<br/>


<input type="hidden" name="EnableStats" value="0"/>
<h1 style="border-top: medium none; padding-top: 0pt;">Statistical Functions

        <a href="<%=Config.getProperty("BaseUrl")%>/UserGuide/UserGuide.html#UseStep4" target="_blank">
        <img alt="help" src="<%=Config.getProperty("BaseUrl")%>/images/green-help.jpeg" />
        </a>
      </h1>
      Select statistical function and field:<br/>
<select name="Stat1" onChange="&#10;            if (this.selectedIndex != 0) &#10;&#9;&#9;      {checkEnable(EnableStats); for (i=1; i!=9; i++) &#10;&#9;&#9;           { eval(&quot;Select&quot; + i).checked=false; } &#10;&#9;&#9;&#9;   self.status=&quot;Cannot select Output Data whilst statistical function selected.&quot;; } &#9;&#9;&#9;   &#10;&#9;&#9;       else {checkEnable(EnableStats);self.status=&quot;&quot;;} if (EnableStats.value == 1) {&#10;&#9;&#9;          Enable(  document.getElementById('chartId') ); } &#10;&#9;&#9;&#9;  else{ Disable(  document.getElementById('chartId') ); }&#10;&#9;&#9;&#9;  ">
<option/>
<option value="Function:Count(*)~Order:1~Table:SALARIES~Table:TEAMS~Table:TEAMS~Table:SALARIES~Table:PLAYERS~Table:PLAYERS~Table:PLAYERS~Table:PLAYERS">
            Count(*)
          </option>
<option value="Function:Count~Order:1">
            Count
          </option>
<option value="Function:Count(DISTINCT~Order:1">
            Count Distinct
          </option>
<option value="Function:Max~Order:1">
            Max
          </option>
<option value="Function:Min~Order:1">
            Min
          </option>
<option value="Function:Avg~Order:1">
            Average
          </option>
<option value="Function:Stddev~Order:1">
            Standard Deviation
          </option>
<option value="Function:Sum~Order:1">
            Sum
          </option>
<option value="Function:Variance~Order:1">
            Variance
          </option>
</select>
<select name="Stat1" onChange="&#10;               &#10;               &#10;&#9;&#9;      if (this.selectedIndex != 0) &#10;&#9;&#9;      {checkEnable(EnableStats); for (i=1; i!=9; i++) &#10;&#9;&#9;           { eval(&quot;Select&quot; + i).checked=false; } &#10;&#9;&#9;&#9;   self.status=&quot;Cannot select Output Data whilst statistical function selected.&quot;; } &#9;&#9;&#9;   &#10;&#9;&#9;       else {checkEnable(EnableStats);self.status=&quot;&quot;;} if (EnableStats.value == 1) {&#10;&#9;&#9;          Enable(  document.getElementById('chartId') ); } &#10;&#9;&#9;&#9;  else{ Disable(  document.getElementById('chartId') ); }&#10;&#9;&#9;      ">
<option/>
<option value="Table:SALARIES~Field:YEARID~Order:1~Type:Numeric">Year</option>
<option value="Table:PLAYERS~Field:BIRTHYEAR~Order:1~Type:Numeric">Birth Year</option>
<option value="Table:SALARIES~Field:SALARY~Order:1~Type:Numeric">Salary</option>
</select>
<br/>
<select name="Stat2" onChange="&#10;            if (this.selectedIndex != 0) &#10;&#9;&#9;      {checkEnable(EnableStats); for (i=1; i!=9; i++) &#10;&#9;&#9;           { eval(&quot;Select&quot; + i).checked=false; } &#10;&#9;&#9;&#9;   self.status=&quot;Cannot select Output Data whilst statistical function selected.&quot;; } &#9;&#9;&#9;   &#10;&#9;&#9;       else {checkEnable(EnableStats);self.status=&quot;&quot;;} if (EnableStats.value == 1) {&#10;&#9;&#9;          Enable(  document.getElementById('chartId') ); } &#10;&#9;&#9;&#9;  else{ Disable(  document.getElementById('chartId') ); }&#10;&#9;&#9;&#9;  ">
<option/>
<option value="Function:Count(*)~Order:2~Table:SALARIES~Table:TEAMS~Table:TEAMS~Table:SALARIES~Table:PLAYERS~Table:PLAYERS~Table:PLAYERS~Table:PLAYERS">
            Count(*)
          </option>
<option value="Function:Count~Order:2">
            Count
          </option>
<option value="Function:Count(DISTINCT~Order:2">
            Count Distinct
          </option>
<option value="Function:Max~Order:2">
            Max
          </option>
<option value="Function:Min~Order:2">
            Min
          </option>
<option value="Function:Avg~Order:2">
            Average
          </option>
<option value="Function:Stddev~Order:2">
            Standard Deviation
          </option>
<option value="Function:Sum~Order:2">
            Sum
          </option>
<option value="Function:Variance~Order:2">
            Variance
          </option>
</select>
<select name="Stat2" onChange="&#10;               &#10;               &#10;&#9;&#9;      if (this.selectedIndex != 0) &#10;&#9;&#9;      {checkEnable(EnableStats); for (i=1; i!=9; i++) &#10;&#9;&#9;           { eval(&quot;Select&quot; + i).checked=false; } &#10;&#9;&#9;&#9;   self.status=&quot;Cannot select Output Data whilst statistical function selected.&quot;; } &#9;&#9;&#9;   &#10;&#9;&#9;       else {checkEnable(EnableStats);self.status=&quot;&quot;;} if (EnableStats.value == 1) {&#10;&#9;&#9;          Enable(  document.getElementById('chartId') ); } &#10;&#9;&#9;&#9;  else{ Disable(  document.getElementById('chartId') ); }&#10;&#9;&#9;      ">
<option/>
<option value="Table:SALARIES~Field:YEARID~Order:2~Type:Numeric">Year</option>
<option value="Table:PLAYERS~Field:BIRTHYEAR~Order:2~Type:Numeric">Birth Year</option>
<option value="Table:SALARIES~Field:SALARY~Order:2~Type:Numeric">Salary</option>
</select>
<br/>
<select name="Stat3" onChange="&#10;            if (this.selectedIndex != 0) &#10;&#9;&#9;      {checkEnable(EnableStats); for (i=1; i!=9; i++) &#10;&#9;&#9;           { eval(&quot;Select&quot; + i).checked=false; } &#10;&#9;&#9;&#9;   self.status=&quot;Cannot select Output Data whilst statistical function selected.&quot;; } &#9;&#9;&#9;   &#10;&#9;&#9;       else {checkEnable(EnableStats);self.status=&quot;&quot;;} if (EnableStats.value == 1) {&#10;&#9;&#9;          Enable(  document.getElementById('chartId') ); } &#10;&#9;&#9;&#9;  else{ Disable(  document.getElementById('chartId') ); }&#10;&#9;&#9;&#9;  ">
<option/>
<option value="Function:Count(*)~Order:3~Table:SALARIES~Table:TEAMS~Table:TEAMS~Table:SALARIES~Table:PLAYERS~Table:PLAYERS~Table:PLAYERS~Table:PLAYERS">
            Count(*)
          </option>
<option value="Function:Count~Order:3">
            Count
          </option>
<option value="Function:Count(DISTINCT~Order:3">
            Count Distinct
          </option>
<option value="Function:Max~Order:3">
            Max
          </option>
<option value="Function:Min~Order:3">
            Min
          </option>
<option value="Function:Avg~Order:3">
            Average
          </option>
<option value="Function:Stddev~Order:3">
            Standard Deviation
          </option>
<option value="Function:Sum~Order:3">
            Sum
          </option>
<option value="Function:Variance~Order:3">
            Variance
          </option>
</select>
<select name="Stat3" onChange="&#10;               &#10;               &#10;&#9;&#9;      if (this.selectedIndex != 0) &#10;&#9;&#9;      {checkEnable(EnableStats); for (i=1; i!=9; i++) &#10;&#9;&#9;           { eval(&quot;Select&quot; + i).checked=false; } &#10;&#9;&#9;&#9;   self.status=&quot;Cannot select Output Data whilst statistical function selected.&quot;; } &#9;&#9;&#9;   &#10;&#9;&#9;       else {checkEnable(EnableStats);self.status=&quot;&quot;;} if (EnableStats.value == 1) {&#10;&#9;&#9;          Enable(  document.getElementById('chartId') ); } &#10;&#9;&#9;&#9;  else{ Disable(  document.getElementById('chartId') ); }&#10;&#9;&#9;      ">
<option/>
<option value="Table:SALARIES~Field:YEARID~Order:3~Type:Numeric">Year</option>
<option value="Table:PLAYERS~Field:BIRTHYEAR~Order:3~Type:Numeric">Birth Year</option>
<option value="Table:SALARIES~Field:SALARY~Order:3~Type:Numeric">Salary</option>
</select>
<br/>
<br/>
          Group by
            <select name="Group4" onChange="&#10;        if (this.selectedIndex != 0) {Enable(EnableStats); for (i=1; i!=9; i++){ eval(&quot;Select&quot; + i).checked=false; } self.status=&quot;Cannot select Output Data whilst statistical function selected.&quot;; document.getElementById('chartId').disabled=false; return true} else {Disable(EnableStats),self.status=&quot;&quot;; document.getElementById('chartId').disabled=true; document.getElementById('chartId').checked=false; return true}&#10;      ">
<option/>
<option value="Table:SALARIES~Field:YEARID~Order:4">Year</option>
<option value="Table:TEAMSFRANCHISES~Field:FRANCHNAME~Order:4">Team</option>
<option value="Table:TEAMS~Field:PARK~Order:4">Park</option>
<option value="Table:SALARIES~Field:SALARY~Order:4">Salary</option>
<option value="Table:PLAYERS~Field:NAMEFIRST~Order:4">First Name</option>
<option value="Table:PLAYERS~Field:NAMELAST~Order:4">Last Name</option>
<option value="Table:PLAYERS~Field:NAMENICK~Order:4">Nick Name</option>
<option value="Table:PLAYERS~Field:BIRTHYEAR~Order:4">Birth Year</option>
</select>
            then by
            <select name="Group5" onChange="&#10;        if (this.selectedIndex != 0) {Enable(EnableStats); for (i=1; i!=9; i++){ eval(&quot;Select&quot; + i).checked=false; } self.status=&quot;Cannot select Output Data whilst statistical function selected.&quot;; document.getElementById('chartId').disabled=false; return true} else {Disable(EnableStats),self.status=&quot;&quot;; document.getElementById('chartId').disabled=true; document.getElementById('chartId').checked=false; return true}&#10;      ">
<option/>
<option value="Table:SALARIES~Field:YEARID~Order:5">Year</option>
<option value="Table:TEAMSFRANCHISES~Field:FRANCHNAME~Order:5">Team</option>
<option value="Table:TEAMS~Field:PARK~Order:5">Park</option>
<option value="Table:SALARIES~Field:SALARY~Order:5">Salary</option>
<option value="Table:PLAYERS~Field:NAMEFIRST~Order:5">First Name</option>
<option value="Table:PLAYERS~Field:NAMELAST~Order:5">Last Name</option>
<option value="Table:PLAYERS~Field:NAMENICK~Order:5">Nick Name</option>
<option value="Table:PLAYERS~Field:BIRTHYEAR~Order:5">Birth Year</option>
</select>
              then by
            <select name="Group6" onChange="&#10;        if (this.selectedIndex != 0) {Enable(EnableStats); for (i=1; i!=9; i++){ eval(&quot;Select&quot; + i).checked=false; } self.status=&quot;Cannot select Output Data whilst statistical function selected.&quot;; document.getElementById('chartId').disabled=false; return true} else {Disable(EnableStats),self.status=&quot;&quot;; document.getElementById('chartId').disabled=true; document.getElementById('chartId').checked=false; return true}&#10;      ">
<option/>
<option value="Table:SALARIES~Field:YEARID~Order:6">Year</option>
<option value="Table:TEAMSFRANCHISES~Field:FRANCHNAME~Order:6">Team</option>
<option value="Table:TEAMS~Field:PARK~Order:6">Park</option>
<option value="Table:SALARIES~Field:SALARY~Order:6">Salary</option>
<option value="Table:PLAYERS~Field:NAMEFIRST~Order:6">First Name</option>
<option value="Table:PLAYERS~Field:NAMELAST~Order:6">Last Name</option>
<option value="Table:PLAYERS~Field:NAMENICK~Order:6">Nick Name</option>
<option value="Table:PLAYERS~Field:BIRTHYEAR~Order:6">Birth Year</option>
</select>
<br/>
<span style="font-size:70%">*When grouping on a particular column, that column will automatically be included in the output</span>
<br/>
<div align="center">
<input type="submit" value="Retrieve Data"/>
<br/>
<br/>
<input type="reset" value="Reset Form"/>
</div>
</div>
      </form>


    </body>
</html>
