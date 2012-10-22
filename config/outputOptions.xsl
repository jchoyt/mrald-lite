<?xml version="1.0"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:template name="analysisOutput">
        <strong>Analysis</strong>
        <br />
        
        
        <input type="radio" name="Format" value="MITRE_PA" />
        MITRE: Pathalizer
        <br />
                

        <input type="radio" name="Format" value="MITRE_FD" />
        MITRE: Find Distrib
        <br />

        <input type="radio" name="Format" value="WEKA_ID3" />
        WEKA: ID3
	<br />
<!-- 
        <input type="radio" name="Format" value="WEKA_J48" />
        WEKA: J48
	<br />
-->
</xsl:template>

</xsl:stylesheet>
