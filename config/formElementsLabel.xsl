<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

  <xsl:include href="outputFormatLabel.xsl" />

  <xsl:template name="formElementsLabel">

    <xsl:for-each select="//formElements">
      <xsl:call-template name="DoLinks"></xsl:call-template>
      <xsl:call-template name="NiceFormats"></xsl:call-template>
      <xsl:apply-templates select="time"></xsl:apply-templates>
      <br></br><xsl:call-template name="OutputFields"></xsl:call-template>
      <br></br><xsl:call-template name="Filters"></xsl:call-template>
      <br></br><xsl:call-template name="Statistics"></xsl:call-template>
      <br></br><xsl:call-template name="OutputFormatLabel"></xsl:call-template>
    </xsl:for-each>
      
    <br></br><xsl:call-template name="ActionButtons"></xsl:call-template>
  </xsl:template>

  
</xsl:stylesheet>

