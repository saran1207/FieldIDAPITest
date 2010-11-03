function startFileUpload() {
	activeFileUploads++;	
}

function completedFileUpload() {
	activeFileUploads--;
}

var uploadWarning = "";
function checkForUploads() {
	if( activeFileUploads > 0 ) {
		return confirm( uploadWarning );
	} else {
		return true;
	}
}

var removeText = '';
function fileUploaded( frameId, frameCount, fileName, directory ){

	eval( "var func =  function() { $('"+frameId+"').remove(); };" );
	
	var div = new Element( 'div', { 'id':frameId, 'class':'fileUpload infoSet'} 
		).insert( new Element( 'input', { 'type':'hidden', 'name':'uploadedFiles[' + frameCount + '].fileName', value:directory } ) 
		).insert( new Element( 'label', {'class':"commentsLabel"} ).update("Attachment")
		).insert( fileName + " " 
		).insert( new Element( 'a', { id: frameId + "_remove", href:"javascript:void(0)" } ).update( removeText )
		).insert( new Element( 'div', {'class':'commentContainer'} 
			).insert( new Element( 'label', {'class':"label"} ).update( commentsText ) 
			).insert( new Element( 'span' , {'class':"fieldHolder"}
				).insert( new Element( 'textarea', {id: 'uploadedFiles[' + frameCount + '].comments', 'rows': '3', 'cols': '50', 'name': 'uploadedFiles[' + frameCount + '].comments'} ) )
			)
		);
	
	$(frameId).replace( div );
	$(frameId + "_remove").onclick = func ;
}

var frameCount = 0;
var activeFileUploads = 0;
var uploadUrl = '' ;
var uploadFileLimit = 10;
var tooManyFileMessage = "Too many files already attached.";
function addUploadFile(type) {
	if (type == undefined) {
		type = ""; 
	}
	
	if ($$('.fileUpload').size() >= uploadFileLimit) {
		alert(tooManyFileMessage);
		return;
	}
	
	var frameId = 'frame_'+ frameCount;
	var iframe = '<iframe id="' + frameId +'" class="fileUpload" src="'+ uploadUrl + '?frameId=' + frameId + '&frameCount=' + frameCount + '&typeOfUpload=' + type + '" scrolling="no" scrollbar="no" style="overflow:hidden;" frameborder="0" width="500" height="21" ></iframe>';
	$('uploadedfiles').insert( { bottom: iframe } );
	frameCount++;
}
