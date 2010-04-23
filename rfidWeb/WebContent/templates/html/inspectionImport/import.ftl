${action.setPageType('inspection_type', 'import_export')!}

<#assign labelTarget='inspection' >
<#assign importAction='importInspections' >
<@s.url id="updateUrl" namespace="/ajax" action="inspectionImportStatus" />
<@s.url id="backToUrl" action="inspectionImportExport" uniqueID="${uniqueID}"/>

<#macro hiddenFields>
	<@s.hidden name="uniqueID" />
</#macro>


<#include '../importExport/_import.ftl' />