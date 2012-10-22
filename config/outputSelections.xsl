<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:template name="OutputFields">
    <xsl:variable name="numFields" select="count(//field[@output='yes'])"></xsl:variable>
      <!-- Start Output Selections -->
      <div class="leftSideBar">
      <p class="sideBarTitle">Include in output
      <xsl:text disable-output-escaping="yes">
        <![CDATA[
        <a href="<%=Config.getProperty("BaseUrl")%>/UserGuide/UserGuide.html#UseStep3" target="_blank">
        <img alt="help" src="<%=Config.getProperty("BaseUrl")%>/images/green-help.jpeg" />
        </a>]]>
      </xsl:text>
      </p>
      <xsl:choose>
        <xsl:when test="/MraldForm/multiDb='yes'">
      <xsl:for-each select="//field[@output='yes']">
        <xsl:sort select="order" data-type="number" order="ascending"/>
        <xsl:element name="input">
          <xsl:attribute name="name">Select<xsl:number value="position()" format="1"/></xsl:attribute>
          <xsl:attribute name="type">checkbox</xsl:attribute>
          <xsl:attribute name="value">DBName:<xsl:value-of select="dbName"/>~Schema:<xsl:value-of select="schemaName"/>~Table:<xsl:value-of select="table"/>~Field:<xsl:value-of select="column"/>~Order:<xsl:value-of select="order"/>~SqlThread:<xsl:value-of select="sqlThread"/></xsl:attribute>
          <xsl:attribute name="onMouseOver">if (EnableStats.value == 1) {}</xsl:attribute>
          <xsl:attribute name="onFocus">if (EnableStats.value == 1) {this.click(), this.blur()}</xsl:attribute>
          <xsl:attribute name="onDblClick">if (EnableStats.value == 1) {this.click(), this.blur()}</xsl:attribute>
          <xsl:if test="@checked='yes'">
            <xsl:attribute name="checked"></xsl:attribute>
          </xsl:if>
          <xsl:value-of select="label"/>
        </xsl:element>
        <br />
      </xsl:for-each>
      </xsl:when>
	<xsl:otherwise>
	<xsl:for-each select="//field[@output='yes']">
        <xsl:sort select="order" data-type="number" order="ascending"/>
        <xsl:element name="input">
          <xsl:attribute name="name">Select<xsl:number value="position()" format="1"/></xsl:attribute>
          <xsl:attribute name="type">checkbox</xsl:attribute>
          <xsl:attribute name="value">Table:<xsl:value-of select="table"/>~Field:<xsl:value-of select="column"/>~Order:<xsl:value-of select="order"/>~SqlThread:<xsl:value-of select="sqlThread"/></xsl:attribute>
          <xsl:attribute name="onMouseOver">if (EnableStats.value == 1) {}</xsl:attribute>
          <xsl:attribute name="onFocus">if (EnableStats.value == 1) {this.click(), this.blur()}</xsl:attribute>
          <xsl:attribute name="onDblClick">if (EnableStats.value == 1) {this.click(), this.blur()}</xsl:attribute>
          <xsl:if test="@checked='yes'">
            <xsl:attribute name="checked"></xsl:attribute>
          </xsl:if>
          <xsl:value-of select="label"/>
        </xsl:element>
        <br />
      </xsl:for-each>
      </xsl:otherwise>
    </xsl:choose>
      
      <br />
      <p class="sideBarTitle">Sort Criteria
      <xsl:text disable-output-escaping="yes">
        <![CDATA[
        <a href="<%=Config.getProperty("BaseUrl")%>/UserGuide/UserGuide.html#UseStep3" target="_blank">
        <img alt="help" src="<%=Config.getProperty("BaseUrl")%>/images/green-help.jpeg" />
        </a>]]>
      </xsl:text>
      </p>
      Sort by
      <xsl:call-template name="sortbody">
        <xsl:with-param name="pass">
          1
       </xsl:with-param>
      </xsl:call-template>
      then by
      <xsl:call-template name="sortbody">
        <xsl:with-param name="pass">
          2
       </xsl:with-param>
      </xsl:call-template>
      then by
      <xsl:call-template name="sortbody">
        <xsl:with-param name="pass">
          3
        </xsl:with-param>
      </xsl:call-template>
    </div>
      <!-- End OutputSelections -->
  </xsl:template>

  <xsl:template name="sortbody">
    <xsl:param name="pass">
      <xsl:number value="1"/>
    </xsl:param>
    <xsl:element name="select">
      <xsl:attribute name="name">Sort<xsl:number value="$pass"/></xsl:attribute>
      <xsl:element name="option"></xsl:element>
      
      <xsl:choose>
        <xsl:when test="/MraldForm/multiDb='yes'">

      <xsl:for-each select="//field[@sort='yes']">
        <xsl:sort select="order" data-type="number" order="ascending"/>
        <xsl:element name="option">
          <xsl:attribute name="value">DBName:<xsl:value-of select="dbName"/>~Schema:<xsl:value-of select="schemaName"/>~Table:<xsl:value-of select="table"/>~Field:<xsl:value-of select="column"/>~Order:<xsl:number value="$pass"/>~SqlThread:<xsl:value-of select="sqlThread"/></xsl:attribute>
          <xsl:value-of select="label"/>
        </xsl:element>
      </xsl:for-each>
      </xsl:when>
	<xsl:otherwise>
	<xsl:for-each select="//field[@sort='yes']">
        <xsl:sort select="order" data-type="number" order="ascending"/>
        <xsl:element name="option">
          <xsl:attribute name="value">Table:<xsl:value-of select="table"/>~Field:<xsl:value-of select="column"/>~Order:<xsl:number value="$pass"/>~SqlThread:<xsl:value-of select="sqlThread"/></xsl:attribute>
          <xsl:value-of select="label"/>
        </xsl:element>
      </xsl:for-each>
      </xsl:otherwise>
    </xsl:choose>
    
    </xsl:element>
    <xsl:element name="input">
      <xsl:attribute name="name">Sort<xsl:number value="$pass"/></xsl:attribute>
      <xsl:attribute name="type">checkbox</xsl:attribute>
      <xsl:attribute name="value">OrderType:DESC</xsl:attribute>
    </xsl:element>Desc<br />
  </xsl:template>

  <xsl:template name="SimpleOutputFields">
    <xsl:variable name="numFields" select="count(//field[@output='yes'])" />
    <xsl:variable name="break">
      <xsl:choose>
        <xsl:when test="$numFields &lt;= 5">
        </xsl:when>
        <xsl:when test="$numFields &gt; 5 and $numFields &lt;= 10">
          <xsl:number value="ceiling($numFields div 2)" format="1" />
        </xsl:when>
        <xsl:otherwise>
          <xsl:number value="ceiling($numFields div 3)" format="1" />
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
<!-- Start Output Selections -->
<xsl:choose>
        <xsl:when test="/MraldForm/multiDb='yes'">
		<xsl:for-each select="//field[@output='yes']">
			<xsl:if test="@checked='yes'">
				<xsl:element name="input">
				<xsl:attribute name="name">Select<xsl:number value="position()" format="1" /></xsl:attribute>
				<xsl:attribute name="type">hidden</xsl:attribute><xsl:attribute name="value">DBName:<xsl:value-of select="dbName"/>~Schema:<xsl:value-of select="schemaName"/>~Table:<xsl:value-of select="table" />~Field:<xsl:value-of select="column" />~Order:<xsl:value-of select="order" />~SqlThread:<xsl:value-of select="sqlThread"/></xsl:attribute>
				</xsl:element>
			</xsl:if>
		</xsl:for-each>
	</xsl:when>
	<xsl:otherwise>
	<xsl:for-each select="//field[@output='yes']">
		
		<xsl:if test="@checked='yes'">
				<xsl:element name="input">
				<xsl:attribute name="name">Select<xsl:number value="position()" format="1" /></xsl:attribute>
				<xsl:attribute name="type">hidden</xsl:attribute><xsl:attribute name="value">Table:<xsl:value-of select="table" />~Field:<xsl:value-of select="column" />~Order:<xsl:value-of select="order" />~SqlThread:<xsl:value-of select="sqlThread"/></xsl:attribute>
				</xsl:element>
			</xsl:if>
	</xsl:for-each>
        </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
</xsl:stylesheet>


