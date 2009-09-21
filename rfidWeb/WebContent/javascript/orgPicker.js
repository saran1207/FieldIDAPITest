var orgListUrl  = "";

function showOrgSearch(event) {
	var element =  Event.element(event);
	event.stop();
	
	
	Try.these(
		function() { showOrgPicker(element); },
		function() { showOrgPicker(element); } // second call to get IE to work on the second time you open the orgPicker. 
	);
}

function showOrgPicker(element) {
	$('orgFilter').value=element.getAttribute('orgFilter');
	var orgSelector = $('orgSelector');
	var orgPicker=element.up(".orgPicker");
	orgSelector.clonePosition(orgPicker, {setWidth:false, setHeight:false});
	orgSelector.setStyle("position:absolute");
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
	var params = new Object();
	params.ownerId = orgId;
	getResponse(orgListUrl, "get", params);
	$('orgPickerCurrentOrg').value = orgId;
}

function updateOrgBrowser(orgLists) {
	updateDropDown($('orgList'), orgLists.orgList, orgLists.orgId);
	updateDropDown($('customerList'), orgLists.customerList, orgLists.customerId);
	updateDropDown($('divisionList'), orgLists.divisionList, orgLists.divisionId);
	
}

function updateDropDown(select, newList, selectId) {
	
	select.options.length = 0;
	
	newList.each(function (element) {
			var option = new Element("option", { value : element.id }).update(element.name);
			select.insert(option);
		});
	
	select.value= selectId;
}


function updateOwner(event) {
	event.stop();
	var element = Event.element(event);
	
	var containerTarget = element.up("*[targetId]");
	
	var containerContext = containerTarget.getAttribute("targetId");
	var containerId = "";
	
	if (containerContext != null || containerContext != "") {
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
	
	var foundMe = false;
	for (var i = 0; i < selectElements.size(); i++) {
		if (selectElements[i].type.startsWith('select')) {
			if (foundMe) {
				selectElements[i].options.length = 0;
			} else if (selectElements[i].id == element.id) {
				foundMe = true;
			}
		}
	}
	
	getUpdatedOrgBrowser(getOwnerValues().id);
}

function setOwner(containerId, ownerId, ownerName) {
	var orgInputs = $$(containerId + " .orgSelected");
	orgInputs.first().value=ownerId;
	orgInputs.first().next('input').value=ownerName; 
	orgInputs.first().next('input').fire('field:change');
	
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
	
	if (containerContext != null || containerContext != "") {
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
	if (divisions.getValue() != undefined && divisions.getValue() != -1 ) {
		org.id = divisions.getValue();
		org.name = divisions.options[divisions.selectedIndex].text;
	} else if (customers.getValue() != undefined && customers.getValue() != -1) {
		org.id = customers.getValue();
		org.name = customers.options[customers.selectedIndex].text;
	} else {
		org.id = orgs.getValue();
		org.name = orgs.options[orgs.selectedIndex].text;
	}
	return org;
}


function searchForOrg() { 
	
}


function openOrgBrowser() {
	$('orgBrowser').show();
	$('switchOrgBrowser').addClassName("selected");
	$('orgSearch').hide();
	$('switchOrgSearch').removeClassName("selected");
}

function openOrgSearch() {
	$('orgSearch').show();
	$('switchOrgSearch').addClassName("selected");
	$('orgBrowser').hide();
	$('switchOrgBrowser').removeClassName("selected");
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