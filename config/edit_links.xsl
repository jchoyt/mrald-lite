<?xml version="1.0"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- Author  : CRS Group                         -->
<!-- Date    : 8/15/2003                         -->
<!-- Version : Version 1                         -->
<!-- Purpose : all necessary templates for the   -->
<!--           links section                     -->
<!-- Last                                        -->
<!-- Updated : 8/15/2003                         -->
<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->


<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- template linksMain                          -->
<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- SPECIFIC TO LINKS SECTION -->
<xsl:template name="linksMain">
   <xsl:call-template name="parentTable">
     <xsl:with-param name="section">section_links</xsl:with-param>
   </xsl:call-template>
</xsl:template>


<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- template parentRow_Links                    -->
<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- SPECIFIC TO LINKS SECTION -->
<xsl:template name="parentRow_Links">
  <tr>
  <td>

  <xsl:call-template name="sectionHelpLink">
    <xsl:with-param name="helpLinkName">Links</xsl:with-param>
  </xsl:call-template>

  <xsl:call-template name="links"></xsl:call-template>

  </td>
  </tr>
</xsl:template>

<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- template links                              -->
<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- SPECIFIC TO LINKS SECTION -->
<xsl:template name="links">
<xsl:variable name="link_count"><xsl:number value="count(//link)"/></xsl:variable>
<center>
  <table cellspacing="0" cellpadding="0" border="1">

    <tr align="center">
      <font size="-1">
        <th width="25%">Primary</th>

        <th width="25%">Foreign</th>

        <th width="12%">Ignore</th>

        <th width="12%">Full</th>
      </font>
    </tr>

    <xsl:for-each select="//link">
    <tr align="center">
      <td align="left" width="25%"><xsl:value-of select="primarytable"/>.<xsl:value-of select="primaryfield"/></td>
      <td align="left" width="25%"><xsl:value-of select="secondarytable"/>.<xsl:value-of select="secondaryfield"/></td>

      <td width="25%">
        <xsl:element name="input">
          <xsl:attribute name="type">radio</xsl:attribute>
          <xsl:attribute name="name">FBLink<xsl:value-of select="position()-1"/></xsl:attribute>
          <xsl:attribute name="value">Table1:<xsl:value-of select="primarytable"/>~Field1:<xsl:value-of select="primaryfield"/>~Table2:<xsl:value-of select="secondarytable"/>~Field2:<xsl:value-of select="secondaryfield"/>~Link:Ignore</xsl:attribute>
        </xsl:element>
      </td>
    
      <td width="25%">
        <xsl:element name="input">
          <xsl:attribute name="type">radio</xsl:attribute>
          <xsl:attribute name="name">FBLink<xsl:value-of select="position()-1"/></xsl:attribute>
          <xsl:attribute name="value">Table1:<xsl:value-of select="primarytable"/>~Field1:<xsl:value-of select="primaryfield"/>~Table2:<xsl:value-of select="secondarytable"/>~Field2:<xsl:value-of select="secondaryfield"/>~Link:Full</xsl:attribute>
          <xsl:attribute name="checked"></xsl:attribute>
        </xsl:element>
      </td>
    </tr>
    </xsl:for-each>

    <xsl:call-template name="linkRow">
      <xsl:with-param name="num"><xsl:value-of select="$link_count"/></xsl:with-param>
    </xsl:call-template>

    <xsl:call-template name="linkRow">
      <xsl:with-param name="num"><xsl:value-of select="1+$link_count"/></xsl:with-param>
    </xsl:call-template>

    <xsl:call-template name="linkRow">
      <xsl:with-param name="num"><xsl:value-of select="2+$link_count"/></xsl:with-param>
    </xsl:call-template>
    
    <tr align="center">
      <br>
      </br>

      <font size="-1">( Note: all tables included on this form
      must be joined to at least one other table )</font>
    </tr>
  </table>
</center>
</xsl:template>

<!--
<tr align="center">
<td width="25%" align="left">NFDC_CDR.RTCODE</td>
<td width="25%" align="left">NFDC_CDR.RTCODE:Ignore:Ignore:Ignore</td>
<td width="25%">
  <input type="radio" name="FBLink0" value="Table1:NFDC_CDR~Field1:RTCODE~Table2:NFDC_CDR~Field2:RTCODE:Ignore:Ignore:Ignore:Ignore"></td>
<td width="25%">
  <input type="radio" name="FBLink0" value="Table1:NFDC_CDR~Field1:RTCODE~Table2:NFDC_CDR~Field2:RTCODE:Ignore:Ignore:Ignore:Full" checked></td>
</tr>
-->

<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- template linkRow                            -->
<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- SPECIFIC TO LINKS SECTION -->
<xsl:template name="linkRow">
<xsl:param name="num"/>
    <tr align="center">
      <td>
        <xsl:element name="select">
          <xsl:attribute name="name">FBLink<xsl:value-of select="$num"/></xsl:attribute>
          <option></option>
          <xsl:call-template name="dropDownValues">
            <xsl:with-param name="temp">1</xsl:with-param>
          </xsl:call-template>
        </xsl:element>
      </td>

      <td>
        <xsl:element name="select">
          <xsl:attribute name="name">FBLink<xsl:value-of select="$num"/></xsl:attribute>
          <option></option>
          <xsl:call-template name="dropDownValues">
            <xsl:with-param name="temp">2</xsl:with-param>
          </xsl:call-template>
        </xsl:element>
      </td>

      <td width="25%">
        <xsl:element name="input">
          <xsl:attribute name="type">radio</xsl:attribute>
          <xsl:attribute name="name">FBLink<xsl:value-of select="$num"/></xsl:attribute>
          <xsl:attribute name="value">~Link:Ignore</xsl:attribute>
          <xsl:attribute name="checked"></xsl:attribute>
        </xsl:element>
      </td>

      <td width="25%">
        <xsl:element name="input">
          <xsl:attribute name="type">radio</xsl:attribute>
          <xsl:attribute name="name">FBLink<xsl:value-of select="$num"/></xsl:attribute>
          <xsl:attribute name="value">~Link:Full</xsl:attribute>
        </xsl:element>
      </td>
    </tr>
</xsl:template>

</xsl:stylesheet>
