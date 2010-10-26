<#assign html>
	<#assign assetId=asset.id />
	<#assign inInspection=true />
	<#assign updateSubAssetAction="updateSubAssetInInspection"/>
	<#include "/templates/html/subAssetCrud/_header.ftl"/>
</#assign>

<#escape x as x?js_string>
	$('subAssetHeader_${subAsset.asset.id}').update('${html}');
		
	$('subAssetHeader_${subAsset.asset.id}').highlight();
	
</#escape>