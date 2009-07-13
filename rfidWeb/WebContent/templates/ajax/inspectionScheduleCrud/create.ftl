<#assign form>
	<#include "/templates/html/inspectionScheduleCrud/_addForm.ftl" >
</#assign>
<#assign html>
	<#include "/templates/html/inspectionScheduleCrud/show.ftl" >
</#assign>

<#escape x as x?j_string >
	$('scheduleList').show();
	$('schedules').insert({top: "${html}"});
	$('newSchedule').replace("${form}");
	$('type_${inspectionSchedule.id}').highlight();
</#escape>