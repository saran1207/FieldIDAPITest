String.prototype.trim = function() {
    a = this.replace(/^\s+/, '');
    return a.replace(/\s+$/, '');
};

function clearForm(form, reset) {
	var inputs = form.getElementsByTagName("input");
	var selects = form.getElementsByTagName("select");
	var textareas = form.getElementsByTagName("textarea");
	if (reset == undefined) {
		reset = false;
	}

	for ( var i = 0; i < inputs.length; i++) {
		if (inputs[i].type == "text") {
			if (reset) {
				form.reset();
			} else {
				inputs[i].value = "";
			}
			if (typeof inputs[i].onchange != "undefined"
					&& inputs[i].onchange != null) {
				inputs[i].onchange(inputs[i]);
			}
		}
	}

	for ( var i = 0; i < textareas.length; i++) {
		if (reset) {
			form.reset();
		} else {
			textareas[i].value = "";
		}

	}

	for ( var i = 0; i < selects.length; i++) {
		if (reset) {
			form.reset();
		} else {
			selects[i].selectedIndex = 0;
		}

		if (typeof selects[i].onchange != "undefined"
				&& selects[i].onchange != null) {
			selects[i].onchange(selects[i]);
		}
	}
}

function getResponseNonInteractive(url, method, parameters) {
	if (parameters == undefined) {
		parameters = null;
	}
	getResponse(url, method, parameters, true);
}

function getStandardCallbacks() {
	return {
		onComplete : contentCallback,
		onFailure : failureCallback
	};
}

function getResponse(url, method, parameters, nonInteractive) {
	if (method == undefined || method == null) {
		method = "post";
	}

	var ajaxOptions = getStandardCallbacks();
	if (nonInteractive) {
		ajaxOptions.onFailure = function() {
		};
	}

	if (parameters != undefined && parameters != null) {
		ajaxOptions.parameters = parameters;
	}

	ajaxOptions.method = method;

	try {
		new Ajax.Request(url, ajaxOptions);
	} catch (e) {
		alert('your request could not be processed correctly.');
	}
	return false;
}

function failureCallback() {
	alert('Error occurred contacting the server.');
}



function contentCallback(transport) {
	switch (transport.status) {
	case 500:
	case 503:
		break;
	default:
		evalResponse(transport.responseText);
	}
}

function contentResponse(response) {
	return evalResponse(response);
}

function evalResponse(response) {
	try {
		eval(response);
	} catch (e) {
		alert(e);
		return;
	}

}

function updateMessages(messages, errors, modifiedId) {
	var idPrefix = "";
	if (modifiedId != undefined && modifiedId != null) {
		idPrefix = modifiedId + "_";
	}

	var messageContainer = $(idPrefix + 'message');
	var errorContainer = $(idPrefix + 'error');
	var highlight = true;
	if (messages.size() == 0 && errors.size() == 0
			&& messageContainer.childElements().size() == 0
			&& errorContainer.childElements().size() == 0) {
		highlight = false;
	}

	messageContainer.update("");
	errorContainer.update("");

	var messageList = new Element('ul');

	var errorList = new Element('ul');

	for ( var i = 0; i < messages.length; i++) {
		messageList.appendChild(new Element('li').update(new Element('span', {
			'class' : "actionMessage"
		}).update(messages[i])));
	}

	for ( var i = 0; i < errors.length; i++) {
		errorList.appendChild(new Element('li').update(new Element('span', {
			'class' : "errorMessage"
		}).update(errors[i])));
	}

	if (messages.length > 0) {
		messageContainer.appendChild(messageList);
		messageContainer.show();
	} else {
		messageContainer.hide();
	}
	if (errors.length > 0) {
		errorContainer.appendChild(errorList);
		errorContainer.show();
	} else {
		errorContainer.hide();
	}

	if (highlight) {
		$('notifications').highlight();
	}
}

