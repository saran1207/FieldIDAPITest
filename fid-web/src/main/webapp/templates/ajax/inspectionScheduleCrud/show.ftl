<#escape x as x?j_string >
<#assign html>
	<#include "/templates/html/inspectionScheduleCrud/_show.ftl" >
</#assign>

	$('nextDate_${uniqueID}').update("${html}");


</#escape>