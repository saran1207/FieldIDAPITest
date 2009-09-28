var networkSmartSearchUrl = "";

function showNetworkSmartSearch(event) {
	event.stop();
	$('registerOverNetworkLinkContainer').hide();
	$('linkedProductContainer').hide();
	$('networkSmartSearchContainer').show();
}

function cancelNetworkSmartSearch(event) {
	event.stop();
	$('smartSearchStatus').update('');
	$('linkedProductId').value = null;
	$('registerOverNetworkLinkContainer').show();
	$('linkedProductContainer').hide();
	$('networkSmartSearchContainer').hide();
}

function showLinkedProductInfo() {
	$('registerOverNetworkLinkContainer').hide();
	$('linkedProductContainer').show();
	$('networkSmartSearchContainer').hide();
}

function submitSearch(event) {
	event.stop();
	$('smartSearchStatus').update('');
	var params = new Object();
	
	params.vendorId = $('snSmartSearchVendors').getValue();
	params.searchText = $('snSmartSearchText').getValue();
	
	getResponse(networkSmartSearchUrl, "get", params);
}

function updateLinkedProductInfo(product) {
	$('linkedProductId').setValue(product.id);
	$('linkedProductSerial').update(product.serialNumber);
	$('linkedProductRfid').update(product.rfidNumber);
	$('linkedProductOwner').update(product.owner);
	$('linkedProductType').update(product.type);
	
	showLinkedProductInfo();
}

document.observe(
	"dom:loaded", function() {
		$('showSmartSearchLink').observe("click", showNetworkSmartSearch);
		$('snSmartSearchCancel').observe("click", cancelNetworkSmartSearch);
		$('snSmartSearchSubmit').observe("click", submitSearch);
		$('unregisterSubmit').observe("click", cancelNetworkSmartSearch);
	}
);