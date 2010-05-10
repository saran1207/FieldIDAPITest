var buttonStates = new Object();
			 
function changeButton( id ) {
	var image = $( "criteriaImage_" + id );
	var text = $( "criteriaText_" + id );
	var state = $( "criteriaResultState_" + id );
	
	criteria = buttonStates[ "criteria_" + id ];
	var nextState = null;
	
	for( var i = 0; i < criteria.length; i++ ) {
		
		if( state.value == criteria[i].stateId ) {
			if( i + 1 < criteria.length ) {
				nextState = criteria[i+1]
			} else {
				nextState = criteria[0];
			}
		}
	}			 
	
	if( nextState == null  ) {
		nextState = criteria[0];
	}
	state.value = nextState.stateId;
	text.innerHTML = nextState.stateText;
	image.src = nextState.stateButton;
}

function cycleRecommendation(criteriaId, index) {
	
	var observation = $('rec_' + criteriaId + "_" + index);
	
	var observationId = $('rec_' + criteriaId + "_" + index + '_id');
	var observationText = $('rec_' + criteriaId + "_" + index + '_text');
	var observationState = $('rec_' + criteriaId + "_" + index + '_state');
	
	var outstandingState="OUTSTANDING";
	var notselectedState="NOTSELECTED";
	
	var outstandingClass = "recSelected";
	
	// recomendations only have 2 states (selected and notselected).
	switch(observationState.value) {
		case notselectedState:
			// if observation is not selected, move to OUTSTANDING
			observation.toggleClassName(outstandingClass);
			
			if (observationId != null) {
				observationId.enable();
			}
			observationText.enable();
			observationState.enable();
			observationState.value=outstandingState;
			
			// update the rec count for this criteria
			recCounts[criteriaId]++;
			
			break;
		case outstandingState:
			// if recommendation is selected, move to NOTSELECTED
			observation.toggleClassName(outstandingClass);

			if (observationId != null) {
				observationId.disable();
			}
			observationText.disable();
			observationState.disable();
			observationState.value=notselectedState;
			
			// update the rec count for this criteria
			recCounts[criteriaId]--;
			
			break;
	}
	
	// update the rec image
	updateRecommendationImage(criteriaId);
}

function cycleDeficiency(criteriaId, index) {
	
	var observation = $('def_' + criteriaId + "_" + index);
	
	var observationId = $('def_' + criteriaId + "_" + index + '_id');
	var observationText = $('def_' + criteriaId + "_" + index + '_text');
	var observationState = $('def_' + criteriaId + "_" + index + '_state');
	
	var outstandingState="OUTSTANDING";
	var repairedonsiteState="REPAIREDONSITE";
	var repairedState="REPAIRED";
	var notselectedState="NOTSELECTED";
	
	var outstandingClass = "defSelected";
	var repairedOnSiteClass = "defRepairedOnSite";
	var repairedClass = "defRepaired";
	
	// Observations are now always available and have the same set 
	// of states between add/edit.  States move as follows:
	// NOTSELECTED -> OUTSTANDING -> REPAIREDONSITE -> REPAIRED
	switch(observationState.value) {
		case notselectedState:
			// if observation is NOTSELECTED, move to OUTSTANDING
			observation.toggleClassName(outstandingClass);
		
			if (observationId != null) {
				observationId.enable();
			}	
			observationText.enable();
			observationState.enable();
			observationState.value=outstandingState;
			
			// update the def count for this criteria
			defCounts[criteriaId]++;
			
			break;
		case outstandingState:
			// if observation is OUTSTANDING, move to REPAIREDONSITE
			observation.toggleClassName(outstandingClass);
			observation.toggleClassName(repairedOnSiteClass);
			observationState.value=repairedonsiteState;
			
			break;
		case repairedonsiteState:
			// if observation is REPAIREDONSITE, move to REPAIRED
			observation.toggleClassName(repairedOnSiteClass);
			observation.toggleClassName(repairedClass);
			observationState.value=repairedState;
			
			break;
		case repairedState:
			// if observation is REPAIRED, move to NOTSELECTED
			observation.toggleClassName(repairedClass);
			
			if (observationId != null) {
				observationId.disable();
			}	
			observationText.disable();
			observationState.disable();
			observationState.value=notselectedState;
			
			// update the def count for this criteria
			defCounts[criteriaId]--;
			
			break;
	}
	
	updateDeficiencyImage(criteriaId);
}



