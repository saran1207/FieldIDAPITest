${action.setPageType('product_type', 'import_export')!}

<#assign labelTarget='asset' >
<@s.url id="exportExample" action="downloadExampleProductExport" namespace="/file" productTypeId="${productTypeId}"/>
<@s.url id="importUrl" action="showImportProducts" productTypeId="${productTypeId}"/>

<#include '../importExport/_importExport.ftl' />