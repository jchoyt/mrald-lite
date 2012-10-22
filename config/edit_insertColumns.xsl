<?xml version="1.0"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- template availableColumnsMain               -->
<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- SPECIFIC TO AVAILABLE COLUMNS SECTION -->
<xsl:template name="insertAvailableColumnsMain">
       <xsl:call-template name="parentRow_AvailableInsertColumns"></xsl:call-template>
</xsl:template>

<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- template parentRow_AvailableColumns         -->
<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- SPECIFIC TO AVAILABLE COLUMNS SECTION -->
<xsl:template name="parentRow_AvailableInsertColumns">
  <tr>
  <td>

  <xsl:call-template name="sectionHelpLink">
    <xsl:with-param name="helpLinkName">Fields</xsl:with-param>
  </xsl:call-template>

  <xsl:call-template name="insertAvailableColumns">
  </xsl:call-template>

  </td>
  </tr>
</xsl:template>

<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- template availableColumns                   -->
<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- SPECIFIC TO AVAILABLE COLUMNS SECTION -->
<xsl:template name="insertAvailableColumns">
<table summary="" width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="bord">
      <table summary="" width="100%" border="0" cellspacing="1" cellpadding="2">
        <xsl:call-template name="fieldHeadingsInsert"></xsl:call-template>

        <xsl:for-each select="//field">
          <xsl:sort select="order" order="ascending" data-type="number"/>
          
          <xsl:call-template name="fieldDataInsert">
          </xsl:call-template>
        </xsl:for-each>

      </table>
    </td>
  </tr>
</table>
</xsl:template>

<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- template fieldHeadings                      -->
<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- SPECIFIC TO AVAILABLE COLUMNS SECTION -->
<xsl:template name="fieldHeadingsInsert">
  <tr align="left">
    <font size="-1">
    <th>Ignore</th>
    <th>Table</th>
    <th>Column Name</th>
    <th>Column Label</th>
    <th>Order</th>
    <th>List Column</th>
    <th>Comments</th>
    </font>
  </tr>
  <tr>
    <xsl:call-template name="allSelectorInsert">
      <xsl:with-param name="heading">Ignore</xsl:with-param>
    </xsl:call-template>
    <th> </th>  
    <th> </th>  
    <th> </th>  
    <th> </th>  
    <th> </th>  
    <th> </th>  
  </tr>
</xsl:template>

<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- template allSelector                        -->
<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- SPECIFIC TO AVAILABLE COLUMNS SECTION -->
<xsl:template name="allSelectorInsert">
<xsl:param name="heading"/>
  <th>
    <xsl:element name="input">
      <xsl:attribute name="type">checkbox</xsl:attribute>
      <xsl:attribute name="onclick">if(this.checked==false) {ClearAll('<xsl:value-of select="$heading"/>:true');} else {CheckAll('<xsl:value-of select="$heading"/>:true');}</xsl:attribute>
      <xsl:if test="$heading!='Ignore'"><xsl:attribute name="checked"></xsl:attribute></xsl:if>
    </xsl:element>          
  </th>
</xsl:template>


<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- template fieldData                          -->
<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- SPECIFIC TO AVAILABLE COLUMNS SECTION -->
<xsl:template name="fieldDataInsert">
 <tr>

   <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
   <!-- display Ignore column values                -->
   <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
   <td align="center">
     <xsl:element name="input">
       <xsl:attribute name="name">FBInsert<xsl:number value="order"/></xsl:attribute>
       <xsl:attribute name="value">Ignore:true</xsl:attribute>
       <xsl:attribute name="type">checkbox</xsl:attribute>
     </xsl:element>
   </td>

   <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
   <!-- display Table column values                 -->
   <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
   <td><xsl:value-of select="table"/></td>

   <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
   <!-- display Column Name column values           -->
   <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
   <td>
     <xsl:element name="input">
       <xsl:attribute name="type">hidden</xsl:attribute>
       <xsl:attribute name="name">FBInsert<xsl:number value="order"/></xsl:attribute>
       <xsl:attribute name="value">Table:<xsl:value-of select="table"/>~Field:<xsl:value-of select="column"/>~Type:<xsl:value-of select="type"/>~Output:true</xsl:attribute>
       <xsl:value-of select="column"/>
     </xsl:element>
   </td>

   <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
   <!-- display Column Label column values          -->
   <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
   <td>
     <xsl:element name="input">
       <xsl:attribute name="name">FBInsert<xsl:number value="order"/></xsl:attribute>
       <xsl:attribute name="type">text</xsl:attribute>
       <xsl:attribute name="size">15</xsl:attribute>
       <xsl:attribute name="value"><xsl:value-of select="label"/></xsl:attribute>
     </xsl:element>
   </td>

   <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
   <!-- display Order column values                 -->
   <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
   <td align="center">
     <xsl:element name="input">
       <xsl:attribute name="name">FBInsert<xsl:number value="order"/></xsl:attribute>
       <xsl:attribute name="type">text</xsl:attribute>
       <xsl:attribute name="size">3</xsl:attribute>
       <xsl:attribute name="value"><xsl:number value="order"/></xsl:attribute>
     </xsl:element>
   </td>

   <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
   <!-- display List Column column values                -->
   <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
   <td align="center">
     <xsl:for-each select="listTable">       
       <xsl:element name="select">
         <xsl:attribute name="name">FBInsert<xsl:number value="../order"/></xsl:attribute>
         <xsl:element name="option">
           <xsl:attribute name="value">listTable:<xsl:value-of select="."/>~listColumn:<xsl:value-of select="../listColumn"/>~listIdCol:<xsl:value-of select="../listIdCol"/></xsl:attribute>       
             <xsl:value-of select="."/>.<xsl:value-of select="../listColumn"/>
	   </xsl:element>
	 </xsl:element>
      </xsl:for-each>
   </td>

 

   <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
   <!-- display comments column values              -->
   <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
   <td align="left">no comments for this field</td>
 </tr>
</xsl:template>

</xsl:stylesheet>