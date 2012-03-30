
	if( $( 'asset_${asset.id}' ) != null ) {
		$( 'asset_${asset.id}' ).remove();
	}
	
	
	<#if page?exists && page.list.size() gt 4 >
		<#assign asset=page.list[4] >
		<#assign html>
			<#include "/templates/html/projects/_attachedAsset.ftl"/> 
		</#assign>
		$( 'linkedAssets' ).insert( { bottom:'${html?js_string}' } );
		$$( '#asset_${asset.id} .removeAssetLink' ).first().observe('click', removeAsset);
	</#if>
	
	if( $$( '#linkedAssets div').size() == 0 ) {
		$('linkedAssets').hide();
		$('noLinkedAssets').show();
	}
	
	
	
${action.clearFlashScope()}