function findPos(obj) {
    var curtop = 0;
	var curleft = 0;
	if (obj.offsetParent) {
		do {
			curleft += obj.offsetLeft;
			curtop += obj.offsetTop;
		} while (obj = obj.offsetParent);
	}
	return [ curleft, curtop ];

}

function translate(target, relativeElement, offsetY, offsetX) {
	var position = findPos(relativeElement);

	target.setStyle({'top': position[1] + offsetY + "px",
					'left': position[0] + offsetX + "px"});
}

function runJavascript(container) {
	var scripts = container.getElementsByTagName("script");

	for ( var i = 0; i < scripts.length; i++) {
		eval(scripts[i].text);
	}
}

function removeChildren(element) {
	while (element.childNodes.length > 0) {
		element.removeChild(element.firstChild);
	}
}

function redirect(url) {
	window.location = url;
	return false;
}

function openNewWindow(url){
	window.open(url);
	return false;
}

Ajax.Responders.register( {
	onComplete : function(transport) {
		if (transport.transport.state == 200) {
			pageTracker._trackPageview(transport.url);
		}

	}
});

function openSection(idToOpen, openLinkId, closeLinkId, afterEvent) {
	var openLink = $(openLinkId);
	var closeLink = $(closeLinkId);
	
	openLink.hide();
	closeLink.show();
	
	closeLink.suspendedOnClick = closeLink.onclick;
	closeLink.onclick = null;
	
	Effect.BlindDown(idToOpen, {
		duration : 0.2,
		afterFinish : function(effect) {
			closeLink.onclick = closeLink.suspendedOnClick;
			
			if (afterEvent) {
				afterEvent(this);
			}
		}
	});
	
	return false;
}

function closeSection(idToClose, closeLinkId, openLinkId, afterEvent) {
	var openLink = $(openLinkId);
	var closeLink = $(closeLinkId);
	
	openLink.show();
	closeLink.hide();
	
	openLink.suspendedOnClick = openLink.onclick;
	openLink.onclick = null;
	
	Effect.BlindUp(idToClose, {
		duration : 0.2,
		afterFinish : function(effect) {
			openLink.onclick = openLink.suspendedOnClick;
			
			if (afterEvent) {
				afterEvent(this);
			}
		}
	});
	
	return false;
}

function sortList(selectId) {
	var lb = $(selectId);
	var arrTexts = new Array();
	var names = new Object();
	for (i = 0; i < lb.length; i++) {
		arrTexts[i] = lb.options[i].text;
		names[lb.options[i].text] = lb.options[i].value;
	}

	arrTexts.sort();

	for (i = 0; i < lb.length; i++) {
		lb.options[i].text = arrTexts[i];
		lb.options[i].value = names[arrTexts[i]];
	}
}

function cleanAjaxForm(form, errorPrefix) {
	if (errorPrefix == null || errorPrefix == undefined) {
		errorPrefix = "";
	}
	var inputs = form.getElements();

	for ( var i = 0; inputs.size() > i; i++) {
		var input = inputs[i];
		input.removeClassName("inputError");
		input.title = "";
	}

	if ($(errorPrefix + 'formErrors') != null) {
		$(errorPrefix + 'formErrors').remove();
	}
}

function convertLinkToAjax(link) {
	link.observe('click', function(event) {
		getResponse(Event.element(event).href);
		event.stop();
	});
}

function convertClassedLinksToAjax(className) {
	var links = $$('a.' + className);

	links.each(convertLinkToAjax);

}

function convertPaginationLinksToAjax() {
	convertClassedLinksToAjax('paginationLink');
}

