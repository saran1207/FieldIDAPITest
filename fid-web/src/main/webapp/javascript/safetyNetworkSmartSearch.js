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

function updateLinkedAssetInfo(product) {
	populateLinkedAssetInfo(product);
	pushRegisteredProductInformationToLocalProduct(product);
}
	
function populateLinkedAssetInfo(product) {
	$('linkedAssetId').setValue(product.id);
	$('linkedAssetSerial').update(product.serialNumber);
	$('linkedAssetRfid').update(product.rfidNumber);
	$('linkedAssetOwner').update(product.owner);
	$('linkedAssetType').update(product.type);
	
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


function pushRegisteredProductInformationToLocalProduct(product) {
	if ($('uniqueID') != null && $('uniqueID').getValue() == "") {
		$("serialNumberText").value = product.serialNumber;
		$("rfidNumber").value = product.rfidNumber;
		$("customerRefNumber").value = product.referenceNumber;
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