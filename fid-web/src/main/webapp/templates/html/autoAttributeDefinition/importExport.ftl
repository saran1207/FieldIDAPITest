${action.setPageType('auto_attribute', 'import_export')!}

<#assign labelTarget='autoattrib' >
<@s.url id="exportExcel" action="autoAttributeExport" namespace="/aHtml" criteriaId="${criteriaId}"/>
<@s.url id="exportExample" action="downloadExampleAutoAttributeExport" namespace="/file" criteriaId="${criteriaId}"/>
<@s.url id="importUrl" action="showImportAutoAttributes" criteriaId="${criteriaId}"/>

<#include '../importExport/_importExport.ftl' />