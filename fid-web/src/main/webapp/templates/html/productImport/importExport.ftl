${action.setPageType('product_type', 'import_export')!}

<#assign labelTarget='asset' >
<@s.url id="exportExample" action="downloadExampleProductExport" namespace="/file" assetTypeId="${assetTypeId}"/>
<@s.url id="importUrl" action="showImportProducts" assetTypeId="${assetTypeId}"/>

<#include '../importExport/_importExport.ftl' />