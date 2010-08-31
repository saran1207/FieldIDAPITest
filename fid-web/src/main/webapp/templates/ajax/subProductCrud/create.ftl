<#assign subProduct_index=subProductIndex/>
<#assign html>
	<#include "/templates/html/subProductCrud/_subProductForm.ftl"/>
</#assign>

<#escape x as x?js_string>
	if( $('subProduct_${subProduct.product.id}') == null ) {
		$('productComponentList').insert( '${html}' );
		if( $( 'subProductCreateForm_${subProduct.product.type.id}' ) != null ) { 
			Effect.BlindUp( 'subProductCreateForm_${subProduct.product.type.id}' );
		}
		
	} else {
		alert( "<@s.text name="error.subproductattached"/>" );
	}
	
</#escape>