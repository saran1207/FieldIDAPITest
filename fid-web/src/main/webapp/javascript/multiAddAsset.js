

function mergeAndSubmit(form1id, form2id, masterFormId, submitButton) {
	var formMaster = $(masterFormId);
	
	formMaster.select('input[type=hidden]').each(function (hiddenInput) {
		hiddenInput.remove();
	});
	
	if (!validateForm4()) {
		return;
	}
	
	convertAndAppendInputsToForm(formMaster, $(form1id).getElements());
	convertAndAppendInputsToForm(formMaster, $(form2id).getElements());
	
	validateRfids(submitButton);
}

function convertInputToHidden(input) {
    var value = input.getValue();

    if (input.type == "checkbox") {
        value = (value == "on") ? "true" : "false";
    }

	return new Element("input", {'type': 'hidden', 'name': input.name, 'value': value});
}

function convertAndAppendInputsToForm(form, inputs) {
	
	inputs.each(function(input) {
		form.appendChild(convertInputToHidden(input));
        if (input.type == "checkbox") {
            form.appendChild(new Element("input", {'type': 'hidden', 'name': +"__checkbox_"+input.name, 'value': 'true'}));
        }
	});
	
}

var maxAssets;
function quantityIsValid() {
	var quantity = $('quantity').getValue();
	if (quantity == null || isNaN(parseInt(quantity))  || quantity < 1 || quantity > maxAssets) {
		$('quantityInvalid').show();
		$('step2next').disable();
		return false;
	} else {
		$('quantity').value=parseInt(quantity);
		$('quantityInvalid').hide();
		$('step2next').enable();
		return true;
	}
}


function notNullOrBlank(field) {
	if (field != null && !field.blank()) {
		return true;
	} else {
		return false;
	}
}

function notTooLong(field, maxLength) {
	return (field.length <= maxLength);
}


function identifierIsValid(idx) {
	var identifier = $('identifier_' + idx).getValue();
	
	if (notNullOrBlank(identifier) && notTooLong(identifier, 50)) {
		$('identifierInvalid_' + idx).hide();
		return true;
	} else {
		$('identifierInvalid_' + idx).show();
		return false;
	}
}

function validateOwner() {
	var owner = $('ownerId').getValue();
	
	if (notNullOrBlank(owner)) {
		return true;
	} else {
		return false;
	}
}

function validateIdentified() {
	var identDate = $('identified').getValue();
	
	if (notNullOrBlank(identDate)) {
		return true;
	} else {
		return false;
	}
}

function validateInfoOptions() {
	var reqFields = $$('.requiredField');
	
	var valid = true;
	for (var i = 0; i < reqFields.length; i++) {
		if (reqFields[i].nodeName == 'SELECT') {
			if (reqFields[i].getValue() == '0') {
				valid = false;
			}
		} else if (!notNullOrBlank(reqFields[i].getValue())) {
			valid = false;
		} 
	}
	
	return valid;
}

function validateForm1() {
	if (validateOwner() && validateIdentified() && validateInfoOptions()) {
		$('form1required').hide();
		
		$('step1').hide();
		$('step2').show();
		$('step1').up(".step").addClassName("stepClosed");
		$('step2').up(".step").removeClassName("stepClosed");
		return true;
	}
	
	$('form1required').show();
	$('form1required').scrollTo();
	$('form1required').highlight();
	
	return false;
}

function validateForm2() {
	if (quantityIsValid()) {
		$('step2').hide();
		$('step3').show(); 
		
		$('step2').up(".step").addClassName("stepClosed");
		$('step3').up(".step").removeClassName("stepClosed");
		
		return true;
	}
	
	return false;
}


function startNumberIsValid() {
	if ($('snRange').getValue() != null) {
		if (isNaN(parseInt($('start').getValue())) || $('start').getValue().length > 18 ) {
			return false;
		}	
	}
	return true;
}
function validateForm3() {
	if (startNumberIsValid()) {
		$('rangeStartInvalid').hide();
		$('step3').hide(); 
		$('step3').up(".step").addClassName("stepClosed");
		$('step4Loading').up(".step").removeClassName("stepClosed");
		$('step4Loading').show();
        $('step23AssetTypeId').value = $('assetType').value;
		$('step23Form').request(getStandardCallbacks());
		return true;
	}
	$('rangeStartInvalid').show();
	return false;
	
}

function validateForm4() {
	var identifierFields = $$('.identifier');
	
	var valid = true;
	for (var i = 0; i < identifierFields.length; i++) {
		if (!identifierIsValid(i)) {
			valid = false;
		}
	}
	return valid;

}

function validateRfids(submitButton) {
	var rfidFields = $$('.rfidNumber');
	var rfids = new Array();
	for (var i = 0; i < rfidFields.length; i++) {
		var rfid = rfidFields[i].getValue();
		if ( rfid != null && rfid.length > 0) {
			rfids.push(rfid);
		}
	}
	
	checkDuplicateRfids(rfids, submitButton);
}

function backToStep2() {
	$('step3').hide(); 
	$('step2').show();
	
	$('step3').up(".step").addClassName("stepClosed");
	$('step2').up(".step").removeClassName("stepClosed");
	
}

function backToStep1() {
	$('step2').hide(); 
	$('step1').show();
	
	$('step2').up(".step").addClassName("stepClosed");
	$('step1').up(".step").removeClassName("stepClosed");
	
}
function backToStep3() {
	$('step4').hide(); 
	$('step3').show(); 
	
	$('step4').up(".step").addClassName("stepClosed");
	$('step3').up(".step").removeClassName("stepClosed");
	
}
