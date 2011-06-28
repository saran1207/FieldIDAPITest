function updateUploadForm() {
	var uniqueId = $('eventTypeSelect').value;

	$('downloadTemplate').href = $('templateUrl').value + "&uniqueID=" + uniqueId
	$('downloadMinimalTemplate').href = $('minimalTemplateUrl').value + "&uniqueID=" + uniqueId
	
	$('uploadEventTypeId').value = uniqueId;
}

document.observe("dom:loaded", function() {
	updateUploadForm();
});