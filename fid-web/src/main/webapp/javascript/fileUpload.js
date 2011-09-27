function startFileUpload() {
	activeFileUploads++;	
	hasUploadForm = false;
}

function completedFileUpload() {
	activeFileUploads--;
	addUploadFile('${uploadFileType!}');
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

	eval( "var func =  function() {$('"+frameId+"').remove();addUploadFile('${uploadFileType!}');};" );
	
	var div = new Element( 'div', { 'id':frameId, 'class':'fileUpload imageUploadPreview attachementsPreview'} 
		).insert( new Element( 'input', { 'type':'hidden', 'name':'uploadedFiles[' + frameCount + '].fileName', value:directory } )
		).insert( new Element( 'div', {'class':'previewImageDisplay'} 
			).insert( new Element( 'img', {'alt':'fileName',  'class':'previewImage', 'src':'images/file-icon.png', 'width': '27px'}))
		).insert( new Element('span'
			).insert( new Element('label').update( fileName + " | ")
			).insert( new Element( 'a', { id: frameId + "_remove", href:"javascript:void(0)" } ).update( removeText ))
		).insert( new Element( 'div', {'class':'commentContainer'} 
			).insert( new Element( 'span' , {'class':"fieldHolder"}
				).insert( new Element( 'textarea', {id: 'uploadedFiles[' + frameCount + '].comments', 'rows': '3', 'cols': '50', 'name': 'uploadedFiles[' + frameCount + '].comments'} ) )
			)
		);
	
	div.select('img[class="previewImage"]').each(function(x) {x.height=27; x.width=28;});	
	$(frameId).replace( div );
	$(frameId + "_remove").onclick = func;
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
		return;
	}
	
	if (hasUploadForm) {
		return;
	}
	
	var frameId = 'frame_'+ frameCount;
	var iframe = '<iframe id="' + frameId +'" class="fileUpload form" src="'+ uploadUrl + '?frameId=' + frameId + '&frameCount=' + frameCount + '&typeOfUpload=' + type + '" scrolling="no" scrollbar="no" style="overflow:hidden;" frameborder="0" width="500" height="105" ></iframe>';
	$('uploadedfiles').insert( { bottom: iframe } );
	frameCount++;
	hasUploadForm = true;
}
