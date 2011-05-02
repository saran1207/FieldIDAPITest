<#escape x as x?js_string >

	<#assign assetEventSchedules>
		<#include "/templates/html/eventCrud/_schedules.ftl" />
	</#assign>

	updatingAssetComplete();
	replaceEventSchedules( "${assetEventSchedules}" );

</#escape>