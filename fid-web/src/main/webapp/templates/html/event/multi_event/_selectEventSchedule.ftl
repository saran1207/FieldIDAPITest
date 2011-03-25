<@s.form action="retrieveEventDetails" namespace="/multiEvent/ajax" id="retrieveEventDetails" cssClass="fullForm fluidSets">
	<#if false>
		<@s.text name="error.no_available_event_schedules"><@s.param>${eventType.name?html}</@s.param></@s.text> 
	</#if>
	
	<#list assets as asset>
		<#assign eventSchedules = multiEventScheduleListHelper.getEventSchedulesForAsset(asset)/>
		<div class="infoSet">
			<label class="label">${asset.displayName?html}</label>
			<@s.select id="eventSchedules" name="scheduleId" headerKey="0" headerValue="${action.getText('label.notscheduled')}" list=eventSchedules listKey="id" listValue="nextDate" onchange="pushScheduleIdIntoHiddenVariable(${asset_index},this)"/>
		</div>
	</#list>
	
	<@s.hidden name="type" id="eventTypeId"/>
	
	<#list assetIds as assetId>
		<@s.hidden name="assetIds[${assetId_index}]"/> 
	</#list>
</@s.form>

