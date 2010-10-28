${action.setPageType('inspection_type', 'import_export')!}

<#assign labelTarget='inspection' >
<@s.url id="exportExample" action="downloadExampleEventExport" namespace="/file" uniqueID="${uniqueID}" />
<@s.url id="importUrl" action="showImportEvents" uniqueID="${uniqueID}" />

<#include '../importExport/_importExport.ftl' />