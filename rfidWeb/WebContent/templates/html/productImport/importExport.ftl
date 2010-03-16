${action.setPageType('product_type', 'import_export')!}

<@s.url id="exportExample" action="downloadExampleProductExport" namespace="/file" productTypeId="${productTypeId}"/>
<@s.url id="importUrl" action="showImportProducts" productTypeId="${productTypeId}"/>

<#include '../importExport/_importExport.ftl' />