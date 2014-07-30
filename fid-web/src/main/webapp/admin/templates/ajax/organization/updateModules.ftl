<#escape x as x?j_string >
<#assign html>
	<#include "../../html/organization/_modules.ftl" >
</#assign>
	$('modules').update("${html}");
</#escape>