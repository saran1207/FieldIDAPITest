${action.setPageType('product_type', 'import_export')!}

<#assign labelTarget='product' >
<#assign importAction='importProducts' >
<@s.url id="updateUrl" namespace="/ajax" action="productImportStatus" />
<@s.url id="backToUrl" action="productImportExport" assetTypeId="${assetTypeId}"/>

<#macro hiddenFields>
	<@s.hidden name="assetTypeId" />
</#macro>


<#include '../importExport/_import.ftl' />