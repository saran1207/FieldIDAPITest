<title><@s.text name="title.safety_network"/></title>
<head>
	<@n4.includeStyle href="safetyNetwork" type="page"/>
</head>

<#include '_vendorinfo.ftl'>

<div id="mainContent">
<h1><@s.text name="title.pre_assigned_assets"/></h1>

<#assign isSearch=false />
<#include '_list.ftl'>		