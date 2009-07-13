<#assign html>
	<#assign productId=product.id />
	<#include "/templates/html/subProductCrud/_subProductForm.ftl"/>
</#assign>

<#escape x as x?js_string>
	$('subProduct_${subProduct.product.id}').replace('${html}');	
		
	$('subProduct_${subProduct.product.id}').highlight();
	
</#escape>