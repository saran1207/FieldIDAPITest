	
${action.setPageType('customer', 'users')!}
		
<#include "_secondaryNav.ftl"/>
<#assign userSaveAction='customersUserCreate' >
<#assign backToList>
	<a href="<@s.url action="customersUsers" currentPage="${currentPage!}" uniqueID="" includeParams="get"/>"><@s.text name="label.cancel" /></a>
</#assign>
<#include "../userCrud/_userForm.ftl">