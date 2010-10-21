<#assign html>
	<#assign assetId=asset.id />
	<#assign inInspection=true />
	<#assign updateSubProductAction="updateSubProductInInspection"/>
	<#include "/templates/html/subProductCrud/_header.ftl"/>
</#assign>

<#escape x as x?js_string>
	$('subProductHeader_${subProduct.asset.id}').update('${html}');
		
	$('subProductHeader_${subProduct.asset.id}').highlight();
	
</#escape>