<!DOCTYPE html  PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#include "/templates/common/nocacheHeaders.ftl"><html>
	<head>
		<#include "publicHead.ftl">
	</head>
	<body>
		
		<div id="page">
			<div id="pageHeader">
				<#include "_systemLogo.ftl"/>
				<#include "_notificationArea.ftl"/>
			</div>
						
			<div id="pageContent" class="publicContainer public pageContent">
				${body}
			</div>
			
			<#include "brandedPublicFooter.ftl"/>
		</div>
		
		<#include "googleAnalytics.ftl">
	</body>
</html>