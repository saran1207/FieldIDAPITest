var arrayIndex = 0;
var removed = new Array();
var arrayIndexOptions = 0;
var removedOptions = new Array();
var STARTING_WEIGHT_VALUE = 0;

var unretireLabel = "";
var retireLabel = "";

function openCloseOptions( id ) {
	var container = $(id);
	if( container.style.display == "none" ) {
		openOptions(id);
	} else {
		closeOptions(id);
	}
}

function closeOptions( id ) {
	var container = $(id);
	container.style.display = "none";
}
function openOptions( id ) {
	var container = $(id);
	container.style.display = "block";
}




function changeFieldType( select ) {
	if( select.options[ select.selectedIndex ].value == "TextField" 
		|| select.options[ select.selectedIndex ].value == "UnitOfMeasure"
		|| select.options[ select.selectedIndex ].value == "DateField" ) {
		showOptionalElements(select.parentNode.parentNode);
		hideInfoOptions( select.parentNode.parentNode );
	} else {
		hideOptionalElements(select.parentNode.parentNode);
		showInfoOptions(select.parentNode.parentNode);
	}
	
	if( select.options[ select.selectedIndex ].value == "UnitOfMeasure" ) {
		showDefaultUnitOfMeasure( select.parentNode.parentNode );
	} else {
		hideDefaultUnitOfMeasure( select.parentNode.parentNode );
	}
}


function showDefaultUnitOfMeasure( target ) {
	var unitOfMeasure = $( "assetTypeUpdate_infoFields_" + findFieldIndex( target.id ) + "__defaultUnitOfMeasure" )
	if( unitOfMeasure != null ) 
		unitOfMeasure.show();
}

function hideDefaultUnitOfMeasure( target ) {
	var unitOfMeasure = $( "assetTypeUpdate_infoFields_" + findFieldIndex( target.id ) + "__defaultUnitOfMeasure" )
	if( unitOfMeasure != null ) 
		unitOfMeasure.hide();
}

function showOptionalElements( field ) {
	var optionalFields = field.getElementsByClassName("optional");
	if( optionalFields != null ) {
		for( var i = 0; i < optionalFields.length; i++ ) {
			optionalFields[i].style.visibility = "visible";
		}
	}
}

function hideInfoOptions(target) {
	closeOptions( "infoOptionContainer_" + findFieldIndex( target.id ) );
	target.getElementsByClassName( "editInfoOptions" )[0].style.display = "none"; 
}
function showInfoOptions(target) {
	closeOptions( "infoOptionContainer_" + findFieldIndex( target.id ) );
	target.getElementsByClassName( "editInfoOptions" )[0].style.display = "inline";
}

function hideOptionalElements( field ) {
	var optionalFields = field.getElementsByClassName("optional");
	if( optionalFields != null ) {
		for( var i = 0; i < optionalFields.length; i++ ) {
			optionalFields[i].style.visibility = "hidden";
		}
	}
}

function createField() {
	
	
	fieldName = 'field_'+ arrayIndex;
	
	var newDiv = $('standby').cloneNode(true);
	newDiv.id=fieldName;
	
	var element_name_prefix = "infoFields[" + arrayIndex + "].";
	var element_id_prefix = "assetTypeUpdate_infoFields_" + arrayIndex + "__" ;
		
	renameElements( newDiv , "assetTypeUpdate_info", "info", element_id_prefix, element_name_prefix );
	
	$('infoFields').appendChild(newDiv);
	
	var infoOptionContainerDivs = newDiv.getElementsByClassName( 'infoOptionContainer' );
	infoOptionContainerDivs[0].id = 'infoOptionContainer_' + arrayIndex;
	
	var infoOptionDivs = infoOptionContainerDivs[0].getElementsByTagName( 'div' );
	infoOptionDivs[0].id = 'infoOptions_' + arrayIndex;
	
	
	Effect.BlindDown(newDiv.id, { duration: 0.5});
	destroyLineItemSortables();
	createLineItemSortables();
	arrayIndex++;
}

