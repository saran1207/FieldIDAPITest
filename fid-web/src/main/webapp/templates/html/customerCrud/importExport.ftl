${action.setPageType('customer', 'import_export')!}

<head> 
	<@n4.includeStyle type="page" href="downloads" />
</head>

<#assign labelTarget='customer' >
<@s.url id="exportExcel" action="customerExport" namespace="/aHtml" />
<@s.url id="exportExample" action="downloadExampleCustomerExport" namespace="/file" />
<@s.url id="importUrl" action="showImportCustomers"/>

<#include '../importExport/_importExport.ftl' />