<title>
	<#if uniqueID?exists >
		${action.setPageType('user','edit')!}
		
	<#else>
		${action.setPageType('user','addemployee')!}
	</#if>
	
</title>

<#assign userSaveAction='userSave' >
<#assign backToList>
	<a href="<@s.url action="userList" currentPage="${currentPage!}" listFilter="${listFilter!}" userType="${userType!}"/>"><@s.text name="label.cancel" /></a>
</#assign>

<#include "_userForm.ftl">