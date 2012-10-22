<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

  <xsl:template match="WorkFlow">
    <table width="90%" border="0" cellspacing="0" cellpadding="0" >
      <tr>
        <td class="bord" >
   
            <tr>
              <td colspan="3">
                <table width="100%">
                					
                	 <xsl:call-template name="fieldHeadings"></xsl:call-template>

			<xsl:call-template name="WorkflowPath"></xsl:call-template>
    		   </table>
    		</td>
    		</tr>
    		
    		</td>
    		</tr>
    		</table>	
    
  </xsl:template>

  <xsl:template name="WorkflowStep">
         <xsl:param name ="wfStepPos"/>
	 <xsl:for-each select="WfObject">
	 <div>
			
	<tr>		
	<td>
	 		<xsl:if test="position()=1">
				<xsl:element name="input">
      					<xsl:attribute name="name">WfStep<xsl:value-of select="$wfStepPos"/>~Ignore</xsl:attribute>
					<xsl:attribute name="type">checkbox</xsl:attribute>
					<xsl:attribute name="value">true</xsl:attribute>
 
      				</xsl:element> 
      			</xsl:if>
    	</td>
    	
	 <td>
	 	<xsl:if test="position()=1">
	 
	 		<xsl:element name="input">
	      			<xsl:attribute name="name">WfStep<xsl:value-of select="$wfStepPos"/>~WfName</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="../WfName"/></xsl:attribute>
			</xsl:element>
			
			<xsl:element name="input">
	      			<xsl:attribute name="name">WfStep<xsl:value-of select="$wfStepPos"/>~Count</xsl:attribute>
	      			<xsl:attribute name="type">hidden</xsl:attribute>
				<!-- The count has to have 1 added to it, so that the blank lines are taken care of -->
				<xsl:attribute name="value"><xsl:value-of select="count(../WfObject) + 1"/></xsl:attribute>
			</xsl:element>

		</xsl:if>
	 </td>
	 </tr>	
	 	
	 
	 <tr>
	 	<td>
	 		<xsl:element name="input">
      				<xsl:attribute name="type">checkbox</xsl:attribute>
				<xsl:attribute name="name">WfStep<xsl:value-of select="$wfStepPos"/>~WfObject<xsl:number value="position() " format="1"/>~Ignore</xsl:attribute>
				<xsl:attribute name="value">true</xsl:attribute>
      			</xsl:element> 
    		</td>
	 
	 <td></td>
	 
	 <td>
	 	<xsl:element name="input">
	      		<xsl:attribute name="name">WfStep<xsl:value-of select="$wfStepPos"/>~WfObject<xsl:number value="position() " format="1"/>~Name</xsl:attribute>
		<xsl:attribute name="value"><xsl:value-of select="ObjectName"/></xsl:attribute>
		 <xsl:attribute name="size">50</xsl:attribute>
                       
		</xsl:element>
	</td>
	<td>
		<xsl:element name="input">
	      		<xsl:attribute name="name">WfStep<xsl:value-of select="$wfStepPos"/>~WfObject<xsl:number value="position() " format="1"/>~Order</xsl:attribute>
		<xsl:attribute name="value"><xsl:value-of select="Order"/></xsl:attribute>
		<xsl:attribute name="size">10</xsl:attribute>
                 </xsl:element>

	</td>	
	</tr>
	</div>
	 </xsl:for-each>
	 
	  <xsl:call-template name="BlankLine">  	
	  	<xsl:with-param name="wfNo"><xsl:value-of select="$wfStepPos"></xsl:value-of></xsl:with-param>
	  	<xsl:with-param name="wfStepNo"><xsl:value-of select="count(WfObject) + 1"></xsl:value-of></xsl:with-param>
  	 </xsl:call-template>
  </xsl:template>
  
  <xsl:template name="WorkflowPath">	 
    <xsl:for-each select="WfPath">
     
	  <xsl:call-template name="WorkflowStep">  	
	  	<xsl:with-param name="wfStepPos"><xsl:number value="position()" format="1"/></xsl:with-param>
	  </xsl:call-template>

    </xsl:for-each>
    
    <xsl:call-template name="AddWf">  	
	  	<xsl:with-param name="wfStepPos"><xsl:number value="position()" format="1"/></xsl:with-param>
    </xsl:call-template>
    
  </xsl:template>

<xsl:template name="AddWf">
          			
	<tr>		
	<td>
	 	<xsl:element name="input">
      			<xsl:attribute name="name">WfStep<xsl:value-of select="count(WfPath) + 1"/>~Ignore</xsl:attribute>
			<xsl:attribute name="type">checkbox</xsl:attribute>
 			<xsl:attribute name="value">true</xsl:attribute>
      		</xsl:element> 

    	</td>
    
	 <td>
	 	<xsl:element name="input">
	      		<xsl:attribute name="name">WfStep<xsl:value-of select="count(WfPath) + 1"/>~WfName</xsl:attribute>
			<xsl:attribute name="value"></xsl:attribute>
		</xsl:element>
	 	
		<xsl:element name="input">
	      		<xsl:attribute name="name">WfStep<xsl:value-of select="count(WfPath) + 1"/>~Count</xsl:attribute>
	      		<xsl:attribute name="type">hidden</xsl:attribute>
			<!-- The count has to have 1 added to it, so that the blank lines are taken care of -->
			<xsl:attribute name="value"><xsl:value-of select="1"/></xsl:attribute>
		</xsl:element>

		</td>
	 </tr>
	 
	 <xsl:call-template name="BlankLine">  	
	  	<xsl:with-param name="wfNo"><xsl:value-of select="count(WfPath) + 1"></xsl:value-of></xsl:with-param>
	  	<xsl:with-param name="wfStepNo"><xsl:value-of select="1"></xsl:value-of></xsl:with-param>
  	 </xsl:call-template>
	 
</xsl:template>

<xsl:template name="BlankLine">
<xsl:param name ="wfNo"/>
<xsl:param name ="wfStepNo"/>
<tr>
<td></td>
<td></td>
<td>
		<xsl:element name="input">
	      		<xsl:attribute name="name">WfStep<xsl:value-of select="$wfNo"/>~WfObject<xsl:value-of select="$wfStepNo"/>~Name</xsl:attribute>
		 <xsl:attribute name="size">50</xsl:attribute>
                 <xsl:attribute name="value"><xsl:value-of select="ObjectName"/></xsl:attribute>
       
		</xsl:element>


	</td>
	
	
	<td>
		<xsl:element name="input">
	      		<xsl:attribute name="name">WfStep<xsl:value-of select="$wfNo"/>~WfObject<xsl:value-of select="$wfStepNo"/>~Order</xsl:attribute>
			<xsl:attribute name="size">10</xsl:attribute>
			<xsl:attribute name="value"><xsl:value-of select="Order"/></xsl:attribute>
 
		</xsl:element>

	</td>
</tr>

</xsl:template>

<xsl:template name="fieldHeadings">
  <tr align="center">
    <font size="-1">
    <th>Delete</th>
    <th>Workflow</th>
    <th>Object</th>
    <th>Order</th>
      </font>
  </tr>
   </xsl:template>
</xsl:stylesheet>

