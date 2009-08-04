<!DOCTYPE html  PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#include "/templates/common/nocacheHeaders.ftl"><html>
	<head>
		<#include "head.ftl">
		<@n4.includeStyle href="public"/>
	</head>
	<body>
		
		<div id="page">
			<div id="pageHeader">
				<div id="companyLogo">
					<#if securityGuard?exists>
						<img width="215" height="61" src="<@s.url action="downloadTenantLogo" namespace="/file" uniqueID="${securityGuard.tenantId!}" />"/>
					<#else>
						<img width="215" height="61" src="<@s.url value="/images/FieldIDLogo.jpg"/>"/>
					</#if>
				</div>
				
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