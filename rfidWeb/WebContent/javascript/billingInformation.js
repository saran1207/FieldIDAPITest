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

document.observe("dom:loaded", function() {
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