function showQuickView(elementId, event) {

	if (event != undefined) {
		event.clickid = elementId
	}

	hideAnyOpenQuickViewBoxes();
	var quickViewBox = $(elementId);
	quickViewBox.removeClassName("hidden");
	quickViewBox.addClassName("quickView");
	var button = Event.findElement(event, "A");

	quickViewBox.show();
	quickViewBox.absolutize();
	

	var position = button.cumulativeOffset();

	quickViewBox.setStyle( {
		top : position['top'] + "px",
		left : (position['left'] + 25) + "px"
	});

	moveInsideViewPort(quickViewBox);

	quickViewBox.fx = function(event) {
		var clickid;

		if (event.clickid != undefined) {
			clickid = event.clickid;
		} else {
			var element = Event.element(event);
			if (element.id != undefined
					&& (element.id == elementId + "_img" || element.id == elementId
							+ "_button")) {
				clickid = elementId;
			}
		}

		if (clickid != elementId) {
			hideQuickView(elementId);
		}
	};
	quickViewBox.bfx = quickViewBox.fx.bindAsEventListener(quickViewBox);
	Element.extend(document).observe('click', quickViewBox.bfx);
}

function moveInsideViewPort(element) {
	var position = findPos(element);
	
	var boxTopRightCorner = element.getWidth() + element.viewportOffset().left;
	if (boxTopRightCorner > document.viewport.getWidth()) {
		var offset = boxTopRightCorner - document.viewport.getWidth();
		element.setStyle( {
			left : (position[0] - offset) + "px"
		});
	}
	
	var boxBottomLeftCorner = element.getHeight() + element.viewportOffset().top;
	if (boxBottomLeftCorner > document.viewport.getHeight()) {
		var offset = boxBottomLeftCorner - document.viewport.getHeight();
		element.setStyle( {
			top : (position[1] - offset) + "px"
		});
	}
}

function hideAnyOpenQuickViewBoxes() {
	$$('.quickView').each( function(element) {
			hideQuickViewElement(element);
		});
}

function hideQuickView(elementId) {
	hideQuickViewElement($(elementId));
	
}

function hideQuickViewElement(quickViewBox) {
	quickViewBox.hide();
	quickViewBox.addClassName("hidden");
	quickViewBox.removeClassName("quickView");
	Element.extend(document).stopObserving('click', quickViewBox.bfx);
}

function alertErrors(errors) {
	var alertMessage = "";
	if (errors.empty) {
		return;
	}

	for ( var i = 0; i < errors.size(); i++) {
		alertMessage += errors[i] + "\n";
	}
	alert(alertMessage);
}

function selectAll(containerId) {
	var checkboxes = $$('#' + containerId + ' input[type="checkbox"]');
	for ( var i = 0; checkboxes.length > i; i++) {
		checkboxes[i].checked = true;
	}
}
function selectNone(containerId) {
	var checkboxes = $$('#' + containerId + ' input[type="checkbox"]');
	for ( var i = 0; checkboxes.length > i; i++) {
		checkboxes[i].checked = false;
	}
}

Element.addMethods( {
	getStyles : function(element) {
		element = $(element);
		return $A(element.style).inject( {}, function(styles, styleName) {
			styles[styleName.camelize()] = element.getStyle(styleName);
			return styles;
		});

	},

	clone : function(element) {
		var clone = new Element(element.tagName);
		$A(element.attributes).each( function(attribute) {
			if (attribute.name != 'style')
				clone[attribute.name] = attribute.value;
		});

		clone.setStyle(element.getStyles());
		clone.update(element.innerHTML);
		return clone;

	}
});
var fieldDescriptions = new Object();
function clearDescription(event) {
	var element = Event.element(event);
	if (element.hasClassName("description")) {
		element.removeClassName("description");
		fieldDescriptions[element.id] = element.value;
		element.value = "";

	}
}
function replaceDescription(event) {
	var element = Event.element(event);
	if (element.value.strip().empty()) {
		element.addClassName("description");
		element.value = fieldDescriptions[element.id];

	}
}
function submitSmartSearch(event) {
	var element = $('searchText');
	if (element.value.strip().empty() || element.hasClassName("description")) {
		alert("You cannot search for a blank identifier or rfid number");
		event.stop();
	}
}

function highlightButton(event) {
	var element = Event.element(event);
	event.stop();
	element.addClassName("activated");
}

