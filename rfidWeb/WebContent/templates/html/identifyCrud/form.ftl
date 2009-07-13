${action.setPageType('product', 'add_with_order')!}
<head>
	<link rel="stylesheet" type="text/css" href="<@s.url value="/style/pageStyles/identify.css"/>" />
</head>
<#if Session.sessionUser.hasAccess("tag") >
	<#include "_searchForm.ftl"/>
	<#if orderNumber?exists>
		<#include "_list.ftl"/>
	</#if>
<#else>
	
</#if>