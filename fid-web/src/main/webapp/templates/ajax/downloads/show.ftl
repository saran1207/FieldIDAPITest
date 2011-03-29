<#escape x as x?j_string >
<#assign html>
	<#include "/templates/html/downloads/_show.ftl" >
</#assign>
	$('download_${fileId}').update("${html}");
</#escape>