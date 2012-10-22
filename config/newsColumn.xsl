<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:template match="news">
    <xsl:for-each select="//newsday">
      <xsl:variable name="archive">
        <xsl:value-of select="@archive" />
      </xsl:variable>
      <xsl:if test="$archive='no'">
        <p class="sideBarTitle">
          <xsl:value-of select="@date" />
        </p>
        <xsl:for-each select="newsitem">
          <!-- Begin News Item -->
          <strong>
            <xsl:value-of select="title" />
          </strong>
          <br />
          <xsl:value-of select="body" disable-output-escaping="yes" />
          <br />
          <!-- End News Item -->
        </xsl:for-each>
      </xsl:if>
    </xsl:for-each>
  </xsl:template>
</xsl:stylesheet>
