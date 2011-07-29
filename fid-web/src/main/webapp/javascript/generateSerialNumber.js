var serialNumberUrl = "";
function generateSerialNumber(elementId, uniqueID, assetTypeId) {
	new Ajax.Request( serialNumberUrl, {
		method: 'post',
        parameters: { assetTypeId: assetTypeId },
		onSuccess: function(transport) {
			var element = $(elementId);
			element.value = transport.responseText.strip();
			checkSerialNumber(elementId, uniqueID);
		}
	});	
}

var checkSerialNumberUrl = "";
function checkSerialNumber(elementId, uniqueID) {
	
	var parameters = new Object();
	if (elementId == undefined) {
		elementId = 'serialNumberText';
	}
	if (uniqueID == undefined) {
		uniqueID = '';
	}
	parameters.serialNumber = $(elementId).getValue();
	parameters.uniqueId = uniqueID;
	getResponse(checkSerialNumberUrl, "get", parameters);
}