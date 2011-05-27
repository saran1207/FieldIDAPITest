<#escape x as x?j_string >
	$('type_${uniqueID}').remove();
	if ($$('#scheduleList tr').size() == 1) {
		$('scheduleList').hide();
		$('schedulesBlankSlate').show();
	}
</#escape>