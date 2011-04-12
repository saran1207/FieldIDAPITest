var editScheduleUrl = ''; 
var cancelScheduleUrl = '';
var removeScheduleUrl = '';

function editSchedule( inspTypeId, assetId, uniqueId ) {
	hideScheduleActionLinks();
	var url = editScheduleUrl + '?type=' + inspTypeId + '&assetId=' + assetId +'&uniqueID=' + uniqueId;
	getResponse(url, "get"); 
}

function saveSchedule( scheudleId ) {
	showScheduleActionLinks();
	$( 'schedule_' + scheudleId ).request( { onComplete: function( transport ) { contentResponse( transport.responseText ) } } );
}

function cancelSchedule( inspTypeId, assetId, uniqueId ) {
	showScheduleActionLinks();
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


function hideScheduleActionLinks(){
	$('scheduleActions').hide();
}

function showScheduleActionLinks(){
	$('scheduleActions').show();
}

function doSubmit(event, uniqueId){
	if (event.keyCode == 13) { 
		saveSchedule( uniqueId );
		return false; 
	}
}