var networkSmartSearchUrl = "";

function showNetworkSmartSearch(event) {
	event.stop();
	$('networkSmartSearchContainer').setStyle("position:absolute");
	$('networkSmartSearchContainer').show();
	translate( $('networkSmartSearchContainer'),$('registerOverNetworkLinkContainer'), 0, 0);
	
	$('registerOverNetworkLinkContainer').hide();
	$('linkedProductContainer').hide();
	
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
	$$('#networkSmartSearchContainer').invoke("hide");
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
var snSmartSearch = "";
document.observe(
	"dom:loaded", function() {
			
		document.body.insert(snSmartSearch);
		
		$('showSmartSearchLink').observe("click", showNetworkSmartSearch);
		$('snSmartSearchCancel').observe("click", cancelNetworkSmartSearch);
		$('snSmartSearchSubmit').observe("click", submitSearch);
		$('unregisterSubmit').observe("click", cancelNetworkSmartSearch);
	}
); 