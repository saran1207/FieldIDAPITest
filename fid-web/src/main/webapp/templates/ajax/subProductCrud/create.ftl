<#assign subAsset_index=subAssetIndex/>
<#assign html>
	<#include "/templates/html/subProductCrud/_subProductForm.ftl"/>
</#assign>

<#escape x as x?js_string>
	if( $('subAsset_${subAsset.asset.id}') == null ) {
		$('assetComponentList').insert( '${html}' );
		if( $( 'subAssetCreateForm_${subAsset.asset.type.id}' ) != null ) {
			Effect.BlindUp( 'subAssetCreateForm_${subAsset.asset.type.id}' );
		}
		
	} else {
		alert( "<@s.text name="error.subproductattached"/>" );
	}
	
</#escape>