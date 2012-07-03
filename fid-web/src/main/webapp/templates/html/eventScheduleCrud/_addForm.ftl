<head>
	<@n4.includeScript type="text/javascript">
	    document.observe("dom:loaded", function() {
			$('newSchedule').observe('submit', 
				function(event) {
					event.stop();
					$('newScheduleForm').hide();
					$('newScheduleForm').relativize();
					$('schedulesBlankSlate').hide();
					$('newSchedule').request( getStandardCallbacks() );
			});			
		});

    function allDay(date) {
        $('allDay').checked = true;
    }

	</@n4.includeScript>

	<@n4.includeScript src="schedulePickerDates"/>
</head>

<@s.form id="newSchedule" action="eventScheduleCreate" cssClass="crudForm pageSection" namespace="/ajax" theme="fieldid" >
	<div id="schedulesForm">
			
		<div class="headerActions">
			<#if sessionUser.hasAccess("createevent")>
				<a id="newScheduleButton" class="newScheduleButton" href="#" onclick="$('newScheduleForm').show(); $('newScheduleForm').absolutize();  return false;"><@s.text name="label.addschedule"/></a>
			</#if>
		</div>		
		
		<#include "../common/_formErrors.ftl"/>
		
		<div class="fluentSets schedulesAdd newScheduleForm" id="newScheduleForm" style="display:none" >
			<h2><@s.text name="label.add_a_schedule"/></h2>
			<@s.hidden name="assetId"/>
			<div class="infoSet"> 
				<label class="label"><@s.text name="label.what_should_be_scheduled"/></label>
				<@s.select name="type" list="eventTypes" listKey="id" listValue="name"/>
			</div>
					
			<#if securityGuard.projectsEnabled>
				<div class="infoSet"> 
					<label class="label" >
						<@s.text name="label.assign_to_a_job"/><span class="egColor" style="width:auto;">(<@s.text name="label.optional"/>)</span>
					</label>
					<@s.select name="project" list="jobs" listKey="id" listValue="name" emptyOption="true"/>
				</div>
			</#if>

            <div class="infoSet">
                 <label class="label"><@s.text name="label.when_is_it_due"/></label>
				<@s.textfield id="nextDate" name="nextDate" cssClass="datetimepicker" />
                <label><input id="allDay" type="checkbox" checked="false" class="all-day-checkbox" onclick="updateDateTimePicker(this,'#nextDate');"><@s.text name="label.all_day"/></label>
                <span class="dateQuickLinks">
    				<a href="javascript:void(0);" onclick="allDay();$('nextDate').value = formatDate(new Date(), '${sessionUser.jqueryDateFormat}');">Today</a> |
					<a href="javascript:void(0);" onclick="allDay();$('nextDate').value = formatDate(addDays(new Date(), 1), '${sessionUser.jqueryDateFormat}');"><@s.text name="label.tomorrow"/></a> |
					<a href="javascript:void(0);" onclick="allDay();$('nextDate').value = formatDate(addMonths(new Date(), 6), '${sessionUser.jqueryDateFormat}');"><@s.text name="label.in_6_months"/></a> |
					<a href="javascript:void(0);" onclick="allDay();$('nextDate').value = formatDate(addYears(new Date(), 1), '${sessionUser.jqueryDateFormat}');"><@s.text name="label.next_year"/></a>
				</span>
			</div>	
			
			<div class="formAction">
				<@s.submit key="label.addschedule" />
				<@s.text name="label.or"/>
				<a href="#" onclick="$('newScheduleForm').hide(); return false;"><@s.text name="label.cancel"/></a>
			</div>
		</div>
	</div>
</@s.form>