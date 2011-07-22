var editNoteUrl = "";
var cancelNoteUrl = "";
var editPlanUrl = "";
var cancelPlanUrl = "";
var editNameUrl = "";
var cancelNameUrl = "";


function editNote( id ) {
	var params = new Object();
	params.id = id;
	getResponse(editNoteUrl, "get", params);

}

function updateNote() {
	$('updateNoteButton').disable();
	$('noteFormLoading').style.visibility = "visible"; 
	$( 'noteForm' ).request(getStandardCallbacks());
}

function cancelNote( id ) {
	var params = new Object();
	params.id = id;
	getResponse(cancelNoteUrl, "get", params);

}

function editPlan( id ) {
	var params = new Object();
	params.id = id;
	getResponse(editPlanUrl, "get", params);
}

function updatePlan() {
	$('updatePlanButton').disable();
	$('planFormLoading').style.visibility = "visible"; 
	$( 'planForm' ).request(getStandardCallbacks());
}

function cancelPlan( id ) {
	$('updatePlanButton').disable();
	$('planFormLoading').style.visibility = "visible"; 
	var params = new Object();
	params.id = id;
	getResponse(cancelPlanUrl, "get", params);

}

function saveExtendedFeature( feature ) {
	$( 'loading_' + feature ).style.visibility = "visible"; 
	$( 'extendedFeature_' + feature ).request(getStandardCallbacks());
}

function savePlansAndPricing() {
	$( 'loading_plansAndPricing' ).style.visibility = "visible"; 
	$( 'plansAndPricingForm' ).request(getStandardCallbacks());
}

function saveSecondaryOrgs() {
	$( 'loading_secondaryOrgs' ).style.visibility = "visible"; 
	$( 'secondaryOrgsForm' ).request(getStandardCallbacks());
}

function updateStatus() {
	$( 'statusForm' ).request(getStandardCallbacks());
}

function editName( id ) {
	var params = new Object();
	params.id = id;
	getResponse(editNameUrl, "get", params);
}

function cancelName( id ) {
	var params = new Object();
	params.id = id;
	getResponse(cancelNameUrl, "get", params);
}

function updateName() {
	$( 'nameForm' ).request(getStandardCallbacks());
}

function doSubmit(event) { 
	if (event.keyCode == 13) { 
		updateName();
		return false; 
	}
}
