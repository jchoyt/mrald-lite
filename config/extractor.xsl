<?xml version="1.0"?>
<!-- extracts labels from a valid MRALD form and brings them up into columnNames.props -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:output method="text"/>
  <xsl:template match="/">
    <xsl:for-each select="//field">
      <xsl:value-of select="table"/>
      <xsl:text>.</xsl:text>
      <xsl:value-of select="column"/>
      <xsl:text>=</xsl:text>
      <xsl:value-of select="label"/>
      <xsl:text>&#xA;</xsl:text>
    </xsl:for-each>
  </xsl:template>
</xsl:stylesheet>
