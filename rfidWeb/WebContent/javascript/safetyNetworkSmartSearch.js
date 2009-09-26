var networkSmartSearchUrl = "";

function showNetworkSmartSearch(event) {
	event.stop();
	$('registerOverNetworkLinkContainer').hide();
	$('linkedProductContainer').hide();
	$('networkSmartSearchContainer').show();
}

function cancelNetworkSmartSearch(event) {
	event.stop();
	$('linkedProductId').value = null;
	$('registerOverNetworkLinkContainer').show();
	$('linkedProductContainer').hide();
	$('networkSmartSearchContainer').hide();
}

function showLinkedProductInfo(event) {
	event.stop();
	$('registerOverNetworkLinkContainer').hide();
	$('linkedProductContainer').show();
	$('networkSmartSearchContainer').hide();
}

function submitSearch(event) {
	event.stop();
	
	var params = new Object();
	
	params.vendorId = $('snSmartSearchVendors').getValue();
	params.searchText = $('snSmartSearchText').getValue();
	
	getResponse(networkSmartSearchUrl, "get", params);
}

document.observe(
	"dom:loaded", function() {
		$('showSmartSearchLink').observe("click", showNetworkSmartSearch);
		$('snSmartSearchCancel').observe("click", cancelNetworkSmartSearch);
		$('snSmartSearchSubmit').observe("click", submitSearch);
	}
);