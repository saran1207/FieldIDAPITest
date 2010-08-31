<#assign html>
	<#assign productId=product.id />
	<#assign inInspection=true />
	<#include "/templates/html/masterInspectionCrud/_subProductInspection.ftl"/>
</#assign>

<#escape x as x?js_string>
	if( $( 'attachSubProduct_${subProduct.product.id}' ) != null ||  $('subProduct_${subProduct.product.id}') == null  ) {
		var html = '${html}'; 
		
			$('productComponents').insert( html );
		
		
	} else {
		alert( "<@s.text name="error.subproductattached"/>" );
	}
	
	if( $( 'subProductCreateForm_${subProduct.product.type.id}' ) != null ) {
		Effect.BlindUp( 'subProductCreateForm_${subProduct.product.type.id}' );
	}
	
	$('subProduct_${subProduct.product.id}').highlight();
	
</#escape>
