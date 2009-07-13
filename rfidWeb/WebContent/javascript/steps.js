function backToStep(step) {
	toStep(step);
}

function toStep(step, openTarget) {
	if (openTarget == undefined) {
		openTarget = 'step' + step;
	}
	
	$$('.stepContent').each( function(element) {
		element.hide();
	});
	
	$$('.step').each( function(element) {
		element.addClassName("stepClosed");
	});
	
	
	$(openTarget).show();
	$(openTarget).up(".step").removeClassName("stepClosed");
	
}