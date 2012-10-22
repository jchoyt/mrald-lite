<%@ taglib uri="/WEB-INF/mrald.tld" prefix="mrald"%>

<%@ page import="org.mitre.mrald.util.Config" %>
<%@ page import="org.mitre.mrald.util.*" %>
<%@ page import="org.mitre.mrald.util.DBMetaData" %>
<%@ page import="java.util.HashMap" %>
<%
    String[] ds = (String[])request.getAttribute("Datasource");
 %>
<!DOCTYPE html PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN'>
<html>
  <head>
    <meta name="generator" content="HTML Tidy, see www.w3.org">
    <title>
      Step #3: Select Columns and Filters to be Used
    </title>
    <%=Config.getProperty( "CSS" ) %>
<script type="text/javascript">
    function CheckAll(val, filter)
    {
//alert(val + "::" + filter);
        var ml = document.o;
        var len = ml.elements.length;
        for (var i = 0; i < len; i++)
        {
            var e = ml.elements[i];
            if (e.value==val && CheckFilter(e, filter))
            {
                e.checked = true;
            }
        }
    }
    
    function CheckFilter(e, filter)
    {
    	if (filter && e && e.parentNode && e.parentNode.parentNode) {
    		var row = e.parentNode.parentNode;
		var inputs = row.getElementsByTagName('input');
		for (var i = 0; i < inputs.length; i++) {
			var child = inputs[i];
			if (child && child.value && child.value.indexOf('Field:'+filter+'~') > 0) {
				return true;
    			}
    		}
		return false;
    	}
    	return true;
    }

    function ClearAll(val, filter)
    {
        var ml = document.o;
        var len = ml.elements.length;
        for (var i = 0; i < len; i++)
        {
            var e = ml.elements[i];
            if (e.value==val && CheckFilter(e, filter))
            {
                e.checked = false;
            }
         }
    }

    function HandlePivot(select)
    {
        var option = select.options[select.selectedIndex];
        var text = option.text;
        var table = text.substring(0, text.indexOf('.'));
        var field = text.substring(text.indexOf('.')+1);
        CheckAll('Output:true~Table:'+table,field);
        CheckAll('Default:true~Table:'+table,field);
        if (option.value.indexOf('Entity') < 0) {
            ClearAll('Filter:true~Table:'+table,field);
            ClearAll('Stat:true~Table:'+table,field);
            ClearAll('Group:true~Table:'+table,field);
            ClearAll('Sort:true~Table:'+table,field);
        }
    }

