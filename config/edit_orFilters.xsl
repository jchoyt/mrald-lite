<?xml version="1.0"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- Author  : CRS Group                         -->
<!-- Date    : 8/15/2003                         -->
<!-- Version : Version 1                         -->
<!-- Purpose : centralized collection of all the -->
<!--           templates that are used for the   -->
<!--           or filters section of program     -->
<!-- Last                                        -->
<!-- Updated : 8/15/2003                         -->
<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->

<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- template orFiltersMain                      -->
<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- SPECIFIC TO OR FILTERS SECTION -->
<xsl:template name="orFiltersMain">
  <xsl:call-template name="parentTable">
    <xsl:with-param name="section">section_OrFilters</xsl:with-param>
  </xsl:call-template>
</xsl:template>

<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- template parentRow_OrFilters                -->
<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- SPECIFIC TO OR FILTERS SECTION -->
<xsl:template name="parentRow_OrFilters">
  <tr>
    <td>
      <xsl:call-template name="sectionHelpLink">
        <xsl:with-param name="helpLinkName">OrFilters</xsl:with-param>
      </xsl:call-template>
 
      <xsl:for-each select="//orFilter">
        <xsl:call-template name="orFilters">
          <xsl:with-param name="orFilters_num"><xsl:value-of select="position()"/></xsl:with-param>
        </xsl:call-template>
        
      </xsl:for-each>

      <xsl:call-template name="orFilters">
        <xsl:with-param name="orFilters_num">A</xsl:with-param>
      </xsl:call-template>
      

      <xsl:call-template name="orFilters">
        <xsl:with-param name="orFilters_num">B</xsl:with-param>
      </xsl:call-template>
      
    </td>
  </tr>
</xsl:template>

<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- template orFilters                          -->
<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- SPECIFIC TO OR FILTERS SECTION -->
<xsl:template name="orFilters">
<xsl:param name="orFilters_num"/>
  <table cellspacing="0" cellpadding="0" border="0">
   <tr>
     <td>
       <strong>Title:</strong>
     </td>
     <td>
       <xsl:element name="input">
         <xsl:attribute name="name">OrFilter<xsl:value-of select="$orFilters_num"/>~label</xsl:attribute>
         <xsl:attribute name="type">text</xsl:attribute>
         <xsl:attribute name="size">30</xsl:attribute>
         <xsl:attribute name="value"><xsl:value-of select="label"/></xsl:attribute>
       </xsl:element>
     </td>
   </tr>
   <tr>
     <td>
       <strong>First Field:</strong>
     </td>
     <td>
      <xsl:element name="select">
        <xsl:attribute name="name">OrFilter<xsl:value-of select="$orFilters_num"/></xsl:attribute>
        <xsl:element name="option">
          <xsl:choose>
            <xsl:when test="not(//orFilter/table1)"></xsl:when>
            <xsl:when test="table1">
              <xsl:attribute name="value">Table1:<xsl:value-of select="table1"/>~Field1:<xsl:value-of select="column1"/></xsl:attribute>
              <xsl:value-of select="table1"/>.<xsl:value-of select="column1"/>
            </xsl:when>
            <xsl:otherwise></xsl:otherwise>
          </xsl:choose>
        </xsl:element>
       <xsl:if test="label"><option></option></xsl:if>
        <xsl:call-template name="dropDownValues">
          <xsl:with-param name="temp">1</xsl:with-param>
        </xsl:call-template>
      </xsl:element>
     </td>
  
     <td>
       <strong>First Operator:</strong>
     </td>
     <td>
      <xsl:element name="select">
        <xsl:attribute name="name">OrFilter<xsl:value-of select="$orFilters_num"/></xsl:attribute>
        <xsl:call-template name="operators">
          <xsl:with-param name="operator_type"><xsl:value-of select="operator1"/></xsl:with-param>
          <xsl:with-param name="operator_num">1</xsl:with-param>
        </xsl:call-template>
      </xsl:element>
     </td>
   </tr>
  
   <tr>
     <td>
       <strong>Second Field:</strong>
     </td>
     <td>
      <xsl:element name="select">
        <xsl:attribute name="name">OrFilter<xsl:value-of select="$orFilters_num"/></xsl:attribute>
        <xsl:element name="option">
          <xsl:choose>
            <xsl:when test="not(//orFilter/table2)"></xsl:when>
            <xsl:when test="table2">
              <xsl:attribute name="value">Table2:<xsl:value-of select="table2"/>~Field2:<xsl:value-of select="column2"/></xsl:attribute>
              <xsl:value-of select="table2"/>.<xsl:value-of select="column2"/>
            </xsl:when>
            <xsl:otherwise></xsl:otherwise>
          </xsl:choose>
        </xsl:element>
       <xsl:if test="label"><option></option></xsl:if>
        <xsl:call-template name="dropDownValues">
          <xsl:with-param name="temp">2</xsl:with-param>
        </xsl:call-template>
      </xsl:element>
     </td>
     <td>
       <strong>Second Operator:</strong>
     </td>
     <td>
      <xsl:element name="select">
        <xsl:attribute name="name">OrFilter<xsl:value-of select="$orFilters_num"/></xsl:attribute>
        <xsl:call-template name="operators">
          <xsl:with-param name="operator_type"><xsl:value-of select="operator2"/></xsl:with-param>
          <xsl:with-param name="operator_num">2</xsl:with-param>
        </xsl:call-template>
      </xsl:element>
     </td>
   </tr>
   <tr>
     <td>
       <strong>Values:</strong>
     </td>
     <td colspan="3">
       <xsl:element name="input">
         <xsl:attribute name="name">OrFilter<xsl:value-of select="$orFilters_num"/></xsl:attribute>
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
