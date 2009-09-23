${action.setPageType('user','addemployee')!}

<#assign userSaveAction='employeeUserCreate' >
<#assign backToList>
	<a href="<@s.url action="userList" currentPage="${currentPage!}" listFilter="${listFilter!}" userType="${userType!}"/>"><@s.text name="label.cancel" /></a>
</#assign>
<#if limits.employeeUsersMaxed>
	<div class="limitWarning">
	<@s.text name="label.exceeded_your_employee_user_limit">
		<@s.param>${limits.employeeUsersMax}</@s.param>
	</@s.text>
	</div>
<#else>
	<#include "_form.ftl"/>
</#if>