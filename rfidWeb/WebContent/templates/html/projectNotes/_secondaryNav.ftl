
<ul class="secondaryNav" >
	<#if !secondaryNavAction?exists || secondaryNavAction != "list">
		<li><a href="<@s.url action="jobNotes" projectId="${projectId}" currentPage="${currentPage!}"/>">&#171; <@s.text name="label.view_all_notes"/></a></li>
	<#elseif sessionUser.hasAccess("managejobs")>
		<li class="add button"><a href="<@s.url value="jobNoteAdd.action" includeParams="get"/>" ><@s.text name="label.add_note"/></a></li>
	</#if>
</ul>