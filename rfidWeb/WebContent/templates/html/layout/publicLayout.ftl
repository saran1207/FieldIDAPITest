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
					<img width="215" height="61" src="<@s.url value="/images/FieldIDLogo.jpg"/>"/>
				</div>
			</div>
			<div id="contentHeader">
				<#include "_notificationArea.ftl"/>
				<h1>${title}</h1>
				<#include "_options.ftl"/>
		    </div>
			
			<div id="pageContent">
				${body}
			</div>
			
			<#include "publicFooter.ftl"/>
		</div>
		
		<#include "googleAnalytics.ftl">
	</body>
</html>