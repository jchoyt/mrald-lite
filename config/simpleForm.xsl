<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:output method="xml" indent="yes"
    doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"
    doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN" />
<xsl:include href="simpleFormElements.xsl" />
<xsl:include href="mraldCommon.xsl" />
<xsl:include href="filters.xsl" />
<xsl:include href="stats.xsl" />
<xsl:template match="MraldForm">
  <html>
  <head>
    <xsl:text disable-output-escaping="yes">
    <![CDATA[
    <%@ page import="org.mitre.mrald.util.Config" %>
    <%@ taglib uri="/WEB-INF/mrald.tld" prefix="mrald"%>
    <%@ page errorPage="/ErrorHandler.jsp"%>
    <%=Config.getProperty("CSS")%>
    <mrald:validate />
    <script type="text/javascript" language="Javascript1.2" src="<%=Config.getProperty( "BaseUrl" ) %>/enable.js"></script>
    <script type="text/javascript" language="Javascript1.2" src="<%=Config.getProperty( "BaseUrl" ) %>/showSampleValue.js"></script>
    <script language="JavaScript1.2" type="text/javascript" src="<%=Config.getProperty( "BaseUrl" ) %>/dropdown.js"></script>]]>
    
    </xsl:text>
    <title><xsl:value-of select="title"/></title>

  </head>
  <body>
    <div id="header">
      <h1 class="headerTitle"><xsl:value-of select="title"/> - Simple Form </h1>
    </div>
    <div class="subHeader">
    <span class="doNotDisplay">Navigation:</span>
    MRALD - Finding the gems in your data |
    <xsl:text disable-output-escaping="yes"><![CDATA[<a href="<%=Config.getProperty("BaseUrl")%>/switchForm.jsp">Advanced Form</a>]]></xsl:text>
    |
    <xsl:text disable-output-escaping="yes"><![CDATA[<a href="<%=Config.getProperty("BaseUrl")%>/index.jsp">Home</a>]]></xsl:text>
    |
    <a href="http://mitre.org">MITRE</a> | <a href="mailto:mrald-dev-list@lists.mitre.org">Contact Developers</a>
    </div>
    <xsl:text disable-output-escaping="yes">
    <![CDATA[<form name="form" method="POST" action="<%=Config.getProperty("BaseUrl")%>/FormSubmit" enctype="x-www-form-urlencoded">]]></xsl:text>
    <!-- add output and links to advanced form -->
     <!-- GH: MultiDb: Change the outputSidebar if this is multiDb. -->
    <xsl:choose>
      	<xsl:when test="multiDb='yes'">
		<xsl:text disable-output-escaping="yes">
		<![CDATA[ <jsp:include page="/multiDbOutputSidebar.jsp" /> ]]>
		</xsl:text>
      	</xsl:when>
	<xsl:otherwise>
		<xsl:text disable-output-escaping="yes">
		<![CDATA[ <jsp:include page="/simpleOutputSidebar.jsp" /> ]]>
		</xsl:text>
	</xsl:otherwise>
	</xsl:choose>     
   <!-- GH: End MultiDb -->
    <!-- Start Form and Workflow data -->
    <xsl:variable name="workflow" >Building SQL</xsl:variable>
    <xsl:element name="input">
      <xsl:attribute name="name">Schema</xsl:attribute>
      <xsl:attribute name="type">hidden</xsl:attribute>
      <xsl:attribute name="value"><xsl:value-of select="schema"/></xsl:attribute>
    </xsl:element>
    <!-- GH: MultiDb: Add a hidden element for each datasource. -->
    <xsl:for-each select="datasource">
    	<xsl:element name="input">
	<xsl:attribute name="name">Datasource</xsl:attribute>
	<xsl:attribute name="type">hidden</xsl:attribute>
	<xsl:attribute name="value"><xsl:value-of select="."/></xsl:attribute>
        </xsl:element>
     </xsl:for-each>
     <!-- GH: End MultiDb -->
     <xsl:element name="input">
      <xsl:attribute name="name">form</xsl:attribute>
      <xsl:attribute name="type">hidden</xsl:attribute>
      <xsl:attribute name="value"><xsl:value-of select="title"/>-simple</xsl:attribute>
    </xsl:element>
     <!-- GH: Add a hidden element when there are multiple databases. -->
     <!-- GH: Add a test -to see if multiple databases - if there are then change the workflow. -->  
     <xsl:choose>
      	<xsl:when test="multiDb='yes'">
		<input type="hidden" name="workflow" value="Multi SQL" />
		<xsl:element name="input">
			<xsl:attribute name="name">multiDb</xsl:attribute>
			<xsl:attribute name="type">hidden</xsl:attribute>
			<xsl:attribute name="value"><xsl:value-of select="multiDb"/></xsl:attribute>
		</xsl:element>
		<xsl:element name="input">
			<xsl:attribute name="name">linking</xsl:attribute>
			<xsl:attribute name="type">hidden</xsl:attribute>
			<xsl:attribute name="value">unlinked</xsl:attribute>
		</xsl:element>
    	</xsl:when>
	<xsl:otherwise>
		<input type="hidden" name="workflow" value="Building SQL" />
	</xsl:otherwise>
	</xsl:choose>     
    <!-- GH: End MultiDb -->
    <!-- PM: Add a hidden element when the results are to be pivoted. -->
    <xsl:if test="//pivot">
      <xsl:element name="input">
        <xsl:attribute name="name">pivot</xsl:attribute>
        <xsl:attribute name="type">hidden</xsl:attribute>
        <!-- Copy the entity~attribute~value information into the jsp for use by the output manager. -->
        <xsl:attribute name="value"><xsl:value-of select="//pivot"/></xsl:attribute>
      </xsl:element>
      <xsl:text disable-output-escaping="yes">
        &lt;%@ page import="org.mitre.mrald.query.PivotFilter" %&gt;
        &lt;%@ page import="org.mitre.mrald.query.PivotAggregateFilter" %&gt;
      </xsl:text>
    </xsl:if>
    <!-- PM: End pivot results -->
     <div id="main-copy" style="margin-right:0px">
        <xsl:call-template name="formElements" />
      </div>
    <xsl:text disable-output-escaping="yes">
      <![CDATA[<span id="popUp"><input type="hidden" name="dummy"/></span></form>]]>
    </xsl:text>
  </body>
