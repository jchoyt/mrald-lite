<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

 <xsl:include href="edit_updateColumns.xsl"/>

<xsl:template name="UpdateForm">
  <html>
    <head>
    <link rel="stylesheet" href="mrald.css" type="text/css" />
    <title><xsl:value-of select="title"/></title>
    <xsl:text disable-output-escaping="yes">
        <![CDATA[
<script type="text/javascript">
    function CheckAll(val)
    {
        var ml = document.o;
        var len = ml.elements.length;
        for (var i = 0; i < len; i++)
        {
            var e = ml.elements[i];
            if (e.value==val)
            {
                e.checked = true;
            }
        }
    }

    function ClearAll(val)
    {
        var ml = document.o;
        var len = ml.elements.length;
        for (var i = 0; i < len; i++)
        {
            var e = ml.elements[i];
            if (e.value == val)
            {
                e.checked = false;
            }
         }
    }
</script>
         ]]>
	 </xsl:text>
    </head>
    <body>
    <div id="header">
        <h1 class="headerTitle"><xsl:value-of select="title"/></h1>
    </div>
    <div class="subHeader">
    <span class="doNotDisplay">Navigation:</span>

      MRALD - Finding the gems in your data |
      <a href="help" onclick="window.open('help/step2.html', 'acepopup', 'width=640,height=480,status=yes,scrollbars=yes,toolbar=yes,resizable' );return false;">Help with this Form?</a>
      |
    <a href="index.jsp" target="_top">Home</a>
    |
    <a href="http://mitre.org">MITRE</a> | <a href="mailto:mrald-dev-list@lists.mitre.org">Contact Developers</a>
    </div>

      <!-- Start Form and Workflow data -->

      <form method="POST" action="FormSubmit" enctype="x-www-form-urlencoded" name="o">

        <input type="hidden" name="form" value="BuildForm" />
        <xsl:element name="input">
          <xsl:attribute name="type">hidden</xsl:attribute>
          <xsl:attribute name="name">Title</xsl:attribute>
          <xsl:attribute name="value"><xsl:value-of select="//title"/></xsl:attribute>
        </xsl:element>

        <xsl:element name="input">
          <xsl:attribute name="type">hidden</xsl:attribute>
          <xsl:attribute name="name">Table</xsl:attribute>
          <xsl:attribute name="value"><xsl:value-of select="//field/table"/></xsl:attribute>
        </xsl:element>

        <xsl:element name="input">
          <xsl:attribute name="type">hidden</xsl:attribute>
          <xsl:attribute name="name">Datasource</xsl:attribute>
          <xsl:attribute name="value"><xsl:value-of select="//datasource"/></xsl:attribute>
        </xsl:element>

        <input type="hidden" name="tableDepth" value="0" />
        <input type="hidden" name="form" value="UpdateFormBuilder2.jsp" />
        <input type="hidden" name="workflow" value="Form Builder" />
        <input type="hidden" name="stats" value="yes" />
        <input type="hidden" name="formType" value="Update" />
        <center>
          <xsl:call-template name="formElementsUpdate"></xsl:call-template>
        </center>
   <br />

   <div align="center">
    <input type="submit" value="Build Form" />
    <br />
    <br />
    <input type="reset" value="Reset Form" />
   </div>
   </form>
  </body>
</html>
</xsl:template>
<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- template to match formElementsInsert              -->
<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<xsl:template name="formElementsUpdate">
  <br />
  <br />
  <xsl:call-template name="updateAvailableColumnsMain"></xsl:call-template>
  <br />
  <br></br>
  <xsl:call-template name="filtersMain"></xsl:call-template>
  <br />
  <br />
  <xsl:call-template name="catFiltersMain"></xsl:call-template>
  <br />
  <br />
  <xsl:call-template name="orFiltersMain"></xsl:call-template>
  <br />
  <br />
  <xsl:call-template name="rangeFiltersMain"></xsl:call-template>
  <br />
  <br />
  <xsl:call-template name="timeFilterMain"></xsl:call-template>
</xsl:template>

</xsl:stylesheet>
