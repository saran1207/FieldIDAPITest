var editScheduleUrl = ''; 
var cancelScheduleUrl = '';
var removeScheduleUrl = '';
var removeWarning = '';

function editSchedule( inspectionTypeId, productTypeId, uniqueId ) {
	var url = editScheduleUrl + '?inspectionTypeId=' + inspectionTypeId + '&productTypeId=' + productTypeId;
	if( uniqueId != null ) { 
	 	url += '&uniqueID=' + uniqueId ;
	}
	new Ajax.Request( url, 
		{ method: "get", onComplete: function( transport ) { contentResponse( transport.responseText ) } } ); 
}

function saveSchedule( inspectionTypeId ) {
	$( 'schedule_' + inspectionTypeId ).request( { onComplete: function( transport ) { contentResponse( transport.responseText ) } } );
	
}

function cancelSchedule( inspectionTypeId, productTypeId, uniqueId ) {
	var url = cancelScheduleUrl + '?inspectionTypeId=' + inspectionTypeId + '&productTypeId=' + productTypeId ;
	if( uniqueId != null ) { 
	 	url += '&uniqueID=' + uniqueId ;
	}
	new Ajax.Request( url,
		{ method: "get", onComplete: function( transport ) { contentResponse( transport.responseText ) } } );  
	
}

function removeSchedule( inspectionTypeId, productTypeId, uniqueId, tryConfirm ) {
	var doRemove = true;
	
	if( tryConfirm && ( $('eventFrequencyOverrides_' + inspectionTypeId + '_container' ).getElementsByClassName( 'customerOverride' ).length != 0 )) {
		doRemove = confirm( removeWarning );
	}
	 
	if( doRemove ) {
		var url = removeScheduleUrl + '?inspectionTypeId=' + inspectionTypeId + '&productTypeId=' + productTypeId;
		if( uniqueId != null ) { 
		 	url += '&uniqueID=' + uniqueId ;
		}
		new Ajax.Request( url, 
			{ method: "get", onComplete: function( transport ) { contentResponse( transport.responseText ) } } );  
	}	
}

function expandOverride( inspectionTypeId ) {
	$('overrides_'+ inspectionTypeId).style.display="block";
	$('overrideExpand_'+ inspectionTypeId).style.display="none";
	$('overrideCollapse_'+ inspectionTypeId).style.display="inline";
	
}

function collapseOverride( inspectionTypeId ) {
	$('overrides_'+ inspectionTypeId).style.display="none";
	$('overrideExpand_'+ inspectionTypeId).style.display="inline";
	$('overrideCollapse_'+ inspectionTypeId).style.display="none";
	
}  