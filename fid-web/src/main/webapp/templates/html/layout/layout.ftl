<#include "/templates/common/nocacheHeaders.ftl"><#rt/>
<!DOCTYPE html  PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<#assign head>
			${head}
			<#include "/templates/html/common/_expiredSession.ftl" />
		</#assign>
		<#include "head.ftl"/>
		
	</head>
	<body id="fieldidBody">
		${clickTailStart}
		<div id="systemNotices">
		</div>
		<div id="page">
			<#include "header.ftl"/>
			<#include "contentHeader.ftl"/>
			<div id="pageContent">
				${body}
			</div>
			
			<#include "footer.ftl"/>
		</div>
		
		<#include "googleAnalytics.ftl"/>
		${clickTailEnd}
	</body>
</html>