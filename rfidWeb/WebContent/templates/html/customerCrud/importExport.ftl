${action.setPageType('customer', 'import_export')!}

<@s.url id="exportExcel" action="customerExport" namespace="/aHtml" exportType="excel" />
<@s.url id="exportCsv" action="customerExport" namespace="/aHtml" exportType="csv" />
<@s.url id="exportExample" action="downloadExampleCustomerExport" namespace="/file" />
<@s.url id="importUrl" action="showImportCustomers"/>

<#include '../importExport/_importExport.ftl' />