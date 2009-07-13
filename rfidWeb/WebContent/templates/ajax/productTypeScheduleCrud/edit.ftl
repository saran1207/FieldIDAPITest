<#escape x as x?j_string >
<#assign html>
	<#include "/templates/html/productTypeScheduleCrud/_form.ftl" >
</#assign>
<#if schedule.customer?exists>
	<#assign containerId="eventFrequencyOverride_${inspectionTypeId}_${schedule.customer.id}"/>
<#else>
	<#assign containerId="eventFrequency_${inspectionTypeId}" />
</#if>

	
	$('${containerId}').innerHTML = "${html}";
	

</#escape>