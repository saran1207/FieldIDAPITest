<#escape x as x?j_string >
<#assign html>
	<#include "/templates/html/eventScheduleCrud/_show.ftl" >
</#assign>

	$('nextDate_${uniqueID}').update("${html}");


</#escape>