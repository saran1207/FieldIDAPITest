<#if !user?exists ><#assign user=inspection.inspector /></#if>
<#if inspection.tenant.id == Session.sessionUser.tenant.id >
	${(user.userLabel)!action.getText("label.unknown")}
<#else>
	<#assign tenant=inspection.tenant/>
	<#include "../common/_displayTenantLogo.ftl"/> 
</#if>