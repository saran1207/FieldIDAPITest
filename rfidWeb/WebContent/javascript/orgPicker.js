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
	
	$('orgSearch').hide();
}

document.observe("dom:loaded", function() {
	$$(".searchOwner").each(function(element) {
		element.observe('click', showOrgSearch);
	});
});