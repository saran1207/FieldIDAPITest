${action.setPageType('event_type_group', 'show')!}
<div class="crudForm bigForm pageSection">
	<h2><@s.text name="label.groupdetails"/> <a href="<@s.url action="eventTypeGroupEdit" uniqueID="${group.id}"/>"><@s.text name="label.littleedit"/></a></h2>
	<div class="sectionContent">
		<div class="infoSet">
			<label for="name"><@s.text name="label.name"/></label>
			<span>${(group.name?html) !}</span>
		</div>
		
		<div class="infoSet">
			<label for="reporttitle"><@s.text name="label.reporttitle"/></label>
			<span>${(group.reportTitle?html) !}</span>
		</div>
		
		<div class="infoSet">
			<label for="reportstyle"><@s.text name="label.reportstyle"/></label>
			<span>${(group.printOut.name?html)!action.getText("label.none")}</span>
		</div>
		<div class="infoSet">
			<label for="observationreportstyle"><@s.text name="label.observationreportstyle"/></label>
			<span>${(group.observationPrintOut.name?html)!action.getText("label.none")}</span>
		</div>
	</div>
</div>

<div class="pageSection">
	<h2><@s.text name="label.eventtypes"/></h2>
	<div class="crudForm bigForm sectionContent">
		
		<#list eventTypes as eventType>
			<div class="infoSet">
				<label class="line" ><a href="<@s.url action="eventType" uniqueID="${eventType.id}"/>">${(eventType.name?html) !}</a></label>
			</div>
		</#list>
		<#if eventTypes.empty >
			<div class="infoSet">
				<label class="line"><@s.text name="label.noeventtypesundergroup"/></label>
			</div>
		</#if>	
		<div class="infoSet">
			<label class="line"><a href="<@s.url action="eventTypeAdd" group="${group.id}"/>"><@s.text name="label.addneweventtype"/></a></label>
		</div>
		 
	</div>
</div>

