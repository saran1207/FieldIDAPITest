${action.setPageType('event_type', 'import_export')!}

<#assign labelTarget='event' >
<#assign importAction='importEvents' >
<@s.url id="updateUrl" namespace="/ajax" action="eventImportStatus" />
<@s.url id="backToUrl" action="eventImportExport" uniqueID="${uniqueID}"/>

<#macro hiddenFields>
	<@s.hidden name="uniqueID" />
</#macro>


<#include '../importExport/_import.ftl' />