function renameElement( node, old_id_prefix, old_name_prefix, element_id_prefix, element_name_prefix, name  ) {
	if( node.id == old_id_prefix + name ) {
		node.id = element_id_prefix + name;
		node.name = element_name_prefix + name;
	}
	// checkbox inputs have a second hidden input.
	if( node.name == "__checkbox_" + old_name_prefix  + name ) {
		node.name = "__checkbox_" + element_name_prefix + name;
	}
}


function renameElements(newDiv, old_id_prefix, old_name_prefix, element_id_prefix, element_name_prefix ) {
	for ( index in  newDiv.childNodes ) {
		var node = newDiv.childNodes[index];
		
		renameElement(node, old_id_prefix, old_name_prefix, element_id_prefix, element_name_prefix, "weight" );
		renameElement(node, old_id_prefix, old_name_prefix, element_id_prefix, element_name_prefix, "uniqueID" );
		renameElement(node, old_id_prefix, old_name_prefix, element_id_prefix, element_name_prefix, "deleted" );
		for ( inputs_index in  node.childNodes ) {
			var secondNode = node.childNodes[inputs_index];
			
			renameElement(secondNode, old_id_prefix, old_name_prefix, element_id_prefix, element_name_prefix, "name" );
			renameElement(secondNode, old_id_prefix, old_name_prefix, element_id_prefix, element_name_prefix, "fieldType" );
			renameElement(secondNode, old_id_prefix, old_name_prefix, element_id_prefix, element_name_prefix, "defaultUnitOfMeasure" );
			renameElement(secondNode, old_id_prefix, old_name_prefix, element_id_prefix, element_name_prefix, "required" );
			
		}
	}
}

function renameElementsOptions(newDiv, old_id_prefix, old_name_prefix, element_id_prefix, element_name_prefix ) {
	for ( index in  newDiv.childNodes ) {
		var node = newDiv.childNodes[index];

		renameElement(node, old_id_prefix, old_name_prefix, element_id_prefix, element_name_prefix, "weight" );
		renameElement(node, old_id_prefix, old_name_prefix, element_id_prefix, element_name_prefix, "infoFieldIndex" );
		renameElement(node, old_id_prefix, old_name_prefix, element_id_prefix, element_name_prefix, "name" );
		renameElement(node, old_id_prefix, old_name_prefix, element_id_prefix, element_name_prefix, "deleted" );
	}
}


function destroyField( target ) {
	var handle = target.parentNode.parentNode;
	Effect.BlindUp(handle.id, { duration: 0.5}); 
	removed.push( handle );
	$('assetTypeUpdate_infoFields_'+ findFieldIndex( handle.id ) + '__deleted').value=true;
}


function createOption( optionContainer, infoFieldContainer ) {
	var container = $( optionContainer );
	optionName = 'infoOption_'+ arrayIndexOptions;
	
	var newDiv = $('optionStandby').cloneNode(true);
	newDiv.id=optionName;
	
	var element_name_prefix = "editInfoOptions[" + arrayIndexOptions + "].";
	var element_id_prefix = "assetTypeUpdate_editInfoOptions_" + arrayIndexOptions + "__" ;
		
	renameElementsOptions( newDiv , "assetTypeUpdate_infoOp_", "infoOp.", element_id_prefix, element_name_prefix);
	
	container.appendChild(newDiv);
	$( element_id_prefix + 'infoFieldIndex' ).value = findFieldIndex( infoFieldContainer );
	
	Effect.BlindDown(newDiv.id, { duration: 0.5});
	arrayIndexOptions++;
	
}

function destroyOption( target ) {
	var handle = target.parentNode;
	Effect.BlindUp(handle.id, { duration: 0.5});
	$('assetTypeUpdate_editInfoOptions_'+ findOptionIndex( handle.id ) + '__deleted').value=true;
	
	var id = handle.parentNode.parentNode.parentNode.id;
	if( typeof(removedOptions[findFieldIndex( id )]) == 'undefined' ) {
		removedOptions[findFieldIndex( id )] = new Array();
	}
	 
	removedOptions[findFieldIndex( id )].push( handle );
	
}


