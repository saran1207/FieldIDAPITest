<ul class="secondaryNav" >
	<#if !secondaryNavAction?exists || secondaryNavAction != "list">
		<li><a href="<@s.url action="autoAttributeDefinitionList" criteriaId="${criteriaId}" currentPage="${currentPage!}"/>">&#171; <@s.text name="label.view_all_definitions"/></a></li>
	<#else>
		<li class="add button"><a href="<@s.url value="autoAttributeDefinitionEdit.action" criteriaId="${criteriaId}"/>"><@s.text name="label.add_definition" /></a></li>
	</#if>
</ul>
