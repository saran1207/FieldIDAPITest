<#assign html>
	<#assign assetId=asset.id />
	<#assign inInspection=true />
	<#assign updateSubAssetAction="updateSubProductInInspection"/>
	<#include "/templates/html/subProductCrud/_header.ftl"/>
</#assign>

<#escape x as x?js_string>
	$('subAssetHeader_${subAsset.asset.id}').update('${html}');
		
	$('subAssetHeader_${subAsset.asset.id}').highlight();
	
</#escape>