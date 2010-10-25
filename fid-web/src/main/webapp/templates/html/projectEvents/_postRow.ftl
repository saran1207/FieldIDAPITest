<#assign assetId="${action.getAssetIdForInspectionScheduleId(entityId)}" />
<#assign jobId="${action.getJobForSchedule(entityId)!0}" />
<td>
	<a id="assignToJob_${entityId}" href='javascript:void(0);' onclick="$('assignToJob_${entityId}').hide(); $('assignmentIndicator_${entityId}').show(); getResponse('<@s.url namespace="/ajax" action="inspectionScheduleAssignToJob" uniqueID="${entityId}" assetId="${assetId}" project="${job.id}"/>');" <#if jobId != "0">style="display:none"</#if>>
		<@s.text name="label.assigntojob"/>
	</a>
	
	<a id="removeFromJob_${entityId}" href='javascript:void(0);' onclick="$('removeFromJob_${entityId}').hide(); $('assignmentIndicator_${entityId}').show(); getResponse('<@s.url namespace="/ajax" action="inspectionScheduleAssignToJob" uniqueID="${entityId}" assetId="${assetId}" project=""/>');" <#if jobId == "0">style="display:none"</#if>>
		<@s.text name="label.removefromjob"/>
	</a>
	<img src="<@s.url value="/images/indicator_mozilla_blu.gif"/>" id="assignmentIndicator_${entityId}" style="display:none" alt="<@s.text name="label.loading"/>"/>
</td>
