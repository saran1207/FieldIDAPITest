<#escape x as x?j_string >
<#assign html>
	<#include "../../html/organization/_signUpDetails.ftl" >
</#assign>
	$('signUpDetails').update("${html}");
    $$('.currentPlanDisplay').each(function(element) { element.update("${primaryOrg.signUpPackage.displayName}"); } )
</#escape>