</html>
</xsl:template>

<xsl:template match="LabelForm">
  <html>
  <head>
    <xsl:text disable-output-escaping="yes">
    <![CDATA[
    <%@ page import="org.mitre.mrald.util.Config" %>
    <%@ taglib uri="/WEB-INF/mrald.tld" prefix="mrald"%>
    <%@ page errorPage="/ErrorHandler.jsp"%>
    <%=Config.getProperty("CSS")%>
    <mrald:validate />]]>
    </xsl:text>
    <title><xsl:value-of select="title"/></title>
  </head>
  <body>
    <div id="header">
      <h1 class="headerTitle"><xsl:value-of select="title"/> - Simple Form </h1>
    </div>
    <div class="subHeader">
    <span class="doNotDisplay">Navigation:</span>
    MRALD - Finding the gems in your data |
    <xsl:text disable-output-escaping="yes">
      <![CDATA[<a href="<%=Config.getProperty("BaseUrl")%>/switchForm.jsp">Advanced Form</a>]]>
    </xsl:text>
    |
    <xsl:text disable-output-escaping="yes">
      <![CDATA[<a href="<%=Config.getProperty("BaseUrl")%>/index.jsp">Home</a>]]>
    </xsl:text>
    |
    <a href="http://mitre.org">MITRE</a> | <a href="mailto:mrald-dev-list@lists.mitre.org">Contact Developers</a>
    </div>
    <xsl:text disable-output-escaping="yes">
      <![CDATA[
        <form name="form" method="POST" action="<%=Config.getProperty("BaseUrl")%>/FormSubmit" enctype="x-www-form-urlencoded">
      ]]>
    </xsl:text>
    <!-- add output and links to advanced form -->
    <xsl:text disable-output-escaping="yes">
      <![CDATA[ <jsp:include page="/simpleLabelOutputSidebar.jsp" /> ]]>
    </xsl:text>
    <!-- Start Form and Workflow data -->

    <xsl:element name="input">
      <xsl:attribute name="name">Schema</xsl:attribute>
      <xsl:attribute name="type">hidden</xsl:attribute>
      <xsl:attribute name="value"><xsl:value-of select="schema"/></xsl:attribute>
    </xsl:element>
    <xsl:element name="input">
      <xsl:attribute name="name">form</xsl:attribute>
      <xsl:attribute name="type">hidden</xsl:attribute>
      <xsl:attribute name="value"><xsl:value-of select="title"/></xsl:attribute>
    </xsl:element>

    <xsl:element name="input">
      <xsl:attribute name="name">form</xsl:attribute>
      <xsl:attribute name="type">hidden</xsl:attribute>
      <xsl:attribute name="value"><xsl:value-of select="title"/></xsl:attribute>
    </xsl:element>

    <xsl:element name="input">
      <xsl:attribute name="name">ParamsLabel</xsl:attribute>
      <xsl:attribute name="type">hidden</xsl:attribute>
      <xsl:attribute name="value">Table:<xsl:value-of select="//dataLabel/table"/>~Column:<xsl:value-of select="//dataLabel/column"/>
      </xsl:attribute>
    </xsl:element>

     <xsl:for-each select="//field[@output='yes']">
       <xsl:if test="position() = 1">

         <xsl:element name="input">
            <xsl:attribute name="name">table</xsl:attribute>
            <xsl:attribute name="type">hidden</xsl:attribute>
            <xsl:attribute name="value"><xsl:value-of select="table"/></xsl:attribute>
         </xsl:element>

       </xsl:if>
    </xsl:for-each>

    <input type="hidden" name="workflow" value="Building SQL" />
      <div id="main-copy">
        <xsl:call-template name="formElements" />
      </div>

    <xsl:text disable-output-escaping="yes">
      <![CDATA[<span id="popUp"><input type="hidden" name="dummy"/></span></form>]]>
    </xsl:text>

  </body>
</html>
</xsl:template>

</xsl:stylesheet>