function turnOffHighlightButton(event) {
	event.stop();
	var element = Event.element(event);
	element.removeClassName("activated");
}



function ajaxFormEvent(event) {
	event.stop();
	var form = Event.element(event);
	ajaxForm(form);
}

function ajaxForm(form) {
	form.request(getStandardCallbacks());
}


function updateDropDown(select, newList, selectId) {
	replaceSelectList(select, newList);
	
	setValueOnSelect(select, selectId);
}

function replaceSelectList(select, newList) {
	while (select.length > 0) {
		select.remove(0);
	} 
	
	newList.each(function (element) {
			var option = new Element("option", { value : element.id }).update(element.name);
			select.insert(option);
		});
	
}

function setValueOnSelect(selectElement, valueToSelect) {
	for ( var i = 0; i < selectElement.options.length; i++) {
		if (selectElement.options[i].value == valueToSelect) {
			selectElement.selectedIndex = i;
			return
		}
	}
}

function isValueDefined(value, additionalUndefinedValue) {
	if (additionalUndefinedValue == undefined) {
		additionalUndefinedValue = null;
	}
	
	return (value != undefined &&  value != null && value != additionalUndefinedValue);
}


function postForm(url, values) {
	var form = new Element('form');
	form.action = url;
	form.method = 'POST';
	for (var name in values) {
		form.insert(new Element('input', { type:"hidden", name: name, value: values[name]}));
	}
	
	form.hide();
	$$('body').first().insert(form);

	form.submit();
	$$('body').first().remove(form);
}

function onDocumentLoad(functionToRun) {
	document.observe("dom:loaded", functionToRun);
}


onDocumentLoad(function() {
		$$('.hide').each(function(element) {
			element.hide();
			element.removeClassName('hide');
		});
	});


onDocumentLoad(function() {
	$$('.secondaryNav li a').each( function(element) {
		element.observe('mousedown', highlightButton);
		element.observe('mouseup', turnOffHighlightButton);
		element.observe('mouseout', turnOffHighlightButton);
	});
});


function positionDropDown(a, entityId){
	var list = $(a.id + "_list_" + entityId );
	var actionsContainer = $("actionsContainer_"+entityId);
	var coordinates = findPos(actionsContainer);
	
	if(Prototype.Browser.IE){
		list.setStyle({	'top': coordinates[1] + (actionsContainer.offsetHeight/2) + "px"});
		//alert(actionsContainer.offsetHeight);
	}
	list.setStyle({	'left': coordinates[0] - (130 - actionsContainer.offsetWidth) + "px"});
}

function positionDropDownForElements(a, list, actionsContainer) {
    var coordinates = findPos(actionsContainer);

    if(Prototype.Browser.IE){
        list.setStyle({	'top': coordinates[1] - (a.offsetHeight - actionsContainer.offsetHeight)+ "px"});
    }
    list.setStyle({	'left': coordinates[0] - (130 - actionsContainer.offsetWidth) + "px"});
}

 function createBookmark(url, title) {

	if (window.sidebar) { // Mozilla Firefox Bookmark
		window.sidebar.addPanel(title, url, "");
	} else if (window.external) { // IE Favorite
		window.external.AddFavorite(url, title);
	} else if (window.opera && window.print) { // Opera Hotlist
		return true;
	}
}
 
 function toggleDisabled(element){
	if (element.disabled){
		element.disabled=false;
	}else{
		element.disabled=true;
	}
}
 
 function toggleHidden(element){
	if (element.visible()){
		element.hide();
	}else{
		element.show();
	}
 }

function toggleDisableLocationPicker(listOfInputElements, chooseButton){
	listOfInputElements.each(function(element) {
		toggleDisabled(element);
	});
		toggleHidden(chooseButton);
}

function setAssignedToAsCurrentUser(thisUserId){
	$$('select#assignedToSelectBox option').each(function(option){
		if (option.value==thisUserId){
			option.selected=true;
		}
	});
}

