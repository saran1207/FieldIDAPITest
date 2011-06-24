function updateUploadForm() {
	var assetTypeId = $('assetTypeSelect').value;

	$('downloadTemplate').href = $('templateUrl').value + "?assetTypeId=" + assetTypeId

	$('uploadAssetTypeId').value = assetTypeId;
}

document.observe("dom:loaded", function() {
	updateUploadForm();
});