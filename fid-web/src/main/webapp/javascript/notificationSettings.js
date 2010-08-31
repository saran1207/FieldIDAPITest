var retireImageSrc;
var addressCount;
function addAddress() {
	var addressButton = $('addAddressButton');
	
	var emailDiv = new Element("div", {id: "addressSpan_" + addressCount});
	
	var emailSpan = new Element("span");
	
	var addressInput = new Element("input", {'type': 'text', id: "addressInput_" + addressCount, name: "view.addresses[" + addressCount + "]"});

	emailSpan.appendChild(addressInput);

	var removeLink = new Element("a", {href: 'javascript:void(0);', onclick: "removeAddress(" + addressCount + "); return false;"});
	
	removeLink.appendChild(new Element("img", {src: retireImageSrc}));
	
	emailDiv.appendChild(emailSpan);
	emailDiv.appendChild(removeLink);
	
	addressButton.insert({before: emailDiv});
	
	addressCount++;
}

function removeAddress(addressIndex) {
	var elementId = 'addressSpan_' + addressIndex;
	
	Effect.Fade(elementId, {
		afterFinish: function() {
			$(elementId).remove(); 
		}
	});
	
}


