<#assign list>
	<#include "/templates/html/eventScheduleCrud/_list.ftl" >
</#assign>

<#escape x as x?j_string >
	var errors = $('formErrors');
	
	if(errors.childElements().size() > 0) {
		errors.down().remove();
		errors.hide();
		$('nextDate').removeClassName('inputError');
	}

	$('scheduleList').replace("${list}");
</#escape>