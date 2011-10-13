var formChanged = false;

function changedForm() {
	formChanged = true;
	$('.saveLink').addClass('disabled');
}

function observeFormChange(formId) {
    var elements = $("#"+formId + ' :input');
    elements.bind('change', function(event) {changedForm();});
}