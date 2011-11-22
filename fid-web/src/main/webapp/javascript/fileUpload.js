function startFileUpload() {
	activeFileUploads++;	
	hasUploadForm = false;
}

function completedFileUpload() {
	activeFileUploads--;
	addUploadFile('${uploadFileType!}', false);
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

	eval( "var func =  function() {$('"+frameId+"').remove();addUploadFile('${uploadFileType!}', true);};" );
	
	var div = new Element( 'div', { 'id':frameId, 'class':'fileUpload assetUploadPreview attachementsPreview'} 
		).insert( new Element( 'input', { 'type':'hidden', 'name':'uploadedFiles[' + frameCount + '].fileName', value:directory } )
		).insert( new Element( 'div', {'class':'previewImageDisplay'} 
			).insert( new Element( 'img', {'alt':'fileName',  'class':'previewImage', 'src':'images/attachment-icon.png'}))
		).insert( new Element('div', {'class':'previewImageLabel'}
			).insert( new Element('div', {'id':'attachmentLabel_'+frameId, 'class':'attachmentLabel'}
				).insert( new Element('span', {'class':'ellipsis_text'}).update( fileName ))
			).insert( new Element( 'a', { id: frameId + "_remove", href:"javascript:void(0)" } ).update( removeText ))
		).insert( new Element( 'div', {'class':'commentContainer'} 
			).insert( new Element( 'textarea', {id: 'uploadedFiles[' + frameCount + '].comments', 'rows': '3', 'cols': '50', 'name': 'uploadedFiles[' + frameCount + '].comments'} ) )
		);
	
	$(frameId).replace( div );
	$(frameId + "_remove").onclick = func;
	
	var ext =  fileName.slice(fileName.lastIndexOf('.'));
	/*jQuery("#attachmentLabel_"+frameId).ThreeDots({ max_rows:1, whole_word:false, allow_dangle: true, ellipsis_string:'... '+ ext });*/

}

var frameCount = 0;
var activeFileUploads = 0;
var uploadUrl = '' ;
var uploadFileLimit = 10;
var tooManyFileMessage = "Too many files already attached.";
function addUploadFile(type, isRemove) {
	if (type == undefined) {
		type = ""; 
	}
		
	if (!isRemove && ($$('.assetUploadPreview').size() + 1) >= uploadFileLimit) {
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
