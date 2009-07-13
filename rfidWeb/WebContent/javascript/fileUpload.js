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
	
	var div = new Element( 'div', { 'id':frameId, 'class':'fileUpload'} 
		).insert( new Element( 'input', { 'type':'hidden', 'name':'uploadedFiles[' + frameCount + '].fileName', value:directory } ) 
		).insert( fileName + " " 
		).insert( new Element( 'a', { id: frameId + "_remove", href:"javascript:void(0)" } ).update( removeText )
		).insert( new Element( 'div' 
			).insert( new Element( 'label' ).update( commentsText ) 
			).insert( new Element( 'span' 
				).insert( new Element( 'textarea', {id: 'uploadedFiles[' + frameCount + '].comments', 'rows': '3', 'cols': '50', 'name': 'uploadedFiles[' + frameCount + '].comments'} ) )
			)
		);
	
	$(frameId).replace( div );
	
	$(frameId + "_remove").onclick = func ;
}

var frameCount = 0;
var activeFileUploads = 0;
var uploadUrl = '' ;
var uploadFileLimit = 100;
var tooManyFileMessage = "Too many files already attached. You can have a total of " + uploadFileLimit + " attachments";
function addUploadFile(type) {
	if (type == undefined) {
		type = ""; 
	}
	
	if ($$('.fileUpload').size() >= uploadFileLimit) {
		alert(tooManyFileMessage);
		return;
	}
	
	var frameId = 'frame_'+ frameCount;
	var iframe = '<iframe id="' + frameId +'" class="fileUpload" src="'+ uploadUrl + '?frameId=' + frameId + '&frameCount=' + frameCount + '&typeOfUpload=' + type + '" scrollbar="no" style="overflow:hidden;" frameborder="0" width="500" height="21" ></iframe>';
	$('uploadedfiles').insert( { bottom: iframe } );
	frameCount++;
}



