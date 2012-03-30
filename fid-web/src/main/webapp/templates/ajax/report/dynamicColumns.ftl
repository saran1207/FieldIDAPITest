<#if dynamicGroups[0]?exists>
	<#assign html>
		<#assign group = dynamicGroups[0]/>
		<#include "/templates/html/customizableSearch/_columnGroup.ftl"/>
	</#assign>
	
	$('${dynamicGroups[0].id}').replace('${html?js_string}');
</#if>

<#if dynamicGroups[1]?exists>
	<#assign html>
		<#assign group = dynamicGroups[1]/>
		<#include "/templates/html/customizableSearch/_columnGroup.ftl"/>
	</#assign>

	$('${dynamicGroups[1].id}').replace('${html?js_string}');
</#if>

var area = $('selectColumnNotificationArea');
area.update('<@s.text name="label.availablecolumnsupdated"/>');
area.show();