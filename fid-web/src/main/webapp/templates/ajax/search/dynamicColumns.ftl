<#assign html>
	<#list dynamicGroups as group>
		<#include "/templates/html/customizableSearch/_columnGroup.ftl"/>
	</#list>
</#assign>

$('${dynamicGroups[0].id}').replace('${html?js_string}');

var area = $('selectColumnNotificationArea');
area.update('<@s.text name="label.availablecolumnsupdated"/>');
area.show();