<@s.form id="newSchedule" action="eventScheduleCreate" cssClass="crudForm pageSection" namespace="/ajax" theme="fieldid" >
	<#include "../common/_formErrors.ftl"/>		
	<div id="schedulesForm">
		<div class="headerActions">
			<#if sessionUser.hasAccess("createevent")>
				<a id="newScheduleButton" href="#" onclick="$('newScheduleForm').show(); $('newScheduleForm').absolutize();  return false;"><@s.text name="label.addschedule"/></a>
			</#if>
		</div>

		<div class="fluentSets schedulesAdd" id="newScheduleForm" style="display:none" >
			<h2><@s.text name="label.add_a_schedule"/></h2>
			<@s.hidden name="assetId"  />
			<div class="infoSet"> 
				<label for="type"><@s.text name="label.eventtype"/></label>
				<@s.select name="type" list="eventTypes" listKey="id" listValue="name"/>
			</div>
			
			<div class="infoSet">
				<label for="type"><@s.text name="label.scheduledate"/></label>
				<@s.datetimepicker name="nextDate" />
			</div>	
			
			<#if securityGuard.projectsEnabled>
				<div class="infoSet"> 
					<label for="job"><@s.text name="label.job"/></label>
					<@s.select name="project" list="jobs" listKey="id" listValue="name" emptyOption="true"/>
				</div>
			</#if>
			
			<div class="formAction">
				<@s.submit key="label.save" />
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
			$('newSchedule').request( getStandardCallbacks() );
			
		});
</script>
