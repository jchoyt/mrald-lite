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
<xsl:template name="analysisMain">
  <xsl:call-template name="parentTable">
    <xsl:with-param name="section">section_Analysis</xsl:with-param>
  </xsl:call-template>
</xsl:template>

<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- template parentRow_TimeFilter               -->
<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- SPECIFIC TO TIME FILTER SECTION -->
<xsl:template name="parentRow_Analysis">
  <tr>
    <td>
      <xsl:call-template name="sectionHelpLink">
        <xsl:with-param name="helpLinkName">AnalysisOutput</xsl:with-param>
      </xsl:call-template>
      <xsl:for-each select="//analysis">
        <xsl:call-template name="analysis"></xsl:call-template>
      </xsl:for-each>
    </td>
  </tr>
</xsl:template>

<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- Beginning of the template timeFilterBody    -->
<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- SPECIFIC TO  ANALYSIS SECTION -->
<xsl:template name="analysis">
  <table cellspacing="0" cellpadding="0" border="0">
    <tr>
      <td width="85%">
        <b>Add Analysis As An Output Option?</b>
      </td>
      <td width="24%">
        <xsl:element name="input">
          <xsl:attribute name="name">FBAnalysis</xsl:attribute>
          <xsl:attribute name="type">checkbox</xsl:attribute>
          <xsl:attribute name="size">25</xsl:attribute>
           <xsl:attribute name="checked">yes</xsl:attribute>
         <xsl:attribute name="value">IncludeAnalysis</xsl:attribute>
         </xsl:element>
      </td>
    </tr>
  </table>
</xsl:template>


</xsl:stylesheet>        
