<#assign html>
	<#assign assetId=asset.id />
	<#include "/templates/html/subProductCrud/_subProductForm.ftl"/>
</#assign>

<#escape x as x?js_string>
	$('subProduct_${subProduct.asset.id}').replace('${html}');
		
	$('subProduct_${subProduct.asset.id}').highlight();
	
</#escape>