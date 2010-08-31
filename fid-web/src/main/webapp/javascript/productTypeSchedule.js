var editScheduleUrl = ''; 
var cancelScheduleUrl = '';
var removeScheduleUrl = '';
var removeWarning = '';

function editSchedule( inspectionTypeId, productTypeId, uniqueId, ownerId ) {
	getResponse(editScheduleUrl,"get", makeInspectionFrequencyParams(inspectionTypeId, productTypeId, uniqueId, ownerId)); 
}

function saveSchedule( inspectionTypeId ) {
	$( 'schedule_' + inspectionTypeId ).request(getStandardCallbacks());
}

function cancelSchedule( inspectionTypeId, productTypeId, uniqueId ) {
	getResponse(cancelScheduleUrl, "get", makeInspectionFrequencyParams(inspectionTypeId, productTypeId, uniqueId));  
}

function removeSchedule( inspectionTypeId, productTypeId, uniqueId, tryConfirm ) {
	var doRemove = true;
	
	if( tryConfirm && ( $('eventFrequencyOverrides_' + inspectionTypeId + '_container' ).getElementsByClassName( 'customerOverride' ).length != 0 )) {
		doRemove = confirm( removeWarning );
	}
	 
	if (doRemove) {
		getResponse(removeScheduleUrl, "get", makeInspectionFrequencyParams(inspectionTypeId, productTypeId, uniqueId));  
	}	
}

function makeInspectionFrequencyParams( inspectionTypeId, productTypeId, uniqueId, ownerId ) {
	var params = new Object();
	params.inspectionTypeId= inspectionTypeId;
	params.productTypeId= productTypeId;
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