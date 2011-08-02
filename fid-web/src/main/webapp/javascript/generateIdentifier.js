var identifierUrl = "";
function generateIdentifier(elementId, uniqueID, assetTypeId) {
	new Ajax.Request( identifierUrl, {
		method: 'post',
        parameters: { assetTypeId: assetTypeId },
		onSuccess: function(transport) {
			var element = $(elementId);
			element.value = transport.responseText.strip();
			checkIdentifier(elementId, uniqueID);
		}
	});	
}

var checkIdentifierUrl = "";
function checkIdentifier(elementId, uniqueID) {
	
	var parameters = new Object();
	if (elementId == undefined) {
		elementId = 'identifierText';
	}
	if (uniqueID == undefined) {
		uniqueID = '';
	}
	parameters.identifier = $(elementId).getValue();
	parameters.uniqueId = uniqueID;
	getResponse(checkIdentifierUrl, "get", parameters);
}