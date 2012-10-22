<%@ taglib uri="/WEB-INF/mrald.tld" prefix="mrald"%>
<%@ page import="org.mitre.mrald.util.*" %>

<mrald:validate />
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
    String tableName = WebUtils.getRequiredParameter( request, "tableName" );
    String pkColumnName = WebUtils.getRequiredParameter( request, "pkColumnName" );
    String listColumnName = WebUtils.getRequiredParameter( request, "listColumnName" );
    String filterCategoricalNo = WebUtils.getRequiredParameter( request, "filterCategoricalNo" );
    String datasource = WebUtils.getRequiredParameter( request, "datasource" );
   	String filterTable = WebUtils.getOptionalParameter( request, "filterTable" );
    String filterColumn = WebUtils.getOptionalParameter( request, "filterColumn" );
    String filterColumnValue = WebUtils.getOptionalParameter( request, "filterColumnValue" );
   	String getNextLink = WebUtils.getOptionalParameter( request, "getNextLink" );
   	if (getNextLink.equals("")) getNextLink ="true";
   
%>
<select name="FilterCategorical<%=filterCategoricalNo%>" onclick = "clickNav(this.form.FilterCategorical<%=filterCategoricalNo%>, '<%=filterCategoricalNo%>','<%=getNextLink%>');">
<option value=""/>
<mrald:dropDownList table="<%=tableName%>" datasource="<%=datasource%>" pkColumn="<%=pkColumnName%>" listColumn="<%=listColumnName%>" filterTable="<%=filterTable%>" filterColumn="<%=filterColumn%>" filterColumnValue="<%=filterColumnValue%>"/></select>

