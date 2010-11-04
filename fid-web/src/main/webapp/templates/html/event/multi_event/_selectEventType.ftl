	
		
<#if multiEventGroupSorter.groups.empty>
	<@s.text name="error.no_common_event_types"/>  <a href="<@s.url action="assetSelection" namespace="/"/>"><@s.text name="label.select_a_new_set_of_assets"/></a>
<#else>

	<ul id="eventTypes" class="groupEventTypeList">
		<#list multiEventGroupSorter.groups as group>
			<li class="group">
				<h3>${group.name?html}</h3>
				<ul class="types">
					<#list multiEventGroupSorter.getEventTypesForGroup(group) as eventType>
			
						<li><a href="#" class="eventType" value="${eventType.id}">${eventType.name?html}</a></li>
					</#list>
				</ul>
			</li>
		</#list>
	</ul>
</#if>	
	
<@s.form action="retrieveEventDetails" namespace="/multiEvent/ajax" id="retrieveEventDetails">
	<@s.hidden name="type" id="eventTypeId"/>
	
	<#list assetIds as assetId>
		<@s.hidden name="assetIds[${assetId_index}]"/> 
	</#list>
</@s.form>




<@n4.includeScript>
	function performEvent(event) {
		event.stop();
		
		var element = Event.element(event);
		
		$('eventTypeId').value = element.readAttribute('value');
		$('retrieveEventDetails').request(getStandardCallbacks());
		toStep(2, "step2Loading");
	}
	
	
	onDocumentLoad(function() {
		$$('.eventType').each(function(element) {
			element.observe('click', performEvent);
		});
	});
	
</@n4.includeScript>