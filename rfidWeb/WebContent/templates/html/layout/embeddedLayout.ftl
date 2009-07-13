<!DOCTYPE html  PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#include "/templates/common/nocacheHeaders.ftl"><html>
	<head>
		<link rel="stylesheet" type="text/css" href="<@s.url value="/style/reset.css" />" /> 
		<link rel="stylesheet" type="text/css" href="<@s.url value="/style/fieldid.css"/>" />
		<link rel="stylesheet" type="text/css" href="<@s.url value="/style/public.css"/>"/>
		${head!}
	</head>
	<body>
		
		<div id="page">
			<div id="pageContent">
				${body}
			</div>
			
			<#include "publicFooter.ftl">
		</div>
		
		<#include "googleAnalytics.ftl">
	</body>
</html>