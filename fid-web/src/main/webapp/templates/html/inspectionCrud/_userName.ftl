<#if !user?exists ><#assign user=inspection.performedBy /></#if>
<#if inspection.tenant.id == sessionUser.tenant.id >
	${(user.userLabel)!action.getText("label.unknown")}
<#else>
	<#assign tenant=inspection.tenant/>
	<#include "../common/_displayTenantLogo.ftl"/> 
</#if>