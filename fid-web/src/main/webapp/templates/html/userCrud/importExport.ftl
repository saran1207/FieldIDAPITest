${action.setPageType('user', 'import_export')!}

<head> 
	<@n4.includeStyle type="page" href="downloads" />
</head>

<#assign labelTarget='user' >
<@s.url id="exportExcel" action="userExport" namespace="/aHtml" />
<@s.url id="exportExample" action="downloadExampleUserExport" namespace="/file" />
<@s.url id="importUrl" action="showImportUsers"/>

<#include '../importExport/_importExport.ftl' />