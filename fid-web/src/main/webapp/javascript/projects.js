

function openNoteForm() {
	$( 'addNewNote' ).hide();
	$( 'closeNewNote' ).show();
	$( 'addNote' ).show();
	$( 'noteComments' ).select();
}

function closeNoteForm() {
	$( 'addNewNote' ).show();
	$( 'closeNewNote' ).hide();
	$( 'addNote' ).hide();
}

function clearNoteForm() {
	$( 'noteComments' ).clear();
	addUploadFile();
	cleanAjaxForm( $( 'addNote' ), "addNote_" );
}


function openAssetSearch() {
	$( 'addNewAsset' ).hide();
	$( 'closeNewAsset' ).show();
	$( 'projectAssetSearch_search' ).select();
	$( 'assetLookup' ).show();
}

function closeAssetSearch() {
	$( 'closeNewAsset' ).hide();
	$( 'addNewAsset' ).show();
	$( 'assetLookup' ).hide();
	$( 'results' ).update("");
}



function findAssets() {
	
	var assetLinks = $$( '.assetLink' );
	if( assetLinks != null ) {
		if( assetLinks.size() == 1 ) {
			
			attachAsset(assetLinks[0]);
		} else {
			for (var i = 0; i < assetLinks.length; i++ ) {
				assetLinks[i].observe('click', attachAssetListener);
			}
		}
	}
}

var attachAssetToProjectUrl = "";

function attachAssetListener( event ) {
	event.stop();
	attachAsset( Event.element( event ).getAttribute( 'assetId' ) );
}

function oneResultAsset( assetId, results ) {
	attachAsset( assetId );
}


function attachAsset( assetId ) {
	var urlParams = new Object;
	urlParams.uniqueID= assetId
	urlParams.projectId = $( 'uniqueID' ).getValue();
	
	getResponse( attachAssetToProjectUrl, "post", urlParams );
}

var removeAssetUrl = "";

function removeAsset( event ) {
	event.stop(); 
	var asset = Event.element( event ); 
	
	
	var urlParams = new Object;
	urlParams.uniqueID= asset.getAttribute( 'assetId' );
	urlParams.projectId = $( 'uniqueID' ).getValue();
	
	getResponse( removeAssetUrl, "post", urlParams );	
}

var removeNoteUrl = "";
var removeNoteWarning = "Note will be deleted. Are you sure?"
function removeNote( event ) {
	event.stop(); 
	var note = Event.element( event );
	
	if( confirm( removeNoteWarning ) ) {
		var urlParams = new Object;
		urlParams.uniqueID= note.getAttribute( 'noteId' );
		urlParams.projectId = $( 'uniqueID' ).getValue();
		
		getResponse(removeNoteUrl, "post", urlParams);
	}	
}


	

var uploadUrl = '';
function addUploadFile(limitReached, limitReachedHTML) {
	var iframe = '';
	if (limitReached) {
		iframe = limitReachedHTML;
	} else {
		iframe = '<iframe id="attachment" class="fileUpload" src="'+ uploadUrl + '?frameId=attachment&frameCount=1" scrolling="no" scrollbar="no" style="overflow:hidden;" frameborder="0" width="500" height="21" ></iframe>';
	}
	$('attachment').replace( iframe );
}

var removeText = '';

function fileUploaded( frameId, frameCount, fileName, directory ){

	var div = new Element( 'span', { 'id':frameId } )
			.insert( new Element( 'input', { 'type':'hidden', 'name':'note.fileName', value:directory } ) )
			.insert( fileName + " " )
			.insert( new Element( 'a', { id: frameId + "_remove", href:"javascript:void(0)" } ).update( removeText ) );
	
	$(frameId).replace( div );
	
	$(frameId + "_remove").onclick = addUploadFile ;
}


function progressStopped(uniqueId) {
	$('startEventNow_' + uniqueId).show();
	$('stopProgress_' + uniqueId).hide();
}


function openResourceForm() {
	$('addNewResource').hide();
	$('closeNewResource').show();
	$('addResource').show();
	
}

function closeResourceForm() {
	$('addNewResource').show();
	$('closeNewResource').hide();
	$('addResource').hide();
}