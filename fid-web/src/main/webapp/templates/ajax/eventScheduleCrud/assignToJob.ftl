$('assignmentIndicator_${uniqueID}').hide();
	
<#if eventSchedule.project?exists>
	$('assignToJob_${uniqueID}').hide();
	$('removeFromJob_${uniqueID}').show();
<#else>
	$('assignToJob_${uniqueID}').show();
	$('removeFromJob_${uniqueID}').hide();
</#if>