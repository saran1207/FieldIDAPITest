<#escape x as x?j_string >
<#assign html>
	<#include "/templates/html/assetTypeScheduleCrud/_form.ftl" >
</#assign>
<#if schedule.override>
	<#assign containerId="eventFrequencyOverride_${eventTypeId}_${schedule.owner.id}"/>
<#else>
	<#assign containerId="eventFrequency_${eventTypeId}" />
</#if>

	
	$('${containerId}').update("${html}");
	

</#escape>