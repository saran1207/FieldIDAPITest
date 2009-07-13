var updateProductTypeUrl = '';
		
function updateProductType( productTypeSelect ) {
	if( productTypeSelect.options[ productTypeSelect.selectedIndex ].value != "" ) {
		updatingProduct();
		var productTypeSelectId = productTypeSelect.options[ productTypeSelect.selectedIndex ].value;
		var url = updateProductTypeUrl + '?productTypeId='+ productTypeSelectId;
		getResponse( url, "get" );
	} else {
		replacedProductType( null, null );
	}
}

function updatingProduct() {
	var productTypeIndicator = $('productTypeIndicator');
	productTypeIndicator.style.visibility = "visible";
}

function updatingProductComplete() {
	var productTypeIndicator = $('productTypeIndicator');
	productTypeIndicator.style.visibility = "hidden";
}


function replacedProductType( infoOptions, subTypes ) {
	var infoOptionSet = $('infoOptions');
	if( infoOptions == null || infoOptions == "" ) {
		infoOptionSet.update();
	} else { 
		infoOptionSet.replace( infoOptions );
	}
}


var originalProductType = 0;
var productTypeChangeWarning = "";
function checkProductTypeChange() {
	var currentProductType = $( 'productType' ).getValue();
	
	if( currentProductType !=  originalProductType ) {
		return confirm( productTypeChangeWarning );
	}
	
	return true;
}

function saveProduct( button ) {
	if( checkProductTypeChange() ) {
		checkDuplicateRfids('rfidNumber', button, $( 'uniqueID' ).getValue() );
	} 
}