function showObservations( elementId, event) {
	
	if( event != undefined  ) {
		event.clickid = elementId
	}
	
	var observationBox = $(elementId);
	var button = $(elementId + "_button");
	
	observationBox.show();
	observationBox.absolutize();
	
	
	var position = button.positionedOffset();
	
	observationBox.setStyle({
		top: position['top'] + "px",
		left: (position['left'] + 25) + "px"
	});
	
	var viewPort = observationBox.viewportOffset();
	var boxTopRightCorner = observationBox.getWidth() + observationBox.viewportOffset().left;
	if( boxTopRightCorner >  document.viewport.getWidth() ) {
		var offset = boxTopRightCorner - document.viewport.getWidth();
		observationBox.setStyle({
			left: (position['left'] - offset) + "px"
		});
	}
	
	observationBox.fx = function(event) {
		
		var clickid;
		
		if( event.clickid != undefined ) {
			clickid = event.clickid;
		} else {
			var element = Event.element( event );
			if( element.id != undefined && 
				( element.id == elementId+ "_img" || element.id == elementId + "_button" ) ) {
				clickid = elementId;
			}
		}
		
		
		if(  clickid  != elementId  ) {
			hideObservations( elementId );
		}
	} 
	observationBox.bfx = observationBox.fx.bindAsEventListener( observationBox ) ;
	Element.extend( document ).observe( 'click', observationBox.bfx );
}

function hideObservations(elementId) {
	
	$(elementId).hide();
	
	Element.extend( document ).stopObserving( 'click', $(elementId).bfx );
}

var currentSectionId = 0;
var sectionCount = 0;

function checkProofTestType( proofTestType ) {
	var value = $( proofTestType ).getValue();
	if( proofTestTypes[ value ] ) {
		if( !$( 'proofTestUpload' ).visible() ) {
			Effect.Fade( 'proofTestManual', { duration: 0.75, afterFinish: function() { Effect.Appear( 'proofTestUpload', { duration: 0.75 } ); } } );
		}
	} else {
		if( !$( 'proofTestManual' ).visible() ) {
			Effect.Fade( 'proofTestUpload', { duration: 0.75, afterFinish: function() { Effect.Appear( 'proofTestManual', { duration: 0.75 } ); } } );
		}
	}
}

function repositionCertLinks(list, link) {
	var list =  $(list);
	var link =  $(link);
	translate(list, $(link), link.getHeight(), (link.getWidth()) - list.getWidth());
}

function positionDropdown( a ) {
	var list = $(a.id + "_list");
	translate(list, a, 14, -30);
}
	
function jumpSelectToSection( event ) {
	if( event ) {
		event.stop();
		id = Event.element( event ).selectedIndex;
		$(Event.element( event ).id).sectionRotator.jumpToSection( id - 1 );
	}
}

function jumpLinkToSection( event ) {
	if( event ) {
		
		var element = $(this.id);
		id = element.readAttribute( 'selectedIndex' );
		id = parseInt(id);
		element.sectionRotator.jumpToSection( id );
		event.stop();
	}
}
	
		
function jumpToSection( id ) {
	
	if( this.currentSectionId == id ) { return; }
	
	if( id >= this.sectionCount ) { id = 0; }
	
	if( id < 0 ) { id = this.sectionCount - 1; }
	
	var currentSection = $( 'section_' + this.identifier + "_" + this.currentSectionId );
	var nextSection = $( 'section_' + this.identifier + "_" + id );
	
	if( currentSection != null ) {
		currentSection.hide();
	}
	
	nextSection.show();
	this.currentSectionId = id; 
}


function SectionRotator( sectionCount, currentSectionId,  identifier ) {
	this.currentSectionId = currentSectionId;
	this.sectionCount = sectionCount;
	this.identifier = identifier;
	this.jumpToSection = jumpToSection;
}

