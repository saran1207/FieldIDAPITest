
<span >${action.formatDate(inspectionSchedule.nextDate, false)}</span>
<#if securityGuard.projectsEnabled>
	<span >${(inspectionSchedule.project.name)!action.getText('label.nojob')}</span>
</#if>
<#if sessionUser.hasAccess( "createinspection" ) && !inVendorContext>
	<span>
		<a id="edit_${inspectionSchedule.id}" href="javascript:void(0);" onclick="editSchedule( ${inspectionSchedule.inspectionType.id}, ${assetId}, ${(inspectionSchedule.id)!"null"}); return false;" ><@s.text name="label.edit" /></a> |
		<a id="delete_${inspectionSchedule.id}" href="javascript:void(0);" onclick="removeSchedule( ${inspectionSchedule.inspectionType.id}, ${assetId}, ${(inspectionSchedule.id)!"null"}); return false; " ><@s.text name="label.remove" /></a> |
		<a id="inspectNow_${inspectionSchedule.id}" <#if inspectionSchedule.status.getName() == "IN_PROGRESS">style="display:none"</#if> href='<@s.url action="selectEventAdd" namespace="/" assetId="${assetId}" type="${inspectionSchedule.inspectionType.id}" scheduleId="${inspectionSchedule.id}" />'><@s.text name="label.starteventnow"/></a>
		<a id="stopProgress_${inspectionSchedule.id}" <#if inspectionSchedule.status.getName() == "SCHEDULED">style="display:none"</#if> href='javascript:void(0);' onclick="getResponse('<@s.url action="eventScheduleStopProgress" namespace="/ajax" assetId="${assetId}" uniqueID="${inspectionSchedule.id}" />'); return false"><@s.text name="label.stopprogress"/></a>
	</span>
</#if>	

