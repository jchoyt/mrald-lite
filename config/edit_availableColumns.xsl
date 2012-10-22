<?xml version="1.0"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- Author  : CRS Group                         -->
<!-- Date    : 8/15/2003                         -->
<!-- Version : Version 1                         -->
<!-- Purpose : all necessary templates for the   -->
<!--           available columns section         -->
<!-- Last                                        -->
<!-- Updated : 8/15/2003                         -->
<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->

<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- template availableColumnsMain               -->
<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- SPECIFIC TO AVAILABLE COLUMNS SECTION -->
<xsl:template name="availableColumnsMain">
 <xsl:call-template name="parentTable">
   <xsl:with-param name="section">section_availableColumns</xsl:with-param>
 </xsl:call-template>
</xsl:template>

<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- template parentRow_AvailableColumns         -->
<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- SPECIFIC TO AVAILABLE COLUMNS SECTION -->
<xsl:template name="parentRow_AvailableColumns">
  <tr>
  <td>

  <xsl:call-template name="sectionHelpLink">
    <xsl:with-param name="helpLinkName">Fields</xsl:with-param>
  </xsl:call-template>

  <xsl:call-template name="availableColumns">
  </xsl:call-template>

  </td>
  </tr>
</xsl:template>

<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- template availableColumns                   -->
<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- SPECIFIC TO AVAILABLE COLUMNS SECTION -->
<xsl:template name="availableColumns">
<table summary="" width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="bord">
      <table summary="" width="100%" border="0" cellspacing="1" cellpadding="2">
        <xsl:call-template name="fieldHeadings"></xsl:call-template>

        <xsl:for-each select="//field">
          <xsl:sort select="order" order="ascending" data-type="number"/>
          
          <xsl:call-template name="fieldData">
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
<xsl:template name="fieldHeadings">
  <tr align="left">
    <font size="-1">
    <th>Ignore</th>
    <th>Table</th>
    <th>Column Name</th>
    <th>Column Label</th>
    <th>Output</th>
    <th>Default Selection</th>
    <th>Filter</th>
    <th>Stats</th>
    <th>Group By</th>
    <th>Order</th>
    <th>Sort</th>
    <th>Format</th>
    <th>Comments</th>
    </font>
  </tr>
  <tr>
    <xsl:call-template name="allSelector">
      <xsl:with-param name="heading">Ignore</xsl:with-param>
    </xsl:call-template>
    <th> </th>  
    <th> </th>  
    <th> </th>  

    <xsl:call-template name="allSelector">
      <xsl:with-param name="heading">Output</xsl:with-param>
    </xsl:call-template>

    <xsl:call-template name="allSelector">
      <xsl:with-param name="heading">Default</xsl:with-param>
    </xsl:call-template>

    <xsl:call-template name="allSelector">
      <xsl:with-param name="heading">Filter</xsl:with-param>
    </xsl:call-template>

    <xsl:call-template name="allSelector">
      <xsl:with-param name="heading">Stat</xsl:with-param>
    </xsl:call-template>

    <xsl:call-template name="allSelector">
      <xsl:with-param name="heading">Group</xsl:with-param>
    </xsl:call-template>

    <th> </th>  

    <xsl:call-template name="allSelector">
      <xsl:with-param name="heading">Sort</xsl:with-param>
    </xsl:call-template>
    <th> </th>  
    <th> </th>  
  </tr>
</xsl:template>

<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- template allSelector                        -->
<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- SPECIFIC TO AVAILABLE COLUMNS SECTION -->
<xsl:template name="allSelector">
<xsl:param name="heading"/>
  <th>
    <xsl:element name="input">
      <xsl:attribute name="type">checkbox</xsl:attribute>
      <xsl:attribute name="onClick">if(this.checked==false){ClearAll('<xsl:value-of select="$heading"/>:true');} else{CheckAll('<xsl:value-of select="$heading"/>:true');}</xsl:attribute>
      <xsl:if test="$heading!='Ignore'"><xsl:attribute name="checked"></xsl:attribute></xsl:if>
    </xsl:element>          
  </th>
</xsl:template>


