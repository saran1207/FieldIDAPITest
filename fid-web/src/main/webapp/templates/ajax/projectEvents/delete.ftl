	if( $( 'event_${uniqueID}' ) != null ) {
		$( 'event_${uniqueID}' ).remove();
	}


if( $('linkedEvents') != null ) {
	<#if page?exists && page.list.size() gt 4 >
		<#assign event=page.list[4] >
		<#assign html>
			<#include "/templates/html/projects/_attachedEvents.ftl"/> 
		</#assign>
		$( 'linkedEvents' ).insert( { bottom:'${html?js_string}' } );
	</#if>
	if( $$( '#linkedEvents div').size() == 0 ) {
		$('linkedEvents').hide();
		$('emptyEventList').show();
	}
}
if ($('incompleteEventCount') != null) {
	$('incompleteEventCount').update('${action.getText("label.incompleteevents", "", countOfIncompleteSchedules.toString())?html}');
	$('completeEventCount').update('${action.getText("label.completeevents", "", countOfCompleteSchedules.toString())?html}');
}


