

	if( $( 'note_${note.id}' ) != null ) {
		$( 'note_${note.id}' ).remove();
	}
	
	<#if project.notes.size() gt 5 >
		<#assign note=(project.notes?reverse)[4] >
		<#assign html>
			<#include "/templates/html/projects/_attachedNote.ftl"/> 
		</#assign>
		$( 'notes' ).insert( { bottom:'${html?js_string}' } );
		$$( '#note_${note.id} .removeNoteLink' )[0].observe( 'click', removeNote );
		
				
	</#if>
	
	if( $$( '#notes div').size() == 0 ) {
		$('notes').hide();
		$('noNotes').show();
	}
	${action.clearFlashScope()}