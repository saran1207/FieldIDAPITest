${action.setPageType('event_type', 'import_export')!}

<#assign labelTarget='event' >
<@s.url id="exportExampleMinimal" action="downloadExampleEventExport" namespace="/file" uniqueID="${uniqueID}" includeRecommendationsAndDeficiencies="false"/>
<@s.url id="exportExample" action="downloadExampleEventExport" namespace="/file" uniqueID="${uniqueID}" includeRecommendationsAndDeficiencies="true"/>
<@s.url id="importUrl" action="showImportEvents" uniqueID="${uniqueID}" />

<#include '../importExport/_importExport.ftl' />
