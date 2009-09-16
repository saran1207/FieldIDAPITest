<title>
	<#if uniqueID?exists >
		${action.setPageType('user','edit')!}
		
	<#else>
		${action.setPageType('user','addemployee')!}
	</#if>
	
</title>

<#assign userSaveAction='userSave' >
<#assign employee=true/>
<#assign backToList>
	<a href="<@s.url action="userList" currentPage="${currentPage!}" listFilter="${listFilter!}" userType="${userType!}"/>"><@s.text name="label.cancel" /></a>
</#assign>
<#if !(uniqueID?exists) && limits.employeeUsersMaxed>
	<div class="limitWarning">
	<@s.text name="label.exceeded_your_employee_user_limit">
		<@s.param>${limits.employeeUsersMax}</@s.param>
	</@s.text>
	</div>
<#else>
	<#include "_userForm.ftl">
</#if>
