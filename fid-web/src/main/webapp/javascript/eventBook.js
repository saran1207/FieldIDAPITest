function updateEventBookList( list ) {
	var books = $('eventBooks');
	var blankOption = books.options[0];
		
	books.options.length = 0;
	books.options[0] = blankOption;
	if( list != null ) {
		for( var i = 0; i < list.length; i++ ) {
			books.options[i+1] = new Option( list[i].name, list[i].id );
		}
	}
	
}

var updateEventBooksUrl = '';

function updateEventBooks(event) {
	
	var ownerId = Event.element(event).getValue();
	var url =  updateEventBooksUrl + '?ownerId='+ ownerId + '&withClosed=false';

	getResponse( url );		

}

Element.extend(document).observe("owner:change", updateEventBooks);

