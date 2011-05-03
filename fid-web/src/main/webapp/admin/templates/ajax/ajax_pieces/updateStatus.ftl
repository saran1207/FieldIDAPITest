<#escape x as x?j_string >
<#assign html>
	<#include "../../html/organization/_status.ftl" >
</#assign>
	$('orgStatus').update("${html}");
</#escape>