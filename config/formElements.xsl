<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:template name="formElements">
    <xsl:for-each select="//formElements">
      <xsl:call-template name="DoLinks">
      </xsl:call-template>
      <xsl:call-template name="NiceFormats">
      </xsl:call-template>
      <xsl:call-template name="Filters">
        <xsl:with-param name="advanced">true</xsl:with-param>
      </xsl:call-template>
      <xsl:call-template name="Statistics">
      </xsl:call-template>
    </xsl:for-each>
    <br />
    <center id="notify">
            <!-- This div must be kept down here due to IE layout bugs -->
            <span/>
            <!-- The span must be kept here to prevent the <center> tag from
            collapsing, which results in the retrieve buttons disappearing whent
            the user clicks on the retrieve button and the form not submitting
            -->
    </center>
    <xsl:call-template name="ActionButtons">
    </xsl:call-template>
  </xsl:template>
</xsl:stylesheet>
