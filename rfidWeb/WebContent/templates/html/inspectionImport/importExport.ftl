${action.setPageType('inspection_type', 'import_export')!}

<@s.url id="exportExample" action="downloadExampleInspectionExport" namespace="/file" uniqueID="${uniqueID}" />
<@s.url id="importUrl" action="showImportInspections" uniqueID="${uniqueID}" />

<#include '../importExport/_importExport.ftl' />