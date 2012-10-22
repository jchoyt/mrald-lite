<?xml version="1.0"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:template name="compareFieldsMain">
  <xsl:call-template name="parentTable">
    <xsl:with-param name="section">section_CompareFieldsFilters</xsl:with-param>
  </xsl:call-template>
</xsl:template>

<xsl:template name="parentRow_CompareFieldFilters">
  <tr>
    <td>
      <xsl:call-template name="sectionHelpLink">
        <xsl:with-param name="helpLinkName">FieldComparison</xsl:with-param>
      </xsl:call-template>

      <xsl:for-each select="//compareFieldsFilter">
        <xsl:call-template name="compareFieldsFilter">
          <xsl:with-param name="compareFieldsFilter_num"><xsl:value-of select="position()"/></xsl:with-param>
        </xsl:call-template>
        
      </xsl:for-each>

      <xsl:call-template name="compareFieldsFilter">
        <xsl:with-param name="compareFieldsFilter_num">A</xsl:with-param>
      </xsl:call-template>
      

      <xsl:call-template name="compareFieldsFilter">
        <xsl:with-param name="compareFieldsFilter_num">B</xsl:with-param>
      </xsl:call-template>
    </td>
  </tr>
</xsl:template>

<xsl:template name="compareFieldsFilter">
<xsl:param name="compareFieldsFilter_num"/>
  <table cellspacing="0" cellpadding="0" border="0">
   <tr>
     <td><strong>Title:</strong></td>
     <td>
       <xsl:element name="input">
         <xsl:attribute name="name">CompareFieldsFilter<xsl:value-of select="$compareFieldsFilter_num"/>~label</xsl:attribute>
         <xsl:attribute name="type">text</xsl:attribute>
         <xsl:attribute name="size">30</xsl:attribute>
         <xsl:attribute name="value"><xsl:value-of select="label"/></xsl:attribute>
       </xsl:element>
     </td>
   </tr>
   <tr>
     <td><strong>First Field:</strong></td>
     <td>
      <xsl:element name="select">
        <xsl:attribute name="name">CompareFieldsFilter<xsl:value-of select="$compareFieldsFilter_num"/></xsl:attribute>
        <xsl:element name="option">
          <xsl:choose>
            <xsl:when test="not(//compareFieldsFilter/table1)"></xsl:when>
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
     <td><strong>Operator:</strong></td>
     <td>
      <xsl:element name="select">
        <xsl:attribute name="name">CompareFieldsFilter<xsl:value-of select="$compareFieldsFilter_num"/></xsl:attribute>
        <xsl:call-template name="short-operators">
          <xsl:with-param name="operator_type"><xsl:value-of select="operator"/></xsl:with-param>
          <xsl:with-param name="operator_num">1</xsl:with-param>
        </xsl:call-template>
      </xsl:element>
     </td>
     <td><strong>Second Field:</strong></td>
     <td>
      <xsl:element name="select">
        <xsl:attribute name="name">CompareFieldsFilter<xsl:value-of select="$compareFieldsFilter_num"/></xsl:attribute>
        <xsl:element name="option">
          <xsl:choose>
            <xsl:when test="not(//compareFieldsFilter/table2)"></xsl:when>
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
   </tr>
  </table>

</xsl:template>

</xsl:stylesheet>
