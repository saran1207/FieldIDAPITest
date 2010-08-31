<#escape x as x?j_string >
	<#noescape>
		var messages = ${ json.toJson( actionMessages ) };
		var errors = ${ json.toJson( actionErrors ) };
	</#noescape>
	${ action.clearFlashMessages()! }${ action.clearFlashErrors()! }
	$( "book_${book.id}" ).remove();	
	
	
	updateMessages( messages, errors );
</#escape>