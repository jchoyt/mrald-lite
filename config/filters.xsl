<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:include href="operators.xsl"/>
  <xsl:template name="Filters">
    <xsl:param name="advanced"/>
    <xsl:variable name="numOrs">
      <xsl:number value="count(//filter)"/>
    </xsl:variable>
    <xsl:variable name="numRanges">
      <xsl:number value="count(//range)"/>
    </xsl:variable>
      <h1 style="border-top: none; padding-top: 0;margin-top:0em;">Filters
      <xsl:text disable-output-escaping="yes">
        <![CDATA[<a href="<%=Config.getProperty("BaseUrl")%>/UserGuide/UserGuide.html#UseStep3" target="_blank">
        <img alt="help" src="<%=Config.getProperty("BaseUrl")%>/images/green-help.jpeg" />
        </a>]]>
      </xsl:text>
      </h1>
      <br />
      <!-- Do the time first -->
      <xsl:apply-templates select="time" />
      <!-- Do the Categorical filters -->
      <xsl:call-template name="catFilters" />
      <!-- Do the normal filters next -->
      <xsl:call-template name="filters">
        <xsl:with-param name="advanced"><xsl:value-of select="$advanced" /></xsl:with-param>
      </xsl:call-template>
      <!-- Do the Or filters next -->
      <xsl:call-template name="orFilters">
        <xsl:with-param name="advanced"><xsl:value-of select="$advanced" /></xsl:with-param>
      </xsl:call-template>
      <!-- Do the Field comparison filters next -->
      <xsl:call-template name="compareFieldsFilters">
        <xsl:with-param name="advanced"><xsl:value-of select="$advanced" /></xsl:with-param>
      </xsl:call-template>
      <!-- Do the range  next -->
      <xsl:call-template name="rangeFilters" />
      <!--xsl:if test="$advanced='true'"-->
        <p class="filter">

          <!-- Three General filters  -->
          <strong>General Filters</strong> - Select field, operator, and filter value<br />
            <xsl:call-template name="filterbody">
              <xsl:with-param name="pass">
                <xsl:number value="1+$numOrs+$numRanges" />
              </xsl:with-param>
            </xsl:call-template>
            <xsl:call-template name="filterbody">
              <xsl:with-param name="pass">
                <xsl:number value="2+$numOrs+$numRanges" />
              </xsl:with-param>
            </xsl:call-template>
            <xsl:call-template name="filterbody">
              <xsl:with-param name="pass">
                <xsl:number value="3+$numOrs+$numRanges" />
              </xsl:with-param>
            </xsl:call-template>

          <!-- Two Pivot filters -->
          <xsl:if test="//pivot">
          <strong>Attribute Filters</strong> - Select attribute, operator, and filter value<br />
            <xsl:call-template name="pivotfilterbody">
              <xsl:with-param name="pass">
                <xsl:number value="1" />
              </xsl:with-param>
            </xsl:call-template>
            <xsl:call-template name="pivotfilterbody">
              <xsl:with-param name="pass">
                <xsl:number value="2" />
              </xsl:with-param>
            </xsl:call-template>

		<!-- Add a multi-attribute filter in the advanced form -->
            <xsl:if test="$advanced='true'">
              <xsl:call-template name="pivotaggregatefilterbody">
                <xsl:with-param name="pass">
                  <xsl:number value="1" />
                </xsl:with-param>
              </xsl:call-template>
		</xsl:if>

          </xsl:if>
        <!-- End Filter Selections -->
        </p>
    <!--/xsl:if-->
  </xsl:template>

  <xsl:template name="filters">
    <xsl:param name="advanced"/>
    <xsl:for-each select="filter">
      <p class="filter">
      <xsl:variable name="filterno">
        <xsl:value-of select="position()"/>
      </xsl:variable>
      <!-- grab the type value from the corresponding field's entry -->
      <!-- i.e., find the 'field' element with child nodes table and column that
          this filter's table and column values, and grab the type -->
      <xsl:variable name="table">
        <xsl:value-of select="table"/>
      </xsl:variable>
      <xsl:variable name="column">
        <xsl:value-of select="column"/>
      </xsl:variable>
      <xsl:variable name="type">
        <xsl:value-of select="//formElements/field[table=$table and column=$column]/type"/>
      </xsl:variable>
      <strong>
        <xsl:value-of select="label"/>
      </strong>
      <br />
      <!-- list the 'or' boxes -->
      <xsl:for-each select="value">
        <xsl:element name="input">
          <xsl:attribute name="type">checkbox</xsl:attribute>
          <xsl:attribute name="name">Filter<xsl:value-of select="$filterno"/></xsl:attribute>
          <xsl:attribute name="value">Value:<xsl:value-of select="."/></xsl:attribute>
        </xsl:element>
          <xsl:value-of select="."/>
        <xsl:text disable-output-escaping="yes">
          <![CDATA[<img alt="" src="<%=Config.getProperty("BaseUrl")%>/images/spacer.gif" width="10" height="1"/>]]>
        </xsl:text>
        </xsl:for-each>
      <xsl:element name="input">
        <xsl:attribute name="type">hidden</xsl:attribute>
        <xsl:attribute name="name">Filter<xsl:value-of select="$filterno"/></xsl:attribute>
        <xsl:attribute name="value">Table:<xsl:value-of select="table"/>~Field:<xsl:value-of select="column"/>~Operator:<xsl:value-of select="operator"/>~Type:<xsl:value-of select="$type"/></xsl:attribute>
      </xsl:element>
      <!-- One type in box ??-->
      <xsl:if test="$advanced='true'">
        <br />Others:
        <xsl:element name="input">
          <xsl:attribute name="type">text</xsl:attribute>
          <xsl:attribute name="name">Filter<xsl:value-of select="$filterno"/></xsl:attribute>
          <xsl:attribute name="size">5</xsl:attribute>
        </xsl:element>
        <xsl:element name="input">
          <xsl:attribute name="type">text</xsl:attribute>
          <xsl:attribute name="name">Filter<xsl:value-of select="$filterno"/></xsl:attribute>
          <xsl:attribute name="size">5</xsl:attribute>
        </xsl:element>
        <xsl:element name="input">
          <xsl:attribute name="type">text</xsl:attribute>
          <xsl:attribute name="name">Filter<xsl:value-of select="$filterno"/></xsl:attribute>
          <xsl:attribute name="size">5</xsl:attribute>
        </xsl:element>
      </xsl:if>
      </p>
    </xsl:for-each>
  </xsl:template>

  <xsl:template name="orFilters">
    <xsl:param name="advanced"/>
    <xsl:for-each select="orFilter">
      <p class="filter">
      <xsl:variable name="filterno">
        <xsl:value-of select="position()"/>
      </xsl:variable>
      <strong>
        <xsl:value-of select="label"/>
      </strong>
      <br />
      <!-- add the hidden info common to all-->
      <xsl:element name="input">
        <xsl:attribute name="type">hidden</xsl:attribute>
        <xsl:attribute name="name">OrFilter<xsl:value-of select="$filterno"/></xsl:attribute>
        <xsl:attribute name="value">Table1:<xsl:value-of select="table1"/>~Field1:<xsl:value-of select="column1"/>~Operator1:<xsl:value-of select="operator1"/>~Table2:<xsl:value-of select="table2"/>~Field2:<xsl:value-of select="column2"/>~Operator2:<xsl:value-of select="operator2"/></xsl:attribute>
      </xsl:element>
      <!-- One for none -->
      <xsl:element name="input">
        <xsl:attribute name="type">radio</xsl:attribute>
        <xsl:attribute name="name">OrFilter<xsl:value-of select="$filterno"/></xsl:attribute>
        <xsl:attribute name="value"></xsl:attribute>
        <xsl:attribute name="checked">checked</xsl:attribute>
        <xsl:text>none </xsl:text>
      </xsl:element>
      <xsl:text disable-output-escaping="yes">
        <![CDATA[<img alt="" src="<%=Config.getProperty("BaseUrl")%>/images/spacer.gif" width="10" height="1"/>]]>
      </xsl:text>
      <!-- list the 'or' radio buttons -->
      <xsl:for-each select="value">
        <xsl:element name="input">
          <xsl:attribute name="type">radio</xsl:attribute>
          <xsl:attribute name="name">OrFilter<xsl:value-of select="$filterno"/></xsl:attribute>
          <xsl:attribute name="value">Value:<xsl:value-of select="."/></xsl:attribute>
        </xsl:element>
          <xsl:value-of select="."/>
        <xsl:text disable-output-escaping="yes">
          <![CDATA[<img alt="" src="<%=Config.getProperty("BaseUrl")%>/images/spacer.gif" width="10" height="1"/>]]>
        </xsl:text>
      </xsl:for-each>
      <xsl:if test="$advanced='true'">
        <br />Other: <xsl:element name="input">
          <xsl:attribute name="type">radio</xsl:attribute>
          <xsl:attribute name="name">OrFilter<xsl:value-of select="$filterno"/></xsl:attribute>
          <xsl:attribute name="value"></xsl:attribute>
          <xsl:attribute name="value">Table1:<xsl:value-of select="table1"/>~Field1:<xsl:value-of select="column1"/>~Operator1:<xsl:value-of select="operator1"/>Table2:<xsl:value-of select="table2"/>~Field2:<xsl:value-of select="column"/>~Operator2:<xsl:value-of select="operator2"/></xsl:attribute>
        </xsl:element>
        <xsl:element name="input">
          <xsl:attribute name="type">text</xsl:attribute>
          <xsl:attribute name="name">OrFilter<xsl:value-of select="$filterno"/></xsl:attribute>
        </xsl:element>
      </xsl:if>
      </p>
    </xsl:for-each>
  </xsl:template>

  <xsl:template name="compareFieldsFilters">
    <xsl:param name="advanced"/>
    <xsl:for-each select="compareFieldsFilter">
      <p class="filter">
      <xsl:variable name="filterno">
        <xsl:value-of select="position()"/>
      </xsl:variable>
      <!-- add the hidden info common to all-->
      <xsl:element name="input">
        <xsl:attribute name="type">hidden</xsl:attribute>
        <xsl:attribute name="name">CompareFieldsFilter<xsl:value-of select="$filterno"/></xsl:attribute>
        <xsl:attribute name="value">Table1:<xsl:value-of select="table1"/>~Field1:<xsl:value-of select="column1"/>~Operator:<xsl:value-of select="operator"/>~Table2:<xsl:value-of select="table2"/>~Field2:<xsl:value-of select="column2"/>~LookForCheck:true</xsl:attribute>
      </xsl:element>
      <xsl:element name="input">
        <xsl:attribute name="type">checkbox</xsl:attribute>
        <xsl:attribute name="name">CompareFieldsFilter<xsl:value-of select="$filterno"/></xsl:attribute>
        <!-- <xsl:attribute name="checked">checked</xsl:attribute> -->
        <xsl:attribute name="value">checked:true</xsl:attribute>
      </xsl:element>
      <strong>
        <xsl:value-of select="label"/>
      </strong>
      <br />
      </p>
    </xsl:for-each>
  </xsl:template>

  <xsl:template name="catFilters">
    <!-- use a Name that starts with Filter so it get processed like a regular filter, but
    need to separate it from the regular filters, so use the name FilterCategorical##-->
    <xsl:for-each select="categoricalfilter">
      <!-- grab the type value from the corresponding field's entry -->
      <!-- i.e., find the 'field' element with child nodes table and column that
          this filter's table and column values, and grab the type -->
      <xsl:variable name="table">
        <xsl:value-of select="table"/>
      </xsl:variable>
      <xsl:variable name="column">
        <xsl:value-of select="column"/>
      </xsl:variable>
      <xsl:variable name="type">
        <xsl:value-of select="//formElements/field[table=$table and column=$column]/type"/>
      </xsl:variable>
      <!-- start filter text -->
      <p class="filter">
      <xsl:variable name="filterno">
        <xsl:value-of select="position()"/>
      </xsl:variable>
      <strong>
        <xsl:value-of select="label"/>
      </strong>
      <br />
      <!-- hidden tag for filter info -->
      <xsl:element name="input">
        <xsl:attribute name="type">hidden</xsl:attribute>
        <xsl:attribute name="name">FilterCategorical<xsl:value-of select="$filterno"/></xsl:attribute>
        <xsl:attribute name="value">Table:<xsl:value-of select="table"/>~Field:<xsl:value-of select="column"/>~Operator:<xsl:value-of select="operator"/>~Type:<xsl:value-of select="$type"/></xsl:attribute>
      </xsl:element>
      <!-- the outer select tags for the drop down -->
      <xsl:element name="select">
        <xsl:attribute name="name">FilterCategorical<xsl:value-of select="$filterno"/></xsl:attribute>
        <option value=""></option>
        <!-- the drop down tag -->
         <xsl:text disable-output-escaping="yes">
            <![CDATA[<mrald:dropDownList]]></xsl:text> table="<xsl:value-of select="category-table"/>" pkColumn="<xsl:value-of select="category-field"/>" listColumn="<xsl:value-of select="list-column"/>" datasource="<xsl:value-of select="//datasource"/>"<xsl:text disable-output-escaping="yes"><![CDATA[/>]]></xsl:text>
        </xsl:element>
        </p>
    </xsl:for-each>
  </xsl:template>

  <xsl:template name="rangeFilters">
    <xsl:for-each select="//range">
      <!-- grab the type value from the corresponding field's entry -->
      <!-- i.e., find the 'field' element with child nodes table and column that
          this filter's table and column values, and grab the type -->
      <xsl:variable name="table">
        <xsl:value-of select="table"/>
      </xsl:variable>
      <xsl:variable name="column">
        <xsl:value-of select="column"/>
      </xsl:variable>
      <xsl:variable name="type">
        <xsl:value-of select="//formElements/field[table=$table and column=$column]/type"/>
      </xsl:variable>
      <p class="filter">
      <b>Range of <xsl:value-of select="label"/></b><br/>
      <i>minimum</i>:   <xsl:element name="input">
        <xsl:attribute name="name">FilterRangeMin<xsl:number value="position()"/></xsl:attribute>
        <xsl:attribute name="type">text</xsl:attribute>
        <xsl:attribute name="size">9</xsl:attribute>
      </xsl:element>
      <xsl:element name="input">
        <xsl:attribute name="name">FilterRangeMin<xsl:number value="position()"/></xsl:attribute>
        <xsl:attribute name="value">Table:<xsl:value-of select="table"/>~Field:<xsl:value-of select="column"/>~Operator:&gt;=~Type:<xsl:value-of select="$type"/></xsl:attribute>
        <xsl:attribute name="type">hidden</xsl:attribute>
      </xsl:element>
      (inclusive)<br/>
      <i>maximum</i>: <xsl:element name="input">
        <xsl:attribute name="name">FilterRangeMax<xsl:number value="position()"/></xsl:attribute>
        <xsl:attribute name="type">text</xsl:attribute>
        <xsl:attribute name="size">9</xsl:attribute>
      </xsl:element>
      <xsl:element name="input">
        <xsl:attribute name="name">FilterRangeMax<xsl:number value="position()"/></xsl:attribute>
        <xsl:attribute name="value">Table:<xsl:value-of select="table"/>~Field:<xsl:value-of select="column"/>~Operator:&lt;=~Type:<xsl:value-of select="$type"/></xsl:attribute>
        <xsl:attribute name="type">hidden</xsl:attribute>
      </xsl:element>
      (inclusive)
      </p>
    </xsl:for-each>
  </xsl:template>

  <xsl:template name="filterbody">
    <xsl:param name="pass">
      <xsl:number value="1"/>
    </xsl:param>


        <xsl:element name="select">
          <xsl:attribute name="name">Filter<xsl:number value="$pass"/></xsl:attribute>
		  <xsl:attribute name="id">Filter<xsl:number value="$pass"/>List</xsl:attribute>
          <xsl:element name="option"></xsl:element>
	  <xsl:choose>
        <xsl:when test="/MraldForm/multiDb='yes'">
          <xsl:for-each select="//field[@filter='yes']">
            <xsl:sort select="order" data-type="number" order="ascending"/>
            <xsl:element name="option">
              <xsl:attribute name="value">DBName:<xsl:value-of select="dbName"/>~Schema:<xsl:value-of select="schemaName"/>~Table:<xsl:value-of select="table"/>~Field:<xsl:value-of select="column"/>~Type:<xsl:value-of select="type"/>~SqlThread:<xsl:value-of select="sqlThread"/></xsl:attribute>
              <xsl:value-of select="label"/>
            </xsl:element>
          </xsl:for-each>

	  </xsl:when>
	<xsl:otherwise>
		<xsl:for-each select="//field[@filter='yes']">
			<xsl:sort select="order" data-type="number" order="ascending"/>
			<xsl:element name="option">
			<xsl:attribute name="value">Table:<xsl:value-of select="table"/>~Field:<xsl:value-of select="column"/>~Type:<xsl:value-of select="type"/>~SqlThread:<xsl:value-of select="sqlThread"/></xsl:attribute>
			<xsl:value-of select="label"/>
			</xsl:element>
		</xsl:for-each>
	</xsl:otherwise>
	</xsl:choose>
        </xsl:element>


        <xsl:element name="select">
          <xsl:attribute name="name">Filter<xsl:number value="$pass"/></xsl:attribute>
          <xsl:call-template name="operator-list"></xsl:call-template>
          </xsl:element>
        <xsl:element name="input">
          <xsl:attribute name="name">Filter<xsl:number value="$pass"/></xsl:attribute>
          <xsl:attribute name="type">text</xsl:attribute>
          <xsl:attribute name="size">22</xsl:attribute>
		  <xsl:attribute name="id">Filter<xsl:number value="$pass"/>ListValue</xsl:attribute>
        </xsl:element>

		 <xsl:element name="img">
			<xsl:attribute name="src">../images/mrald_sample.jpg</xsl:attribute>
			<xsl:attribute name="height">24</xsl:attribute>
			<xsl:attribute name="width">24</xsl:attribute>
			<xsl:attribute name="onclick">showSample('popUp<xsl:number value="$pass"/>', 'Filter<xsl:number value="$pass"/>List' ,'../')</xsl:attribute>
		</xsl:element>Display Sample

		<xsl:element name="span">
			<xsl:attribute name="id">popUp<xsl:number value="$pass"/></xsl:attribute>

			<input type="hidden" name="dummy"></input>
		</xsl:element>

		<br />
  </xsl:template>

  <xsl:template name="pivotfilterbody">
    <xsl:param name="pass">
      <xsl:number value="1"/>
    </xsl:param>
        <xsl:element name="select">
          <xsl:attribute name="name">PivotFilter<xsl:number value="$pass"/></xsl:attribute>
		  <xsl:attribute name="id">PivotFilter<xsl:number value="$pass"/>List</xsl:attribute>
                <xsl:text disable-output-escaping="yes">&lt;%=PivotFilter.populateOptions("</xsl:text>
                <xsl:value-of select="//pivot"/>
                <xsl:text disable-output-escaping="yes">", "</xsl:text>
                <xsl:value-of select="//datasource"/>
                <xsl:text disable-output-escaping="yes">")%&gt;</xsl:text>
        </xsl:element>
        <xsl:element name="select">
          <xsl:attribute name="name">PivotFilter<xsl:number value="$pass"/></xsl:attribute>
          <xsl:call-template name="operator-list"></xsl:call-template>
          </xsl:element>
        <xsl:element name="input">
          <xsl:attribute name="name">PivotFilter<xsl:number value="$pass"/></xsl:attribute>
          <xsl:attribute name="type">text</xsl:attribute>
          <xsl:attribute name="size">22</xsl:attribute>
		  <xsl:attribute name="id">PivotFilter<xsl:number value="$pass"/>ListValue</xsl:attribute>
        </xsl:element>
	<br />
  </xsl:template>

  <xsl:template name="pivotaggregatefilterbody">
    <xsl:param name="pass">
      <xsl:number value="1"/>
    </xsl:param>
        <xsl:element name="select">
          <xsl:attribute name="name">PivotAggregateFilter<xsl:number value="$pass"/></xsl:attribute>
	    <xsl:element name="option"/>
	    <xsl:element name="option">
	      <xsl:attribute name="value"><xsl:text disable-output-escaping="yes">HavingOperator:=</xsl:text></xsl:attribute>Exactly
	    </xsl:element>
	    <xsl:element name="option">
	      <xsl:attribute name="value"><xsl:text disable-output-escaping="yes">HavingOperator:&gt;=</xsl:text></xsl:attribute>At least
	    </xsl:element>
	    <xsl:element name="option">
	      <xsl:attribute name="value"><xsl:text disable-output-escaping="yes">HavingOperator:&lt;=</xsl:text></xsl:attribute>At most
	    </xsl:element>
        </xsl:element>
        <xsl:element name="select">
          <xsl:attribute name="name">PivotAggregateFilter<xsl:number value="$pass"/></xsl:attribute>
		  <xsl:attribute name="id">PivotAggregateFilter<xsl:number value="$pass"/>List</xsl:attribute>
                <xsl:text disable-output-escaping="yes">&lt;%=PivotAggregateFilter.populateOptions("</xsl:text>
                <xsl:value-of select="//pivot"/>
                <xsl:text disable-output-escaping="yes">", "</xsl:text>
                <xsl:value-of select="//datasource"/>
                <xsl:text disable-output-escaping="yes">")%&gt;</xsl:text>
        </xsl:element>
attribute(s) whose value
        <xsl:element name="select">
          <xsl:attribute name="name">PivotAggregateFilter<xsl:number value="$pass"/></xsl:attribute>
          <xsl:call-template name="operator-list"></xsl:call-template>
          </xsl:element>
        <xsl:element name="input">
          <xsl:attribute name="name">PivotAggregateFilter<xsl:number value="$pass"/></xsl:attribute>
          <xsl:attribute name="type">text</xsl:attribute>
          <xsl:attribute name="size">22</xsl:attribute>
		  <xsl:attribute name="id">PivotAggregateFilter<xsl:number value="$pass"/>ListValue</xsl:attribute>
        </xsl:element>
	<br />
  </xsl:template>

    <!-- TODO: don't put this on if there are no date fields -->
  <xsl:template match="time">
    <div class="filter">
      <strong>Time Filter</strong>
      <br />
      Time entered based on:
      <select name="Time1">
        <option>No time filter</option>
                        <xsl:for-each select="//field[type='Date']">
                          <xsl:sort select="order" order="ascending" data-type="number" />
                          <xsl:element name="option">
                            <xsl:attribute name="value">Table:<xsl:value-of select="table"/>~Field:<xsl:value-of select="column"/></xsl:attribute>
                            <xsl:value-of select="label"/>
                          </xsl:element>
                        </xsl:for-each>
      </select>
      <table>
        <tr>
          <td class="tdbox" valign="top" style="padding-right:3em;border-right:1px solid rgb(216,210,195);">
          <strong>Start Date &amp; Time</strong>
          <br />
                      <xsl:element name="input">
                        <xsl:attribute name="name">Time1~StartDate</xsl:attribute>
                        <xsl:attribute name="type">text</xsl:attribute>
                        <xsl:attribute name="size">13</xsl:attribute>
                        <xsl:attribute name="value"><xsl:value-of select="default"/></xsl:attribute>
                      </xsl:element>
          (mm/dd/yyyy)
          <br />
          <input name="Time1~StartTime" type="text" size="13" value="00:00" />
          (hh:mm[:ss])</td>
          <td class="tdbox" valign="top" style="padding-left:3em;">
          <strong>End by</strong>
          <br />
          <input name="EnableTime" type="radio" value="EndTime" checked="checked" />
          Date &amp; Time:
          <br />
          <div style="padding-left:3em">
                      <xsl:element name="input">
                        <xsl:attribute name="name">Time1~EndDate</xsl:attribute>
                        <xsl:attribute name="type">text</xsl:attribute>
                        <xsl:attribute name="size">13</xsl:attribute>
                        <xsl:attribute name="value"><xsl:value-of select="endBy"/></xsl:attribute>
                        <xsl:attribute name="onFocus">if ( EndTimeSelected.disabled ) this.blur()</xsl:attribute>
                      </xsl:element>
          (mm/dd/yyyy)
          <br />
          <input name="Time1~EndTime" type="text" size="13" value="23:59" />
          (hh:mm[:ss])</div>
          <input name="EnableTime" type="radio" value="AddTime" />
          Duration:
          <br />
          <div style="padding-left:3em">
          <input name="Time1~Month" type="text" size="5" value="" />
          Months
          <br />
          <input name="Time1~Day" type="text" size="5" value="" />
          Days
          <br />
          <input name="Time1~Hour" type="text" size="5" value="" />
          Hours
          <br />
          <input name="Time1~Minute" type="text" size="5" value="" />
          Minutes
          <br />
          <input name="Time1~Second" type="text" size="5" value="" />
          Seconds</div>
          </td>
        </tr>
      </table>
    </div>

  </xsl:template>
</xsl:stylesheet>

