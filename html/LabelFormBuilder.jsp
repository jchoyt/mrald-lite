<%@ taglib uri="/WEB-INF/mrald.tld" prefix="mrald"%>
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
     if( form.Title.value == "" )
     {
         alert( "You Have Not Entered A Page Title" );
         return false;
     }
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
        <%=Config.getProperty( "TITLE" ) %> Label Form Builder
      </h1>
      <a href="help/lattice-help.html#labelFormBuild"  target="_blank">Help With This Form?</a> 
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
      <form method='POST' action='LabelFormBuilder2.jsp' enctype='x-www-form-urlencoded' onsubmit="return checkForm( this )">
        <input type='hidden' name='form' value='BuildForm'>
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
                  <td COLSPAN="3">
                    <b>Page Title:</b> <input name="Title" type='text' size='60'> <i>(required)</i> (<a href="javascript:NewWindow('help/step1.html#PageTitle','acepopup','640','480','center','front');">What is this</a>)</td>
                </tr>
                <tr>
                  <td colspan="3">
                    <b>Search database for related tables to depth of:</b> <input type='text' name='tableDepth' value="0" size="6"> Use zero (0) for only checked tables. (<a href="javascript:NewWindow('help/step1.html#Depth','acepopup','640','480','center','front');">What is this</a>)<br>
                  </td>
                </tr>
                <mrald:tableList />
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
