<title><@s.text name="title.safety_network"/></title>
<head>
	<@n4.includeStyle href="vendor" type="page"/>
</head>

<#include '_vendorinfo.ftl'>

<div id="mainContent">
<h1><@s.text name="label.register_asset"/></h1>
<#assign isSearch=true />
<#assign currentAction="searchNetworkAsset" />
<#include '_list.ftl'>		