var updateOwnerUrl= '';

function updateOwner ( event ) {
	var ownerId = Event.element(event).getValue();
	if ( ownerId != "" ) {
		var url = updateOwnerUrl + '?assetTypeId='+ $('assetType').value + "&ownerId=" + ownerId;
        if ($('eventTypeId') != null) {
            url += "&type=" + $('eventTypeId').value;
        }
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