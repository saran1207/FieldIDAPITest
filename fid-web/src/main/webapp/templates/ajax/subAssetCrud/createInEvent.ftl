<#assign html>
	<#assign assetId=asset.id />
	<#assign inEvent=true />
	<#include "/templates/html/masterEventCrud/_subAssetEvent.ftl"/>
</#assign>

<#escape x as x?js_string>
	if( $( 'attachSubAsset_${subAsset.asset.id}' ) != null ||  $('subAsset_${subAsset.asset.id}') == null  ) {
		var html = '${html}'; 
		
			$('assetComponents').insert( html );
		
		
	} else {
		alert( "<@s.text name="error.subassetattached"/>" );
	}
	
	if( $( 'subAssetCreateForm_${subAsset.asset.type.id}' ) != null ) {
		jQuery( '#subAssetCreateForm_${subAsset.asset.type.id}' ).hide('blind', 500);
	}
	
</#escape>
