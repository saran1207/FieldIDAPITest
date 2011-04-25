<#assign list>
	<#include "/templates/html/eventScheduleCrud/_list.ftl" >
</#assign>

<#escape x as x?j_string >
	$('scheduleList').replace("${list}");
	$('type_${eventSchedule.id}').highlight();
</#escape>