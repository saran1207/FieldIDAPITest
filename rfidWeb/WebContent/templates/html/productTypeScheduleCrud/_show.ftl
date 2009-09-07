<#if (schedule.id)?exists && schedule.owner.customer >
	<span class="customer">${schedule.owner.name}</span> <@s.text name="label.every" />
</#if>
<span class="frequency">
	<#if (schedule.id)?exists>
		${(schedule.frequency)!} <@s.text name="label.days"/>, ${schedule.autoSchedule?string( action.getText( "label.autoscheduled" ), "" )}
	<#else>
		<@s.text name="label.notscheduled"/>
	</#if> 
	
	
</span>
<span class="actions">
	<a href="javascript:void(0);" onclick="editSchedule( ${inspectionType.id}, ${productType.uniqueID}, ${(schedule.id)!"null"} ); return false;" ><@s.text name="label.edit" /></a>
	<#if (schedule.id)?exists > 
		| <a href="javascript:void(0);" onclick="removeSchedule( ${inspectionType.id}, ${productType.uniqueID}, ${(schedule.id)!"null"}, ${(!(schedule.customer)?exists)?string} ); " ><@s.text name="label.remove" /></a>
	</#if>	
</span>
