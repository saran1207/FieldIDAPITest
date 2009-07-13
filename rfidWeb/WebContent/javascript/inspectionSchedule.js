var editScheduleUrl = ''; 
var cancelScheduleUrl = '';
var removeScheduleUrl = '';

function editSchedule( inspTypeId, productId, uniqueId ) {
	var url = editScheduleUrl + '?type=' + inspTypeId + '&productId=' + productId +'&uniqueID=' + uniqueId;
	new Ajax.Request( url, 
		{ method: "get", onComplete: function( transport ) { contentResponse( transport.responseText ) } } ); 
}

function saveSchedule( scheudleId ) {
	$( 'schedule_' + scheudleId ).request( { onComplete: function( transport ) { contentResponse( transport.responseText ) } } );
}

function cancelSchedule( inspTypeId, productId, uniqueId ) {
	new Ajax.Request( cancelScheduleUrl + '?type=' + inspTypeId + '&productId=' + productId +'&uniqueID=' + uniqueId ,
		{ method: "get", onComplete: function( transport ) { contentResponse( transport.responseText ) } } );  
}

function removeSchedule( inspTypeId, productId, uniqueId ) {
	var url = removeScheduleUrl + '?type=' + inspTypeId + '&productId=' + productId +'&uniqueID=' + uniqueId ;
	
	new Ajax.Request( url, 
		{ method: "get", onComplete: function( transport ) { contentResponse( transport.responseText ) } } );  
}


function progressStopped(uniqueId) {
	$('inspectNow_' + uniqueId).show();
	$('stopProgress_' + uniqueId).hide();
}