function createLineItemSortables() {
	Sortable.create('infoFields',{tag:'div',dropOnEmpty: true, containment: ['infoFields'],only:'handle'});
}

function destroyLineItemSortables() {
	Sortable.destroy('infoFields');
}

function formSubmit(){
	clearDisabledFields();			
	setWeights();
}

function clearDisabledFields() {
	var infoFieldContainer = $('infoFields');
	changeDisabledOn( infoFieldContainer, false );
}

function findOptionIndex( id ) {
	var regexForIndex = new RegExp("infoOption_([0-9]+)");
	var m = regexForIndex.exec( id );
	if( m != null ) {
		return m[1];
	} else {
		return '';
	}
}

function findFieldIndex( id ) {
	var regexForIndex = new RegExp("field_([0-9]+)");
	var m = regexForIndex.exec( id );
	if( m != null ) {
		return m[1];
	} else {
		return '';
	}
}



function setWeights(){
	var infoFieldContainer = $('infoFields');
	var weight = STARTING_WEIGHT_VALUE;
	var node = null;	
	
	
	// setn info field sort order.
	for ( var index = 0; index <  infoFieldContainer.childNodes.length; index++ ) {
		node = infoFieldContainer.childNodes[index];
		for( var inputIndex = 0; inputIndex < node.childNodes.length; inputIndex++ ) {
			var input = node.childNodes[inputIndex];
			if( typeof(input.id) != 'undefined' && input.id.indexOf( "_weight" ) != -1 ) {
				input.value = weight++;
			}
		}
	}
	
	// set options order.
	var infoOptionContainers = infoFieldContainer.getElementsByClassName('infoOptions');
	for( var i = 0; i < infoOptionContainers.length; i++) {
		weight = STARTING_WEIGHT_VALUE;
		for ( var j = 0; j <  infoOptionContainers[i].childNodes.length; j++ ) {
			node = infoOptionContainers[i].childNodes[j];
			for( var inputIndex = 0; inputIndex < node.childNodes.length; inputIndex++ ) {
				var input = node.childNodes[inputIndex];
				if( typeof(input.id) != 'undefined' && input.id.indexOf( "_weight" ) != -1 ) {
					input.value = weight++;
				}
			}		
		}
	}
}

function undoDeletes() {
	
	for( var index = 0; index < removed.length; index++ ) {
		$('assetTypeUpdate_infoFields_'+ findFieldIndex( removed[index].id ) + '__deleted').value = 'false';  
		Effect.BlindDown(removed[index].id, { duration: 0.5});
	}
	removed = new Array();
}

function undoOptionDeletes( target ) {

	
	handle = target.parentNode.parentNode.id;
	
	if( typeof(removedOptions[findFieldIndex( handle )]) == 'undefined' ) {
		removedOptions[findFieldIndex( handle )] = new Array();
	}
	
	for( var index = 0; index < removedOptions[findFieldIndex( handle )].length; index++ ) {
		$('assetTypeUpdate_editInfoOptions_'+ findOptionIndex( removedOptions[findFieldIndex( handle )][index].id ) + '__deleted').value = 'false';  
		Effect.BlindDown(removedOptions[findFieldIndex( handle )][index].id, { duration: 0.5});
	}
	
	removedOptions[findOptionIndex( handle )] = new Array();
}


function retire( target ) {
	var handle = target.parentNode.parentNode;
	
	if( $('assetTypeUpdate_infoFields_'+ findFieldIndex( handle.id ) + '__retired').value == "true" ) {
		unRetire( handle, target );
	} else {
		applyRetire( handle, target );
	}
	
	
}

function applyRetire( handle, target ) {
	handle.addClassName( "retired" );
	target.innerHTML = unretireLabel;  
	$('assetTypeUpdate_infoFields_'+ findFieldIndex( handle.id ) + '__retired').value="true";
	changeDisabledOn( handle, true );
}

