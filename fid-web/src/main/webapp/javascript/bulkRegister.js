function validateForm1() {
    $('step1').hide();
    $('step2').show();
    $('step1').up(".step").addClassName("stepClosed");
    $('step2').up(".step").removeClassName("stepClosed");
    return true;
}

function backToStep1() {
	$('step2').hide();
	$('step1').show();

	$('step2').up(".step").addClassName("stepClosed");
	$('step1').up(".step").removeClassName("stepClosed");
}
