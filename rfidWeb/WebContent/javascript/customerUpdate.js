function customerChanged( customerSelect ) {
	if( customerSelect.options[ customerSelect.selectedIndex ].value != "" ) {
		var customerId = customerSelect.options[ customerSelect.selectedIndex ].value;
		var url =  customerChangeUrl + '?customerId='+ customerId;

		new Ajax.Request(url, {
			method: 'get',
			onSuccess: function( transport ) { contentResponse( transport.responseText ); } } );		

	} else {
		// clear the list.
		updateDivisionList( null ); 
	}
}

function updateDivisionList( list ) {
	var divisionList = $('division');
	var blankOption = divisionList.options[0]; 
	divisionList.options.length = 0;
	divisionList.options[0] = blankOption;
	if( list != null ) {
		for( var i = 0; i < list.length; i++ ) {
			divisionList.options[i+1] = new Option( list[i].displayName, list[i].id );
		}
	}
}
