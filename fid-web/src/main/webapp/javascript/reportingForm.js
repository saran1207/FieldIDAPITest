var formChanged = false;
function changedForm() {
	formChanged = true;
	var links = $$('.saveLink');
	for (var i = 0; i < links.size(); i++) {
		links[i].addClassName("disabled");
	}
}

function observeFormChange(formId) {
    var elements = $(formId).getElements();
    for (var i = 0; i < elements.size(); i++) {
        Element.extend(elements[i]).observe('change', function(event) { changedForm(); });
    }
}