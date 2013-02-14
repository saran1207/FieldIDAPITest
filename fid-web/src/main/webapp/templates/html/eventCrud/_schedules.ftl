<head>
	<@n4.includeScript src="schedulePickerDates"/>
	<@n4.includeScript>
		index = ${nextSchedules.size()};
		addScheduleUrl = '<@s.url action="addSchedule" namespace="/ajax" />';
		autoSuggestUrl = '<@s.url action="autoSuggestSchedule" namespace="/ajax" />';
		dateErrorText = '<@s.text name="error.mustbeadate"/>';
		eventTypeId = '${type!0}';
		assetId = '${assetId!0}';
	
		onDocumentLoad(scheduleListUpdated);

        function allDay(date) {
            $('allDay').checked = true;
        }

    </@n4.includeScript>
</head>

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
	<div id="emptySchedules" class="emptySchedules">
		<div class="fieldHolder">
			<@s.text name="label.no_schedules_have_been_created"/>
		</div>
	</div>
	<div class="addNewSchedule"><button class="addNewScheduleButton" onclick="$('newScheduleForm').show(); translatePosition($('newScheduleForm'), $(this), 0, 0); return false;" ><@s.text name="label.add_a_schedule"/></button></div>
	<div class="fluentSets schedulesAdd newScheduleForm" id="newScheduleForm" style="display:none" >
		<h2><@s.text name="label.add_a_schedule"/></h2>
		<div class="infoSet">
			<label class="label"><@s.text name="label.what_should_be_scheduled"/></label>
			<@s.select name="nextEventTypeSelection" id="nextEventTypeSelection" list="eventTypes" listKey="id" listValue="name" theme="fieldid"/>
		</div>
		
		<#if securityGuard.projectsEnabled>
			<div class="infoSet">
				<label class="label">
					<@s.text name="label.assign_to_a_job"/>
					<span class="egColor">(<@s.text name="label.optional"/>)</span>
				</label>
				<@s.select name="jobSelection" id="jobSelection" list="jobs" listKey="id" listValue="name" emptyOption="true" theme="fieldid"/>
			</div>
		</#if>

        <div class="infoSet">
            <label class="label"><@s.text name="label.when_is_it_due"/></label>
        <@s.textfield id="dueDate" name="newScheduleDate" cssClass="datetimepicker" theme="fieldid"/>
            <label class="all-day-checkbox-label"><input id="allDay" type="checkbox" class="all-day-checkbox" onclick="updateDateTimePicker(this,'#dueDate');"><@s.text name="label.all_day"/></label>
            <span class="dateQuickLinks">
				<a href="javascript:void(0);" onclick="$('dueDate').value = formatDate(new Date(), '${sessionUser.jqueryDateFormat}');">Today</a> |
				<a href="javascript:void(0);" onclick="$('dueDate').value = formatDate(addDays(new Date(), 1), '${sessionUser.jqueryDateFormat}');"><@s.text name="label.tomorrow"/></a> |
				<a href="javascript:void(0);" onclick="$('dueDate').value = formatDate(addMonths(new Date(), 1), '${sessionUser.jqueryDateFormat}');"><@s.text name="label.next_month"/></a> |
				<a href="javascript:void(0);" onclick="$('dueDate').value = formatDate(addMonths(new Date(), 6), '${sessionUser.jqueryDateFormat}');"><@s.text name="label.in_6_months"/></a> |
				<a href="javascript:void(0);" onclick="$('dueDate').value = formatDate(addYears(new Date(), 1), '${sessionUser.jqueryDateFormat}');"><@s.text name="label.next_year"/></a>
			</span>
        </div>

        <div class="infoSet">
            <label class="label"><@s.text name="label.assignee"/></label>
            <@s.select name="assignee" id="assignee" emptyOption="true" theme="fieldid">
                <#if !userGroups.isEmpty()>
                    <@s.optgroup label="User Group" list="userGroups" listKey="keyForStruts" listValue="name" />
                </#if>
                <@s.optgroup label="User" list="assignees" listKey="keyForStruts" listValue="displayName" />
            </@s.select>
        </div>

        <div class="blockSeparated">
			<button onclick="return addSchedule();"><@s.text name="label.addschedule"/></button> <@s.text name="label.or"/> <a href="#" onclick="$('newScheduleForm').hide(); return false;"><@s.text name="label.cancel"/></a>
		</div>
	</div>
</div>