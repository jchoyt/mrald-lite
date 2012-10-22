<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:template name="operator-list">
      <xsl:element name="option"></xsl:element>
      <xsl:element name="option">
        <xsl:attribute name="value">Operator:=</xsl:attribute>=
      </xsl:element>
      <xsl:element name="option">
        <xsl:attribute name="value">Operator:!=</xsl:attribute>Not equal (!=)
      </xsl:element>
      <xsl:element name="option">
        <xsl:attribute name="value">Operator:&lt;</xsl:attribute>&lt;
      </xsl:element>
      <xsl:element name="option">
        <xsl:attribute name="value">Operator:&gt;</xsl:attribute>&gt;
      </xsl:element>
      <xsl:element name="option">
        <xsl:attribute name="value">Operator:&lt;=</xsl:attribute>&lt;=
      </xsl:element>
      <xsl:element name="option">
        <xsl:attribute name="value">Operator:&gt;=</xsl:attribute>&gt;=
      </xsl:element>
      <xsl:element name="option">
        <xsl:attribute name="value">Operator:like</xsl:attribute>Contains
      </xsl:element>
      <xsl:element name="option">
        <xsl:attribute name="value">Operator:not like</xsl:attribute>Does Not Contain
      </xsl:element>
      <xsl:element name="option">
        <xsl:attribute name="value">Operator:starts</xsl:attribute>Starts With
      </xsl:element>
      <xsl:element name="option">
        <xsl:attribute name="value">Operator:not starts</xsl:attribute>Does Not Start With
      </xsl:element>
      <xsl:element name="option">
        <xsl:attribute name="value">Operator:IN</xsl:attribute>IN
      </xsl:element>
      <xsl:element name="option">
        <xsl:attribute name="value">Operator:NOT IN</xsl:attribute>NOT IN
      </xsl:element>
      <xsl:element name="option">
        <xsl:attribute name="value">Operator:IS NULL</xsl:attribute>IS NULL
      </xsl:element>
      <xsl:element name="option">
        <xsl:attribute name="value">Operator:IS NOT NULL</xsl:attribute>IS NOT NULL
      </xsl:element>
  </xsl:template>
  <xsl:template name="short-operator-list">
      <xsl:element name="option"></xsl:element>
      <xsl:element name="option">
        <xsl:attribute name="value">Operator:=</xsl:attribute>=
      </xsl:element>
      <xsl:element name="option">
        <xsl:attribute name="value">Operator:!=</xsl:attribute>Not equal (!=)
      </xsl:element>
      <xsl:element name="option">
        <xsl:attribute name="value">Operator:&lt;</xsl:attribute>&lt;
      </xsl:element>
      <xsl:element name="option">
        <xsl:attribute name="value">Operator:&gt;</xsl:attribute>&gt;
      </xsl:element>
      <xsl:element name="option">
        <xsl:attribute name="value">Operator:&lt;=</xsl:attribute>&lt;=
      </xsl:element>
      <xsl:element name="option">
        <xsl:attribute name="value">Operator:&gt;=</xsl:attribute>&gt;=
      </xsl:element>
  </xsl:template>
</xsl:stylesheet>


