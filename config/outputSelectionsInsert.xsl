<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:template name="OutputFieldsInsert">
    <xsl:variable name="numFields" select="count(//field[@output='yes'])"></xsl:variable>
    <xsl:variable name="break">
      <xsl:choose>
        <xsl:when test="$numFields &lt;= 5"></xsl:when>
        <xsl:when test="$numFields &gt; 5 and $numFields &lt;= 10"><xsl:number value="ceiling($numFields div 2)" format="1"/></xsl:when>
        <xsl:otherwise><xsl:number value="ceiling($numFields div 3)" format="1"/></xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
		  <table class="filter">
                      <xsl:for-each select="//field[@output='yes']">
                        <xsl:sort select="order" data-type="number" order="ascending"/>
                        <tr>
                        <td class="tdbox">
                             <xsl:if test="required"><font color="#FF0000">*</font></xsl:if>
                             <b><xsl:value-of select="label"/>:</b>
                        </td>
                        <td class="tdbox">
                        <xsl:choose>
                           <xsl:when test="listTable">
                               <xsl:element name="select">
                               <xsl:attribute name="name">Insert<xsl:number value="position()" format="1"/></xsl:attribute>
                               <xsl:text disable-output-escaping="yes"><![CDATA[<mrald:dropDownList table=']]></xsl:text>
                               <xsl:value-of select="listTable"/>
                               <xsl:text disable-output-escaping="yes"><![CDATA[' listColumn=']]></xsl:text>
                               <xsl:value-of select="listColumn"/>
                               <xsl:text disable-output-escaping="yes"><![CDATA[' datasource=']]></xsl:text>
                               <xsl:value-of select="../../datasource"/>
                               <xsl:text disable-output-escaping="yes"><![CDATA[' pkColumn=']]></xsl:text>
                               <xsl:value-of select="listIdCol"/>
                               <xsl:text disable-output-escaping="yes"><![CDATA['/>]]></xsl:text>
                               </xsl:element>
                           </xsl:when>
                           <xsl:otherwise>
                               <xsl:element name="input">
                                   <xsl:attribute name="type">text</xsl:attribute>
                                   <xsl:attribute name="length">30</xsl:attribute>
                                   <xsl:attribute name="name">Insert<xsl:number value="position()" format="1"/></xsl:attribute>
                                   <xsl:if test="pkey">
                                    <xsl:attribute name="onchange">checkPkExists('<xsl:value-of select="table"/>','<xsl:value-of select="column"/>', this.value, '<xsl:value-of select="label"/>')</xsl:attribute>
                                   </xsl:if>
                               </xsl:element>
                           </xsl:otherwise>
                        </xsl:choose>
                        <xsl:if test="contains(type, 'Date')">
                                 <xsl:text disable-output-escaping="yes"><![CDATA[<SCRIPT LANGUAGE="JavaScript">var cal]]></xsl:text>
                                 <xsl:number value="position()" format="1"/>
                                 <xsl:text disable-output-escaping="yes"><![CDATA[ = new CalendarPopup();</SCRIPT>]]></xsl:text>
                                 <!-- <xsl:attribute name="name">Insert<xsl:number value="position()" format="1"/></xsl:attribute> -->
                                 <xsl:text disable-output-escaping="yes"><![CDATA[<a HREF="#" onClick="cal]]></xsl:text>
                                 <xsl:number value="position()" format="1"/>
                                 <xsl:text disable-output-escaping="yes"><![CDATA[.select(document.FormInsert.Insert]]></xsl:text>
                                 <xsl:number value="position()" format="1"/>
                                 <xsl:text disable-output-escaping="yes"><![CDATA[[0],'anchor]]></xsl:text>
                                 <xsl:number value="position()" format="1"/>
                                 <xsl:text disable-output-escaping="yes"><![CDATA[','MM/dd/yyyy'); return false;" TITLE="cal]]></xsl:text>
                                 <xsl:number value="position()" format="1"/>
                                 <xsl:text disable-output-escaping="yes"><![CDATA[.select(document.FormInsert.Insert]]></xsl:text>
                                 <xsl:number value="position()" format="1"/>
                                 <xsl:text disable-output-escaping="yes"><![CDATA[[0],'anchor]]></xsl:text>
                                 <xsl:number value="position()" format="1"/>
                                 <xsl:text disable-output-escaping="yes"><![CDATA[','MM/dd/yyyy'); return false; "  NAME="anchor]]></xsl:text>
                                 <xsl:number value="position()" format="1"/>
                                 <xsl:text disable-output-escaping="yes"><![CDATA[" ID="anchor]]></xsl:text>
                                 <xsl:number value="position()" format="1"/>
                                 <xsl:text disable-output-escaping="yes"><![CDATA[" ><img src="../images/cal.gif" width="17" height="17" border="0" alt="Click Here to Pick up the timestamp"></a>]]></xsl:text>
                                 (example 12/30/2005)<br />
                       </xsl:if>
                       <xsl:element name="input">
                        <xsl:attribute name="type">hidden</xsl:attribute>
                        <xsl:attribute name="value">Table:<xsl:value-of select="table"/>~Field:<xsl:value-of select="column"/>~Order:<xsl:value-of select="order"/>~Type:<xsl:value-of select="type"/></xsl:attribute>
                        <xsl:attribute name="name">Insert<xsl:number value="position()" format="1"/></xsl:attribute>
                        </xsl:element>
                        <br />
                        <xsl:if test="position() mod $break = 0">
                          <xsl:text disable-output-escaping="yes">&lt;/td&gt;&lt;td valign="top"&gt;</xsl:text>
                        </xsl:if>
                    </td>
                    </tr>
                    </xsl:for-each>
                    <tr><td colspan="2" align="left"><font color="#FF0000">*</font><b>Any fields marked with an asterisk are required</b></td></tr>
		    </table>
  </xsl:template>
</xsl:stylesheet>

