<#escape x as x?j_string >
<#assign html>
	<#include "/templates/html/eventScheduleCrud/_show.ftl" >
</#assign>
<#assign jobNameHtml>
	<#include "/templates/html/eventScheduleCrud/_showJob.ftl" >
</#assign>

	$('nextDate_${uniqueID}').update("${html}");
	$('jobName').update("${jobNameHtml}")

</#escape>