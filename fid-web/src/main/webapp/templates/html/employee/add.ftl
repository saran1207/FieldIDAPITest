${action.setPageType('user','addemployee')!}

<head>
	<title><@s.text name="label.addemployee" /></title>
</head>

<#assign backToList>
<a href="/fieldid/w/setup/usersList" ><@s.text name="label.cancel" /></a>
</#assign>
<#assign userSaveAction='employeeUserCreate' >
<#include "../userCrud/_userForm.ftl"/>

