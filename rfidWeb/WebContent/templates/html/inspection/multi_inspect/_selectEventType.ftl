	
		
<#if multiInspectGroupSorter.group.empty>
	<@s.text name="error.ANOTHER_PROLLEM"/>
<#else>
	<ul id="eventTypes">
		<#list multiInspectGroupSorter.groups as group>
			<li class="group">
				${group.name?html}
				<ul class="types">
					<#list multiInspectGroupSorter.getInspectionTypesForGroup(group) as eventType>
						<li><a href="#" class="eventType" value="${eventType.id}">${eventType.name?html}</a></li>
					</#list>
				</ul>
			</li>
							
		</#list>
	</ul>
</#if>	
	
	


<@s.form action="retrieveInspectionDetails" namespace="/multiInspect/ajax" id="retrieveInspectionDetails">
	<@s.hidden name="type" id="eventTypeId"/>
	
	<#list assetIds as assetId>
		<@s.hidden name="assetIds[${assetId_index}]"/> 
	</#list>
</@s.form>

<@s.text name="label.number_of_assets"><@s.param>${assetIds.size()}</@s.param></@s.text>



<@n4.includeScript>
	function performInspection(event) {
		event.stop();
		
		
		var element = Event.element(event);
		
		$('eventTypeId').value = element.readAttribute('value');
		$('retrieveInspectionDetails').request(getStandardCallbacks());
		toStep(2, "step2Loading");
		
		
	}
	
	
	onDocumentLoad(function() {
		$$('.eventType').each(function(element) {
			element.observe('click', performInspection);
		});
	});
	
</@n4.includeScript>