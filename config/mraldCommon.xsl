<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:template name="ActionButtons">
      <!-- Action Buttons -->
      <div align="center">
       	
      <input type="hidden" value="0" name="listCount" id="listCount"></input>
       
      	  <input type="button"  name="count" value="Hits from current criteria" onclick="this.form.Format.value='CountList';returnNumber(this.form,'99','../');"/>			
	 <div id="list99"><input type ="text"></input></div>

        <input type="submit" value="Retrieve Data" onclick="this.form.Format.value='browserHtml';setProcessing();return true;"/>
        <br /><br />
        <input type="reset" value="Reset Form" />
      </div>
  </xsl:template>
  <xsl:template name="DoLinks">
    <xsl:for-each select="//link">
      <xsl:element name="input">
        <xsl:attribute name="name">Link<xsl:number value="position()" format="1"/></xsl:attribute>
        <xsl:attribute name="type">hidden</xsl:attribute>
	  <xsl:choose>
	  <xsl:when test="/MraldForm/multiDb='yes'">
		<xsl:attribute name="value">PrimaryLink:<xsl:value-of select="primarytable"/>.<xsl:value-of select="primaryfield"/>~SecondaryLink:<xsl:value-of select="secondarytable"/>.<xsl:value-of select="secondaryfield"/>~SqlThread:<xsl:value-of select="sqlThread"/></xsl:attribute>
	  </xsl:when>
	 <xsl:otherwise>
		<xsl:attribute name="value">PrimaryLink:<xsl:value-of select="primarytable"/>.<xsl:value-of select="primaryfield"/>~SecondaryLink:<xsl:value-of select="secondarytable"/>.<xsl:value-of select="secondaryfield"/></xsl:attribute>
     	</xsl:otherwise>
	</xsl:choose>
	</xsl:element>     
    </xsl:for-each>
    <xsl:for-each select="//crossLink">
      <xsl:element name="input">
        <xsl:attribute name="name">DbCrossLink<xsl:number value="position()" format="1"/></xsl:attribute>
        <xsl:attribute name="type">hidden</xsl:attribute>
	  <xsl:choose>
	  <xsl:when test="/MraldForm/multiDb='yes'">
		<xsl:attribute name="value">PrimaryLink:<xsl:value-of select="primarytable"/>.<xsl:value-of select="primaryfield"/>~SecondaryLink:<xsl:value-of select="secondarytable"/>.<xsl:value-of select="secondaryfield"/>~SqlThread:<xsl:value-of select="sqlThread"/></xsl:attribute>
	  </xsl:when>
	 <xsl:otherwise>
		<xsl:attribute name="value">PrimaryLink:<xsl:value-of select="primarytable"/>.<xsl:value-of select="primaryfield"/>~SecondaryLink:<xsl:value-of select="secondarytable"/>.<xsl:value-of select="secondaryfield"/></xsl:attribute>
     	</xsl:otherwise>
	</xsl:choose>
	</xsl:element>     
    </xsl:for-each>
  </xsl:template>
  <xsl:template name="NiceFormats">
    <xsl:for-each select="//field[format]">
      <xsl:sort select="order" order="ascending"/>
      <xsl:element name="input">
        <xsl:attribute name="name">outputFormat<xsl:number value="position()" format="1"/></xsl:attribute>
        <xsl:attribute name="type">hidden</xsl:attribute>
        <xsl:attribute name="value">fieldname:<xsl:value-of select="column"/>~nicename:<xsl:value-of select="label"/>~type:<xsl:value-of select="type"/>~formatpattern:<xsl:value-of select="format"/></xsl:attribute>
      </xsl:element>
    </xsl:for-each>

    <xsl:for-each select="//field[not(format)]">
      <xsl:sort select="order" order="ascending"/>
      <xsl:element name="input">
        <xsl:attribute name="name">outputFormat2<xsl:number value="position()" format="1"/></xsl:attribute>
        <xsl:attribute name="type">hidden</xsl:attribute>
        <xsl:attribute name="value">fieldname:<xsl:value-of select="column"/>~nicename:<xsl:value-of select="label"/>~type:<xsl:value-of select="type"/></xsl:attribute>
      </xsl:element>
    </xsl:for-each>
  </xsl:template>


<xsl:template name="dropDownList">
    <xsl:param name="pos">
    <xsl:number value="1"/>
    </xsl:param>

     <xsl:element name="input">
       <xsl:attribute name="name">fKeyTable<xsl:number value="$pos" format="1"/></xsl:attribute>
       <xsl:attribute name="type">hidden</xsl:attribute>
       <xsl:attribute name="value"><xsl:value-of select="."/></xsl:attribute>
     </xsl:element>


     <xsl:element name="input">
        <xsl:attribute name="name">fKey<xsl:number value="$pos" format="1"/></xsl:attribute>
        <xsl:attribute name="type">hidden</xsl:attribute>
        <xsl:attribute name="value"><xsl:value-of select="../column"/></xsl:attribute>
     </xsl:element>


     <xsl:element name="input">
        <xsl:attribute name="name">fKeyId<xsl:number value="$pos" format="1"/></xsl:attribute>
        <xsl:attribute name="type">hidden</xsl:attribute>
        <xsl:attribute name="value"><xsl:value-of select="../listIdCol"/></xsl:attribute>
     </xsl:element>


     <xsl:element name="input">
        <xsl:attribute name="name">fKeyList<xsl:number value="$pos" format="1"/></xsl:attribute>
        <xsl:attribute name="type">hidden</xsl:attribute>
        <xsl:attribute name="value"><xsl:value-of select="../listColumn"/></xsl:attribute>
        </xsl:element>

</xsl:template>
</xsl:stylesheet>
