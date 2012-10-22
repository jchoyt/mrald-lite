<?xml version="1.0"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- template availableColumnsMain               -->
<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- SPECIFIC TO AVAILABLE COLUMNS SECTION -->
<xsl:template name="updateAvailableColumnsMain">
 <xsl:call-template name="parentTable">
   <xsl:with-param name="section">section_availableUpdateColumns</xsl:with-param>
 </xsl:call-template>
</xsl:template>

<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- template parentRow_AvailableColumns         -->
<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- SPECIFIC TO AVAILABLE COLUMNS SECTION -->
<xsl:template name="updateParentRow_AvailableColumns">
  <tr>
  <td>

  <xsl:call-template name="sectionHelpLink">
    <xsl:with-param name="helpLinkName">Fields</xsl:with-param>
  </xsl:call-template>

  <xsl:call-template name="updateAvailableColumns">
  </xsl:call-template>

  </td>
  </tr>
</xsl:template>

<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- template availableColumns                   -->
<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- SPECIFIC TO AVAILABLE COLUMNS SECTION -->
<xsl:template name="updateAvailableColumns">
<table summary="" width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="bord">
      <table summary="" width="100%" border="0" cellspacing="1" cellpadding="2">
        <xsl:call-template name="fieldHeadingsUpdate"></xsl:call-template>

        <xsl:for-each select="//field">
          <xsl:sort select="order" order="ascending" data-type="number"/>
          
          <xsl:call-template name="fieldDataUpdate">
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
<xsl:template name="fieldHeadingsUpdate">
  <tr align="left">
    <font size="-1">
    <th>Table</th>
    <th>Column Name</th>
    <th>Column Label</th>
    <th>Index For Update (Primary Keys)</th>
    <th>List Column</th>
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
    <th> </th>  
    <th> </th>  
    <th> </th>  
    <th> </th>  
    <th> </th>  

    <xsl:call-template name="allSelectorUpdate">
      <xsl:with-param name="heading">Filter</xsl:with-param>
    </xsl:call-template>

    <xsl:call-template name="allSelectorUpdate">
      <xsl:with-param name="heading">Stat</xsl:with-param>
    </xsl:call-template>

    <xsl:call-template name="allSelectorUpdate">
      <xsl:with-param name="heading">Group</xsl:with-param>
    </xsl:call-template>

    <th> </th>  

    <xsl:call-template name="allSelectorUpdate">
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
   <xsl:template name="allSelectorUpdate">
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
<xsl:template name="fieldDataUpdate">
 <tr>


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
       <xsl:attribute name="name">FBUpdate<xsl:number value="order"/></xsl:attribute>
       <xsl:attribute name="value">Table:<xsl:value-of select="table"/>~Field:<xsl:value-of select="column"/>~Type:<xsl:value-of select="type"/>~Output:true~Default:true</xsl:attribute>
       <xsl:value-of select="column"/>
     </xsl:element>
   </td>

   <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
   <!-- display Column Label column values          -->
   <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
   <td>
     <xsl:element name="input">
       <xsl:attribute name="name">FBUpdate<xsl:number value="order"/></xsl:attribute>
       <xsl:attribute name="type">text</xsl:attribute>
       <xsl:attribute name="size">15</xsl:attribute>
       <xsl:attribute name="value"><xsl:value-of select="label"/></xsl:attribute>
     </xsl:element>
   </td>

    <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
   <!-- display Drop Down values                -->
   <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
   <td align="left">
     <xsl:element name="input">
        <xsl:attribute name="name">FBUpdate<xsl:number value="order"/></xsl:attribute>
       <xsl:attribute name="value">PrimaryKey:<xsl:value-of select="primaryKey"/></xsl:attribute>
       <xsl:attribute name="type">checkbox</xsl:attribute>
     
       <xsl:for-each select="primaryKey">
            <xsl:attribute name="checked"></xsl:attribute>
        </xsl:for-each>
     </xsl:element>
   </td>
   
   <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
   <!-- display Filter column values                -->
   <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
  <td align="center">
   
    <xsl:for-each select="listTable">       
       <xsl:element name="select">
         <xsl:attribute name="name">FBUpdate<xsl:number value="../order"/></xsl:attribute>
         <xsl:element name="option">
           <xsl:attribute name="value">listTable:<xsl:value-of select="."/>~listColumn:<xsl:value-of select="../listColumn"/>~listIdCol:<xsl:value-of select="../listIdCol"/></xsl:attribute>       
             <xsl:value-of select="."/>.<xsl:value-of select="../listColumn"/>
	   </xsl:element>
	 </xsl:element>
        </xsl:for-each>
     </td>


   <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
   <!-- display Filter column values                -->
   <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
   <td align="center">
     <xsl:element name="input">
       <xsl:attribute name="name">FBUpdate<xsl:number value="order"/></xsl:attribute>
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
       <xsl:attribute name="name">FBUpdate<xsl:number value="order"/></xsl:attribute>
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
       <xsl:attribute name="name">FBUpdate<xsl:number value="order"/></xsl:attribute>
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
       <xsl:attribute name="name">FBUpdate<xsl:number value="order"/></xsl:attribute>
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
       <xsl:attribute name="name">FBUpdate<xsl:number value="order"/></xsl:attribute>
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
           <xsl:attribute name="name">FBUpdate<xsl:number value="order"/></xsl:attribute>
           <xsl:call-template name="numberFormats"></xsl:call-template>
         </xsl:element>
       </td>
     </xsl:when>

     <xsl:when test="type='Date'">
       <td>
         <xsl:element name="select">
           <xsl:attribute name="name">FBUpdate<xsl:number value="order"/></xsl:attribute>
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