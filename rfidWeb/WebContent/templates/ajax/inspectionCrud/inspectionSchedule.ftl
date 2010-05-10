<#if date?exists>
	<#assign html >
		<#include "/templates/html/inspectionCrud/_singleSchedule.ftl" >
	</#assign>
	
	<#escape x as x?js_string>
		$('schedules').insert({bottom: '${html}'});
		$('schedule_${index}').highlight();
		index++;
		scheduleListUpdated();
		$('newScheduleForm').hide();
	</#escape>
</#if>