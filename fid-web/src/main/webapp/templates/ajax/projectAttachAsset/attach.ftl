<#assign html >
	<#include "/templates/html/projects/_attachedAsset.ftl"/>
</#assign>

<#escape x as x?js_string>
	if( $('noLinkedAssets').visible() ) {
		$('noLinkedAssets').hide();
		$('linkedAssets').show();
	}
	
	$( 'linkedAssets' ).insert( { top:'${html}' } );
	if( $$( '#asset_${asset.id} .removeAssetLink' ).size() > 0 ) {
		$$( '#asset_${asset.id} .removeAssetLink' )[0].observe( 'click', removeAsset );
	}
	$( 'linkedAssetsMore' ).show();
	closeAssetSearch();
	
	var assets = $$( '#linkedAssets .projectAsset' );
	if( assets.size() > 5 ) {
		assets.last().remove();
	}
	
</#escape>

${action.clearFlashScope()}