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
				<@s.textfield id="nextDate" name="nextDate" cssClass="datepicker" />
				<span class="dateQuickLinks">
					<a href="javascript:void(0);" onclick="$('nextDate').value = formatDate(new Date());">Today</a> |
					<a href="javascript:void(0);" onclick="$('nextDate').value = formatDate(addDays(new Date(), 1));">Tomorrow</a> |
					<a href="javascript:void(0);" onclick="$('nextDate').value = formatDate(addMonths(new Date(), 1));">Next Month</a> |
					<a href="javascript:void(0);" onclick="$('nextDate').value = formatDate(addMonths(new Date(), 6));">In 6 Months</a> |
					<a href="javascript:void(0);" onclick="$('nextDate').value = formatDate(addYears(new Date(), 1));">Next Year</a>
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
	
<script type="text/javascript">
	$('newSchedule').observe('submit', 
		function(event) {
			event.stop();
			$('newScheduleForm').hide();
			$('schedulesBlankSlate').hide();
			$('newSchedule').request( getStandardCallbacks() );
			
		});
		
	function formatDate(date) {
		return jQuery.datepicker.formatDate('${sessionUser.jqueryDateFormat}', date);
	}
	
	function addDays(date, days) {
		return new Date( date.setDate(date.getDate() + days) );
	}
	function addMonths(date, months) {
		return new Date( date.setMonth(date.getMonth() + months) );
	}
	function addYears(date, years) {
		return new Date( date.setFullYear(date.getFullYear() + years) );
	}
	
</script>
