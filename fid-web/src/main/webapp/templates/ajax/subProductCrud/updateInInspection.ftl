<#assign html>
	<#assign productId=product.id />
	<#assign inInspection=true />
	<#assign updateSubProductAction="updateSubProductInInspection"/>
	<#include "/templates/html/subProductCrud/_header.ftl"/>
</#assign>

<#escape x as x?js_string>
	$('subProductHeader_${subProduct.product.id}').update('${html}');	
		
	$('subProductHeader_${subProduct.product.id}').highlight();
	
</#escape>