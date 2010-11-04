${action.setPageType('event_type', 'import_export')!}

<#assign labelTarget='event' >
<@s.url id="exportExample" action="downloadExampleEventExport" namespace="/file" uniqueID="${uniqueID}" />
<@s.url id="importUrl" action="showImportEvents" uniqueID="${uniqueID}" />

<#include '../importExport/_importExport.ftl' />
