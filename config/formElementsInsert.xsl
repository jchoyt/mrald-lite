<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:include href="outputSelectionsInsert.xsl" />


   <xsl:template name="formElementsInsert">

    <xsl:for-each select="//formElements">
      <xsl:call-template name="NiceFormats"></xsl:call-template>
      <xsl:apply-templates select="time"></xsl:apply-templates>
      <br /><xsl:call-template name="OutputFieldsInsert"></xsl:call-template>
    </xsl:for-each>

   <!--  <br /><xsl:call-template name="ActionButtons"></xsl:call-template> -->

  </xsl:template>

</xsl:stylesheet>

