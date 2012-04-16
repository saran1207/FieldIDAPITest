<!DOCTYPE html  PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<#include "/templates/html/layout/head.ftl"/>
		<@n4.includeScript>
			function promptForLogin() {
				parent.promptForLogin();
			}
			
			function closeLightBox() {
				parent.closeLightbox();
			}
		</@n4.includeScript>
		<style type="text/css">
	    	html, body {
	    		background-color: #FFF;
	    	}
	    </style>
	</head>
	<body>
		<#include "/templates/html/layout/_notificationArea.ftl"/>
		${body}
		
		<#include "/templates/html/layout/googleAnalytics.ftl"/>
	</body>
</html>