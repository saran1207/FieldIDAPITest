${action.setPageType('customer', 'import_export')!}

<#assign labelTarget='customer' >
<@s.url id="exportExcel" action="customerExport" namespace="/aHtml" />
<@s.url id="exportExample" action="downloadExampleCustomerExport" namespace="/file" />
<@s.url id="importUrl" action="showImportCustomers"/>

<#include '../importExport/_importExport.ftl' />