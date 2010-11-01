${action.setPageType('event_type_group', 'edit')!}
<div class="pageSection crudForm bigForm" >
	<h2><@s.text name="label.reasonfornotdeleteing"/></h2>
	<div class="sectionContent">
		<div class="infoSet">
			
			<label class="line">
				<#if action.canBeDeleted(group)>
					<@s.text name="label.has_archived_event_types_contact_support"/>
				<#else>
					<@s.text name="label.cannothaveanyeventtypes"/>
				</#if>
			</label>
		</div>
	</div>
	<div class="formAction">
		<@s.url id="showUrl" action="eventTypeGroup" uniqueID="${uniqueID}"/>
		<@s.submit key="label.ok" onclick="return redirect('${showUrl}')"/>
	</div>
</div>
