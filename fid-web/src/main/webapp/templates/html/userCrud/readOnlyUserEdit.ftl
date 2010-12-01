${action.setPageType('user','edit')!}

<#assign userSaveAction='readOnlyUserUpdate' >
<#assign backToList>
	<a href="<@s.url action="userList" currentPage="${currentPage!}" listFilter="${listFilter!}" userType="${userType!}"/>"><@s.text name="label.cancel" /></a>
</#assign>
<#include "_userEditForm.ftl">