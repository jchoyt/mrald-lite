<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>

    <%@ page import="org.mitre.mrald.util.Config" %>
    <%@ taglib uri="/WEB-INF/mrald.tld" prefix="mrald"%>
    <%@ page errorPage="/ErrorHandler.jsp"%>
    <%=Config.getProperty("CSS")%>

<title>Salaries</title>
</head>
<body>
<div id="header">
<h1 class="headerTitle">Salaries - Simple Form</h1>
</div>
<div class="subHeader">
<span class="doNotDisplay">Navigation:</span>
    MRALD - Finding the gems in your data |

      <span class="highlight"><a href="demo1.jsp">Advanced Form</a>
    </span>
    |

      <a href="<%=Config.getProperty("BaseUrl")%>/index.jsp">Home</a>

    |
    <a href="http://mitre.org">MITRE</a> | <a href="mailto:mrald-dev-list@lists.mitre.org">Contact Developers</a>
</div>

        <form method="POST" action="<%=Config.getProperty("BaseUrl")%>/FormSubmit" target="_BLANK" enctype="x-www-form-urlencoded">


       <jsp:include page="/simpleOutputSidebar.jsp" />

<input name="Schema" type="hidden" value="PUBLIC"/>
<input name="form" type="hidden" value="Salaries-simple"/>
<input type="hidden" name="workflow" value="Building SQL"/>
<input type="hidden" name="Datasource" value="db_baseball.props"/>
<div id="main-copy" style="margin-right:0px">
<input name="Link1" type="hidden" value="PrimaryLink:PLAYERS.PLAYERID~SecondaryLink:SALARIES.PLAYERID"/>
<input name="Link2" type="hidden" value="PrimaryLink:TEAMS.TEAMID~SecondaryLink:SALARIES.TEAMID"/>
<input name="Link3" type="hidden" value="PrimaryLink:TEAMS.YEARID~SecondaryLink:SALARIES.YEARID"/>
<input name="Link4" type="hidden" value="PrimaryLink:TEAMS.LGID~SecondaryLink:SALARIES.LGID"/>
<input name="Link5" type="hidden" value="PrimaryLink:TEAMS.FRANCHID~SecondaryLink:TEAMSFRANCHISES.FRANCHID"/>
<input name="outputFormat1" type="hidden" value="fieldname:SALARY~nicename:Salary~type:Numeric~formatpattern:$#,##0.00"/>
<input name="outputFormat21" type="hidden" value="fieldname:YEARID~nicename:Year~type:Numeric"/>
<input name="outputFormat22" type="hidden" value="fieldname:FRANCHNAME~nicename:Team~type:String"/>
<input name="outputFormat23" type="hidden" value="fieldname:PARK~nicename:Park~type:String"/>
<input name="outputFormat24" type="hidden" value="fieldname:NAMEFIRST~nicename:First Name~type:String"/>
<input name="outputFormat25" type="hidden" value="fieldname:NAMELAST~nicename:Last Name~type:String"/>
<input name="outputFormat26" type="hidden" value="fieldname:NAMENICK~nicename:Nick Name~type:String"/>
<input name="outputFormat27" type="hidden" value="fieldname:BIRTHYEAR~nicename:Birth Year~type:Numeric"/>
<input name="Select2" type="hidden" value="Table:TEAMSFRANCHISES~Field:FRANCHNAME~Order:46"/>
<input name="Select3" type="hidden" value="Table:TEAMS~Field:PARK~Order:47"/>
<input name="Select4" type="hidden" value="Table:SALARIES~Field:SALARY~Order:60"/>
<input name="Select5" type="hidden" value="Table:PLAYERS~Field:NAMEFIRST~Order:56"/>
<input name="Select6" type="hidden" value="Table:PLAYERS~Field:NAMELAST~Order:57"/>
<input name="Select7" type="hidden" value="Table:PLAYERS~Field:NAMENICK~Order:58"/>
<h1 style="border-top: none; padding-top: 0;margin-top:0em;">Filters

        <img alt="help" src="<%=Config.getProperty("BaseUrl")%>/images/green-help.jpeg" />
      </h1>
<br/>

<div id ="help" class="helpBox" style="right:0px">
<p class="helpBoxTitle">Tutorial - Slide 1 : Simple Form</p>
<p class="helpBoxText">A form allows a User to easily retrieve data from the database. </p>
<p class="helpBoxText">The Forms can be either a <i>Simple Form</i> or an <i>Advanced Form</i>. The Form shown is a <i>Simple Form</i>. </p>
<p class="helpBoxText">Pressing the Retrieve Data will automatically allow for data to be retrieved from the database.  The data retrieved will show the Salary data for all players earning more than $10 million dollars for year 2004.  For the purposes of this demo, results will be shown in a separate window, which you can then close and continue on with the demonstration..</p>
<p class="helpBoxText">To have greater control over what data is retrieved, or to examine statistics about the data, an <i>Advanced Form</i> is provided.</p>
<p class="helpBoxText">Clicking on the Advanced Form button highlighted above - will display this form and provide information on how to use it.</p>

</div>


<p class="filter">
<strong>League</strong>
<br/>
<input type="hidden" name="FilterCategorical1" value="Table:SALARIES~Field:LGID~Operator:=~Type:"/>
<select name="FilterCategorical1">
<option value=""/>
            <mrald:dropDownList datasource="db_baseball.props" table="SALARIES" pkColumn="lgid" listColumn="lgid" /></select>
</p>
<p class="filter">
<strong>Salary greater than</strong>
<br/>
<input type="checkbox" name="Filter1" value="Value:1000000"/>1000000
          <img alt="" src="<%=Config.getProperty("BaseUrl")%>/images/spacer.gif" width="10" height="1"/>
        <input type="checkbox" name="Filter1" value="Value: 2500000"/> 2500000
          <img alt="" src="<%=Config.getProperty("BaseUrl")%>/images/spacer.gif" width="10" height="1"/>
        <input type="checkbox" name="Filter1" value="Value: 10000000" checked="" /> 10000000
          <img alt="" src="<%=Config.getProperty("BaseUrl")%>/images/spacer.gif" width="10" height="1"/>
        <input type="hidden" name="Filter1" value="Table:SALARIES~Field:SALARY~Operator:&gt;~Type:Numeric"/>
</p>
<strong>General Filters</strong> - Select field, operator, and filter value<br/>
<select name="Filter2">
<option/>
<option value="Table:SALARIES~Field:YEARID~Type:Numeric" selected="">Year</option>
<option value="Table:TEAMSFRANCHISES~Field:FRANCHNAME~Type:String" >Team</option>
<option value="Table:TEAMS~Field:PARK~Type:String">Park</option>
<option value="Table:PLAYERS~Field:NAMEFIRST~Type:String">First Name</option>
<option value="Table:PLAYERS~Field:NAMELAST~Type:String">Last Name</option>
<option value="Table:PLAYERS~Field:NAMENICK~Type:String">Nick Name</option>
<option value="Table:PLAYERS~Field:BIRTHYEAR~Type:Numeric">Birth Year</option>
<option value="Table:SALARIES~Field:SALARY~Type:Numeric">Salary</option>
</select>
<select name="Filter2">
<option/>
<option value="Operator:=" selected="">=
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
<input name="Filter2" type="text" size="22" value="2004"></input>
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
<br/>
<div align="center">
	<span class="highlight">
		<input type="submit" value="Retrieve Data"/>

	</span>
	<b> <- Click Here</b>
<br/>
<br/>
<input type="reset" value="Reset Form"/><b></b>
</div>

      </form>
    </body>
</html>
