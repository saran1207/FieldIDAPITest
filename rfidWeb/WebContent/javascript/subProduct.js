var subProductIndex = 0;
var addSubProductUrl = "";
function getToken() {
	var token = $( 'searchToken' );
	
	if( token == null ) { return null; }
	
	return token.getValue();
}

function attachProduct( event, productId ) {
	if( event ) {
		event.stop();
		element = Event.element( event );
		productId = element.getAttribute( 'productId' );
	}
	Lightview.hide();
	
	var url = addSubProductUrl + "?uniqueID=" + $('uniqueID').value + "&subProduct.product.iD=" + productId + "&subProductIndex=" + subProductIndex + "&token=" + getToken();
	subProductIndex++;
	getResponse( url, "get" );
}



var createProductUrl = "";	

function submitCreateForm( event, form ) {
	// block default form submit
	if( event ) { 
		event.stop();
		form = Event.element( event ); 
	}
	
	form.request( { onComplete: contentCallback } );
}


var addProductUrl = "";
function addSubProduct(productType, ownerId) {
	var params = new Object();
	params.productTypeId = productType;
	params.token = getToken();
	params.ownerId = ownerId;
	
	getResponse(addProductUrl , "get", params);
}


function findSubProduct(productType) {
	$('subProductSearchForm').observe('submit', submitSearchForm);
	var productLinks = $$('.productLink');
	if (productLinks != null) {
		for (var i = 0; i < productLinks.length; i++) {
			productLinks[i].observe('click', attachProduct);
		}
	}		
}

var lookupProductUrl = "";	
var productLookupTitle = "";

function submitSearchForm(event) {
	// block default form submit
 	event.stop();
  
	Lightview.show({
		href: lookupProductUrl,
		rel: 'ajax',
		title: productLookupTitle,
		options: {
			scrolling:true, 
			width: 700, 
			height: 420,
			ajax: {
				parameters: Form.serialize('subProductSearchForm'),
				onComplete: findSubProduct
			}
		}
	});
}

var removeSubProductUrl = "";
function removeSubProduct( subProductId ) {
	var url = removeSubProductUrl + "?subProductId=" + subProductId + "&uniqueID=" + $('uniqueID').getValue();
	getResponse( url, "post" );
}


function submitForm() {
	var inputElement = new Element("input");
	inputElement.type = "hidden";
	inputElement.name = buttonPressed.name;
	inputElement.value = buttonPressed.value;
	mainForm.appendChild(inputElement);
	
	submitCreateForm( null, mainForm  );
}

var reorderProductsUrl = '';
function onDrop() {
	var rows = Sortable.sequence("productComponentList");
	var params = new Object();
	for (var i = 0; i < rows.size(); i++) {
		params['indexes[' + i + ']'] = rows[i];
	}
	params['uniqueID'] = $('uniqueID').getValue();
	
	getResponse(reorderProductsUrl, 'post', params);
}

var labelFormWarning = '';
function startOrdering() {
	var labelForms = $$('.labelForm');
	for (var i=0; i < labelForms.size(); i++) { 
		if (labelForms[i].visible()) { 
			alert(lableFormWarning);
			return;
		}
	}
	$$('.notAllowedDuringOrdering').each( function(element) { element.hide() } );
	$$('.drag').each( function(element) { element.show() } );
	Sortable.create("productComponentList", {handle: 'drag', tag: 'div', onUpdate: onDrop});
	$('startOrdering').hide();
	$('stopOrdering').show();
}

function stopOrdering() {
	$$('.notAllowedDuringOrdering').each( function(element) { element.show() } );
	$$('.drag').each( function(element) { element.hide() } );
	Sortable.destroy("productComponentList");
	$('startOrdering').show();
	$('stopOrdering').hide();
}
