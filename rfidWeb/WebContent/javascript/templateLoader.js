function updateTemplate(theForm) {

	var selectedProductIndex = document.getElementById('productInfoSelect').selectedIndex;
	var selectedProductId = document.getElementById('productInfoSelect').options[selectedProductIndex].value;

   	var url = "editproduct.do?method=ajaxUpdateTemplate&productInfoId=" + selectedProductId;
   		

	// iterate through the rest of the dynamic info fields and add them to the url
	for (i=0; i < theForm.elements.length; i++) {		
		if (theForm.elements[i].className == 'dynamic-InfoField') {
			if (theForm.elements[i].type == 'select-one') {
				url += "&"+theForm.elements[i].name+"="+theForm.elements[i].value;
			}
		}
	}
	
	new Ajax.Request(url, {
		method: 'post',
		onSuccess: function(transport) {
			populateFromTemplate(transport.responseText);
		}
	});		
}

function populateFromTemplate(jsonString) {
	var jsonObject = JSON.parse(jsonString);
	
	if( jsonObject.templateFound == "true" ) { 
		
		for( var i = 0; i < jsonObject.fields.length; i++ ) {
			var element = document.getElementById( jsonObject.fields[i].id );
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