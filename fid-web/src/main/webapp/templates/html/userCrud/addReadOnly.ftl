${action.setPageType('user', 'addreadonly')!}

<head>
	<title><@s.text name="label.addreadonly" /></title>
</head>

<#assign userSaveAction='readOnlyUserCreate' >
<#assign backToList>
	<a href="<@s.url action="userList" currentPage="${currentPage!}" listFilter="${listFilter!}" userType="${userType!}"/>"><@s.text name="label.cancel" /></a>
</#assign>
<#include "_userForm.ftl">