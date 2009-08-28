var pricingUrl = "";
function updatePrice(event) {
	event.stop();
	var parameters = new Object();
	$$(".changesPrice").each(function(element) {
		parameters[element.name] = element.getValue();
	});
	getResponse(pricingUrl, "get", parameters);
}

function setsCountry(event) {
	event.stop();
	var countryElement = Event.element(event);
	$$(".country").each(function(element) {
		element.value = countryElement.getValue();
	});
	
}

Element.extend(document).observe("dom:loaded", 
	function() {
		$$(".changesPrice").each(function(element) {
			element.observe("change", updatePrice);
		});
		$$(".updatePrice").each(function(element) {
			element.observe("click", updatePrice);
		});
		$$(".setsCountry").each(function(element) {
			element.observe("change", setsCountry);
		});
	});