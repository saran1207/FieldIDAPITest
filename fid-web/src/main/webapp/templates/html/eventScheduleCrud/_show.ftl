
<span >${action.formatDate(eventSchedule.nextDate, false)}</span>
<#if securityGuard.projectsEnabled>
	<span >${(eventSchedule.project.name)!action.getText('label.nojob')}</span>
</#if>
<#if sessionUser.hasAccess( "createevent" ) && !inVendorContext>
	<span>
		<a id="edit_${eventSchedule.id}" href="javascript:void(0);" onclick="editSchedule( ${eventSchedule.eventType.id}, ${assetId}, ${(eventSchedule.id)!"null"}); return false;" ><@s.text name="label.edit" /></a> |
		<a id="delete_${eventSchedule.id}" href="javascript:void(0);" onclick="removeSchedule( ${eventSchedule.eventType.id}, ${assetId}, ${(eventSchedule.id)!"null"}); return false; " ><@s.text name="label.remove" /></a> |
		<a id="startEventNow_${eventSchedule.id}" <#if eventSchedule.status.getName() == "IN_PROGRESS">style="display:none"</#if> href='<@s.url action="selectEventAdd" namespace="/" assetId="${assetId}" type="${eventSchedule.eventType.id}" scheduleId="${eventSchedule.id}" />'><@s.text name="label.starteventnow"/></a>
		<a id="stopProgress_${eventSchedule.id}" <#if eventSchedule.status.getName() == "SCHEDULED">style="display:none"</#if> href='javascript:void(0);' onclick="getResponse('<@s.url action="eventScheduleStopProgress" namespace="/ajax" assetId="${assetId}" uniqueID="${eventSchedule.id}" />'); return false"><@s.text name="label.stopprogress"/></a>
	</span>
</#if>	

