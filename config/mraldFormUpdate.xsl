<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:output method="xml" indent="yes"
    doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"
    doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN" />
<xsl:include href="formElementsUpdate.xsl" />

<xsl:template name="UpdateForm">
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
      <h1 class="headerTitle"><xsl:value-of select="title"/> - Update Form</h1>
    </div>
    <div class="subHeader">
    <span class="doNotDisplay">Navigation:</span>
    MRALD - Finding the gems in your data |
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
      <![CDATA[ <jsp:include page="/updateOutputSidebar.jsp" /> ]]>
    </xsl:text>
    <!-- Start Form and Workflow data -->
    <xsl:element name="input">
      <xsl:attribute name="name">Schema</xsl:attribute>
      <xsl:attribute name="type">hidden</xsl:attribute>
      <xsl:attribute name="value"><xsl:value-of select="schema"/></xsl:attribute>
    </xsl:element>
    <xsl:element name="input">
      <xsl:attribute name="name">Datasource</xsl:attribute>
      <xsl:attribute name="type">hidden</xsl:attribute>
      <xsl:attribute name="value"><xsl:value-of select="datasource"/></xsl:attribute>
    </xsl:element>
    <xsl:element name="input">
      <xsl:attribute name="name">form</xsl:attribute>
      <xsl:attribute name="type">hidden</xsl:attribute>
      <xsl:attribute name="value"><xsl:value-of select="title"/>-simple</xsl:attribute>
    </xsl:element>
    <input type="hidden" name="workflow" value="Building SQL" />
      <div id="main-copy">
        <xsl:call-template name="formElementsUpdate" />
      </div>
      <br />
      <!-- Action Buttons -->
      <div align="center">
        <input type="submit" value="Retrieve Data to Update" />
        <br /><br />
        <input type="reset" value="Reset Form" />
      </div>
      <xsl:text disable-output-escaping="yes">
      <![CDATA[</form>]]>
    </xsl:text>
  </body>
</html>
</xsl:template>
</xsl:stylesheet>
