var editScheduleUrl = ''; 
var cancelScheduleUrl = '';
var removeScheduleUrl = '';
var removeWarning = '';

function editSchedule( inspectionTypeId, assetTypeId, uniqueId, ownerId ) {
	getResponse(editScheduleUrl,"get", makeInspectionFrequencyParams(inspectionTypeId, assetTypeId, uniqueId, ownerId)); 
}

function saveSchedule( inspectionTypeId ) {
	$( 'schedule_' + inspectionTypeId ).request(getStandardCallbacks());
}

function cancelSchedule( inspectionTypeId, assetTypeId, uniqueId ) {
	getResponse(cancelScheduleUrl, "get", makeInspectionFrequencyParams(inspectionTypeId, assetTypeId, uniqueId));  
}

function removeSchedule( inspectionTypeId, assetTypeId, uniqueId, tryConfirm ) {
	var doRemove = true;
	
	if( tryConfirm && ( $('eventFrequencyOverrides_' + inspectionTypeId + '_container' ).getElementsByClassName( 'customerOverride' ).length != 0 )) {
		doRemove = confirm( removeWarning );
	}
	 
	if (doRemove) {
		getResponse(removeScheduleUrl, "get", makeInspectionFrequencyParams(inspectionTypeId, assetTypeId, uniqueId));  
	}	
}

function makeInspectionFrequencyParams( inspectionTypeId, assetTypeId, uniqueId, ownerId ) {
	var params = new Object();
	params.inspectionTypeId= inspectionTypeId;
	params.assetTypeId= assetTypeId;
	if (uniqueId != null) { 
	 	params.uniqueID= uniqueId;
	}
	if (ownerId != null) { 
		params.ownerId= ownerId;
	}
	return params;
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