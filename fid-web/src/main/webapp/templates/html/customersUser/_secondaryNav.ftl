<ul class="secondaryNav" >
	<#if !secondaryNavAction?exists || secondaryNavAction != "list">
		<li><a href="<@s.url action="customersUsers" uniqueID=""  includeParams="get"/>">&#171; <@s.text name="label.view_all_users"/></a></li>
	</#if>
</ul>
