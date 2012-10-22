<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

    <%@ page import="org.mitre.mrald.util.*" %>
    <%@ taglib uri="/WEB-INF/mrald.tld" prefix="mrald"%>
    <%@ page errorPage="/ErrorHandler.jsp"%>
    <%=Config.getProperty("CSS")%>
    <mrald:validate />
    <title>People Manager</title></head>
  <body><div id="header"><h1 class="headerTitle">People Manager - Update Form</h1></div><div class="subHeader"><span class="doNotDisplay">Navigation:</span>
    MRALD - Finding the gems in your data |

      <a href="<%=Config.getProperty("BaseUrl")%>/index.jsp">Home</a>

    |
    <a href="http://mitre.org">MITRE</a> | <a href="mailto:mrald-dev-list@lists.mitre.org">Contact Developers</a></div>

        <form name="form" method="POST" action="<%=Config.getProperty("BaseUrl")%>/FormSubmit" enctype="x-www-form-urlencoded">


       <jsp:include page="/updateOutputSidebar.jsp" />
    <input name="Schema" type="hidden" value="" /><input name="Datasource" type="hidden" value="<%=MetaData.ADMIN_DB%>" /><input name="form" type="hidden" value="People Manager-simple" /><input type="hidden" name="workflow" value="Building SQL" /><div id="main-copy"><input name="outputFormat21" type="hidden" value="fieldname:email~nicename:email~type:String" /><input name="outputFormat22" type="hidden" value="fieldname:peopletypeid~nicename:peopletypeid~type:Numeric" /><input name="outputFormat23" type="hidden" value="fieldname:password~nicename:password~type:String" /><input name="table" type="hidden" value="people" /><input name="PrimaryKey" type="hidden" value="EMAIL" /><input name="Select1" type="hidden" value="Table:people~Field:email~Order:1" /><input name="Select2" type="hidden" value="Table:people~Field:peopletypeid~Order:2" /><input name="Select3" type="hidden" value="Table:people~Field:password~Order:3" /><h1 style="border-top: none; padding-top: 0;margin-top:0em;">Filters

        <a href="<%=Config.getProperty("BaseUrl")%>/UserGuide/UserGuide.html#UseStep3" target="_blank">
        <img alt="help" src="<%=Config.getProperty("BaseUrl")%>/images/green-help.jpeg" />
        </a>
      </h1><br /><p class="filter"><strong>Email contains</strong><br /><input type="checkbox" name="Filter1" value="Value:mitre.org" />mitre.org
          <img alt="" src="<%=Config.getProperty("BaseUrl")%>/images/spacer.gif" width="10" height="1"/>
        <input type="hidden" name="Filter1" value="Table:people~Field:email~Operator:like~Type:String" /><br />Others:
        <input type="text" name="Filter1" size="5" /><input type="text" name="Filter1" size="5" /><input type="text" name="Filter1" size="5" /></p><p class="filter"><strong>User is</strong><br /><input type="checkbox" name="Filter2" value="Value:1" />1
          <img alt="" src="<%=Config.getProperty("BaseUrl")%>/images/spacer.gif" width="10" height="1"/>
        <input type="checkbox" name="Filter2" value="Value:3" />3
          <img alt="" src="<%=Config.getProperty("BaseUrl")%>/images/spacer.gif" width="10" height="1"/>
        <input type="hidden" name="Filter2" value="Table:people~Field:peopletypeid~Operator:=~Type:Numeric" /><br />Others:
        <input type="text" name="Filter2" size="5" /><input type="text" name="Filter2" size="5" /><input type="text" name="Filter2" size="5" /></p><p class="filter"><strong>General Filters</strong> - Select field, operator, and filter value<br /><select name="Filter3" id="Filter3List"><option></option><option value="Table:people~Field:email~Type:String">email</option><option value="Table:people~Field:peopletypeid~Type:Numeric">peopletypeid</option><option value="Table:people~Field:password~Type:String">password</option></select><select name="Filter3"><option></option><option value="Operator:=">=
      </option><option value="Operator:!=">Not equal (!=)
      </option><option value="Operator:&lt;">&lt;
      </option><option value="Operator:&gt;">&gt;
      </option><option value="Operator:&lt;=">&lt;=
      </option><option value="Operator:&gt;=">&gt;=
      </option><option value="Operator:like">Contains
      </option><option value="Operator:starts">Starts With
      </option><option value="Operator:IN">IN
      </option><option value="Operator:NOT IN">NOT IN
      </option><option value="Operator:IS NULL">IS NULL
      </option><option value="Operator:IS NOT NULL">IS NOT NULL
      </option></select><input name="Filter3" type="text" size="22" id="Filter3ListValue" /><img src="../images/mrald_sample.jpg" height="24" width="24" onclick="showSample('popUp3', 'Filter3List' ,'../')" />Display Sample

		<span id="popUp3"><input type="hidden" name="dummy" /></span><br /><select name="Filter4" id="Filter4List"><option></option><option value="Table:people~Field:email~Type:String">email</option><option value="Table:people~Field:peopletypeid~Type:Numeric">peopletypeid</option><option value="Table:people~Field:password~Type:String">password</option></select><select name="Filter4"><option></option><option value="Operator:=">=
      </option><option value="Operator:!=">Not equal (!=)
      </option><option value="Operator:&lt;">&lt;
      </option><option value="Operator:&gt;">&gt;
      </option><option value="Operator:&lt;=">&lt;=
      </option><option value="Operator:&gt;=">&gt;=
      </option><option value="Operator:like">Contains
      </option><option value="Operator:starts">Starts With
      </option><option value="Operator:IN">IN
      </option><option value="Operator:NOT IN">NOT IN
      </option><option value="Operator:IS NULL">IS NULL
      </option><option value="Operator:IS NOT NULL">IS NOT NULL
      </option></select><input name="Filter4" type="text" size="22" id="Filter4ListValue" /><img src="../images/mrald_sample.jpg" height="24" width="24" onclick="showSample('popUp4', 'Filter4List' ,'../')" />Display Sample

		<span id="popUp4"><input type="hidden" name="dummy" /></span><br /><select name="Filter5" id="Filter5List"><option></option><option value="Table:people~Field:email~Type:String">email</option><option value="Table:people~Field:peopletypeid~Type:Numeric">peopletypeid</option><option value="Table:people~Field:password~Type:String">password</option></select><select name="Filter5"><option></option><option value="Operator:=">=
      </option><option value="Operator:!=">Not equal (!=)
      </option><option value="Operator:&lt;">&lt;
      </option><option value="Operator:&gt;">&gt;
      </option><option value="Operator:&lt;=">&lt;=
      </option><option value="Operator:&gt;=">&gt;=
      </option><option value="Operator:like">Contains
      </option><option value="Operator:starts">Starts With
      </option><option value="Operator:IN">IN
      </option><option value="Operator:NOT IN">NOT IN
      </option><option value="Operator:IS NULL">IS NULL
      </option><option value="Operator:IS NOT NULL">IS NOT NULL
      </option></select><input name="Filter5" type="text" size="22" id="Filter5ListValue" /><img src="../images/mrald_sample.jpg" height="24" width="24" onclick="showSample('popUp5', 'Filter5List' ,'../')" />Display Sample

		<span id="popUp5"><input type="hidden" name="dummy" /></span><br /></p></div><br /><div align="center"><input type="submit" value="Retrieve Data to Update" /><br /><br /><input type="reset" value="Reset Form" /></div>
      </form>
    </body>
</html>
