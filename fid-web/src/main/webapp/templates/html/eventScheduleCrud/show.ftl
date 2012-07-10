<tr id="type_${eventSchedule.id}">
	<td id="nextDate_${eventSchedule.id}">
		
	<#include "_show.ftl"/>
		
	</td>

	<td>${eventSchedule.type.name}</td>

	<#if securityGuard.projectsEnabled>
		<td id="jobName_${eventSchedule.id}">
			<#include "_showJob.ftl"/>
		</td>
	</#if>
		
	<td>
		<#if sessionUser.hasAccess( "createevent" )>
			<span id="scheduleActions_${eventSchedule.id}">
				<a id="startEventNow_${eventSchedule.id}" href='<@s.url action="selectEventAdd" namespace="/" assetId="${assetId}" type="${eventSchedule.type.id}" scheduleId="${eventSchedule.id}" />'><@s.text name="label.start_this_event"/></a> |
				<a id="edit_${eventSchedule.id}" href="javascript:void(0);" onclick="editSchedule( ${eventSchedule.eventType.id}, ${assetId}, ${(eventSchedule.id)!"null"}); return false;" ><@s.text name="label.edit" /></a> |
				<a id="delete_${eventSchedule.id}" href="javascript:void(0);" onclick="removeSchedule( ${eventSchedule.eventType.id}, ${assetId}, ${(eventSchedule.id)!"null"}); return false; " ><@s.text name="label.remove" /></a> 
			</span>
		</#if>	
	</td>
</tr>