<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

   <!-- <xsl:include href="outputSelectionsUpdate.xsl" /> -->
   <!-- <xsl:include href="outputSelections.xsl" /> -->
   <xsl:template name="formElementsUpdate">

    <xsl:for-each select="//formElements">
      <xsl:call-template name="DoLinks"></xsl:call-template>
      <xsl:call-template name="NiceFormats"></xsl:call-template>
      <xsl:call-template name="SetPrimaryKeys" />
      <xsl:call-template name="SimpleOutputFields"></xsl:call-template>
      <!-- <xsl:call-template name="OutputFieldsUpdate" /> -->
      <xsl:call-template name="Filters">
        <xsl:with-param name="advanced">true</xsl:with-param>
      </xsl:call-template>
      <!-- <br /><xsl:call-template name="ActionButtons"></xsl:call-template>-->
    </xsl:for-each>
  </xsl:template>

    <xsl:template name="SetPrimaryKeys">
        <xsl:for-each select="//field[@output='yes']">
	 <xsl:variable name="pos" select="position()"/>

            <xsl:if test="position() = 1">
             <xsl:element name="input">
                <xsl:attribute name="name">table</xsl:attribute>
                <xsl:attribute name="type">hidden</xsl:attribute>
                <xsl:attribute name="value"><xsl:value-of select="table"/></xsl:attribute>
             </xsl:element>
            </xsl:if>
	    <xsl:for-each select="listTable">
                <xsl:call-template name ="dropDownList">
                <xsl:with-param name="pos" select="$pos"/>
                </xsl:call-template>
         </xsl:for-each>
        </xsl:for-each>
        <xsl:for-each select="//field/primaryKey">
            <xsl:element name="input">
              <xsl:attribute name="name">PrimaryKey</xsl:attribute>
              <xsl:attribute name="type">hidden</xsl:attribute>
              <xsl:attribute name="value"><xsl:value-of select="."/></xsl:attribute>
            </xsl:element>
        </xsl:for-each>
	
    </xsl:template>

</xsl:stylesheet>

