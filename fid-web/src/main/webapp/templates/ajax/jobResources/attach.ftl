${action.clearFlashScope()!}
<#assign user=employee/>
<#assign html><#include "/templates/html/projects/_attachedUser.ftl"/></#assign>

$('jobResources').insert({top:'${html?js_string}'});

$('noResources').hide();
$('jobResources').show();
closeResourceForm();

var employeeSelection = $('employee');
for (var i = 0; employeeSelection.options.length > i; i++) {
	if (employeeSelection.options[i].value == ${uniqueID}) {
		employeeSelection.remove(i);
		break;
	}
}

