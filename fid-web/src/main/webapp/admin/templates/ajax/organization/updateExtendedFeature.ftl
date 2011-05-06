<#escape x as x?j_string >
<#assign feature="${featureName}">
<#assign featureLabel="${action.getExtendedFeatureLabel(featureName)}">
<#assign html>
	<#include "../../html/organization/_extendedFeatures.ftl" >
</#assign>
	$('extendedFeatureRow_${feature}').update("${html}");
</#escape>