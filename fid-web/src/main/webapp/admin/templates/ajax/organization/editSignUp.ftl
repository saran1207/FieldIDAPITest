<#escape x as x?j_string >
<#assign html>
	<#include "../../html/organization/_signUpDetailsForm.ftl" >
</#assign>
	$('signUpDetails').update("${html}");
</#escape>