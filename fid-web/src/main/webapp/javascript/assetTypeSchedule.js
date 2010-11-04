var editScheduleUrl = ''; 
var cancelScheduleUrl = '';
var removeScheduleUrl = '';
var removeWarning = '';

function editSchedule( eventTypeId, assetTypeId, uniqueId, ownerId ) {
	getResponse(editScheduleUrl,"get", makeEventFrequencyParams(eventTypeId, assetTypeId, uniqueId, ownerId)); 
}

function saveSchedule( eventTypeId ) {
	$( 'schedule_' + eventTypeId ).request(getStandardCallbacks());
}

function cancelSchedule( eventTypeId, assetTypeId, uniqueId ) {
	getResponse(cancelScheduleUrl, "get", makeEventFrequencyParams(eventTypeId, assetTypeId, uniqueId));
}

function removeSchedule( eventTypeId, assetTypeId, uniqueId, tryConfirm ) {
	var doRemove = true;
	
	if( tryConfirm && ( $('eventFrequencyOverrides_' + eventTypeId + '_container' ).getElementsByClassName( 'customerOverride' ).length != 0 )) {
		doRemove = confirm( removeWarning );
	}
	 
	if (doRemove) {
		getResponse(removeScheduleUrl, "get", makeEventFrequencyParams(eventTypeId, assetTypeId, uniqueId));
	}	
}

function makeEventFrequencyParams( eventTypeId, assetTypeId, uniqueId, ownerId ) {
	var params = new Object();
	params.eventTypeId= eventTypeId;
	params.assetTypeId= assetTypeId;
	if (uniqueId != null) { 
	 	params.uniqueID= uniqueId;
	}
	if (ownerId != null) { 
		params.ownerId= ownerId;
	}
	return params;
}

function expandOverride( eventTypeId ) {
	$('overrides_'+ eventTypeId).style.display="block";
	$('overrideExpand_'+ eventTypeId).style.display="none";
	$('overrideCollapse_'+ eventTypeId).style.display="inline";
	
}

function collapseOverride( eventTypeId ) {
	$('overrides_'+ eventTypeId).style.display="none";
	$('overrideExpand_'+ eventTypeId).style.display="inline";
	$('overrideCollapse_'+ eventTypeId).style.display="none";
	
}  