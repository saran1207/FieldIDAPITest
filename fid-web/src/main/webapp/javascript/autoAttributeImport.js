function updateUploadForm() {
	var assetTypeId = $('assetTypeSelect').value;

	$('downloadTemplate').href = $('templateUrl').value + "?criteriaId=" + assetTypeId

	$('uploadAutoAttributeId').value = assetTypeId;
}

document.observe("dom:loaded", function() {
	updateUploadForm();
});