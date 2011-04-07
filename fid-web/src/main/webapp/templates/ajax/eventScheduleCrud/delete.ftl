<#escape x as x?j_string >
	$('type_${uniqueID}').remove();
	if ($$('#schedules tr').size() == 0) {
		$('scheduleList').hide();
		$('schedulesBlankSlate').show();
	}
</#escape>