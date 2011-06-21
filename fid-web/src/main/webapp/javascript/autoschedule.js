var updateAutoScheduleUrl='';

function updateOwner ( event ) {
	var ownerId = Event.element(event).getValue();
	if ( ownerId != "" ) {
		var url = updateAutoScheduleUrl + '?assetTypeId='+ $('assetType').value + "&ownerId=" + ownerId;
		if ($('identified') != null) {
			url += "&identified=" + encodeURIComponent($('identified').value);
		}
        if ($('eventTypeId') != null) {
            url += "&type=" + $('eventTypeId').value;
        }
		getResponse( url, "get" );
	} else {
		replaceEventSchedules( null );
	}
}

function updateIdentified ( event ) {
	var identified = Event.element(event).getValue();
	if ( identified != "" ) {
		var url = updateAutoScheduleUrl + '?assetTypeId='+ $('assetType').value + "&ownerId=" + $('ownerId').value + "&identified=" + encodeURIComponent(identified);
		getResponse( url, "get" );
	} else {
		replaceEventSchedules( null );
	}
}

Element.extend(document).observe("owner:change", updateOwner);

function replaceEventSchedules( assetEventSchedules ) {
	var eventScheduleList = $('schedulesForm');

	if (eventScheduleList != null) {

		if( assetEventSchedules == null || assetEventSchedules == "" ) {
			eventScheduleList.update();
		} else {
			eventScheduleList.replace( assetEventSchedules );
		}
		scheduleListUpdated();
	}
}