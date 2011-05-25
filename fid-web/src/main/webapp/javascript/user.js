function updateInitials() {
	var initials = $('initials');
	var firstName = $('firstname').value;
	var lastName = $('lastname').value;
	var newValue = "";
	
	if (firstName.length > 0) {
		newValue += firstName[0];
	}
	
	if (lastName.length > 0) {
		newValue += lastName[0];
	}
	
	if(newValue.length > 0) {
		initials.value=newValue.toLowerCase()
	}
}


function allPermissionsOn() {
	var onElements = $$('.permissionOn');
	for( var i = 0; i < onElements.length; i++ ) {
		onElements[i].checked=true;
	}

}

function allPermissionsOff() {
	var offElements = $$('.permissionOff');
	for( var i = 0; i < offElements.length; i++ ) {
		offElements[i].checked=true;
			
	} 
}

var signatrueUploadUrl = "";

function imageFileUploaded(fileName, directory){
	$("imageUploadField").hide();
	$("imageUpload").remove();
	
	$("imageUploaded").show();
	$('imagePreview').hide();
	$('uploadedImage').show();
	
	$("removeImage").value = "false";
	$("newImage").value = "true";
	$("imageDirectory").value = directory;
}

function removeUploadImage() { 
	$( "imageUploaded" ).hide();
	$("removeImage").value = "true";
	$("newImage").value = "false";
	insertImageUploadFrame();
	
	$("imageUploadField").show();
	$("imageUploaded").removeClassName( "inputError" );
	$("imageUploaded").title = "";
}

function insertImageUploadFrame() {
	var iframe = '<iframe id="imageUpload" src="' + signatureUploadUrl + '" scrolling="no" scrollbar="no" style="overflow:hidden;" frameborder="0" width="250" height="35" ></iframe>';
	$("imageUploadField").insert( { top: iframe } );
}

function updatePersonalMessage() {
	if ($('sendEmail') == null) { return };
	if ($('sendEmail').checked) {
		$('personalMessage').show();
	} else {
		$('personalMessage').hide();
	}
}
function injectUploadFrameIfVisible() { 
	if ($('imageUploadField').visible()) { 
		insertImageUploadFrame(); 
	} 
}


function updatePasswordInputs() {
	if ($('assignPassword') == null) { return }
	if ($('assignPassword').checked) {
		$('passwords').show();
	} else {
		$('passwords').hide();
	}
}


var sendingWelcomeEmailText = "";
var sendWelcomeEmailUrl = "";

onDocumentLoad(function() {
	$$('#sendWelcomeEmail').each(
		function(element) { 
			element.observe('click', 
				function(event) {
					event.stop();
					getResponse(sendWelcomeEmailUrl);
					$('sendWelcomeEmail').replace('<span id="sendWelcomeEmail">' + sendingWelcomeEmailText + '</span>');
				});
		});
});

onDocumentLoad(function() { $$('.initialsInput').each(function(element) { element.observe("change", updateInitials); }); });
onDocumentLoad(function() { $('initials').observe('dblclick', updateInitials); });
onDocumentLoad(updatePersonalMessage);
onDocumentLoad(function() { $$('#sendEmail').each(
		function(element) { 
			element.observe('change', updatePersonalMessage); 
		}); 
	});
onDocumentLoad(injectUploadFrameIfVisible);	
onDocumentLoad(updatePasswordInputs);
onDocumentLoad(function() { 
		$$('#assignPassword').each(function(element) {
			element.observe("click", updatePasswordInputs);
		});
	});