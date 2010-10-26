<#assign html>
	<#assign assetId=asset.id />
	<#assign inInspection=true />
	<#include "/templates/html/masterInspectionCrud/_subAssetInspection.ftl"/>
</#assign>

<#escape x as x?js_string>
	if( $( 'attachSubAsset_${subAsset.asset.id}' ) != null ||  $('subAsset_${subAsset.asset.id}') == null  ) {
		var html = '${html}'; 
		
			$('assetComponents').insert( html );
		
		
	} else {
		alert( "<@s.text name="error.subproductattached"/>" );
	}
	
	if( $( 'subAssetCreateForm_${subAsset.asset.type.id}' ) != null ) {
		Effect.BlindUp( 'subAssetCreateForm_${subAsset.asset.type.id}' );
	}
	
	$('subAsset_${subAsset.asset.id}').highlight();
	
</#escape>
