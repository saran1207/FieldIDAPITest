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
		new jQuery.ajax(url, ajaxOptions);
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
}

function findPos(obj) {
    var offset = $(obj).offset();
    return [ offset.left, offset.top ];
}

function translateWithin(target, relativeElement, withinElement, offsetY, offsetX) {
    var withinElementPos = findPos(withinElement);
    var position = findPos(relativeElement);

    var newX = position[0] - withinElementPos[0] + offsetX;
    var newY = position[1] - withinElementPos[1] + offsetY;
	target.css({position: 'absolute',
        'top': newY + "px",
					'left': newX + "px"});
}

function translate(target, relativeElement, offsetY, offsetX) {
	var position = findPos(relativeElement);

    target.css({ position: 'absolute' });
	target.css({'top': position[1] + offsetY + "px",
					'left': position[0] + offsetX + "px"});
}

function translatePosition(target, relativeElement, offsetY, offsetX) {
	var position = relativeElement.position();
    target.css({ position: 'absolute' });
	target.css({'top': position.top + offsetY + "px",
					'left': position.left + offsetX + "px"});
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

$(document).ajaxComplete(
	function(e,xhr,settings) {
		if (e.result == 200) {
			pageTracker._trackPageview(transport.url);
		}
	}
);

function openSection(idToOpen, openLinkId, closeLinkId) {
	var openLink = $('#'+openLinkId);
	var closeLink = $('#'+closeLinkId);
	
	openLink.hide();
	closeLink.show();
	
	closeLink.suspendedOnClick = closeLink.onclick;
	closeLink.onclick = null;

    $('#'+idToOpen).slideDown(200, function() {
        closeLink.onclick = closeLink.suspendedOnClick;
    });

	return false;
}

function closeSection(idToClose, closeLinkId, openLinkId) {
    var openLink = $('#'+openLinkId);
    var closeLink = $('#'+closeLinkId);
	
	openLink.show();
	closeLink.hide();
	
	openLink.suspendedOnClick = openLink.onclick;
	openLink.onclick = null;

    $('#'+idToClose).slideUp(200, function() {
        openLink.onclick = openLink.suspendedOnClick;
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
	var quickViewBox = $("#"+elementId);
	quickViewBox.removeClass("hidden");
	quickViewBox.addClass("quickView");
	var button = event.target;

	quickViewBox.show();
	
	var position;
	var topPadding = 0;
	if($(button).hasClass('printAllPDFs')) {
		position = $(button).offsetParent().position();
		topPadding = 50;
	} else {
		position = $(button).position();
	}
	
	quickViewBox.css( {
        position: "absolute",
		top : (position['top'] + topPadding) + "px",
		left : (position['left'] + 25) + "px"
	});

    var interval = setInterval(function() {
        clearInterval(interval);
        quickViewBox.fadeOut();
    }, 1800);
}

function moveInsideViewPort(element) {
	var position = findPos(element);
	
	var boxTopRightCorner = $(element).width() + $(element).position().left;
	if (boxTopRightCorner > $(window).width()) {
		var offset = boxTopRightCorner - $(window).width();
		element.css( {
			left : (position[0] - offset) + "px"
		});
	}
	
	var boxBottomLeftCorner = $(element).height() + $(element).position().top;
	if (boxBottomLeftCorner > $(window).height()) {
		var offset = boxBottomLeftCorner - $(window).height();
		element.css( {
			top : (position[1] - offset) + "px"
		});
	}
}

function hideAnyOpenQuickViewBoxes() {
	$('.quickView').each( function() {
			hideQuickViewElement(this);
		});
}

function hideQuickView(elementId) {
	hideQuickViewElement($(elementId));
	
}

function hideQuickViewElement(quickViewBox) {
	$(quickViewBox).hide();
	$(quickViewBox).addClass("hidden");
	$(quickViewBox).removeClass("quickView");
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

//Element.addMethods( {
//	getStyles : function(element) {
//		element = $(element);
//		return $A(element.style).inject( {}, function(styles, styleName) {
//			styles[styleName.camelize()] = element.getStyle(styleName);
//			return styles;
//		});
//
//	},
//
//	clone : function(element) {
//		var clone = new Element(element.tagName);
//		$A(element.attributes).each( function(attribute) {
//			if (attribute.name != 'style')
//				clone[attribute.name] = attribute.value;
//		});
//
//		clone.setStyle(element.getStyles());
//		clone.update(element.innerHTML);
//		return clone;
//
//	}
//});

var fieldDescriptions = new Object();
function clearDescription(event) {
	var element = event.target;
	if ($(element).hasClass("description")) {
		$(element).removeClass("description");
		fieldDescriptions[element.id] = element.value;
		element.value = "";
	}
}

function replaceDescription(event) {
	var element = event.target;
	if (jQuery.trim(element.value).length == 0) {
		$(element).addClass("description");
		element.value = fieldDescriptions[element.id];
	}
}

function submitSmartSearch(event) {
	var element = $('#searchText');
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
	$(document).ready(functionToRun);
}


onDocumentLoad(function() {
		$('.hide').hide().removeClass('hide');
	});


onDocumentLoad(function() {
	$('.secondaryNav li a').each( function(element) {
		element.bind('mousedown', highlightButton);
		element.bind('mouseup', turnOffHighlightButton);
		element.bind('mouseout', turnOffHighlightButton);
	});
});


function positionDropDown(a, entityId){
	var list = $("#"+ a.id + "_list_" + entityId );
	var actionsContainer = $("#actionsContainer_"+entityId);
	
	positionDropDownForElements(a, list, actionsContainer);
}

function positionDropDownForElements(a, list, actionsContainer) {
    var coordinates = findPos(actionsContainer);

	if($.browser.msie){
		list.css({	'top': coordinates[1] + (actionsContainer[0].offsetHeight/2) + "px"});
	}
	list.css({	'left': coordinates[0] - (462 - actionsContainer[0].offsetWidth) + "px"});
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
	$('select#assignedToSelectBox option').each(function(option){
		if (option.value==thisUserId){
			option.selected=true;
		}
	});
}

function closeLightbox() {
    $.colorbox.close();
}