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
	$('snSmartSearchResults').update('');
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

var ajaxUpdatingImage = ""
function submitSearch(event) {
	event.stop();
	$('snSmartSearchResults').update(ajaxUpdatingImage);
	
	
	$('snSmartSearch').request(getStandardCallbacks())
}

function updateLinkedProductInfo(product) {
	$('linkedProductId').setValue(product.id);
	$('linkedProductSerial').update(product.serialNumber);
	$('linkedProductRfid').update(product.rfidNumber);
	$('linkedProductOwner').update(product.owner);
	$('linkedProductType').update(product.type);
	
	showLinkedProductInfo();
}

function updateLinkedProductFromMultipleResults(event) {
	var result = Event.element(event);
	var product = new Object();
	
	product.id = result.getAttribute("productId");
	product.serialNumber = result.getAttribute("serialNumber");
	product.rfidNumber = result.getAttribute("rfidNumber");
	product.owner = result.getAttribute("owner");
	product.type = result.getAttribute("productType");
	
	updateLinkedProductInfo(product);
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