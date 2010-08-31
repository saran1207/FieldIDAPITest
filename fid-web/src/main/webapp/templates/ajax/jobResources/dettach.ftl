${action.clearFlashScope()!}

$('resource_${employee.id}').remove();
if ($$('#jobResources .jobResource').size() == 0) {
	$('noResources').show();
	$('jobResources').hide();
	$('noResources').highlight();
}

var employeeSelection = $('employee');

var userOption = new Element('option');
userOption.text = '${employee.displayName?js_string}';
userOption.value = ${uniqueID};

try {
	employeeSelection.add(userOption, null); // standards compliant; doesn't work in IE
}
catch(ex) {
	employeeSelection.add(userOption); // IE only
}


var selectOptions = new Array();
for(var i = 0; employeeSelection.options.length > i; i++) {
	selectOptions[i] = employeeSelection.options[i];
}  
 
 
selectOptions.sort(
	function(a,b) {
		aText = a.text.toLowerCase();
		bText = b.text.toLowerCase();
		if (aText > bText) {
			return 1;
		} else if (aText == bText) {
			return 0;
		}
		return -1;
	});
	
employeeSelection.options.length = 0;
for(var i = 0; selectOptions.length > i; i++) {
	try {
		employeeSelection.add(selectOptions[i], null);
	} catch (e) {
		employeeSelection.add(selectOptions[i]);
	}
}