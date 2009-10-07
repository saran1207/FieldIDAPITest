function moveToStep2() {
	var params = new Object();
	params[$('remoteTenantId').name] = $('remoteTenantId').getValue();
	getResponse(remoteTenantUrl, 'get', params);
	$('chooseTenant').hide();
	$('chooseConnection').show();
}

function updateReciever(name) {
	$('receiver').update(name);
}

function updateSender(name) {
	$('senderOrg').update(name);
}

var remoteTenantUrl = '';
document.observe("dom:loaded", function() {
		$('chooseConnection').hide();
		$('messageInput').hide();
		
		$('continueButton').observe('click', function(event) {
				event.stop();
				
				$('chooseConnection').hide();
				updateReciever($('remoteOrgList').options[$('remoteOrgList').selectedIndex].innerHTML);
				updateSender($('localOrgName_orgName').getValue());
				$('messageInput').show();
				
			});
	
	
		
		$('findRemoteOrg').observe('submit', function(event) {
				var form = Event.element(event);
				form.request(getStandardCallbacks());
				event.stop();
			});
	});