<%@page contentType="text/html"%>
<%@taglib uri='/WEB-INF/cewolf.tld' prefix='cewolf' %>
<%@ page import = "javax.servlet.http.HttpSession"%>
<%@ page import="org.mitre.mrald.util.Config" %>
<%

  String[] params = {"query", "chartType", "graphTitle", "yaxislabel", "xaxislabel","labelLimit", "rowCount", "startCount"};
  String[] defaults = {"select subject_id, value::INT from subject_values ", 
                        "stackedverticalbar3d",
			"Data Results", "Data ", "Method","50", "100000", "0"};
			
  HttpSession sess = request.getSession();
  int paramLength = params.length;
  String[] values = new String[paramLength];
  
  for (int i=0; i < params.length; i++)
  {
    String value = request.getParameter( params[i]);
  
     if ( value == null || value.equals( "null" ) || value.equals( null ) || value.equals( "" ) )
     {
         if (sess.getAttribute(params[i]) != null)
	     value = sess.getAttribute(params[i].toString()).toString();
     }
     if ( value == null || value.equals( "null" ) || value.equals( null ) || value.equals( "" ) )
       value = defaults[i];
       
     values[i] = value;
  }
%>
<HTML>
<title>
      <%=Config.getProperty( "TITLE" ) %>
    </title>
    <%=Config.getProperty( "CSS" ) %>
<BODY>
<H1>Page View Statistics</H1>
<HR>
<jsp:useBean id="pageViews" class="org.mitre.mrald.chart.ChartData"/>
<cewolf:chart 
    id="line" 
    title='<%=values[2]%>' 
    type='<%=values[1]%>'
    xaxislabel='<%=values[4]%>'
    yaxislabel='<%=values[3]%>'>
    <cewolf:data>
        <cewolf:producer id="pageViews">
	  <cewolf:param name="query" value='<%=values[0]%>'></cewolf:param>
	  <cewolf:param name="labelLimit" value='<%=values[5]%>'></cewolf:param>
	  <cewolf:param name="rowCount" value='<%=values[6]%>'></cewolf:param>
	  <cewolf:param name="startCount" value='<%=values[7]%>'></cewolf:param>
	</cewolf:producer>
    </cewolf:data>
    <cewolf:gradientpaint>
    <cewolf:point x="300" y="0" color="#F7FFF2"></cewolf:point>
    <cewolf:point x="300" y="300" color="#BCD2BC"/>
    </cewolf:gradientpaint>
    <cewolf:chartpostprocessor id="pageViews">
      <cewolf:param name="rotate_at" value='<%= new Integer(1) %>'/>
    </cewolf:chartpostprocessor>
</cewolf:chart>
<cewolf:img chartid="line" renderer="cewolf" width="800" height="600">
    <cewolf:map tooltipgeneratorid="pageViews" linkgeneratorid="pageViews"/>
</cewolf:img>
<P>
</BODY>
</HTML>
