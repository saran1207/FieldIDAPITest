var maxNumberOfImages = 0;
var undoButtonGroupUrl = "";
var buttonsUpdated = new Object();
var changesWarning = "";

function confirmNoChanges() {
	var changesNotSaved = false;
	for ( states in buttonsUpdated) {
		if( buttonsUpdated[states] == true ) {
			changesNotSaved = true;
		}
	}
	if( changesNotSaved == true && confirm( changesWarning ) ) {
		saveAllChanges();
	} 
}

function saveAllChanges(){
	numberOfButtonGroups=$$('form.stateSetForm').size();
	for(i=0; i < numberOfButtonGroups; i++){
		if(buttonsUpdated[ 'state_' + i ] == true)
		saveButtonGroup(i);
	}
	return true;
}

function changeButtonImage( link ) {
	var hidden = link.getElementsByTagName( 'input' )[0];
	var imageNumber = parseInt( hidden.value.slice(3) );
	var img = link.getElementsByTagName( 'img' )[0]; 
	
	// increase the number;
	imageNumber++;
	if( imageNumber >= maxNumberOfImages ) {
		imageNumber = 0;
	}
	var newImageName = imageNumber.toString();
	newImageName = 'btn' + newImageName;
	img.src = img.src.replace( hidden.value, newImageName );
	hidden.value = newImageName
}

function saveButtonGroup( stateSetId ) {
	var form = $('stateSet_' + stateSetId +'_form');
	form.request( { onSuccess: contentCallback } );
}

function undoButtonGroup( stateSetId, stateSetIndex ) {
	if( stateSetId != undefined && stateSetId != null ) {
		getResponse( undoButtonGroupUrl + "?uniqueID=" + stateSetId + "&buttonGroupIndex=" + stateSetIndex);
	} else {
		
		var stateSet = $( 'stateRow_' + stateSetIndex ).remove();
		
	}
	buttonsUpdated[ 'state_' + stateSetIndex ] = false;
}

function enableGroupSaveButton( stateSetId ) {
    $('buttons_' + stateSetId).show();
	buttonsUpdated[ 'state_' + stateSetId ] = true;
}


function enableButton( buttonGroup, id ) {
	var button = $('buttonState__' + buttonGroup + '_' + id);
	var selects = button.getElementsByTagName( 'select' );
	var inputs = button.getElementsByTagName( 'input' );
	var image = button.getElementsByTagName( 'img' )[0];
	for( var i =0 ; i < selects.length; i++ ) {
		selects[i].disabled= false;
	}
	for( var i =0 ; i < inputs.length; i++ ) {
		inputs[i].disabled= false;
	}
	
	image.style.visibility = "visible";
	
	var add_button = $('buttonState__' + buttonGroup + '_' + id + '_add');
	var remove_button = $('buttonState__' + buttonGroup + '_' + id + '_remove');
	
	remove_button.show()
	add_button.hide()
}


function disableButton( buttonGroup, id ) {
	var button = $('buttonState__'+ buttonGroup + '_' + id);
	var selects = button.getElementsByTagName( 'select' );
	var inputs = button.getElementsByTagName( 'input' );
	var image = button.getElementsByTagName( 'img' )[0];
	var add_button = $('buttonState__' + buttonGroup + '_'+ id + '_add');
	var remove_button = $('buttonState__' + buttonGroup + '_' + id + '_remove');
	
	for( var i =0 ; i < selects.length; i++ ) {
		selects[i].disabled= true;
	}
	for( var i =0 ; i < inputs.length; i++ ) {
		inputs[i].disabled= true;
	}
	
	image.style.visibility = "hidden";
	
	remove_button.hide()
	add_button.show()
}

var buttonGroupIndex = new Array();
var addButtonGroupUrl = '';
var addButtonUrl = '';

function addButtonGroup() {
	getResponse( addButtonGroupUrl + '?buttonGroupIndex='+buttonGroupIndex.length );
	buttonGroupIndex.push( 0 );
}



function retireButton( buttonGroup, button ) {
    jQuery('#buttonState__' + buttonGroup + '_' + button).hide('puff', 750);
	$("buttonState__" + buttonGroup + "_" + button + "_retired").value = "true";
	getResponse( addButtonUrl + '?buttonGroupIndex=' + buttonGroup + '&buttonIndex=' + buttonGroupIndex[buttonGroup]++ );
}