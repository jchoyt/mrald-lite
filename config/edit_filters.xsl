<?xml version="1.0"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- Author  : CRS Group                         -->
<!-- Date    : 8/15/2003                         -->
<!-- Version : Version 1                         -->
<!-- Purpose : centralized collection of all the -->
<!--           templates used for the filter     -->
<!--           section of the program            -->
<!-- Last                                        -->
<!-- Updated : 8/15/2003                         -->
<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->

<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- template filtersMain                      -->
<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- SPECIFIC TO FILTERS SECTION -->
<xsl:template name="filtersMain">
  <xsl:call-template name="parentTable">
    <xsl:with-param name="section">section_filters</xsl:with-param>
  </xsl:call-template>
</xsl:template>

<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- template parentRow_Filters                  -->
<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- SPECIFIC TO FILTERS SECTION -->
<xsl:template name="parentRow_Filters">
<xsl:variable name="filtersNo"><xsl:value-of select="position()"/></xsl:variable>
  <tr>
    <td>
      <xsl:call-template name="sectionHelpLink">
        <xsl:with-param name="helpLinkName">Filters</xsl:with-param>
      </xsl:call-template>

      <xsl:for-each select="//filter">
        <xsl:call-template name="filters">
          <xsl:with-param name="filters_num"><xsl:value-of select="position()"/></xsl:with-param>
        </xsl:call-template>
        
      </xsl:for-each>

      <xsl:call-template name="filters">
        <xsl:with-param name="filters_num">A</xsl:with-param>
      </xsl:call-template>
      

      <xsl:call-template name="filters">
        <xsl:with-param name="filters_num">B</xsl:with-param>
      </xsl:call-template>
      

      <xsl:call-template name="filters">
        <xsl:with-param name="filters_num">C</xsl:with-param>
      </xsl:call-template>
      

      <xsl:call-template name="filters">
        <xsl:with-param name="filters_num">D</xsl:with-param>
      </xsl:call-template>
      
  </td>
  </tr>
</xsl:template>

<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- template filters                            -->
<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- SPECIFIC TO FILTERS SECTION -->
<xsl:template name="filters">
<xsl:param name="filters_num"/>
<table cellspacing="0" cellpadding="0" border="0">
  <tr>
   <td>
     <strong>Title:</strong>
   </td>
   <td>
     <xsl:element name="input">
       <xsl:attribute name="name">Filter<xsl:value-of select="$filters_num"/>~label</xsl:attribute>
       <xsl:attribute name="type">text</xsl:attribute>
       <xsl:attribute name="size">30</xsl:attribute>
       <xsl:attribute name="value"><xsl:value-of select="label"/></xsl:attribute>
     </xsl:element>
   </td>
 </tr>
 <tr>
   <td>
     <strong>Field:</strong>
   </td>
   <td>
     <xsl:element name="select">
       <xsl:attribute name="name">Filter<xsl:value-of select="$filters_num"/></xsl:attribute>
       <xsl:element name="option">
         <xsl:attribute name="value">Table:<xsl:value-of select="table"/>~Field:<xsl:value-of select="column"/></xsl:attribute>
           <xsl:choose>
             <xsl:when test="not(//filter/table)"></xsl:when>
             <xsl:when test="table"><xsl:value-of select="table"/>.<xsl:value-of select="column"/></xsl:when>
             <xsl:otherwise></xsl:otherwise>
           </xsl:choose>
       </xsl:element>
       <xsl:if test="label"><option></option></xsl:if>
       <xsl:call-template name="dropDownValues"></xsl:call-template>
     </xsl:element>

   </td>
   <td>
     <strong>Operator:</strong>
   </td>
   <td>
    <xsl:element name="select">
       <xsl:attribute name="name">Filter<xsl:value-of select="$filters_num"/></xsl:attribute>
       <xsl:call-template name="operators">
         <xsl:with-param name="operator_type"><xsl:value-of select="operator"/></xsl:with-param>
       </xsl:call-template>
     </xsl:element>

   </td>
 </tr>
 <tr>
   <td>
     <strong>Values:</strong>
   </td>
   <td colspan="4">
     <xsl:element name="input">
       <xsl:attribute name="name">Filter<xsl:value-of select="$filters_num"/></xsl:attribute>
       <xsl:attribute name="type">text</xsl:attribute>
       <xsl:attribute name="size">22</xsl:attribute>
       <xsl:attribute name="value">
         <xsl:for-each select="value">
           <xsl:value-of select="."/>
           <xsl:if test="position()!=last()">,</xsl:if>
         </xsl:for-each>
       </xsl:attribute>

     </xsl:element>
     (Comma delmited list. "this , that" will result in two values "this " and " that")
   </td>
 </tr>
</table>

</xsl:template>


</xsl:stylesheet>
