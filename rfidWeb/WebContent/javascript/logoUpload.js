var uploadImageUrl = "";
function imageFileUploaded( fileName, directory ){
	$("imageUpload").remove();
	$("imageUploaded").show();
	$("removeImage").value = "false";
	$("previewImage").hide();
	$("newImage").value = "true";
	$("newImageUploaded").show();
	$("imageDirectory").value = directory;
}

function removeUploadImage() { 
	$( "imageUploaded" ).hide();
	$("removeImage").value = "true";
	var iframe = '<iframe id="imageUpload" src="' + uploadImageUrl + '" scrollbar="no" style="overflow:hidden;" frameborder="0" width="500" height="35" ></iframe>';
	$( "imageUploadField" ).insert( { top: iframe } );
	$("imageUploaded").removeClassName( "inputError" );
	$("imageUploaded").title = "";
}