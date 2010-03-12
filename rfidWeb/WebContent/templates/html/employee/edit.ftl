${action.setPageType('user','edit')!}

<#assign userSaveAction='employeeUserUpdate' >
<#assign backToList>
	<a href="<@s.url action="userList" currentPage="${currentPage!}" listFilter="${listFilter!}" userType="${userType!}"/>"><@s.text name="label.cancel" /></a>
</#assign>
<#include "../userCrud/_userEditForm.ftl">
