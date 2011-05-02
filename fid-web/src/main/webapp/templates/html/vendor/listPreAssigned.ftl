<title><@s.text name="title.safety_network"/></title>
<head>
	<@n4.includeStyle href="vendor" type="page"/>
</head>

<#include '_vendorinfo.ftl'/>

<div id="mainContent">
<div style="font-size:1.5em;font-weight:bold;display:inline;"><@s.text name="title.pre_assigned_assets"/></div> | <a href="<@s.url action="bulkRegList" uniqueID="${uniqueID}"/>">Bulk Register</a>

<#assign isSearch=false />
<#assign currentAction="preAssignedAssets" />
<#include '_list.ftl'/>
