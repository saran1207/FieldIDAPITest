${action.setPageType('auto_attribute', 'import_export')!}

<@s.url id="exportExcel" action="autoAttributeExport" namespace="/aHtml" exportType="excel" criteriaId="${criteriaId}"/>
<@s.url id="exportCsv" action="autoAttributeExport" namespace="/aHtml" exportType="csv" criteriaId="${criteriaId}"/>
<@s.url id="exportExample" action="downloadExampleAutoAttributeExport" namespace="/file" />
<@s.url id="importUrl" action="showImportAutoAttributes" criteriaId="${criteriaId}"/>

<#include '../importExport/_importExport.ftl' />