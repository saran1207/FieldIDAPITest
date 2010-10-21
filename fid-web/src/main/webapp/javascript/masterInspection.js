
function clearSubProduct( subProductId ) {
	$( "subProduct_" + subProductId ).remove();
}


function attachSubProductSubmit( event ) {
	
	
}

function attachProduct( event, assetId ) {
	if( event ) {
		event.stop();
		element = Event.element( event );
		assetId = element.getAttribute( 'assetId' );
	}
	Lightview.hide();
	
	var url = addSubProductUrl + "?uniqueID=" + $('assetId').value + "&subProduct.asset.iD=" + assetId + "&subProductIndex=" + subProductIndex + "&token=" + getToken();
	subProductIndex++;
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
		