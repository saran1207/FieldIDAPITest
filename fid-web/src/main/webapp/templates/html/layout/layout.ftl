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
		<div id="systemNotices">
		</div>
		
		<#include "header.ftl"/>
		<div id="headerPadding" ></div>
		<div id="page">
			<#include "contentHeader.ftl"/>
			<div id="pageContent" class="pageContent">
				${body}
			</div>
			
			<#include "footer.ftl"/>
		</div>
		
		<#include "googleAnalytics.ftl"/>
	</body>
</html>