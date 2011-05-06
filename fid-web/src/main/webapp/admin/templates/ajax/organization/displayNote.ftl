<#escape x as x?j_string >
<#assign html>
	<#include "../../html/organization/_noteDisplay.ftl" >
</#assign>
	$('notes').update("${html}");
</#escape>