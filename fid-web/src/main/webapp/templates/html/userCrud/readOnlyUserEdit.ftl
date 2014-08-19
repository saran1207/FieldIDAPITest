${action.setPageType('user','edit')!}

<#assign userSaveAction='readOnlyUserUpdate' >
<#assign backToList>
<a href="/fieldid/w/setup/usersList" ><@s.text name="label.cancel" /></a>
</#assign>
<#include "_userEditForm.ftl">