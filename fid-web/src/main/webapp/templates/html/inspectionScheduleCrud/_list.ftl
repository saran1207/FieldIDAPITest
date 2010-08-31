<head>
	<#include "/templates/html/common/_calendar.ftl"/>
	<script type="text/javascript" src="javascript/inspectionSchedule.js"></script>
	
</head>

<#if !inVendorContext && action.sessionUser.hasAccess("createinspection")>
	<#include "_addForm.ftl"/>
</#if>

<table class="list" id="scheduleList" <#if inspectionSchedules?exists && inspectionSchedules.isEmpty() >style="display:none"</#if>>
	<tr>
		<th class="rowName"><@s.text name="label.inspectiontype"/></th>
		<th><@s.text name="label.nextscheduleddate"/></th>
	</tr>
	<tbody id="schedules">
		<#list inspectionSchedules as inspectionSchedule>
			<#include "show.ftl"/>
		</#list>
	</tbody>
</table>

<#if !inVendorContext>
	<script type="text/javascript" >
		editScheduleUrl =  '<@s.url action="inspectionScheduleEdit" namespace="/ajax" />';
		cancelScheduleUrl = '<@s.url action="inspectionScheduleShow" namespace="/ajax"/>';
		removeScheduleUrl = '<@s.url action="inspectionScheduleDelete" namespace="/ajax"/>';
	</script>
</#if>