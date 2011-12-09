var identifierUrl = "";
function generateIdentifier(elementId, uniqueID, assetTypeId) {
	getResponse( identifierUrl, 'post', {
        assetTypeId: assetTypeId,
        uniqueId:uniqueID,
        elementId: elementId
	});
}

function identifierGenerated(elementId, uniqueID, generatedIdentifier) {
    $(elementId).value = generatedIdentifier;

    checkIdentifier(elementId, uniqueID);
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