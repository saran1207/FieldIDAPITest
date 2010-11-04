<@s.form id="newSchedule" action="eventScheduleCreate" cssClass="crudForm pageSection" namespace="/ajax" theme="fieldid" >
	<#include "../common/_formErrors.ftl"/>				
	<h2><@s.text name="label.addschedule"/></h2>
	<div class="contentSection">
		<@s.hidden name="assetId"  />
		<div class="infoSet">
			<label for="type"><@s.text name="label.scheduledate"/></label>
			<@s.datetimepicker name="nextDate" />
		</div>	
		
		<div class="infoSet"> 
			<label for="type"><@s.text name="label.eventtype"/></label>
			<@s.select name="type" list="eventTypes" listKey="id" listValue="name"/>
		</div>
		<#if securityGuard.projectsEnabled>
			<div class="infoSet"> 
				<label for="job"><@s.text name="label.job"/></label>
				<@s.select name="project" list="jobs" listKey="id" listValue="name" emptyOption="true"/>
			</div>
		</#if>
		<div class="formAction">
			<@s.reset key="label.cancel"/>
			<@s.submit key="label.save"/>
		</div>
	</div>	
</@s.form>
	
<script type="text/javascript">
	$('newSchedule').observe('submit', 
		function(event) {
			event.stop();
			$('newSchedule').request( getStandardCallbacks() );
			
		});
</script>
