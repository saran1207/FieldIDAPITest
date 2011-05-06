<#escape x as x?j_string >
<#assign html>
	<#include "../../html/organization/_nameDisplay.ftl" >
</#assign>
<#assign loginUrl>
	<#include "../../html/organization/_loginUrl.ftl" >
</#assign>
	$('tenantName').update("${html}");
	$('loginUrl').update("${loginUrl}");
</#escape>