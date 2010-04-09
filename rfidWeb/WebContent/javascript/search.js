var formChanged = false;
function changedForm() {
	formChanged = true;
	var links = $$('.saveLink');
	for (var i = 0; i < links.size(); i++) {
		links[i].addClassName("disabled");
	}
}

function cancelReportChanges(form) {
	formChanged = false;
	var links = $$('.saveLink');
	for (var i = 0; i < links.size(); i++) {
		links[i].removeClassName("disabled");
	}
	clearForm(form, true);
}

var updatingColumnText = ""; 
var dynamicColumnUrl = "";
function productTypeChanged(productType) {
	var dynamicSections = $$('.dynamic div');
	for (var i = 0; i < dynamicSections.length; i++) {
		dynamicSections[i].remove();
	}
	var area = $('selectColumnNotificationArea');
	area.update(updatingColumnText);
	area.show();
	getResponse(dynamicColumnUrl, "GET", { "criteria.productType": Element.extend(productType).getValue(), "criteria.productTypeGroup" : $('productTypeGroup').getValue() } );
}

var groupToProductType = new Object();
function productTypeGroupChanged() {
	var productTypeGroup = $('productTypeGroup');
	var selectedProductTypeGroupName = productTypeGroup.options[productTypeGroup.selectedIndex].text;
	var newSelectList = new Array().concat({id:"",name:""}).concat(groupToProductType[selectedProductTypeGroupName])
	updateDropDown($('productType'), newSelectList, $('productType').getValue());
	$('productType').onchange();
}