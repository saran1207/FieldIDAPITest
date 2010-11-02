${action.setPageType('inspection_type', 'edit')!}



<@s.form action="eventTypeDelete" id="mainContent" cssClass="fullForm fluidSets" theme="fieldid">
	<h2 class="clean"><@s.text name="label.delete_summary"/></h2>
	<#if archiveSummary.canBeRemoved()>
		<div class="pageInstructions">
			<@s.text name="instruction.delete_event_type"/>
		</div>
	<#else>
		<div class="errors">
			<@s.text name="error.can_not_delete_event_type.part_of_master"><@s.param>${archiveSummary.eventsPartOfMaster}</@s.param></@s.text>
		</div>
	</#if>
		
	<@s.hidden name="uniqueID"/>
	<div class="multiColumn">
		<div class="infoBlock">
			<div class="infoSet">
				<label class="label"><@s.text name="label.remove_from_asset_types"/></label>
				<span class="fieldHolder">${archiveSummary.removeFromAssetTypes}</span>
			</div>
			<div class="infoSet">
				<label class="label"><@s.text name="label.delete_event_frequencies"/></label>
				<span class="fieldHolder">${archiveSummary.deleteEventFrequencies}</span>
			</div>
			<div class="infoSet">
				<label class="label"><@s.text name="label.delete_notifcation_settings"/></label>
				<span class="fieldHolder">${archiveSummary.notificationsToDelete}</span>
			</div>
		</div>
		<div class="infoBlock">
			<div class="infoSet <#if !archiveSummary.canBeRemoved()>error</#if>">
				<label class="label"><@s.text name="label.delete_part_of_master_events"/></label>
				<span class="fieldHolder">${archiveSummary.eventsPartOfMaster}</span>
			</div>
			<div class="infoSet ">
				<label class="label"><@s.text name="label.delete_events"/></label>
				<span class="fieldHolder">${archiveSummary.deleteEvents}</span>
			</div>
			
			<div class="infoSet ">
				<label class="label"><@s.text name="label.delete_schedules"/></label>
				<span class="fieldHolder">${archiveSummary.deleteSchedules}</span>
			</div>
		</div>
	</div>
	
	<div class="actions">
		<#if archiveSummary.canBeRemoved()>
			<@s.submit key="label.delete"/> <@s.text name="label.or"/> 
		</#if>
		<a href="<@s.url action="eventTypeEdit" uniqueID="${uniqueID}"/>" ><@s.text name="label.cancel"/></a>
	</div>
</@s.form>
