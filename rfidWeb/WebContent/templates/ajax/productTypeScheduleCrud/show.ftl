<#escape x as x?j_string >
<#assign html>
	<#include "/templates/html/productTypeScheduleCrud/_show.ftl" >
</#assign>

<#if schedule.customer?exists>
	<#assign containerId="eventFrequencyOverride_${inspectionTypeId}_${schedule.customer.id}"/>
<#else>
	<#assign containerId="eventFrequency_${inspectionTypeId}" />
</#if>

	var container = $('${containerId}');
	
	<#if schedule.customer?exists >
		<#if schedule.id?exists >
			if( container == null ) {
				container = new Element('div', { id: '${containerId}', 'class':'override customerOverride' } );
				$('eventFrequencyOverrides_${inspectionTypeId}').insert(container);
				container = $('${containerId}');
			}
		<#else>
			if( container != null ) {
				container.remove();
			}
		</#if>
		
	<#else> 
		<#if schedule?exists && schedule.id?exists>
			$('eventFrequencyOverrides_${inspectionTypeId}_container').show();
		<#else>
			$('eventFrequencyOverrides_${inspectionTypeId}_container').hide();
			removeChildren( $('eventFrequencyOverrides_${inspectionTypeId}') );
		</#if>
	</#if>
	
	container.innerHTML = "${html}";
</#escape>