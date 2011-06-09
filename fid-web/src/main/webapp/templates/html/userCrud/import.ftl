${action.setPageType('user', 'import_export')!}

<#assign labelTarget='user' >
<#assign importAction='importUsers' >
<@s.url id="updateUrl" namespace="/ajax" action="userImportStatus" />
<@s.url id="backToUrl" action="userImportExport"/>

<#macro hiddenFields>
</#macro>

<#include '../importExport/_import.ftl' />