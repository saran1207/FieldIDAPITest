<span class="fullForm fluidSets">
<#if oneScheduleAvailable>
	 <p class="multiEventInstructions"><@s.text name="label.found_multiple_schedules"><@s.param>${eventType.name?html}</@s.param></@s.text></p>

	<div class="infoSet schedulesHeading">
		<label class="label widenedLabel"><@s.text name="label.number_of_assets" /></label>
		<label class="label widenedLabel"><@s.text name="label.target_scheduled_date" /> | <a href="#" id="selectNone" onclick="selectUnscheduledForAllDropDowns(); return false;"><@s.text name="label.mark_all_not_scheduled"/></a></label>
	</div>

	<div class="schedulesBorder">
		<#list assets as asset>
			<#assign eventSchedules = multiEventScheduleListHelper.getEventSchedulesForAsset(asset)/>
			<div class="infoSet scheduleContainer">
				<label class="label widenedLabel unweighted">${asset_index + 1}. ${asset.serialNumber?html} ${asset.type.displayName?html} <br/>&nbsp;&nbsp;&nbsp; ${asset.type.descriptionTemplate?html} </label>
				<@s.select id="eventScheduleSelectBox_${asset_index}" cssClass="eventSchedules" name="scheduleId" headerKey="0" headerValue="${action.getText('label.notscheduled')}" list=eventSchedules listKey="id" listValue="nextDate" value=multiEventScheduleListHelper.getSuggestedEventScheduleIdForAsset(asset) onchange="storeScheduleId(${asset_index},this)"/>
			</div>
		</#list>
	</div>
	
	<@n4.includeScript>
	var index=0
	$$('.eventSchedules').each(function(selectBox){	
		storeScheduleId(index, selectBox);
		index++;
	});
</@n4.includeScript>
<#else>
	<p class="multiEventInstructions"><@s.text name="error.no_available_event_schedules"><@s.param>${eventType.name?html}</@s.param></@s.text></p>
</#if>
	</span >
	
<@s.form action="retrieveEventDetails" namespace="/multiEvent/ajax" id="retrieveEventDetails" cssClass="fullForm fluidSets">
	<@s.hidden name="type" id="eventTypeId"/>
	
	<#list assetIds as assetId>
		<@s.hidden name="assetIds[${assetId_index}]"/> 
	</#list>
</@s.form>

