<#escape x as x?js_string >

	<#assign assetEventSchedules>	
		<#include "/templates/html/assetCrud/_assetEventSchedules.ftl">
	</#assign>
	
	replaceEventSchedules( "${assetEventSchedules}" );
	
</#escape>