<#assign html>
	<#assign assetId=asset.id />
	<#assign inEvent=true />
	<#assign updateSubAssetAction="updateSubAssetInEvent"/>
	<#include "/templates/html/subAssetCrud/_header.ftl"/>
</#assign>

<#escape x as x?js_string>
	$('subAssetHeader_${subAsset.asset.id}').update('${html}');
</#escape>