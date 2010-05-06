${action.setPageType('inspection', 'add')!}

<#if !eventTypes?exists || eventTypes.empty>
	<@s.text name="error.no_common_inspection_types"/>
<#else>
	<#list eventTypes as eventType>
		<a href="#" class="eventType" value="${eventType.id}">${eventType.name?html}</a>
	</#list>
</#if>
<@s.form action="performEvent" id="perfromEvent">
	<@s.hidden name="eventTypeId" id="eventTypeId"/>
	
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
		$('perfromEvent').submit();
	}
	
	
	onDocumentLoad(function() {
		$$('.eventType').each(function(element) {
			element.observe('click', performInspection);
		});
	});
	
</@n4.includeScript>