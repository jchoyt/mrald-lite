<?xml version="1.0"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- Author  : CRS Group                         -->
<!-- Date    : 8/15/2003                         -->
<!-- Version : Version 1                         -->
<!-- Purpose : centralized collection of all the -->
<!--           templates that are used for the   -->
<!--           range filters section of program  -->
<!-- Last                                        -->
<!-- Updated : 8/15/2003                         -->
<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->

<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- template rangeFiltersMain                   -->
<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- SPECIFIC TO RANGE FILTERS SECTION -->
<xsl:template name="rangeFiltersMain">
  <xsl:call-template name="parentTable">
    <xsl:with-param name="section">section_RangeFilters</xsl:with-param>
  </xsl:call-template>
</xsl:template>


<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- template parentRow_RangeFilters             -->
<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- SPECIFIC TO RANGE FILTERS SECTION -->
<xsl:template name="parentRow_RangeFilters">
  <tr>
    <td>
      <xsl:call-template name="sectionHelpLink">
        <xsl:with-param name="helpLinkName">RangeFilter</xsl:with-param>
      </xsl:call-template>

      <xsl:for-each select="//range">
        <xsl:call-template name="rangeFilters">
          <xsl:with-param name="range_num"><xsl:value-of select="position()"/></xsl:with-param>
        </xsl:call-template>
        
      </xsl:for-each>

      <xsl:call-template name="rangeFilters">
        <xsl:with-param name="range_num">A</xsl:with-param>
      </xsl:call-template>
      
      
      <xsl:call-template name="rangeFilters">
        <xsl:with-param name="range_num">B</xsl:with-param>
      </xsl:call-template>


    </td>
  </tr>
</xsl:template>



<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- template rangeFilters                       -->
<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- SPECIFIC TO RANGE FILTERS SECTION -->
<xsl:template name="rangeFilters">
<xsl:param name="range_num"/>  
  <strong>Field :</strong>
  
  <xsl:element name="select">
    <xsl:attribute name="name">Range<xsl:value-of select="$range_num"/></xsl:attribute>
      <xsl:element name="option">
        <xsl:choose>
          <xsl:when test="not(//range/table)"></xsl:when>
          <xsl:when test="table">
            <xsl:attribute name="value">Table:<xsl:value-of select="table"/>~Field:<xsl:value-of select="column"/></xsl:attribute>
            <xsl:value-of select="table"/>.<xsl:value-of select="column"/>
          </xsl:when>
          <xsl:otherwise></xsl:otherwise>
        </xsl:choose>
      </xsl:element>
       <xsl:if test="label"><option></option></xsl:if>
      <xsl:call-template name="dropDownValues"></xsl:call-template>
  </xsl:element>
   
  <strong> Desired Field Label :</strong>

  <xsl:element name="input">
    <xsl:attribute name="name">Range<xsl:value-of select="$range_num"/></xsl:attribute>
    <xsl:attribute name="type">text</xsl:attribute>
    <xsl:attribute name="size">22</xsl:attribute>
    <xsl:attribute name="value"><xsl:value-of select="label"/></xsl:attribute>
  </xsl:element>

</xsl:template>

</xsl:stylesheet>
