<!DOCTYPE html  PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#include "/templates/common/nocacheHeaders.ftl"><html>
	<head>
		<#include "publicHead.ftl">
	</head>
	<body>
		
		<div id="page">
			<div id="pageHeader">
				<#if securityGuard?exists>	
					<#include "_companyLogo.ftl"/>
				<#else>
					<#include "_systemLogo.ftl"/>
				</#if>
				
				<#include "_notificationArea.ftl"/>
		    </div>
			
			<div id="pageContent" class="brandedContainer public">
				${body}
			</div>
			
			<#include "brandedPublicFooter.ftl">
		</div>
		
		<#include "googleAnalytics.ftl">
	</body>
</html>