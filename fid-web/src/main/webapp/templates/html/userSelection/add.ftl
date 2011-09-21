<#if !userLimitService.readOnlyUsersEnabled && !userLimitService.liteUsersEnabled && !userLimitService.employeeUsersAtMax>
	<script type="text/javascript">
		$$('#contentTitle h1').first().hide();
		redirect('<@s.url namespace="/" action="addEmployeeUser"/>');
	</script> 
<#else>
	<#include "_selectUserType.ftl"/>
</#if>