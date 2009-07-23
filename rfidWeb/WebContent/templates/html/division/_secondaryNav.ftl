<ul class="secondaryNav" >
	<#if !secondaryNavAction?exists || secondaryNavAction != "list">
		<li><a href="<@s.url action="divisions" uniqueID=""  includeParams="get"/>">&#171; <@s.text name="label.view_all_divisions"/></a></li>
	<#else>
		<li class="add button"><a href="<@s.url action="divisionAdd" uniqueID=""  includeParams="get"/>" ><@s.text name="label.add_division" /></a></li>
	</#if>
</ul>
