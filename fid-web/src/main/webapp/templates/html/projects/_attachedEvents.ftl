<div class="event " id="event_${event.id}">
	<span class="projectEventIdentifier"><a href="/fieldid/w/assetSummary?uniqueID=${event.asset.id}"/>${event.asset.identifier?html}</a></span>
	<span class="projectEventType">${(event.asset.type.name?html)!}</span>
	<span class="projectEventType">${(event.eventType.name?html)!}</span>
	<span class="projectEventType">
		<#if event.eventState.name() == "COMPLETED">
			<@s.text name="label.completed"/> | <a href="<@s.url action="event" uniqueID="${event.id}"/>"><@s.text name="label.view"/></a>
		<#else>
			
			<@s.url id="eventUrl" action="selectEventAdd" namespace="/" assetId="${event.asset.id}" type="${event.eventType.id}" scheduleId="${event.id}" />
			
			<span id="startEventNow_${event.id}"><@s.text name="label.scheduled"/> <#if sessionUser.hasAccess("createevent")>| <a href='${eventUrl}' ><@s.text name="label.starteventnow"/></a></#if></span>

		</#if>
	</span>
	<#if sessionUser.hasAccess("managejobs" ) >
		<span class="projectEventRemove"><a href="<@s.url action="jobEventDelete" namespace="/ajax" projectId="${project.id}" uniqueID="${event.id}"/>" eventId="${event.id}" class="removeEventLink" onclick="getResponse(this.href, 'post'); return false;"><img alt="x" src="<@s.url value="/images/x.gif"/>" eventId="${event.id}"/></a></span>
	</#if>
</div>
