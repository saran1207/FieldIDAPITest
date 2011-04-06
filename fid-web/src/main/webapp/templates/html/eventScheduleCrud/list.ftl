<head>
	<#include "/templates/html/common/_calendar.ftl"/>
	<script type="text/javascript" src="javascript/eventSchedule.js"></script>
</head>

<#if action.sessionUser.hasAccess("createevent")>
	<#include "_addForm.ftl"/>
</#if>

<#if eventSchedules?exists && eventSchedules.isEmpty() >

<#else>
	<table class="list" id="scheduleList" >
		<tr>
			<th><@s.text name="label.schedule_date"/></th>
			<th><@s.text name="label.eventtype"/></th>
			<th><@s.text name="label.job"/></th>
			<th></th>
		</tr>
		<tbody id="schedules">
			<#list eventSchedules as eventSchedule>
				<#include "show.ftl"/>
			</#list>
		</tbody>
	</table>
</#if>
<#if !inVendorContext>
	<script type="text/javascript" >
		editScheduleUrl =  '<@s.url action="eventScheduleEdit" namespace="/ajax" />';
		cancelScheduleUrl = '<@s.url action="eventScheduleShow" namespace="/ajax"/>';
		removeScheduleUrl = '<@s.url action="eventScheduleDelete" namespace="/ajax"/>';
	</script>
</#if>
