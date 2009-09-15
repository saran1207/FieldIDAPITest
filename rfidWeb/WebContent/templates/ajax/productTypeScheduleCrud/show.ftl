<#escape x as x?j_string >
<#assign html>
	<#include "/templates/html/productTypeScheduleCrud/_show.ftl" >
</#assign>
var container = null;
<#if schedule.override>
	<#assign containerId="eventFrequencyOverride_${inspectionTypeId}_${schedule.owner.id}"/>
	container = $('${containerId}');
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
	<#assign containerId="eventFrequency_${inspectionTypeId}" />
	container = $('${containerId}');
	<#if schedule?exists && schedule.id?exists>
		$('eventFrequencyOverrides_${inspectionTypeId}_container').show();
	<#else>
		$('eventFrequencyOverrides_${inspectionTypeId}_container').hide();
		removeChildren( $('eventFrequencyOverrides_${inspectionTypeId}') );
	</#if>
</#if>

	
	container.update("${html}");
</#escape>