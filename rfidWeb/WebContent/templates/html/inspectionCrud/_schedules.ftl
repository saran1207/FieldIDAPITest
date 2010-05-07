
<h2><@s.text name="label.schedules"/></h2>
<div id="schedules">
	<#if nextSchedules?exists && !nextSchedules.empty>
		<#list nextSchedules as nextSchedule>
			<#assign index=nextSchedule_index/>
			<#include "_singleSchedule.ftl"/>
		</#list>
	</#if>
</div>
<div id="emptySchedules">
	<@s.text name="label.no_schedules_have_been_created"/>
</div>


<div><button id="addNewSchedule" onclick="$('newScheduleForm').show(); $('newScheduleForm').absolutize();  return false;" ><@s.text name="label.add_a_schedule"/></button></div>
<div class="infoSet schedulesAdd" id="newScheduleForm" style="display:none" >
	
	<div>
		<@s.select name="nextInspectionTypeSelection" id="nextInspectionTypeSelection" list="eventTypes" listKey="id" listValue="name" theme="fieldidSimple"/>
	</div>
	
	<div><@s.datetimepicker id="nextDate" name="newScheduleDate" theme="fieldidSimple"/></div>
	<#if securityGuard.projectsEnabled>
		<div>
			<@s.select name="jobSelection" id="jobSelection" list="jobs" listKey="id" listValue="name" emptyOption="true" theme="fieldidSimple"/>
		</div>
	</#if>
	
	<button onclick="return addSchedule();">add</button> <@s.text name="label.or"/> <a href="#" onclick="$('newScheduleForm').hide(); return false;"><@s.text name="label.cancel"/></a>
</div>


<@n4.includeScript>
	index = 0;
	addScheduleUrl = '<@s.url action="addSchedule" namespace="/ajax" />';
	autoSuggestUrl = '<@s.url action="autoSuggestSchedule" namespace="/ajax" />';
	dateErrorText = '<@s.text name="error.mustbeadate"/>';
	inspectionTypeId = '${type!0}';
	productId = '${productId!0}';
	<#if !noAutoSuggest?exists || !noAutoSuggest>
		onDocumentLoad(autoSuggest);
	</#if>
</@n4.includeScript>
