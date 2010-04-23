${action.setPageType('customer', 'import_export')!}

<#assign labelTarget='customer' >
<#assign importAction='importCustomers' >
<@s.url id="updateUrl" namespace="/ajax" action="customerImportStatus" />
<@s.url id="backToUrl" action="customerImportExport"/>

<#macro hiddenFields>
</#macro>

<#include '../importExport/_import.ftl' />