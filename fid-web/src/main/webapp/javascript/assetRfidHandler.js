var mainForm;
var buttonPressed = null;
var checkRfidUrl= "";


function checkDuplicateRfids(rfids, submitButton, uniqueId) {
	lockSubmitButtons();
		
	mainForm = submitButton.form;
	buttonPressed = submitButton 
	new Ajax.Request(checkRfidUrl, {
		parameters: {
            uniqueId: uniqueId,
			rfids: rfids
		},
		method: 'post',
		asynchronous:false,
		onSuccess: contentCallback
	});
}

	
function cancel() {
	Lightview.hide();
	unlockSubmitButtons();	
}
function closeAndSubmit() {
	submitForm();
}
		
		
function submitForm( formId ) {
	
	var inputElement = new Element("input");
	inputElement.type = "hidden";
	inputElement.name = buttonPressed.name;
	inputElement.value = buttonPressed.value;
	mainForm.appendChild(inputElement);

	mainForm.submit();
}
		
document.observe( 'lightview:hidden', function(event) {	unlockSubmitButtons(); } );

