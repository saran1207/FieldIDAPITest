var uploadImageUrl = "";
function imageFileUploaded(fileName, directory) {
    $("newImage").value = "true";
    $("imageDirectory").value = directory;
	$("imageUpload").remove();
	$("previewImage").hide();

	$("newImageUploaded").show();
}