<?xml version="1.0"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:template name="pivotMain">
  <xsl:call-template name="parentTable">
    <xsl:with-param name="section">section_Pivot</xsl:with-param>
  </xsl:call-template>
</xsl:template>

<xsl:template name="parentRow_Pivot">
<xsl:variable name="x1" select="//pivot/text()" />
<xsl:variable name="eta" select="substring-before($x1, '~')" />
<xsl:variable name="x2" select="substring-after($x1, '~')" />
<xsl:variable name="efi" select="substring-before($x2, '~')" />
<xsl:variable name="x3" select="substring-after($x2, '~')" />
<xsl:variable name="ety" select="substring-before($x3, '~')" />
<xsl:variable name="x4" select="substring-after($x3, '~')" />
<xsl:variable name="ata" select="substring-before($x4, '~')" />
<xsl:variable name="x5" select="substring-after($x4, '~')" />
<xsl:variable name="afi" select="substring-before($x5, '~')" />
<xsl:variable name="x6" select="substring-after($x5, '~')" />
<xsl:variable name="aty" select="substring-before($x6, '~')" />
<xsl:variable name="x7" select="substring-after($x6, '~')" />
<xsl:variable name="vta" select="substring-before($x7, '~')" />
<xsl:variable name="x8" select="substring-after($x7, '~')" />
<xsl:variable name="vfi" select="substring-before($x8, '~')" />
<xsl:variable name="vty" select="substring-after($x8, '~')" />
  <tr>
    <td>
      <xsl:call-template name="sectionHelpLink">
        <xsl:with-param name="helpLinkName">Pivot</xsl:with-param>
      </xsl:call-template>

      <table cellspacing="0" cellpadding="0" border="0">
        <tr>
          <td width="5%" align="center">
            <xsl:element name="input">
              <xsl:attribute name="name">FBPivot</xsl:attribute>
              <xsl:attribute name="type">checkbox</xsl:attribute>
              <xsl:attribute name="value">PivotResults</xsl:attribute>
              <xsl:if test="//pivot">
                <xsl:attribute name="checked"></xsl:attribute>
              </xsl:if>
            </xsl:element>
          </td>
          <td width="20%">Pivot the output</td>
          <td width="5%">Entity:</td>
          <td width="20%">
            <xsl:element name="select">
              <xsl:attribute name="name">FBPivot</xsl:attribute>
              <xsl:attribute name="onChange">HandlePivot(this);</xsl:attribute>
              <xsl:if test="//pivot">
                <xsl:element name="option">
                  <xsl:attribute name="value">EntityTable:<xsl:value-of select="$eta"/>~EntityField:<xsl:value-of select="$efi"/>~EntityType:<xsl:value-of select="$ety"/></xsl:attribute>
                  <xsl:value-of select="$eta"/>.<xsl:value-of select="$efi"/>
                </xsl:element>
              </xsl:if>
              <xsl:element name="option"></xsl:element>
              <xsl:for-each select="//field">
                <xsl:element name="option">
                  <xsl:attribute name="value">EntityTable:<xsl:value-of select="./table/text()"/>~EntityField:<xsl:value-of select="./column/text()"/>~EntityType:<xsl:value-of select="./type/text()"/></xsl:attribute>
                  <xsl:value-of select="./table/text()"/>.<xsl:value-of select="./column/text()"/>
                </xsl:element>
              </xsl:for-each>
            </xsl:element>
          </td>
          <td width="5%">Attribute:</td>
          <td width="20%">
            <xsl:element name="select">
              <xsl:attribute name="name">FBPivot</xsl:attribute>
              <xsl:attribute name="onChange">HandlePivot(this);</xsl:attribute>
              <xsl:if test="//pivot">
                <xsl:element name="option">
                  <xsl:attribute name="value">AttributeTable:<xsl:value-of select="$ata"/>~AttributeField:<xsl:value-of select="$afi"/>~AttributeType:<xsl:value-of select="$aty"/></xsl:attribute>
                  <xsl:value-of select="$ata"/>.<xsl:value-of select="$afi"/>
                </xsl:element>
              </xsl:if>
              <xsl:element name="option"></xsl:element>
              <xsl:for-each select="//field">
                <xsl:element name="option">
                  <xsl:attribute name="value">AttributeTable:<xsl:value-of select="./table/text()"/>~AttributeField:<xsl:value-of select="./column/text()"/>~AttributeType:<xsl:value-of select="./type/text()"/></xsl:attribute>
                  <xsl:value-of select="./table/text()"/>.<xsl:value-of select="./column/text()"/>
                </xsl:element>
              </xsl:for-each>
            </xsl:element>
          </td>
          <td width="5%">Value:</td>
          <td width="20%">
            <xsl:element name="select">
              <xsl:attribute name="name">FBPivot</xsl:attribute>
              <xsl:attribute name="onChange">HandlePivot(this);</xsl:attribute>
              <xsl:if test="//pivot">
                <xsl:element name="option">
                  <xsl:attribute name="value">ValueTable:<xsl:value-of select="$vta"/>~ValueField:<xsl:value-of select="$vfi"/>~ValueType:<xsl:value-of select="$vty"/></xsl:attribute>
                  <xsl:value-of select="$vta"/>.<xsl:value-of select="$vfi"/>
                </xsl:element>
              </xsl:if>
              <xsl:element name="option"></xsl:element>
              <xsl:for-each select="//field">
                <xsl:element name="option">
                  <xsl:attribute name="value">ValueTable:<xsl:value-of select="./table/text()"/>~ValueField:<xsl:value-of select="./column/text()"/>~ValueType:<xsl:value-of select="./type/text()"/></xsl:attribute>
                  <xsl:value-of select="./table/text()"/>.<xsl:value-of select="./column/text()"/>
                </xsl:element>
              </xsl:for-each>
            </xsl:element>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</xsl:template>

</xsl:stylesheet>
