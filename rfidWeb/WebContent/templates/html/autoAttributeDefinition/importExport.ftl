${action.setPageType('auto_attribute', 'import_export')!}

<@s.url id="exportExcel" action="customerExport" namespace="/aHtml" exportType="excel" criteriaId="${criteriaId}"/>
<@s.url id="exportCsv" action="customerExport" namespace="/aHtml" exportType="csv" criteriaId="${criteriaId}"/>
<@s.url id="exportExample" action="downloadExampleCustomerExport" namespace="/file" />
<@s.url id="importUrl" action="showImportCustomers"/>

<#include '../importExport/_importExport.ftl' />