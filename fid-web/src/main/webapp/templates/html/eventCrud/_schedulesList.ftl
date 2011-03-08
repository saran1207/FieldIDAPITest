<div id="schedulesList"> 
	<div id="schedules">
		<#if nextSchedules?exists && !nextSchedules.empty>
			<#list nextSchedules as nextSchedule>
				<#if nextSchedule?exists>
					<#assign index=nextSchedule_index/>
					<#include "_singleSchedule.ftl"/>
				</#if>
			</#list>
		</#if>
	</div>
	<div id="emptySchedules">
		<div class="fieldHolder">
			<@s.text name="label.no_schedules_have_been_created"/>
		</div>
	</div>
</div>