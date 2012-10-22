<?xml version="1.0"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="formElementsInsert.xsl" />

  <xsl:template name="InsertForm">
    <xsl:text disable-output-escaping="yes">
    <![CDATA[
    <%@ taglib uri="/WEB-INF/mrald.tld" prefix="mrald"%><%@ page errorPage="../ErrorHandler.jsp" %>
    <%@ page import="org.mitre.mrald.util.Config" %>
     <%=Config.getProperty("CSS")%>
    <mrald:validate />
    ]]>
    </xsl:text>
    <html>
    <head>
    <title>
    <xsl:value-of select="title"/>
    </title>
    <SCRIPT>function Enable(objElement)
    {
    objElement.disabled = false;
    objElement.value = '1';
    }
    function Disable(objElement)
    {
    objElement.disabled = true;
    objElement.value = '0';
    }
    </SCRIPT>
    </head>
        <body>
    <div id="header">
      <h1 class="headerTitle"><xsl:value-of select="title"/> - Insert Form</h1>
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
            <script language="JavaScript1.2" type="text/javascript" src="../CalendarPopup.js" ></script>
            <script language="JavaScript1.2" type="text/javascript" src="../checkPkValue.js" ></script>
            <script language="JavaScript1.2" type="text/javascript" src="../checkField.js"></script>
          ]]>
       </xsl:text>
    <!-- add output and links to advanced form -->
    <xsl:text disable-output-escaping="yes">
      <![CDATA[ <jsp:include page="/insertOutputSidebar.jsp" /> ]]>
    </xsl:text>


    <div style="text-align: center">

    <form method="POST" name="FormInsert" action="../FormSubmit" enctype="x-www-form-urlencoded">
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
        <xsl:attribute name="value"><xsl:value-of select="title"/></xsl:attribute>
      </xsl:element>
      <input type="hidden" name="workflow" value="Building DDL" />
      <input type="hidden" name="SuccessUrl" value="successInsert.jsp" />
      <input type="hidden" name="FailureUrl" value="failedInsert.jsp" />

       <div id="main-copy">
        <xsl:call-template name="formElementsInsert" />
      </div>
      <br />
      <!-- Action Buttons -->
      <div align="center">
        <input type="submit" value="Insert Data" />
        <br /><br />
        <input type="reset" value="Reset Form" />
      </div>
    </form>
    </div>
    </body>
    </html>
  </xsl:template>


</xsl:stylesheet>
