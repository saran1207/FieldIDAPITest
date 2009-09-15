<#escape x as x?j_string >
	var container = null;
	
	<#if schedule.override>
		<#assign containerId="eventFrequencyOverride_${inspectionTypeId}_${schedule.owner.id}"/>
		container = $('${containerId}');
		if( container != null ) {
			container.remove();
		}
		
	<#else>
		
		<#assign containerId="eventFrequency_${inspectionTypeId}" />
		container = $('${containerId}');
		$('eventFrequencyOverrides_${inspectionTypeId}_container').hide();
		removeChildren( $('eventFrequencyOverrides_${inspectionTypeId}') );
		
		${schedule.setId(null)!}
		<#assign html>
			<#include "/templates/html/productTypeScheduleCrud/_show.ftl" >
		</#assign>
		container.update("${html}");
	</#if>

	
</#escape>