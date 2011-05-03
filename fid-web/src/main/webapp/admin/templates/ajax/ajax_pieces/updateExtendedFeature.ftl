<#escape x as x?j_string >
<#assign html>
	<#include "../../html/organization/_extendedFeatures.ftl" >
</#assign>
	$('extendedFeatures').update("${html}");
</#escape>