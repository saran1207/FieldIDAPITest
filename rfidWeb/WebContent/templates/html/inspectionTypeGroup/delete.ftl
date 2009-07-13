${action.setPageType('event_type_group', 'edit')!}
<div class="pageSection crudForm bigForm" >
	<h2><@s.text name="label.reasonfornotdeleteing"/></h2>
	<div class="sectionContent">
		<div class="infoSet">
			<label class="line"><@s.text name="label.cannothaveanyinspectiontypes"/></label>
		</div>
	</div>
	<div class="formAction">
		<@s.url id="showUrl" action="eventTypeGroup" uniqueID="${uniqueID}"/>
		<@s.submit key="label.ok" onclick="return redirect('${showUrl}')"/>
	</div>
</div>
