var countryChangeUrl;

function countryChanged(event) {
	event.stop();
	var countrySelect = Event.element(event);
	
	if(countrySelect.getValue() != "") {
		var countryId = countrySelect.getValue();
		var url =  countryChangeUrl + '?countryId='+ countryId;

		getResponse(url, "get");
	} else {
		updateTimezoneList( null ); 
	}
}

function updateTimezoneList(list) {
	var timezoneList = $('tzlist');
	timezoneList.options.length = 0;
	if( list != null ) {
		for( var i = 0; i < list.length; i++ ) {
			timezoneList.options[i] = new Option(list[i].name, list[i].name);
		}
	}
}

Element.extend(document).observe("dom:loaded", function() {
	$$(".changesTimeZone").each(function(element) {
		element.observe("change", countryChanged);
	});

});