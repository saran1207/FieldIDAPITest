<#escape x as x?j_string >
<#assign html>
	<#include "/templates/html/inspectionScheduleCrud/_form.ftl" >
</#assign>

	$('nextDate_${uniqueID}').update("${html}");
</#escape>