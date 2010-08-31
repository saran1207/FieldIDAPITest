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

function selectPreferredPaymentMethod() {
	
	var usingCC = $("usingCreditCard");
	
	if (usingCC != null) {
		if(usingCC.getValue() == "true") {
			enableCC();
		} else {
			enablePO();
		}
	}	
}

document.observe("dom:loaded", function() {
		$$("#payPurchaseOrder").each(function(element) {
			element.observe("click", payPurchaseOrderClick);
		});
		$$("#payCreditCard").each(function(element) {
			element.observe("click", payCreditCardClick);
		});
		
		selectPreferredPaymentMethod();
	});