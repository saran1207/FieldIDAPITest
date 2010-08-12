<span class="customer">
	<#if (schedule.id)?exists && schedule.override >
		 <@s.text name="label.capital_for" /><b> <@s.text name=" ${schedule.owner.name}" /> </b> 
	</#if>
</span>
<span class="frequency">
	<#if (schedule.id)?exists>
		 <@s.text name="label.capital_schedule_a"/> <b> ${inspectionType.name}</b> <@s.text name="label.every"/> <b>${(schedule.frequency)!}</b> <@s.text name="label.days"/>
	<#else>
		<@s.text name="label.notscheduled"/>
	</#if> 
</span>
<span class="actions">
	<a href="#edit_schedule" onclick="editSchedule(${inspectionType.id}, ${productType.uniqueID}, ${(schedule.id)!"null"}, ${(schedule.owner.id)!"null"} ); return false;" ><@s.text name="label.edit" /></a>
	<#if (schedule.id)?exists > 
		| <a href="#remove_schedule" onclick="removeSchedule( ${inspectionType.id}, ${productType.uniqueID}, ${(schedule.id)!"null"}, ${(!(schedule.customer)?exists)?string} ); " ><@s.text name="label.remove" /></a>
	</#if>	
</span>
