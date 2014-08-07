${action.setPageType('user', 'addlite')!}

<head>
	<title><@s.text name="label.addliteuser" /></title>
</head>

<#assign userSaveAction='liteUserCreate' >
<#assign backToList>
<a href="/fieldid/w/setup/usersList" ><@s.text name="label.cancel" /></a>
</#assign>
<#include "_userForm.ftl">