var editScheduleUrl = ''; 
var cancelScheduleUrl = '';
var removeScheduleUrl = '';

function editSchedule( inspTypeId, productId, uniqueId ) {
	var url = editScheduleUrl + '?type=' + inspTypeId + '&productId=' + productId +'&uniqueID=' + uniqueId;
	getResponse(url, "get"); 
}

function saveSchedule( scheudleId ) {
	$( 'schedule_' + scheudleId ).request( { onComplete: function( transport ) { contentResponse( transport.responseText ) } } );
}

function cancelSchedule( inspTypeId, productId, uniqueId ) {
	var url = cancelScheduleUrl + '?type=' + inspTypeId + '&productId=' + productId +'&uniqueID=' + uniqueId;
	getResponse(url, "get");  
}

function removeSchedule( inspTypeId, productId, uniqueId ) {
	var url = removeScheduleUrl + '?type=' + inspTypeId + '&productId=' + productId +'&uniqueID=' + uniqueId ;
	
	getResponse(url, "get");  
}


function progressStopped(uniqueId) {
	$('inspectNow_' + uniqueId).show();
	$('stopProgress_' + uniqueId).hide();
}