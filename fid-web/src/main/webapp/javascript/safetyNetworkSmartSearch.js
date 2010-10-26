var networkSmartSearchUrl = "";

function showNetworkSmartSearch(event) {
	event.stop();
	$('networkSmartSearchContainer').setStyle("position:absolute");
	$('networkSmartSearchContainer').show();
	translate( $('networkSmartSearchContainer'),$('registerOverNetworkLinkContainer'), 0, 0);
	
	$('registerOverNetworkLinkContainer').hide();
	$('linkedAssetContainer').hide();
	
}

function cancelNetworkSmartSearch(event) {
	event.stop();
	$('snSmartSearchResults').update('');
	$('linkedAssetId').value = null;
	$('registerOverNetworkLinkContainer').show();
	$('linkedAssetContainer').hide();
	$('networkSmartSearchContainer').hide();
}

function showLinkedAssetInfo() {
	$('registerOverNetworkLinkContainer').hide();
	$('linkedAssetContainer').show();
	$$('#networkSmartSearchContainer').invoke("hide");
}

var ajaxUpdatingImage = "";
function submitSearch(event) {
	event.stop();
	$('snSmartSearchResults').update(ajaxUpdatingImage);
	
	
	$('snSmartSearch').request(getStandardCallbacks());
}

function updateLinkedAssetInfo(asset) {
	populateLinkedAssetInfo(asset);
	pushRegisteredAssetInformationToLocalAsset(asset);
}
	
function populateLinkedAssetInfo(asset) {
	$('linkedAssetId').setValue(asset.id);
	$('linkedAssetSerial').update(asset.serialNumber);
	$('linkedAssetRfid').update(asset.rfidNumber);
	$('linkedAssetOwner').update(asset.owner);
	$('linkedAssetType').update(asset.type);
	
	showLinkedAssetInfo();
}

function updateLinkedAssetFromMultipleResults(event) {
	var result = Event.element(event);
	var asset = new Object();
	
	asset.id = result.getAttribute("assetId");
	asset.serialNumber = result.getAttribute("serialNumber");
	asset.rfidNumber = result.getAttribute("rfidNumber");
	asset.owner = result.getAttribute("owner");
	asset.type = result.getAttribute("assetType");
	asset.referenceNumber = result.getAttribute('referenceNumber');
	updateLinkedAssetInfo(asset);
}


function pushRegisteredAssetInformationToLocalAsset(asset) {
	if ($('uniqueID') != null && $('uniqueID').getValue() == "") {
		$("serialNumberText").value = asset.serialNumber;
		$("rfidNumber").value = asset.rfidNumber;
		$("customerRefNumber").value = asset.referenceNumber;
	}
}


var snSmartSearch = "";
document.observe(
	"dom:loaded", function() {
			
		Element.extend(document.body).insert(snSmartSearch);
		
		$$('#showSmartSearchLink').invoke("observe", "click", showNetworkSmartSearch);
		$$('#snSmartSearchCancel').invoke("observe", "click", cancelNetworkSmartSearch);
		$$('#snSmartSearchSubmit').invoke("observe", "click", submitSearch);
		$$('#unregisterSubmit').invoke("observe", "click", cancelNetworkSmartSearch);
	}
); 