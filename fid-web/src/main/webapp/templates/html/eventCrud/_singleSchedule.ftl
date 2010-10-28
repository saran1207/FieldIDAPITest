<div class="blockSeparated inspectionSchedule ${nextSchedule.autoScheduled?string('autoSuggested','')}" id="schedule_${index}">
	<label id="nextInspectionTypeText">${nextSchedule.typeName?html}</label>
	<@s.hidden name="nextSchedules[${index}].typeName" id="nextInspectionTypeName_${index}" value="${nextSchedule.typeName}"/>
	<@s.hidden name="nextSchedules[${index}].date" id="nextInspectionDate_${index}" value="${nextSchedule.date}"/>
	<@s.hidden name="nextSchedules[${index}].type" id="nextInspectionType_${index}" value="${nextSchedule.type}"/>
	<@s.hidden name="nextSchedules[${index}].autoScheduled" id="autoScheduled_${index}" value="${nextSchedule.autoScheduled?string}"/>
	
	<#if securityGuard.projectsEnabled && job?exists>
		<@s.hidden name="nextSchedules[${index}].job" id="nextInspectionJob_${index}" value="${nextSchedule.job!}"/>
		<@s.hidden name="nextSchedules[${index}].jobName" id="nextInspectionJobName_${index}" value="${nextSchedule.jobName!}"/>
	</#if>
	<@s.text name="label.on"/> ${nextSchedule.date}
	<#if securityGuard.projectsEnabled && job?exists>
		<@s.text name="label.for"/> ${nextSchedule.jobName?html}
	</#if>
	
	<a href="#remove" name="remove" onclick="return removeSchedule(${index})"><@s.text name="label.remove"/></a>
</div>