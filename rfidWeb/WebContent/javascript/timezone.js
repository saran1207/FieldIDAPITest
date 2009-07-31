var countryChangeUrl;

function countryChanged( countrySelect ) {
	if(countrySelect.options[countrySelect.selectedIndex].value != "") {
		var countryId = countrySelect.options[countrySelect.selectedIndex].value;
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
			timezoneList.options[i] = new Option(list[i].displayName, list[i].id);
		}
	}
}