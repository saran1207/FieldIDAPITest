

function toggleButtonClicked(id) { 
	var button = jQuery('#'+id);
	if (button.hasClass('buttonClicked')) {
		button.addClass('buttonClicked');
	} else {
		button.removeClass('buttonClicked');
	}
}

