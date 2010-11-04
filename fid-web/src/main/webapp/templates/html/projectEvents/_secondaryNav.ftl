<ul class="secondaryNav" >
	<#if !secondaryNavAction?exists || secondaryNavAction != "list">
		<li><a href="<@s.url action="jobEvents" projectId="${projectId}" currentPage="${currentPage!}"/>">&#171; <@s.text name="label.View_all_events"/></a></li>
	<#elseif sessionUser.hasAccess("createevent")>
		<li class="add button"><a href="<@s.url action="startAssignSchedulesToJob" jobId="${project.id}"/>"><@s.text name="label.add_event"/></a></li>
	</#if>
</ul>