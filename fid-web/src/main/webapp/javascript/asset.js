var updateAssetTypeUrl = '';

function updateAssetType( assetTypeSelect ) {
	if( assetTypeSelect.options[ assetTypeSelect.selectedIndex ].value != "" ) {
		updatingAsset();
		var assetTypeSelectId = assetTypeSelect.options[ assetTypeSelect.selectedIndex ].value;
		var url = updateAssetTypeUrl + '?assetTypeId='+ assetTypeSelectId + "&ownerId=" + $('ownerId').value;
		getResponse( url, "get" );
	} else {
		replaceInfoOptions( null, null );
        //into autoschedule.js
		replaceEventSchedules( null );
	}
}


function updatingAsset() {
	var assetTypeIndicator = $('assetTypeIndicator');
	assetTypeIndicator.style.visibility = "visible";
}

function updatingAssetComplete() {
	var assetTypeIndicator = $('assetTypeIndicator');
	assetTypeIndicator.style.visibility = "hidden";
}


function replaceInfoOptions( infoOptions, subTypes ) {
	var infoOptionSet = $('infoOptions');
	if( infoOptions == null || infoOptions == "" ) {
		infoOptionSet.update();
	} else { 
		infoOptionSet.replace( infoOptions );
	}
}

var originalAssetType = 0;
var assetTypeChangeWarning = "";
function checkAssetTypeChange() {
	var currentAssetType = $( 'assetType' ).getValue();
	
	if( currentAssetType !=  originalAssetType ) {
		return confirm( assetTypeChangeWarning );
	}
	
	return true;
}

function saveAsset( button ) {
	if( checkAssetTypeChange() ) {
		checkDuplicateRfids('rfidNumber', button, $( 'uniqueID' ).getValue() );
	} 
}