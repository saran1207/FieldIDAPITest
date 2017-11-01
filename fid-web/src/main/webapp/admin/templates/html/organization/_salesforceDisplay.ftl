<label class="bold">Salesforce ID: </label>${tenant.salesforceId}
<#if superUser>
|
<a href="javascript:void(0);" onClick="editSalesforceId(${id});">
    <@s.text name="label.edit"/>
</a>
</#if>
