<%@ taglib uri="/WEB-INF/mrald.tld" prefix="mrald"%>
<%@ taglib uri="/WEB-INF/lattice.tld" prefix="lattice"%>

<%@ page import="org.mitre.mrald.util.Config" %>
<mrald:validate />
<!DOCTYPE html PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN'>
<html>
  <head>
    <META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE">
    <meta name="generator" content="HTML Tidy, see www.w3.org">
    <title>
      Step #1: Select Tables to be Used
    </title>
    <%=Config.getProperty( "CSS" ) %>
<script type="text/javascript">
<!--
   function checkForm(form)
   {
     
     for( var i=0; i < form.Table.length; i++)
       if( form.Table[i].checked )
         return true;
     alert( "You Have Not Selected Any Tables" );
    return false;
   }
var win=null;
function NewWindow(mypage,myname,w,h,pos,infocus){
if(pos=="random"){myleft=(screen.width)?Math.floor(Math.random()*(screen.width-w)):100;mytop=(screen.height)?Math.floor(Math.random()*((screen.height-h)-75)):100;}
if(pos=="center"){myleft=(screen.width)?(screen.width-w)/2:100;mytop=(screen.height)?(screen.height-h)/2:100;}
else if((pos!='center' && pos!="random") || pos==null){myleft=0;mytop=20}
settings="width=" + w + ",height=" + h + ",top=" + mytop + ",left=" + myleft + ",scrollbars=yes,location=no,directories=no,status=no,menubar=no,toolbar=no,resizable=yes";win=window.open(mypage,myname,settings);
win.focus();}
// -->
</script>
  </head>
  <body>
    <center>
      <div id="header">
      <h1 class="headerTitle">
        <%=Config.getProperty( "TITLE" ) %> Table Labeller.
      </h1>
      <br/><!--a href="javascript:NewWindow('help/step1.html','acepopup','640','480','center','front');">Help With This Form?</a-->
      <a href="help/lattice-help.html#labelTables" target="_blank">Help With This Form?</a> 
	  
    </div>
    <div class="subHeader">
    <span class="doNotDisplay">Navigation:</span>
    MRALD - Finding the gems in your data |
    <a href="index.jsp" target="_top">Home</a>
    |
    <a href="http://mitre.org">MITRE</a> | <a href="mailto:mrald-dev-list@lists.mitre.org">Contact Developers</a>
    </div>
              
      <!-- Start Form and Workflow data -->
      <br>
      <form method='POST' action='FormSubmit' enctype='x-www-form-urlencoded' onsubmit="return checkForm( this )">
        <input type='hidden' name='form' value='test'>
       <INPUT VALUE="Table Label" NAME="workflow" TYPE="hidden">
       <table cellpadding="0" cellspacing="0" border="0" width="95%">
          <tr>
            <td class="bord">
              <table cellpadding="5" cellspacing="1" border="0" width="100%">
                <tr>
                  <th class="title" COLSPAN="3">
                    Form Details
                  </th>
                </tr>
                
                <tr>
                </tr>
                <lattice:tableList />
              </table>
            </td>
          </tr>
        </table>
        <br>
         <input type='submit' value='Proceed'><br>
        <br>
         <input type='reset' value='Reset Form'>
      </form>
    </center>
  </body>
</html>
