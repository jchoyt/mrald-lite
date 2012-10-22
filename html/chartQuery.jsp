
<%@ page import="org.mitre.mrald.util.Config" %>
<%@ taglib uri="/WEB-INF/mrald.tld" prefix="mrald"%>
<%
  String query = request.getParameter( "query" );
  if ( query == null || query.equals( "null" ) || query.equals( null ) )
    query = "";
%>
<mrald:validate />
<html>
  <head>
    <META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE">
    <meta name="generator" content="HTML Tidy, see www.w3.org">
    <title>
      Direct Query Page
    </title>
    <%=Config.getProperty( "CSS" ) %>
  </head>
  <body>
   <CENTER>
    <div id="header">
      <h1 class="headerTitle">
        <%=Config.getProperty( "TITLE" ) %>
      </h1>
    </div>
    <script language="JavaScript1.2" type="text/javascript" src="<%=Config.getProperty( "BaseUrl" ) %>/navi.js"></script>
      <br>
    
    <form method="POST" action="chart.jsp" enctype="x-www-form-urlencoded" name="chart">
    <input type="hidden" name="form" value="directquery"> <input type="hidden" name="workflow" value="Direct SQL">
      <table width="90%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td class="bord">
            <table width="100%" border="0" cellspacing="1" cellpadding="0">
              <tr>
                <th>
                  SQL query
                </th>
              </tr>
              <tr>
                <td>
                  <table align="left">
                    <tr>
                      <td valign="top">
                        <strong>Query text:</strong> <br>
                        Any valid SQL for the target database
                      </td>
                      <td>
                        <textarea name="query" rows="7" cols="60" wrap="soft"><%=query %></textarea>
                      </td>
		      <td>
		       <tr>
			    <td><b>Graph Title</b> </td>
			    <td>
                              <input type = 'text' name="graphTitle"></input>
			    </td>
			</tr>
		        <tr>
			    <td><b>Limit Label Length</b> </td>
			    <td>
                              <input type = 'text' name="labelLimit"></input>
			    </td>
			</tr>
			<tr>
			    <td><b>X Axis Label </b></td>
			    <td>
                              <input type = 'text' name="xaxislabel"></input>
			    </td>
			</tr>
			<tr>
			    <td><b>Y axis Label </b></td>
			    <td>
                              <input type = 'text' name="yaxislabel"></input>
			    </td>
			</tr>
			<tr>
			    <td><b>Start at </b></td>
			    <td>
                              <input type = 'text' name="startCount"></input>
			    </td>
			    <td><b>Limit Output Size </b></td>
			    <td>
                              <input type = 'text' name="rowCount"></input>
			    </td>
			</tr>
                      </td>
                    </tr>
                  </table>
                </td>
              </tr>
	       <tr>
                <td>
                  <table align="left">
                    <tr>
                      <td valign="top">
                        <strong>Chart Type</strong> <br>
                        Any valid SQL for the target database
                      </td>
                      <td>
                        <select name="chartType">
			<option value='stackedverticalbar3d'>Stacked 3D</option>
			<option value='verticalbar3d'>3D Vertical Bar</option>			
			<option value='area'>Area</option>
			<option value='horizontalbar'>Horizontal Bar</option>
			<option value='horizontalbar3d'>3D Horizontal Bar</option>
			<option value='line'>Line</option>
			<option value='stackedhorizontalbar'>Stacked Horizontal Bar</option>
			<option value='stackedverticalbar'>Stacked Vertical Bar</option>
			<option value='stackedverticalbar3d'>3D Stacked Vertical Bar</option>
			<option value='verticalbar'>Vertical Bar</option>
			<option value='verticalbar3d'>Vertical Bar 3D</option>			
			</select>
                      </td>
                    </tr>
                  </table>
                </td>
              </tr>
            </table>
          </td>
        </tr>
      </table>
      <div style="text-align: center">
        <input value="Get Chart" type="submit">
      </div>
    </form>
    </CENTER>
  </body>
</html>
