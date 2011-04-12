<style type="text/css">
	.useractions{
		padding-bottom: 0;
	}
</style>

<#if !secondaryNavAction?exists || secondaryNavAction != "list">
	<div class="secondaryNav">
		<a href="<@s.url action="divisions"  customerId="${customerId}"/>">&#171; <@s.text name="label.view_all_divisions"/></a>
	</div>
<#else>
	<div class="useractions addicon">
		<p><a href="<@s.url action="divisionAdd" customerId="${customerId}"/>"><@s.text name="label.add_division"/></a></p>
	</div>	
</#if>
