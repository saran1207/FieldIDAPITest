<#escape x as x?js_string >

	<#assign assetEventSchedules>	
		<#include "/templates/html/eventCrud/_schedulesList.ftl" />
	</#assign>
	
	replaceEventSchedules( "${assetEventSchedules}" );
	
</#escape>