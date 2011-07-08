var retireImageSrc;
var addressCount;
function addAddress() {
	var addressButton = $('addAddressButton');
	
	var emailDiv = new Element("div", {id: "addressSpan_" + addressCount});
	
	var emailSpan = new Element("span");
	
	var addressInput = new Element("input", {'type': 'text', id: "addressInput_" + addressCount, name: "view.addresses[" + addressCount + "]"});

	emailSpan.appendChild(addressInput);

	var removeLink = new Element("a", {href: 'javascript:void(0);'});
	removeLink.observe('click', function (event) {
    Event.stop(event);	   
    var p = Event.element(event).parentNode.parentNode;
    p.fade( { duration:0.25,
          afterFinish: function() {
        	  p.remove();
          }
    	});
	});
	
	removeLink.appendChild(new Element("img", {src: retireImageSrc}));
		
	emailDiv.appendChild(emailSpan);
	emailDiv.appendChild(removeLink);
	
	addressButton.insert({before: emailDiv});
	
	addressCount++;
}

function removeAddress(addressIndex) {
	var elementId = 'addressSpan_' + addressIndex;
	
	Effect.Fade(elementId, { duration:0.25,
		afterFinish: function() {
			$(elementId).remove(); 
		}
	});
	
}





