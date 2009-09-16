<title>
	<#if uniqueID?exists >
		${action.setPageType('user','edit')!}
	<#else>
		${action.setPageType('user','addcustomer')!}
	</#if>
	
</title>

<#assign userSaveAction='customerUserSave' >
<#assign employee=false>
<#assign backToList>
	<a href="<@s.url action="customerShow" currentPage="${currentPage!}" listFilter="${listFilter!}" uniqueID="${customer}"/>"><@s.text name="label.cancel" /></a>
</#assign>

<#include "_userForm.ftl" />
