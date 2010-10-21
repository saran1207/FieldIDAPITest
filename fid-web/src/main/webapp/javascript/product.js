var updateProductTypeUrl = '';
		
function updateProductType( assetTypeSelect ) {
	if( assetTypeSelect.options[ assetTypeSelect.selectedIndex ].value != "" ) {
		updatingProduct();
		var productTypeSelectId = assetTypeSelect.options[ assetTypeSelect.selectedIndex ].value;
		var url = updateProductTypeUrl + '?assetTypeId='+ productTypeSelectId;
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