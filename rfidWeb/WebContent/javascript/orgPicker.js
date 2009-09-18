var orgListUrl  = "";

function showOrgSearch(event) {
	var element =  Event.element(event);
	event.stop();
	
	$('orgFilter').value=element.getAttribute('orgFilter');
	
	var orgSelector = $('orgSelector');
	orgSelector.clonePosition(element.up(".orgPicker"), {setWidth:false, setHeight:false});
	orgSelector.style.position = "absolute";
	orgSelector.show();
	$('orgSelector').setAttribute('targetId', element.up(".orgPicker").id);
	
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
}

function updateOrgBrowser(orgLists) {
	updateDropDown($('orgList'), orgLists.orgList, orgLists.orgId);
	updateDropDown($('customerList'), orgLists.customerList, orgLists.customerId);
	updateDropDown($('divisionList'), orgLists.divisionList, orgLists.divisionId);
}

function updateDropDown(select, newList, selectId) {
	while(select.options.length > 0) {
		select.options.length = 0;
	}
	var i = 0;
	newList.each(function (element) {
		select.options[i]= new Element("option", { value : element.id }).update(element.name);
			i++;
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
	var selectElement = element; 
	while(selectElement = selectElement.next('select')) {
		selectElement.options.length = 0;
	}
	getUpdatedOrgBrowser(element.getValue());
}

function setOwner(containerId, ownerId, ownerName) {
	var orgInputs = $$(containerId + " .orgSelected");
	orgInputs.first().value=ownerId;
	orgInputs.first().next('input').value=ownerName; 
	orgInputs.first().next('input').fire('field:change');
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
	if (divisions.getValue() != -1) {
		org.id = divisions.getValue();
		org.name = divisions.options[divisions.selectedIndex].text;
	} else if (customers.getValue() != -1) {
		org.id = customers.getValue();
		org.name = customers.options[customers.selectedIndex].text;
	} else {
		org.id = orgs.getValue();
		org.name = orgs.options[orgs.selectedIndex].text;
	}
	return org;
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