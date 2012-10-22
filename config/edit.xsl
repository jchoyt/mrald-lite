<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:include href="editFormInsert.xsl"/>
<xsl:include href="editForm.xsl" />
<xsl:include href="editFormUpdate.xsl" />


<!--xsl:include href="editFormLabel.xsl" /-->
  <!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- include other xsl files for reference       -->
<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->

  <xsl:include href="operators.xsl" />
  <xsl:include href="edit_common.xsl"/>
  <xsl:include href="edit_links.xsl"/>
  <xsl:include href="edit_filters.xsl"/> 
  <xsl:include href="edit_orFilters.xsl"/>
  <xsl:include href="edit_compareFieldsFilters.xsl"/>
  <xsl:include href="edit_categoricalFilters.xsl"/>
  <xsl:include href="edit_rangeFilters.xsl"/>
  <xsl:include href="edit_timeFilter.xsl"/>  
  <xsl:include href="edit_analysis.xsl"/>
  <xsl:include href="edit_pivot.xsl"/>

  <xsl:template match="MraldForm">
    <xsl:call-template name="MraldForm"></xsl:call-template>
  </xsl:template>

  <xsl:template match="UpdateForm">
    <xsl:call-template name="UpdateForm"></xsl:call-template>
  </xsl:template>

  <xsl:template match="InsertForm">
    <xsl:call-template name="InsertForm"></xsl:call-template>
  </xsl:template>

 <!--xsl:template match="LabelForm">
    <xsl:call-template name="LabelForm"></xsl:call-template>
  </xsl:template-->


</xsl:stylesheet>
