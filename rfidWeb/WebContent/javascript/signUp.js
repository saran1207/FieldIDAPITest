var pricingUrl = "";
function updatePrice(event) {
	event.stop();
	var parameters = new Object();
	$$(".changesPrice").each( function(element) {
			parameters[element.name] = element.getValue();
		});
	getResponse(pricingUrl, "get", parameters);
}

Element.extend(document).observe("dom:loaded", 
	function() {
		$$(".changesPrice").each(function(element) {
				element.observe("change", updatePrice);
			});
		$$(".updatePrice").each(function(element) {
				element.observe("click", updatePrice);
			});
	});