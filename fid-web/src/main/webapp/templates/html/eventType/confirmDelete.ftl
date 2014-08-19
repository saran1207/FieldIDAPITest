${action.setPageType('event_type', 'edit')!}
<head>
    <@n4.includeStyle href="/style/legacy/newCss/component/buttons.css" type="page"/>
</head>

<@s.form action="eventTypeDelete" id="mainContent" cssClass="fullForm fluidSets" theme="fieldid">
	<h2><@s.text name="label.delete_summary"/></h2>
    <div class="pageInstructions">
        <@s.text name="instruction.delete_event_type"/>
    </div>

	<@s.hidden name="uniqueID"/>
	<div class="multiColumn">
		<div class="infoBlock">
            <#if archiveSummary.thingEventType>
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
            </#if>
            <#if archiveSummary.thingEventType || archiveSummary.actionEventType>
                <div class="infoSet">
                    <label class="label"><@s.text name="label.savedreports"/></label>
                    <span class="fieldHolder">${archiveSummary.savedReportsToDelete}</span>
                </div>
            </#if>
            <#if archiveSummary.placeEventType>
                <div class="infoSet">
                    <label class="label"><@s.text name="label.remove_from_places"/></label>
                    <span class="fieldHolder">${archiveSummary.removeFromPlaces}</span>
                </div>
                <div class="infoSet">
                    <label class="label"><@s.text name="label.delete_place_recurrences"/></label>
                    <span class="fieldHolder">${archiveSummary.recurringPlaceEventsToDelete}</span>
                </div>
            </#if>
            <#if archiveSummary.procedureAuditEventType>
                <div class="infoSet">
                    <label class="label"><@s.text name="label.delete_procedure_audit_reccurrences"/></label>
                    <span class="fieldHolder">${archiveSummary.recurringProcedureAuditEvents}</span>
                </div>
            </#if>
		</div>
		<div class="infoBlock">
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
	
	<div class="actions borderLess">
        <@s.submit key="label.delete"/> <@s.text name="label.or"/>
		<a href="<@s.url action="eventTypeEdit" uniqueID="${uniqueID}"/>" ><@s.text name="label.cancel"/></a>
	</div>
</@s.form>
