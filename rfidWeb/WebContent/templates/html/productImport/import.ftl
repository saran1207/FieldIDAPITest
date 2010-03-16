${action.setPageType('product_type', 'import_export')!}

<#assign importAction='importProducts' >
<@s.url id="updateUrl" namespace="/ajax" action="productImportStatus" />
<@s.url id="backToUrl" action="productImportExport" productTypeId="${productTypeId}"/>

<#macro hiddenFields>
	<@s.hidden name="productTypeId" />
</#macro>


<#include '../importExport/_import.ftl' />