
function attachAsset( event, assetId ) {
	if( event ) {
		event.stop();
		element = Event.element( event );
		assetId = element.getAttribute( 'assetId' );
	}
    closeLightbox();
	
	var url = addSubAssetUrl + "?uniqueID=" + $('assetId').value + "&subAsset.asset.iD=" + assetId + "&subAssetIndex=" + subAssetIndex + "&token=" + getToken();
	subAssetIndex++;
	getResponse( url, "get" );
}

function submitForm() {
	var inputElement = new Element("input");
	inputElement.type = "hidden";
	inputElement.name = buttonPressed.name;
	inputElement.value = buttonPressed.value;
	mainForm.appendChild(inputElement);
	
	submitCreateForm( null, mainForm  );
}
		