var autoAttributeUrl = "";
function updateAttributes(attribute) {
	
	var theForm = Element.extend(attribute).up("form");
	
	var selectedAssetId;
	var elements = theForm.elements
	for( var i = 0; i < elements.length; i++ ) {
		if( elements[i].id == "assetType" ) {
			selectedAssetId = Element.extend( elements[i] ).getValue();
		}
	}

   	var url = autoAttributeUrl + "?uniqueID=" + selectedAssetId;
   		
	// iterate through the rest of the dynamic info fields and add them to the url
	var lookUpIndex = 0;
	for (i=0; i < theForm.elements.length; i++) {		
		if (Element.extend(theForm.elements[i]).hasClassName('attribute')) {
			if (theForm.elements[i].type == 'select-one') {
				url += "&lookUpInputs[" + lookUpIndex +"].id="+theForm.elements[i].id+"&lookUpInputs[" + lookUpIndex + "].name="+theForm.elements[i].value;
				lookUpIndex++;
			}
		}
	}
	
	getResponse( url, 'get' );		
}

function populateFromAutoAttributes(jsonObject) {
	if( jsonObject.templateFound ) { 
		
		for( var i = 0; i < jsonObject.fields.length; i++ ) {
			var element = $( jsonObject.fields[i].id );
			if( element != null ) {
				if(  element.nodeType == 'SELECT' ) {
					for( var j = 0; j < element.options; j++ ) {
						if( element.options[j].value == jsonObject.fields[i].name ) {
							element.selectedIndex = j;
						}
					}
				} else {
					element.value = jsonObject.fields[i].name;
				}
			}  
		}
	}
		
}