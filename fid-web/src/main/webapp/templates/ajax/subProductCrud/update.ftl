<#assign html>
	<#assign assetId=asset.id />
	<#include "/templates/html/subProductCrud/_subProductForm.ftl"/>
</#assign>

<#escape x as x?js_string>
	$('subAsset_${subAsset.asset.id}').replace('${html}');
		
	$('subAsset_${subAsset.asset.id}').highlight();
	
</#escape>