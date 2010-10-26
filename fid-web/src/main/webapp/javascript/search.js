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
function assetTypeChanged(assetType) {
	var dynamicSections = $$('.dynamic div');
	for (var i = 0; i < dynamicSections.length; i++) {
		dynamicSections[i].remove();
	}
	var area = $('selectColumnNotificationArea');
	area.update(updatingColumnText);
	area.show();
	getResponse(dynamicColumnUrl, "GET", { "criteria.assetType": Element.extend(assetType).getValue(), "criteria.assetTypeGroup" : $('assetTypeGroup').getValue() } );
}

var groupToAssetType = new Object();
function assetTypeGroupChanged() {
	var assetTypeGroup = $('assetTypeGroup');
	var selectedAssetTypeGroupName = assetTypeGroup.options[assetTypeGroup.selectedIndex].text;
	var newSelectList = new Array().concat({id:"",name:""}).concat(groupToAssetType[selectedAssetTypeGroupName])
	updateDropDown($('assetType'), newSelectList, $('assetType').getValue());
	$('assetType').onchange();
}