${action.setPageType('user', 'addreadonly')!}

<head>
	<title><@s.text name="label.addreadonly" /></title>
</head>

<#assign userSaveAction='readOnlyUserCreate' >
<#assign backToList>
<a href="/fieldid/w/setup/usersList" ><@s.text name="label.cancel" /></a>
</#assign>
<#include "_userForm.ftl">