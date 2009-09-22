var pricingUrl = "";
function updatePriceClick(event) {
	event.stop();
	updatePrice(event);
}

function updatePrice() {
	var parameters = new Object();
	$$(".changesPrice").each(function(element) {
		parameters[element.name] = element.getValue();
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

function payPurchaseOrderClick(event) {
	event.stop();
	enablePO();
}

function payCreditCardClick(event) {
	event.stop();
	enableCC();
}

function enableCC() {
	$("usingCreditCard").value = "true";
	$$(".payCC").invoke("enable");
	$$(".payPO").invoke("disable");
}

function enablePO() {
	$("usingCreditCard").value = "false";
	$$(".payPO").invoke("enable");
	$$(".payCC").invoke("disable");
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
		$$("#payPurchaseOrder").each(function(element) {
			element.observe("click", payPurchaseOrderClick);
		});
		$$("#payCreditCard").each(function(element) {
			element.observe("click", payCreditCardClick);
		});
		
		if($("usingCreditCard").getValue() == "true") {
			enableCC();
		} else {
			enablePO();
		}
	});