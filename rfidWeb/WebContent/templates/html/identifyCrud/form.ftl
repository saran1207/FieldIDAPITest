${action.setPageType('product', 'add_with_order')!}
<head>
	<<@n4.includeStyle href="pageStyles/identify" />
</head>
<#if Session.sessionUser.hasAccess("tag") >
	<#include "_searchForm.ftl"/>
	<#if orderNumber?exists>
		<#include "_list.ftl"/>
	</#if>
<#else>
	
</#if>