<div class="editEventSchedule">
	<@s.form id="schedule_${uniqueID}" action="eventScheduleSave" namespace="/ajax" theme="fieldidSimple" >				
		<@s.hidden name="type"  />
		<@s.hidden name="assetId"  />
		<@s.hidden name="uniqueID" />
		
		<@s.datetimepicker name="nextDate" onkeypress="return doSubmit(event,  ${uniqueID});"/>
		<#if securityGuard.projectsEnabled>
			<@s.select name="project" list="jobs" listKey="id" listValue="name" emptyOption="true"/>
		</#if>
		<div class="eventScheduleEditActions">
			<a id="save_${eventSchedule.id}"href="javascript:void(0);" onclick="saveSchedule( ${uniqueID} ); return false;" ><@s.text name="label.save" /></a> |
			<a id="cancel_${eventSchedule.id}"href="javascript:void(0);" onclick="cancelSchedule(${type}, ${assetId},  ${uniqueID}); return false;" ><@s.text name="label.cancel" /></a>
		</div>
		
		<@s.fielderror>
			<@s.param>nextDate</@s.param>				
		</@s.fielderror>
		
	</@s.form>
	
</div>