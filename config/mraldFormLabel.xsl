<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:output method="xml" indent="yes"
    doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"
    doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN" />


  <!-- NOTE - Line 47, below, has the system title in it - you should replace
  that with an applicable title, or remove it all together.  -->


  <xsl:template name="LabelForm">
    <html><head>
    <xsl:text disable-output-escaping="yes">
    <![CDATA[<<%@ taglib uri="/WEB-INF/mrald.tld" prefix="mrald"%><%@ taglib uri="/WEB-INF/lattice.tld" prefix="lattice"%><%@ page errorPage="../ErrorHandler.jsp" %>
    <%@ page import="org.mitre.mrald.util.Config" %>
    <%=Config.getProperty("CSS")%>
    <mrald:validate />]]> </xsl:text>
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
        <h1 class="headerTitle"><xsl:value-of select="title"/></h1>
      </div>
      <div class="subHeader">
      <span class="doNotDisplay">Navigation:</span>
      MRALD - Finding the gems in your data |
      <xsl:text disable-output-escaping="yes">
        <![CDATA[<a href="<%=Config.getProperty("BaseUrl")%>/switchForm.jsp">Simple Form</a>]]>
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
        <![CDATA[
      <div id ="outer" class="rightSideBar">
        <p class="sideBarTitle" style="margin-top:0;">Include in Results
        <img alt="help" src="images/green-help.jpeg" />
        </p>
        <p class="sideBarText">
        <input value="true" name="showQuery" type="checkbox" checked="checked" />
        The query
        <br />
        <input type="checkbox" name="showDuplicates" value="true" />
        Duplicate rows</p>
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
         <br />
        <input type="radio" name="Format" value="setLabel" checked="checked" />
	Set Label
        <br />
        <input type="radio" name="Format" value="browserHtml" />
        HTML
	<br />
	<input type="radio" name="Format" value="browseRecordHtml" >Record (HTML)</input>
        <br />
        <input type="radio" name="Format" value="browserLinks" />
        HTML with Links
        <br />
        <input type="radio" name="Format" value="browserCsv" />
        CSV (
        <input name="browserFormatDelimiter" type="text" value="," size="2" />
        )
        <br />
        <input type="radio" name="Format" value="browserText" />
        Text (tab delimited)
        <br />
        <input type="radio" name="Format" value="XmlRaw" />
        XML
		<br />
		]]>
	  </xsl:text>

      <xsl:text disable-output-escaping="yes">
        <![CDATA[
	 </p>
	 <br/>
	 <p class="sideBarTitle">Destination</p>

        <p class="sideBarText">
        <strong>File</strong>
        <br />
        <input type="radio" name="Format" value="fileCsv" />
        CSV (
        <input name="fileFormatDelimiter" type="text" value="," size="2" />
        )
        <br />
        <input type="radio" name="Format" value="fileText" />
        Text (tab delimited)</p>
        <p class="sideBarText">
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

   <input type="hidden" name="workflow" value="Building SQL"></input>
    <div class="leftSideBar">
     <br/>
      <p class="sideBarTitle">Current COI</p>
     <xsl:text disable-output-escaping="yes">
      <![CDATA[<center><lattice:showCOI /></center>	]]>
    </xsl:text>
    <br/>
      <p class="sideBarTitle">Advanced Label Form</p>
      <xsl:text disable-output-escaping="yes">
        <![CDATA[<p class="sideBarText">This form allows for changing the COI that is associated with data.
    Once the data is retrieved, you will be presented with a page that will allow you to update the COI associated with the retieved data.
    This form gives the user much more control than the simple version, however it is more complex to use.  For a simpler version, use the <a href="<%=Config.getProperty("BaseUrl")%>/switchForm.jsp"> simple version of this form</a></p>]]>
      </xsl:text>

    </div>
    <xsl:call-template name="OutputFields" />

      <div id="main-copy">
        <xsl:call-template name="formElements" />
      </div>
    <xsl:text disable-output-escaping="yes">
      <![CDATA[</form>]]>
    </xsl:text>
  </body>
</html>
</xsl:template>
</xsl:stylesheet>
