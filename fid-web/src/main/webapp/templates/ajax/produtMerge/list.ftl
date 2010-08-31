<#assign html >
	<#assign actionTarget="product"/>
	<#include "/templates/html/inspectionGroup/_productList.ftl"/>
</#assign>

<#escape x as x?js_string>	
	$('results').replace( '${html}' );
	findAssets();
	$('results').highlight();
</#escape>