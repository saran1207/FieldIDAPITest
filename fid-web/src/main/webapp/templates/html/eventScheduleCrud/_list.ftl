<table class="list" id="scheduleList" <#if eventSchedules?exists && eventSchedules.isEmpty() >style="display:none"</#if>>
	<tr>
		<th><@s.text name="label.schedule_date"/></th>
		<th><@s.text name="label.eventtype"/></th>
		<#if securityGuard.projectsEnabled>
			<th><@s.text name="label.job"/></th>
		</#if>
		<th></th>
	</tr>
	<tbody id="schedules">
		<#list eventSchedules as eventSchedule>
			<#include "show.ftl"/>
		</#list>
	</tbody>
</table>