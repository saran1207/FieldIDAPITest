var orgListUrl  = "";

function showOrgSearch(event) {
	var element =  Event.element(event);
	event.stop();
	
	showOrgPicker(element);
	
}

function showOrgPicker(element) {
	$$('.orgFilter').each(function(filter) { filter.value = element.getAttribute('orgFilter'); });
	
	var orgSelector = $('orgSelector');
	var orgPicker;
	if (element.hasClassName("searchOwner")) {
		orgPicker = element;
	} else {
		element.up(".searchOwner");
	}
	orgSelector.setStyle("position:absolute");
	translate(orgSelector, orgPicker, 0, 0);
	orgSelector.show();
	
	$('orgSelector').setAttribute('targetId', orgPicker.id);
	$('orgPickerResults').update("");
	openOrgBrowser();
	setUpOrgBrowser($(element.getAttribute("orgId")).getValue());
}

function setUpOrgBrowser(orgId) {
	if ($('orgPickerCurrentOrg').getValue() != orgId) {
		getUpdatedOrgBrowser(orgId);
	}
}
function getUpdatedOrgBrowser(orgId) {
	$('selectOrg').disable();
	$('orgBrowserLoading').show();
	var params = new Object();
	params.ownerId = orgId;
	params.orgTypeFilter =$('orgFilter').getValue();
	getResponse(orgListUrl, "get", params);
	$('orgPickerCurrentOrg').value = orgId;
}

function resultsReturned() {
	$('selectOrg').enable();
	$('orgBrowserLoading').hide();
}

function updateOrgBrowser(orgLists) {
	updateDropDown($('orgList'), orgLists.orgList, orgLists.orgId);
	updateDropDown($('customerList'), orgLists.customerList, orgLists.customerId);
	updateDropDown($('divisionList'), orgLists.divisionList, orgLists.divisionId);
	resultsReturned();
}


function updateOwner(event) {
	event.stop();
	var element = Event.element(event);
	
	var containerTarget = element.up("*[targetId]");
	
	var containerContext = containerTarget.getAttribute("targetId");
	var containerId = "";
	
	if (containerContext != null && containerContext != "") {
		containerId = "#" + containerContext; 
	}
	setOwner(containerId,  element.getAttribute("org"), element.getAttribute("orgName"));

	closeOrgPicker();
	
	var clearOrg = $$(containerId + " .clearSearchOwner");
	
	if (clearOrg.size() >= 1) {
		clearOrg.first().show();
	}
}

function closeOrgPicker() {
	$('orgSelector').hide();
}

function cancelOrgBrowse(event) {
	event.stop();
	closeOrgPicker();
}

function changeOrgList(event) {
	var element = Event.element(event);
	var selectElements = $(element.form.id).getElements(); 
	
	var foundElement = false;
	for (var i = 0; i < selectElements.size(); i++) {
		if (selectElements[i].type.startsWith('select')) {
			if (foundElement) {
				selectElements[i].options.length = 0;
			} else if (selectElements[i].id == element.id) {
				foundElement = true;
			}
		}
	}
	
	getUpdatedOrgBrowser(getOwnerValues().id);
}

function setOwner(containerId, ownerId, ownerName) {
	var orgInputs = $$(containerId + " .orgSelected");
	orgInputs.first().value=ownerId;
	orgInputs.first().next('input').value=ownerName; 
	orgInputs.first().fire('owner:change');
	
	var clearOrg = $$(containerId + " .clearSearchOwner");
	
	if (clearOrg.size() >= 1) {
		clearOrg.first().show();
	}
}

function clearOrgSearch(event) {
	var element =  Event.element(event);
	event.stop();
	element.up(".orgPicker").down(".orgSelected").value = "";
	element.up(".orgPicker").down(".orgSelected").next('input').value = "";
	element.hide();
}

function selectOrg(event) {
	event.stop();
	var element = Event.element(event);
	
	
	var containerTarget = element.up("*[targetId]");
	
	var containerContext = containerTarget.getAttribute("targetId");
	var containerId = "";
	
	if (containerContext != null && containerContext != "") {
		containerId = "#" + containerContext; 
	}
	
	var org = getOwnerValues();
	
	setOwner(containerId, org.id, org.name);
	
	closeOrgPicker();
}

function getOwnerValues() {
	var divisions = $('divisionList');
	var customers = $('customerList');
	var orgs = $('orgList');
	
	var org = new Object();
	if (isValueDefined(divisions.getValue(), -1)) {
		org.id = divisions.getValue();
		org.name = divisions.options[divisions.selectedIndex].text;
	} else if (isValueDefined(customers.getValue(), -1)) {
		org.id = customers.getValue();
		org.name = customers.options[customers.selectedIndex].text;
	} else {
		org.id = orgs.getValue();
		org.name = orgs.options[orgs.selectedIndex].text;
	}
	return org;
}





function openOrgBrowser() {
	$('orgBrowser').show();
	$('switchOrgBrowser').addClassName("selected");
	$('orgList').focus();
	$('orgSearch').hide();
	$('switchOrgSearch').removeClassName("selected");
}

function openOrgSearch() {
	$('orgSearch').show();
	$('switchOrgSearch').addClassName("selected");
	$('orgSearchName').focus();
	$('orgBrowser').hide();
	$('switchOrgBrowser').removeClassName("selected");
}

function searchForOrgs(event) {
	$('orgPickerLoading').show();
	$('orgPickerResults').update("");
	ajaxFormEvent(event);
}


function attachOrgEvents(containerCssRule) {
	$$(containerCssRule + " .searchOwner").each(function(element) {
		element.observe('click', showOrgSearch);
	});
	
	$$(containerCssRule + " .clearSearchOwner").each(function(element) {
		element.observe('click', clearOrgSearch);
	});
}
	


document.observe("dom:loaded", function() {
	attachOrgEvents("body");
	
});