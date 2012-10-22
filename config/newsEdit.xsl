<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

  <xsl:template match="news">
      <table width="90%" border="0" cellspacing="0" cellpadding="0" >
        <tr>
    <td class="bord" >
        <tr>
          <td colspan="3">
      <table width="100%">
         <xsl:call-template name="fieldHeadings"></xsl:call-template>
         <xsl:call-template name="NewsFile"></xsl:call-template>
      </table>
          </td>
        </tr>
    </td>
        </tr>
      </table>
  </xsl:template>


  <xsl:template name="NewsFile">

      <xsl:call-template name="AddND">
      <xsl:with-param name="NewsItemPos"><xsl:number value="position()" format="1"/></xsl:with-param>
      </xsl:call-template>

      <xsl:for-each select="newsday">
      <xsl:call-template name="NewsDay">
      <xsl:with-param name="NewsItemPos"><xsl:number value="position()" format="1"/></xsl:with-param>
      </xsl:call-template>
      </xsl:for-each>

  </xsl:template>


  <xsl:template name="NewsDay">
     <xsl:param name ="NewsItemPos"/>
     <xsl:for-each select="newsitem">
     <xsl:variable name="archive"><xsl:value-of select="../@archive"/></xsl:variable>
     <div>
     <tr>
     <td valign="top">
      <xsl:if test="position()=1">
      <!-- Print the Archive checkbox for the entire news day -->
        <xsl:element name="input">
          <xsl:attribute name="name">NewsDay<xsl:value-of select="$NewsItemPos + 1"/>~Archive</xsl:attribute>
          <xsl:attribute name="type">checkbox</xsl:attribute>
          <xsl:attribute name="value">true</xsl:attribute>
          <xsl:if test="$archive='yes'">
            <xsl:attribute name="checked" />
          </xsl:if>
        </xsl:element>
      </xsl:if>

      <xsl:element name="input">
              <xsl:attribute name="name">NewsDay<xsl:value-of select="$NewsItemPos + 1"/>~Count</xsl:attribute>
              <xsl:attribute name="type">hidden</xsl:attribute>
        <!-- The count has to have 1 added to it, so that the blank lines are taken care of -->
        <xsl:attribute name="value"><xsl:value-of select="count(../newsitem) + 1"/></xsl:attribute>
      </xsl:element>

     </td>
     <td valign="top">
      <xsl:if test="position()=1">
      <!-- Print the News Date textbox  -->
        <xsl:element name="input">
          <xsl:attribute name="name">NewsDay<xsl:value-of select="$NewsItemPos + 1"/>~Date</xsl:attribute>
          <xsl:attribute name="size">25</xsl:attribute>
          <xsl:attribute name="value"><xsl:value-of select="../@date"/></xsl:attribute>
        </xsl:element>
      </xsl:if>
     </td>
     </tr>
     <tr>
      <td></td>
      <td></td>
      <td>
        <xsl:element name="input">
          <xsl:attribute name="name">NewsDay<xsl:value-of select="$NewsItemPos + 1"/>~NewsArticle<xsl:number value="position() " format="1"/>~Title</xsl:attribute>
          <xsl:attribute name="value"><xsl:value-of select="title"/></xsl:attribute>
          <xsl:attribute name="size">50</xsl:attribute>
        </xsl:element>
      <br />
        <xsl:element name="textarea">
          <xsl:attribute name="name">NewsDay<xsl:value-of select="$NewsItemPos + 1"/>~NewsArticle<xsl:number value="position() " format="1"/>~Content</xsl:attribute>
          <xsl:attribute name="rows">3</xsl:attribute>
          <xsl:attribute name="cols">49</xsl:attribute>
          <xsl:value-of select="body"/> <xsl:text> </xsl:text>
        </xsl:element>
      </td>
    </tr>
    </div>
     </xsl:for-each>

      <xsl:call-template name="BlankLine">
      <xsl:with-param name="newsdayNo"><xsl:value-of select="$NewsItemPos + 1"></xsl:value-of></xsl:with-param>
      <xsl:with-param name="newsitemNo"><xsl:value-of select="count(newsitem) + 1"></xsl:value-of></xsl:with-param>
     </xsl:call-template>
  </xsl:template>


  <xsl:template name="AddND">
    <tr>
    <td valign="top">
      <xsl:element name="input">
        <xsl:attribute name="name">NewsDay<xsl:value-of select="1"/>~Archive</xsl:attribute>
        <xsl:attribute name="type">checkbox</xsl:attribute>
        <xsl:attribute name="value">true</xsl:attribute>
      </xsl:element>
    </td>
    <td valign="top">
      <xsl:element name="input">
        <xsl:attribute name="name">NewsDay<xsl:value-of select="1"/>~Date</xsl:attribute>
        <xsl:attribute name="size">25</xsl:attribute>
      </xsl:element>

      <xsl:element name="input">
              <xsl:attribute name="name">NewsDay<xsl:value-of select="1"/>~Count</xsl:attribute>
              <xsl:attribute name="type">hidden</xsl:attribute>
        <!-- The count has to have 1 added to it, so that the blank lines are taken care of -->
        <xsl:attribute name="value"><xsl:value-of select="1"/></xsl:attribute>
      </xsl:element>
    </td>
    </tr>

     <xsl:call-template name="BlankLine">
      <xsl:with-param name="newsdayNo"><xsl:value-of select="1"></xsl:value-of></xsl:with-param>
      <xsl:with-param name="newsitemNo"><xsl:value-of select="1"></xsl:value-of></xsl:with-param>
     </xsl:call-template>
  </xsl:template>


  <xsl:template name="BlankLine">
    <xsl:param name ="newsdayNo"/>
    <xsl:param name ="newsitemNo"/>
    <tr>
    <td></td>
    <td></td>
    <td>
      <xsl:element name="input">
        <xsl:attribute name="name">NewsDay<xsl:value-of select="$newsdayNo"/>~NewsArticle<xsl:value-of select="$newsitemNo"/>~Title</xsl:attribute>
        <xsl:attribute name="size">50</xsl:attribute>
      </xsl:element>
    <br />
      <xsl:element name="textarea">
        <xsl:attribute name="name">NewsDay<xsl:value-of select="$newsdayNo"/>~NewsArticle<xsl:value-of select="$newsitemNo"/>~Content</xsl:attribute>
        <xsl:attribute name="cols">49</xsl:attribute>
        <xsl:attribute name="rows">3</xsl:attribute>
        <xsl:text> </xsl:text>
      </xsl:element>
    </td>
    </tr>
  </xsl:template>

  <xsl:template name="fieldHeadings">
    <tr align="center">
      <font size="-1">
      <th>Archive</th>
      <th>News Day</th>
      <th>Title and Article</th>
      </font>
    </tr>
  </xsl:template>


</xsl:stylesheet>
