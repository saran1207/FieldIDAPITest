function removeAssetEvent( event ) {
	removeSubAsset( Event.element(event).assetId, event );
} 
var removeString = "";
function includeSubAsset( event ) {
	if( event ) {
		event.stop();
	}
	var subAsset = $('addSubAsset');
	var assetName = subAsset.options[subAsset.selectedIndex].text;
	var item = new Element( 'li', { id: "subAsset_" + subAsset.getValue() } );
	var assetNameSpan = new Element( 'span', {id: "assetName_" + subAsset.getValue() } ).update(assetName);
	
	item.insert( new Element( 'input', { type: "hidden", name: "subAssetIds["+ numberOfSubTypes +"]", value: subAsset.getValue() } ) );
	var subAssetId = subAsset.getValue();
	numberOfSubTypes++;
	var assetLink = new Element( 'a', { id: "removeAssetLink_" + subAsset.getValue(), href: "removeSubAsset" , assetTypeId: subAssetId } ).update(removeString) ;
	assetLink.observe('click', removeAssetEvent );
	assetLink.assetId = subAsset.getValue();
	item.insert( assetNameSpan );
	item.insert( assetLink );
	$( 'subAssets' ).insert( item );
	subAsset.remove( subAsset.selectedIndex );
	
}

var numberOfSubTypes = 0;
function removeSubAsset( subAssetId, event ) {
	if( event ) {
		event.stop();
		var element = Event.element( event );
		subAssetId = element.readAttribute( "assetTypeId" );
	}
	var subAsset = $( 'subAsset_' + subAssetId );
	
	var subAssetList = $( 'addSubAsset' );
	var newOption = new Element( 'option' );

	newOption.text= $( 'assetName_' + subAssetId ).innerHTML;
	newOption.value= subAssetId;
	subAssetList.options.add( newOption );
	sortList( subAssetList.id );
	subAsset.remove();
}