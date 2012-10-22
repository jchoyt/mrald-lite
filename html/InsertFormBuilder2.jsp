<%@ taglib uri="/WEB-INF/mrald.tld" prefix="mrald"%>

<%@ page import="org.mitre.mrald.util.Config" %>
<!DOCTYPE html PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN'>
<html>
  <head>
    <meta name="generator" content="HTML Tidy, see www.w3.org">
    <title>
      Step #2: Select Columns and Filters to be Used
    </title>
    <%=Config.getProperty( "CSS" ) %>
<script type="text/javascript">
    function CheckAll(val)
    {
        var ml = document.o;
        var len = ml.elements.length;
        for (var i = 0; i < len; i++)
        {
            var e = ml.elements[i];
            if (e.value==val)
            {
                e.checked = true;
            }
        }
    }

    function ClearAll(val)
    {
        var ml = document.o;
        var len = ml.elements.length;
        for (var i = 0; i < len; i++)
        {
            var e = ml.elements[i];
            if (e.value == val)
            {
                e.checked = false;
            }
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
<div id="header">
      <h1 class="headerTitle">
        <%=Config.getProperty( "TITLE" ) %><br/> Custom Insert Form Builder
      </h1>
    </div>
    <div class="subHeader">
    <span class="doNotDisplay">Navigation:</span>
    MRALD - Finding the gems in your data |
    <a href="index.jsp" target="_top">Home</a>
    |
    <a href="http://mitre.org">MITRE</a> | <a href="mailto:mrald-dev-list@lists.mitre.org">Contact Developers</a>
    </div>
  </head>
  <body>
      <mrald:loadMetaData listAllLinkTables="true"/>
      <center>

      <br>
      <!-- Start Form and Workflow data -->
      <form method='POST' action='FormSubmit' enctype="x-www-form-urlencoded" name="o">
      <mrald:carryParams />
      <input type='hidden' name='form' value='InsertFormBuilder2.jsp'>
      <input type='hidden' name='workflow' value='Form Builder'>
      <input type='hidden' name='formType' value='Insert'>
      <input type="hidden" name="stats" value="no">

      <br><br>
      <mrald:addSection className="org.mitre.mrald.formbuilder.FieldInsertElement" title="Available Columns">
        <center><a href="javascript:NewWindow('help/step2.html#Fields','acepopup','640','480','center','front');">Help with this Section?</a><BR></center>
      </mrald:addSection>
      <br><br>
      <div align="center">
        <input value="Build Form" type="submit"><br><br><input value="Reset Form" type="reset">
      </div>
      </form>
  </body>
</html>

