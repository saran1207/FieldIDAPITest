<label class="bold">Company ID: </label>${tenant.name}
<#if superUser>
| 
<a href="javascript:void(0);" onClick="editName(${id});">
	<@s.text name="label.edit"/>
</a>
</#if>
