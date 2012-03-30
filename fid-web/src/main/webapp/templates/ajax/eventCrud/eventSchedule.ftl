<#if date?exists>
	<#assign html >
		<#include "/templates/html/eventCrud/_singleSchedule.ftl" >
	</#assign>
	
	<#escape x as x?js_string>
		$('schedules').insert({bottom: '${html}'});
		index++;
		scheduleListUpdated();
		$('newScheduleForm').hide();
	</#escape>
</#if>