var recCounts = new Array();
var defCounts = new Array();

var recImageUrl = "";
var defImageUrl = "";
var recSelectImageUrl = "";
var defSelectImageUrl = "";

function updateRecommendationImage(criteriaId) {
	var recImage = $('recImage_' + criteriaId);
	
	if (recCounts[criteriaId] > 0) {
		recImage.src = recSelectImageUrl;
	} else {
		recImage.src = recImageUrl;
	}
}

function updateDeficiencyImage(criteriaId) {
	var defImage = $('defImage_' + criteriaId);
	
	if (defCounts[criteriaId] > 0) {
		defImage.src = defSelectImageUrl;
	} else {
		defImage.src = defImageUrl;
	}
}

var currentRecIsEmpty;
var currentDefIsEmpty;

function captureRecCommentState(observationText) {
	if (observationText.length > 0) {
		currentRecIsEmpty = false;
	} else {
		currentRecIsEmpty = true;
	}
}

function captureDefCommentState(observationText) {
	if (observationText.length > 0) {
		currentDefIsEmpty = false;
	} else {
		currentDefIsEmpty = true;
	}
}

function checkRecComment(criteriaId, observationText) {
	if (observationText.length > 0) {
		if (currentRecIsEmpty) {
			recCounts[criteriaId]++;
		}
	} else {
		if (!currentRecIsEmpty) {
			recCounts[criteriaId]--;
		}
	}
	
	updateRecommendationImage(criteriaId);
}

function checkDefComment(criteriaId, observationText) {
	if (observationText.length > 0) {
		if (currentDefIsEmpty) {
			defCounts[criteriaId]++;
		}
	} else {
		if (!currentDefIsEmpty) {
			defCounts[criteriaId]--;
		}
	}

	updateDeficiencyImage(criteriaId);
}


function changeToNewInspectionBook() {
	$('inspectionBookSelect').hide();
	$('inspectionBooks').disable();
	$('inspectionBookTitle').show();
	$('newInspectionBook').enable();
	$('newInspectionBook').select();
}

function changeToInspectionBookSelect() {
	$('inspectionBookSelect').show();
	$('inspectionBooks').enable();
	$('inspectionBookTitle').hide();
	$('newInspectionBook').disable();
	$('inspectionBooks').focus();
}

var index;
var addScheduleUrl;
var autoSuggestUrl;
var dateErrorText;
var inspectionTypeId;
var productId;

function addSchedule() {
	var types = $('nextInspectionTypeSelection');
	var jobs = $('jobSelection');
	var nextDate = $('nextDate');
	
	
	var params = new Object();
	params.date =  nextDate.getValue();
	params.inspectionTypeId = types.options[types.selectedIndex].value;
	params.index = index;
	
	if (jobs != null) {
		params.jobId = jobs.options[jobs.selectedIndex].value;
	}
	
	getResponse(addScheduleUrl, "post", params);
	
	return false;
}

function removeSchedule(idx) {
	$('schedule_' + idx).remove();
	scheduleListUpdated();
	return false;
}

function validDate(date) {
	return !(date.trim() == "");	
}

function autoSuggest() {
	var params = new Object();
	params.inspectionTypeId = inspectionTypeId;
	params.inspectionDate = $('inspectionDate').getValue();
	params.product = productId;
	params.index = index;
	
	getResponse(autoSuggestUrl, "post", params);
	
	return false;
}

function updateAutoSuggest() {
	var autoSuggestedSchedules = $$('.autoSuggested');
	
	/*
	 * if there is no auto suggested schedule, or the user has removed it, 
	 * there's no need for update.
	 */
	if (autoSuggestedSchedules.size() == 0) {
		return;
	}
	
	autoSuggestedSchedules.each(function(item) {
		item.remove();
	});
	
	autoSuggest();
}

function scheduleListUpdated() {
	if ($('schedules').empty()) {
		$('schedules').hide();
		$('emptySchedules').show();
	} else {
		$('schedules').show();
		$('emptySchedules').hide();
	}
}