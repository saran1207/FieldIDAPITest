<#escape x as x?j_string >
<#assign html>
	<#include "/templates/html/productTypeScheduleCrud/_form.ftl" >
</#assign>
<#if schedule.override>
	<#assign containerId="eventFrequencyOverride_${inspectionTypeId}_${schedule.owner.id}"/>
<#else>
	<#assign containerId="eventFrequency_${inspectionTypeId}" />
</#if>

	
	$('${containerId}').update("${html}");
	

</#escape>