function unRetire( handle, target ) {

	handle.removeClassName( "retired" );
	target.innerHTML = retireLabel;  
	$('assetTypeUpdate_infoFields_'+ findFieldIndex( handle.id ) + '__retired').value= "false";
	changeDisabledOn( handle, false );
}


function changeDisabledOn( handle, disabledValue ) {
	
	var inputs = handle.getElementsByTagName('input');
	var selects = handle.getElementsByTagName( 'select' );
	
	for( var i = 0; i < inputs.length; i++ ) {
		inputs[i].disabled = disabledValue;
	}
	for( var i = 0; i < selects.length; i++ ) {
		selects[i].disabled = disabledValue;
	}
}

function prepInterface() {
	var contianers = ['infoFields', 'standby' ];
	for( var j = 0; j < contianers.length; j++ ) {
		
		var selects = $( contianers[j] ).getElementsByTagName( 'select' );
		for( var i = 0; i< selects.length; i++ ) {
			if( typeof(selects[i].onchange) != "undefined" && selects[i].onchange != null ) {
				selects[i].onchange();
			}
		}
	}
}





function setupRemoved(){
	var infoFieldContainer = $('infoFields');

	var infoFields = infoFieldContainer.getElementsByClassName('handle');
	for ( var index = 0; index <  infoFields.length; index++ ) {
		node = infoFields[index];
		if( $('assetTypeUpdate_infoFields_'+ findFieldIndex( node.id ) + '__deleted').value == 'true' ) {
			removed.push( node );
		}
	}
	
	var infoOptionContainers = infoFieldContainer.getElementsByClassName('infoOptions');
	for( var i = 0; i < infoOptionContainers.length; i++) {
		var infoOptions = infoOptionContainers[i].getElementsByClassName('infoOpitonHandle');
		for ( var j = 0; j < infoOptions .length; j++ ) {
			node = infoOptions[j];
			if( $('assetTypeUpdate_editInfoOptions_'+ findOptionIndex( node.id ) + '__deleted').value == 'true' ) {
				
				
				
				var id = node.parentNode.parentNode.parentNode.id;
				
				if( typeof(removedOptions[findFieldIndex( id )]) == 'undefined' ) {
				
					removedOptions[findFieldIndex( id )] = new Array();
				}
	 
				removedOptions[findFieldIndex( id )].push( handle );
				
			}				
		}
	}
}


function correctSorting() {
	var infoFieldContainer = $('infoFields');
	var newOrder = new Array();
	var infoFields = infoFieldContainer.getElementsByClassName('handle');
	for ( var index = 0; index <  infoFields.length; index++ ) {
		node = infoFields[index];
		var weight = $('assetTypeUpdate_infoFields_'+ findFieldIndex( node.id ) + '__weight').value
		 
		newOrder.push( new Array( node, parseInt(weight ) ) );
		newOrder.sort( function(a,b) { return a[1] - b[1] } );
		
	}
	
	
	if ( infoFieldContainer.hasChildNodes() )
	{
	    while ( infoFieldContainer.childNodes.length >= 1 )
	    {
	        infoFieldContainer.removeChild( infoFieldContainer.firstChild );       
	    } 
	}
	
	for ( var index = 0; index <  newOrder.length; index++ ) {
		if( typeof( newOrder[index] ) != "undefined" ) {
			infoFieldContainer.appendChild( newOrder[index][0] );
		}
	}
	
	var infoOptionContainers = infoFieldContainer.getElementsByClassName('infoOptions');
	for( var i = 0; i < infoOptionContainers.length; i++) {
		var infoOptions = infoOptionContainers[i].getElementsByClassName('infoOpitonHandle');
		for ( var j = 0; j < infoOptions .length; j++ ) {
			node = infoOptions[j];
			// move to position				
		}
	}

}

function removeUploadedImage() {
	$('removeImage').value="true";
	Effect.Fade( 'removeImageLink' );
} 
