<#escape x as x?j_string >
<#assign html>
	<#include "../../html/organization/_nameForm.ftl" >
</#assign>
	$('tenantName').update("${html}");
</#escape>