${action.setPageType('user','addemployee')!}
<#assign backToList>
	<a href="<@s.url action="userList" currentPage="${currentPage!}" listFilter="${listFilter!}" userType="${userType!}"/>"><@s.text name="label.cancel" /></a>
</#assign>
<#assign userSaveAction='employeeUserCreate' >
<#include "../userCrud/_userForm.ftl"/>

