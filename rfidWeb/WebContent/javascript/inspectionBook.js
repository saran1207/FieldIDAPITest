function updateInspectionBookList( list ) {
	var books = $('inspectionBooks');
	var blankOption = books.options[0];
		
	books.options.length = 0;
	books.options[0] = blankOption;
	if( list != null ) {
		for( var i = 0; i < list.length; i++ ) {
			books.options[i+1] = new Option( list[i].name, list[i].id );
		}
	}
	
}

var updateInspectionBooksUrl = '';

function updateInspectionBooks( customerSelect, includeClosed ) {
	
	var customerId = $(customerSelect).getValue();
	var url =  updateInspectionBooksUrl + '?customerId='+ customerId + '&withClosed=' + includeClosed;

	getResponse( url );		

}