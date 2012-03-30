<#assign html>
	<#include "/templates/html/projects/_attachedNote.ftl"/>
</#assign>

closeNoteForm();
clearNoteForm();

if( $('noNotes').visible() ) {
	$('noNotes').hide();
	$('notes').show();
}

$( 'notes' ).insert( { top:'${html?js_string}' } );

if( $$( '#note_${note.id} .removeNoteLink' ).size()  > 0 ) {
	$$( '#note_${note.id} .removeNoteLink' )[0].observe( 'click', removeNote );
}

$( 'notesMore' ).show();

var notes = $$( '#notes .note' );
if( notes.size() > 5 ) {
	notes.last().remove();
	
}


${action.clearFlashScope()}