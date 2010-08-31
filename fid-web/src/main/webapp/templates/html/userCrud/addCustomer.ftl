${action.setPageType('user', 'addcustomer')!}

<#assign userSaveAction='customerUserCreate' >
<#assign backToList>
	<a href="<@s.url action="userList" currentPage="${currentPage!}" listFilter="${listFilter!}" userType="${userType!}"/>"><@s.text name="label.cancel" /></a>
</#assign>
<#include "_userForm.ftl">