var win=null;
function NewWindow(mypage,myname,w,h,pos,infocus){
if(pos=="random"){myleft=(screen.width)?Math.floor(Math.random()*(screen.width-w)):100;mytop=(screen.height)?Math.floor(Math.random()*((screen.height-h)-75)):100;}
if(pos=="center"){myleft=(screen.width)?(screen.width-w)/2:100;mytop=(screen.height)?(screen.height-h)/2:100;}
else if((pos!='center' && pos!="random") || pos==null){myleft=0;mytop=20}
settings="width=" + w + ",height=" + h + ",top=" + mytop + ",left=" + myleft + ",scrollbars=yes,location=no,directories=no,status=no,menubar=no,toolbar=no,resizable=yes";win=window.open(mypage,myname,settings);
win.focus();}
</script>
  </head>
  <body>
      <mrald:loadMultiDbMetaData />
    <div id="header">
      <h1 class="headerTitle">
        <%=Config.getProperty( "TITLE" ) %>
      </h1>
    </div>
    <div class="subHeader">
    <span class="doNotDisplay">Navigation:</span>
      MRALD - Finding the gems in your data |
      <a href="javascript:NewWindow('help/step2.html','acepopup','640','480','center','front');">Help with this Form?</a>
      |
    <a href="index.jsp" target="_top">Home</a>
    |
    <a href="http://mitre.org">MITRE</a> | <a href="mailto:mrald-dev-list@lists.mitre.org">Contact Developers</a>
    </div>
      <center>
    <br />
      <!-- Start Form and Workflow data -->
      <form method='POST' action='FormSubmit' enctype="x-www-form-urlencoded" name="o">
      <mrald:carryParams />
      <input type='hidden' name='form' value='MultiDbFormBuilder2.jsp' />
      <input type='hidden' name='workflow' value='Form Builder' />
      <input type="hidden" name="stats" value="yes" />
       <input type='hidden' name='multiDb' value='yes'>
       
     <%
     	HashMap<String, DBMetaData> dbmds = (HashMap<String,DBMetaData>)request.getAttribute("MultiDBMetaData");
	int threadNum =0;
	for (String dbName: dbmds.keySet())
     	//for (DBMetaData db: dbmds.values())
	{
		DBMetaData db = dbmds.get(dbName);
		 String displayName = WebUtils.getDatasourceDisplayName(dbName);
    	 	pageContext.setAttribute("DBMetaData", db, PageContext.PAGE_SCOPE);
		MraldOutFile.logToFile(Config.getProperty("LOGFILE"), "MultiDbFormBuilder: database properties: " + db.getDbProps());
		threadNum++;
	%>	
	
	
	<table summary="" width="95%" border="0" cellspacing="0" cellpadding="0">
		<tr><td>
			<table summary="" width="100%" border="1" cellspacing="1" cellpadding="2">
				<tr><th style="font-style: italic;font-size: 175%;background-color: white;color: rgb(0, 112,0)"><i>Data Source: <%=displayName%></i></th></tr>
				<tr><td colspan="2" align="center" style="background-color: rgb(230,223,207);">
	
      <mrald:addSection className="org.mitre.mrald.formbuilder.LinkElement" title="Links" thread="<%=threadNum%>">
        <center><a href="javascript:NewWindow('help/step2.html#Links','acepopup','640','480','center','front');">Help with this Section</a><br /></center>
      </mrald:addSection>
      <br /><br />
      <mrald:addSection className="org.mitre.mrald.formbuilder.FieldElement" title="Available Columns" thread="<%=threadNum%>">
        <center><a href="javascript:NewWindow('help/step2.html#Fields','acepopup','640','480','center','front');">Help with this Section</a><br /></center>
      </mrald:addSection>

      
      <br /><br />
      <mrald:addSection className="org.mitre.mrald.query.FilterElement" howMany="3" title="Filters" thread="<%=threadNum%>">
        <center><a href="javascript:NewWindow('help/step2.html#Filters','acepopup','640','480','center','front');">Help with this Section</a><br /></center>
      </mrald:addSection>
    
      </td></tr></table></td></tr></table>
      <br />
            <% } %>
      <br />
      
       <mrald:addMultiDbSection className="org.mitre.mrald.formbuilder.CrossLinkElement" title="Cross Database Links" thread="<%=threadNum%>">
        <center><a href="javascript:NewWindow('help/step2.html#Links','acepopup','640','480','center','front');">Help with this Section</a><br /></center>
      </mrald:addMultiDbSection>
      
      <br /><br />
      
      <mrald:addSection className="org.mitre.mrald.formbuilder.CategoricalFilterElement" howMany="3" title="Categorical Filters">
        <center><a href="javascript:NewWindow('help/step2.html#CategoricalFilters','acepopup','640','480','center','front');">Help with this Section</a><br /></center>
      </mrald:addSection>
      <br /><br />
      <mrald:addSection className="org.mitre.mrald.query.OrFilterElement" howMany="2" title="Or Filters">
        <center><a href="javascript:NewWindow('help/step2.html#OrFilters','acepopup','640','480','center','front');">Help with this Section</a><br /></center>
      </mrald:addSection>
      <br /><br />
      <mrald:addSection className="org.mitre.mrald.query.CompareFieldsFilter" howMany="2" title="Field Comparisons">
        <center><a href="javascript:NewWindow('help/step2.html#FieldComparison','acepopup','640','480','center','front');">Help with this Section</a><br /></center>
      </mrald:addSection>
      <br /><br />
      <mrald:addSection className="org.mitre.mrald.query.RangeElement" howMany="2" title="Range Filters">
        <center><a href="javascript:NewWindow('help/step2.html#RangeFilter','acepopup','640','480','center','front');">Help with this Section</a><br /></center>
      </mrald:addSection>
      <br /><br />
      <mrald:addSection className="org.mitre.mrald.formbuilder.TimeElement" title="Time Filter">
        <center><a href="javascript:NewWindow('help/step2.html#TimeFilter','acepopup','640','480','center','front');">Help with this Section</a><br /></center>
      </mrald:addSection>
      <br /><br />
      <mrald:addSection className="org.mitre.mrald.formbuilder.AnalysisElement" title="Analysis Output">
        <center><a href="javascript:NewWindow('help/step2.html#AnalysisOutput','acepopup','640','480','center','front');">Help with this Section</a><br /></center>
      </mrald:addSection>
      <br /><br />
      <!-- PM: Adding a pivot option. -->
      <!-- Note that the pivot option assumes that the entity, attribute and value field names are unique.
           If they are not unique, the pivoted resultset will treat the first field with the indicated name
           as the correct one. -->
      <mrald:addSection className="org.mitre.mrald.formbuilder.PivotElement" title="Pivot Results">
        <center><a href="javascript:NewWindow('help/step2.html#Pivot','acepopup','640','480','center','front');">Help with this Section</a><br /></center>
      </mrald:addSection>
      <br /><br />
      <!-- PM: End of pivot option. -->
      </center>
      <div align="center">
        <input value="Build Form" type="submit"><br /><br /><input value="Reset Form" type="reset" />
      </div>
      <br /><br />
      </form>
  </body>
</html>

