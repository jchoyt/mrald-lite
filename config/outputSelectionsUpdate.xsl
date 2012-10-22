<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:template name="OutputFieldsUpdate">
    <xsl:variable name="numFields" select="count(//field[@output='yes'])"></xsl:variable>
    <xsl:variable name="break">
      <xsl:choose>
        <xsl:when test="$numFields &lt;= 5"></xsl:when>
        <xsl:when test="$numFields &gt; 5 and $numFields &lt;= 10"><xsl:number value="ceiling($numFields div 2)" format="1"/></xsl:when>
        <xsl:otherwise><xsl:number value="ceiling($numFields div 3)" format="1"/></xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
      <!-- Start Output Selections -->
    <table width="90%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td class="bord">
          <table width="100%" border="0" cellpadding="3" cellspacing="1">
            <tr>
              <th>
                Output Data Selections
              </th>
            </tr>
            <tr>
               <xsl:for-each select="//field/primaryKey">

                        <xsl:element name="input">
                          <xsl:attribute name="name">PrimaryKey</xsl:attribute>
                          <xsl:attribute name="type">hidden</xsl:attribute>
                          <xsl:attribute name="value"><xsl:value-of select="."/></xsl:attribute>
                        </xsl:element>
               </xsl:for-each>



              <td valign="top">
                <table width="100%">
                  <tr>
                    <td>

                      <xsl:for-each select="//field[@output='yes']">
                        <xsl:sort select="order" data-type="number" order="ascending"/>
                        <xsl:variable name="pos" select="position()"/>

                        <xsl:element name="input">
                          <xsl:attribute name="name">Select<xsl:number value="position()" format="1"/></xsl:attribute>
                          <xsl:if test="primaryKey">
                            <xsl:attribute name="type">hidden</xsl:attribute>
                          </xsl:if>
                          <xsl:if test="not(primaryKey)">
                            <xsl:attribute name="type">checkbox</xsl:attribute>
                          </xsl:if>
                          <xsl:attribute name="value">Table:<xsl:value-of select="table"/>~Field:<xsl:value-of select="column"/>~Order:<xsl:value-of select="order"/></xsl:attribute>
                          <xsl:attribute name="onMouseOver">if (EnableStats.value == 1) {}</xsl:attribute>
                          <xsl:attribute name="onFocus">if (EnableStats.value == 1) {this.click(), this.blur()}</xsl:attribute>
                          <xsl:attribute name="onDblClick">if (EnableStats.value == 1) {this.click(), this.blur()}</xsl:attribute>
                          <xsl:if test="@checked='yes'">
                            <xsl:attribute name="checked"></xsl:attribute>
                          </xsl:if>
                          <xsl:value-of select="label"/>
                          <xsl:if test="primaryKey">
                            <xsl:text> (required)</xsl:text>
                          </xsl:if>
                        </xsl:element>
                        <br />
                        <xsl:if test="position() mod $break = 0">
                          <xsl:text disable-output-escaping="yes">&lt;/td&gt;&lt;td valign="top"&gt;</xsl:text>
                        </xsl:if>

                        <xsl:for-each select="listTable">

                           <xsl:call-template name ="dropDownList">
                             <xsl:with-param name="pos" select="$pos"/>
                           </xsl:call-template>

                        </xsl:for-each>

                      </xsl:for-each>
                    </td>
                  </tr>
                  <tr>
                    <td colspan="3">
                      <hr />
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
                    </td>
                  </tr>
                </table>
              </td>
            </tr>
          </table>
        </td>
      </tr>
    </table>
      <!-- End OutputSelections -->
  </xsl:template>

</xsl:stylesheet>


