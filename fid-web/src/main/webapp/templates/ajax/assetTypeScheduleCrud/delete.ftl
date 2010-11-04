<#escape x as x?j_string >
	var container = null;
	
	<#if schedule.override>
		<#assign containerId="eventFrequencyOverride_${eventTypeId}_${schedule.owner.id}"/>
		container = $('${containerId}');
		if( container != null ) {
			container.remove();
		}
		
	<#else>
		
		<#assign containerId="eventFrequency_${eventTypeId}" />
		container = $('${containerId}');
		$('eventFrequencyOverrides_${eventTypeId}_container').hide();
		removeChildren( $('eventFrequencyOverrides_${eventTypeId}') );
		
		${schedule.setId(null)!}
		<#assign html>
			<#include "/templates/html/assetTypeScheduleCrud/_show.ftl" >
		</#assign>
		container.update("${html}");
	</#if>

	
</#escape>