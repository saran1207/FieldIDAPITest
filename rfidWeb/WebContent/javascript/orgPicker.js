var baseOrgUrl = "";

function showOrgSearch(event) {
	var element =  Event.element(event);
	event.stop();
	$('orgSearch').clonePosition(element.up(".orgPicker"), {setWidth:false, setHeight:false});
	$('orgSearch').style.position = "absolute";
	$('orgSearch').show();
	
}

function updateOwner(event) {
	var element = Event.element(event);
	event.stop();
	
	$$(".orgSelected").first().value= element.getAttribute("org");

	$$(".orgSelected").first().next('input').value= element.getAttribute("orgName");
	
	$$(".orgSelected").first().next('input').fire('field:change');
	
	$('orgSearch').hide();
}

function clearOrgSearch(event) {
	var element =  Event.element(event);
	event.stop();
	$$(".orgSelected").first().value = "";
	$$(".orgSelected").first().next('input').value = "";
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