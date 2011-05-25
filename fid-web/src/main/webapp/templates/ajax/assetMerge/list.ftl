<#assign html >
	<#assign actionTarget="asset"/>
    <#assign lookingForAssetsOfType=true>
	<#include "/templates/html/eventGroup/_assetList.ftl"/>
</#assign>

<#escape x as x?js_string>	
	$('results').replace( '${html}' );
	findAssets();
	$('results').highlight();
	usingAjaxPagination=true;
	attachListenerToPageJumpInputs();
</#escape>
