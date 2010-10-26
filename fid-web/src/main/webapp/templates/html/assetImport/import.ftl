${action.setPageType('asset_type', 'import_export')!}

<#assign labelTarget='asset' >
<#assign importAction='importAssets' >
<@s.url id="updateUrl" namespace="/ajax" action="assetImportStatus" />
<@s.url id="backToUrl" action="assetImportExport" assetTypeId="${assetTypeId}"/>

<#macro hiddenFields>
	<@s.hidden name="assetTypeId" />
</#macro>


<#include '../importExport/_import.ftl' />