<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- template fieldData                          -->
<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- SPECIFIC TO AVAILABLE COLUMNS SECTION -->
<xsl:template name="fieldData">
 <tr>

   <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
   <!-- display Ignore column values                -->
   <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
   <td align="center">
     <xsl:element name="input">
       <xsl:attribute name="name">Field<xsl:number value="order"/></xsl:attribute>
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
       <xsl:attribute name="name">Field<xsl:number value="order"/></xsl:attribute>
       <xsl:attribute name="value">Table:<xsl:value-of select="table"/>~Field:<xsl:value-of select="column"/>~Type:<xsl:value-of select="type"/></xsl:attribute>
       <xsl:value-of select="column"/>
     </xsl:element>
   </td>

   <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
   <!-- display Column Label column values          -->
   <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
   <td>
     <xsl:element name="input">
       <xsl:attribute name="name">Field<xsl:number value="order"/></xsl:attribute>
       <xsl:attribute name="type">text</xsl:attribute>
       <xsl:attribute name="size">15</xsl:attribute>
       <xsl:attribute name="value"><xsl:value-of select="label"/></xsl:attribute>
     </xsl:element>
   </td>

   <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
   <!-- display Output column values                -->
   <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
   <td align="center">
     <xsl:element name="input">
       <xsl:attribute name="name">Field<xsl:number value="order"/></xsl:attribute>
       <xsl:attribute name="value">Output:true</xsl:attribute>
       <xsl:attribute name="type">checkbox</xsl:attribute>
       <xsl:if test="@output='yes'"><xsl:attribute name="checked"></xsl:attribute></xsl:if>
     </xsl:element>
   </td>

   <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
   <!-- display Default Selection column values     -->
   <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
   <td align="center">
     <xsl:element name="input">
       <xsl:attribute name="name">Field<xsl:number value="order"/></xsl:attribute>
       <xsl:attribute name="value">Default:true</xsl:attribute>
       <xsl:attribute name="type">checkbox</xsl:attribute>
       <xsl:if test="@checked='yes'"><xsl:attribute name="checked"></xsl:attribute></xsl:if>
     </xsl:element>
   </td>

   <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
   <!-- display Filter column values                -->
   <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
   <td align="center">
     <xsl:element name="input">
       <xsl:attribute name="name">Field<xsl:number value="order"/></xsl:attribute>
       <xsl:attribute name="value">Filter:true</xsl:attribute>
       <xsl:attribute name="type">checkbox</xsl:attribute>
       <xsl:if test="@filter='yes'"><xsl:attribute name="checked"></xsl:attribute></xsl:if>
     </xsl:element>
   </td>

   <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
   <!-- display Stats column values                 -->
   <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
   <td align="center">
     <xsl:element name="input">
       <xsl:attribute name="name">Field<xsl:number value="order"/></xsl:attribute>
       <xsl:attribute name="value">Stat:true</xsl:attribute>
       <xsl:attribute name="type">checkbox</xsl:attribute>
       <xsl:if test="@stats='yes'"><xsl:attribute name="checked"></xsl:attribute></xsl:if>
     </xsl:element>
   </td>

   <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
   <!-- display Group column values                 -->
   <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
   <td align="center">
     <xsl:element name="input">
       <xsl:attribute name="name">Field<xsl:number value="order"/></xsl:attribute>
       <xsl:attribute name="value">Group:true</xsl:attribute>
       <xsl:attribute name="type">checkbox</xsl:attribute>
       <xsl:if test="@groupby='yes'"><xsl:attribute name="checked"></xsl:attribute></xsl:if>
     </xsl:element>
   </td>

   <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
   <!-- display Order column values                 -->
   <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
   <td align="center">
     <xsl:element name="input">
       <xsl:attribute name="name">Field<xsl:number value="order"/></xsl:attribute>
       <xsl:attribute name="type">text</xsl:attribute>
       <xsl:attribute name="size">3</xsl:attribute>
       <xsl:attribute name="value"><xsl:number value="order"/></xsl:attribute>
     </xsl:element>
   </td>

   <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
   <!-- display Sort column values                  -->
   <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
   <td align="center">
     <xsl:element name="input">
       <xsl:attribute name="name">Field<xsl:number value="order"/></xsl:attribute>
       <xsl:attribute name="value">Sort:true</xsl:attribute>
       <xsl:attribute name="type">checkbox</xsl:attribute>
       <xsl:if test="@sort='yes'"><xsl:attribute name="checked"></xsl:attribute></xsl:if>
     </xsl:element>
   </td>

   <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
   <!-- display Format column values                -->
   <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
   <xsl:choose>
     <xsl:when test="type='Numeric'">
       <td>
         <xsl:element name="select">
           <xsl:attribute name="name">Field<xsl:number value="order"/></xsl:attribute>
           <xsl:call-template name="numberFormats"></xsl:call-template>
         </xsl:element>
       </td>
     </xsl:when>

     <xsl:when test="type='Date'">
       <td>
         <xsl:element name="select">
           <xsl:attribute name="name">Field<xsl:number value="order"/></xsl:attribute>
           <xsl:call-template name="dateFormats">
             <xsl:with-param name="date_fmt"><xsl:value-of select="format"/></xsl:with-param>
           </xsl:call-template>
         </xsl:element>
       </td>
     </xsl:when>
     <xsl:when test="type='String'">
       <td> </td>
     </xsl:when>
   </xsl:choose>

   <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
   <!-- display comments column values              -->
   <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
   <td align="left">no comments for this field</td>
 </tr>
</xsl:template>

</xsl:stylesheet>