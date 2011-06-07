<#escape x as x?j_string >
<#if schedule.new>
	<#assign schedule=action.newSchedule() />
</#if>

<#assign html>
		<#if schedule.override>
			<#include "/templates/html/assetTypeScheduleCrud/_showOverrides.ftl" >
		<#else>
			<#include "/templates/html/assetTypeScheduleCrud/_show.ftl" >
		</#if>
</#assign>

var container = null;
<#if !schedule.new && schedule.override>
	<#assign containerId="eventFrequencyOverride_${eventTypeId}_${schedule.owner.id}"/>
	container = $('${containerId}');
	<#if schedule.id?exists >
		if( container == null ) {
			container = new Element('div', { id: '${containerId}', 'class':'override customerOverride' } );
			$('eventFrequencyOverrides_${eventTypeId}').insert(container);
			container = $('${containerId}');
		}
	<#else>
		if( container != null ) {
			container.remove();
		}
	</#if>
<#else>
	<#assign containerId="eventFrequency_${eventTypeId}" />
	container = $('${containerId}');
	<#if schedule?exists && schedule.id?exists>
		$('eventFrequencyOverrides_${eventTypeId}_container').show();
	<#else>
		$('eventFrequencyOverrides_${eventTypeId}_container').hide();
		removeChildren( $('eventFrequencyOverrides_${eventTypeId}') );
	</#if>
</#if>
	
	container.update("${html}");
</#escape>