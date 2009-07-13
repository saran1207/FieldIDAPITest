	if( $( 'event_${uniqueID}' ) != null ) {
		$( 'event_${uniqueID}' ).highlight();
		$( 'event_${uniqueID}' ).remove();
	}


if( $('linkedEvents') != null ) {
	<#if page?exists && page.list.size() gt 4 >
		<#assign event=page.list[4] >
		<#assign html>
			<#include "/templates/html/projects/_attachedEvents.ftl"/> 
		</#assign>
		$( 'linkedEvents' ).insert( { bottom:'${html?js_string}' } );
		$( 'event_${event.id}' ).highlight();
	</#if>
	if( $$( '#linkedEvents div').size() == 0 ) {
		$('linkedEvents').hide();
		$('emptyEventList').show();
		$('emptyEventList').highlight();
	}
}
if ($('incompleteInspectionCount') != null) {
	$('incompleteInspectionCount').update('${action.getText("label.incompleteevents", "", countOfIncompleteSchedules.toString())?html}');
	$('completeInspectionCount').update('${action.getText("label.completeevents", "", countOfCompleteSchedules.toString())?html}');
}


