<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
version="1.0">
<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- Author  : CRS Group                         -->
<!-- Date    : 8/15/2003                         -->
<!-- Version : Version 1                         -->
<!-- Purpose : centralized collection of all the -->
<!--           common templates that are used    -->
<!--           throughout the entire program     -->
<!-- Last                                        -->
<!-- Updated : 8/15/2003                         -->
<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- template parentTable                        -->
<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- COMMON TEMPLATE -->
  <xsl:template name="parentTable">
    <xsl:param name="section" />
    <table summary="" width="95%" border="0" cellspacing="0"
    cellpadding="0">
      <tr>
        <td class="bord">
          <table summary="" width="100%" border="0"
          cellspacing="1" cellpadding="2">
<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- call sectionName template here              -->
<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
            <xsl:choose>
              <xsl:when test="$section='section_links'">
                <xsl:call-template name="sectionName">
                  <xsl:with-param name="sectionTitle">
                  Links</xsl:with-param>
                </xsl:call-template>
                <xsl:call-template name="parentRow_Links">
                </xsl:call-template>
              </xsl:when>
              <xsl:when
              test="$section='section_availableColumns'">
                <xsl:call-template name="sectionName">
                  <xsl:with-param name="sectionTitle">Available
                  Columns</xsl:with-param>
                </xsl:call-template>
                <xsl:call-template
                name="parentRow_AvailableColumns">
                </xsl:call-template>
              </xsl:when>
              <xsl:when
              test="$section='section_availableUpdateColumns'">
                <xsl:call-template name="sectionName">
                  <xsl:with-param name="sectionTitle">Available
                  Columns</xsl:with-param>
                </xsl:call-template>
                <xsl:call-template
                name="updateParentRow_AvailableColumns">
                </xsl:call-template>
              </xsl:when>
              <xsl:when test="$section='section_insertColumns'">
                <xsl:call-template name="sectionName">
                  <xsl:with-param name="sectionTitle">Available
                  Columns</xsl:with-param>
                </xsl:call-template>
                <xsl:call-template
                name="parentRow_AvailableInsertColumns">
                </xsl:call-template>
              </xsl:when>
              <xsl:when test="$section='section_filters'">
                <xsl:call-template name="sectionName">
                  <xsl:with-param name="sectionTitle">
                  Filters</xsl:with-param>
                </xsl:call-template>
                <xsl:call-template name="parentRow_Filters">
                </xsl:call-template>
              </xsl:when>
              <xsl:when test="$section='section_CatFilters'">
                <xsl:call-template name="sectionName">
                  <xsl:with-param name="sectionTitle">Categorical
                  Filters</xsl:with-param>
                </xsl:call-template>
                <xsl:call-template name="parentRow_CatFilters">
                </xsl:call-template>
              </xsl:when>
              <xsl:when test="$section='section_OrFilters'">
                <xsl:call-template name="sectionName">
                  <xsl:with-param name="sectionTitle">Or
                  Filters</xsl:with-param>
                </xsl:call-template>
                <xsl:call-template name="parentRow_OrFilters">
                </xsl:call-template>
              </xsl:when>
              <xsl:when test="$section='section_CompareFieldsFilters'">
                <xsl:call-template name="sectionName">
                  <xsl:with-param name="sectionTitle">Field Comparisons</xsl:with-param>
                </xsl:call-template>
                <xsl:call-template name="parentRow_CompareFieldFilters">
                </xsl:call-template>
              </xsl:when>
              <xsl:when test="$section='section_RangeFilters'">
                <xsl:call-template name="sectionName">
                  <xsl:with-param name="sectionTitle">Range
                  Filters</xsl:with-param>
                </xsl:call-template>
                <xsl:call-template name="parentRow_RangeFilters">
                </xsl:call-template>
              </xsl:when>
              <xsl:when test="$section='section_TimeFilter'">
                <xsl:call-template name="sectionName">
                  <xsl:with-param name="sectionTitle">Time
                  Filter</xsl:with-param>
                </xsl:call-template>
                <xsl:call-template name="parentRow_TimeFilter">
                </xsl:call-template>
              </xsl:when>
	       <xsl:when test="$section='section_Analysis'">
                <xsl:call-template name="sectionName">
                  <xsl:with-param name="sectionTitle">Analysis
                  Output</xsl:with-param>
                </xsl:call-template>
                <xsl:call-template name="parentRow_Analysis">
                </xsl:call-template>
              </xsl:when>
	       <xsl:when test="$section='section_Pivot'">
                <xsl:call-template name="sectionName">
                  <xsl:with-param name="sectionTitle">Pivot Results</xsl:with-param>
                </xsl:call-template>
                <xsl:call-template name="parentRow_Pivot">
                </xsl:call-template>
              </xsl:when>
              <xsl:otherwise>
                <xsl:call-template name="sectionName">
                  <xsl:with-param name="sectionTitle">
                  MISCELLANEOUS</xsl:with-param>
                </xsl:call-template>
              </xsl:otherwise>
            </xsl:choose>
          </table>
        </td>
      </tr>
    </table>
  </xsl:template>
<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- template sectionName                        -->
<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- COMMON TEMPLATE -->
  <xsl:template name="sectionName">
    <xsl:param name="sectionTitle" />
    <tr>
      <th>
        <xsl:value-of select="$sectionTitle" />
      </th>
    </tr>
  </xsl:template>
<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- template sectionHelpLink                    -->
<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- COMMON TEMPLATE -->
  <xsl:template name="sectionHelpLink">
    <xsl:param name="helpLinkName" />
    <center>
      <xsl:element name="a">
        <xsl:attribute name="href">help</xsl:attribute>
        <xsl:attribute name="onclick">window.open('help/step2.html#<xsl:value-of select="$helpLinkName" />', 'acepopup', 'width=640,height=480,status=yes,scrollbars=yes,toolbar=yes,resizable' );return false;</xsl:attribute>
        Help with this Section?
      </xsl:element>
      <br />
    </center>
  </xsl:template>
