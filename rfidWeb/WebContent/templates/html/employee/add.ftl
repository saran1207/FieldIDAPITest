${action.setPageType('user','addemployee')!}

<#assign userSaveAction='employeeUserCreate' >
<#assign backToList>
	<a href="<@s.url action="userList" currentPage="${currentPage!}" listFilter="${listFilter!}" userType="${userType!}"/>"><@s.text name="label.cancel" /></a>
</#assign>
<#include "_form.ftl"/>
