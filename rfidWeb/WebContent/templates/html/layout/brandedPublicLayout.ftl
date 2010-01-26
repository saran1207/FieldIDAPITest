<!DOCTYPE html  PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<#include "/templates/common/nocacheHeaders.ftl"><html>
	<head>
		<#include "publicHead.ftl">
	</head>
	<body>
		
		<div id="page">
			<div id="pageHeader">
				<#if securityGuard?exists>	
					<div id="companyLogo">
						<img width="215" height="61" src="<@s.url action="downloadTenantLogo" namespace="/file" uniqueID="${securityGuard.tenantId!}" />"/>
					</div>
				<#else>
					<div id="systemLogo">
						<img width="215" height="61" src="<@s.url value="/images/FieldIDLogo.jpg"/>"/>
					</div>
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