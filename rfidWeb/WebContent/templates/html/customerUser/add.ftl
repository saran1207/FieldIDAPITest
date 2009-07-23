	
${action.setPageType('customer', 'users')!}
		
<#include "_secondaryNav.ftl"/>
<#assign userSaveAction='customerUserCreate' >
<#assign backToList>
	<a href="<@s.url action="customerUsers" currentPage="${currentPage!}" uniqueID="" includeParams="get"/>"><@s.text name="label.cancel" /></a>
</#assign>
<#include "../userCrud/_userForm.ftl">