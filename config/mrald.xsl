<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="mraldForm.xsl" />
<xsl:include href="mraldFormUpdate.xsl" />
<xsl:include href="mraldFormInsert.xsl" />
<xsl:include href="mraldFormLabel.xsl" />
  
  <xsl:include href="mraldCommon.xsl" />
  <xsl:include href="filters.xsl" />
  <xsl:include href="stats.xsl" />


  <xsl:template match="MraldForm">
    <xsl:call-template name="MraldForm"></xsl:call-template>
  </xsl:template>

  <xsl:template match="UpdateForm">
    <xsl:call-template name="UpdateForm"></xsl:call-template>
  </xsl:template>

  <xsl:template match="InsertForm">
    <xsl:call-template name="InsertForm"></xsl:call-template>
  </xsl:template>

 <xsl:template match="LabelForm">
    <xsl:call-template name="LabelForm"></xsl:call-template>
  </xsl:template>


</xsl:stylesheet>
