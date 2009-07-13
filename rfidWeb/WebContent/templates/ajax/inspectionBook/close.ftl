<#escape x as x?j_string >
<#assign html>
	<#include "/templates/html/inspectionBook/_openLink.ftl" >
</#assign>
	<#noescape>
		var messages = ${ json.toJSON( actionMessages ) };
		var errors = ${ json.toJSON( actionErrors ) };
	</#noescape>
	${ action.clearFlashMessages()! }${ action.clearFlashErrors()! }
	
	
	$( 'bookStatusLink_${book.id}' ).update( "${html}" );
	$( 'bookStatus_${book.id}' ).update( "${book.open?string( action.getText( "label.open" ), action.getText( "label.closed" ) ) }" );
	updateMessages( messages, errors );
</#escape>