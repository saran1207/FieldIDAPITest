${action.setPageType('auto_attribute', 'import_export')!}

<#assign labelTarget='autoattrib' >
<#assign importAction='importAutoAttributes' >
<@s.url id="updateUrl" namespace="/ajax" action="autoAttributeImportStatus" />
<@s.url id="backToUrl" action="autoAttributeImportExport" criteriaId="${criteriaId}"/>

<#macro hiddenFields>
	<@s.hidden name="criteriaId" />
</#macro>


<#include '../importExport/_import.ftl' />