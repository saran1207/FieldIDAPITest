var baseOrgUrl = "";

function showOrgSearch(event) {
	var element =  Event.element(event);
	event.stop();
	$('orgSearch').clonePosition(element.up(".orgPicker"), {setWidth:false, setHeight:false});
	$('orgSearch').style.position = "absolute";
	$('orgSearch').show();
	$('orgPickerResults').setAttribute('targetId', element.up(".orgPicker").id);
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
	
	var orgInputs = $$(containerId + " .orgSelected");
	orgInputs.first().value= element.getAttribute("org");
	orgInputs.first().next('input').value= element.getAttribute("orgName");
	orgInputs.first().next('input').fire('field:change');

	$('orgSearch').hide();
}

function clearOrgSearch(event) {
	var element =  Event.element(event);
	event.stop();
	element.up(".orgPicker").down(".orgSelected").first().value = "";
	element.up(".orgPicker").down(".orgSelected").first().next('input').value = "";
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