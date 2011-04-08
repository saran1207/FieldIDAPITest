<#escape x as x?j_string >
<#assign html>
	<#include "/templates/html/customerMerge/_list.ftl" >
</#assign>
	$('results').update("${html}");
	$('results').show();
</#escape>