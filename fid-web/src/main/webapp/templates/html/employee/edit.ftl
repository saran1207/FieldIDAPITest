${action.setPageType('user','edit')!}

<#assign userSaveAction='employeeUserUpdate' >
<#assign backToList>
<a href="/fieldid/w/setup/usersList" ><@s.text name="label.cancel" /></a>
</#assign>
<#include "../userCrud/_userEditForm.ftl">
