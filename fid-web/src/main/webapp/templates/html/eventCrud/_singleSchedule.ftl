<div class="blockSeparated eventSchedule ${nextSchedule.autoScheduled?string('autoSuggested','')}" id="schedule_${index}">
	
	<@s.hidden name="nextSchedules[${index}].typeName" id="nextEventTypeName_${index}" value="${nextSchedule.typeName}"/>
	<@s.hidden name="nextSchedules[${index}].date" id="nextEventDate_${index}" value="${nextSchedule.date}"/>
	<@s.hidden name="nextSchedules[${index}].type" id="nextEventType_${index}" value="${nextSchedule.type!}"/>
	<@s.hidden name="nextSchedules[${index}].autoScheduled" id="autoScheduled_${index}" value="${nextSchedule.autoScheduled?string}"/>
	
	<#if securityGuard.projectsEnabled && job?exists>
		<@s.hidden name="nextSchedules[${index}].job" id="nextEventJob_${index}" value="${nextSchedule.job!}"/>
		<@s.hidden name="nextSchedules[${index}].jobName" id="nextEventJobName_${index}" value="${nextSchedule.jobName!}"/>
	</#if>
	
	<div class="addScheduleDate">${nextSchedule.date}</div>
	
	<div class="addScheduleLabel">
		<label id="nextEventTypeText">${nextSchedule.typeName?html}</label>
			
			<#if securityGuard.projectsEnabled && job?exists>
				<div class="inline addScheduleJob"> 
					<@s.text name="label.on"/> ${nextSchedule.jobName?html}
				</div>
			</#if>
	</div>
	<a href="#remove" name="remove" onclick="return removeSchedule(${index})"><img src="<@s.url value="/images/small-x.png"/>" /></a>
</div>