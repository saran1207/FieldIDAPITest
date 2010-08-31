<div class="event " id="event_${event.id}">
	<span class="projectEventSerial"><a href="<@s.url action="product" namespace="/" uniqueID="${event.product.id}"/>">${event.product.serialNumber?html}</a></span>
	<span class="projectEventType">${(event.product.type.name?html)!}</span>
	<span class="projectEventType">${(event.inspectionType.name?html)!}</span>
	<span class="projectEventType">
		<#if event.status.name() == "COMPLETED">
			<@s.text name="label.completed"/> | <a href="<@s.url action="inspection" uniqueID="${event.inspection.id}"/>"><@s.text name="label.view"/></a>
		<#else>
			
			<@s.url id="inspectUrl" action="selectInspectionAdd" namespace="/" productId="${event.product.id}" type="${event.inspectionType.id}" scheduleId="${event.id}" />
			
			<span id="inspectNow_${event.id}" <#if event.status.getName() == "IN_PROGRESS">style="display:none"</#if>><@s.text name="label.scheduled"/> <#if sessionUser.hasAccess("createinspection")>| <a href='${inspectUrl}' ><@s.text name="label.inspect"/></a></#if></span>
			<span id="stopProgress_${event.id}" <#if event.status.getName() == "SCHEDULED">style="display:none"</#if> ><@s.text name="label.in_progress"/><#if sessionUser.hasAccess("createinspection")> | <a href='javascript:void(0);' onclick="getResponse('<@s.url action="inspectionScheduleStopProgress" namespace="/ajax" productId="${event.product.id}" uniqueID="${event.id}" />'); return false"  ><@s.text name="label.stop"/></a></#if></span>

		</#if>
	</span>
	<#if sessionUser.hasAccess("managejobs" ) >
		<span class="projectEventRemove"><a href="<@s.url action="jobEventDelete" namespace="/ajax" projectId="${project.id}" uniqueID="${event.id}"/>" eventId="${event.id}" class="removeEventLink" onclick="getResponse(this.href, 'post'); return false;"><img alt="x" src="<@s.url value="/images/x.gif"/>" eventId="${event.id}"/></a></span>
	</#if>
</div>