<ul class="secondaryNav" >
	<#if !secondaryNavAction?exists || secondaryNavAction != "list">
		<li><a href="<@s.url action="customerUsers" uniqueID=""  includeParams="get"/>">&#171; <@s.text name="label.view_all_users"/></a></li>
	<#else>
		<li class="add button"><a href="<@s.url action="customerUserAdd" uniqueID="" includeParams="get"/>" ><@s.text name="label.add_user" /></a></li>
	</#if>
</ul>
