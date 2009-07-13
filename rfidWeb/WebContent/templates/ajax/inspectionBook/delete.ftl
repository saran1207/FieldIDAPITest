<#escape x as x?j_string >
	<#noescape>
		var messages = ${ json.toJSON( actionMessages ) };
		var errors = ${ json.toJSON( actionErrors ) };
	</#noescape>
	${ action.clearFlashMessages()! }${ action.clearFlashErrors()! }
	$( "book_${book.id}" ).remove();	
	
	
	updateMessages( messages, errors );
</#escape>