<div class="infoSet <#if inspectionDate?exists>autoSuggested</#if>" id="schedule_${index}">
	<label class="label" id="nextInspectionTypeText">${nextSchedule.typeName}</label>
	<span  class="fieldHolder">
		<@s.hidden name="nextSchedules[${index}].typeName" id="nextInspectionTypeName_${index}" value="${nextSchedule.typeName}"/>
		<@s.hidden name="nextSchedules[${index}].date" id="nextInspectionDate_${index}" value="${nextSchedule.date}"/>
		<@s.hidden name="nextSchedules[${index}].type" id="nextInspectionType_${index}" value="${nextSchedule.type}"/>
		
		<#if securityGuard.projectsEnabled && job?exists>
			<@s.hidden name="nextSchedules[${index}].job" id="nextInspectionJob_${index}" value="${nextSchedule.job!}"/>
			<@s.hidden name="nextSchedules[${index}].jobName" id="nextInspectionJobName_${index}" value="${nextSchedule.jobName!}"/>
		</#if>
		<span class="date">	on <span class="date" style="display:inline; float:none;" id="nextInspectionDateText_${index}">${nextSchedule.date}</span></span>
		<#if securityGuard.projectsEnabled && job?exists>
			<span class="date">
				<span class="date" style="display:inline; float:none;" id="nextInspectionJobText_${index}">${nextSchedule.jobName}</span>
				
			</span>
		</#if>
		<a href="#remove" name="remove" onclick="return removeSchedule(${index})">remove</a>
	</span>
</div>