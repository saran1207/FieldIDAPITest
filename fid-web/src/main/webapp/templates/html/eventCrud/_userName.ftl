<#if !user?exists ><#assign user=event.performedBy /></#if>
<#if event.tenant.id == sessionUser.tenant.id >
	${(user.userLabel)!action.getText("label.unknown")}
<#else>
	<#assign tenant=event.tenant/>
	<#include "../common/_displayTenantLogo.ftl"/> 
</#if>