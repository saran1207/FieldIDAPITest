<div class="infoSet <#if inspectionDate?exists>autoSuggested</#if>" id="schedule_${index}">
	<label class="label" id="nextInspectionTypeText">${type.name}</label>
	<span  class="fieldHolder">
		<@s.hidden name="nextInspectionTypes[${index}]" id="nextInspectionType_${index}" value="${type.id}"/>
		<@s.hidden name="nextInspectionDates[${index}]" id="nextInspectionDate_${index}" value="${date}"/>
		<#if securityGuard.projectsEnabled && job?exists>
			<@s.hidden name="nextInspectionJobs[${index}]" id="nextInspectionJob_${index}" value="${job.id}"/>
		</#if>
		<span class="date">	on <span class="date" style="display:inline; float:none;" id="nextInspectionDateText_${index}">${date}</span></span>
		<#if securityGuard.projectsEnabled && job?exists>
			<span class="date">
				<span class="date" style="display:inline; float:none;" id="nextInspectionJobText_${index}">${job.name}</span>
			</span>
		</#if>
		<a href="#remove" name="remove" onclick="return removeSchedule(${index})">remove</a>
	</span>
</div>