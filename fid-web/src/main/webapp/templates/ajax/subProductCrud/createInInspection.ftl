<#assign html>
	<#assign assetId=asset.id />
	<#assign inInspection=true />
	<#include "/templates/html/masterInspectionCrud/_subProductInspection.ftl"/>
</#assign>

<#escape x as x?js_string>
	if( $( 'attachSubProduct_${subProduct.asset.id}' ) != null ||  $('subProduct_${subProduct.asset.id}') == null  ) {
		var html = '${html}'; 
		
			$('productComponents').insert( html );
		
		
	} else {
		alert( "<@s.text name="error.subproductattached"/>" );
	}
	
	if( $( 'subProductCreateForm_${subProduct.asset.type.id}' ) != null ) {
		Effect.BlindUp( 'subProductCreateForm_${subProduct.asset.type.id}' );
	}
	
	$('subProduct_${subProduct.asset.id}').highlight();
	
</#escape>
