${action.setPageType('user', 'addlite')!}

<head>
	<title><@s.text name="label.addliteuser" /></title>
</head>

<#assign userSaveAction='liteUserCreate' >
<#assign backToList>
	<a href="<@s.url action="userList" currentPage="${currentPage!}" listFilter="${listFilter!}" userType="${userType!}"/>"><@s.text name="label.cancel" /></a>
</#assign>
<#include "_userForm.ftl">