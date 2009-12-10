var pricingUrl = "";
function updatePriceClick(event) {
	event.stop();
	updatePrice(event);
}

function updatePrice() {
	var elementNames = new Hash();

	var form = $('mainContent').serialize(true);
	
	var parameters = new Object();
	
	$$(".changesPrice").each(function(element) {
		parameters[element.name] = form[element.name];
	});

	
	getResponse(pricingUrl, "get", parameters);
}

function setsCountry(event) {
	var countryElement = Event.element(event);
	$$(".country").each(function(element) {
		element.value = countryElement.getValue();
	});
}

var validatePromoCodeUrl = "";
function validatePromoCode(event) {
	var parameters = new Object();
	$$("#promoCode").each(function(element) {
		parameters[element.name] = element.getValue();
	});
	getResponse(validatePromoCodeUrl, "get", parameters);
	
}


Element.extend(document).observe("dom:loaded", 
	function() {
		$$(".changesPrice").each(function(element) {
			element.observe("change", updatePrice);
		});
		$$(".updatePrice").each(function(element) {
			element.observe("click", updatePriceClick);
		});
		$$(".setsCountry").each(function(element) {
			element.observe("change", setsCountry);
		});
		$$("#promoCode").each(function(element) {
			element.observe("change", validatePromoCode);
		});
		
	});