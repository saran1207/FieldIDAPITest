// this is used to track how many sections and criteria we have per section.
// each element is a number representing the number of criteria in that section 
// (where the section is index of the element) 
var sectionIndexes = new Array();

var addSectionUrl = '';
function addSection(){
	getResponse( addSectionUrl + '&sectionIndex='+ sectionIndexes.length );
	sectionIndexes.push( new Array() );
}

var addCriteriaUrl = '';
function addCriteria( section ){
	getResponse(addCriteriaUrl + '&sectionIndex='+ section + '&criteriaIndex=' + sectionIndexes[section].length);
	
	var recDefIndexes = new Array();
	recDefIndexes['RECOMMENDATION'] = 0;
	recDefIndexes['DEFICIENCY'] = 0;
	
	sectionIndexes[section].push(recDefIndexes);
	
	if ( !$("sectionContainer_" + section).visible() ) {
		openSection("sectionContainer_" + section, "expandSection_" + section, "collapseSection_" + section);
	}
}

var addObservationUrl = '';
function addObservation(type, section, criteria) {
	getResponse(addObservationUrl + '&sectionIndex=' + section + '&criteriaIndex=' + criteria + '&observationIndex=' + sectionIndexes[section][criteria][type] + '&observationType=' + type);
	sectionIndexes[section][criteria][type]++;
}

function retireRecommendation(sectionIndex, criteriaIndex, recommendationIndex) {
	var id = 'recommendation_' + sectionIndex + '_' + criteriaIndex + '_' + recommendationIndex;
	retireObservation(id);
	
}


function retireDeficiency(sectionIndex, criteriaIndex, deficiencyIndex ) {
	var id = 'deficiency_' + sectionIndex + '_' + criteriaIndex + '_' + deficiencyIndex;
	retireObservation(id);
}

function retireObservation(id) {
	fade(id);
	var element = $$("#" + id + " input").first();
	element.value = "--deleted--"  + element.getValue();
}

function fadeAndRemove(elementId) {
	Effect.Fade(elementId, {
		afterFinish: function() {
			$(elementId).remove(); 
		}
	});
}

function fade(elementId) {
	Effect.Fade(elementId);
}


function retireCriteria( sectionIndex, criteriaIndex ) {	
	$( 'retire_' + sectionIndex + '_' + criteriaIndex ).value="true";
	
	Effect.Fade( 'criteriaHolder_' + sectionIndex + '_' + criteriaIndex );
	if( $( 'id_' + sectionIndex + '_' + criteriaIndex ).value == "0" ) {
		$( 'criteriaHolder_' + sectionIndex + '_' + criteriaIndex ).remove();
	}
}

function retireSection( sectionIndex ) {
	$( 'retire_' + sectionIndex ).value="true";
	
	Effect.BlindUp( 'criteriaSection_' + sectionIndex, {duration: 0.75} );
	if( $( 'id_' + sectionIndex ).value == "0" ) {
		$( 'criteriaSection_' + sectionIndex ).remove();
	}
}

var inspectionFormChangedMessage = '';
var formChanged = false;

/**
 * Checks to see if the form has changed and display a conformation message (inspectionFormChangedMessage)
 * if it has
 */
function hasFormChanged() {
	if( formChanged == true ) {
		return confirm( inspectionFormChangedMessage );
	}

	return true;
}

/**
 * Marks the form as having changed
 */
function changeToForm() {
	formChanged = true;
}

/**
 * Given the id of a state set, adds or removes a highlight class to the Element.
 */
function toggleStateSetHighlight(id) {
	var highlightClass = "stateSetHighlight"
	var stateSet = $('stateSet_' + id);
	
	if(!stateSet.hasClassName(highlightClass)) {
		stateSet.addClassName(highlightClass);
	} else {
		stateSet.removeClassName(highlightClass);
	}
}

/**
 * warns the user about making changes to this form
 */
function warnFormChange() {
	Lightview.show({ 
		href:"#formChangewarning", 
		title: "",
		options:{
			autosize: true,
		 	overlay: {close: true},
		 	menubar: false
		} 
	});
}
