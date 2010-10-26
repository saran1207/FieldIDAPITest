<#escape x as x?js_string >
	$('customerRefNumber').disabled = "";
	$('customerRefNumberButton').disabled = "";
	$('updatingRefNumber').hide();
	$('updatedRefNumber').style.display = "inline";
	$('updatedRefNumber').fade({ duration: 2.0});
</#escape>