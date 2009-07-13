var addURL = 'ajax/divisionChange!add.action';
var updateURL = 'ajax/divisionChange!update.action';
var deleteURL = 'ajax/divisionChange!delete.action';
var uniqueID = 0;

function addDivision() {
	clearMessages();
	
	var addDivisionElement = $('newDivisionName'); 
	
	var divisionName = addDivisionElement.value;
	
	var url = addURL;

	new Ajax.Request(url, {
		method: 'post',
		parameters: { customerId:uniqueID, name:divisionName },
		onSuccess: function(transport) {
			populateFromResponse(transport.responseText, 'divisionMessage');
		}
	});		
	
	addDivisionElement.value = '';
}

function updateDivision(divisionId) {
	clearMessages();

	var updateDivisionElement = $('division_'+divisionId);
	
	var divisionName = updateDivisionElement.value;
	
	var url = updateURL;

	new Ajax.Request(url, {
		method: 'post',
		parameters: { uniqueId: divisionId, customerId:uniqueID, name:divisionName },
		onSuccess: function(transport) {
			populateFromResponse(transport.responseText, 'divisionMessage_'+divisionId);
		}
	});		
}

function deleteDivision(divisionId) {
	clearMessages();
	
	var url = deleteURL+'?uniqueId='+divisionId;

	new Ajax.Request(url, {
		method: 'post',
		onSuccess: function(transport) {
			populateFromResponse(transport.responseText, 'divisionMessage_'+divisionId);
		}
	});		
}	

function clearMessages() {
	var divisionEditor = $('divisionEditor');
	var messageElements = divisionEditor.getElementsByClassName('error');
	for ( var i = 0; i < messageElements.length; i++ ) {
		messageElements[i].innerHTML = ' ';
	} 
}

function populateFromResponse(jsonString, elementName) {
	var jsonObject = JSON.parse(jsonString);
	
	var messageElement = document.getElementById(elementName);
	
	messageElement.innerHTML = ' '+jsonObject.resultMessage;
	
	if (jsonObject.functionEqual == 'add') {
		if (jsonObject.divisionId > 0) {
			var container = $('divisionContainer');
			
			var newInput = $('divisionStandby').cloneNode(true);
			newInput.id = 'division_'+jsonObject.divisionId;
			newInput.value = jsonObject.name;
			container.appendChild(newInput);
			
			var newLink = $('updateStandby').cloneNode(true);
			newLink.id = 'updateDiv_'+jsonObject.divisionId;
			newLink.onclick = function() { updateDivision(jsonObject.divisionId); };
			
			container.appendChild(newLink);
			container.appendChild(new Element('span').update(" "));
			 		
			var deleteLink = $('deleteStandby').cloneNode(true);
			deleteLink.id = 'deleteDiv_'+jsonObject.divisionId;
			deleteLink.onclick = function() { deleteDivision(jsonObject.divisionId); };
			
			container.appendChild(deleteLink);

			var divisionMessage = $('divisionMessageStandby').cloneNode(true);
			divisionMessage.id = 'divisionMessage_'+jsonObject.divisionId;
							
			container.appendChild(divisionMessage);
			container.appendChild(document.createElement('br'));
		}
	} else if (jsonObject.functionEqual == 'delete') {
		if (jsonObject.divisionId > 0) {
			var divisionInput = $('division_'+jsonObject.divisionId);
			var divisionLink = $('updateDiv_'+jsonObject.divisionId);
			var divisionDel = $('deleteDiv_'+jsonObject.divisionId);
			var divisionMsg = $('divisionMessage_'+jsonObject.divisionId);
			
			Effect.BlindUp(divisionInput.id, { duration: 0.5});
			Effect.BlindUp(divisionLink.id, { duration: 0.5});
			Effect.BlindUp(divisionDel.id, { duration: 0.5});
			Effect.BlindUp(divisionMsg.id, { duration: 0.5});
		}
	}
}