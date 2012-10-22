<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:template match="news">
    <xsl:for-each select="//newsday">
      <xsl:variable name="archive">
        <xsl:value-of select="@archive" />
      </xsl:variable>
      <xsl:if test="$archive='yes'">
        <tr>
		 <th>
          <xsl:value-of select="@date" />
		 </th>
        </tr>
        <xsl:for-each select="newsitem">
          <!-- Begin News Item -->
          <tr>
		   <td align="left">
            <b><xsl:value-of select="title" /></b>
		   </td>
		  </tr>
          <tr>
		   <td align="left">
           <xsl:value-of select="body" />
           <br />
           <br />
		   </td>
		  </tr>
          <!-- End News Item -->
        </xsl:for-each>
      </xsl:if>
    </xsl:for-each>
  </xsl:template>
</xsl:stylesheet>
