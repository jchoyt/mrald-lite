<?xml version="1.0"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- Author  : CRS Group                         -->
<!-- Date    : 8/7/2003                          -->
<!-- Version : Version 1                         -->
<!-- Purpose : Allow user to edit personalized   -->
<!--           forms                             -->
<!-- Last                                        -->
<!-- Updated : 8/19/2003                         -->
<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
 <xsl:include href="edit_availableColumns.xsl"/>

<xsl:template name="MraldForm">

  <html>
    <head>
    <link rel="stylesheet" href="mrald.css" type="text/css"></link>
    <title><xsl:value-of select="title"/></title>
    <xsl:text disable-output-escaping="yes">
        <![CDATA[
<script type="text/javascript">
    function CheckAll(val, filter)
    {
//alert(val + "::" + filter);
        var ml = document.o;
        var len = ml.elements.length;
        for (var i = 0; i < len; i++)
        {
            var e = ml.elements[i];
//alert(e.value);
            if (e.value==val && CheckFilter(e, filter))
            {
                e.checked = true;
            }
        }
    }
    
    function CheckFilter(e, filter)
    {
    	if (filter && e && e.parentNode && e.parentNode.parentNode) {
    		var row = e.parentNode.parentNode;
		var inputs = row.getElementsByTagName('input');
		for (var i = 0; i < inputs.length; i++) {
			var child = inputs[i];
			if (child && child.value && child.value.indexOf('Field:'+filter+'~') > 0) {
				return true;
    			}
    		}
		return false;
    	}
    	return true;
    }

    function ClearAll(val, filter)
    {
        var ml = document.o;
        var len = ml.elements.length;
        for (var i = 0; i < len; i++)
        {
            var e = ml.elements[i];
            if (e.value==val && CheckFilter(e, filter))
            {
                e.checked = false;
            }
         }
    }

    function HandlePivot(select)
    {
        var option = select.options[select.selectedIndex];
        var text = option.text;
        var table = text.substring(0, text.indexOf('.'));
        var field = text.substring(text.indexOf('.')+1);
        CheckAll('Output:true',field);
        CheckAll('Default:true',field);
        if (option.value.indexOf('Entity') < 0) {
            ClearAll('Filter:true',field);
            ClearAll('Stat:true',field);
            ClearAll('Group:true',field);
            ClearAll('Sort:true',field);
        }
    }
</script>
         ]]>
	 </xsl:text>
    </head>
    <body>
    <div id="header">
        <h1 class="headerTitle"><xsl:value-of select="title"/></h1>
    </div>
    <div class="subHeader">
    <span class="doNotDisplay">Navigation:</span>

      MRALD - Finding the gems in your data |
    <a href="help" onclick="window.open('help/step2.html', 'acepopup', 'width=640,height=480,status=yes,scrollbars=yes,toolbar=yes,resizable' );return false;">Help with this Form?</a>
      |
    <a href="index.jsp" target="_top">Home</a>
    |
    <a href="http://mitre.org">MITRE</a> | <a href="mailto:mrald-dev-list@lists.mitre.org">Contact Develoopers</a>
    </div>

      <!-- Start Form and Workflow data -->

        <form method="POST" action="FormSubmit" enctype="x-www-form-urlencoded" name="o">
        <input type="hidden" name="form" value="BuildForm" />
        <xsl:element name="input">
          <xsl:attribute name="type">hidden</xsl:attribute>
          <xsl:attribute name="name">Title</xsl:attribute>
          <xsl:attribute name="value"><xsl:value-of select="//title"/></xsl:attribute>
        </xsl:element>

        <xsl:element name="input">
          <xsl:attribute name="type">hidden</xsl:attribute>
          <xsl:attribute name="name">Table</xsl:attribute>
          <xsl:attribute name="value"><xsl:value-of select="//field/table"/></xsl:attribute>
        </xsl:element>

        <xsl:element name="input">
          <xsl:attribute name="type">hidden</xsl:attribute>
          <xsl:attribute name="name">Datasource</xsl:attribute>
          <xsl:attribute name="value"><xsl:value-of select="//datasource"/></xsl:attribute>
        </xsl:element>

        <input type="hidden" name="tableDepth" value="0" />
        <input type="hidden" name="form" value="FormBuilder2.jsp" />
        <input type="hidden" name="workflow" value="Form Builder" />
        <input type="hidden" name="stats" value="yes" />

   <br />
   <br />
   <center>
    <xsl:call-template name="formElements"></xsl:call-template>
   </center>
    <br />

   <div align="center">
    <input type="submit" value="Build Form" />
    <br />
    <br />
    <input type="reset" value="Reset Form" />
   </div>
   </form>
  </body>
</html>
</xsl:template>

<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<!-- template to match formElements              -->
<!--~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~-->
<xsl:template name="formElements">
  <xsl:call-template name="linksMain"></xsl:call-template>
  <br />
  <br />
  <xsl:call-template name="availableColumnsMain"></xsl:call-template>
  <br />
  <br />
  <xsl:call-template name="filtersMain"></xsl:call-template>
  <br />
  <br />
  <xsl:call-template name="catFiltersMain"></xsl:call-template>
  <br />
  <br />
  <xsl:call-template name="orFiltersMain"></xsl:call-template>
  <br />
  <br />
  <xsl:call-template name="compareFieldsMain"></xsl:call-template>
  <br />
  <br />
  <xsl:call-template name="rangeFiltersMain"></xsl:call-template>
  <br />
  <br />
  <xsl:call-template name="timeFilterMain"></xsl:call-template>
  <br />
  <xsl:call-template name="analysisMain"></xsl:call-template>
  <br />
  <xsl:call-template name="pivotMain"></xsl:call-template>
</xsl:template>

</xsl:stylesheet>
