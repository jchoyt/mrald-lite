<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:output method="xml" indent="yes"
    doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"
    doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN" />
<xsl:include href="formElements.xsl" />
<xsl:include href="outputSelections.xsl" />
<xsl:include href="outputOptions.xsl" />

<xsl:template name="MraldForm">
    <xsl:text disable-output-escaping="yes">
    <![CDATA[
    <%@ page import="org.mitre.mrald.util.Config" %>
    <%@ taglib uri="/WEB-INF/mrald.tld" prefix="mrald"%>
    <mrald:validate />]]>
    </xsl:text>
  <html>
    <head>
    <xsl:text disable-output-escaping="yes">
    <![CDATA[
    <%=Config.getProperty("CSS")%>
    <script language="JavaScript" type="text/javascript" src="<%=Config.getProperty( "BaseUrl" ) %>/tree.js"></script>
    <script language="Javascript" type="text/javascript" src="<%=Config.getProperty( "BaseUrl" ) %>/enable.js"></script>
    <script language="Javascript" type="text/javascript" src="<%=Config.getProperty( "BaseUrl" ) %>/showSampleValue.js"></script>
    ]]>
    </xsl:text>
    <title><xsl:value-of select="title"/></title>
    </head>
    <body>

    <div id="header">
        <h1 class="headerTitle"><xsl:value-of select="title"/></h1>
    </div>
    <div class="subHeader">
    <span class="doNotDisplay">Navigation:</span>
      MRALD - Finding the gems in your data |
      <xsl:text disable-output-escaping="yes">
        <![CDATA[
      <a href="<%=Config.getProperty("BaseUrl")%>/switchForm.jsp">Simple Form</a> |
      <a href="<%=Config.getProperty("BaseUrl")%>/index.jsp" target="_top">Home</a>
        ]]>
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
        <![CDATA[
      <div id ="outer" class="rightSideBar">
        <p class="sideBarTitle" style="margin-top:0;">Include in Results
        <!--
		<a href="<%=Config.getProperty("BaseUrl")%>/UserGuide/UserGuide.html#UseStep5" target="_blank">
        <img alt="help" src="<%=Config.getProperty("BaseUrl")%>/images/green-help.jpeg" />
        </a>
        -->
        </p>
        <p class="sideBarText">
        <input value="true" name="showQuery" type="checkbox" checked="checked" />
        The query
        <br />
        <input type="checkbox" name="showDuplicates" value="true" />
        Duplicate rows
        </p>
        <p class="sideBarTitle">Output size limit</p>
        <p class="sideBarText">
        	<input type="radio" value="none" name="outputSize" />
			None
			<br />
			<input type="radio" value="lines" name="outputSize" checked="checked" />
			<input name="outputLinesCount" type="text" size="6" value="500" />
			Lines
			<br />
			<input type="radio" value="mb" name="outputSize" />
			<input name="outputMBSize" type="text" size="5" value="1" />
			MB
		</p>
        <p class="sideBarTitle">Format</p>
        <p class="sideBarText">
        <strong>Browser</strong>

		]]>
      </xsl:text>
        <br />

	       <!-- GH: Add a test -to see if multiple databases - if there are then add this output. -->
	       <xsl:choose>
	       <xsl:when test="multiDb='yes'">
			<input type="radio" name="Format" value="crossDbHtml" checked="checked"/>Cross Database HTML
			<br />
		</xsl:when>
		 <xsl:otherwise>
		 	<input type="radio" name="Format" value="browserHtml" checked="checked" />
		 	HTML
			<br />
		</xsl:otherwise>
		</xsl:choose>
	   <!-- GH: End MultiDb -->
	<xsl:text disable-output-escaping="yes">
        <![CDATA[
        <input type="radio" name="Format" value="browserLinks" />
        HTML with Links
        <br />
        <input type="radio" name="Format" value="TabularHtml" />
        Tabular HTML
        <br />
        <input type="radio" name="Format" value="browserCsv" />
        CSV (
        <input name="browserFormatDelimiter" type="text" value="," size="2" />
        )
        <br />
        <input type="radio" name="Format" value="browserText" />
        Text (tab delimited)
        <br />
        <input type="radio" name="Format" value="browserTabularText" />
        Tabular Text
        <br />
        <input type="radio" name="Format" value="XmlRaw" />
        XML
		<br />
		]]>
      </xsl:text>

	  <xsl:for-each select="//analysis">
	  	<xsl:call-template name="analysisOutput"></xsl:call-template>
	  </xsl:for-each>
      <xsl:text disable-output-escaping="yes">
        <![CDATA[
        <strong>File</strong>
        <br />
        <input type="radio" name="Format" value="fileCsv" />
        CSV (
        <input name="fileFormatDelimiter" type="text" value="," size="2" />
        )
        <br />
        <input type="radio" name="Format" value="fileText" />
        Text (tab delimited)
        <br/>
        <input type="radio" name="Format" value="fileTabularText" />
        Tabular Text
        <br />
        <input type="radio" name="Format" value="excel" />
        Excel</p>
      </div>
		]]>
      </xsl:text>
    <!-- Start Form and Workflow data -->
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
      <xsl:attribute name="value"><xsl:value-of select="title"/></xsl:attribute>
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
    <input type="hidden" name="workflow" value="Building SQL" />
    <div class="leftSideBar">
      <p class="sideBarTitle">Advanced Form</p>
      <xsl:text disable-output-escaping="yes">
        <![CDATA[<p class="sideBarText">This form gives the user much more control than the simple version, however it is more complex to use.  For a simpler version, use the <a href="<%=Config.getProperty("BaseUrl")%>/switchForm.jsp"> simple version of this form</a></p>]]>
      </xsl:text>
    </div>
    <xsl:call-template name="OutputFields" />

      <div id="main-copy">
        <xsl:call-template name="formElements" />
      </div>
	   <xsl:text disable-output-escaping="yes">
      <![CDATA[<span id="popUp"><input type="hidden" name="dummy"/></span>]]>
    </xsl:text>

    <xsl:text disable-output-escaping="yes">
      <![CDATA[</form>]]>
    </xsl:text>
  </body>
</html>
</xsl:template>
</xsl:stylesheet>