${action.setPageType('inspection_type', 'edit')!}



<@s.form action="eventTypeDelete" id="mainContent" cssClass="fullForm fluidSets" theme="fieldid">
	<h2 class="clean"><@s.text name="label.delete_summary"/></h2>
	<#if archiveSummary.canBeRemoved()>
		<div class="pageInstructions">
			<@s.text name="instruction.delete_inspection_type"/>
		</div>
	<#else>
		<div class="errors">
			<@s.text name="error.can_not_delete_inspection_type.part_of_master"><@s.param>${archiveSummary.inspectionsPartOfMaster}</@s.param></@s.text>
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
				<label class="label"><@s.text name="label.delete_inspection_frequencies"/></label>
				<span class="fieldHolder">${archiveSummary.deleteInspectionFrequencies}</span>
			</div>
			<div class="infoSet">
				<label class="label"><@s.text name="label.delete_notifcation_settings"/></label>
				<span class="fieldHolder">${archiveSummary.notificationsToDelete}</span>
			</div>
		</div>
		<div class="infoBlock">
			<div class="infoSet <#if !archiveSummary.canBeRemoved()>error</#if>">
				<label class="label"><@s.text name="label.delete_part_of_master_inspections"/></label>
				<span class="fieldHolder">${archiveSummary.inspectionsPartOfMaster}</span>
			</div>
			<div class="infoSet ">
				<label class="label"><@s.text name="label.delete_inspections"/></label>
				<span class="fieldHolder">${archiveSummary.deleteInspections}</span>
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