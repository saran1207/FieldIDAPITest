<#assign html>
	<#list dynamicGroups as group>
		<#include "/templates/html/customizableSearch/_columnGroup.ftl"/>
	</#list>
</#assign>

$('${dynamicGroups[0].id}').replace('${html?js_string}');

$('${dynamicGroups[0].id}').highlight();
var area = $('selectColumnNotificationArea');
area.update('<@s.text name="label.availablecolumnsupdated"/>');
area.show();
area.highlight({ endcolor: "#D0DAFD", afterFinish: 
		function() {
			$('selectColumnNotificationArea').fade({ delay:5 }); 
		} 
	});
