<!DOCTYPE html  PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#include "/templates/common/nocacheHeaders.ftl"><html>
	<head>
		<@n4.includeStyle href="reset" /> 
		<@n4.includeStyle href="fieldid"/>
		<@n4.includeStyle href="public"/>
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