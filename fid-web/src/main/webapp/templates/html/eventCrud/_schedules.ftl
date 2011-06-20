<div id="schedulesForm">
	<h2><@s.text name="label.schedules"/></h2>
	
	<div id="schedules">
	<#if nextSchedules?exists && !nextSchedules.empty>
		<#list nextSchedules as nextSchedule>
			<#if nextSchedule?exists>
				<#assign index=nextSchedule_index/>
				<#include "_singleSchedule.ftl"/>
			</#if>
		</#list>
	</#if>
	</div>
	<div id="emptySchedules">
		<div class="fieldHolder">
			<@s.text name="label.no_schedules_have_been_created"/>
		</div>
	</div>
	
	<div class="blockSeparated"><button id="addNewSchedule" onclick="$('newScheduleForm').show(); $('newScheduleForm').absolutize();  return false;" ><@s.text name="label.add_a_schedule"/></button></div>
	<div class="fluentSets schedulesAdd" id="newScheduleForm" style="display:none" >
		<h2><@s.text name="label.add_a_schedule"/></h2>
		<div class="infoSet">
			<label class="label"><@s.text name="label.event_type"/></label>
			<@s.select name="nextEventTypeSelection" id="nextEventTypeSelection" list="eventTypes" listKey="id" listValue="name" theme="fieldid"/>
		</div>
		
		<div class="infoSet">
			<label class="label"><@s.text name="label.for_date"/></label>
			<span class="fieldHolder">
				<@s.textfield id="nextDate" name="newScheduleDate" cssClass="datepicker"/>
			</span>
		</div>
		<#if securityGuard.projectsEnabled>
			<div class="infoSet">
				<label class="label"><@s.text name="label.job"/></label>
				<@s.select name="jobSelection" id="jobSelection" list="jobs" listKey="id" listValue="name" emptyOption="true" theme="fieldid"/>
			</div>
		</#if>
		<div class="blockSeparated"> 
			<button onclick="return addSchedule();"><@s.text name="label.add"/></button> <@s.text name="label.or"/> <a href="#" onclick="$('newScheduleForm').hide(); return false;"><@s.text name="label.cancel"/></a>
		</div>
	</div>
</div>

<@n4.includeScript>
	index = ${nextSchedules.size()};
	addScheduleUrl = '<@s.url action="addSchedule" namespace="/ajax" />';
	autoSuggestUrl = '<@s.url action="autoSuggestSchedule" namespace="/ajax" />';
	dateErrorText = '<@s.text name="error.mustbeadate"/>';
	eventTypeId = '${type!0}';
	assetId = '${assetId!0}';
	
	onDocumentLoad(scheduleListUpdated);
</@n4.includeScript>
