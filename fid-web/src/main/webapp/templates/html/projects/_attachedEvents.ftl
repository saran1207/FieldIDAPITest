<div class="event " id="event_${event.id}">
	<span class="projectEventSerial"><a href="<@s.url action="asset" namespace="/" uniqueID="${event.asset.id}"/>">${event.asset.serialNumber?html}</a></span>
	<span class="projectEventType">${(event.asset.type.name?html)!}</span>
	<span class="projectEventType">${(event.eventType.name?html)!}</span>
	<span class="projectEventType">
		<#if event.status.name() == "COMPLETED">
			<@s.text name="label.completed"/> | <a href="<@s.url action="event" uniqueID="${event.event.id}"/>"><@s.text name="label.view"/></a>
		<#else>
			
			<@s.url id="eventUrl" action="selectEventAdd" namespace="/" assetId="${event.asset.id}" type="${event.eventType.id}" scheduleId="${event.id}" />
			
			<span id="inspectNow_${event.id}" <#if event.status.getName() == "IN_PROGRESS">style="display:none"</#if>><@s.text name="label.scheduled"/> <#if sessionUser.hasAccess("createevent")>| <a href='${eventUrl}' ><@s.text name="label.starteventnow"/></a></#if></span>
			<span id="stopProgress_${event.id}" <#if event.status.getName() == "SCHEDULED">style="display:none"</#if> ><@s.text name="label.in_progress"/><#if sessionUser.hasAccess("createevent")> | <a href='javascript:void(0);' onclick="getResponse('<@s.url action="eventScheduleStopProgress" namespace="/ajax" assetId="${event.asset.id}" uniqueID="${event.id}" />'); return false"  ><@s.text name="label.stop"/></a></#if></span>

		</#if>
	</span>
	<#if sessionUser.hasAccess("managejobs" ) >
		<span class="projectEventRemove"><a href="<@s.url action="jobEventDelete" namespace="/ajax" projectId="${project.id}" uniqueID="${event.id}"/>" eventId="${event.id}" class="removeEventLink" onclick="getResponse(this.href, 'post'); return false;"><img alt="x" src="<@s.url value="/images/x.gif"/>" eventId="${event.id}"/></a></span>
	</#if>
</div>
