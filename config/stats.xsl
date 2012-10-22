<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:template name="Statistics">
    <xsl:if test="//stat[@useStats='yes']">
      <!-- Start Statistical Selections -->
      <input type="hidden" name="EnableStats" value="0" />
      <h1 style="border-top: medium none; padding-top: 0pt;">Statistical Functions
      <xsl:text disable-output-escaping="yes">
        <![CDATA[<a href="<%=Config.getProperty("BaseUrl")%>/UserGuide/UserGuide.html#UseStep4" target="_blank">
        <img alt="help" src="<%=Config.getProperty("BaseUrl")%>/images/green-help.jpeg" />
        </a>]]>
      </xsl:text>
      </h1>
      <xsl:if test="//field[@stats='yes']">
      Select statistical function and field:<br />
        <!-- Drop down stats -->
        <xsl:call-template name="statbody">
          <xsl:with-param name="pass">
            <xsl:number value="1" />
          </xsl:with-param>
        </xsl:call-template>
        <xsl:call-template name="statbody">
          <xsl:with-param name="pass">
            <xsl:number value="2" />
          </xsl:with-param>
        </xsl:call-template>
        <xsl:call-template name="statbody">
          <xsl:with-param name="pass">
            <xsl:number value="3" />
          </xsl:with-param>
        </xsl:call-template>
          <br />
          Group by
            <xsl:call-template name="groupbody">
              <xsl:with-param name="pass">
                <xsl:number value="4" />
              </xsl:with-param>
            </xsl:call-template>
            then by
            <xsl:call-template name="groupbody">
              <xsl:with-param name="pass">
                <xsl:number value="5" />
              </xsl:with-param>
            </xsl:call-template>
              then by
            <xsl:call-template name="groupbody">
              <xsl:with-param name="pass">
                <xsl:number value="6" />
              </xsl:with-param>
            </xsl:call-template>
      </xsl:if>
      <xsl:if test="not(//field[@stats='yes'])">
        <!-- Drop down stats -->
        <xsl:call-template name="countstaronly" />
            Group by
            <xsl:call-template name="groupbody">
              <xsl:with-param name="pass">
                <xsl:number value="4" />
              </xsl:with-param>
            </xsl:call-template>
            then by
            <xsl:call-template name="groupbody">
              <xsl:with-param name="pass">
                <xsl:number value="5" />
              </xsl:with-param>
            </xsl:call-template>
              then by
            <xsl:call-template name="groupbody">
              <xsl:with-param name="pass">
                <xsl:number value="6" />
              </xsl:with-param>
            </xsl:call-template>
      </xsl:if>
      <br />
      <span style="font-size:70%">*When grouping on a particular column, that column will automatically be included in the output</span>
      <!-- End Statistical Selections -->
    </xsl:if>
  </xsl:template>

  <xsl:template name="countstaronly">
        <xsl:element name="input">
          <xsl:attribute name="name">Stat1</xsl:attribute>
          <xsl:attribute name="onChange">
		     if (this.selectedIndex != 0)
		      {checkEnableCountOnly(EnableStats); for (i=1; i!=<xsl:number value="count(//field[@output='yes'])+1"></xsl:number>; i++)
		           { eval("Select" + i).checked=false; }
			   self.status="Cannot select Output Data whilst statistical function selected."; }
		       else {checkEnableCountOnly(EnableStats);self.status="";} if (EnableStats.value == 1) {
		          Enable(  document.getElementById('chartId') ); }
			  else{ Disable(  document.getElementById('chartId') ); }
			  </xsl:attribute>
          <xsl:attribute name="type">checkbox</xsl:attribute>
          <xsl:attribute name="value">Function:Count(*)~Order:1~Type:<xsl:value-of select="type"/><xsl:for-each select="//field">~Table:<xsl:value-of select="table" /></xsl:for-each></xsl:attribute>
        </xsl:element>
          Count(*)<br />
  </xsl:template>

  <xsl:template name="statbody">
    <xsl:param name="pass">
      <xsl:number value="1"/>
    </xsl:param>
        <xsl:element name="select">
          <xsl:attribute name="name">Stat<xsl:number value="$pass"/></xsl:attribute>
          <xsl:attribute name="onChange">
            if (this.selectedIndex != 0)
		      {checkEnable(EnableStats); for (i=1; i!=<xsl:number value="count(//field[@output='yes'])+1"></xsl:number>; i++)
		           { eval("Select" + i).checked=false; }
			   self.status="Cannot select Output Data whilst statistical function selected."; }
		       else {checkEnable(EnableStats);self.status="";} if (EnableStats.value == 1) {
		          Enable(  document.getElementById('chartId') ); }
			  else{ Disable(  document.getElementById('chartId') ); }
			  </xsl:attribute>
          <xsl:element name="option"></xsl:element>
          <xsl:element name="option">
            <xsl:attribute name="value">Function:Count(*)~Order:<xsl:value-of select="$pass"/><xsl:for-each select="//field">~Table:<xsl:value-of select="table" /></xsl:for-each></xsl:attribute>
            Count(*)
          </xsl:element>
          <xsl:element name="option">
            <xsl:attribute name="value">Function:Count~Order:<xsl:value-of select="$pass"/></xsl:attribute>
            Count
          </xsl:element>
          <xsl:element name="option">
            <xsl:attribute name="value">Function:Count(DISTINCT~Order:<xsl:value-of select="$pass"/></xsl:attribute>
            Count Distinct
          </xsl:element>
          <xsl:element name="option">
            <xsl:attribute name="value">Function:Max~Order:<xsl:value-of select="$pass"/></xsl:attribute>
            Max
          </xsl:element>
          <xsl:element name="option">
            <xsl:attribute name="value">Function:Min~Order:<xsl:value-of select="$pass"/></xsl:attribute>
            Min
          </xsl:element>
          <xsl:element name="option">
            <xsl:attribute name="value">Function:Avg~Order:<xsl:value-of select="$pass"/></xsl:attribute>
            Average
          </xsl:element>
          <xsl:element name="option">
            <xsl:attribute name="value">Function:Stddev~Order:<xsl:value-of select="$pass"/></xsl:attribute>
            Standard Deviation
          </xsl:element>
          <xsl:element name="option">
            <xsl:attribute name="value">Function:Sum~Order:<xsl:value-of select="$pass"/></xsl:attribute>
            Sum
          </xsl:element>
          <xsl:element name="option">
            <xsl:attribute name="value">Function:Variance~Order:<xsl:value-of select="$pass"/></xsl:attribute>
            Variance
          </xsl:element>
        </xsl:element>
        <!-- Frequency column removed to avoid confusion
        <xsl:if test="//field[@stats='yes'][type='DATE']">
            <xsl:text disable-output-escaping="yes">&lt;/td&gt;&lt;td&gt;</xsl:text>
            <xsl:element name="select">
              <xsl:attribute name="name">Stat<xsl:number value="$pass"/></xsl:attribute>
              <xsl:attribute name="onChange">
                if (this.selectedIndex != 0) {Enable(EnableStats); for (i=1; i!=<xsl:number value="count(//field[@output='yes'])+1"></xsl:number>; i++){ eval("Select" + i).checked=false; } self.status="Cannot select Output Data whilst statistical function selected."; return true} else {Disable(EnableStats),self.status=""; return true}
              </xsl:attribute>
              <xsl:element name="option"></xsl:element>
              <xsl:for-each select="//field[@stats='yes'][type='DATE']">
                <xsl:element name="option">
                  <xsl:attribute name="value">Group:Hour~GroupSelect:<xsl:value-of select="table"/>.<xsl:value-of select="column"/>~Order:<xsl:value-of select="$pass"/></xsl:attribute>
                  Hourly (by <xsl:value-of select="label"/>)
                </xsl:element>
                <xsl:element name="option">
                  <xsl:attribute name="value">Group:Day~GroupSelect:<xsl:value-of select="table"/>.<xsl:value-of select="column"/>~Order:<xsl:value-of select="$pass"/></xsl:attribute>
                  Daily (by <xsl:value-of select="label"/>)
                </xsl:element>
              </xsl:for-each>
            </xsl:element>
        </xsl:if>
        -->
          <!-- Field selection column -->
            <xsl:element name="select">
              <xsl:attribute name="name">Stat<xsl:number value="$pass"/></xsl:attribute>
              <xsl:attribute name="onChange">
               <!-- if (this.selectedIndex != 0) {Enable(EnableStats); for (i=1; i!=<xsl:number value="count(//field[@output='yes'])+1"></xsl:number>; i++){ eval("Select" + i).checked=false; } self.status="Cannot select Output Data whilst statistical function selected."; return true} else {Disable(EnableStats),self.status=""; return true}-->
               <!--if (this.selectedIndex != 0) {Enable(EnableStats); for (i=1; i!=<xsl:number value="count(//field[@output='yes'])+1"></xsl:number>; i++){ eval("Select" + i).checked=false; } self.status="Cannot select Output Data whilst statistical function selected."; document.getElementById('chartId').disabled=false; return true} else {Disable(EnableStats),self.status=""; document.getElementById('chartId').disabled=true; document.getElementById('chartId').checked=false; return true} -->
		      if (this.selectedIndex != 0)
		      {checkEnable(EnableStats); for (i=1; i!=<xsl:number value="count(//field[@output='yes'])+1"></xsl:number>; i++)
		           { eval("Select" + i).checked=false; }
			   self.status="Cannot select Output Data whilst statistical function selected."; }
		       else {checkEnable(EnableStats);self.status="";} if (EnableStats.value == 1) {
		          Enable(  document.getElementById('chartId') ); }
			  else{ Disable(  document.getElementById('chartId') ); }
		      </xsl:attribute>
              <xsl:element name="option"></xsl:element>
              <xsl:for-each select="//field[@stats='yes']">
                <xsl:sort select="order" data-type="number" order="ascending"/>
                <xsl:element name="option">
                  <xsl:attribute name="value">Table:<xsl:value-of select="table"/>~Field:<xsl:value-of select="column"/>~Order:<xsl:value-of select="$pass"/>~Type:<xsl:value-of select="type"/></xsl:attribute>
                  <xsl:value-of select="label"/>
                </xsl:element>
              </xsl:for-each>
            </xsl:element>
            <br />
  </xsl:template>

  <xsl:template name="groupbody">
    <xsl:param name="pass">
      <xsl:number value="1"/>
    </xsl:param>
    <xsl:element name="select">
      <xsl:attribute name="name">Group<xsl:number value="$pass"/></xsl:attribute>
      <xsl:attribute name="onChange">
        if (this.selectedIndex != 0) {Enable(EnableStats); for (i=1; i!=<xsl:number value="count(//field[@output='yes'])+1"></xsl:number>; i++){ eval("Select" + i).checked=false; } self.status="Cannot select Output Data whilst statistical function selected."; document.getElementById('chartId').disabled=false; return true} else {Disable(EnableStats),self.status=""; document.getElementById('chartId').disabled=true; document.getElementById('chartId').checked=false; return true}
      </xsl:attribute>
      <xsl:element name="option"></xsl:element>
      <xsl:for-each select="//field[@groupby='yes']">
        <xsl:element name="option">
          <xsl:attribute name="value">Table:<xsl:value-of select="table"/>~Field:<xsl:value-of select="column"/>~Order:<xsl:value-of select="$pass"/></xsl:attribute>
          <xsl:value-of select="label"/>
        </xsl:element>
      </xsl:for-each>
    </xsl:element>
  </xsl:template>
</xsl:stylesheet>
