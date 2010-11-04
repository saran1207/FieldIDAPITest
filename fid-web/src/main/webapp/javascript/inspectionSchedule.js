var editScheduleUrl = ''; 
var cancelScheduleUrl = '';
var removeScheduleUrl = '';

function editSchedule( inspTypeId, assetId, uniqueId ) {
	var url = editScheduleUrl + '?type=' + inspTypeId + '&assetId=' + assetId +'&uniqueID=' + uniqueId;
	getResponse(url, "get"); 
}

function saveSchedule( scheudleId ) {
	$( 'schedule_' + scheudleId ).request( { onComplete: function( transport ) { contentResponse( transport.responseText ) } } );
}

function cancelSchedule( inspTypeId, assetId, uniqueId ) {
	var url = cancelScheduleUrl + '?type=' + inspTypeId + '&assetId=' + assetId +'&uniqueID=' + uniqueId;
	getResponse(url, "get");  
}

function removeSchedule( inspTypeId, assetId, uniqueId ) {
	var url = removeScheduleUrl + '?type=' + inspTypeId + '&assetId=' + assetId +'&uniqueID=' + uniqueId ;
	
	getResponse(url, "get");  
}


function progressStopped(uniqueId) {
	$('startEventNow_' + uniqueId).show();
	$('stopProgress_' + uniqueId).hide();
}