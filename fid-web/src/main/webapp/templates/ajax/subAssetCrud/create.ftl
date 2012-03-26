<#assign subAsset_index=subAssetIndex/>
<#assign html>
	<#include "/templates/html/subAssetCrud/_subAssetForm.ftl"/>
</#assign>

<#escape x as x?js_string>
	if( $('subAsset_${subAsset.asset.id}') == null ) {
		$('assetComponentList').insert( '${html}' );
		if( $( 'subAssetCreateForm_${subAsset.asset.type.id}' ) != null ) {
			jQuery( '#subAssetCreateForm_${subAsset.asset.type.id}' ).hide('blind', 500);
		}
		
	} else {
		alert( "<@s.text name="error.subassetattached"/>" );
	}
	
</#escape>