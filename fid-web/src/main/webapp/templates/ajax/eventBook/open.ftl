<#escape x as x?j_string >
<#assign html>
	<#include "/templates/html/eventBook/_closeLink.ftl" >
</#assign>
	<#noescape>
		var messages = ${ json.toJson( actionMessages ) };
		var errors = ${ json.toJson( actionErrors ) };
	</#noescape>
	${ action.clearFlashMessages()! }${ action.clearFlashErrors()! }
	
	
	$( 'bookStatusLink_${book.id}' ).update( "${html}" );
	$( 'bookStatus_${book.id}' ).update( "${book.open?string( action.getText( "label.open" ), action.getText( "label.closed" ) ) }" );
	updateMessages( messages, errors );

</#escape>