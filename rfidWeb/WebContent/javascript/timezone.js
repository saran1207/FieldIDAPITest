var countryChangeUrl;

function countryChanged(event) {
	event.stop();
	var countrySelect = Event.element(event);
	
	if(countrySelect.getValue() != "") {
		var countryId = countrySelect.getValue();
		var url =  countryChangeUrl + '?countryId='+ countryId;

		new Ajax.Request(url, {
			method: 'get',
			onSuccess: function( transport ) { contentResponse( transport.responseText ); } } );		

	} else {
		updateTimezoneList( null ); 
	}
}

function updateTimezoneList(list) {
	var timezoneList = $('tzlist');
	timezoneList.options.length = 0;
	if( list != null ) {
		for( var i = 0; i < list.length; i++ ) {
			timezoneList.options[i] = new Option(list[i].name, list[i].timeZoneId);
		}
	}
}

Element.extend(document).observe("dom:loaded", function() {
	$$(".changesTimeZone").each(function(element) {
		element.observe("change", countryChanged);
	});

});