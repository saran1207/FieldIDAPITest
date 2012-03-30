<#assign html>
	<#assign assetId=asset.id />
	<#include "/templates/html/subAssetCrud/_subAssetForm.ftl"/>
</#assign>

<#escape x as x?js_string>
	$('subAsset_${subAsset.asset.id}').replace('${html}');
</#escape>