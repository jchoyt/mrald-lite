<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/TR/WD-xsl">
   <xsl:template match = "rows/row/columnValue/@columnName['FLIGHTDATE']">
	  <xsl:eval>Test</xsl:eval> 
    </xsl:template>
    <xsl:template match="/">
    <TABLE STYLE="border:1px solid black">
      <TR STYLE="font-size:12pt; font-family:Verdana; font-weight:bold; text-decoration:underline">
 	<TD STYLE="background-color:lightblue"><xsl:value-of select="rows/row[1]/columnValue[0]/@columnname"/></TD>
 	<TD STYLE="background-color:lightgrey"><xsl:value-of select="rows/row[1]/columnValue[1]/@columnname"/></TD>
 	<TD STYLE="background-color:lightyellow"><xsl:value-of select="rows/row[1]/columnValue[2]/@columnname"/></TD>
 	<TD STYLE="background-color:lightblue"><xsl:value-of select="rows/row[1]/columnValue[3]/@columnname"/></TD>
 	<TD STYLE="background-color:lightgrey"><xsl:value-of select="rows/row[1]/columnValue[4]/@columnname"/></TD>
  	<TD STYLE="background-color:lightgrey"><xsl:value-of select="rows/row[1]/columnValue[5]/@columnname"/></TD>
  	<TD STYLE="background-color:lightgrey"><xsl:value-of select="rows/row[1]/columnValue[6]/@columnname"/></TD>
       </TR>
      <xsl:for-each select="rows/row">
        <TR STYLE="font-family:Verdana; font-size:12pt; padding:0px 6px">
         <TD STYLE="background-color:lightblue"><xsl:value-of select="columnValue[0]"/></TD>
         <TD STYLE="background-color:lightgrey"><xsl:value-of select="columnValue[1]"/></TD>
         <TD STYLE="background-color:lightyellow"><xsl:value-of select="columnValue[2]"/></TD>
         <TD STYLE="background-color:lightblue"><xsl:value-of select="columnValue[3]"/></TD>
         <TD STYLE="background-color:lightgrey"><xsl:value-of select="columnValue[4]"/></TD>
         <TD STYLE="background-color:lightgrey"><xsl:value-of select="columnValue[5]"/></TD>
         <TD STYLE="background-color:lightblue"><xsl:value-of select="columnValue[6]"/></TD>
        </TR>
      </xsl:for-each>
    </TABLE>
  </xsl:template>
</xsl:stylesheet>