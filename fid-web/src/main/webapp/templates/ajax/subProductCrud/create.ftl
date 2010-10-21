<#assign subProduct_index=subProductIndex/>
<#assign html>
	<#include "/templates/html/subProductCrud/_subProductForm.ftl"/>
</#assign>

<#escape x as x?js_string>
	if( $('subProduct_${subProduct.asset.id}') == null ) {
		$('productComponentList').insert( '${html}' );
		if( $( 'subProductCreateForm_${subProduct.asset.type.id}' ) != null ) {
			Effect.BlindUp( 'subProductCreateForm_${subProduct.asset.type.id}' );
		}
		
	} else {
		alert( "<@s.text name="error.subproductattached"/>" );
	}
	
</#escape>