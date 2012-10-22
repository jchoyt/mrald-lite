<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <!-- Create the table structure and fill it in -->
  <xsl:template name="catFiltersMain">
    <xsl:call-template name="parentTable">
      <xsl:with-param name="section">section_CatFilters</xsl:with-param>
    </xsl:call-template>
  </xsl:template>

  <xsl:template name="parentRow_CatFilters">
    <xsl:variable name="filtersNo">
      <xsl:value-of select="position()" />
    </xsl:variable>
    <tr>
      <td>
        <xsl:call-template name="sectionHelpLink">
          <xsl:with-param name="helpLinkName">
          Filters</xsl:with-param>
        </xsl:call-template>
        <xsl:for-each select="//categoricalfilter">
          <xsl:call-template name="catFilters">
            <xsl:with-param name="filters_num">
              <xsl:value-of select="position()" />
            </xsl:with-param>
          </xsl:call-template>
        </xsl:for-each>
        <xsl:call-template name="catFilters">
          <xsl:with-param name="filters_num">A</xsl:with-param>
        </xsl:call-template>
        <xsl:call-template name="catFilters">
          <xsl:with-param name="filters_num">B</xsl:with-param>
        </xsl:call-template>
        <xsl:call-template name="catFilters">
          <xsl:with-param name="filters_num">C</xsl:with-param>
        </xsl:call-template>
        <xsl:call-template name="catFilters">
          <xsl:with-param name="filters_num">D</xsl:with-param>
        </xsl:call-template>
      </td>
    </tr>
  </xsl:template>

  <xsl:template name="catFilters">
    <xsl:param name="filters_num" />
    <p class="filter">
    <strong>Title:</strong>
    <xsl:element name="input">
      <xsl:attribute name="name">CategoricalFilter<xsl:value-of select="$filters_num" />~label</xsl:attribute>
      <xsl:attribute name="type">text</xsl:attribute>
      <xsl:attribute name="size">30</xsl:attribute>
      <xsl:attribute name="value">
        <xsl:value-of select="label" />
      </xsl:attribute>
    </xsl:element>
    <br />
    <br />
    <strong>Filter on:</strong>
    <br />
    Field: 
    <xsl:element name="select">
      <xsl:attribute name="name">CategoricalFilter<xsl:value-of select="$filters_num" /></xsl:attribute>
      <xsl:element name="option">
        <xsl:attribute name="value">Table:<xsl:value-of select="table" />~Field:<xsl:value-of select="column" /></xsl:attribute>
        <xsl:choose>
          <xsl:when test="not(//filter/table)">
          </xsl:when>
          <xsl:when test="table">
          <xsl:value-of select="table" />.<xsl:value-of select="column" />
          </xsl:when>
          <xsl:otherwise>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:element>
      <xsl:if test="label">
        <option>
        </option>
      </xsl:if>
      <xsl:call-template name="dropDownValues">
      </xsl:call-template>
    </xsl:element>
    <img alt="" src="images/spacer.gif" width="25" height="1" />
    Operator: 
    <xsl:element name="select">
      <xsl:attribute name="name">CategoricalFilter<xsl:value-of select="$filters_num" /></xsl:attribute>
      <xsl:call-template name="operators">
        <xsl:with-param name="operator_type">
          <xsl:value-of select="operator" />
        </xsl:with-param>
      </xsl:call-template>
    </xsl:element>
    <br />
    <br />
    <strong>Values from:</strong>
    <br />
    Table: 
    <xsl:element name="input">
      <xsl:attribute name="name">CategoricalFilter<xsl:value-of select="$filters_num" />~CategoryTable</xsl:attribute>
      <xsl:attribute name="type">text</xsl:attribute>
      <xsl:attribute name="size">22</xsl:attribute>
      <xsl:attribute name="value">
        <xsl:value-of select="category-table" />
      </xsl:attribute>
    </xsl:element>
    <img alt="" src="images/spacer.gif" width="33" height="1" />
    Field: 
    <xsl:element name="input">
      <xsl:attribute name="name">CategoricalFilter<xsl:value-of select="$filters_num" />~CategoryField</xsl:attribute>
      <xsl:attribute name="type">text</xsl:attribute>
      <xsl:attribute name="size">22</xsl:attribute>
      <xsl:attribute name="value">
        <xsl:value-of select="category-field" />
      </xsl:attribute>
    </xsl:element>
    <br />
    Label: 
    <xsl:element name="input">
      <xsl:attribute name="name">CategoricalFilter<xsl:value-of select="$filters_num" />~DropDownLabel</xsl:attribute>
      <xsl:attribute name="type">text</xsl:attribute>
      <xsl:attribute name="size">22</xsl:attribute>
      <xsl:attribute name="value">
        <xsl:value-of select="list-column" />
      </xsl:attribute>
    </xsl:element>
    <img alt="" src="images/spacer.gif" width="33" height="1" />
    <a href="displayDB.jsp" target="_blank">Show me a list of
    all tables and columns</a>
    </p>
  </xsl:template>
</xsl:stylesheet>

