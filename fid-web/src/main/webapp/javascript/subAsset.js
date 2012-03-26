var subAssetIndex = 0;
var addSubAssetUrl = "";
function getToken() {
	var token = $( 'searchToken' );
	
	if( token == null ) { return null; }
	
	return token.getValue();
}

function attachAsset( event, assetId ) {
	if( event ) {
		event.stop();
		element = Event.element( event );
		assetId = element.getAttribute( 'assetId' );
	}
	Lightview.hide();
	
	var url = addSubAssetUrl + "?uniqueID=" + $('uniqueID').value + "&subAsset.asset.iD=" + assetId + "&subAssetIndex=" + subAssetIndex + "&token=" + getToken();
	subAssetIndex++;
	getResponse( url, "get" );
}



var createAssetUrl = "";	

function submitCreateForm( event, form ) {
	// block default form submit
	if( event ) { 
		event.stop();
		form = Event.element( event ); 
	}
	
	form.request( { onComplete: contentCallback } );
}


var addAssetUrl = "";
function addSubAsset(assetType, ownerId) {
	var params = new Object();
	params.assetTypeId = assetType;
	params.token = getToken();
	params.ownerId = ownerId;
	
	getResponse(addAssetUrl , "get", params);
}


function findSubAsset(assetType) {
	$('subAssetSearchForm').observe('submit', submitSearchForm);
	var assetLinks = $$('.assetLink');
	if (assetLinks != null) {
		for (var i = 0; i < assetLinks.length; i++) {
			assetLinks[i].observe('click', attachAsset);
		}
	}		
}

var lookupAssetUrl = "";
var assetLookupTitle = "";

function submitSearchForm(event) {
	// block default form submit
 	event.stop();
  
	Lightview.show({
		href: lookupAssetUrl,
		rel: 'ajax',
		title: assetLookupTitle,
		options: {
			scrolling:true, 
			width: 700, 
			height: 420,
			ajax: {
				parameters: Form.serialize('subAssetSearchForm'),
				onComplete: findSubAsset
			}
		}
	});
}

var removeSubAssetUrl = "";
var assetIdentifier = 'assetId';
function removeSubAsset( subAssetId ) {
	var params = new Object();
	params.subAssetId =  subAssetId;
	params.uniqueID = $(assetIdentifier).getValue();
	getResponse( removeSubAssetUrl, "post", params );
}


function submitForm() {
	var inputElement = new Element("input");
	inputElement.type = "hidden";
	inputElement.name = buttonPressed.name;
	inputElement.value = buttonPressed.value;
	mainForm.appendChild(inputElement);
	
	submitCreateForm( null, mainForm  );
}

var reorderAssetsUrl = '';

var labelFormWarning = '';
function startOrdering() {
	var labelForms = $$('.labelForm');
	for (var i=0; i < labelForms.size(); i++) { 
		if (labelForms[i].visible()) { 
			alert(lableFormWarning);
			return;
		}
	}
	$$('.notAllowedDuringOrdering').each( function(element) { element.hide() } );
	$$('.drag').each( function(element) { element.show() } );
    jQuery('#assetComponentList').sortable({
        handle: '.drag',
        update: function(event, ui) {
            var rows = jQuery(this).sortable('toArray');
            var params = new Object();
            for (var i = 0; i < rows.size(); i++) {
                params['indexes[' + i + ']'] = jQuery("#" + rows[i]).attr('assetid');
            }
            params['uniqueID'] = $('uniqueID').getValue();

            getResponse(reorderAssetsUrl, 'post', params);
        }

    });


	$('startOrdering').hide();
	$('stopOrdering').show();
}

function stopOrdering() {
	$$('.notAllowedDuringOrdering').each( function(element) { element.show() } );
	$$('.drag').each( function(element) { element.hide() } );
    jQuery('#assetComponentList').sortable('destroy');
	$('startOrdering').show();
	$('stopOrdering').hide();
}