<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- template dropDownvalues                     -->
<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- COMMON TEMPLATE -->
  <xsl:template name="dropDownValues">
    <xsl:param name="temp" />
    <xsl:for-each select="//field">
      <xsl:sort select="order" order="ascending" data-type="number" />
      <xsl:element name="option"><xsl:attribute name="value">Table<xsl:value-of select="$temp" />:<xsl:value-of select="table" />~Field<xsl:value-of select="$temp" />:<xsl:value-of select="column" /></xsl:attribute><xsl:value-of select="table" />.<xsl:value-of select="column" /></xsl:element>
    </xsl:for-each>
  </xsl:template>
<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- template operators                          -->
<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- COMMON TEMPLATE -->
  <xsl:template name="operators">
    <xsl:param name="operator_type" />
    <xsl:param name="operator_num" />
    <xsl:element name="option">
      <xsl:attribute name="value">Operator<xsl:value-of select="$operator_num" />:<xsl:value-of select="$operator_type" /></xsl:attribute>
      <xsl:choose>
        <xsl:when test="string-length($operator_type)=0">
        </xsl:when>
        <xsl:when test="$operator_type='!='">Not equal
        (!=)</xsl:when>
        <xsl:when test="$operator_type='like'">
        Contains</xsl:when>
        <xsl:when test="$operator_type='starts'">Starts
        With</xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="$operator_type" />
        </xsl:otherwise>
      </xsl:choose>
    </xsl:element>
    <xsl:if test="string-length($operator_type)!=0">
      <option>
      </option>
    </xsl:if>
    <xsl:call-template name="operator-list">
    </xsl:call-template>
  </xsl:template>
<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- template short-operators                    -->
<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- COMMON TEMPLATE -->
  <xsl:template name="short-operators">
    <xsl:param name="operator_type" />
    <xsl:param name="operator_num" />
    <xsl:element name="option">
      <xsl:attribute name="value">Operator<xsl:value-of select="$operator_num" />:<xsl:value-of select="$operator_type" /></xsl:attribute>
      <xsl:choose>
        <xsl:when test="string-length($operator_type)=0">
        </xsl:when>
        <xsl:when test="$operator_type='!='">Not equal
        (!=)</xsl:when>
        <xsl:when test="$operator_type='like'">
        Contains</xsl:when>
        <xsl:when test="$operator_type='starts'">Starts
        With</xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="$operator_type" />
        </xsl:otherwise>
      </xsl:choose>
    </xsl:element>
    <xsl:if test="string-length($operator_type)!=0">
      <option>
      </option>
    </xsl:if>
    <xsl:call-template name="short-operator-list">
    </xsl:call-template>
  </xsl:template>
<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- template dateFormats                        -->
<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- COMMON TEMPLATE -->
  <xsl:template name="dateFormats">
    <xsl:param name="date_fmt" />
    <xsl:element name="option">
      <xsl:attribute name="value">Format:
      <xsl:value-of select="$date_fmt" />
      </xsl:attribute>
      <xsl:value-of select="$date_fmt" />
    </xsl:element>
    <option>
    </option>
    <option value="Format:yyyy">yyyy</option>
    <option value="Format:MM/yy">MM/yy</option>
    <option value="Format:MM/dd">MM/dd</option>
    <option value="Format:MM/yyyy">MM/yyyy</option>
    <option value="Format:MM/dd/yyyy">MM/dd/yyyy</option>
    <option value="Format:yyyy MMMM d">yyyy MMMM d</option>
    <option value="Format:d MMMM yyyy">d MMMM yyyy</option>
    <option value="Format:MM/dd/yyyy hh:mm a">MM/dd/yyyy hh:mm
    a</option>
    <option value="Format:MM/dd/yyyy HH:mm">MM/dd/yyyy
    HH:mm</option>
    <option value="Format:dd/MMM/yyyy:hh:mm:ss">DD/MON/YYYY:HH:MM:SS</option>
    <option value="Format:MM/dd/yyyy hh:mm:ss a">MM/dd/yyyy
    hh:mm:ss a</option>
    <option value="Format:MM/dd/yyyy HH:mm:ss">MM/dd/yyyy
    HH:mm:ss</option>
    <option value="Format:yyyy MMMM d, HH:mm:ss">yyyy MMMM d,
    HH:mm:ss</option>
    <option value="Format:d MMMM yyyy, HH:mm:ss">d MMMM yyyy,
    HH:mm:ss</option>
  </xsl:template>
<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- template numberFormats                      -->
<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- COMMON TEMPLATE -->
  <xsl:template name="numberFormats">
    <xsl:element name="option">
    <xsl:attribute name="value">Format:<xsl:value-of select="format"/></xsl:attribute>
    <xsl:value-of select="format"/></xsl:element>
    <option>As Stored
    </option>
    <option value="Format:#">#</option>
    <option value="Format:#.#">#.#</option>
    <option value="Format:#.##">#.##</option>
    <option value="Format:#.0">#.0</option>
    <option value="Format:#,###">#,###</option>
    <option value="Format:#,###.0">#,###.0</option>
    <option value="Format:#,##0.0">#,##0.0</option>
    <option value="Format:#.##%">#.##%</option>
    <option value="Format:$#,##0.00">$#,##0.00</option>
    <option value="Format:$#,##0.00;($#,##0.00)">
    $#,##0.00;($#,##0.00)</option>
    <option value="Format:#.###E0">#.###E0</option>
    <option value="Format:#.#####E0">#.#####E0</option>
  </xsl:template>
</xsl:stylesheet>

