<?xml version="1.0"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- Author  : CRS Group                         -->
<!-- Date    : 8/15/2003                         -->
<!-- Version : Version 1                         -->
<!-- Purpose : centralized collection of all the -->
<!--           templates that are used for the   -->
<!--           time filter section of program    -->
<!-- Last                                        -->
<!-- Updated : 8/15/2003                         -->
<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->

<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- template timeFilterMain                     -->
<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- SPECIFIC TO TIME FILTERS SECTION -->
<xsl:template name="timeFilterMain">
  <xsl:call-template name="parentTable">
    <xsl:with-param name="section">section_TimeFilter</xsl:with-param>
  </xsl:call-template>
</xsl:template>

<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- template parentRow_TimeFilter               -->
<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- SPECIFIC TO TIME FILTER SECTION -->
<xsl:template name="parentRow_TimeFilter">
  <tr>
    <td>
      <xsl:call-template name="sectionHelpLink">
        <xsl:with-param name="helpLinkName">TimeFilter</xsl:with-param>
      </xsl:call-template>
      <xsl:for-each select="//time">
        <xsl:call-template name="timeFilter"></xsl:call-template>
      </xsl:for-each>
      <xsl:if test="not(//time)"><xsl:call-template name="timeFilter"></xsl:call-template></xsl:if>
    </td>
  </tr>
</xsl:template>

<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- Beginning of the template timeFilterBody    -->
<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- SPECIFIC TO TIME FILTER SECTION -->
<xsl:template name="timeFilter">
  <table cellspacing="0" cellpadding="0" border="0">
    <tr>
      <td width="24%">
        <b>Start Date <i>(MM/DD/YYYY)</i></b>
      </td>
      <td width="24%">
        <xsl:element name="input">
          <xsl:attribute name="name">FBTime1</xsl:attribute>
          <xsl:attribute name="type">text</xsl:attribute>
          <xsl:attribute name="size">25</xsl:attribute>
          <xsl:attribute name="value"><xsl:value-of select="default"/></xsl:attribute>
        </xsl:element>
      </td>
    </tr>
    <tr>
      <td width="24%">
        <b>End Date <i>(MM/DD/YYYY)</i></b>
      </td>
      <td width="24%">
        <xsl:element name="input">
          <xsl:attribute name="name">FBTime1~EndDate</xsl:attribute>
          <xsl:attribute name="type">text</xsl:attribute>
          <xsl:attribute name="size">25</xsl:attribute>
          <xsl:attribute name="value"><xsl:value-of select="endBy"/></xsl:attribute>
        </xsl:element>
      </td>
    </tr>
  </table>
</xsl:template>


</xsl:stylesheet>
