var unitOfMeasureUrl = "";


function loadUnitOfMeasure(infoFieldId) {
	var selectUnit = $('unitOfMeasureId_'+infoFieldId);
	var unitId = null;
	var url = unitOfMeasureUrl + "?infoFieldId=" + infoFieldId;
	
	if( selectUnit != null ) {
		unitId = selectUnit.getValue();
		url += "&unitOfMeasureId=" + unitId ;
	}
	
	getResponse( url, "get" ); 	
}


function submitUnitOfMeasure( event ) {
	event.stop();
	
	var form = Event.element( event );
	
	form.request( { onComplete: contentCallback } );	
}

