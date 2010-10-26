${action.setPageType('asset_type', 'import_export')!}

<#assign labelTarget='asset' >
<@s.url id="exportExample" action="downloadExampleAssetExport" namespace="/file" assetTypeId="${assetTypeId}"/>
<@s.url id="importUrl" action="showImportAssets" assetTypeId="${assetTypeId}"/>

<#include '../importExport/_importExport.ftl' />