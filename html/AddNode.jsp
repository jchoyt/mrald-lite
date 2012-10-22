<%@ taglib uri="/WEB-INF/lattice.tld" prefix="lattice"%>

<%@ page import="org.mitre.mrald.util.Config" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

  <head>
    <META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE">
    <meta name="latticeServer" content="HTML Tidy, see www.w3.org">
    <title>
      Lattice Server
    </title>
    <%=Config.getProperty( "CSS" ) %>
  
  <script type="text/javascript">
<!--
   function checkForm(form)
   {
     if( form.securityGroup.value == "" )
     {
         alert( "You Have Not Entered A Node Name." );
         return false;
     }
     
     return true;
   }

// -->
</script>
  </head>
  
  <body>
    <form ENCTYPE="x-www-form-urlencoded" ACTION="FormSubmit" METHOD="POST" target="_parent" onsubmit="return checkForm( this )">
    <center>
    <table cellpadding="0" cellspacing="0" border="0" width="10%">
        <tr>
          <td class="bord">
              <table cellpadding="5" cellspacing="1" border="0" width="100%">
                <tr>
                  <th class="title" COLSPAN="3">
                    Groups
                  </th>
                </tr>
                <tr>
                  <td colspan="3">
                    <b>List Groups</b>
		  </td>
                </tr>
                <input type='hidden' name='workflow' value='Lattice Server'>
		<lattice:groupModifyList />
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

